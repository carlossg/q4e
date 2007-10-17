/***************************************************************************************************
 * Copyright (c) 2007 DevZuz, Inc. (AKA Simula Labs, Inc.) All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **************************************************************************************************/
package org.devzuz.q.maven.jdt.ui.actions;

import org.devzuz.q.maven.jdt.core.MavenNatureHelper;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionDelegate;

public class MavenDisableManageDependenciesAction
    extends AbstractMavenAction
    implements IActionDelegate
{

    protected void runInternal( IAction action )
        throws CoreException
    {
        for ( Object obj : getSelection().toList() )
        {
            if ( obj instanceof IProject )
            {
                MavenNatureHelper.removeNature( (IProject) obj );
            }
        }
    }
}
