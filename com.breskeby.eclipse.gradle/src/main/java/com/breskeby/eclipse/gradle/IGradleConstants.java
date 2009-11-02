package com.breskeby.eclipse.gradle;

public class IGradleConstants {
	 
	 public static final String PLUGIN_ID = "com.breskeby.eclipse.gradle";
	 
	 public static final String IMG_GRADLE_ECLIPSE_RUNTIME_OBJECT = PLUGIN_ID + ".gradleEclipse"; //$NON-NLS-1$
	
	 public static final String IMG_TAB_CLASSPATH = PLUGIN_ID + ".IMG_TAB_CLASSPATH"; //$NON-NLS-1$;

	 /**
	  * Status code used by the 'Run Gradle' status handler which is invoked when	 
	  * the launch dialog is opened by the 'Run Ant' action.
	  */
	public static final int STATUS_INIT_RUN_GRADLE = 1000;
	
	public static final String IMG_TAB_GRADLE_TASKS = PLUGIN_ID +".IMG_TAB_GRADLE_TASKS" ;
	
	public static final String IMG_GRADLE_TASK = PLUGIN_ID +".IMG_GRADLE_TASK" ;
	
	public static final String IMG_GRADLE_DEFAULT_TASK = PLUGIN_ID +".IMG_GRADLE_DEFAULT_TASK";

	public static final String GRADLE_TASKS_ATTRIBUTES = PLUGIN_ID +".GRADLE_TASKS_ATTRIBUTES";

	public static final String ID_GRADLE_PROCESS_TYPE = "com.breskeby.eclipse.gradle.gradleProcess";

}
