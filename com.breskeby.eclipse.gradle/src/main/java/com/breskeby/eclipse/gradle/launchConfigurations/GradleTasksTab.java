package com.breskeby.eclipse.gradle.launchConfigurations;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.externaltools.internal.model.IExternalToolConstants;
import org.gradle.foundation.ProjectView;
import org.gradle.foundation.TaskView;

import com.breskeby.eclipse.gradle.GradleExecScheduler;
import com.breskeby.eclipse.gradle.GradleImages;
import com.breskeby.eclipse.gradle.GradlePlugin;
import com.breskeby.eclipse.gradle.IGradleConstants;
import com.breskeby.eclipse.gradle.model.GradleTaskModelContentProvider;
import com.breskeby.eclipse.gradle.model.GradleTaskModelLabelProvider;
import com.breskeby.eclipse.gradle.util.GradleUtil;

public class GradleTasksTab extends AbstractLaunchConfigurationTab implements IPropertyChangeListener {

	private CheckboxTableViewer fTableViewer = null;
	private List<TaskView> fAllTasks;
	private ILaunchConfiguration fLaunchConfiguration;
	private List<ProjectView> allProjects = null;
	private List<ProjectView> tasks;
	private List<TaskView> defaultTasks;
//	private Object absFileLocation;
	private ProjectView project;
	
	private List<TaskView> selectedTasks = new ArrayList<TaskView>();
	
	public void createControl(Composite parent) {
		
		Font font = parent.getFont();
		
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		GridLayout topLayout = new GridLayout();
		comp.setLayout(topLayout);		
		GridData gd = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(gd);
		comp.setFont(font);
		
		createTasksTable(comp);
//		createSelectionCount(comp);
		
		Composite buttonComposite= new Composite(comp, SWT.NONE);
		GridLayout layout= new GridLayout();
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttonComposite.setLayout(layout);
		buttonComposite.setFont(font);
		
//		createSortTargets(buttonComposite);
//		createFilterInternalTargets(buttonComposite);
		
		createVerticalSpacer(comp, 1);
//		createTargetOrder(comp);
		Dialog.applyDialogFont(parent);
		
	}
	
	/**
	 * Return the number of rows available in the current display using the
	 * current font.
	 * @param parent The Composite whose Font will be queried.
	 * @return int The result of the display size divided by the font size.
	 */
	private int availableRows(Composite parent) {

		int fontHeight = (parent.getFont().getFontData())[0].getHeight();
		int displayHeight = parent.getDisplay().getClientArea().height;

		return displayHeight / fontHeight;
	}
	
	/**
	 * Creates the table which displays the available tasks
	 * @param parent the parent composite
	 */
	private void createTasksTable(Composite parent) {
		Font font= parent.getFont();
		Label label = new Label(parent, SWT.NONE);
		label.setFont(font);
		label.setText(GradleLaunchConfigurationMessages.GradleTasksTab_Check_task_to_e_xecute__1);
				
		final Table table= new Table(parent, SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION );
		
		GridData data= new GridData(GridData.FILL_BOTH);
		int availableRows= availableRows(parent);
		data.heightHint = table.getItemHeight() * (availableRows / 20);
		data.widthHint = 500;
		data.minimumWidth = 500;
		
		table.setLayoutData(data);
		table.setFont(font);	
		table.setHeaderVisible(true);
		table.setLinesVisible(true);		

		TableLayout tableLayout= new TableLayout();
		ColumnWeightData weightData = new ColumnWeightData(250, true);
		tableLayout.addColumnData(weightData);
		weightData = new ColumnWeightData(250, true);
		tableLayout.addColumnData(weightData);		
		table.setLayout(tableLayout);

		final TableColumn column1= new TableColumn(table, SWT.NULL);
		column1.setText(GradleLaunchConfigurationMessages.GradleTasksTab_Name_5);
		column1.setWidth(300);
		
		final TableColumn column2= new TableColumn(table, SWT.NULL);
		column2.setText(GradleLaunchConfigurationMessages.GradleTasksTab_Description_6);
		column2.setWidth(300);

		//TableLayout only sizes columns once. If showing the tasks
		//tab as the initial tab, the dialog isn't open when the layout
		//occurs and the column size isn't computed correctly. Need to
		//recompute the size of the columns once all the parent controls 
		//have been created/sized.
		//HACK Bug 139190 
		getShell().addShellListener(new ShellAdapter() {
			public void shellActivated(ShellEvent e) {
				if(!table.isDisposed()) {
					int tableWidth = table.getSize().x;
					if (tableWidth > 0) {
						int c1 = tableWidth / 3;
						column1.setWidth(c1);
						column2.setWidth(tableWidth - c1);
					}
					getShell().removeShellListener(this);
				}
			}
		});
		
		fTableViewer = new  CheckboxTableViewer(table);

		fTableViewer.setLabelProvider(new GradleTaskModelLabelProvider());
		fTableViewer.setContentProvider(new GradleTaskModelContentProvider());
		fTableViewer.setComparator(new GradleTasksComparator());
//		
//		fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
//			public void doubleClick(DoubleClickEvent event) {
//				ISelection selection= event.getSelection();
//				if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
//					IStructuredSelection ss= (IStructuredSelection)selection;
//					Object element= ss.getFirstElement();
//					boolean checked= !fTableViewer.getChecked(element);
//					fTableViewer.setChecked(element, checked);
//					updateOrderedTargets(element, checked);
//				}
//			}
//		});
//		
		fTableViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				updateOrderedTargets(event.getElement(), event.getChecked());
			}
		});
//		
//		TableColumn[] columns= fTableViewer.getTable().getColumns();
//		for (int i = 0; i < columns.length; i++) {
//			final int index= i;
//			columns[index].addSelectionListener(new SelectionAdapter() {
//				public void widgetSelected(SelectionEvent e) {
//					if (fSortButton.getSelection()) {
//						// index 0 => sort_name (1)
//						// index 1 => sort_description (2)
//						int column= index + 1;
//						if (column == fSortDirection) {
//							column= -column; // invert the sort when the same column is selected twice in a row
//						}
//						setSort(column);
//					}
//				}
//			});
//		}
	}

	
	/**
	 * Updates the ordered targets list in response to an element being checked
	 * or unchecked. When the element is checked, it's added to the list. When
	 * unchecked, it's removed.
	 * 
	 * @param element the element in question
	 * @param checked whether the element has been checked or unchecked
	 */
	private void updateOrderedTargets(Object element , boolean checked) {
		if (checked) {
			 selectedTasks.add((TaskView)element);
		} else {
			selectedTasks.remove(element);
		}	 
		updateLaunchConfigurationDialog();	
	}
	
	/**
	 * Returns all tasks in the buildfile.
	 * @return all tasks in the buildfile
	 */
	private List<TaskView> getTasks() {
		if (fAllTasks == null || isDirty()) {
			fAllTasks= null;
			
			setDirty(false);
			setErrorMessage(null);
			setMessage(null);
			
			final CoreException[] exceptions= new CoreException[1];
			try {
				IRunnableWithProgress operation= new IRunnableWithProgress() {
					
					/* (non-Javadoc)
					 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
					 */
					@SuppressWarnings("restriction")
					public void run(IProgressMonitor monitor) {
						try {
							String variableString = fLaunchConfiguration.getAttribute(IExternalToolConstants.ATTR_LOCATION, "");
							String absFileLocation = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(variableString);
							allProjects = GradleExecScheduler.getInstance().getProjectViews(absFileLocation);
						} catch (CoreException ce) {
							exceptions[0]= ce;
						}
					}
				};
				
				IRunnableContext context= PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (context == null) {
				    context= getLaunchConfigurationDialog();
				}

				ISchedulingRule rule= null;
				if (!ResourcesPlugin.getWorkspace().isTreeLocked()) {
					//only set a scheduling rule if not in a resource change callback
					String variableString = fLaunchConfiguration.getAttribute(IExternalToolConstants.ATTR_LOCATION, "");
					String absFileLocation = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(variableString);
					rule= GradleUtil.getFileForLocation(absFileLocation, null);
				}
				PlatformUI.getWorkbench().getProgressService().runInUI(context, operation, rule);
			}catch (CoreException e) {
			    GradlePlugin.log("Internal error occurred retrieving targets", e); //$NON-NLS-1$
			    setErrorMessage(GradleLaunchConfigurationMessages.GradleTasksTab_1);
			    return null;
			} 
			catch (InvocationTargetException e) {
			    GradlePlugin.log("Internal error occurred retrieving targets", e.getTargetException()); //$NON-NLS-1$
			    setErrorMessage(GradleLaunchConfigurationMessages.GradleTasksTab_1);
			    return null;
			} catch (InterruptedException e) {
			    GradlePlugin.log("Internal error occurred retrieving targets", e); //$NON-NLS-1$
			    setErrorMessage(GradleLaunchConfigurationMessages.GradleTasksTab_1);
			    return null;
			}
			
			if (exceptions[0] != null) {
				IStatus exceptionStatus= exceptions[0].getStatus();
				IStatus[] children= exceptionStatus.getChildren();
				StringBuffer message= new StringBuffer(exceptions[0].getMessage());
				for (int i = 0; i < children.length; i++) {
					message.append(' ');
					IStatus childStatus = children[i];
					message.append(childStatus.getMessage());
				}
				setErrorMessage(message.toString());
				return new ArrayList<TaskView>();
			}
			
			if (allProjects == null) {
			    //if an error was not thrown during parsing then having no task is valid
			    return  new ArrayList<TaskView>();
			}
			
			project = allProjects.get(0);
			defaultTasks = project.getDefaultTasks();
			fAllTasks = project.getTasks();
		}
		
		return fAllTasks;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return GradleLaunchConfigurationMessages.GradleTasksTab_1;
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getImage()
	 */
	public Image getImage() {
		return GradleImages.getImage(IGradleConstants.IMG_TAB_GRADLE_TASKS);
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		fLaunchConfiguration= configuration;
		fAllTasks = getTasks();
		fTableViewer.setInput(project);
		
		//change rows with defaulttasks checked
		if(defaultTasks!=null){
			for(TaskView defTask : defaultTasks){
				fTableViewer.setChecked(defTask, true);
			}			
		}
		fTableViewer.refresh();
	}
	
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		//build tasks string
		StringBuilder sb = new StringBuilder();
		for(TaskView task : selectedTasks){
			sb.append(task.toString()).append(" ");
		}
		
		configuration.setAttribute(IGradleConstants.GRADLE_TASKS_ATTRIBUTES, sb.toString().trim());

	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		//change rows with defaulttasks checked
		for(TaskView defTask : defaultTasks){
			fTableViewer.setChecked(defTask, true);
		}
	}

	
	public void propertyChange(PropertyChangeEvent event) {
		
	}
}
