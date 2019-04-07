/*
 * Created on Dec 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.graph;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import ai.sim.diagram.info.TimeCurveValuePair;
import ai.sim.diagram.info.plot.TimeCurveSetPlot;
import ai.sim.diagram.util.ConfigFile;

/**
 * @author Qiong Cheng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestTimeGraph extends ApplicationFrame implements ActionListener {
	private int SUBPLOT_COUNT = ConfigFile.getDefaultSubplots();
    private double[] lastValue = new double[SUBPLOT_COUNT];
    
    private JPanel testPanel = null;
    private JPanel buttonsPanel = null;
    private static String startTimerComm = "Start Timer";
    private static String stopTimerComm = "Stop Timer";
    private static String munuAddAll = "ADD_ALL";
    private static String hideOne = "Hide";
    private static String reshowOne = "Reshow";
    
    private CombinedDomainTimeGraph timeGraph = null;
    
    private static JButton jbutstart, jbutstop, jbuttonhide, jbuttonreshow, jbutton;
    class DataGenerator extends Timer
    implements ActionListener
	{

	    public void actionPerformed(ActionEvent actionevent)
	    {
	        Millisecond millisecond = new Millisecond();
	        //System.out.println("Now = " + millisecond.toString());
	        for(int j = 0; j < SUBPLOT_COUNT; j++)
	        {
	            lastValue[j] = lastValue[j] * (0.90000000000000002D + 0.20000000000000001D * Math.random());
	            timeGraph.addTimeCurveSetPlot( j, new TimeCurveValuePair(new Millisecond(), lastValue[j]));
	        }
	    }

	    DataGenerator(int i)
	    {
	        super(i, null);
	        addActionListener(this);
	    }
	}
    private static DataGenerator datagen;
    
	/**
	 * @param arg0
	 */
	public TestTimeGraph(String arg0) {
		super(arg0);
	
		TimeCurveSetPlot[] datasetPlots = new TimeCurveSetPlot[SUBPLOT_COUNT] ;
		for ( int i = 0; i < SUBPLOT_COUNT; i++ ){
			lastValue[i] = 100D;
			datasetPlots[ i ] = new TimeCurveSetPlot(i, "Time", "Excitation "+ i, "Behavior "+ i, null );
		}
		
		timeGraph = new CombinedDomainTimeGraph( "DEVS Simulation Time-Excitation Graph", datasetPlots );
		testPanel = new JPanel(new BorderLayout());
		testPanel.setOpaque( true );
		testPanel.add( timeGraph.getGraphPanel(), "Center" );
		
		//Add buttons
		buttonsPanel = new JPanel(new FlowLayout());
        jbutstart = new JButton(startTimerComm);
        jbutstart.setActionCommand(startTimerComm);
        jbutstart.addActionListener(this);
        jbutstart.setEnabled( false );
        buttonsPanel.add(jbutstart);
        
        jbutstop = new JButton(stopTimerComm);
        jbutstop.setActionCommand(stopTimerComm);
        jbutstop.addActionListener(this);
        jbutstop.setEnabled( true );
        buttonsPanel.add(jbutstop);
        
        /*
        for(int j = 0; j < SUBPLOT_COUNT; j++)
        {
            JButton jbutton1 = new JButton("Series " + j);
            jbutton1.setActionCommand("ADD_DATA_" + j);
            jbutton1.addActionListener(this);
            jpanel.add(jbutton1);
        }
        */

        jbutton = new JButton(munuAddAll);
        jbutton.setActionCommand(munuAddAll);
        jbutton.addActionListener(this);
        jbutton.setEnabled( false );
        buttonsPanel.add(jbutton);
        
        jbuttonhide = new JButton(hideOne);
        jbuttonhide.setActionCommand(hideOne);
        jbuttonhide.addActionListener(this);
        jbuttonhide.setEnabled( false );
        buttonsPanel.add(jbuttonhide);
        
        jbuttonreshow = new JButton(reshowOne);
        jbuttonreshow.setActionCommand(reshowOne);
        jbuttonreshow.addActionListener(this);
        jbuttonreshow.setEnabled( false );
        buttonsPanel.add(jbuttonreshow);
        
        testPanel.add(buttonsPanel, "South");
        
        this.getContentPane().add( testPanel );
        
	}

    public void actionPerformed(ActionEvent actionevent)
    {
    	if(actionevent.getActionCommand().equalsIgnoreCase( startTimerComm )){
   			datagen.restart();

    		jbutstart.setEnabled( false );
    		jbutton.setEnabled( false );
    		jbutstop.setEnabled( true );
    		jbuttonhide.setEnabled( false );
    	}else if (actionevent.getActionCommand().equalsIgnoreCase( stopTimerComm )){
    		datagen.stop();
    		
    		jbutstart.setEnabled( true );
    		jbutton.setEnabled( true );
    		jbutstop.setEnabled( false );
    		jbuttonhide.setEnabled( true );
    	}else if ( actionevent.getActionCommand().equalsIgnoreCase( hideOne )){
    		jbutstart.setEnabled( true );
    		jbutton.setEnabled( true );
    		jbuttonhide.setEnabled( true );
    		jbutstop.setEnabled( false );
    	}else{
       		jbutstart.setEnabled( true );
    		jbutton.setEnabled( true );
    		jbuttonhide.setEnabled( true );
    		jbutstop.setEnabled( false );
    	}
    	
    	
        for(int i = 0; i < SUBPLOT_COUNT; i++){
            if(actionevent.getActionCommand().endsWith(String.valueOf(i))){
                Millisecond millisecond1 = new Millisecond();
                //System.out.println("Now = " + millisecond1.toString());
                lastValue[i] = lastValue[i] * (0.90000000000000002D + 0.20000000000000001D * Math.random());
                timeGraph.addTimeCurveSetPlot( i, new TimeCurveValuePair(new Millisecond(), lastValue[i]));
            }
        }

        if(actionevent.getActionCommand().equals(munuAddAll)){
            Millisecond millisecond = new Millisecond();
            System.out.println("Now = " + millisecond.toString());
            for(int j = 0; j < SUBPLOT_COUNT; j++)
            {
                lastValue[j] = lastValue[j] * (0.90000000000000002D + 0.20000000000000001D * Math.random());
                timeGraph.addTimeCurveSetPlot( j, new TimeCurveValuePair(new Millisecond(), lastValue[j]));
            }
        }else if (actionevent.getActionCommand().equals(hideOne)){
        	if ( timeGraph != null )
        		timeGraph.hideTimeCurvePlot( 0 );
        }else if (actionevent.getActionCommand().equals(reshowOne)){
        	if ( timeGraph != null )
        		timeGraph.reshowTimeCurvePlot( 0 );
        }
    }
    
    public static void main(String args[])
    {
    	TestTimeGraph test = new TestTimeGraph(ConfigFile.getGraphWindowTitle());
        test.pack();
        RefineryUtilities.centerFrameOnScreen(test);
        test.setVisible(true);
        
        datagen = test.new DataGenerator(100);
		datagen.start();
		jbutstart.setEnabled( true );
		jbutton.setEnabled( true );
		jbutstop.setEnabled( true );
		jbuttonhide.setEnabled( true );
		jbuttonreshow.setEnabled( true );
    }
}
