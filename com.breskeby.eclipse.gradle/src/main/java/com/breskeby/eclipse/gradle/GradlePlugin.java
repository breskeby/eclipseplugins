package com.breskeby.eclipse.gradle;

import java.util.Locale;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class GradlePlugin extends AbstractUIPlugin {

	private static final String EMPTY_STRING= ""; //$NON-NLS-1$
	
	/**
	 * Status code indicating an unexpected internal error.
	 * @since 2.1
	 */
	public static final int INTERNAL_ERROR = 120;		

	// The plug-in ID
	public static final String PLUGIN_ID = "com.breskeby.eclipse.gradle";

	public static final int ERROR_LIBRARY_NOT_SPECIFIED = 0;

	public static final int ERROR_MALFORMED_URL = 0;

	/**
	 * Simple identifier constant (value <code>"headless"</code>) of a tag
	 */
	public static final String HEADLESS = "headless"; //$NON-NLS-1$
	
	// The shared instance
	private static GradlePlugin plugin;

	/**
	 * Returns the standard display to be used. The method first checks, if
	 * the thread calling this method has an associated display. If so, this
	 * display is returned. Otherwise the method returns the default display.
	 */
	public static Display getStandardDisplay() {
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}
	
	/**
	 * The constructor
	 */
	public GradlePlugin() {
	}

	/**
	 * Returns this plug-in instance.
	 *
	 * @return the single instance of this plug-in runtime class
	 */
	public static GradlePlugin getPlugin() {
		return plugin;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static GradlePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * Returns whether the current OS claims to be Mac
	 */
	public static boolean isMacOS() {
		String osname = System.getProperty("os.name").toLowerCase(Locale.US); //$NON-NLS-1$
		return osname.indexOf("mac") != -1; //$NON-NLS-1$
	}
	
	/**
	 * Logs the specified throwable with this plug-in's log.
	 * 
	 * @param t throwable to log 
	 */
	public static void log(Throwable t) {
		IStatus status= new Status(IStatus.ERROR, PLUGIN_ID, INTERNAL_ERROR, "Error logged from Ant UI: ", t); //$NON-NLS-1$
		log(status);
	}
	
	/**
	 * Logs the specified status with this plug-in's log.
	 * 
	 * @param status status 
	 */
	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}
	
	/**
	 * Writes the message to the plug-in's log
	 * 
	 * @param message the text to write to the log
	 */
	public static void log(String message, Throwable exception) {
		IStatus status = newErrorStatus(message, exception);
		log(status);
	}
	
	/**
	 * Returns a new <code>IStatus</code> for this plug-in
	 */
	public static IStatus newErrorStatus(String message, Throwable exception) {
		if (message == null) {
			message= EMPTY_STRING; 
		}		
		return new Status(IStatus.ERROR, PLUGIN_ID, 0, message, exception);
	}
	
	/**
	* Returns the active workbench page or <code>null</code> if none.
	*/
   public static IWorkbenchPage getActivePage() {
	   IWorkbenchWindow window= getActiveWorkbenchWindow();
	   if (window != null) {
		   return window.getActivePage();
	   }
	   return null;
   }

   /**
	* Returns the active workbench window or <code>null</code> if none
	*/
   public static IWorkbenchWindow getActiveWorkbenchWindow() {
	   return getDefault().getWorkbench().getActiveWorkbenchWindow();
   }
}
