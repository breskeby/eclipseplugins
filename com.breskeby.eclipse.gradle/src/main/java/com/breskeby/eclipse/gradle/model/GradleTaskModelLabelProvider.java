/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.breskeby.eclipse.gradle.model;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.gradle.foundation.TaskView;

import com.breskeby.eclipse.gradle.GradleImages;
import com.breskeby.eclipse.gradle.IGradleConstants;


public class GradleTaskModelLabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex == 0) {
			return getImage(element);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		if (columnIndex == 0){
			return getText(element);
		}
		String desc= ((TaskView)element).getDescription();
		if (desc == null) {
			return ""; //$NON-NLS-1$
		}
		return desc;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(Object)
	 */
	public Image getImage(Object anElement) {
		TaskView node = (TaskView)anElement;
		if(node.isDefault()){
			return GradleImages.getImage(IGradleConstants.IMG_GRADLE_DEFAULT_TASK);
		}else{
			return GradleImages.getImage(IGradleConstants.IMG_GRADLE_TASK);
		}
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(Object)
	 */
	public String getText(Object node) {
		TaskView element= (TaskView) node;
		return element.getFullTaskName();
	}

	public Color getForeground(Object node) {
		if(node instanceof TaskView && ((TaskView)node).isDefault()){
			//set default tasks blue
			return Display.getDefault().getSystemColor(SWT.COLOR_BLUE);			
		}//set normal tasks black
			return Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	}

	public Color getBackground(Object element) {
		return null;
	}
}
