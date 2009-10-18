package com.breskeby.eclipse.gradle.launchConfigurations;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.ILaunchShortcut2;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.externaltools.internal.launchConfigurations.ExternalToolsUtil;
import org.eclipse.ui.externaltools.internal.model.IExternalToolConstants;

import com.breskeby.eclipse.gradle.GradlePlugin;
import com.breskeby.eclipse.gradle.IGradleConstants;
import com.breskeby.eclipse.gradle.preferences.IGradlePreferenceConstants;
import com.breskeby.eclipse.gradle.util.GradleUtil;
import com.ibm.icu.text.MessageFormat;

/**
 * This class provides the Run/Debug As -> Gradle Build launch shortcut.
 * 
 */
@SuppressWarnings("restriction")
public class GradleLaunchShortcut implements ILaunchShortcut2 {

	private boolean fShowDialog= false;
	private static final int MAX_TARGET_APPEND_LENGTH = 30;
	private static final String DEFAULT_TARGET = "default"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.jface.viewers.ISelection, java.lang.String)
	 */
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection)selection;
			Object object = structuredSelection.getFirstElement();
			if (object instanceof IAdaptable) {
				IResource resource = (IResource)((IAdaptable)object).getAdapter(IResource.class);
				if (resource != null) {
					if (!("gradle".equalsIgnoreCase(resource.getFileExtension()))) { //$NON-NLS-1$
						if (resource.getType() == IResource.FILE) {
							resource = resource.getParent();
						}
						resource = findBuildFile((IContainer)resource);
					} 
					if (resource != null) {
						IFile file = (IFile) resource;
						launch(file.getFullPath(), file.getProject(), mode, null);
						return;
					}
				}
			}
		}
		gradleFileNotFound();
	}
	
	/**
	 * Launch the given targets in the given build file. The tasks are
	 * launched in the given mode.
	 * 
	 * @param filePath the path to the build file to launch
	 * @param project the project for the path
	 * @param mode the mode in which the build file should be executed
	 * @param targetAttribute the tasks to launch or <code>null</code> to use tasks on existing configuration,
	 *  or <code>DEFAULT</code> for default tasks explicitly.
	 *  
	 * configuration targets attribute.
	 */
	@SuppressWarnings("unchecked")
	public void launch(IPath filePath, IProject project, String mode, String targetAttribute) {
		ILaunchConfiguration configuration = null;
		IFile backingfile = null;
		if(project != null) {
			//need to get the full location of a workspace file to compare against the resolved config location attribute
			backingfile = project.getFile(filePath.removeFirstSegments(1));
		}
		List configs = collectConfigurations((backingfile != null && backingfile.exists() ? backingfile.getLocation() : filePath));
		if (configs.isEmpty()) {
			configuration = createDefaultLaunchConfiguration(filePath, (project != null && project.exists() ? project : null));
		} else if (configs.size() == 1) {
			configuration= (ILaunchConfiguration)configs.get(0);
		} else {
			configuration = chooseConfig(configs);
			if(configuration == null) {
				//fail gracefully if the user cancels choosing a configuration
				return;
			}
		}
		
		// set the target to run, if applicable
		if (configuration != null) {
			try {
				if (targetAttribute != null && !targetAttribute.equals(configuration.getAttribute(IGradleLaunchConfigurationConstants.ATTR_GRADLE_TARGETS, DEFAULT_TARGET))) {
					ILaunchConfigurationWorkingCopy copy = configuration.getWorkingCopy();
					String attrValue = null;
					if (!DEFAULT_TARGET.equals(targetAttribute)) {
						attrValue = targetAttribute;
					}
					copy.setAttribute(IGradleLaunchConfigurationConstants.ATTR_GRADLE_TARGETS, attrValue);
					configuration = copy.doSave();
				}
			} catch (CoreException exception) {
				reportError(MessageFormat.format(GradleLaunchConfigurationMessages.GradleLaunchShortcut_Exception_launching, new String[] {filePath.toFile().getName()}), exception);
				return;
			}
			launch(mode, configuration);
		} else {
			gradleFileNotFound();
		}
	}

	
	/**
	 * Inform the user that a gradle file was not found to run.
	 */
	private void gradleFileNotFound() {
		reportError(GradleLaunchConfigurationMessages.GradleLaunchShortcut_Unable, null);	
	}

	
	
	/**
	 * Returns a unique name for a copy of the given launch configuration with the given
	 * targets. The name seed is the same as the name for a new launch configuration with
	 *   &quot; [targetList]&quot; appended to the end.
	 * @param filePath the path to the buildfile
     * @param projectName the name of the project containing the buildfile or <code>null</code> if no project is known
	 * @param taskAttribute the listing of task to execute or <code>null</code> for default task execution
	 * @return a unique name for the copy
	 */
	public static String getNewLaunchConfigurationName(IPath filePath, String projectName, String taskAttribute) {
		StringBuffer buffer = new StringBuffer();
		if (projectName != null) {
			buffer.append(projectName);
			buffer.append(' ');
			buffer.append(filePath.lastSegment());
		} else {
			buffer.append(filePath.lastSegment());
		}
		
		if (taskAttribute != null) {
			buffer.append(" ["); //$NON-NLS-1$
			if (taskAttribute.length() > MAX_TARGET_APPEND_LENGTH + 3) {
				// The target attribute can potentially be a long, comma-separated list
				// of target. Make sure the generated name isn't extremely long.
				buffer.append(taskAttribute.substring(0, MAX_TARGET_APPEND_LENGTH));
				buffer.append("..."); //$NON-NLS-1$
			} else {
				buffer.append(taskAttribute);
			}
			buffer.append(']');
		}
		
		String name= DebugPlugin.getDefault().getLaunchManager().generateUniqueLaunchConfigurationNameFrom(buffer.toString());
		return name;
	}
	
	
	
	/**
	 * Delegate method to launch the specified <code>ILaunchConfiguration</code> in the specified mode
	 * @param mode the mode to launch in
	 * @param configuration the <code>ILaunchConfiguration</code> to launch
	 */
	@SuppressWarnings("deprecation")
	private void launch(String mode, ILaunchConfiguration configuration) {
        if (fShowDialog) {
        	
			// Offer to save dirty editors before opening the dialog as the contents
			// of an Ant editor often affect the contents of the dialog.
			if (!DebugUITools.saveBeforeLaunch()) {
				return;
			}
			@SuppressWarnings("unused")
			IStatus status = new Status(IStatus.INFO, GradlePlugin.PLUGIN_ID,IGradleConstants.STATUS_INIT_RUN_GRADLE, "BLUBB", null); //$NON-NLS-1$
			String groupId;
			if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			    groupId= IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP;
			} else {
			    groupId= IExternalToolConstants.ID_EXTERNAL_TOOLS_LAUNCH_GROUP;
			}
			DebugUITools.openLaunchConfigurationDialog(GradlePlugin.getActiveWorkbenchWindow().getShell(), configuration, groupId, null);
		} else {
			DebugUITools.launch(configuration, mode);
		}
    }

    /**
	 * Walks the file hierarchy looking for a build file.
	 * Returns the first build file found that matches the 
	 * search criteria.
	 */
	private IFile findBuildFile(IContainer parent) {
		String[] names= getBuildFileNames();
		if (names == null) {
			return null;
		}
		IContainer lparent = parent;
		IResource file= null;
		while (file == null || file.getType() != IResource.FILE) {		
			for (int i = 0; i < names.length; i++) {
				String string = names[i];
				file= lparent.findMember(string);
				if (file != null && file.getType() == IResource.FILE) {
					break;
				}
			}
			lparent = lparent.getParent();
			if (lparent == null) {
				return null;
			}
		}
		return (IFile)file;
	}
	
	/**
	 * Returns an array of build file names from the gradle preference store
	 * @return an array of build file names
	 */
	private String[] getBuildFileNames() {
		IPreferenceStore prefs= GradlePlugin.getDefault().getPreferenceStore();
		String buildFileNames= prefs.getString(IGradlePreferenceConstants.GRADLE_FIND_BUILD_FILE_NAMES);
		if (buildFileNames.length() == 0) {
			//the user has not specified any names to look for
			return null;
		}
		return GradleUtil.parseString(buildFileNames, ","); //$NON-NLS-1$
	}
	
	/**
	 * Creates and returns a default launch configuration for the given file.
	 * 
	 * @param file
	 * @return default launch configuration
	 */
	public static ILaunchConfiguration createDefaultLaunchConfiguration(IFile file) {
		return createDefaultLaunchConfiguration(file.getFullPath(), file.getProject());
	}

	/**
	 * Creates and returns a default launch configuration for the given file path
	 * and project.
	 * 
	 * @param filePath the path to the buildfile
	 * @param project the project containing the buildfile or <code>null</code> if the
	 * buildfile is not contained in a project (is external).
	 * @return default launch configuration or <code>null</code> if one could not be created
	 */
	public static ILaunchConfiguration createDefaultLaunchConfiguration(IPath filePath, IProject project) {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(IGradleLaunchConfigurationConstants.ID_GRADLE_LAUNCH_CONFIGURATION_TYPE);
				
		String projectName= project != null ? project.getName() : null;
		String name = getNewLaunchConfigurationName(filePath, projectName, null);
		try {
			ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, name);
			if (project != null) {
				workingCopy.setAttribute(IExternalToolConstants.ATTR_LOCATION,
						VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression("workspace_loc", filePath.toString())); //$NON-NLS-1$
			} else {
				workingCopy.setAttribute(IExternalToolConstants.ATTR_LOCATION, filePath.toString());
			}
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER, "org.eclipse.ant.ui.AntClasspathProvider"); //$NON-NLS-1$
			// set default for common settings
			CommonTab tab = new CommonTab();
			tab.setDefaults(workingCopy);
			tab.dispose();
			
			//set the project name so that the correct default VM install can be determined
			if (project != null) {
				workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, project.getName());
			}
			
			IFile file= GradleUtil.getFileForLocation(filePath.toString(), null);
			workingCopy.setMappedResources(new IResource[] {file});
			
			return workingCopy.doSave();
		} catch (CoreException e) {
			reportError(MessageFormat.format(GradleLaunchConfigurationMessages.GradleLaunchShortcut_2, new String[]{filePath.toString()}), e);
		}
		return null;
	}
	
	/**
	 * Returns a list of existing launch configuration for the given file.
	 * 
	 * @param file the buildfile resource
	 * @return list of launch configurations
	 */
	@SuppressWarnings("unchecked")
	public static List findExistingLaunchConfigurations(IFile file) {
		List validConfigs = new ArrayList();
		if(file != null) {
			IPath filePath = file.getLocation();
			if(filePath != null) {
				ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
				ILaunchConfigurationType type = manager.getLaunchConfigurationType(IGradleLaunchConfigurationConstants.ID_GRADLE_LAUNCH_CONFIGURATION_TYPE);
				if (type != null) {
					try {
						ILaunchConfiguration[] configs = manager.getLaunchConfigurations(type);
						for (int i = 0; i < configs.length; i++) {
							try {
								if (filePath.equals(ExternalToolsUtil.getLocation(configs[i]))) {
									validConfigs.add(configs[i]);
								}
							}
							catch(CoreException ce) {}
						}
					} catch (CoreException e) {
						reportError(GradleLaunchConfigurationMessages.GradleLaunchShortcut_3, e);
					}
				}
			}
		}
		return validConfigs;
	}
	
	/**
	 * Prompts the user to choose from the list of given launch configurations
	 * and returns the config the user choose or <code>null</code> if the user
	 * pressed Cancel or if the given list is empty.
	 */
	public static ILaunchConfiguration chooseConfig(List configs) {
		if (configs.isEmpty()) {
			return null;
		}
		ILabelProvider labelProvider = DebugUITools.newDebugModelPresentation();
		ElementListSelectionDialog dialog= new ElementListSelectionDialog(Display.getDefault().getActiveShell(), labelProvider);
		dialog.setElements(configs.toArray(new ILaunchConfiguration[configs.size()]));
		dialog.setTitle(GradleLaunchConfigurationMessages.GradleLaunchShortcut_4);
		dialog.setMessage(GradleLaunchConfigurationMessages.GradleLaunchShortcut_5);
		dialog.setMultipleSelection(false);
		int result = dialog.open();
		labelProvider.dispose();
		if (result == Window.OK) {
			return (ILaunchConfiguration) dialog.getFirstResult();
		}
		return null;
	}

	/**
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.ui.IEditorPart, java.lang.String)
	 */
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
		IFile file = (IFile)input.getAdapter(IFile.class);
		IPath filepath = null;
		if (file != null) {
			filepath = file.getFullPath();
		}
		if(filepath == null) {
		    ILocationProvider locationProvider= (ILocationProvider)input.getAdapter(ILocationProvider.class);
		    if (locationProvider != null) {
				filepath = locationProvider.getPath(input);
			}
		}
		if(filepath != null && "gradle".equals(filepath.getFileExtension())) { //$NON-NLS-1$
//			TODO
//			launch(filepath, (file == null ? null : file.getProject()), mode, null);
			return;
		}
		if (file != null) {
			file = findBuildFile(file.getParent());
			if (file != null) {
				//launch
//				launch(file.getFullPath(), file.getProject(), mode, null);
				return;
			}
		}
		gradleFileNotFound();
	}
	
	/**
	 * Opens an error dialog presenting the user with the specified message and throwable
	 * @param message
	 * @param throwable
	 */
	protected static void reportError(String message, Throwable throwable) {
		IStatus status = null;
		if (throwable instanceof CoreException) {
			status = ((CoreException)throwable).getStatus();
		} else {
			status = new Status(IStatus.ERROR, IGradleConstants.PLUGIN_ID, 0, message, throwable);
		}
		ErrorDialog.openError(GradlePlugin.getActiveWorkbenchWindow().getShell(), GradleLaunchConfigurationMessages.GradleLaunchShortcut_Error_7, GradleLaunchConfigurationMessages.GradleLaunchShortcut_Build_Failed_2, status); 
	}

	/**
	 * Sets whether to show the external tools launch configuration dialog
	 * 
	 * @param showDialog If true the launch configuration dialog will always be
	 * 			shown
	 */
	public void setShowDialog(boolean showDialog) {
		fShowDialog = showDialog;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchShortcut2#getLaunchConfigurations(org.eclipse.jface.viewers.ISelection)
	 */
	public ILaunchConfiguration[] getLaunchConfigurations(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection)selection;
			Object object = structuredSelection.getFirstElement();
			if (object instanceof IAdaptable) {
				IResource resource = (IResource)((IAdaptable)object).getAdapter(IResource.class);
				if (resource != null) {
					if (!("gradle".equalsIgnoreCase(resource.getFileExtension()))) { //$NON-NLS-1$
						if (resource.getType() == IResource.FILE) {
							resource = resource.getParent();
						}
						resource = findBuildFile((IContainer)resource);
					} 
					if (resource != null) {
						IPath location = ((IFile) resource).getLocation();
						if (location != null) {
							List list = collectConfigurations(location);
							return (ILaunchConfiguration[]) list.toArray(new ILaunchConfiguration[list.size()]);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns a listing of <code>ILaunchConfiguration</code>s that correspond to the specified build file.
	 * 
	 * @param filepath the path to the buildfile to launch
	 * @return the list of <code>ILaunchConfiguration</code>s that correspond to the specified build file.
	 * 
	 * @since 3.4
	 */
	@SuppressWarnings("unchecked")
	protected List collectConfigurations(IPath filepath) {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(IGradleLaunchConfigurationConstants.ID_GRADLE_LAUNCH_CONFIGURATION_TYPE);
		if(type != null) {
			try {
				ILaunchConfiguration[] configs = manager.getLaunchConfigurations(type);
				List list = new ArrayList();
				IPath location = null;
				for(int i = 0; i < configs.length; i++) {
					if(configs[i].exists()) {
						try {
							location = ExternalToolsUtil.getLocation(configs[i]);
							if(location != null && location.equals(filepath)) {
								list.add(configs[i]);
							}
						}
						catch(CoreException ce) {}
					}
				}
				return list;
			} catch (CoreException e) {}
		}
		return new ArrayList();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchShortcut2#getLaunchConfigurations(org.eclipse.ui.IEditorPart)
	 */
	public ILaunchConfiguration[] getLaunchConfigurations(IEditorPart editor) {
		IEditorInput input = editor.getEditorInput();
		IFile file = (IFile)input.getAdapter(IFile.class);
		IPath filepath = null;
		if (file != null) {
			filepath = file.getLocation();
		}
		if(filepath == null) {
		    ILocationProvider locationProvider= (ILocationProvider)input.getAdapter(ILocationProvider.class);
		    if (locationProvider != null) {
				filepath = locationProvider.getPath(input);
			}
		}
		if(filepath != null && "xml".equals(filepath.getFileExtension())) { //$NON-NLS-1$
			List list = collectConfigurations(filepath);
			return (ILaunchConfiguration[]) list.toArray(new ILaunchConfiguration[list.size()]);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchShortcut2#getLaunchableResource(org.eclipse.jface.viewers.ISelection)
	 */
	public IResource getLaunchableResource(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection)selection;
			Object object = structuredSelection.getFirstElement();
			if (object instanceof IAdaptable) {
				IResource resource = (IResource)((IAdaptable)object).getAdapter(IResource.class);
				if (resource != null) {
					if (!("xml".equalsIgnoreCase(resource.getFileExtension()))) { //$NON-NLS-1$
						if (resource.getType() == IResource.FILE) {
							resource = resource.getParent();
						}
						resource = findBuildFile((IContainer)resource);
					} 
					return resource;
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchShortcut2#getLaunchableResource(org.eclipse.ui.IEditorPart)
	 */
	public IResource getLaunchableResource(IEditorPart editor) {
		IEditorInput input = editor.getEditorInput();
		return (IFile)input.getAdapter(IFile.class);
	}
}
