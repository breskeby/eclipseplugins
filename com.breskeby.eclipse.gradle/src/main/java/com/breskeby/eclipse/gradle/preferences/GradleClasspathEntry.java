/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package com.breskeby.eclipse.gradle.preferences;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;

import com.breskeby.eclipse.gradle.GradlePlugin;

public class GradleClasspathEntry implements IGradleClasspathEntry {

	private String entryString;
	private boolean eclipseRequired= false;
	private URL url= null;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ant.core.IAntClasspathEntry#getLabel()
	 */
	public String getLabel() {
		
		return entryString;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ant.core.IAntClasspathEntry#getEntryURL()
	 */
	public URL getEntryURL() {
		if (url != null) {
			return url;
		}
		try {
			String expanded = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(entryString);
			return new URL("file:" + expanded); //$NON-NLS-1$
		} catch (CoreException e) {
			try {
				return new URL("file:" + entryString); //$NON-NLS-1$
			} catch (MalformedURLException e1) {
				return null;
			}
		} catch (MalformedURLException e) {
			GradlePlugin.log(e);
		}
		return null;
	}

	public GradleClasspathEntry(String entryString) {
		this.entryString= entryString;
	}
	
	public GradleClasspathEntry(URL url) {
		this.url= url;
		this.entryString= new File(url.getPath()).getAbsolutePath();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof IGradleClasspathEntry) {
			IGradleClasspathEntry other= (IGradleClasspathEntry)obj;
			return entryString.equals(other.getLabel());
		}
		return false;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return entryString.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getLabel();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ant.core.IAntClasspathEntry#isEclipseRuntimeRequired()
	 */
	public boolean isEclipseRuntimeRequired() {
		return eclipseRequired;
	}
	
	public void setEclipseRuntimeRequired(boolean eclipseRequired) {
		this.eclipseRequired = eclipseRequired;
	}
}