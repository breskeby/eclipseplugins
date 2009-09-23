package com.breskeby.eclipse.gradle.preferences;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.breskeby.eclipse.gradle.GradlePlugin;
import com.breskeby.eclipse.gradle.ui.TabFolderLayout;

public class GradleRuntimePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	
	
	private GradleClasspathPage classpathPage;
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
		initializeDialogUnits(parent);
//
		TabFolder folder = new TabFolder(parent, SWT.NONE);
		folder.setLayout(new TabFolderLayout());	
		folder.setLayoutData(new GridData(GridData.FILL_BOTH));
		folder.setFont(parent.getFont());

		classpathPage = new GradleClasspathPage(this);
		classpathPage.createTabItem(folder);
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
		classpathPage.initialize();
//		propertiesPage.initialize();
//
		return folder;
	}
//	
//	/* (non-Javadoc)
//	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
//	 */
	protected void performDefaults() {
		super.performDefaults();
		
		GradlePreferences prefs = GradlePlugin.getPlugin().getPreferences();
//		tasksPage.setInput(prefs.getDefaultTasks());
//		typesPage.setInput(prefs.getDefaultTypes());
		classpathPage.performDefaults();
//		propertiesPage.performDefaults();
	}
//	
//	/* (non-Javadoc)
//	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
//	 */
	public boolean performOk() {
		GradlePreferences prefs = GradlePlugin.getPlugin().getPreferences();
		IDialogSettings settings = GradlePlugin.getDefault().getDialogSettings();
		
		prefs.setGradleHomeClasspathEntries(classpathPage.getGradleHomeEntries());
		
		
//		prefs.setAdditionalClasspathEntries(classpathPage.getAdditionalEntries());
		
		String gradleHome= classpathPage.getGradleHome();
		prefs.setGradleHome(gradleHome);
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
		prefs.updatePluginPreferences();
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
	protected List getLibraryEntries() {
		List urls= new ArrayList();
		urls.addAll(Arrays.asList(classpathPage.getGradleHomeEntries()));
//		urls.addAll(Arrays.asList(classpathPage.getAdditionalEntries()));
//		urls.addAll(Arrays.asList(classpathPage.getContributedEntries()));
		return urls;
	}

}