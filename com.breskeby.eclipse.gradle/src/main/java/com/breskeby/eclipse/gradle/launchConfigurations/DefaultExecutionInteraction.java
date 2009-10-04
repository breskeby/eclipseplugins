package com.breskeby.eclipse.gradle.launchConfigurations;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IProcess;
import org.gradle.foundation.ipc.gradle.ExecuteGradleCommandServerProtocol;

public class DefaultExecutionInteraction implements ExecuteGradleCommandServerProtocol.ExecutionInteraction{

	private final IProgressMonitor monitor;
	private GradleProcess process = null;

	
	public DefaultExecutionInteraction(IProgressMonitor monitor, GradleProcess gradleProcess) {
		this.monitor = monitor;
		this.process = gradleProcess;
	}

	public void reportExecutionFinished(boolean arg0, String arg1,
			Throwable arg2) {
		// TODO Auto-generated method stub
		if(arg2!=null){
			arg2.printStackTrace();
		}
		try {
			process.getStreamsProxy().write("Gradle Process finished & terminated");
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		process.terminated();
		
		monitor.done();
	}

	public void reportExecutionStarted() {
		monitor.beginTask("Executing Gradle Build", 10);
	}

	public void reportLiveOutput(String arg0) {
		// TODO Auto-generated method stub
		if(process!=null){
			try {
				process.getStreamsProxy().write(arg0);
			} catch (IOException e) {
			}
		}
	}

	public void reportTaskComplete(String arg0, float arg1) {
		// TODO Auto-generated method stub
//		monitor.
		System.out.println("GRADLE TASK complete: " + arg0 + "  ---  " + arg1);				
	}

	public void reportTaskStarted(String arg0, float arg1) {
		monitor.worked(10);
		System.out.println("GRADLE TASK started: " + arg0 +"----" + arg1);				
	}
};