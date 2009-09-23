package com.breskeby.eclipse.gradle.preferences;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public interface IGradleBlockContainer {

/**
 * Sets the error message for the container.
 * May be <code>null</code> to remove the error message.
 * 
 * @param message A string error message or <code>null</code>
 */
public void setErrorMessage(String message);

/**
 * Sets the message for the container.
 * May be <code>null</code> to remove the message.
 * 
 * @param message A string message or <code>null</code>
 */
public void setMessage(String message);

/**
 * Creates and returns a properly configured push button with 
 * the supplied label
 * 
 * @param parent The composite parent of the button
 * @param label The button label
 * 
 * @return button the created button
 */
public Button createPushButton(Composite parent, String label);

/**
 * Notifies the container that state has changed.
 */
public void update();

}
