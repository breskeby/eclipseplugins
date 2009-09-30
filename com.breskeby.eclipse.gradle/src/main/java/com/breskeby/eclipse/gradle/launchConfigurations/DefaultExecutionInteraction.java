package com.breskeby.eclipse.gradle.launchConfigurations;

import org.gradle.foundation.ipc.gradle.ExecuteGradleCommandServerProtocol;

public class DefaultExecutionInteraction implements ExecuteGradleCommandServerProtocol.ExecutionInteraction{

	@Override
	public void reportExecutionFinished(boolean arg0, String arg1,
			Throwable arg2) {
		// TODO Auto-generated method stub
		System.out.println("GRADLE FINISHED " + arg1 + "   --   " + arg2);
		if(arg2!=null){
			arg2.printStackTrace();
		}
	}

	@Override
	public void reportExecutionStarted() {
		// TODO Auto-generated method stub

		System.out.println("GRADLE STARTED");
	}

	@Override
	public void reportLiveOutput(String arg0) {
		// TODO Auto-generated method stub

		System.out.println("GRADLE LIVEOUTPUT : " + arg0);
	}

	@Override
	public void reportTaskComplete(String arg0, float arg1) {
		// TODO Auto-generated method stub

		System.out.println("GRADLE TASK complete: " + arg0 + "  ---  " + arg1);				
	}

	@Override
	public void reportTaskStarted(String arg0, float arg1) {
		System.out.println("GRADLE TASK started: " + arg0 +"----" + arg1);				
	}
	
};