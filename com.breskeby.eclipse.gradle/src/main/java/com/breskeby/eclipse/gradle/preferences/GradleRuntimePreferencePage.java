package com.breskeby.eclipse.gradle.preferences;

import java.util.ArrayList;
import java.util.Arrays;

//import org.eclipse.ant.core.AntCorePlugin;
//import org.eclipse.ant.core.AntCorePreferences;
//import org.eclipse.ant.core.Property;
//import org.eclipse.ant.core.Task;
//import org.eclipse.ant.core.Type;
//import org.eclipse.ant.internal.ui.AntUIPlugin;
//import org.eclipse.ant.internal.ui.IAntUIHelpContextIds;
//import org.eclipse.ant.internal.ui.preferences.AntClasspathPage;
//import org.eclipse.ant.internal.ui.preferences.AntPreferencesMessages;
//import org.eclipse.ant.internal.ui.preferences.AntPropertiesPage;
//import org.eclipse.ant.internal.ui.preferences.AntTasksPage;
//import org.eclipse.ant.internal.ui.preferences.AntTypesPage;
//import org.eclipse.ant.internal.ui.preferences.TabFolderLayout;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferencePage;
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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.breskeby.eclipse.gradle.GradlePlugin;
import com.ibm.icu.text.MessageFormat;

public class GradleRuntimePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	
	
//	private AntClasspathPage classpathPage;
//	private AntTasksPage tasksPage;
//	private AntTypesPage typesPage;
//	private AntPropertiesPage propertiesPage;
//	
	/**
	 * Creates the preference page
	 */
	public GradleRuntimePreferencePage() {
		setDescription(GradlePreferencesMessages.GradlePreferencePage_description);
		setPreferenceStore(GradlePlugin.getDefault().getPreferenceStore());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
//	
//	/* (non-Javadoc)
//	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
//	 */
	protected Control createContents(Composite parent) {
////		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IAntUIHelpContextIds.ANT_RUNTIME_PREFERENCE_PAGE);
//		initializeDialogUnits(parent);
//
		TabFolder folder = new TabFolder(parent, SWT.NONE);
//		folder.setLayout(new TabFolderLayout());	
//		folder.setLayoutData(new GridData(GridData.FILL_BOTH));
//		folder.setFont(parent.getFont());
//
//		classpathPage = new AntClasspathPage(this);
//		classpathPage.createTabItem(folder);
//		
//		tasksPage = new AntTasksPage(this);
//		tasksPage.createTabItem(folder);
//		
//		typesPage = new AntTypesPage(this);
//		typesPage.createTabItem(folder);
//
//		propertiesPage= new AntPropertiesPage(this);
//		propertiesPage.createTabItem(folder);
//		
//		tasksPage.initialize();
//		typesPage.initialize();
//		classpathPage.initialize();
//		propertiesPage.initialize();
//
		return folder;
	}
//	
//	/* (non-Javadoc)
//	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
//	 */
//	protected void performDefaults() {
//		super.performDefaults();
//		
//		AntCorePreferences prefs = AntCorePlugin.getPlugin().getPreferences();
//		tasksPage.setInput(prefs.getDefaultTasks());
//		typesPage.setInput(prefs.getDefaultTypes());
//		classpathPage.performDefaults();
//		propertiesPage.performDefaults();
//	}
//	
//	/* (non-Javadoc)
//	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
//	 */
	public boolean performOk() {
//		AntCorePreferences prefs = AntCorePlugin.getPlugin().getPreferences();
//		IDialogSettings settings = AntUIPlugin.getDefault().getDialogSettings();
//		
//		prefs.setAntHomeClasspathEntries(classpathPage.getAntHomeEntries());
//		
//		
//		prefs.setAdditionalClasspathEntries(classpathPage.getAdditionalEntries());
//		
//		String antHome= classpathPage.getAntHome();
//		prefs.setAntHome(antHome);
//		
//		List contents = tasksPage.getContents(false);
//		if (contents != null) {
//			Task[] tasks = (Task[]) contents.toArray(new Task[contents.size()]);
//			prefs.setCustomTasks(tasks);
//		}
//		
//		tasksPage.saveColumnSettings(settings);
//		
//		contents = typesPage.getContents(false);
//		if (contents != null) {
//			Type[] types = (Type[]) contents.toArray(new Type[contents.size()]);
//			prefs.setCustomTypes(types);
//		}
//		
//		typesPage.saveColumnSettings(settings);
//		
//		contents = propertiesPage.getProperties();
//		if (contents != null) {
//			Property[] properties = (Property[]) contents.toArray(new Property[contents.size()]);
//			prefs.setCustomProperties(properties);
//		}
//		
//		String[] files = propertiesPage.getPropertyFiles();
//		prefs.setCustomPropertyFiles(files);
//		
//		propertiesPage.saveAdditionalSettings();
//		
//		prefs.updatePluginPreferences();
//	
		return super.performOk();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.DialogPage#setButtonLayoutData(org.eclipse.swt.widgets.Button)
	 */
	protected GridData setButtonLayoutData(Button button) {
		return super.setButtonLayoutData(button);
	}
//	
//	protected List getLibraryEntries() {
//		List urls= new ArrayList();
//		urls.addAll(Arrays.asList(classpathPage.getAntHomeEntries()));
//		urls.addAll(Arrays.asList(classpathPage.getAdditionalEntries()));
//		urls.addAll(Arrays.asList(classpathPage.getContributedEntries()));
//		return urls;
//	}

}