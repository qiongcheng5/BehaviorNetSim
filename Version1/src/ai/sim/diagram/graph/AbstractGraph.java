/*
 * Created on Dec 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.graph;


import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import ai.sim.diagram.info.plot.AbstractDatasetPlot;

/**
 * @author Qiong Cheng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractGraph {
	private String title;
	protected JFreeChart jchart = null;
	protected ChartPanel jChartPanel = null;
	AbstractDatasetPlot[] dataset = null;
	
	public AbstractGraph( String title ){
		this( title, null );
	}
	
	public AbstractGraph( String title, AbstractDatasetPlot[] datasetPlot ){
		this.title = title;
		
		this.dataset = datasetPlot;
		jchart = createChart( datasetPlot );
		jChartPanel = createChartPanel( jchart );
	}
	
	
	protected abstract JFreeChart createChart( AbstractDatasetPlot[] datasetPlot );
	protected abstract ChartPanel createChartPanel( JFreeChart chart );
	
	public String getTitle(){
		return this.title ;
	}
	
	public JFreeChart getChart(){
		return this.jchart;
	}
	
	public void resetChart(){
		this.jchart = null;
	}
	
    public JPanel getGraphPanel(){
    	return this.jChartPanel;
    }

    public void resetChartPanel(){
    	this.jChartPanel = null;
    }
    
    public abstract void hideTimeCurvePlot( int index );
	
    public abstract void reshowTimeCurvePlot( int index );

}
