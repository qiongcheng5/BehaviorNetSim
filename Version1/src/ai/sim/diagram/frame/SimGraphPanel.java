/*
 * Created on 2005-2-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.jfree.data.time.Millisecond;
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
 * SimGraphPanel
 */
public class SimGraphPanel extends JPanel implements ItemListener  {
	private static int SUBPLOT_COUNT1 = ConfigFile.getDefaultSubplots();
	private int SUBPLOT_COUNT = 0;
    private double[] lastValue = new double[SUBPLOT_COUNT];

    private JPanel buttonsPanel = null;

    private AbstractGraph timeGraph = null;
	JCheckBox[] checkBox;
	TitledBorder title;
	Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

	/**
	 * @param arg0
	 */
	public SimGraphPanel(int size, String[] objNames, String[] objValueNames ){
		super();
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

		this.setLayout( new BorderLayout() );
		this.setOpaque( true );

		this.add( timeGraph.getGraphPanel(), "Center" );

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
		this.add( new JScrollPane().add(buttonsPanel), "West" );

	}

	//----------To Implement ItemChangeLister---------
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

    /**
     * add AnalysisPair
     * @param index
     * @param timestamp
     * @param value
     */
    public void addAnalysisPair( int index, Object timestamp, Object value ){
    	System.out.println( "Diagram add analysisPair");
		//According to the different configure, there are different XYPlots
		if ( ai.sim.diagram.util.ConfigFile.isSimTimeOfX() ){
			((CombinedDomainSimtimeGraph)timeGraph).addSimtimeCurveSetPlot(index, new SimtimeCurveValuePair(((Double)timestamp).doubleValue(), ((Double)value).doubleValue()));
		}else{
			((CombinedDomainTimeGraph)timeGraph).addTimeCurveSetPlot(index, new TimeCurveValuePair((Millisecond)timestamp, ((Double)value).doubleValue()));
		}
    }

    /**
     * getGraphModel
     * @return
     */
    public JPanel getGraphModel(){
    	return this.buttonsPanel;
    }

    public static void main(String args[])
    {
		String[] behNames = new String[SUBPLOT_COUNT1];
		String[] excNames = new String[SUBPLOT_COUNT1];
		for ( int i = 0; i < SUBPLOT_COUNT1; i++ ){
			behNames[i] = "Behavior" + i;
			excNames[i] = "Excitation" + i;
		}
    	//SimGraphPanel test = new SimGraphPanel(SUBPLOT_COUNT1,behNames,  excNames);

    }
}
