package com.breskeby.eclipse.gradle.launchConfigurations;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener;

public class GradleBuildListener implements ILaunchesListener {

	private ILaunch fLaunch;

	public GradleBuildListener(ILaunch launch) {
        super();
        fLaunch= launch;
        DebugPlugin.getDefault().getLaunchManager().addLaunchListener(this);
    }

	public void launchesAdded(ILaunch[] launches) {
		// TODO Auto-generated method stub
		System.out.println("launch added: ");
		for(ILaunch launch : launches){
					System.out.println(launch);
		}
	}

	public void launchesChanged(ILaunch[] launches) {
			System.out.println("launch changed: ");
			for(ILaunch launch : launches){
						System.out.println(launch);
			}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.ILaunchesListener#launchesRemoved(org.eclipse.debug.core.ILaunch[])
	 */
	public void launchesRemoved(ILaunch[] launches) {
	    for (int i = 0; i < launches.length; i++) {
	        ILaunch launch = launches[i];
	        if (launch.equals(fLaunch)) {
//	            shutDown();
	            return;
	        }
	    }
	}
 
}
