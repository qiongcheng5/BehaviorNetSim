/*
 * Created on Dec 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.info.plot;

import ai.sim.diagram.info.AbstractValueSet;
import ai.sim.diagram.info.BaseXYValuePair;
import ai.sim.diagram.info.TimeCurveValuePair;
import ai.sim.diagram.info.TimeCurveValueSet;

/**
 * @author Qiong Cheng
 *
 * AbstractDatasetPlot's subclass, just for realtime and value pairs
 */
public class TimeCurveSetPlot extends AbstractDatasetPlot{
	/**
	 * Constructor
	 * @param objIndex
	 * @param xName
	 * @param yName
	 * @param label
	 * @param dataSet
	 */
	public TimeCurveSetPlot( int objIndex, String xName, String yName, String label, TimeCurveValueSet[] dataSet ){
		super( objIndex, xName, yName, label );
		
		super.plotDataSet = new TimeCurveValueSet( objIndex );
		if ( dataSet != null ){
			super.plotDataSet.addTimeValuePair( dataSet );
		}
	}
	
	public AbstractValueSet getTimeCurveDataSet(){
		return (TimeCurveValueSet)this.plotDataSet ;
	}
	
	public BaseXYValuePair[] getTimeCurveValuePairs(){
		return ((TimeCurveValueSet)getTimeCurveDataSet()).getTimeCurveValuePairs();
	}
	public void addTimeCurveValuePairs(BaseXYValuePair[] dataset){
		((TimeCurveValueSet)this.plotDataSet).addTimeValuePairs( dataset );
	}
	
	public void addTimeCurveValuePair(BaseXYValuePair data){
		((TimeCurveValueSet)this.plotDataSet).addTimeValuePair( data );
	}	
}
