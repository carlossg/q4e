/*******************************************************************************
 * Copyright (c) 2007 Simula Labs
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.devzuz.q.maven.embedder;

import org.devzuz.q.maven.embedder.internal.EclipseMaven;
import org.devzuz.q.maven.embedder.internal.ExtensionPointHelper;
import org.devzuz.q.maven.embedder.log.EclipseLogger;
import org.devzuz.q.maven.embedder.log.Logger;
import org.eclipse.core.internal.runtime.Log;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class MavenCoreActivator implements BundleActivator
{

    // The plug-in ID
    public static final String PLUGIN_ID = "org.devzuz.q.maven.core";

    // The shared instance
    private static MavenCoreActivator plugin;

    private EclipseMaven mavenInstance;
    
    private MavenProjectsManager projectsManager;

    private Logger logger;

    /**
     * The constructor
     */
    public MavenCoreActivator()
    {
        plugin = this;
    }

    public void start( BundleContext context ) throws Exception
    {
        logger = new EclipseLogger( PLUGIN_ID, new Log( context.getBundle() ) );

        // Initialize the maven instance
        mavenInstance = new EclipseMaven();
        mavenInstance.start();
        
        // Initialize the maven workspace projects manager
        projectsManager = new MavenProjectsManager();
        
        ExtensionPointHelper.resolveExtensionPoints( this );
    }

    public void stop( BundleContext context ) throws Exception
    {
        plugin = null;
        mavenInstance.stop();
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static MavenCoreActivator getDefault()
    {
        return plugin;
    }

    public EclipseMaven getMavenInstance()
    {
        return mavenInstance;
    }
    
    public MavenProjectsManager getMavenProjectsManager()
    {
        return projectsManager;
    }

    public void setMavenInstance( EclipseMaven mavenInstance )
    {
        this.mavenInstance = mavenInstance;
    }

    public static Logger getLogger()
    {
        return getDefault().logger;
    }

}
