package com.breskeby.eclipse.gradle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class GradleRunner implements IApplication {

	@Override
	public Object start(IApplicationContext arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Runs the build file.
	 * 
	 * Only one build can occur at any given time.
	 * 
	 * Sets the current threads context class loader to the AntClassLoader
	 * for the duration of the build.
	 * 
	 * @param monitor a progress monitor, or <code>null</code> if progress
	 *    reporting and cancellation are not desired
	 * @throws CoreException Thrown if a build is already occurring or if an exception occurs during the build
	 */
	public void run(IProgressMonitor monitor) throws CoreException {
//		if (buildRunning) {
//			IStatus status= new Status(IStatus.ERROR, AntCorePlugin.PI_ANTCORE, AntCorePlugin.ERROR_RUNNING_BUILD, NLS.bind(InternalCoreAntMessages.AntRunner_Already_in_progess, new String[]{buildFileLocation}), null);
//			throw new CoreException(status);
//		}
//		buildRunning= true;
		Object runner= null;
		Class classInternalAntRunner= null;
		ClassLoader originalClassLoader= Thread.currentThread().getContextClassLoader();
		try {
//			classInternalAntRunner = getInternalAntRunner();
//			runner = classInternalAntRunner.newInstance();
//			// set build file
//			Method setBuildFileLocation = classInternalAntRunner.getMethod("setBuildFileLocation", new Class[] { String.class }); //$NON-NLS-1$
//			setBuildFileLocation.invoke(runner, new Object[] { buildFileLocation });
//			
//			//set the custom classpath
//			if (customClasspath != null) {
//				Method setCustomClasspath = classInternalAntRunner.getMethod("setCustomClasspath", new Class[] { URL[].class }); //$NON-NLS-1$
//				setCustomClasspath.invoke(runner, new Object[] { customClasspath });
//			}
//			
//			// add listeners
//			if (buildListeners != null) {
//				Method addBuildListeners = classInternalAntRunner.getMethod("addBuildListeners", new Class[] { List.class }); //$NON-NLS-1$
//				addBuildListeners.invoke(runner, new Object[] { buildListeners });
//			}
//			
//			if (buildLoggerClassName == null) {
//				//indicate that the default logger is not to be used
//				buildLoggerClassName= ""; //$NON-NLS-1$
//			}
//			// add build logger
//			Method addBuildLogger = classInternalAntRunner.getMethod("addBuildLogger", new Class[] { String.class }); //$NON-NLS-1$
//			addBuildLogger.invoke(runner, new Object[] { buildLoggerClassName });
//			
//			if (inputHandlerClassName != null) {	
//				// add the input handler
//				Method setInputHandler = classInternalAntRunner.getMethod("setInputHandler", new Class[] { String.class }); //$NON-NLS-1$
//				setInputHandler.invoke(runner, new Object[] { inputHandlerClassName });
//			}
//			
//			basicConfigure(classInternalAntRunner, runner);
//			
//			// add progress monitor
//			if (monitor != null) {
//				progressMonitor = monitor;
//				Method setProgressMonitor = classInternalAntRunner.getMethod("setProgressMonitor", new Class[] { IProgressMonitor.class }); //$NON-NLS-1$
//				setProgressMonitor.invoke(runner, new Object[] { monitor });
//			}
//			
//			// set message output level
//			if (messageOutputLevel != 2) { //changed from the default Project.MSG_INFO
//				Method setMessageOutputLevel = classInternalAntRunner.getMethod("setMessageOutputLevel", new Class[] { int.class }); //$NON-NLS-1$
//				setMessageOutputLevel.invoke(runner, new Object[] { new Integer(messageOutputLevel)});
//			}
//			
//			// set execution targets
//			if (targets != null) {
//				Method setExecutionTargets = classInternalAntRunner.getMethod("setExecutionTargets", new Class[] { String[].class }); //$NON-NLS-1$
//				setExecutionTargets.invoke(runner, new Object[] { targets });
//			} 
//
//			// run
//			Method run = classInternalAntRunner.getMethod("run", null); //$NON-NLS-1$
//			run.invoke(runner, null);
//		} catch (NoClassDefFoundError e) {
//			problemLoadingClass(e);
//		} catch (ClassNotFoundException e) {
//			problemLoadingClass(e);
//		} catch (InvocationTargetException e) {
//			handleInvocationTargetException(runner, classInternalAntRunner, e);
//		} catch (Exception e) {
//			String message = (e.getMessage() == null) ? InternalCoreAntMessages.AntRunner_Build_Failed__3 : e.getMessage();
//			IStatus status= new Status(IStatus.ERROR, AntCorePlugin.PI_ANTCORE, AntCorePlugin.ERROR_RUNNING_BUILD, message, e);
//			throw new CoreException(status);
		} finally {
//			buildRunning= false;
			Thread.currentThread().setContextClassLoader(originalClassLoader);
		}
		

		System.out.println("GradleRunner.run finished");
	}

}
