package com.breskeby.eclipse.gradle.launchConfigurations;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.externaltools.internal.launchConfigurations.ExternalToolsMainTab;
import org.eclipse.ui.externaltools.internal.model.IExternalToolConstants;
import org.eclipse.ui.externaltools.internal.ui.FileSelectionDialog;

import com.breskeby.eclipse.gradle.util.GradleUtil;

public class GradleMainTab extends ExternalToolsMainTab {

	private String fCurrentLocation= null;
	private Button fSetInputHandlerButton;
    private IFile fNewFile;

	private void setMappedResources(ILaunchConfigurationWorkingCopy configuration) {
		IFile file= getIFile(configuration);
		configuration.setMappedResources(new IResource[] {file});
	}

	private void updateProjectName(ILaunchConfigurationWorkingCopy configuration) {
        IFile file = getIFile(configuration);
        String projectName= ""; //$NON-NLS-1$
        if (file != null) {
            projectName= file.getProject().getName();
        }
        configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, projectName);
    }

	private IFile getIFile(ILaunchConfigurationWorkingCopy configuration) {
		IFile file= null;
        if (fNewFile != null) {
            file= fNewFile;
            fNewFile= null;
        } else {
            IStringVariableManager manager = VariablesPlugin.getDefault().getStringVariableManager();
            try {
            	String location= configuration.getAttribute(IExternalToolConstants.ATTR_LOCATION, (String)null);
                if (location != null) {
                    String expandedLocation= manager.performStringSubstitution(location);
                    if (expandedLocation != null) {
                        file= GradleUtil.getFileForLocation(expandedLocation, null);
                    }
                }
            } catch (CoreException e) {
            }
        }
		return file;
	}

    /* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		setControl(mainComposite);
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(mainComposite, IAntUIHelpContextIds.ANT_MAIN_TAB);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		mainComposite.setLayout(layout);
		mainComposite.setLayoutData(gridData);
		mainComposite.setFont(parent.getFont());
		createLocationComponent(mainComposite);
		createWorkDirectoryComponent(mainComposite);
		createArgumentComponent(mainComposite);
		createVerticalSpacer(mainComposite, 2);
		createSetInputHandlerComponent(mainComposite);
		Dialog.applyDialogFont(parent);
	}
	
	/**
	 * Creates the controls needed to edit the set input handler attribute of an
	 * Ant build
	 *
	 * @param parent the composite to create the controls in
	 */
	private void createSetInputHandlerComponent(Composite parent) {
//		fSetInputHandlerButton = createCheckButton(parent, AntLaunchConfigurationMessages.AntMainTab_0);
//		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
//		data.horizontalSpan = 2;
//		fSetInputHandlerButton.setLayoutData(data);
//		fSetInputHandlerButton.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				updateLaunchConfigurationDialog();
//			}
//		});
	}
	
//	private void (ILaunchConfiguration configuration) {
//		boolean setInputHandler= true;
//		try {
//			setInputHandler= configuration.getAttribute(IAntUIConstants.SET_INPUTHANDLER, true);
//		} catch (CoreException ce) {
//			AntUIPlugin.log(AntLaunchConfigurationMessages.AntMainTab_1, ce);
//		}updateCheckButtons
//		fSetInputHandlerButton.setSelection(setInputHandler);
//	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.externaltools.internal.launchConfigurations.ExternalToolsMainTab#handleWorkspaceLocationButtonSelected()
	 */
	protected void handleWorkspaceLocationButtonSelected() {
		FileSelectionDialog dialog;
		dialog = new FileSelectionDialog(getShell(), ResourcesPlugin.getWorkspace().getRoot(), GradleLaunchConfigurationMessages.GradleMainTab__Select_a_build_file__1);
		dialog.open();
		IStructuredSelection result = dialog.getResult();
		if (result == null) {
			return;
		}
		Object file= result.getFirstElement();
		if (file instanceof IFile) {
            fNewFile= (IFile)file;
			locationField.setText(VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression("workspace_loc", fNewFile.getFullPath().toString())); //$NON-NLS-1$
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.externaltools.internal.launchConfigurations.ExternalToolsMainTab#getLocationLabel()
	 */
	protected String getLocationLabel() {
		return GradleLaunchConfigurationMessages.GradleMainTab_1;
	}
}
