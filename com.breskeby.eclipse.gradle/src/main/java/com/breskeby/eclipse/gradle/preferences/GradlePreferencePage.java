package com.breskeby.eclipse.gradle.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.breskeby.eclipse.gradle.GradlePlugin;
import com.breskeby.eclipse.gradle.IGradleHelpContextIds;
import com.ibm.icu.text.MessageFormat;

public class GradlePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	private List fConsoleColorList;
	private ColorEditor fConsoleColorEditor;
	
	private BooleanFieldEditor fToolsWarningEditor= null;
	
	// Array containing the message to display, the preference key, and the 
	// default value (initialized in storeInitialValues()) for each color preference
	private final String[][] fAppearanceColorListModel= new String[][] {
		{GradlePreferencesMessages.GradlePreferencePage__Error__2, IGradleUIPreferenceConstants.CONSOLE_ERROR_COLOR, null},
		{GradlePreferencesMessages.GradlePreferencePage__Warning__3, IGradleUIPreferenceConstants.CONSOLE_WARNING_COLOR, null},
		{GradlePreferencesMessages.GradlePreferencePage_I_nformation__4, IGradleUIPreferenceConstants.CONSOLE_INFO_COLOR, null},
		{GradlePreferencesMessages.GradlePreferencePage_Ve_rbose__5, IGradleUIPreferenceConstants.CONSOLE_VERBOSE_COLOR, null},
		{GradlePreferencesMessages.GradlePreferencePage_Deb_ug__6, IGradleUIPreferenceConstants.CONSOLE_DEBUG_COLOR, null},
	};

	/**
 	 * Create the Ant page.
     */
	public GradlePreferencePage() {
		super(GRID);
		setDescription(GradlePreferencesMessages.GradlePreferencePage_General);
		setPreferenceStore(GradlePlugin.getDefault().getPreferenceStore());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		storeAppliedValues();

		Font font= getFieldEditorParent().getFont();
		Label label= new Label(getFieldEditorParent(), SWT.WRAP);
		label.setText(GradlePreferencesMessages.GradlePreferencePage_Enter);
		GridData gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan= 3;
		gd.widthHint= convertWidthInCharsToPixels(60);
		label.setLayoutData(gd);
		label.setLayoutData(gd);
		label.setFont(font);
		
		StringFieldEditor editor = new StringFieldEditor(IGradleUIPreferenceConstants.GRADLE_FIND_BUILD_FILE_NAMES, GradlePreferencesMessages.GradlePreferencePage__Names__3, getFieldEditorParent());
		addField(editor);
		
		IntegerFieldEditor timeout = new IntegerFieldEditor(IGradleUIPreferenceConstants.GRADLE_COMMUNICATION_TIMEOUT, GradlePreferencesMessages.GradlePreferencePage_13, getFieldEditorParent());
        int minValue= GradlePlugin.getDefault().getPreferenceStore().getDefaultInt(IGradleUIPreferenceConstants.GRADLE_COMMUNICATION_TIMEOUT);
        int maxValue = 1200000;
        timeout.setValidRange(minValue, maxValue);
        timeout.setErrorMessage(MessageFormat.format(GradlePreferencesMessages.GradlePreferencePage_14, new Object[] {new Integer(minValue), new Integer(maxValue)})); 
        addField(timeout);
        
        editor = new URLFieldEditor(IGradleUIPreferenceConstants.DOCUMENTATION_URL, GradlePreferencesMessages.GradlePreferencePage_2, getFieldEditorParent());
		addField(editor);
		
		createSpace();
	
		if (!GradlePlugin.isMacOS()) {
			//the mac does not have a tools.jar Bug 40778
		    label= new Label(getFieldEditorParent(), SWT.WRAP);
			label.setText(GradlePreferencesMessages.GradlePreferencePage_0);
			gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL);
			gd.horizontalSpan= 3;
			gd.widthHint= convertWidthInCharsToPixels(60);
			label.setLayoutData(gd);
			label.setFont(font);
			fToolsWarningEditor= new BooleanFieldEditor(IGradleUIPreferenceConstants.GRADLE_TOOLS_JAR_WARNING, GradlePreferencesMessages.GradlePreferencePage_1, getFieldEditorParent());
			addField(fToolsWarningEditor);
			createSpace();
		}
		
		addField(new BooleanFieldEditor(IGradleUIPreferenceConstants.GRADLE_ERROR_DIALOG, GradlePreferencesMessages.GradlePreferencePage_12, getFieldEditorParent()));
		createSpace();
		
		
		addField(new BooleanFieldEditor(IGradleUIPreferenceConstants.GRADLE_CREATE_MARKERS, GradlePreferencesMessages.GradlePreferencePage_15, getFieldEditorParent()));
		label= new Label(getFieldEditorParent(), SWT.WRAP);
		label.setText(GradlePreferencesMessages.GradlePreferencePage_16);
		gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan= 3;
		gd.widthHint= convertWidthInCharsToPixels(60);
		label.setLayoutData(gd);
		label.setFont(font);
		
        createSpace();
		createColorComposite();
		getPreferenceStore().addPropertyChangeListener(this);
	}
	
	private void createSpace() {
		Label label= new Label(getFieldEditorParent(), SWT.NONE);
		GridData gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan= 3;
		label.setLayoutData(gd);
	}

	/**
	 * Stores the initial values of the color preferences. The preference values are updated 
	 * on the fly as the user edits them (instead of only when they press "Apply"). We need
	 * to store the old values so that we can reset them when the user chooses "Cancel".
	 */
	private void storeAppliedValues() {
		IPreferenceStore store= getPreferenceStore();
		for (int i = 0; i < fAppearanceColorListModel.length; i++) {
			String preference = fAppearanceColorListModel[i][1];
			fAppearanceColorListModel[i][2]= store.getString(preference);
		}
	}
	
	private void createColorComposite() {
		Font font= getFieldEditorParent().getFont();
		Label label= new Label(getFieldEditorParent(), SWT.LEFT);
		label.setText(GradlePreferencesMessages.GradlePreferencePage_Gradle_Color_Options__6); 
		label.setFont(font);
		GridData gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan= 2;
		label.setLayoutData(gd);
				
		Composite editorComposite= new Composite(getFieldEditorParent(), SWT.NONE);
		GridLayout layout= new GridLayout();
		layout.numColumns= 2;
		layout.marginHeight= 0;
		layout.marginWidth= 0;
		editorComposite.setLayout(layout);
		editorComposite.setFont(font);
		gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_VERTICAL);
		gd.horizontalSpan= 2;
		editorComposite.setLayoutData(gd);		

		fConsoleColorList= new List(editorComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		gd= new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		gd.heightHint= convertHeightInCharsToPixels(8);
		fConsoleColorList.setLayoutData(gd);
		fConsoleColorList.setFont(font);
				
		Composite stylesComposite= new Composite(editorComposite, SWT.NONE);
		layout= new GridLayout();
		layout.marginHeight= 0;
		layout.marginWidth= 0;
		layout.numColumns= 2;
		stylesComposite.setLayout(layout);
		stylesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		stylesComposite.setFont(font);

		label= new Label(stylesComposite, SWT.LEFT);
		label.setText(GradlePreferencesMessages.GradlePreferencePage_Color__7);
		label.setFont(font);
		gd= new GridData();
		gd.horizontalAlignment= GridData.BEGINNING;
		label.setLayoutData(gd);

		fConsoleColorEditor= new ColorEditor(stylesComposite);
		Button foregroundColorButton= fConsoleColorEditor.getButton();
		gd= new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment= GridData.BEGINNING;
		foregroundColorButton.setLayoutData(gd);
		foregroundColorButton.setFont(font);

		fConsoleColorList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleAppearanceColorListSelection();
			}
		});
		foregroundColorButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int i= fConsoleColorList.getSelectionIndex();
				if (i == -1) { //bug 85590
					return;
				}
				String key= fAppearanceColorListModel[i][1];
				PreferenceConverter.setValue(getPreferenceStore(), key, fConsoleColorEditor.getColorValue());
			}
		});
	}
	
	/**
	 * Restore all color preferences to their values when the page was opened.
	 */
	public boolean performCancel() {
		for (int i = 0; i < fAppearanceColorListModel.length; i++) {
			String preference = fAppearanceColorListModel[i][1];
			PreferenceConverter.setValue(getPreferenceStore(), preference, StringConverter.asRGB(fAppearanceColorListModel[i][2]));
		}
		return super.performCancel();
	}
	
	/**
	 * When the user applies the preferences, update the set of stored
	 * preferences so that we will fall back to the applied values on Cancel.
	 */
	public boolean performOk() {
		storeAppliedValues();
		return super.performOk();
	}
	
	private void handleAppearanceColorListSelection() {	
		int i= fConsoleColorList.getSelectionIndex();
		if (i == -1) { //bug 85590
			return;
		}
		String key= fAppearanceColorListModel[i][1];
		RGB rgb= PreferenceConverter.getColor(getPreferenceStore(), key);
		fConsoleColorEditor.setColorValue(rgb);		
	}
	
	/**
	 * @see FieldEditorPreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IGradleHelpContextIds.GRADLE_PREFERENCE_PAGE);
		return super.createContents(parent);
	}

	/**
	 * @see IWorkbenchPreferencePage#init(IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
	/**
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#initialize()
	 */
	protected void initialize() {
		super.initialize();
		for (int i= 0; i < fAppearanceColorListModel.length; i++) {
			fConsoleColorList.add(fAppearanceColorListModel[i][0]);
		}
		fConsoleColorList.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (fConsoleColorList != null && !fConsoleColorList.isDisposed()) {
					fConsoleColorList.select(0);
					handleAppearanceColorListSelection();
				}
			}
		});
	}
	/**
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		for (int i = 0; i < fAppearanceColorListModel.length; i++) {
			String key= fAppearanceColorListModel[i][1];
			PreferenceConverter.setValue(getPreferenceStore(), key, PreferenceConverter.getDefaultColor(getPreferenceStore(), key));
		}
		handleAppearanceColorListSelection();
		
		super.performDefaults();
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
		if (event.getProperty().equals(IGradleUIPreferenceConstants.GRADLE_TOOLS_JAR_WARNING)) {
			if (fToolsWarningEditor != null) {
				fToolsWarningEditor.load();
			}
		} else {
			super.propertyChange(event);
		}
	}
}