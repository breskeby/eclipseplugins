/**********************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 * IBM - Initial API and implementation
 * dakshinamurthy.karra@gmail.com - bug 165371
 **********************************************************************/
package com.breskeby.eclipse.gradle.launchConfigurations;

import org.eclipse.osgi.util.NLS;

public class GradleLaunchConfigurationMessages extends NLS {
	private static final String BUNDLE_NAME = "com.breskeby.eclipse.gradle.launchConfigurations.GradleLaunchConfigurationMessages";//$NON-NLS-1$

	public static String GradleLaunchDelegate_Launching__0__1;

	public static String GradleMainTab_1;
	
	public static String GradleLaunchDelegate_Running__0__2;

	protected static String GradleLaunchDelegate_22;

	protected static String GradleLaunchDelegate_Failure;


	protected static String GradleLaunchDelegate_Build_In_Progress;
	
	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, GradleLaunchConfigurationMessages.class);
	}
}