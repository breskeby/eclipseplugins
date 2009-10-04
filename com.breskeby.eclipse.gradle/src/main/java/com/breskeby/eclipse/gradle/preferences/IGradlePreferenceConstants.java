package com.breskeby.eclipse.gradle.preferences;

import com.breskeby.eclipse.gradle.GradlePlugin;

public interface IGradlePreferenceConstants {

	public static final String MANUELL_GRADLE_HOME = GradlePlugin.PLUGIN_ID + "_MAN_GRADLE_HOME";
	public static final String GRADLE_FIND_BUILD_FILE_NAMES = "_GRADLE_FIND_BUILD_FILE_NAMES"; //$NON-NLS-1$
	public static final String USE_SPECIFIC_GRADLE_HOME = "_USE_SPECIFIC_GRADLE_HOME";
	
	/**
	 * The symbolic names for colors for displaying the content in the Console
	 * @see org.eclipse.jface.resource.ColorRegistry
	 */
	
	public static final String CONSOLE_ERROR_COLOR = "com.breskeby.eclipse.gradle.ui.errorColor"; //$NON-NLS-1$
	public static final String CONSOLE_WARNING_COLOR = "com.breskeby.eclipse.gradle.ui.warningColor"; //$NON-NLS-1$
	public static final String CONSOLE_INFO_COLOR = "com.breskeby.eclipse.gradle.ui.informationColor"; //$NON-NLS-1$
	public static final String CONSOLE_VERBOSE_COLOR = "com.breskeby.eclipse.gradle.ui.verboseColor"; //$NON-NLS-1$
	public static final String CONSOLE_DEBUG_COLOR = "com.breskeby.eclipse.gradle.ui.debugColor"; //$NON-NLS-1$	
	
	public static final String GRADLE_ERROR_DIALOG = "GRADLE_ERROR_DIALOG";
	

}
