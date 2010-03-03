package com.breskeby.eclipse.gradle;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.ui.externaltools.internal.model.IExternalToolConstants;
import org.gradle.foundation.ProjectView;
import org.gradle.gradleplugin.foundation.GradlePluginLord;
import org.gradle.gradleplugin.foundation.request.ExecutionRequest;
import org.gradle.gradleplugin.foundation.request.RefreshTaskListRequest;
import org.gradle.gradleplugin.foundation.request.Request;

import com.breskeby.eclipse.gradle.jobs.GradleBuildExecutionInteraction;
import com.breskeby.eclipse.gradle.jobs.GradleProcessExecListener;
import com.breskeby.eclipse.gradle.jobs.GradleRefreshRequestExecutionInteraction;
import com.breskeby.eclipse.gradle.launchConfigurations.GradleProcess;

public class GradleExecScheduler {

	private static GradleExecScheduler instance = null;

	//caches buildfile meta informations
	private Map<String, List<ProjectView>> buildFileInformationCache 
			= new HashMap<String, List<ProjectView>>();
	
	public static GradleExecScheduler getInstance() {
		if(instance==null){
			instance = new GradleExecScheduler();
		}
		return instance;
	}
	
	private GradleExecScheduler(){
	}
	
	private IStatus runGradleTaskRefreshProcess(final String absolutePath, IProgressMonitor monitor){
//		System.currentTimeMillis()
		final GradlePluginLord gradlePluginLord = new GradlePluginLord();
		gradlePluginLord.setGradleHomeDirectory(new File(GradlePlugin.getPlugin().getGradleHome()));
		final File absoluteDirectory = new File(absolutePath).getParentFile();
		
		gradlePluginLord.setCurrentDirectory(absoluteDirectory);
		final GradleProcessExecListener executionlistener = new GradleRefreshRequestExecutionInteraction(monitor);
		
		gradlePluginLord.startExecutionQueue();
		final BooleanHolder isComplete = new BooleanHolder();

		GradlePluginLord.RequestObserver observer = new GradlePluginLord.RequestObserver() {
	           
			public void executionRequestAdded( ExecutionRequest request )
	           {
	              request.setExecutionInteraction( executionlistener );
	           }
	           public void refreshRequestAdded( RefreshTaskListRequest request ) { 
	           }
	           public void aboutToExecuteRequest( Request request ) { 
	           }

	           public void requestExecutionComplete( Request request, int result, String output ) {
	        	   isComplete.value = true;
	           }
	        };

	    gradlePluginLord.addRequestObserver(observer, false);
		gradlePluginLord.addRefreshRequestToQueue();
		
		//keep job open til listener reports gradle has finished
		while(!isComplete.value){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				return new Status(IStatus.WARNING, GradlePlugin.PLUGIN_ID, "Error while recalculating Gradle Tasks", e);
			}
		}
		List<ProjectView> projects = gradlePluginLord.getProjects();
		GradleExecScheduler.this.buildFileInformationCache.put(absolutePath, projects);
		if(executionlistener.getThrowable()!=null){
			return new Status(IStatus.WARNING, GradlePlugin.PLUGIN_ID, "Error while recalculating Gradle Tasks");
		}
		return Status.OK_STATUS;
	}

	public void refreshTaskView(final String absolutePath, boolean synched) {
		
		// create gradle job
		if(!synched){
			Job job = new Job("Calculating Gradle Tasks...") {
				protected IStatus run(IProgressMonitor monitor) {		
					return runGradleTaskRefreshProcess(absolutePath, monitor);
				}
			};
			job.setUser(false);
			job.setPriority(Job.LONG);
			job.schedule(); // start as soon as possible
		}else{
			runGradleTaskRefreshProcess(absolutePath, null);
		}
	}
	
	
	public List<ProjectView> getProjectViews(IFile buildFile){
		String absolutePath = new File(buildFile.getFullPath().toString()).getAbsolutePath();	
		return getProjectViews(absolutePath);
	}
	
	public List<ProjectView> getProjectViews(String absolutePath) {
		if(buildFileInformationCache.get(absolutePath)==null){
			refreshTaskView(absolutePath, true);
		}
		return buildFileInformationCache.get(absolutePath);
	}

	public void startGradleBuildRun(final ILaunchConfiguration configuration, final String commandLine, final GradleProcess gradleProcess) throws CoreException{
		final GradlePluginLord gradlePluginLord = new GradlePluginLord();

		//TODO handle debug
		//		gradlePluginLord.setLogLevel(org.gradle.api.logging.LogLevel.DEBUG);

		
		gradlePluginLord.setGradleHomeDirectory(new File(GradlePlugin.getPlugin().getGradleHome()));
		//get build file location
		String buildfilePath = configuration.getAttribute(IExternalToolConstants.ATTR_LOCATION, "");
		
		File buildPath = new File(VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(buildfilePath)).getParentFile();
		gradlePluginLord.setCurrentDirectory(buildPath);
		
		// create gradle job
		Job job = new Job("Running Gradle Build...") {
			protected IStatus run(IProgressMonitor monitor) {
				
				final GradleProcessExecListener executionlistener = new GradleBuildExecutionInteraction(monitor, gradleProcess);
				gradlePluginLord.startExecutionQueue();
				
				final BooleanHolder isComplete = new BooleanHolder();
				///
				GradlePluginLord.RequestObserver observer = new GradlePluginLord.RequestObserver() {
			           public void executionRequestAdded( ExecutionRequest request )
			           {
			              request.setExecutionInteraction( executionlistener );
			           }
			           public void refreshRequestAdded( RefreshTaskListRequest request ) { 
			        	   
			           }
			           public void aboutToExecuteRequest( Request request ) { 
			        	   
			           }

			           public void requestExecutionComplete( Request request, int result, String output ) {
			        	   isComplete.value = true;
			           }
			        };

				///########
			    //add the observer before we add the request due to timing issues. 
				//It's possible for it to completely execute before we return from addExecutionRequestToQueue.
				    
			    gradlePluginLord.addRequestObserver( observer, false );   
			    Request request = gradlePluginLord.addExecutionRequestToQueue( commandLine, configuration.getName() );

				//gradlePluginLord.addExecutionRequestToQueue(commandLine, executionlistener);
				//keep job open til listener reports gradle has finished
			    while(!isComplete.value){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						return new Status(IStatus.WARNING, GradlePlugin.PLUGIN_ID, "Error while running Gradle Tasks", e);
					}
				}
				
				if(executionlistener.getThrowable()!=null){
					return new Status(IStatus.WARNING, GradlePlugin.PLUGIN_ID, "Error while running Gradle Tasks", executionlistener.getThrowable());
				}
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.setPriority(Job.LONG);
		job.schedule(); // start as soon as possible
	}
	
	private class BooleanHolder {
        private boolean value = false;
    }
}
