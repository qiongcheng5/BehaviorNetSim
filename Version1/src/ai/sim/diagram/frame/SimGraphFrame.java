/*
 * Created on Jan 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.jfree.data.time.Millisecond;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import ai.sim.diagram.graph.AbstractGraph;
import ai.sim.diagram.graph.CombinedDomainSimtimeGraph;
import ai.sim.diagram.graph.CombinedDomainTimeGraph;
import ai.sim.diagram.info.SimtimeCurveValuePair;
import ai.sim.diagram.info.TimeCurveValuePair;
import ai.sim.diagram.info.plot.SimtimeCurveSetPlot;
import ai.sim.diagram.info.plot.TimeCurveSetPlot;
import ai.sim.diagram.util.ConfigFile;

/**
 * @author Qiong Cheng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimGraphFrame extends ApplicationFrame implements ActionListener,ItemListener  {
    private static int SUBPLOT_COUNT1 = ConfigFile.getDefaultSubplots();
    private int SUBPLOT_COUNT = 0;
    private double[] lastValue = new double[SUBPLOT_COUNT];

    private JPanel displayPanel = null;
    private JPanel buttonsPanel = null;

    private AbstractGraph timeGraph = null;
	JCheckBox[] checkBox;
	TitledBorder title;
	Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

    /**
     * start
     */
    private static JButton jbutstart, jbutstop, jbuttonhide, jbuttonreshow, jbutton;
    private static String startTimerComm = "Start Timer";
    private static String stopTimerComm = "Stop Timer";
    private static String munuAddAll = "ADD_ALL";
    private static String hideOne = "Hide";
    private static String reshowOne = "Reshow";
    private JPanel buttons = null;
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
	            ((CombinedDomainTimeGraph)timeGraph).addTimeCurveSetPlot( j, new TimeCurveValuePair(new Millisecond(), lastValue[j]));
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
     * end
     */

    /**
     * @param arg0
     */
    public SimGraphFrame(int size, String[] objNames, String[] objValueNames ){
            super(ConfigFile.getGraphWindowTitle());

            if ( objNames == null || objValueNames == null || size != objNames.length || size != objValueNames.length )
                    return;

            SUBPLOT_COUNT = size;

            //Get the number of all behaviors and their label and their output port(??)
            //size is the total of all behaviors
            //According to the different configure, there are different XYPlots
            if ( ai.sim.diagram.util.ConfigFile.isSimTimeOfX() ){
                    SimtimeCurveSetPlot[] datasetPlots = new SimtimeCurveSetPlot[SUBPLOT_COUNT];
                    for ( int i = 0; i < SUBPLOT_COUNT; i++ ){
                            datasetPlots[ i ] = new SimtimeCurveSetPlot(i, ai.sim.analysis.util.ConfigFile.getXAxisLabel(), objValueNames[i], objNames[i], null );
                    }

                    timeGraph = new CombinedDomainSimtimeGraph( "DEVS Simulation Time-Excitation Graph", datasetPlots );

            }else{
                    TimeCurveSetPlot[] datasetPlots = new TimeCurveSetPlot[SUBPLOT_COUNT];
                    for ( int i = 0; i < SUBPLOT_COUNT; i++ ){
                            datasetPlots[ i ] = new TimeCurveSetPlot(i, ai.sim.analysis.util.ConfigFile.getXAxisLabel(), objValueNames[i], objNames[i], null );
                    }

                    timeGraph = new CombinedDomainTimeGraph( "DEVS Simulation Time-Excitation Graph", datasetPlots );
            }

            displayPanel = new JPanel( new BorderLayout() );
            displayPanel.setOpaque( true );

            displayPanel.add( new JScrollPane().add(timeGraph.getGraphPanel()), "Center" );

            buttonsPanel = new JPanel(new GridLayout(SUBPLOT_COUNT,1) );
            title = BorderFactory.createTitledBorder(
                           loweredetched, "Choice");
            title.setTitleJustification(TitledBorder.RIGHT);
            title.setTitleColor( Color.BLACK );
            buttonsPanel.setBorder(title);

            checkBox = new JCheckBox[SUBPLOT_COUNT];
            for ( int i = 0; i < SUBPLOT_COUNT; i++ ){
            checkBox[i] = new JCheckBox( objNames[i] + " : " + objValueNames[i]);
            checkBox[i].setSelected( true );
            checkBox[i].setFont(new Font("SansSerif", Font.BOLD, 12));

            checkBox[i].setForeground( Color.BLACK );
            checkBox[i].addItemListener(this);

            buttonsPanel.add(checkBox[i]);
          }
          displayPanel.add( new JScrollPane().add(buttonsPanel), "West" );


          /**
           * start
           */
          //Add buttons
          /*
          buttons = new JPanel(new FlowLayout());
        jbutstart = new JButton(startTimerComm);
        jbutstart.setActionCommand(startTimerComm);
        jbutstart.addActionListener(this);
        jbutstart.setEnabled( false );
        buttons.add(jbutstart);

        jbutstop = new JButton(stopTimerComm);
        jbutstop.setActionCommand(stopTimerComm);
        jbutstop.addActionListener(this);
        jbutstop.setEnabled( false );
        buttons.add(jbutstop);

        jbutton = new JButton(munuAddAll);
        jbutton.setActionCommand(munuAddAll);
        jbutton.addActionListener(this);
        jbutton.setEnabled( false );
        buttons.add(jbutton);

        jbuttonhide = new JButton(hideOne);
        jbuttonhide.setActionCommand(hideOne);
        jbuttonhide.addActionListener(this);
        jbuttonhide.setEnabled( false );
        buttons.add(jbuttonhide);

        jbuttonreshow = new JButton(reshowOne);
        jbuttonreshow.setActionCommand(reshowOne);
        jbuttonreshow.addActionListener(this);
        jbuttonreshow.setEnabled( false );
        buttons.add(jbuttonreshow);

        displayPanel.add(buttons, "South");
        */
        /**
         * end
         */

        this.getContentPane().add( displayPanel );
        this.setVisible( false );
	}

	/**
	 * getDiagramPanel
	 * @return
	 */
	public JPanel getDiagramPanel(){
		return this.displayPanel;
	}

    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();

		for ( int i = 0; i < SUBPLOT_COUNT; i++ ){
	        if (source == checkBox[i]) {
	        	if ( e.getStateChange() == ItemEvent.DESELECTED ){
	        		timeGraph.hideTimeCurvePlot( i );
	        	}else if ( e.getStateChange() == ItemEvent.SELECTED ){
	        		timeGraph.reshowTimeCurvePlot( i );
	        	}
	        }
		}
    }

    public void addAnalysisPair( int index, Object timestamp, Object value ){
		//According to the different configure, there are different XYPlots
    	System.out.println( "Diagram add analysisPair");
		if ( ai.sim.diagram.util.ConfigFile.isSimTimeOfX() ){
			((CombinedDomainSimtimeGraph)timeGraph).addSimtimeCurveSetPlot(index, new SimtimeCurveValuePair(((Integer)timestamp).intValue(), ((Double)value).doubleValue()));
		}else{
			((CombinedDomainTimeGraph)timeGraph).addTimeCurveSetPlot(index, new TimeCurveValuePair((Millisecond)timestamp, ((Double)value).doubleValue()));
		}
    }

    public void cShow(){
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }

	/**
	 * Just for test, to simulate the realtime data generation
	 */
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
                ((CombinedDomainTimeGraph)timeGraph).addTimeCurveSetPlot( i, new TimeCurveValuePair(new Millisecond(), lastValue[i]));
            }
        }

        if(actionevent.getActionCommand().equals(munuAddAll)){
            Millisecond millisecond = new Millisecond();
            System.out.println("Now = " + millisecond.toString());
            for(int j = 0; j < SUBPLOT_COUNT; j++)
            {
                lastValue[j] = lastValue[j] * (0.90000000000000002D + 0.20000000000000001D * Math.random());
                ((CombinedDomainTimeGraph)timeGraph).addTimeCurveSetPlot( j, new TimeCurveValuePair(new Millisecond(), lastValue[j]));
            }
        }else if (actionevent.getActionCommand().equals(hideOne)){
        	if ( timeGraph != null )
        		timeGraph.hideTimeCurvePlot( 0 );
        }else if (actionevent.getActionCommand().equals(reshowOne)){
        	if ( timeGraph != null )
        		timeGraph.reshowTimeCurvePlot( 0 );
        }
    }
	/**
	 * end
	 */

    public static void main(String args[])
    {
		String[] behNames = new String[SUBPLOT_COUNT1];
		String[] excNames = new String[SUBPLOT_COUNT1];
		for ( int i = 0; i < SUBPLOT_COUNT1; i++ ){
			behNames[i] = "Behavior" + i;
			excNames[i] = "Excitation" + i;
		}
    	SimGraphFrame test = new SimGraphFrame(SUBPLOT_COUNT1,behNames,  excNames);
    	test.cShow();

        datagen = test.new DataGenerator(100);
		//datagen.start();

		jbutstart.setEnabled( false );
		jbutton.setEnabled( false );
		jbutstop.setEnabled( false );
		jbuttonhide.setEnabled( false );
		jbuttonreshow.setEnabled( false );

    }
}
