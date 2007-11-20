package org.devzuz.q.maven.jdt.core.cache;

import org.apache.maven.model.Model;

public class MavenProjectInfo
{
    private String artifactId;

    private String groupId;

    private String version;
    
    // I dislike this because it gives the idea that the MavenProjectInfo is just groupId, artifact Id and version
    // But we'll keep it for now for convenience
    // -- start --
    public static MavenProjectInfo newMavenProjectInfo( String groupId , String artifactId, String version )
    {
        return new MavenProjectInfo( groupId , artifactId , version );
    }
    
    public MavenProjectInfo( String groupId , String artifactId, String version )
    {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }
    // -- end --
    
    public static MavenProjectInfo newMavenProjectInfo( Model pomModel )
    {
        return new MavenProjectInfo( pomModel );
    }
    
    public MavenProjectInfo( Model pomModel )
    {
        artifactId = pomModel.getArtifactId();
        groupId = pomModel.getGroupId();
        version = pomModel.getVersion();
        
        if( artifactId == null || artifactId.equals( "" ) )
            artifactId = pomModel.getParent().getArtifactId();
        
        if( groupId == null || groupId.equals( "" ) )
            groupId = pomModel.getParent().getGroupId();
        
        if( version == null || version.equals( "" ) )
            version = pomModel.getParent().getVersion();
    }
    
    public String getArtifactId()
    {
        return artifactId;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public String getVersion()
    {
        return version;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( artifactId == null ) ? 0 : artifactId.hashCode() );
        result = prime * result + ( ( groupId == null ) ? 0 : groupId.hashCode() );
        result = prime * result + ( ( version == null ) ? 0 : version.hashCode() );
        return result;
    }

    public boolean equals( String artifactId, String groupId, String version )
    {
        return this.artifactId.equals( artifactId ) &&
               this.groupId.equals( groupId ) &&
               this.version.equals( version );
    }
    
    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        final MavenProjectInfo other = (MavenProjectInfo) obj;
        if ( artifactId == null )
        {
            if ( other.artifactId != null )
                return false;
        }
        else if ( !artifactId.equals( other.artifactId ) )
            return false;
        if ( groupId == null )
        {
            if ( other.groupId != null )
                return false;
        }
        else if ( !groupId.equals( other.groupId ) )
            return false;
        if ( version == null )
        {
            if ( other.version != null )
                return false;
        }
        else if ( !version.equals( other.version ) )
            return false;
        return true;
    }
}
