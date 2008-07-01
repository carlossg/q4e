package org.devzuz.q.maven.pomeditor.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.devzuz.q.maven.pomeditor.PomEditorActivator;
import org.devzuz.q.maven.pomeditor.components.IObjectActionMap;
import org.devzuz.q.maven.pomeditor.components.ITreeObjectAction;
import org.devzuz.q.maven.pomeditor.components.ObjectTreeComponent;
import org.devzuz.q.maven.pomeditor.model.ConfigurationTreeContentProvider;
import org.devzuz.q.maven.pomeditor.model.ConfigurationTreeLabelProvider;
import org.devzuz.q.maven.pomeditor.pages.internal.AddEditConfigurationAction;
import org.devzuz.q.maven.pomeditor.pages.internal.AddEditConfigurationItemListAction;
import org.devzuz.q.maven.pomeditor.pages.internal.DeleteItemAction;
import org.devzuz.q.maven.pomeditor.pages.internal.ITreeObjectActionListener;
import org.devzuz.q.maven.pomeditor.pages.internal.Mode;
import org.devzuz.q.maven.ui.dialogs.AbstractResizableDialog;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ConfigurationDialog
    extends AbstractResizableDialog implements ITreeObjectActionListener
{
    private ObjectTreeComponent treeComponent;
    
    private Xpp3Dom dom;

    private ConfigurationTreeContentProvider contentProvider;
    
    public static ConfigurationDialog newConfigurationDialog()
    {
        return new ConfigurationDialog( PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell() );
    }
    
    public ConfigurationDialog( Shell parentShell )
    {
        super( parentShell );
    }

    @Override
    protected Preferences getDialogPreferences()
    {
        return PomEditorActivator.getDefault().getPluginPreferences();
    }

    @Override
    protected Control internalCreateDialogArea( Composite container )
    {
        System.out.println("internalCreateDialogArea start");
        container.setLayout( new FillLayout() );
        
        treeComponent = new ObjectTreeComponent( container, SWT.None );
        contentProvider = new ConfigurationTreeContentProvider( this.dom );
        
        treeComponent.setContentProvider( contentProvider );
        treeComponent.setLabelProvider( new ConfigurationTreeLabelProvider() );
        treeComponent.setObjectActionMap( new ConfigurationActionMap( this, contentProvider ) );
        treeComponent.setInput( this.dom );
        treeComponent.expandAll();
        
        System.out.println("internalCreateDialogArea end");
        
        return container;
    }
    
    public int openWithConfiguration( Xpp3Dom dom )
    {
        if ( dom != null )
        {
            System.out.println("dom not null");
            this.dom = dom;
        }
        else
        {
            System.out.println("dom null");
            this.dom = null;
        }
        
        return open();
    }
    
    private class ConfigurationActionMap implements IObjectActionMap
    {
        private Map<String , List<ITreeObjectAction> > objectActionMap;

        private ITreeObjectActionListener listener;
        
        private ITreeContentProvider contentProvider;

        public ConfigurationActionMap( ITreeObjectActionListener actionListener,
                                       ITreeContentProvider contentProvider )
        {
            this.listener = actionListener;
            this.contentProvider = contentProvider;
            constructObjectActionMap();
        }

        private void constructObjectActionMap()
        {
            objectActionMap = new HashMap<String, List<ITreeObjectAction>>();
            
            List<ITreeObjectAction> configActionMap = new ArrayList<ITreeObjectAction>();
            configActionMap.add( new AddEditConfigurationAction( listener , Mode.EDIT ) );
            configActionMap.add( new DeleteItemAction( listener , "Delete this configuration", "configuration", contentProvider ) );
            
            objectActionMap.put( "Xpp3Dom", configActionMap );
            
            List<ITreeObjectAction> configListActionMap = new ArrayList<ITreeObjectAction>();
            configListActionMap.add( new AddEditConfigurationItemListAction( listener , Mode.EDIT ) );
            configListActionMap.add( new DeleteItemAction( listener , "Delete this configuration", "configuration", contentProvider ) );
            configListActionMap.add( new AddEditConfigurationAction( listener , Mode.ADD ) );
            configListActionMap.add( new AddEditConfigurationItemListAction( listener , Mode.ADD ) );
            
            objectActionMap.put( "Xpp3DomList", configListActionMap );
            
            List<ITreeObjectAction> configObjectActionMap = new ArrayList<ITreeObjectAction>();
            configObjectActionMap.add( new DeleteItemAction( listener , "Delete this configuration", "configuration", contentProvider ) );
            configObjectActionMap.add( new AddEditConfigurationAction( listener , Mode.ADD ) );
            configObjectActionMap.add( new AddEditConfigurationItemListAction( listener , Mode.ADD ) );
            
            objectActionMap.put( "Configuration", configObjectActionMap );
            
        }

        public List<ITreeObjectAction> getObjectActions( Object element )
        {
            if ( element instanceof Xpp3Dom )
            {
                Xpp3Dom dom = ( Xpp3Dom ) element;
                
                if( dom.getValue() == null )
                {
                    if( dom.getName().equalsIgnoreCase( "configuration" ) )
                    {
                        return objectActionMap.get( "Configuration" );
                    }
                    else
                    {
                        return objectActionMap.get( "Xpp3DomList" );
                    }
                }
                else
                {
                    return objectActionMap.get( "Xpp3Dom" );
                }
            }
            
            return objectActionMap.get( "default" );
        }
        
    }

    public void afterAction()
    {
        // TODO Auto-generated method stub
        
    }

}
