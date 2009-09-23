package com.breskeby.eclipse.gradle.preferences;

import java.net.URL;

public interface IGradleClasspathEntry {
	/**
	 * Returns the label for this classpath entry.
	 * @return the label for this entry.
	 */
	public String getLabel();
	
	/**
	 * Returns the URL for this classpath entry or <code>null</code>
	 * if it cannot be resolved.
	 * 
	 * @return the url for this classpath entry.
	 */
	public URL getEntryURL();
	
	/**
	 * Returns whether this classpath entry requires the Eclipse runtime to be 
	 * relevant. Defaults value is <code>true</code>
	 * 
	 * @return whether this classpath entry requires the Eclipse runtime
	 */
	public boolean isEclipseRuntimeRequired();
}
