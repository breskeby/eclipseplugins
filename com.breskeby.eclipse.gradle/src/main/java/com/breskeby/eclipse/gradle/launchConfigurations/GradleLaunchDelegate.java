package com.breskeby.eclipse.gradle.launchConfigurations;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.debug.ui.CommonTab;
import org.gradle.foundation.ipc.gradle.ExecuteGradleCommandServerProtocol;
import org.gradle.gradleplugin.foundation.GradlePluginLord;

import com.breskeby.eclipse.gradle.GradlePlugin;
import com.ibm.icu.text.MessageFormat;

public class GradleLaunchDelegate extends LaunchConfigurationDelegate  {

	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		System.out.println("GradleDefaultHOME: " + GradlePlugin.getPlugin().getDefaultGradleHome());
		
		
		if (monitor.isCanceled()) {
			return;
		}
		if (monitor.isCanceled()) {
			return;
		}
		
		if (CommonTab.isLaunchInBackground(configuration)) {
			monitor.beginTask(MessageFormat.format(GradleLaunchConfigurationMessages.GradleLaunchDelegate_Launching__0__1, new String[] {configuration.getName()}), 10);
		} else {
			monitor.beginTask(MessageFormat.format(GradleLaunchConfigurationMessages.GradleLaunchDelegate_Running__0__2, new String[] {configuration.getName()}), 100);
		}
		if (monitor.isCanceled()) {
			return;
		}

		monitor.worked(1);
		
		runInSeparateVM(configuration, launch, monitor, "idStamp", GradlePlugin.getPlugin().getDefaultGradleHome(), 2233, 2234, new StringBuffer("gradle -t"), false, false);
		monitor.worked(60);
		if (monitor.isCanceled()) {
			return;
		}
		monitor.done();
	}
	
	private void runInSeparateVM(ILaunchConfiguration configuration, ILaunch launch, IProgressMonitor monitor, String idStamp, String gradleHome, int port, int requestPort, StringBuffer commandLine, boolean captureOutput, boolean setInputHandler) throws CoreException {
		GradlePluginLord gradlePluginLord = new GradlePluginLord();
		gradlePluginLord.setLogLevel(org.gradle.api.logging.LogLevel.DEBUG);
		gradlePluginLord.setGradleHomeDirectory(new File(GradlePlugin.getPlugin().getDefaultGradleHome()));
		gradlePluginLord.setCurrentDirectory(new File("/Users/Rene/workspaces/github/runtime-com.breskeby.eclipse.gradle.gradleRunner/TestProjekt"));
		ExecuteGradleCommandServerProtocol.ExecutionInteraction executionlistener = new DefaultExecutionInteraction();
		gradlePluginLord.startExecutionQueue();
		gradlePluginLord.addExecutionRequestToQueue(commandLine.toString(), executionlistener);
	}
}