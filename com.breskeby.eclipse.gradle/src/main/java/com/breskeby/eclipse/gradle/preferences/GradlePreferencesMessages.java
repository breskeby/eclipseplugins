package com.breskeby.eclipse.gradle.preferences;

import org.eclipse.osgi.util.NLS;

public class GradlePreferencesMessages extends NLS {
	private static final String BUNDLE_NAME = "com.breskeby.eclipse.gradle.preferences.GradlePreferencesMessages";//$NON-NLS-1$
	
	public static String GradleRuntimePreferencePage_Description;

	public static String GradleRuntimePreferencePage_GradleHome_Label;

	public static String GradleRuntimePreferencePage_BuildFileName;

	public static String GradleRuntimePreferencePage_Enter;
	public static String GradleRuntimePreferencePage_GradleErrorDialog_Label;

	
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, GradlePreferencesMessages.class);
	}
}
