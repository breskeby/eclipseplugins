package com.breskeby.eclipse.gradle.launchConfigurations;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.ui.externaltools.internal.model.IExternalToolConstants;
import org.gradle.foundation.ipc.gradle.ExecuteGradleCommandServerProtocol;
import org.gradle.gradleplugin.foundation.GradlePluginLord;

import com.breskeby.eclipse.gradle.GradleExecScheduler;
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
		monitor.beginTask("Invoking Gradle", 100);
//		Long start = System.currentTimeMillis();
		monitor.worked(5);
		GradleExecScheduler.getInstance().startGradleBuildRun(configuration, commandLine, getProcess());
		monitor.done();
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
