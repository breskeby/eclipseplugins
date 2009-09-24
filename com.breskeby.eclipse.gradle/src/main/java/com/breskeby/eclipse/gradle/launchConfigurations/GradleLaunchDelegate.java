package com.breskeby.eclipse.gradle.launchConfigurations;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.RefreshTab;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.externaltools.internal.launchConfigurations.ExternalToolsUtil;
import org.eclipse.ui.externaltools.internal.program.launchConfigurations.BackgroundResourceRefresher;

import com.breskeby.eclipse.gradle.GradlePlugin;
import com.breskeby.eclipse.gradle.GradleRunner;
import com.breskeby.eclipse.gradle.preferences.IGradlePreferenceConstants;
import com.ibm.icu.text.MessageFormat;

public class GradleLaunchDelegate extends LaunchConfigurationDelegate  {

	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		if (monitor.isCanceled()) {
			return;
		}
			
		System.out.println("STARTING GRADLE BUILD");

		//create classpath for gradle 
		IPreferenceStore preferenceStore = GradlePlugin.getPlugin().getPreferenceStore();
		
		String gradleHome = preferenceStore.getString(IGradlePreferenceConstants.GRADLE_HOME);
		
		System.out.println("GRADLE HOME: " + gradleHome);

		
		if (monitor.isCanceled()) {
			return;
		}
		
		if (CommonTab.isLaunchInBackground(configuration)) {
			monitor.beginTask(MessageFormat.format(GradleLaunchConfigurationMessages.GradleLaunchDelegate_Launching__0__1, new String[] {configuration.getName()}), 10);
		} else {
			monitor.beginTask(MessageFormat.format(GradleLaunchConfigurationMessages.GradleLaunchDelegate_Running__0__2, new String[] {configuration.getName()}), 100);
		}
		
//		// resolve location
//		IPath location = ExternalToolsUtil.getLocation(configuration);
//		monitor.worked(1);
//		
		if (monitor.isCanceled()) {
			return;
		}
		
//		if (!isSeparateJRE && AntRunner.isBuildRunning()) {
//			IStatus status= new Status(IStatus.ERROR, IAntUIConstants.PLUGIN_ID, 1, MessageFormat.format(AntLaunchConfigurationMessages.AntLaunchDelegate_Build_In_Progress, new String[]{location.toOSString()}), null);
//			throw new CoreException(status);
//		}		
//		
//		// resolve working directory
//		IPath workingDirectory = ExternalToolsUtil.getWorkingDirectory(configuration);
//		String basedir = null;
//		if (workingDirectory != null) {
//			basedir= workingDirectory.toOSString();
//		}
//		monitor.worked(1);
//		
//		if (monitor.isCanceled()) {
//			return;
//		}
//
//		// link the process to its build logger via a timestamp
//		long timeStamp = System.currentTimeMillis();
//		String idStamp = Long.toString(timeStamp);
//		StringBuffer idProperty = new StringBuffer("-D"); //$NON-NLS-1$
//		idProperty.append(AbstractEclipseBuildLogger.ANT_PROCESS_ID);
//		idProperty.append('=');
//		idProperty.append(idStamp);
//		
//		// resolve arguments	
//		String[] arguments = null;
//		if (isSeparateJRE) {
//			arguments = new String[] {getProgramArguments(configuration)};
//        } else { 
//			arguments = ExternalToolsUtil.getArguments(configuration);
//        }
//		
//		Map userProperties= AntUtil.getProperties(configuration);
//		if (userProperties != null) {//create a copy so as to not affect the configuration with transient properties
//			userProperties= new HashMap(userProperties);
//		}
//		String[] propertyFiles= AntUtil.getPropertyFiles(configuration);
//		String[] targets = AntUtil.getTargetNames(configuration);
//		URL[] customClasspath= AntUtil.getCustomClasspath(configuration);
//		String antHome= AntUtil.getAntHome(configuration);
//		
//		boolean setInputHandler= true;
//		try {
//			//check if set specify inputhandler
//			setInputHandler = configuration.getAttribute(IAntUIConstants.SET_INPUTHANDLER, true);
//		} catch (CoreException ce) {
//			AntUIPlugin.log(ce);			
//		}
		
		// resolve location
		IPath location = ExternalToolsUtil.getLocation(configuration);
		monitor.worked(1);
		
		GradleRunner runner= null;
//		if (!isSeparateJRE) {
			runner = new GradleRunner();
//			configureAntRunner(configuration, location, basedir, idProperty, arguments, userProperties, propertyFiles, targets, customClasspath, antHome, setInputHandler);
//		}
		 
//		runInSameVM(configuration, launch, monitor, location, idStamp, runner);
		
		monitor.worked(1);
		
		System.out.println("runInSameVM");

		runInSameVM(configuration, launch, monitor, location, "idStamp", runner, new StringBuffer("commandline"), false);
			
		monitor.worked(60);
								
		if (monitor.isCanceled()) {
			return;
		}
		monitor.done();
		

		System.out.println("FINISHED GRADLE BUILD");
	}
	
	private void handleException(final CoreException e, final String title) {
		IPreferenceStore store= GradlePlugin.getDefault().getPreferenceStore();
//		if (store.getBoolean(IGradlePreferenceConstants.GRADLE_ERROR_DIALOG)) {
			GradlePlugin.getStandardDisplay().asyncExec(new Runnable() {
				public void run() {
					MessageDialogWithToggle.openError(null, title, e.getMessage(), GradleLaunchConfigurationMessages.GradleLaunchDelegate_22, false, GradlePlugin.getDefault().getPreferenceStore(), IGradlePreferenceConstants.GRADLE_ERROR_DIALOG);
				}
			});
//		}
	}
	
	private void runInSameVM(ILaunchConfiguration configuration, ILaunch launch, IProgressMonitor monitor, IPath location, String idStamp, GradleRunner runner, StringBuffer commandLine, boolean captureOutput) throws CoreException {
		System.out.println("runInSameVM start");
		
		Map attributes= new HashMap(2);
//		attributes.put(IProcess.ATTR_PROCESS_TYPE, IAntLaunchConfigurationConstants.ID_ANT_PROCESS_TYPE);
//		attributes.put(AbstractEclipseBuildLogger.ANT_PROCESS_ID, idStamp);
				
		final GradleProcess process = new GradleProcess(location.toOSString(), launch, attributes);
//		setProcessAttributes(process, idStamp, commandLine, captureOutput);
//		boolean debug= fMode.equals(ILaunchManager.DEBUG_MODE);
		System.out.println("CommonTab.isLaunchInBackground(configuration)");
		System.out.println("Configuration: " + configuration);

		if (CommonTab.isLaunchInBackground(configuration)) {
			System.out.println("in CommonTab.isLaunchInBackground(configuration)");
			final GradleRunner finalRunner = runner;

			Runnable r = new Runnable() {
				public void run() {
					try {
						System.out.println("finalRunner.run");
						finalRunner.run(process);
					} catch (CoreException e) {
						handleException(e, GradleLaunchConfigurationMessages.GradleLaunchDelegate_Failure);
					}
					process.terminated();
				}
			};
			Thread background = new Thread(r);
            background.setDaemon(true);
			background.start();
			monitor.worked(1);
			//refresh resources after process finishes
			if (RefreshTab.getRefreshScope(configuration) != null) {
				BackgroundResourceRefresher refresher = new BackgroundResourceRefresher(configuration, process);
				refresher.startBackgroundRefresh();
			}	
		} else {
			// execute the build 
			try {
				runner.run(monitor);
			} catch (CoreException e) {
				System.out.println("process.terminated() start");
				
				process.terminated();

				System.out.println("process.terminated() fertig");
//				monitor.done();
//				handleException(e, AntLaunchConfigurationMessages.AntLaunchDelegate_23);
				return;
			}
//			process.terminated();
			
			// refresh resources
//			RefreshTab.refreshResources(configuration, monitor);
		}
	}
}