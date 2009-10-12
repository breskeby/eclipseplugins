package com.breskeby.eclipse.gradle.launchConfigurations;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.ui.externaltools.internal.model.IExternalToolConstants;
import org.gradle.foundation.ipc.gradle.ExecuteGradleCommandServerProtocol;
import org.gradle.gradleplugin.foundation.GradlePluginLord;

import com.breskeby.eclipse.gradle.GradlePlugin;

@SuppressWarnings("restriction")
public class GradleRunner {

	private ILaunchConfiguration configuration;
	private String commandLine;
	private ILaunch launch;

	public GradleRunner(ILaunchConfiguration configuration, ILaunch launch, String commandLine) throws CoreException {
		this.launch = launch;
		this.configuration = configuration;
		this.commandLine = commandLine;
	}
	
	public void run(IProgressMonitor monitor) throws CoreException{
//		Long start = System.currentTimeMillis();
		GradlePluginLord gradlePluginLord = new GradlePluginLord();
//		gradlePluginLord.setLogLevel(org.gradle.api.logging.LogLevel.DEBUG);
		gradlePluginLord.setGradleHomeDirectory(new File(GradlePlugin.getPlugin().getDefaultGradleHome()));
		//get build file location
		String buildfilePath = configuration.getAttribute(IExternalToolConstants.ATTR_LOCATION, "");
		
		File buildPath = new File(VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(buildfilePath)).getParentFile();
		gradlePluginLord.setCurrentDirectory(buildPath);
		ExecuteGradleCommandServerProtocol.ExecutionInteraction executionlistener = new DefaultExecutionInteraction(monitor, getProcess());
		
		gradlePluginLord.startExecutionQueue();
//		Long end = System.currentTimeMillis();
		gradlePluginLord.addExecutionRequestToQueue(commandLine, executionlistener);
	}

	private GradleProcess getProcess() {
		IProcess[] processes = launch.getProcesses();
		for(IProcess process : processes){
			if(process instanceof GradleProcess){
				return ((GradleProcess)process);
			}
		}
		return null;
	}
}
