package com.breskeby.eclipse.gradle.launchConfigurations;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationMigrationDelegate;

/**
 * Delegate for migrating Gradle launch configurations.
 */

public class GradleMigrationDelegate implements ILaunchConfigurationMigrationDelegate {
	
//	/**
//	 * Method to get the file for the specified launch configuration that should be mapped to the launch configuration  
//	 * 
//	 * @param candidate the launch configuration that the file will be mapped to.
//	 * @return the buildfile or <code>null</code> if not in the workspace
//	 */
//	protected IFile getFileForCandidate(ILaunchConfiguration candidate) {
//		IFile file= null;
//		String expandedLocation= null;
//		String location= null;
//		IStringVariableManager manager = VariablesPlugin.getDefault().getStringVariableManager();
//		try {
//			location= candidate.getAttribute(IExternalToolConstants.ATTR_LOCATION, (String)null);
//			if (location != null) {
//				expandedLocation= manager.performStringSubstitution(location);
//				if (expandedLocation != null) {
//					file= AntUtil.getFileForLocation(expandedLocation, null);
//				}
//			}
//		} catch (CoreException e) {
//		}
//		return file;
//	}
//	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.ILaunchConfigurationMigrationDelegate#isCandidate()
	 */
	public boolean isCandidate(ILaunchConfiguration candidate) throws CoreException {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.ILaunchConfigurationMigrationDelegate#migrate(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void migrate(ILaunchConfiguration candidate) throws CoreException {
//		IFile file = getFileForCandidate(candidate);
//		ILaunchConfigurationWorkingCopy wc = candidate.getWorkingCopy();
//		wc.setMappedResources(new IResource[] {file});
//		wc.doSave();
	}
}