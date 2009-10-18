package com.breskeby.eclipse.gradle.editor;

import org.codehaus.groovy.eclipse.editor.GroovyEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;

import com.breskeby.eclipse.gradle.GradleExecScheduler;

public class GradleEditor extends GroovyEditor{

	
	@SuppressWarnings("restriction")
	public void doSave(IProgressMonitor progressMonitor){
		super.doSave(progressMonitor);
		
		// if changes are done to that buildfile, 
		// update the task informations about it.
//		if(isDirty()){
			//update the task view of the gradle build file
			GradleExecScheduler gradleScheduler = GradleExecScheduler.getInstance();		
			IFile buildFile = getGradleBuildfileResource();
			gradleScheduler.refreshTaskView(buildFile);			
//		}
	}
	
	IFile getGradleBuildfileResource(){
		IEditorInput input = getEditorInput();
		if (input instanceof FileEditorInput) {
			return ((FileEditorInput) input).getFile();
		} else {
			return null;
		}		
	}
}
