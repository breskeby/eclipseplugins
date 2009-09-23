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

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.breskeby.eclipse.gradle.GradlePlugin;
import com.breskeby.eclipse.gradle.GradleUIImages;
import com.breskeby.eclipse.gradle.IGradleUIConstants;

/**
 * Label provider for classpath elements
 */
public class AntClasspathLabelProvider implements ILabelProvider, IColorProvider {


	private GradleClasspathBlock fBlock;
	
	public AntClasspathLabelProvider(GradleClasspathBlock block) {
		fBlock= block;
	}

	private Image getFolderImage() {
		return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
	}

	private Image getJarImage() {
		return JavaUI.getSharedImages().getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_JAR);
	}

	public Image getClasspathImage() {
		return GradleUIImages.getImage(IGradleUIConstants.IMG_TAB_CLASSPATH);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		String file;
		if (element instanceof ClasspathEntry) {
			ClasspathEntry entry = (ClasspathEntry) element;
            if (entry.isEclipseRuntimeRequired()) {
                return GradleUIImages.getImage(IGradleUIConstants.IMG_GRADLE_ECLIPSE_RUNTIME_OBJECT);
            }
			file= entry.toString();
			if (file.endsWith("/")) { //$NON-NLS-1$
				return getFolderImage();
			} 
			return getJarImage();
		}
		
		return getClasspathImage();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		if (element instanceof IGradleClasspathEntry) {
            IGradleClasspathEntry entry= (IGradleClasspathEntry)element;
			StringBuffer label= new StringBuffer(entry.getLabel());
			if (element instanceof GlobalClasspathEntries) {
                if (((GlobalClasspathEntries)element).getType() == ClasspathModel.GRADLE_HOME) {
                	GradlePreferences prefs= GradlePlugin.getPlugin().getPreferences();
    				String defaultAntHome= prefs.getDefaultGradleHome();
    				String currentAntHome= fBlock.getGradleHome();
    				label.append(" ("); //$NON-NLS-1$
    				if (defaultAntHome == null || defaultAntHome.equals(currentAntHome)) {
    					label.append(GradlePreferencesMessages.GradleClasspathLabelProvider_0);
    				} else {
    					label.append(fBlock.getGradleHome());	
    				}
    				label.append(')');
                }
			} 
			return label.toString();
		}
		return element.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
	 */
	public Color getBackground(Object element) {
		if (element instanceof GlobalClasspathEntries) {
            int type= ((GlobalClasspathEntries) element).getType();
            if (type == ClasspathModel.CONTRIBUTED) {
                Display display= Display.getCurrent();
                return display.getSystemColor(SWT.COLOR_INFO_BACKGROUND);
            }
		} else if (element instanceof ClasspathEntry) {
            return getBackground(((ClasspathEntry) element).getParent());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
	 */
	public Color getForeground(Object element) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) {
	}
}
