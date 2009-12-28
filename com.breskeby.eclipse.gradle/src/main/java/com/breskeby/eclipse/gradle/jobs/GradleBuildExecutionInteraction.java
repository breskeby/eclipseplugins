package com.breskeby.eclipse.gradle.jobs;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.gradle.foundation.ipc.gradle.ExecuteGradleCommandServerProtocol;

import com.breskeby.eclipse.gradle.launchConfigurations.GradleProcess;


/**
 * @author breskeby
 * 
 * Default - Implementation of the ExecuteGradleCommandServerProtocol.ExecutionInteraction Interface,
 * this class managed the interaction between the created gradle process and the eclipse IDE.
 * */
public class GradleBuildExecutionInteraction extends GradleProcessExecListener{
	private GradleProcess process = null;
	
	public GradleBuildExecutionInteraction(IProgressMonitor monitor, GradleProcess gradleProcess) {
		super(monitor);
		this.process = gradleProcess;
	}

	
	public void reportExecutionFinished(boolean arg0, String arg1,
			Throwable arg2) {	
		super.reportExecutionFinished(arg0, arg1, arg2);
		process.terminated();
	}

	/**
	 * @see ExecuteGradleCommandServerProtocol.ExecutionInteraction#reportExecutionStarted()
	 * */
	public void reportExecutionStarted() {
		beginTask("Executing Gradle Build", 100);
		worked(10);
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
		worked(10);
	}

	/**
	 * @see ExecuteGradleCommandServerProtocol.ExecutionInteraction#reportTaskStarted(String, float)
	 * */
	public void reportTaskStarted(String arg0, float arg1) {
		subTask("Running Task :" + arg0);
	}


	public void reportNumberOfTasksToExecute(int arg0) {
		// TODO Auto-generated method stub
		
	}
};