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
import org.gradle.gradleplugin.foundation.GradlePluginLord.GeneralPluginObserver;

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
	
	private IStatus runGradleProcess(final String absolutePath, IProgressMonitor monitor){
		final GradlePluginLord gradlePluginLord = new GradlePluginLord();
		gradlePluginLord.setGradleHomeDirectory(new File(GradlePlugin.getPlugin().getDefaultGradleHome()));
		final File absoluteDirectory = new File(absolutePath).getParentFile();
		
		gradlePluginLord.setCurrentDirectory(absoluteDirectory);
		GradleProcessExecListener executionlistener = new GradleRefreshRequestExecutionInteraction(monitor);
		
//		gradlePluginLord.addGeneralPluginObserver(new GeneralPluginObserver() {
//			
//			public void startingProjectsAndTasksReload() {
////				GeneralPluginObserver.this.
//			}
//			
//			public void projectsAndTasksReloaded(boolean arg0) {
//				
//			}
//		}, true);
		gradlePluginLord.startExecutionQueue();
		gradlePluginLord.addRefreshRequestToQueue(executionlistener);
		
		//keep job open til listener reports gradle has finished
		while(!executionlistener.isFinished()){
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
					return runGradleProcess(absolutePath, monitor);
				}
			};
			job.setUser(false);
			job.setPriority(Job.LONG);
			job.schedule(); // start as soon as possible
		}else{
			runGradleProcess(absolutePath, null);
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

	public void startGradleBuildRun(ILaunchConfiguration configuration, final String commandLine, final GradleProcess gradleProcess) throws CoreException{
		final GradlePluginLord gradlePluginLord = new GradlePluginLord();

		//TODO handle debug
		//		gradlePluginLord.setLogLevel(org.gradle.api.logging.LogLevel.DEBUG);

		
		gradlePluginLord.setGradleHomeDirectory(new File(GradlePlugin.getPlugin().getDefaultGradleHome()));
		//get build file location
		String buildfilePath = configuration.getAttribute(IExternalToolConstants.ATTR_LOCATION, "");
		
		File buildPath = new File(VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(buildfilePath)).getParentFile();
		gradlePluginLord.setCurrentDirectory(buildPath);
		
		// create gradle job
		Job job = new Job("Running Gradle Build...") {
			protected IStatus run(IProgressMonitor monitor) {
				
				GradleProcessExecListener executionlistener = new GradleBuildExecutionInteraction(monitor, gradleProcess);
				gradlePluginLord.startExecutionQueue();
				
				gradlePluginLord.addExecutionRequestToQueue(commandLine, executionlistener);
				//keep job open til listener reports gradle has finished
				while(!executionlistener.isFinished()){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						return new Status(IStatus.WARNING, GradlePlugin.PLUGIN_ID, "Error while recalculating Gradle Tasks", e);
					}
				}
				
				if(executionlistener.getThrowable()!=null){
					return new Status(IStatus.WARNING, GradlePlugin.PLUGIN_ID, "Error while recalculating Gradle Tasks", executionlistener.getThrowable());
				}
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.setPriority(Job.LONG);
		job.schedule(); // start as soon as possible
	}
}
