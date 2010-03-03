package com.breskeby.eclipse.gradle.editors;

import java.io.File;

import org.codehaus.groovy.eclipse.editor.GroovyEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;

import com.breskeby.eclipse.gradle.GradleExecScheduler;

public class BasicGradleEditor implements IEditorPart{

	private IEditorPart editorDelegate;

	// If groovy editor is available the gradle editor delegates to this. 
	// If groovy editor is not available, this editor delegates to a plain texteditor
	public BasicGradleEditor() {
		super();
		try {
			//check if groovy editor is available
			//should be done in another way I think (e.g. via extensionregistry)
			Class.forName("org.codehaus.groovy.eclipse.editor.GroovyEditor");
			editorDelegate = new GroovyEditor();
		} catch (ClassNotFoundException e) {
			//groovy editor is not available. so use plain texteditor instead
			editorDelegate = new TextEditor();
		}
	}

	
	@SuppressWarnings("restriction")
	public void doSave(IProgressMonitor progressMonitor){
		editorDelegate.doSave(progressMonitor);
		
		// if changes are done to that buildfile, 
		// update the task informations about it.
//		if(isDirty()){
			//update the task view of the gradle build file
			GradleExecScheduler gradleScheduler = GradleExecScheduler.getInstance();		
			IFile buildFile = getGradleBuildfileResource();
			String absolutePath = new File(buildFile.getLocationURI()).getPath();
			gradleScheduler.refreshTaskView(absolutePath, false);			
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

	
	public void addPropertyListener(IPropertyListener arg0) {
		editorDelegate.addPropertyListener(arg0);
	}

	public void createPartControl(Composite arg0) {
		editorDelegate.createPartControl(arg0);
	}

	public void dispose() {
		editorDelegate.dispose();
	}

	public void doSaveAs() {
		editorDelegate.doSaveAs();
	}

	public Object getAdapter(Class arg0) {
		return editorDelegate.getAdapter(arg0);
	}

	public IEditorInput getEditorInput() {
		return editorDelegate.getEditorInput();
	}

	public IEditorSite getEditorSite() {
		return editorDelegate.getEditorSite();
	}

	public IWorkbenchPartSite getSite() {
		return editorDelegate.getSite();
	}

	public String getTitle() {
		return editorDelegate.getTitle();
	}

	public Image getTitleImage() {
		return editorDelegate.getTitleImage();
	}

	public String getTitleToolTip() {
		return editorDelegate.getTitleToolTip();
	}

	public void init(IEditorSite arg0, IEditorInput arg1)
			throws PartInitException {
		editorDelegate.init(arg0, arg1);
	}

	public boolean isDirty() {
		return editorDelegate.isDirty();
	}

	public boolean isSaveAsAllowed() {
		return editorDelegate.isSaveAsAllowed();
	}

	public boolean isSaveOnCloseNeeded() {
		return editorDelegate.isSaveOnCloseNeeded();
	}

	public void removePropertyListener(IPropertyListener arg0) {
		editorDelegate.removePropertyListener(arg0);
	}

	public void setFocus() {
		editorDelegate.setFocus();
	}
}
