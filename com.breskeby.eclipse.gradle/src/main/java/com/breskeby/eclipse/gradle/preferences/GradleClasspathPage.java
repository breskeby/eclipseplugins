package com.breskeby.eclipse.gradle.preferences;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.breskeby.eclipse.gradle.GradlePlugin;

public class GradleClasspathPage implements IGradleBlockContainer {

	private GradleClasspathBlock fGradleClasspathBlock= new GradleClasspathBlock();
	private GradleRuntimePreferencePage fPreferencePage;
	private ClasspathModel fModel;
	
	/**
	 * Creates an instance.
	 */
	public GradleClasspathPage(GradleRuntimePreferencePage preferencePage) {
		fPreferencePage = preferencePage;
	}
	
	/**
	 * Returns the specified user classpath entries
	 * 
	 * @return set of user classpath entries
	 */
	protected IGradleClasspathEntry[] getAdditionalEntries() {
		return fModel.getEntries(ClasspathModel.GLOBAL_USER);
	}
	
	/**
	 * Returns the specified ant home classpath entries
	 */
	protected IGradleClasspathEntry[] getGradleHomeEntries() {
		return fModel.getEntries(ClasspathModel.GRADLE_HOME);
	}
	
	/**
	 * Returns the contributed classpath entries
	 */
	protected IGradleClasspathEntry[] getContributedEntries() {
		return fModel.getEntries(ClasspathModel.CONTRIBUTED);
	}
	
	protected String getGradleHome() {
		return fGradleClasspathBlock.getGradleHome();
	}
	
	/**
	 * Sets the contents of the tables on this page.
	 */
	protected void initialize() {
		
		GradlePreferences prefs= GradlePlugin.getPlugin().getPreferences();
		createClasspathModel();
		fGradleClasspathBlock.initializeGradleHome(prefs.getGradleHome());
		fGradleClasspathBlock.setInput(fModel);
		
		fPreferencePage.setErrorMessage(null);
		fPreferencePage.setValid(true);
	}
	
	protected void createClasspathModel() {
		fModel= new ClasspathModel();
		GradlePreferences prefs= GradlePlugin.getPlugin().getPreferences();
		fModel.setGradleHomeEntries(prefs.getGradleHomeClasspathEntries());
//		fModel.setGlobalEntries(prefs.getAdditionalClasspathEntries());
        fModel.setContributedEntries(prefs.getContributedClasspathEntries());
	}
	
	protected void performDefaults() {
		GradlePreferences prefs= GradlePlugin.getPlugin().getPreferences();
		fModel= new ClasspathModel();
		fModel.setGradleHomeEntries(prefs.getDefaultGradleHomeEntries());
		List additionalEntries= getDefaultAdditionalEntries();
		if (additionalEntries != null) {
			fModel.setGlobalEntries((IGradleClasspathEntry[]) additionalEntries.toArray(new IGradleClasspathEntry[additionalEntries.size()]));
		} else {
			fModel.setGlobalEntries(new IGradleClasspathEntry[0]);
		}
        fModel.setContributedEntries(prefs.getContributedClasspathEntries());
		fGradleClasspathBlock.initializeGradleHome(prefs.getDefaultGradleHome());
		fGradleClasspathBlock.setInput(fModel);
		update();
	}
	
	private List getDefaultAdditionalEntries() {
		IGradleClasspathEntry toolsJarEntry= GradlePlugin.getPlugin().getPreferences().getToolsJarEntry();
		File libDir= new File(System.getProperty("user.home"), ".ant" + File.separatorChar + "lib"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		URL[] urls= null;
		try {
			urls= getLocationURLs(libDir);
		} catch (MalformedURLException e) {
            GradlePlugin.log(e);
            return new ArrayList(0);
		}
		
		List entries= new ArrayList(urls.length);
		for (int i = 0; i < urls.length; i++) {
			GradleClasspathEntry entry= new GradleClasspathEntry(urls[i]);
			entries.add(entry);
		}
		if (toolsJarEntry != null) {
			entries.add(toolsJarEntry);
		}
		return entries;
	}
	
	private URL[] getLocationURLs(File location) throws MalformedURLException {
		 final String extension= ".jar"; //$NON-NLS-1$
		 URL[] urls = new URL[0];
		 
		 if (!location.exists()) {
			 return urls;
		 }
		 
		 if (!location.isDirectory()) {
			 urls = new URL[1];
			 String path = location.getPath();
			 if (path.toLowerCase().endsWith(extension)) {
				 urls[0] = location.toURL();
			 }
			 return urls;
		 }
		 
		 File[] matches = location.listFiles(
			 new FilenameFilter() {
				 public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(extension);
				 }
			 });
		 
		 urls = new URL[matches.length];
		 for (int i = 0; i < matches.length; ++i) {
			 urls[i] = matches[i].toURL();
		 }
		 return urls;
	 }
	
	/**
	 * Creates the tab item that contains this sub-page.
	 */
	protected TabItem createTabItem(TabFolder folder) {
		TabItem item = new TabItem(folder, SWT.NONE);
		item.setText(GradlePreferencesMessages.GradleClasspathPage_title);
		item.setImage(fGradleClasspathBlock.getClasspathImage());
		item.setData(this);
		item.setControl(createContents(folder));
		return item;
	}
	
	/**
	 * Creates this page's controls
	 */
	protected Composite createContents(Composite parent) {
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IAntUIHelpContextIds.ANT_CLASSPATH_PAGE);
		Font font = parent.getFont();
		
		Composite top = new Composite(parent, SWT.NONE);
		top.setFont(font);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 2;
		layout.marginWidth = 2;
		top.setLayout(layout);

		top.setLayoutData(new GridData(GridData.FILL_BOTH));

		fGradleClasspathBlock.setContainer(this);
		fGradleClasspathBlock.createContents(top);
		
		return top;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ant.internal.ui.preferences.IAntBlockContainer#update()
	 */
	public void update() {
		if (fGradleClasspathBlock.isValidated()){
			return;
		}
		setMessage(null);
		setErrorMessage(null);
		boolean valid= fGradleClasspathBlock.validateAntHome();
	
		if (valid) {
			valid= fGradleClasspathBlock.validateToolsJAR();
		}
		
		fPreferencePage.setValid(valid);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ant.internal.ui.preferences.IAntBlockContainer#setMessage(java.lang.String)
	 */
	public void setMessage(String message) {
		fPreferencePage.setMessage(message);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ant.internal.ui.preferences.IAntBlockContainer#setErrorMessage(java.lang.String)
	 */
	public void setErrorMessage(String message) {
		fPreferencePage.setErrorMessage(message);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ant.internal.ui.preferences.IAntBlockContainer#createPushButton(org.eclipse.swt.widgets.Composite, java.lang.String)
	 */
	public Button createPushButton(Composite parent, String buttonText) {
		Button button = new Button(parent, SWT.PUSH);
		button.setFont(parent.getFont());
		button.setText(buttonText);
		fPreferencePage.setButtonLayoutData(button);
		return button;
	}
}
