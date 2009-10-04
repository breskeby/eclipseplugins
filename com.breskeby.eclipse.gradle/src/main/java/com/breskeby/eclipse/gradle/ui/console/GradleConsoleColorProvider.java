package com.breskeby.eclipse.gradle.ui.console;

import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.console.ConsoleColorProvider;
import org.eclipse.debug.ui.console.IConsole;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.IOConsoleOutputStream;

import com.breskeby.eclipse.gradle.GradlePlugin;
import com.breskeby.eclipse.gradle.launchConfigurations.GradleProcess;
import com.breskeby.eclipse.gradle.launchConfigurations.GradleStreamsProxy;
import com.breskeby.eclipse.gradle.preferences.IGradlePreferenceConstants;

public class GradleConsoleColorProvider extends ConsoleColorProvider implements IPropertyChangeListener {

 	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.console.IConsoleColorProvider#getColor(java.lang.String)
	 */
	public Color getColor(String streamIdentifer) {
		if (streamIdentifer.equals(IDebugUIConstants.ID_STANDARD_OUTPUT_STREAM)) {
			return GradlePlugin.getPreferenceColor(IGradlePreferenceConstants.CONSOLE_INFO_COLOR);
		}
		if (streamIdentifer.equals(IDebugUIConstants.ID_STANDARD_ERROR_STREAM)) {
			return GradlePlugin.getPreferenceColor(IGradlePreferenceConstants.CONSOLE_ERROR_COLOR);
		}				
		if (streamIdentifer.equals(GradleStreamsProxy.GRADLE_DEBUG_STREAM)) {
			return GradlePlugin.getPreferenceColor(IGradlePreferenceConstants.CONSOLE_DEBUG_COLOR);
		}
		if (streamIdentifer.equals(GradleStreamsProxy.GRADLE_VERBOSE_STREAM)) {
			return GradlePlugin.getPreferenceColor(IGradlePreferenceConstants.CONSOLE_VERBOSE_COLOR);
		}
		if (streamIdentifer.equals(GradleStreamsProxy.GRADLE_WARNING_STREAM)) {
			return GradlePlugin.getPreferenceColor(IGradlePreferenceConstants.CONSOLE_WARNING_COLOR);
		}
		return super.getColor(streamIdentifer);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.console.IConsoleColorProvider#connect(org.eclipse.debug.core.model.IProcess, org.eclipse.debug.ui.console.IConsole)
	 */
	public void connect(IProcess process, IConsole console) {
		//Both remote and local Ant builds are guaranteed to have
		//an AntStreamsProxy. The remote Ant builds make use of the
		// org.eclipse.debug.core.processFactories extension point
		
		GradleStreamsProxy proxy = (GradleStreamsProxy)process.getStreamsProxy();
		if (process instanceof GradleProcess) {
			((GradleProcess)process).setConsole(console);
		}
		if (proxy != null) {
			console.connect(proxy.getDebugStreamMonitor(), GradleStreamsProxy.GRADLE_DEBUG_STREAM);
			console.connect(proxy.getWarningStreamMonitor(), GradleStreamsProxy.GRADLE_WARNING_STREAM);
			console.connect(proxy.getVerboseStreamMonitor(), GradleStreamsProxy.GRADLE_VERBOSE_STREAM);
		}
		
		super.connect(process, console);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.console.IConsoleColorProvider#isReadOnly()
	 */
	public boolean isReadOnly() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
	    final String streamId = getStreamId(event.getProperty());
		if (streamId != null) {
			GradlePlugin.getPlugin().getStandardDisplay().asyncExec(new Runnable() {
				public void run() {
				    IOConsoleOutputStream stream = getConsole().getStream(streamId);
				    if (stream != null) {
				        stream.setColor(getColor(streamId));
				    }
				}
			});
		}
	}

	private String getStreamId(String colorId) {
//		if (IAntUIPreferenceConstants.CONSOLE_DEBUG_COLOR.equals(colorId)) {
//			return AntStreamsProxy.ANT_DEBUG_STREAM;
//		} else if (IAntUIPreferenceConstants.CONSOLE_ERROR_COLOR.equals(colorId)) {
//			return IDebugUIConstants.ID_STANDARD_ERROR_STREAM;
//		} else if (IAntUIPreferenceConstants.CONSOLE_INFO_COLOR.equals(colorId)) {
//			return IDebugUIConstants.ID_STANDARD_OUTPUT_STREAM;
//		} else if (IAntUIPreferenceConstants.CONSOLE_VERBOSE_COLOR.equals(colorId)) {
//			return AntStreamsProxy.ANT_VERBOSE_STREAM;
//		} else if (IAntUIPreferenceConstants.CONSOLE_WARNING_COLOR.equals(colorId)) {
//			return GradleStreamsProxy.GRADLE_WARNING_STREAM;
//		}
		System.out.println("getStreamId " + colorId);
		return GradleStreamsProxy.GRADLE_DEBUG_STREAM;
//		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.console.IConsoleColorProvider#disconnect()
	 */
	public void disconnect() {
//		AntUIPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
		super.disconnect();
	}
}