/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.breskeby.eclipse.gradle.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.editors.text.EditorsUI;

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
		prefs.setDefault(IGradleUIPreferenceConstants.GRADLE_FIND_BUILD_FILE_NAMES, "build.gradle"); //$NON-NLS-1$
		
		prefs.setDefault(IGradleUIPreferenceConstants.DOCUMENTATION_URL, "http://gradle.org/userguide.html"); //$NON-NLS-1$
        
        prefs.setDefault(IGradleUIPreferenceConstants.GRADLE_COMMUNICATION_TIMEOUT, 20000);
		
		EditorsUI.useAnnotationsPreferencePage(prefs);
		EditorsUI.useQuickDiffPreferencePage(prefs);
		if (GradlePlugin.isMacOS()) {
			//the mac does not have a tools.jar Bug 40778
			prefs.setDefault(IGradleUIPreferenceConstants.GRADLE_TOOLS_JAR_WARNING, false);
		} else {
			prefs.setDefault(IGradleUIPreferenceConstants.GRADLE_TOOLS_JAR_WARNING, true);
		}
		
		prefs.setDefault(IGradleUIPreferenceConstants.GRADLE_ERROR_DIALOG, true);
		
		prefs.setDefault(IGradleUIPreferenceConstants.GRADLEEDITOR_FILTER_INTERNAL_TARGETS, false);
		prefs.setDefault(IGradleUIPreferenceConstants.GRADLEEDITOR_FILTER_IMPORTED_ELEMENTS, false);
		prefs.setDefault(IGradleUIPreferenceConstants.GRADLEEDITOR_FILTER_PROPERTIES, false);
		prefs.setDefault(IGradleUIPreferenceConstants.GRADLEEDITOR_FILTER_TOP_LEVEL, false);

		// Gradle Editor color preferences
//		PreferenceConverter.setDefault(prefs, IGradleEditorColorConstants.TEXT_COLOR, IGradleEditorColorConstants.DEFAULT);
//		PreferenceConverter.setDefault(prefs, IGradleEditorColorConstants.PROCESSING_INSTRUCTIONS_COLOR, IGradleEditorColorConstants.PROC_INSTR);
//		PreferenceConverter.setDefault(prefs, IGradleEditorColorConstants.STRING_COLOR, IGradleEditorColorConstants.STRING);
//		PreferenceConverter.setDefault(prefs, IGradleEditorColorConstants.TAG_COLOR, IGradleEditorColorConstants.TAG);
//		PreferenceConverter.setDefault(prefs, IGradleEditorColorConstants.XML_COMMENT_COLOR, IGradleEditorColorConstants.XML_COMMENT);
//		PreferenceConverter.setDefault(prefs, IGradleEditorColorConstants.XML_DTD_COLOR, IGradleEditorColorConstants.XML_DTD);
//		
//		PreferenceConverter.setDefault(prefs, IGradleUIPreferenceConstants.CONSOLE_ERROR_COLOR, new RGB(255, 0, 0)); // red - exactly the same as debug Console
//		PreferenceConverter.setDefault(prefs, IGradleUIPreferenceConstants.CONSOLE_WARNING_COLOR, new RGB(250, 100, 0)); // orange
//		PreferenceConverter.setDefault(prefs, IGradleUIPreferenceConstants.CONSOLE_INFO_COLOR, new RGB(0, 0, 255)); // blue
//		PreferenceConverter.setDefault(prefs, IGradleUIPreferenceConstants.CONSOLE_VERBOSE_COLOR, new RGB(0, 200, 125)); // green
//		PreferenceConverter.setDefault(prefs, IGradleUIPreferenceConstants.CONSOLE_DEBUG_COLOR, new RGB(0, 0, 0)); // black
//		
//		GradleEditorPreferenceConstants.initializeDefaultValues(prefs);
	}
}
