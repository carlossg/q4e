/***************************************************************************************************
 * Copyright (c) 2007 DevZuz, Inc. (AKA Simula Labs, Inc.) All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **************************************************************************************************/
package org.devzuz.q.maven.ui.views;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Observable;
import java.util.Observer;

import org.devzuz.q.maven.embedder.MavenManager;
import org.devzuz.q.maven.embedder.Severity;
import org.devzuz.q.maven.ui.Messages;
import org.devzuz.q.maven.ui.dialogs.FilterDialog;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.part.ViewPart;

public class MavenEventView
    extends ViewPart
    implements Observer
{

    public static final String VIEW_ID = MavenEventView.class.getName();

    public static final String SELECTED_INDEX_KEY = "selection";
    
    public static final int KEYBOARD_CTRL_C = 99;

    private Action filterAction;

    private Action clearEventViewAction;

    private Action controlScrollingAction;

    private IMemento memento;

    private IViewSite site;

    private TableViewer eventTableViewer;

    private MavenViewSeverityFilter severityFilter;

    private MavenEventStore store;

       
    /**
     * The constructor.
     */
    public MavenEventView()
    {
    }

    public void init( IViewSite site, IMemento memento )
        throws PartInitException
    {
        super.init( site, memento );
        this.site = site;
        if ( memento == null )
            this.memento = XMLMemento.createWriteRoot( "EVENTVIEW" );
        else
            this.memento = memento;
        initializeMemento();

        severityFilter = new MavenViewSeverityFilter();
        severityFilter.setSeverity( getSeverity() );
       
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    public void createPartControl( Composite parent )
    {
        store = new MavenEventStore();
        store.addObserver( this );
        MavenManager.getMaven().addEventListener( store );
        eventTableViewer = new TableViewer( parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.CTRL);
       
        // Get the SWT Table that's inside the TableViewer
        final Table eventTable = eventTableViewer.getTable();
        TableColumn column = new TableColumn( eventTable, SWT.NONE);
        column.setText( Messages.MavenEventView_Column_CreatedDate );
        column.setWidth( 100 );
        TableColumn column2 = new TableColumn( eventTable,  SWT.NONE);
        column2.setText( Messages.MavenEventView_Column_EventType );
        column2.setWidth( 100 );
        TableColumn column3 = new TableColumn( eventTable,  SWT.NONE);
        column3.setText( Messages.MavenEventView_Column_Description );
        column3.setWidth( 500 );
        // Show the column headers
        eventTable.setHeaderVisible( true );
        eventTableViewer.addFilter( severityFilter );
        
        makeActions();
        addMenusAndToolbars();

        eventTableViewer.setContentProvider( new MavenEventContentProvider() );
        eventTableViewer.setLabelProvider( new MavenEventLabelProvider() );
        eventTableViewer.setInput( store );
 
        KeyListener selectedDataItems = new KeyAdapter()
        {
	        public void keyPressed(KeyEvent e)
            {
		       if( e.keyCode == KEYBOARD_CTRL_C )
               {
			      setBufferData(eventTable.getSelection());
		       }
	        }
        };

        eventTable.addKeyListener(selectedDataItems);
    }
    
    
    private void setBufferData(TableItem [] tblTemp)
    {	    
	   if ( tblTemp.length > 0 )
       {
           StringBuffer strBuff = new StringBuffer();
           for ( int x = 0; x < tblTemp.length; x++ )
           {
               strBuff.append( tblTemp[x].getText( 0 ) );
               strBuff.append( "\t" );
               strBuff.append( tblTemp[x].getText( 1 ) );
               strBuff.append( "\t" );
               strBuff.append( tblTemp[x].getText( 2 ) );
               strBuff.append( "\n" );
           }

           copyDatatoClipboard( strBuff );
           strBuff.delete( 0, strBuff.length() );
       }		
    }
    
    private void copyDatatoClipboard(StringBuffer strbuff)
    {
        StringSelection strSelection  = new StringSelection(strbuff.toString());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents( strSelection,strSelection );
    }
   
    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus()
    {

    }

    @Override
    public void dispose()
    {
        if ( store != null )
        {
            store.deleteObserver( this );
            MavenManager.getMaven().removeEventListener( store );
        }
        super.dispose();
    }

    private void addMenusAndToolbars()
    {
        IActionBars bars = getViewSite().getActionBars();
        // bars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);
        IToolBarManager toolBarManager = bars.getToolBarManager();
        IMenuManager mgr = bars.getMenuManager();

        toolBarManager.add( clearEventViewAction );
        toolBarManager.add( controlScrollingAction );
        
        mgr.add( filterAction );
    }

    
    private void makeActions()
    {
        filterAction = new Action( Messages.MavenEventView_Filter )
        {
            public void run()
            {
                handleFilter();
            }
        };
        filterAction.setToolTipText( Messages.MavenEventView_Filter );
        filterAction.setImageDescriptor( MavenImages.DESC_FILTER );
        filterAction.setDisabledImageDescriptor( MavenImages.DESC_FILTER_DISABLED );

        clearEventViewAction = new Action( Messages.MavenEventView_ClearView )
        {
            public void run()
            {
                handleClearEventView();
            }
        };
        clearEventViewAction.setEnabled( true );
        clearEventViewAction.setToolTipText( Messages.MavenEventView_ClearView );
        clearEventViewAction.setImageDescriptor( MavenImages.DESC_CLEAREVENTVIEW );
        clearEventViewAction.setDisabledImageDescriptor( MavenImages.DESC_CLEAREVENTVIEW_DISABLED );

        controlScrollingAction = new Action( Messages.MavenEventView_ScrollLock, Action.AS_CHECK_BOX )
        {
            public void run()
            {
            }
        };
        controlScrollingAction.setEnabled( true );
        controlScrollingAction.setToolTipText( Messages.MavenEventView_ScrollLock );
        controlScrollingAction.setImageDescriptor( MavenImages.DESC_SCROLLLOCK );
        controlScrollingAction.setDisabledImageDescriptor( MavenImages.DESC_SCROLLLOCK_DISABLED );
        
    }

    // This is the handler for the "events added" or "store cleared" event of MavenEventStore
    public void update( Observable o, Object arg )
    {
        eventTableViewer.getControl().getDisplay().asyncExec( new Runnable()
        {
            public void run()
            {
                eventTableViewer.refresh();
                // If scrolling is enabled, scroll it
                if ( !controlScrollingAction.isChecked() )
                {
                    Table table = eventTableViewer.getTable();
                    if ( ( table != null ) 
                        && ( table.getItemCount() * table.getItemHeight() > table.getClientArea().height ) )
                    {
                        // The idea here is to select the last element to induce a scroll to the bottom
                        table.showItem( table.getItem(table.getItemCount() - 1) );
                    }
                }
            }
        } );
    }

    private void handleFilter()
    {
        FilterDialog dialog = new FilterDialog( site.getShell(), memento );
        dialog.create();
        dialog.getShell().setText( Messages.MavenEventView_FilterDialog_title );
        if ( dialog.open() == FilterDialog.OK )
        {
            severityFilter.setSeverity( getSeverity() );
            // Setting this filter will cause the table to reload with the filter applied
            eventTableViewer.setFilters( new ViewerFilter[] { severityFilter } );
        }
    }

    private void handleClearEventView()
    {
        // This will generate a table refresh event with an empty store
        // thereby clearing the view.
        store.dispose();
    }

    public Severity getSeverity()
    {
        return Severity.getAll()[getSeverityLevel()];
    }

    private Integer getSeverityLevel()
    {
        return memento.getInteger( SELECTED_INDEX_KEY );
    }

    private void initializeMemento()
    {
        if ( getSeverityLevel() == null )
        {
            memento.putInteger( SELECTED_INDEX_KEY, Severity.info.ordinal() );
        }
    }
}