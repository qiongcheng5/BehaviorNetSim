/*
 * Created on Dec 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardLegend;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.Spacer;

import ai.sim.diagram.info.TimeCurveValuePair;
import ai.sim.diagram.info.plot.AbstractDatasetPlot;
import ai.sim.diagram.info.plot.TimeCurveSetPlot;
import ai.sim.diagram.util.ConfigFile;

/**
 * @author Qiong Cheng
 *
 * Combined Graph for Multiple Dataset of Realtime XAxis value and Domain YAxis Value
 */
public class CombinedDomainTimeGraph extends AbstractGraph implements ChangeListener {
    private CombinedDomainXYPlot combineddomainxyplot;
    private DateAxis dateaxis;
    private TimeSeries datasets[];
    private XYPlot xyplots[];

    /**
     * Constructor function
     * @param title
     */
	public CombinedDomainTimeGraph( String title ){
		super(title );
	}

	/**
	 * Constructor function
	 * @param title
	 * @param datasetPlot
	 */
	public CombinedDomainTimeGraph( String title, TimeCurveSetPlot[] datasetPlot ){
		super( title, datasetPlot );
	}

	/**
	 * Just implemented for slide bar bragging event
	 */
    public void stateChanged(ChangeEvent changeevent)
    {
        int i = slider.getValue();
        CombinedDomainXYPlot xyplots = (CombinedDomainXYPlot)jchart.getXYPlot();

        ValueAxis valueaxis = xyplots.getDomainAxis();
        Range range = valueaxis.getRange();
        double d = valueaxis.getLowerBound() + ((double)i / 100D) * range.getLength();
        java.util.List subplots = xyplots.getSubplots();
        for ( int j = 0 ; subplots != null && j < subplots.size() ; j++ ){
        	XYPlot xyplot = ( XYPlot )subplots.get( j );
        	xyplot.setDomainCrosshairValue(d);
        }
    }

	/* (non-Javadoc)
	 * @see ai.simgraph.graph.AbstractGraph#createChart(ai.simgraph.info.plot.AbstractDatasetPlot[])
	 */
	protected JFreeChart createChart(AbstractDatasetPlot[] datasetPlot) {

		if ( datasetPlot == null ) return null;
		this.dataset = (TimeCurveSetPlot[])datasetPlot;

		if ( jchart != null ) jchart = null;

		//Define the same XAxis
		//Suppose that the name of first element is the label of dateaxis
        dateaxis = new DateAxis(this.dataset[0].getXName());
        dateaxis.setTickLabelFont(new Font("SansSerif", 0, 12));
        dateaxis.setLabelFont(new Font("SansSerif", 0, 14));
        dateaxis.setLowerMargin(0.0D);
        dateaxis.setUpperMargin(0.0D);
        dateaxis.setTickLabelsVisible(ConfigFile.isTickLabelsVisible());

        //dateaxis.setMinimumAxisValue( new Millisecond().getMillisecond() );
        dateaxis.setAutoRange( ConfigFile.isTimeAxisAutoRange() );
        dateaxis.setAutoRangeMinimumSize( ConfigFile.getAutoRangeMinimumSize() );

        //Define XYPlot
        combineddomainxyplot = new CombinedDomainXYPlot(dateaxis);
        datasets = new TimeSeries[datasetPlot.length];
        xyplots = new XYPlot[datasetPlot.length];
        for(int i = 0; i < datasetPlot.length; i++)
        {
        	//Define X data set
            datasets[i] =  new TimeSeries( this.dataset[i].getLabel(), org.jfree.data.time.Millisecond.class);
            datasets[i].setHistoryCount( ConfigFile.getGraphHistoryCount() );

            //Define YAxis
            NumberAxis numberaxis = new NumberAxis(this.dataset[i].getYName());
            if ( ConfigFile.isYAxisRangeFixed() ){
            	numberaxis.setAutoRange(ConfigFile.isYAxisRangeFixed());
            	if ( ConfigFile.doesYAxisIncludeZero() )
            		numberaxis.centerRange( 0 );

            	numberaxis.setLowerBound(ConfigFile.getYAxisLowerBound()); //-250);
	            numberaxis.setUpperBound(ConfigFile.getYAxisUpperBound()); //250);
            }
            numberaxis.setAutoRangeIncludesZero(ConfigFile.doesAutoRangeIncludeZero());
            numberaxis.setTickLabelFont(new Font("SansSerif", 0, 12));
            numberaxis.setLabelFont(new Font("SansSerif", 0, 14));

            //Define XYPlot
            xyplots[i] = new XYPlot(new TimeSeriesCollection( datasets[i] ), null, numberaxis, new StandardXYItemRenderer());
            org.jfree.chart.renderer.xy.XYItemRenderer xyitemrenderer = xyplots[i].getRenderer();
            if(xyitemrenderer instanceof StandardXYItemRenderer){
                StandardXYItemRenderer standardxyitemrenderer = (StandardXYItemRenderer)xyitemrenderer;
                standardxyitemrenderer.setPlotShapes(ConfigFile.doesPlotShapes());
                standardxyitemrenderer.setShapesFilled(ConfigFile.isShapesFilled());
                standardxyitemrenderer.setToolTipGenerator( StandardXYToolTipGenerator.getTimeSeriesInstance() );

            }
            xyplots[i].setDomainCrosshairVisible(ConfigFile.isDomainCrosshairVisible() );
            xyplots[i].setRangeCrosshairVisible(ConfigFile.isRangeCrosshairVisible() );
            xyplots[i].setBackgroundPaint( Color.lightGray );
            xyplots[i].setDomainGridlinePaint(Color.white );
            xyplots[i].setRangeGridlinePaint(Color.white );
            combineddomainxyplot.add(xyplots[i]);
        }

        jchart = new JFreeChart(getTitle(), combineddomainxyplot);
        StandardLegend standardlegend = (StandardLegend)jchart.getLegend();
        standardlegend.setDisplaySeriesShapes(false);
        standardlegend.setAnchor(2);

        //Define chart properties
        jchart.setBorderPaint(Color.black );
        jchart.setBorderVisible(ConfigFile.isChartBorderVisible() );
        jchart.setBackgroundPaint(Color.white );
        combineddomainxyplot.setAxisOffset(new Spacer(1, 4D, 4D, 4D, 4D));
        combineddomainxyplot.setGap(10D);
		return jchart;
	}

	/* (non-Javadoc)
	 * @see ai.simgraph.graph.AbstractGraph#createChartPanel(org.jfree.chart.JFreeChart)
	 */
	protected ChartPanel createChartPanel(JFreeChart jfreechart) {
		if ( jChartPanel != null ) jChartPanel = null;

		jChartPanel = new ChartPanel(jfreechart);
		jChartPanel.setVerticalZoom( ConfigFile.isChartVerticalZoom() );
		jChartPanel.setHorizontalZoom( ConfigFile.isChartHorizontalZoom() );
		jChartPanel.setHorizontalAxisTrace( ConfigFile.isChartHorizontalAxisTrace()  );
		jChartPanel.setVerticalAxisTrace( ConfigFile.isChartVerticalAxisTrace() );
		jChartPanel.setPreferredSize(new Dimension(600, 570));
		jChartPanel.setMouseZoomable(true, false);

		jChartPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		return jChartPanel;
	}

	public TimeCurveSetPlot[] getDatasetPlots(){
		return (TimeCurveSetPlot[])(this.dataset);
	}

	private JPanel graphPanel = new JPanel(new BorderLayout());
	private boolean isGraphInit = false;
    private JSlider slider;
	private void initGraphPanel(){
		if ( isGraphInit ) return;

		graphPanel.setOpaque(true);
		graphPanel.add(jChartPanel, "Center");
        JPanel jpane0 = new JPanel(new BorderLayout());
        slider = new JSlider(0, 100, 100);
        slider.addChangeListener(this);
        jpane0.add(slider);

        graphPanel.add(jpane0, "South");
		isGraphInit = true;
	}

	public JPanel getGraphPanel(){
		if ( ! isGraphInit )
			initGraphPanel();

		return graphPanel;
	}

	/**
	 * Add valuepairs of multiple items
	 * @param timesetPlots
	 */
	public void addTimeCurveSetPlots(TimeCurveSetPlot[] timesetPlots){
		if ( timesetPlots == null ) return ;

		for ( int i = 0 ; i < timesetPlots.length ; i++ ){
			addTimeCurveSetPlots( timesetPlots[i].getIndex(), (TimeCurveValuePair[])(timesetPlots[i].getTimeCurveValuePairs()) );
		}
	}

	/**
	 * Add valuepairs of one item
	 * @param index
	 * @param timedataset
	 */
	public void addTimeCurveSetPlots( int index, TimeCurveValuePair[] timedataset){
		TimeCurveSetPlot[] timedatasets = this.getDatasetPlots();

		if ( datasets != null && index >= 0 && index < datasets.length ){
			timedatasets[index].addTimeCurveValuePairs( timedataset );

			for ( int i = 0 ; i < timedataset.length ; i++ ){
				this.datasets[index].add(timedataset[i].getXValue(), timedataset[i].getYValue() );
			}
		}
	}

	public void addTimeCurveSetPlot( int index, TimeCurveValuePair timedataset){
		TimeCurveSetPlot[] timedatasets = this.getDatasetPlots();

		if ( datasets != null && index >= 0 && index < datasets.length ){
			timedatasets[index].addTimeCurveValuePair( timedataset );

			System.out.println( " --- Add " + index + " : " + timedataset.getXValue() + " : " + timedataset.getYValue() );
			this.datasets[index].addOrUpdate(timedataset.getXValue(), timedataset.getYValue() );
		}
	}

	public void hideTimeCurvePlot( int index ){
          if (combineddomainxyplot != null && xyplots != null && index >= 0 &&
              index < xyplots.length)
            combineddomainxyplot.remove(xyplots[index]);

          int i = 0;
	}

	public void reshowTimeCurvePlot( int index ){
		if ( combineddomainxyplot != null && xyplots != null && index >= 0 && index < xyplots.length ){
			combineddomainxyplot.add( xyplots[index] );

			System.out.println( combineddomainxyplot.getSubplots().size() );
		}

		int i = 0;
	}
}
