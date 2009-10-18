package com.breskeby.eclipse.gradle.model;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.gradle.foundation.ProjectView;
import org.gradle.foundation.TaskView;

public class GradleTaskModelContentProvider implements ITreeContentProvider {

	protected static final Object[] EMPTY_ARRAY= new Object[0];
	
	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
	}
    
	/**
	 * do nothing
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(Viewer, Object, Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(Object)
	 */
	public Object[] getChildren(Object parentNode) {		
		if (parentNode instanceof ProjectView) {
			ProjectView parentElement = (ProjectView)parentNode;
			List<TaskView> children = parentElement.getTasks();
			return children.toArray();
		} 
		return EMPTY_ARRAY;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(Object)
	 */
	public Object getParent(Object aNode) {
		if(aNode instanceof TaskView){
			return ((TaskView)aNode).getProject();
		}
		return EMPTY_ARRAY;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(Object)
	 */
	public boolean hasChildren(Object aNode) {
		if(aNode instanceof ProjectView){
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(Object)
	 */
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ProjectView) {
		    List<TaskView> tasks = ((ProjectView) inputElement).getTasks();
		    if (tasks.size() == 0) {
				return new TaskView[0];
			} 
			return tasks.toArray();
		}
		
		if (inputElement instanceof Object[]) {
			return (Object[])inputElement;
		}
		return EMPTY_ARRAY;
	}
}