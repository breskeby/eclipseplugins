package com.breskeby.eclipse.gradle.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.breskeby.eclipse.gradle.GradlePlugin;

public class GradleRuntimePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	/**
 	 * Create the Gradle page.
     */
	public GradleRuntimePreferencePage() {
		super(GRID);
		setDescription(GradlePreferencesMessages.GradleRuntimePreferencePage_Description);
		setPreferenceStore(GradlePlugin.getDefault().getPreferenceStore());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		Font font= getFieldEditorParent().getFont();
		Label label= new Label(getFieldEditorParent(), SWT.WRAP);
		label.setText(GradlePreferencesMessages.GradleRuntimePreferencePage_Enter);
		GridData gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan= 3;
		gd.widthHint= convertWidthInCharsToPixels(60);
		label.setLayoutData(gd);
		label.setLayoutData(gd);
		label.setFont(font);
//		
		FieldEditor editor = new StringFieldEditor(IGradlePreferenceConstants.GRADLE_FIND_BUILD_FILE_NAMES, GradlePreferencesMessages.GradleRuntimePreferencePage_BuildFileName, getFieldEditorParent());
		addField(editor);
		
		editor = new DirectoryFieldEditor(IGradlePreferenceConstants.GRADLE_HOME, GradlePreferencesMessages.GradleRuntimePreferencePage_GradleHome_Label, getFieldEditorParent());
		addField(editor);
	
		editor = new BooleanFieldEditor(IGradlePreferenceConstants.GRADLE_ERROR_DIALOG, GradlePreferencesMessages.GradleRuntimePreferencePage_GradleErrorDialog_Label, getFieldEditorParent());
		addField(editor);
		createSpace();
		getPreferenceStore().addPropertyChangeListener(this);
	}
	
	private void createSpace() {
		Label label= new Label(getFieldEditorParent(), SWT.NONE);
		GridData gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan= 3;
		label.setLayoutData(gd);
	}
		
	
	/**
	 * @see IWorkbenchPreferencePage#init(IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#dispose()
	 */
	public void dispose() {
		getPreferenceStore().removePropertyChangeListener(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
	}
}
