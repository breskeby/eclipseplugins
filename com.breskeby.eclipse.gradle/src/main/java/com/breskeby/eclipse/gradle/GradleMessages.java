package com.breskeby.eclipse.gradle;

import org.eclipse.osgi.util.NLS;

public class GradleMessages extends NLS {
	private static final String BUNDLE_NAME = "com.breskeby.eclipse.gradle.GradleMessages";//$NON-NLS-1$
	
	public static String GradleRunner_Build_Failed__1;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, GradleMessages.class);
	}
}