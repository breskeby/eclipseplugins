/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package com.breskeby.eclipse.gradle.preferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClasspathModel extends AbstractClasspathEntry {
	
	public static final int GRADLE_HOME= 0;
	public static final int GLOBAL_USER= 1;
	public static final int USER= 2;
    public static final int CONTRIBUTED= 3;
	
	private GlobalClasspathEntries fGradleHomeEntry;
	private GlobalClasspathEntries fUserGlobalEntry;
    private GlobalClasspathEntries fContributedGlobalEntry;
	
	public Object addEntry(Object entry) {
		System.out.println(entry);
		if (entry instanceof GlobalClasspathEntries) {
			if (!fChildEntries.contains(entry)) {
				fChildEntries.add(entry);
				return entry;
			}
			return null;
		} 
		ClasspathEntry newEntry= createEntry(entry, null);
		Iterator entries= fChildEntries.iterator();
		while (entries.hasNext()) {
			Object element = entries.next();
			if (element instanceof GlobalClasspathEntries) {
				if(((GlobalClasspathEntries)element).contains(newEntry)) {
					return null;
				}
			} else if (element.equals(newEntry)) {
				return null;
			}
		}
		fChildEntries.add(newEntry);
		return newEntry;
	}
	
	public Object addEntry(int entryType, Object entry) {
		System.out.println(entry);
		
		IClasspathEntry entryParent= null;
		switch (entryType) {
			case GRADLE_HOME :
				if (fGradleHomeEntry == null) {
					String name= GradlePreferencesMessages.ClasspathModel_2;
					fGradleHomeEntry= createGlobalEntry(new IGradleClasspathEntry[0], name, false, true, GRADLE_HOME);
				}
				entryParent= fGradleHomeEntry;
				break;
			case GLOBAL_USER :
				if (fUserGlobalEntry == null) {
					String name= GradlePreferencesMessages.ClasspathModel_3;
					fUserGlobalEntry= createGlobalEntry(new IGradleClasspathEntry[0], name, true, true, GLOBAL_USER);
				}
				entryParent= fUserGlobalEntry;
				break;
            case CONTRIBUTED :
                if (fContributedGlobalEntry == null) {
                    String name= GradlePreferencesMessages.ClasspathModel_0;
                    fContributedGlobalEntry= createGlobalEntry(new IGradleClasspathEntry[0], name, true, true, CONTRIBUTED);
                }
                entryParent= fContributedGlobalEntry;
                break;
			default :
				break;
		}
			
		ClasspathEntry newEntry= createEntry(entry, entryParent);
		Iterator entries= fChildEntries.iterator();
		while (entries.hasNext()) {
			Object element = entries.next();
			if (element instanceof GlobalClasspathEntries) {
				if(((GlobalClasspathEntries)element).contains(newEntry)) {
					return null;
				}
			} else if (element.equals(newEntry)) {
				return null;
			}
		}
		if (entryParent != null) {
			((GlobalClasspathEntries)entryParent).addEntry(newEntry);
		} else {
			fChildEntries.add(newEntry);
		}
		return newEntry;		
	}
	
	/**
	 * Returns entries of the specified kind, possibly empty.
	 * 
	 * @param entryType kind of entries to retrieve
	 * @return entries of the specified kind, possibly empty
	 */
	public IGradleClasspathEntry[] getEntries(int entryType) {
		switch (entryType) {
			case GRADLE_HOME :
				if (fGradleHomeEntry != null) {
					System.out.println(fGradleHomeEntry);
					
					return fGradleHomeEntry.getEntries();
				}
				break;
			case GLOBAL_USER :
				if (fUserGlobalEntry != null) {
					return fUserGlobalEntry.getEntries();
				}
				break;
			case USER : 
				return getUserEntries();
			case CONTRIBUTED : 
				if (fContributedGlobalEntry != null) {
					return fContributedGlobalEntry.getEntries();
				}
				break;
		}
		return new IGradleClasspathEntry[0];
	}
	
	public void remove(Object entry) {
		fChildEntries.remove(entry);
		if (entry == fUserGlobalEntry) {
			fUserGlobalEntry= null;
		}
	}
	
	public ClasspathEntry createEntry(Object entry, IClasspathEntry entryParent) {
		if (entryParent == null) {
			entryParent= this;
		} 
		return new ClasspathEntry(entry, entryParent);
	}

	public void removeAll() {
		if (fGradleHomeEntry != null) {
			fGradleHomeEntry.removeAll();
		} 
		if (fUserGlobalEntry != null) {
			fUserGlobalEntry.removeAll();
		}
	}
	
	public void removeAll(int entryType) {
		switch (entryType) {
			case GRADLE_HOME :
				if (fGradleHomeEntry != null) {
					fGradleHomeEntry.removeAll();
				}
				break;
			case GLOBAL_USER :
				if (fUserGlobalEntry != null) {
					fUserGlobalEntry.removeAll();
				}
				break;
			default :
				break;
		}
	}
	
	public void removeAll(Object[] entries) {
		
		for (int i = 0; i < entries.length; i++) {
			Object object = entries[i];
			if (object instanceof ClasspathEntry) {
				IClasspathEntry entryParent= ((ClasspathEntry)object).getParent();
				if (entryParent instanceof GlobalClasspathEntries) {
					((GlobalClasspathEntries)entryParent).removeEntry((ClasspathEntry) object);
				} else {
					remove(object);
				}
			} else {
				remove(object);
			}
		}
	}

	public void setGradleHomeEntries(IGradleClasspathEntry[] entries) {
		if (fGradleHomeEntry == null) {
			String name= GradlePreferencesMessages.ClasspathModel_2;
			fGradleHomeEntry= createGlobalEntry(entries, name, false, true, GRADLE_HOME);
		} else {
			fGradleHomeEntry.removeAll();
			for (int i = 0; i < entries.length; i++) {
				fGradleHomeEntry.addEntry(new ClasspathEntry(entries[i], fGradleHomeEntry));
			}
		}
	}

	private GlobalClasspathEntries createGlobalEntry(IGradleClasspathEntry[] entries, String name, boolean canBeRemoved, boolean addEntry, int type) {
		
		GlobalClasspathEntries global= new GlobalClasspathEntries(name, this, canBeRemoved, type);
		
		for (int i = 0; i < entries.length; i++) {
			global.addEntry(new ClasspathEntry(entries[i], global));
		}
		
		if (addEntry) {
			addEntry(global);
		}
		return global;
	}

	public void setGlobalEntries(IGradleClasspathEntry[] entries) {
		if (fUserGlobalEntry == null) {
			String name= GradlePreferencesMessages.ClasspathModel_3;
			fUserGlobalEntry= createGlobalEntry(entries, name, true, true, GLOBAL_USER);
		} else {
			fUserGlobalEntry.removeAll();
			for (int i = 0; i < entries.length; i++) {
				fUserGlobalEntry.addEntry(new ClasspathEntry(entries[i], fUserGlobalEntry));
			}
		}
	}
    
    public void setContributedEntries(IGradleClasspathEntry[] entries) {
        if (fContributedGlobalEntry == null) {
            String name= GradlePreferencesMessages.ClasspathModel_0;
            fContributedGlobalEntry= createGlobalEntry(entries, name, false, true, CONTRIBUTED);
        } else {
            fContributedGlobalEntry.removeAll();
            for (int i = 0; i < entries.length; i++) {
                fContributedGlobalEntry.addEntry(new ClasspathEntry(entries[i], fContributedGlobalEntry));
            }
        }
    }
	
    private IGradleClasspathEntry[] getUserEntries() {
		List userEntries= new ArrayList(fChildEntries.size());
		Iterator itr= fChildEntries.iterator();
		while (itr.hasNext()) {
			IClasspathEntry element = (IClasspathEntry) itr.next();
			if (element instanceof GlobalClasspathEntries) {
				continue;
			}
			userEntries.add(element);
		}
		return (IGradleClasspathEntry[])userEntries.toArray(new IGradleClasspathEntry[userEntries.size()]);
	}
}
