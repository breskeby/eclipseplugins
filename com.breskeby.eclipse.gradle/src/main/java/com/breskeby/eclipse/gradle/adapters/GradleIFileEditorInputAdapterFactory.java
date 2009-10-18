 /*
 * Copyright 2003-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.breskeby.eclipse.gradle.adapters;

import org.eclipse.core.runtime.IAdapterFactory;

//import org.codehaus.groovy.ast.ClassNode;
//import org.codehaus.groovy.eclipse.core.GroovyCore;
//import org.eclipse.core.runtime.IAdapterFactory;
//import org.eclipse.ui.IFileEditorInput;

/**
 * This class will take an FileEditorInput and adapt it to varios Groovy friendly 
 * classes / interfaces.
 * 
 * @author David Kerber
 */
public class GradleIFileEditorInputAdapterFactory implements IAdapterFactory {
	
//	private static final Class< ? >[] classes = new Class[] { ClassNode.class }  ;
	

	private static final Class< ? >[] classes = null;

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
    public Object getAdapter(Object adaptableObject, Class adapterType) {
		Object returnValue = null ; 
		
//		if( (ClassNode.class.equals(adapterType) || ClassNode[].class.equals(adapterType) ) && adaptableObject instanceof IFileEditorInput) {
//			try {
//				IFileEditorInput fileEditor = (IFileEditorInput) adaptableObject ;
//				returnValue = fileEditor.getFile().getAdapter(adapterType);
//			} catch (Exception ex) {
//				GroovyCore.logException("error adapting file to ClassNode", ex);
//			}
//		}
		
		
		return returnValue ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	@SuppressWarnings("unchecked")
    public Class[] getAdapterList() {
		return classes ; 
	}

}
