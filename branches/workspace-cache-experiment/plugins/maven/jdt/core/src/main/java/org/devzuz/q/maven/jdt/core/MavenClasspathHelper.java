package org.devzuz.q.maven.jdt.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.devzuz.q.maven.embedder.MavenManager;
import org.devzuz.q.maven.jdt.core.cache.MavenProjectInfo;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;

public class MavenClasspathHelper
{
    public static String POM_XML = "pom.xml";
    
    public static boolean classpathContainsFolder( Collection<IClasspathEntry> classpathSrcEntries , IClasspathEntry folder )
    {
        for( IClasspathEntry entry : classpathSrcEntries )
        {
            if( entry.getPath().equals( folder.getPath() ) ) 
            {
                return true;
            }
        }
        
        return false;
    }
    
    public static MavenProjectInfo getMavenProjectInfo( IClasspathEntry classpathEntry )
    {
        int repoSegmentCount = MavenManager.getMaven().getLocalRepository().getBaseDirectoryPath().segmentCount();
        IPath classpath = classpathEntry.getPath();
        int segmentCount = classpath.segmentCount();
        String version = classpath.segment( segmentCount - 2 );
        String artifactId = classpath.segment( segmentCount - 3 );

        StringBuilder groupId = new StringBuilder( "" );
        for ( int i = repoSegmentCount; i < segmentCount - 3; i++ )
        {
            // Attach the dot
            if ( i != repoSegmentCount )
                groupId.append( "." );
            groupId.append( classpath.segment( i ) );
        }

        return MavenProjectInfo.newMavenProjectInfo( artifactId , groupId.toString() , version );
    }

    // TODO : This does not work on child poms .. groupId will be null
    //        We must go up to the parent if groupId of child pom is null
    public static MavenProjectInfo getMavenProjectInfo( IProject iproject )
    {
        File pom = new File( iproject.getFile( POM_XML ).getLocation().toOSString() );
        MavenProjectInfo info = null;
        try
        {
            FileReader filetoread = new FileReader( pom );
            Model pomModel = new MavenXpp3Reader().read( filetoread );
            info = MavenProjectInfo.newMavenProjectInfo( pomModel.getArtifactId() , pomModel.getGroupId() , pomModel.getVersion() );
            pomModel = null;
            filetoread.close();
        }
        catch ( FileNotFoundException fnfe )
        {
            // TODO Auto-generated catch block
            fnfe.printStackTrace();
        }
        catch ( IOException ioe )
        {
            // TODO Auto-generated catch block
            ioe.printStackTrace();
        }
        catch ( XmlPullParserException xppe )
        {
            // TODO Auto-generated catch block
            xppe.printStackTrace();
        }
        pom = null;
        
        return info;
    }
}
