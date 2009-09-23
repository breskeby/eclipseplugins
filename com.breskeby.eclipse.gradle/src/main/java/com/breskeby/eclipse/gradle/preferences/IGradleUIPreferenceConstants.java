package com.breskeby.eclipse.gradle.preferences;

public class IGradleUIPreferenceConstants {

	public static final String GRADLEVIEW_INCLUDE_ERROR_SEARCH_RESULTS = "gradleview.includeErrorSearchResults"; //$NON-NLS-1$
	public static final String GRADLEVIEW_LAST_SEARCH_STRING = "gradleview.lastSearchString"; //$NON-NLS-1$
	public static final String GRADLEVIEW_LAST_WORKINGSET_SEARCH_SCOPE = "gradleview.lastSearchScope"; //$NON-NLS-1$
	public static final String GRADLEVIEW_USE_WORKINGSET_SEARCH_SCOPE = "gradleview.useWorkingSetSearchScope"; //$NON-NLS-1$
	
	public static final String GRADLE_FIND_BUILD_FILE_NAMES = "gradle.findBuildFileNames"; //$NON-NLS-1$
	
	/**
	 * The symbolic names for colors for displaying the content in the Console
	 * @see org.eclipse.jface.resource.ColorRegistry
	 */
	public static final String CONSOLE_ERROR_COLOR = "com.breskeby.eclipse.gradle.preferences.errorColor"; //$NON-NLS-1$
	public static final String CONSOLE_WARNING_COLOR = "com.breskeby.eclipse.gradle.preferences.warningColor"; //$NON-NLS-1$
	public static final String CONSOLE_INFO_COLOR = "com.breskeby.eclipse.gradle.preferences.informationColor"; //$NON-NLS-1$
	public static final String CONSOLE_VERBOSE_COLOR = "com.breskeby.eclipse.gradle.preferences.verboseColor"; //$NON-NLS-1$
	public static final String CONSOLE_DEBUG_COLOR = "com.breskeby.eclipse.gradle.preferences.debugColor"; //$NON-NLS-1$	
	
	public static final String GRADLE_TOOLS_JAR_WARNING= "toolsJAR"; //$NON-NLS-1$
    
    /**
     * int preference identifier constant which specifies the length of time to wait
     * to connect with the socket that communicates with the separate JRE to capture the output
     */
    public static final String GRADLE_COMMUNICATION_TIMEOUT= "timeout"; //$NON-NLS-1$
	
	public static final String GRADLE_ERROR_DIALOG= "errorDialog"; //$NON-NLS-1$
	
	/**
	 * Boolean preference identifier constant which specifies whether to create Java problem markers
	 * from the javac output of Ant builds.
	 */
	public static final String GRADLE_CREATE_MARKERS= "createMarkers"; //$NON-NLS-1$
	
	/**
	 * Boolean preference identifier constant which specifies whether the Ant editor should
	 * show internal targets in the Outline.
	 */
	public static final String GRADLEEDITOR_FILTER_INTERNAL_TARGETS= "gradleeditor.filterInternalTargets"; //$NON-NLS-1$
	
	/**
	 * Boolean preference identifier constant which specifies whether the Ant editor should
	 * show imported elements in the Outline.
	 */
	public static final String GRADLEEDITOR_FILTER_IMPORTED_ELEMENTS = "gradleeditor.filterImportedElements"; //$NON-NLS-1$
	
	/**
	 * Boolean preference identifier constant which specifies whether the Ant editor should
	 * show properties in the Outline.
	 */
	public static final String GRADLEEDITOR_FILTER_PROPERTIES= "gradleeditor.filterProperties"; //$NON-NLS-1$
	
	/**
	 * Boolean preference identifier constant which specifies whether the Ant editor should
	 * show top level tasks/types in the Outline.
	 */
	public static final String GRADLEEDITOR_FILTER_TOP_LEVEL= "gradleeditor.filterTopLevel"; //$NON-NLS-1$
	
	/**
	 * Boolean preference identifier constant which specifies whether the Ant editor should
	 * sort elements in the Outline.
	 */
	public static final String GRADLEEDITOR_SORT= "gradleeditor.sort"; //$NON-NLS-1$
	
	/**
	 * Boolean preference identifier constant which specifies whether the Ant Outline page 
	 * links its selection to the active Ant editor.
	 */
	public static final String OUTLINE_LINK_WITH_EDITOR= "outline.linkWithEditor"; //$NON-NLS-1$
	
	/**
	 * String preference identifier constant which specifies the URL for the location of the 
	 * Ant documentation.
	 */
	public static final String DOCUMENTATION_URL = "documentation.url"; //$NON-NLS-1$
	
	
	
	// default values
	public static final String DEFAULT_BUILD_FILENAME = "build.xml"; //$NON-NLS-1$

	// preferences
	public static final String PREFERENCE_TASKS = "tasks"; //$NON-NLS-1$
	public static final String PREFERENCE_TYPES = "types"; //$NON-NLS-1$
	
	public static final String PREFIX_TASK = "task."; //$NON-NLS-1$
	public static final String PREFIX_TYPE = "type."; //$NON-NLS-1$
	
	/**
	 * Preferences
	 * @since 3.0
	 */
	public static final String PREFERENCE_GRADLE_HOME_ENTRIES = "gradle_home_entries"; //$NON-NLS-1$
	public static final String PREFERENCE_ADDITIONAL_ENTRIES = "additional_entries"; //$NON-NLS-1$
	
	public static final String PREFERENCE_CLASSPATH_CHANGED = "classpath_changed"; //$NON-NLS-1$
	
	/**
	 * Preferences
	 * @since 2.1
	 */
	public static final String PREFERENCE_GRADLE_HOME = "gradle_home"; //$NON-NLS-1$
	public static final String PREFERENCE_PROPERTIES = "properties"; //$NON-NLS-1$
	public static final String PREFERENCE_PROPERTY_FILES = "propertyfiles"; //$NON-NLS-1$
	public static final String PREFIX_PROPERTY = "property."; //$NON-NLS-1$

}
