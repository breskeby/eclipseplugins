package com.breskeby.eclipse.gradle.jobs;

import org.eclipse.core.runtime.IProgressMonitor;


public class GradleRefreshRequestExecutionInteraction extends
		GradleProcessExecListener {

	public GradleRefreshRequestExecutionInteraction(IProgressMonitor monitor) {
		super(monitor);
	}

	public void reportExecutionStarted() {
		beginTask("Calculating Gradle tasks", 10);
	}

	public void reportLiveOutput(String arg0) {
	}

	public void reportTaskComplete(String arg0, float arg1) {
	}

	public void reportTaskStarted(String arg0, float arg1) {
	}
}
