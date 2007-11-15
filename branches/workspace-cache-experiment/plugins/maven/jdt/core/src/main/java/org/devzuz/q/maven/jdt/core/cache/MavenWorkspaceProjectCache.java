package org.devzuz.q.maven.jdt.core.cache;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;

public class MavenWorkspaceProjectCache
{
    private Map< IProject , MavenProjectInfo > workspaceMavenProjects;
    
    public MavenWorkspaceProjectCache()
    {
        workspaceMavenProjects = new HashMap< IProject , MavenProjectInfo >();
    }
    
    public void addMavenProject( IProject project , MavenProjectInfo cachedInfo )
    {
        workspaceMavenProjects.put( project , cachedInfo );
    }
    
    public void removeMavenProject( IProject project )
    {
        for( Map.Entry< IProject , MavenProjectInfo > entry : workspaceMavenProjects.entrySet() )
        {
            if( entry.getKey().equals( project ) )
            {
                workspaceMavenProjects.remove( entry.getKey() );
                break;
            }
        }
    }
    
    public MavenProjectInfo getMavenProjectCachedInfo( IProject project )
    {
        for( Map.Entry< IProject , MavenProjectInfo > entry : workspaceMavenProjects.entrySet() )
        {
            if( entry.getKey().equals( project ) )
            {
                return entry.getValue();
            }
        }
        
        return null;
    }
}