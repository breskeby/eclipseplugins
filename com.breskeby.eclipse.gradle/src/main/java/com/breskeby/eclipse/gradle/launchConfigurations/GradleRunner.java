package com.breskeby.eclipse.gradle.launchConfigurations;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;

import com.breskeby.eclipse.gradle.GradleExecScheduler;

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
