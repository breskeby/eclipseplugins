package com.breskeby.eclipse.gradle.launchConfigurations;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.debug.ui.console.IConsole;

public class GradleProcess extends PlatformObject implements IProcess, IProgressMonitor {
	private GradleStreamsProxy fProxy;
	private String fLabel = null;
	private ILaunch fLaunch = null;
	
	@SuppressWarnings("unchecked")
	private Map fAttributes = null;
	private boolean fTerminated = false;
	private boolean fCancelled = false;
	private IConsole fConsole = null;
	
	@SuppressWarnings("unchecked")
	public GradleProcess(String label, ILaunch launch, Map attributes) {
		fLabel = label;
		fLaunch = launch;
		fAttributes = attributes;
		fProxy= new GradleStreamsProxy();
		launch.addProcess(this);
	}

	/**
	 * @see org.eclipse.debug.core.model.IProcess#getLabel()
	 */
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
	 * @see org.eclipse.debug.core.model.IProcess#setAttribute(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public void setAttribute(String key, String value) {
		fAttributes.put(key, value);
	}

	/**
	 * @see org.eclipse.debug.core.model.IProcess#getAttribute(java.lang.String)
	 */
	public String getAttribute(String key) {
		return (String)fAttributes.get(key);
	}

	/**
	 * @see org.eclipse.debug.core.model.IProcess#getExitValue()
	 */
	public int getExitValue() {
		return 0;
	}

	/**
	 * @see org.eclipse.debug.core.model.ITerminate#canTerminate()
	 */
	public boolean canTerminate() {
		System.out.println("canTerminate " + isCanceled() + " : " + isTerminated());
		return !isCanceled() && !isTerminated();
	}

	/**
	 * @see org.eclipse.debug.core.model.ITerminate#isTerminated()
	 */
	public boolean isTerminated() {
		return fTerminated;
	}
	
	public void terminated() {
		if (!fTerminated) {
			fTerminated = true;
			if (DebugPlugin.getDefault() != null) {
				DebugPlugin.getDefault().fireDebugEventSet(new DebugEvent[] {new DebugEvent(this, DebugEvent.TERMINATE)});
			}
		}
	}

	/**
	 * @see org.eclipse.debug.core.model.ITerminate#terminate()
	 */
	public void terminate() {
		setCanceled(true);
	}

	/**
	 * Returns the console associated with this process, or <code>null</code> if
	 * none.
	 * 
	 * @return console, or <code>null</code>
	 */
	public IConsole getConsole() {
		return fConsole;
	}
	
	/**
	 * Sets the console associated with this process.
	 * 
	 * @param console
	 */
	public void setConsole(IConsole console) {
		fConsole = console;
	}
	
	// IProgressMonitor implemented to support termination.
	
	/**
	 * @see org.eclipse.core.runtime.IProgressMonitor#beginTask(java.lang.String, int)
	 */
	public void beginTask(String name, int totalWork) {
	}

	/**
	 * @see org.eclipse.core.runtime.IProgressMonitor#done()
	 */
	public void done() {
	}

	/**
	 * @see org.eclipse.core.runtime.IProgressMonitor#internalWorked(double)
	 */
	public void internalWorked(double work) {
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

	/**
	 * @see org.eclipse.core.runtime.IProgressMonitor#setTaskName(java.lang.String)
	 */
	public void setTaskName(String name) {
	}

	/**
	 * @see org.eclipse.core.runtime.IProgressMonitor#subTask(java.lang.String)
	 */
	public void subTask(String name) {
	}

	/**
	 * @see org.eclipse.core.runtime.IProgressMonitor#worked(int)
	 */
	public void worked(int work) {
	}

	public IStreamsProxy getStreamsProxy() {
		return fProxy;
	}
}
