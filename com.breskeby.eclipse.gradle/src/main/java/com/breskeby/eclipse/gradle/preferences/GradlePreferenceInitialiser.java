package com.breskeby.eclipse.gradle.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.breskeby.eclipse.gradle.GradlePlugin;

public class GradlePreferenceInitialiser extends AbstractPreferenceInitializer {

	public GradlePreferenceInitialiser() {
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
		//		prefs.set
	}

	
	private void initGradleRuntimePreferences(IPreferenceStore prefs) {
		prefs.setDefault(IGradlePreferenceConstants.GRADLE_ERROR_DIALOG, true);
		prefs.setDefault(IGradlePreferenceConstants.GRADLE_FIND_BUILD_FILE_NAMES, "build.gradle");
		String gradleHome = getDefaultGradleHome();
		
		if(gradleHome!=null){
			prefs.setDefault(IGradlePreferenceConstants.GRADLE_HOME, gradleHome);
		}
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