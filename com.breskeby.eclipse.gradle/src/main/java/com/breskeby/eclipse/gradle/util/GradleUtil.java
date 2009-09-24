package com.breskeby.eclipse.gradle.util;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.util.FileUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class GradleUtil {
	/**
	 * Return a buildfile from the specified location.
	 * If there isn't one return null.
	 */
	private static File getBuildFile(String path) {
		File buildFile = new File(path);
		if (!buildFile.isFile() || !buildFile.exists()) { 
			return null;
		}

		return buildFile;
	}
	
	/**
	 * Returns the workspace file associated with the given path in the
	 * local file system, or <code>null</code> if none.
	 * If the path happens to be a relative path, then the path is interpreted as
	 * relative to the specified parent file.
	 * 
	 * Attempts to handle linked files; the first found linked file with the correct
	 * path is returned.
	 *   
	 * @param path
	 * @param buildFileParent
	 * @return file or <code>null</code>
	 * @see org.eclipse.core.resources.IWorkspaceRoot#findFilesForLocation(IPath)
	 */
	public static IFile getFileForLocation(String path, File buildFileParent) {
		if (path == null) {
			return null;
		}
		IPath filePath= new Path(path);
		IFile file = null;
		IFile[] files= ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(filePath);
		if (files.length > 0) {
			file= files[0];
		}
		if (file == null) {
			//relative path
			File relativeFile= null;
			try {
				//this call is ok if buildFileParent is null
				relativeFile= FileUtils.getFileUtils().resolveFile(buildFileParent, path);
				filePath= new Path(relativeFile.getAbsolutePath());
				files= ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(filePath);
				if (files.length > 0) {
					file= files[0];
				} else {
					return null;
				}
			} catch (Exception be) {
				return null;
			}
		}
		
		if (file.exists()) {
			return file;
		} 
		File ioFile= file.getLocation().toFile();
		if (ioFile.exists()) {//needs to handle case insensitivity on WINOS
			try {
				files= ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(new Path(ioFile.getCanonicalPath()));
				if (files.length > 0) {
					return files[0];
				}
			} catch (IOException e) {
			}			
		}
			
		return null;
	}

}
