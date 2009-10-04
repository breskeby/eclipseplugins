package com.breskeby.eclipse.gradle.launchConfigurations;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IFlushableStreamMonitor;

public class GradleStreamMonitor implements IFlushableStreamMonitor {

	private StringBuffer fContents = new StringBuffer();
	private ListenerList fListeners = new ListenerList(1);
	private boolean fBuffered = true;
	
	/**
	 * @see org.eclipse.debug.core.model.IStreamMonitor#addListener(org.eclipse.debug.core.IStreamListener)
	 */
	public void addListener(IStreamListener listener) {
		fListeners.add(listener);
	}

	/**
	 * @see org.eclipse.debug.core.model.IStreamMonitor#getContents()
	 */
	public String getContents() {
		return fContents.toString();
	}

	/**
	 * @see org.eclipse.debug.core.model.IStreamMonitor#removeListener(org.eclipse.debug.core.IStreamListener)
	 */
	public void removeListener(IStreamListener listener) {
		fListeners.remove(listener);
	}

	/**
	 * Appends the given message to this stream, and notifies listeners.
	 * 
	 * @param message
	 */
	public void append(String message) {
		if (isBuffered()) {
			fContents.append(message);
		}
		Object[] listeners = fListeners.getListeners();
		for (int i = 0; i < listeners.length; i++) {
			IStreamListener listener = (IStreamListener)listeners[i];
			listener.streamAppended(message, this);
		}
	}
	/**
	 * @see org.eclipse.debug.core.model.IFlushableStreamMonitor#flushContents()
	 */
	public void flushContents() {
		fContents.setLength(0);
	}

	/**
	 * @see org.eclipse.debug.core.model.IFlushableStreamMonitor#isBuffered()
	 */
	public boolean isBuffered() {
		return fBuffered;
	}

	/**
	 * @see org.eclipse.debug.core.model.IFlushableStreamMonitor#setBuffered(boolean)
	 */
	public void setBuffered(boolean buffer) {
		fBuffered = buffer;
	}
}