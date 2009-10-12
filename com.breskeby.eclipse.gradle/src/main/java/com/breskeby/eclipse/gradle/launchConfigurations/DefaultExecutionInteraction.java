package com.breskeby.eclipse.gradle.launchConfigurations;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.gradle.foundation.ipc.gradle.ExecuteGradleCommandServerProtocol;


/**
 * @author breskeby
 * 
 * Default - Implementation of the ExecuteGradleCommandServerProtocol.ExecutionInteraction Interface,
 * this class managed the interaction between the created gradle process and the eclipse IDE.
 * */
public class DefaultExecutionInteraction implements ExecuteGradleCommandServerProtocol.ExecutionInteraction{

	private final IProgressMonitor monitor;
	private GradleProcess process = null;

	
	public DefaultExecutionInteraction(IProgressMonitor monitor, GradleProcess gradleProcess) {
		this.monitor = monitor;
		this.process = gradleProcess;
	}

	
	public void reportExecutionFinished(boolean arg0, String arg1,
			Throwable arg2) {	
		if(arg2!=null){
			arg2.printStackTrace();
		}
		process.terminated();
		
		monitor.done();
	}

	/**
	 * @see ExecuteGradleCommandServerProtocol.ExecutionInteraction#reportExecutionStarted()
	 * */
	public void reportExecutionStarted() {
		monitor.beginTask("Executing Gradle Build", 10);
	}

	/**
	 * @see ExecuteGradleCommandServerProtocol.ExecutionInteraction#reportLiveOutput(String)
	 * */
	public void reportLiveOutput(String arg0) {
		if(process!=null){
			try {
				process.getStreamsProxy().write(arg0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see ExecuteGradleCommandServerProtocol.ExecutionInteraction#reportTaskComplete(String, float)
	 * */
	public void reportTaskComplete(String arg0, float arg1) {
	}

	/**
	 * @see ExecuteGradleCommandServerProtocol.ExecutionInteraction#reportTaskStarted(String, float)
	 * */
	public void reportTaskStarted(String arg0, float arg1) {
		monitor.worked(10);
	}
};