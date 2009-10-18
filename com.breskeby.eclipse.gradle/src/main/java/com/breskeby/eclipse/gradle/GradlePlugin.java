package com.breskeby.eclipse.gradle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.util.tracker.ServiceTracker;

import com.breskeby.eclipse.gradle.util.ColorManager;

/**
 * @author breskeby
 * 
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

	/**
	 * Status code indicating an error occurred running a build.
	 * @since 2.1
	 */
	public static final int ERROR_RUNNING_BUILD = 1;
	
	
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
		IStatus status= new Status(IStatus.ERROR, PLUGIN_ID, INTERNAL_ERROR, "Error logged from GradlePlugin: ", t); //$NON-NLS-1$
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
	
	/**
	 * Simple algorithm to find the highest version of <code>org.codehaus.gradle</code> 
	 * available. If there are other providers that are not <code>org.codehaus.gradle</code>
	 * <code>null</code> is returned so that all bundles will be inspected 
	 * for contributed libraries.
	 * <p>
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=282851
	 * </p>
	 * @param packages the live list of {@link ExportedPackage}s to inspect
	 * @return the bundle that represents the highest version of <code>org.codehaus.gradle</code> or <code>null</code>
	 * if there are other providers for the <code>org.gradle</code> packages.
	 */
	@SuppressWarnings("unchecked")
	Bundle findHighestGradleVersion(ExportedPackage[] packages) {
		Bundle bundle = null;
		Set bundles = new HashSet();
		for (int i = 0; i < packages.length; i++) {
			bundle = packages[i].getExportingBundle();
			if(bundle == null) {
				continue;
			}
			if("org.codehaus.gradle".equals(bundle.getSymbolicName())) { //$NON-NLS-1$
				bundles.add(bundle);
			}
			else {
				return null;
			}
		}
		Bundle highest = null;
		Bundle temp = null;
		for (Iterator<Bundle> iter = bundles.iterator(); iter.hasNext();) {
			temp = iter.next();
			if(highest == null) {
				highest = temp;
			}
			else {
				if(highest.getVersion().compareTo(temp.getVersion()) < 0) {
					highest = temp;
				}
			}
		}
		return highest;
	}
	
	/**
	 * @return the absolute path to the defaultGradleHome directory
	 * */
	public String getDefaultGradleHome(){
		String gradleHomeString = null;
		ServiceTracker tracker = new ServiceTracker(GradlePlugin.getPlugin().getBundle().getBundleContext(), PackageAdmin.class.getName(), null);
		tracker.open();
		try {
			PackageAdmin packageAdmin = (PackageAdmin) tracker.getService();
					
			if (packageAdmin != null) {
				ExportedPackage[] packages = packageAdmin.getExportedPackages("org.gradle"); //$NON-NLS-1$
				Bundle bundle = findHighestGradleVersion(packages);
				if(bundle!=null){
					URL entryURL = bundle.getEntry(".");
					URL fileURL = FileLocator.toFileURL(entryURL);
					gradleHomeString = new File(fileURL.getPath()).getAbsolutePath();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			tracker.close();
		}
		
		//check if gradle scripts are executable
		makeGradleScriptsExecutable(gradleHomeString);
		return gradleHomeString;
	}
	
	
	/**
	 * this is necessary because root.permissions in feature build.properties doesn't work yet
	 * */
	private void makeGradleScriptsExecutable(String gradleHomeString) {
		String gradleBinPath = gradleHomeString + System.getProperty("file.separator") + "bin"+System.getProperty("file.separator");
		File scriptMacLinux = new File(gradleBinPath+"gradle");
		File scriptWin = new File(gradleBinPath+"gradle.bat");
		
		//setScripts executable
		scriptMacLinux.setExecutable(true);
		scriptWin.setExecutable(true);

	}

	/**
	 * Returns the preference color, identified by the given preference.
	 */
	public static Color getPreferenceColor(String pref) {
		return ColorManager.getDefault().getColor(PreferenceConverter.getColor(getDefault().getPreferenceStore(), pref));
	}	
	
//	public GradleExecScheduler getExecScheduler(){
//		GradleExecScheduler scheduler = GradleExecScheduler.getInstance();
//	}
}
