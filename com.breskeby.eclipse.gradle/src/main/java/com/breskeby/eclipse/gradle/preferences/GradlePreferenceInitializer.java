package com.breskeby.eclipse.gradle.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import com.breskeby.eclipse.gradle.GradlePlugin;

public class GradlePreferenceInitializer extends AbstractPreferenceInitializer {

	public GradlePreferenceInitializer() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore prefs = GradlePlugin.getDefault().getPreferenceStore();

		initGradleRuntimePreferences(prefs);
	}

	
	private void initGradleRuntimePreferences(IPreferenceStore prefs) {
		System.out.println("InitGradleRuntime");
		prefs.setDefault(IGradlePreferenceConstants.USE_SPECIFIC_GRADLE_HOME, false);
		prefs.setDefault(IGradlePreferenceConstants.GRADLE_FIND_BUILD_FILE_NAMES, "build.gradle");
		String gradleHome = getDefaultGradleHome();
		
		if(gradleHome!=null){
			prefs.setDefault(IGradlePreferenceConstants.MANUELL_GRADLE_HOME, gradleHome);
		}
		
		
		PreferenceConverter.setDefault(prefs, IGradlePreferenceConstants.CONSOLE_ERROR_COLOR, new RGB(255, 0, 0)); // red - exactly the same as debug Console
		PreferenceConverter.setDefault(prefs, IGradlePreferenceConstants.CONSOLE_WARNING_COLOR, new RGB(250, 100, 0)); // orange
		PreferenceConverter.setDefault(prefs, IGradlePreferenceConstants.CONSOLE_INFO_COLOR, new RGB(0, 0, 255)); // blue
		PreferenceConverter.setDefault(prefs, IGradlePreferenceConstants.CONSOLE_VERBOSE_COLOR, new RGB(0, 200, 125)); // green
		PreferenceConverter.setDefault(prefs, IGradlePreferenceConstants.CONSOLE_DEBUG_COLOR, new RGB(0, 0, 0)); // black

	}
	
	/**
	 * Returns the absolute path of the default gradle.home to use for the build.
	 * 
	 * @return String absolute path of the default ant.home
	 * @since 3.0
	 */
	public String getDefaultGradleHome() {
		String home = System.getProperty("GRADLE_HOME");
		System.out.println("Gradle Home: " + home);
		return home;
	}
}