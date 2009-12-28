package com.breskeby.eclipse.gradle.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.gradle.foundation.ipc.gradle.ExecuteGradleCommandServerProtocol.ExecutionInteraction;

public abstract class GradleProcessExecListener implements
		ExecutionInteraction {

	private IProgressMonitor monitor;
	protected boolean finished = false;
	protected Throwable throwable = null;
	
	public boolean isFinished() {
		return finished;
	}

	public GradleProcessExecListener(IProgressMonitor monitor) {
		this.monitor = monitor;
		
	}

	public void reportExecutionFinished(boolean arg0, String arg1,
			Throwable thrown) {
		finished = true;
		throwable = thrown;
		worked(10);
	}

	

	protected void worked(int worked) {
		if(monitor!=null){
			monitor.worked(worked);
		}
	}
	
	protected void beginTask(String task, int totalWork) {
		if(monitor!=null){
			monitor.beginTask(task, totalWork);
		}
	}

	protected void subTask(String task) {
		if(monitor!=null){
			monitor.subTask(task);
		}
	}
	/*
	 * returns a possibly throwable thrown by the gradle process or null if no throwable was thrown by gradle
	 * 
	 * */
	public Throwable getThrowable() {
		// TODO Auto-generated method stub
		return throwable;
	}
}
