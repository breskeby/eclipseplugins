package com.breskeby.eclipse.gradle;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.gradle.foundation.ProjectView;
import org.gradle.foundation.ipc.gradle.ExecuteGradleCommandServerProtocol;
import org.gradle.gradleplugin.foundation.GradlePluginLord;
import org.gradle.gradleplugin.foundation.GradlePluginLord.GeneralPluginObserver;
import org.gradle.gradleplugin.foundation.request.RefreshTaskListRequest;

import com.breskeby.eclipse.gradle.launchConfigurations.GradleRefreshRequestExecutionInteraction;

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

	public void refreshTaskView(final IFile buildFile) {
		System.out.println("start refreshTaskView");
		final GradlePluginLord gradlePluginLord = new GradlePluginLord();
		gradlePluginLord.setGradleHomeDirectory(new File(GradlePlugin.getPlugin().getDefaultGradleHome()));
		final File absoluteDirectory = new File(buildFile.getLocationURI()).getParentFile();
		
		System.out.println("workdir " + absoluteDirectory.toString());
		gradlePluginLord.setCurrentDirectory(absoluteDirectory);
		ExecuteGradleCommandServerProtocol.ExecutionInteraction executionlistener = new GradleRefreshRequestExecutionInteraction();
		
		gradlePluginLord.addGeneralPluginObserver(new GeneralPluginObserver() {
			
			public void startingProjectsAndTasksReload() {
			}
			
			public void projectsAndTasksReloaded(boolean arg0) {
				List<ProjectView> projects = gradlePluginLord.getProjects();
				if(!projects.isEmpty()){
					System.out.println("projectsAndTasksReloaded: " + projects.get(0).getName());
				}
				final String generateVariableExpression = 
					VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression("workspace_loc", buildFile.toString()); //$NON-NLS-1$
				
				GradleExecScheduler.this.buildFileInformationCache.put(absoluteDirectory.toString(), projects);
			}
		}, true);
		
		gradlePluginLord.startExecutionQueue();
		RefreshTaskListRequest refreshTaskListRequest = (RefreshTaskListRequest) gradlePluginLord.addRefreshRequestToQueue(executionlistener);
	}
	
	public List<ProjectView> getProjectViews(String buildFile){
//		if(buildFileInformationCache.get(buildFile)==null){
//			refreshTaskView(buildFile);
//		}
		
		return buildFileInformationCache.get(buildFile);
	}
}
