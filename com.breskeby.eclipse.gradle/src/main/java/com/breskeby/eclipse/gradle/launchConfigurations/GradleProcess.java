package com.breskeby.eclipse.gradle.launchConfigurations;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ant.internal.ui.launchConfigurations.AntStreamsProxy;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.debug.ui.console.IConsole;

public class GradleProcess extends PlatformObject implements IProcess, IProgressMonitor {

//	private AntStreamsProxy fProxy;
	private String fLabel = null;
	private ILaunch fLaunch = null;
	private Map fAttributes = null;
	private boolean fTerminated = false;
	private boolean fCancelled = false;
	private IConsole fConsole = null;
	
	public GradleProcess(String label, ILaunch launch, Map attributes) {
		fLabel = label;
		fLaunch = launch;
		if (attributes == null) {
			fAttributes = new HashMap();
		} else {
			fAttributes = attributes;
		}
		String captureOutput= launch.getAttribute(DebugPlugin.ATTR_CAPTURE_OUTPUT);
		System.out.println("captureoutput " + captureOutput);
		if(!("false".equals(captureOutput))) { //$NON-NLS-1$
//			fProxy= new AntStreamsProxy();
		}
		launch.addProcess(this);
	}
	

	@Override
	public String getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getExitValue() throws DebugException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLabel() {
		return fLabel;
	}

	/**
	 * @see org.eclipse.debug.core.model.IProcess#getLaunch()
	 */
	public ILaunch getLaunch() {
		return fLaunch;
	}

	/**
	 * @see org.eclipse.debug.core.model.IProcess#getStreamsProxy()
	 */
	public IStreamsProxy getStreamsProxy() {
		return null;
	}

	/**
	 * @see org.eclipse.debug.core.model.IProcess#setAttribute(java.lang.String, java.lang.String)
	 */
	public void setAttribute(String key, String value) {
		fAttributes.put(key, value);
	}

	/**
	 * @see org.eclipse.debug.core.model.ITerminate#isTerminated()
	 */
	public boolean isTerminated() {
		return fTerminated;
	}
	
	protected void terminated() {
		if (!fTerminated) {
			fTerminated = true;
			if (DebugPlugin.getDefault() != null) {
				DebugPlugin.getDefault().fireDebugEventSet(new DebugEvent[] {new DebugEvent(this, DebugEvent.TERMINATE)});
			}
		}
	}
	
	/**
	 * @see org.eclipse.debug.core.model.ITerminate#canTerminate()
	 */
	public boolean canTerminate() {
		return !isCanceled() && !isTerminated();
	}

	@Override
	public void terminate() throws DebugException {
		System.out.println("terminate");
		
	}

	@Override
	public void beginTask(String arg0, int arg1) {

		System.out.println("beginTask");
	}

	@Override
	public void done() {
		System.out.println("done");
	}

	@Override
	public void internalWorked(double arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see org.eclipse.core.runtime.IProgressMonitor#isCanceled()
	 */
	public boolean isCanceled() {
		return fCancelled;
	}

	/**
	 * @see org.eclipse.core.runtime.IProgressMonitor#setCanceled(boolean)
	 */
	public void setCanceled(boolean value) {
		fCancelled = value;
	}


	@Override
	public void setTaskName(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subTask(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void worked(int arg0) {
		// TODO Auto-generated method stub
		System.out.println("worked");
		
	}

}
