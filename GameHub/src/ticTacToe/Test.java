package ticTacToe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class Test {
	
	public static void main ( String[] args ) throws InvocationTargetException, InterruptedException
	{
	    // Main window

	    final JFrame frame = new JFrame ();

	    final JPanel panel = new JPanel ( (LayoutManager) new FlowLayout ( FlowLayout.LEFT, 5, 5 ) )
	    {
	        public Dimension getPreferredSize ()
	        {
	            Dimension ps = super.getPreferredSize ();
	            ps.width = 0;
	            return ps;
	        }
	    };
	    frame.add ( new JScrollPane ( panel ) );

	    frame.setSize ( 600, 500 );
	    frame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
	    frame.setLocationRelativeTo ( null );

	    SwingUtilities.invokeAndWait ( new Runnable ()
	    {
	        public void run ()
	        {
	            frame.setVisible ( true );
	        }
	    } );

	    // Load dialog

	    final JDialog load = new JDialog ( frame );

	    JPanel panel2 = new JPanel ( new BorderLayout () );
	    panel2.setBorder ( BorderFactory.createEmptyBorder ( 15, 15, 15, 15 ) );
	    load.add ( panel2 );

	    final JProgressBar progressBar = new JProgressBar ( 0, 100 );
	    panel2.add ( progressBar );

	    load.setModal ( false );
	    load.pack ();
	    load.setLocationRelativeTo ( frame );

	    SwingUtilities.invokeAndWait ( new Runnable ()
	    {
	        public void run ()
	        {
	            load.setVisible ( true );
	        }
	    } );

	    // Heavy task (takes approx. 10 seconds + some time on buttons creation) 

	    for ( int i = 0; i < 100; i++ )
	    {
	        Thread.sleep ( 100 );

	        final JButton button = new JButton ( "Button" + i );
	        final int finalI = i;

	        // Updating panel and progress in EDT
	        SwingUtilities.invokeLater ( new Runnable ()
	        {
	            public void run ()
	            {
	                panel.add ( button );
	                button.revalidate ();
	                progressBar.setValue ( finalI );
	            }
	        } );
	    }
	}

}
