package com.breskeby.eclipse.gradle.launchConfigurations;

import org.gradle.foundation.ipc.gradle.ExecuteGradleCommandServerProtocol.ExecutionInteraction;

public class GradleRefreshRequestExecutionInteraction implements
		ExecutionInteraction {

	public void reportExecutionFinished(boolean arg0, String arg1,
			Throwable arg2) {
		System.out.println("GetTasks: execution finished:\n" + arg0 + "\n" + arg1);
	}

	public void reportExecutionStarted() {
		System.out.println("GetTasks: execution started");

	}

	public void reportLiveOutput(String arg0) {
		System.out.println("GetTasks: LiveOutput:\n" + arg0);
	}

	public void reportTaskComplete(String arg0, float arg1) {
		System.out.println("GetTasks: task complete:\n" + arg0);
	}

	public void reportTaskStarted(String arg0, float arg1) {
		System.out.println("GetTasks: task started finished:\n" + arg0 + " " + arg1);
	}

}
