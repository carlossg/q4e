package org.devzuz.q.maven.embedder;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public class MavenProjectsManager
{
    private Map< IProject , MavenProjectCachedInfo > mavenProjects;
    
    public MavenProjectsManager()
    {
        mavenProjects = new HashMap< IProject , MavenProjectCachedInfo >();
    }
    
    public void addMavenProject( IProject project , IMavenProject mavenProject )
    {
        mavenProjects.put( project , MavenProjectCachedInfo.newMavenProjectCachedInfo( mavenProject ) );
    }
    
    public void removeMavenProject( IProject project )
    {
        mavenProjects.remove( project );
    }
    
    public IMavenProject getMavenProject( IProject project , boolean resolveTransitively ) throws CoreException
    {
        MavenProjectCachedInfo cachedProject = mavenProjects.get( project );
        
        // TODO : We should also check the digest of the project's POM against our last digest, coz if it
        // changed, we should read the maven project again from the embedder
        
        // If we haven't cached the project yet or if we have cached the project but it's not resolved
        // transitively and resolveTransitively is true
        if( ( cachedProject == null ) ||
            ( ( cachedProject.getMavenProject().getArtifacts() == null ) && 
              ( resolveTransitively == true ) ) )
        {
            IMavenProject mavenProject = MavenManager.getMaven().getMavenProject( project , resolveTransitively );
            cachedProject =  MavenProjectCachedInfo.newMavenProjectCachedInfo( mavenProject );
            mavenProjects.put( project , cachedProject );
        }
        
        return cachedProject.getMavenProject();
    }
    
    public IProject getWorkspaceProject( String groupId, String artifactId , String version )
    {
        for( Map.Entry< IProject , MavenProjectCachedInfo > entry : mavenProjects.entrySet() )
        {
            IMavenProject mavenProject = entry.getValue().getMavenProject();
            if( mavenProject.getGroupId().equals( groupId ) &&
                mavenProject.getArtifactId().equals( artifactId ) && 
                mavenProject.getVersion().equals( version ) )
            {
                return entry.getKey();
            }   
        }
        
        return null;
    }
    
    private static class MavenProjectCachedInfo
    {
        private IMavenProject mavenProject;
        private String        pomDigest;
        
        public static MavenProjectCachedInfo newMavenProjectCachedInfo( IMavenProject mavenProject )
        {
            // TODO : add digest so we could detect if mavenProject's POM has changed
            return new MavenProjectCachedInfo( mavenProject , "" );
        }
        
        private MavenProjectCachedInfo( IMavenProject project , String pomDigest )
        {
            this.mavenProject = project;
            this.pomDigest = pomDigest;
        }
        
        public IMavenProject getMavenProject()
        {
            return mavenProject;
        }
        
        public void setMavenProject( IMavenProject mavenProject )
        {
            this.mavenProject = mavenProject;
        }
        
        public String getPomDigest()
        {
            return pomDigest;
        }
        
        public void setPomDigest( String pomDigest )
        {
            this.pomDigest = pomDigest;
        }
    }
}