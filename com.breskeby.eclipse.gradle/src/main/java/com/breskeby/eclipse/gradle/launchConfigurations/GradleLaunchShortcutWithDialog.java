package com.breskeby.eclipse.gradle.launchConfigurations;

public class GradleLaunchShortcutWithDialog extends GradleLaunchShortcut {
	
	/**
	 * Creates a new Gradle launch shortcut that will open the
	 * launch configuration dialog.
	 */
	public GradleLaunchShortcutWithDialog() {
		super();
		
		setShowDialog(true);
	}
}
