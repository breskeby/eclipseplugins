package com.breskeby.eclipse.gradle.launchConfigurations;

import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.IStreamsProxy;

import com.breskeby.eclipse.gradle.GradlePlugin;

public class GradleStreamsProxy implements IStreamsProxy {
	
	private GradleStreamMonitor fErrorMonitor = new GradleStreamMonitor();
	private GradleStreamMonitor fOutputMonitor = new GradleStreamMonitor();
	
	public static final String GRADLE_DEBUG_STREAM = GradlePlugin.PLUGIN_ID + ".GRADLE_DEBUG_STREAM"; //$NON-NLS-1$
	public static final String GRADLE_VERBOSE_STREAM = GradlePlugin.PLUGIN_ID + ".GRADLE_VERBOSE_STREAM"; //$NON-NLS-1$
	public static final String GRADLE_WARNING_STREAM = GradlePlugin.PLUGIN_ID  + ".GRADLE_WARNING_STREAM"; //$NON-NLS-1$
	
	private GradleStreamMonitor fDebugMonitor = new GradleStreamMonitor();
	private GradleStreamMonitor fVerboseMonitor = new GradleStreamMonitor();
	private GradleStreamMonitor fWarningMonitor = new GradleStreamMonitor();

	/**
	 * @see org.eclipse.debug.core.model.IStreamsProxy#getErrorStreamMonitor()
	 */
	public IStreamMonitor getErrorStreamMonitor() {
		return fErrorMonitor;
	}

	/**
	 * @see org.eclipse.debug.core.model.IStreamsProxy#getOutputStreamMonitor()
	 */
	public IStreamMonitor getOutputStreamMonitor() {
		System.out.println("IStreamMonitor getoutput");
		return fOutputMonitor;
	}

	/**
	 * @see org.eclipse.debug.core.model.IStreamsProxy#write(java.lang.String)
	 */
	public void write(String input) {
		//change this: @FIXME use more sophisticated approach
		String[] split = input.split("\n");
		for(String line : split){
			GradleStreamMonitor monitor = getMonitorByLineParsing(line);
			monitor.append(line);
			monitor.append("\n");
		}
	}

	private GradleStreamMonitor getMonitorByLineParsing(String line) {
		if(line.contains(" [main] DEBUG ")){
			return fDebugMonitor;
		}else if(line.contains("[main] WARN  ")){
			return fWarningMonitor;
		}else if(line.contains("[main] INFO  ")){
			return fWarningMonitor;
		}else if(line.contains("[main] ERROR ")){
			return fErrorMonitor;
		}else if(line.contains("[main] ERROR ")){
			return fErrorMonitor;
		}
		
		return fOutputMonitor;
		
	}

	public IStreamMonitor getWarningStreamMonitor() {
		return fWarningMonitor;
	}
	
	public IStreamMonitor getDebugStreamMonitor() {
		System.out.println("IStreamMonitor debug ");
		
		return fDebugMonitor;
	}	
	
	public IStreamMonitor getVerboseStreamMonitor() {
		return fVerboseMonitor;
	}	
}
