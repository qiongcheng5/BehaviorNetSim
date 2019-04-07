/*
 * Created on Dec 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.test;

/**
 * @author Qiong Cheng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.time.*;
import org.jfree.ui.*;

import ai.sim.diagram.util.ConfigFile;


public class Test extends ApplicationFrame
{
    static class DemoPanel extends JPanel
        implements ActionListener, ChangeListener
    {
        class DataGenerator extends Timer
	    implements ActionListener
		{

		    public void actionPerformed(ActionEvent actionevent)
		    {
		        Millisecond millisecond = new Millisecond();
		        System.out.println("Now = " + millisecond.toString());
		        for(int j = 0; j < SUBPLOT_COUNT; j++)
		        {
		            lastValue[j] = lastValue[j] * (0.90000000000000002D + 0.20000000000000001D * Math.random());
		            datasets[j].add(new Millisecond(), lastValue[j]);
		        }
		    }
	
		    DataGenerator(int i)
		    {
		        super(i, null);
		        addActionListener(this);
		    }
		}
        
        public void actionPerformed(ActionEvent actionevent)
        {
            for(int i = 0; i < SUBPLOT_COUNT; i++)
                if(actionevent.getActionCommand().endsWith(String.valueOf(i)))
                {
                    Millisecond millisecond1 = new Millisecond();
                    System.out.println("Now = " + millisecond1.toString());
                    lastValue[i] = lastValue[i] * (0.90000000000000002D + 0.20000000000000001D * Math.random());
                    datasets[i].add(new Millisecond(), lastValue[i]);
                }

            if(actionevent.getActionCommand().equals("ADD_ALL"))
            {
                Millisecond millisecond = new Millisecond();
                System.out.println("Now = " + millisecond.toString());
                for(int j = 0; j < SUBPLOT_COUNT; j++)
                {
                    lastValue[j] = lastValue[j] * (0.90000000000000002D + 0.20000000000000001D * Math.random());
                    datasets[j].add(new Millisecond(), lastValue[j]);
                }
                
                //test
                //dateaxis.
            }
        }
        public void stateChanged(ChangeEvent changeevent)
        {
            int i = slider.getValue();
            CombinedDomainXYPlot xyplots = (CombinedDomainXYPlot)jfreechart.getXYPlot();
            
            ValueAxis valueaxis = xyplots.getDomainAxis();
            Range range = valueaxis.getRange();
            double d = valueaxis.getLowerBound() + ((double)i / 100D) * range.getLength();
            java.util.List subplots = xyplots.getSubplots();
            for ( int j = 0 ; subplots != null && j < subplots.size() ; j++ ){
            	XYPlot xyplot = ( XYPlot )subplots.get( j );
            	xyplot.setDomainCrosshairValue(d);
            }
        }
        
        private JFreeChart jfreechart;
        private JSlider slider;
        
        private TimeSeries datasets[];
        private double lastValue[];
        
        public static final int SUBPLOT_COUNT = 5;
        
        private String[] eleLabels = {"Behavior 1", "Behavior 2", "Behavior 3", "Behavior 4", "Behavior 5"}; 
        private String[] yLabels = {"Excitation 1", "Excitation 2", "Excitation 3", "Excitation 4", "Excitation 5" };
        
        CombinedDomainXYPlot combineddomainxyplot;
        DateAxis dateaxis;
        public DemoPanel()
        {
            super(new BorderLayout());
            lastValue = new double[SUBPLOT_COUNT];
            
            dateaxis = new DateAxis("Time");
            dateaxis.setTickLabelFont(new Font("SansSerif", 0, 12));
            dateaxis.setLabelFont(new Font("SansSerif", 0, 14));
            dateaxis.setLowerMargin(0.0D);
            dateaxis.setUpperMargin(0.0D);
            dateaxis.setTickLabelsVisible(ConfigFile.isTickLabelsVisible());
            //dateaxis.setMinimumAxisValue( new Millisecond().getMillisecond() );
            dateaxis.setAutoRange( ConfigFile.isTimeAxisAutoRange() );
            dateaxis.setAutoRangeMinimumSize( ConfigFile.getAutoRangeMinimumSize() );
            //dateaxis.setFixedAutoRange( xFixedAutoRange );
            
            combineddomainxyplot = new CombinedDomainXYPlot(dateaxis);
            datasets = new TimeSeries[SUBPLOT_COUNT];
            for(int i = 0; i < SUBPLOT_COUNT; i++)
            {
            	lastValue[i] = 100D;
                //TimeSeries timeseries = new TimeSeries( eleLabels[i], org.jfree.data.time.Millisecond.class);
                //timeseries.setHistoryCount( historyCount );
                //datasets[i] = new TimeSeriesCollection(timeseries);
                datasets[i] =  new TimeSeries( eleLabels[i], org.jfree.data.time.Millisecond.class);
                datasets[i].setHistoryCount( ConfigFile.getGraphHistoryCount() );
                
                NumberAxis numberaxis = new NumberAxis(yLabels[i]);
                numberaxis.setAutoRangeIncludesZero(ConfigFile.doesAutoRangeIncludeZero());
                numberaxis.setTickLabelFont(new Font("SansSerif", 0, 12));
                numberaxis.setLabelFont(new Font("SansSerif", 0, 14));
            
                XYPlot xyplot = new XYPlot(new TimeSeriesCollection( datasets[i] ), null, numberaxis, new StandardXYItemRenderer());
                org.jfree.chart.renderer.xy.XYItemRenderer xyitemrenderer = xyplot.getRenderer();
                if(xyitemrenderer instanceof StandardXYItemRenderer)
                {
                    StandardXYItemRenderer standardxyitemrenderer = (StandardXYItemRenderer)xyitemrenderer;
                    standardxyitemrenderer.setPlotShapes(ConfigFile.doesPlotShapes());
                    standardxyitemrenderer.setShapesFilled(ConfigFile.isShapesFilled());
                    standardxyitemrenderer.setToolTipGenerator( StandardXYToolTipGenerator.getTimeSeriesInstance() );
                    
                }
                xyplot.setDomainCrosshairVisible(ConfigFile.isDomainCrosshairVisible() );
                xyplot.setRangeCrosshairVisible(ConfigFile.isRangeCrosshairVisible() );
                xyplot.setBackgroundPaint(Color.lightGray );
                xyplot.setDomainGridlinePaint(Color.white );
                xyplot.setRangeGridlinePaint(Color.white );
                combineddomainxyplot.add(xyplot);
              
            }

            jfreechart = new JFreeChart("DEVS Excitation Graph", combineddomainxyplot);
            StandardLegend standardlegend = (StandardLegend)jfreechart.getLegend();
            standardlegend.setDisplaySeriesShapes(false);
            standardlegend.setAnchor(2);
            
            jfreechart.setBorderPaint(Color.black );
            jfreechart.setBorderVisible(ConfigFile.isChartBorderVisible() );
            jfreechart.setBackgroundPaint(Color.white );
            //combineddomainxyplot.setBackgroundPaint(Color.lightGray);
            //combineddomainxyplot.setDomainGridlinePaint(Color.white);
            //combineddomainxyplot.setRangeGridlinePaint(Color.white);
            combineddomainxyplot.setAxisOffset(new Spacer(1, 4D, 4D, 4D, 4D));
            
            ChartPanel chartpanel = new ChartPanel(jfreechart);
            chartpanel.setVerticalZoom( ConfigFile.isChartVerticalZoom() );
            chartpanel.setHorizontalZoom( ConfigFile.isChartHorizontalZoom() );
            chartpanel.setHorizontalAxisTrace( ConfigFile.isChartHorizontalAxisTrace()  );
            chartpanel.setVerticalAxisTrace( ConfigFile.isChartVerticalAxisTrace() );
            //chartpanel.setAutoscrolls( true );
            //add(chartpanel);

            chartpanel.setPreferredSize(new Dimension(600, 570));
            chartpanel.setMouseZoomable(true, false);
            add(new JScrollPane (chartpanel), "North");
            
            JPanel jpane0 = new JPanel(new BorderLayout());
            slider = new JSlider(0, 100, 100);
            slider.addChangeListener(this);
            jpane0.add(slider);
            add(jpane0, "Center");
            
            JPanel jpanel = new JPanel(new FlowLayout());
            for(int j = 0; j < SUBPLOT_COUNT; j++)
            {
                JButton jbutton1 = new JButton("Series " + j);
                jbutton1.setActionCommand("ADD_DATA_" + j);
                jbutton1.addActionListener(this);
                jpanel.add(jbutton1);
            }

            JButton jbutton = new JButton("ALL");
            jbutton.setActionCommand("ADD_ALL");
            jbutton.addActionListener(this);
            jpanel.add(jbutton);
            add(jpanel, "South");
            chartpanel.setPreferredSize(new Dimension(600, 570));
            chartpanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        }
        
    }

    static private DemoPanel demoPanel;
    public Test(String s)
    {
        super(s);
        demoPanel = new DemoPanel();
        setContentPane(demoPanel);
    }

    public static JPanel createDemoPanel()
    {
        return new DemoPanel();
    }

    public static String getDemoDescription()
    {
        return "A time series chart with dynamic updates.";
    }

    public static void main(String args[])
    {
        Test test = new Test(ConfigFile.getGraphWindowTitle());
        test.pack();
        RefineryUtilities.centerFrameOnScreen(test);
        test.setVisible(true);
        (demoPanel.new DataGenerator(100)).start();
    }
}
