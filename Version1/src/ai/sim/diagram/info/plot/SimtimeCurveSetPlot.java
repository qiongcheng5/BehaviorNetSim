/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.info.plot;

import org.jfree.data.general.AbstractDataset;

import ai.sim.diagram.info.AbstractValueSet;
import ai.sim.diagram.info.BaseXYValuePair;
import ai.sim.diagram.info.SimtimeCurveValuePair;
import ai.sim.diagram.info.SimtimeCurveValueSet;
import ai.sim.diagram.info.TimeCurveValuePair;
import ai.sim.diagram.info.TimeCurveValueSet;

/**
 * @author Qiong Cheng
 *
 * AbstractDataset's subclass, just for simulation-time and value pairs
 */
public class SimtimeCurveSetPlot extends AbstractDatasetPlot {
	public SimtimeCurveSetPlot( int objIndex, String xName, String yName, String label, SimtimeCurveValueSet[] dataSet ){
		super( objIndex, xName, yName, label );
		
		super.plotDataSet = new SimtimeCurveValueSet( objIndex );
		if ( dataSet != null ){
			super.plotDataSet.addTimeValuePair( dataSet );
		}
	}
	
	public AbstractValueSet getTimeCurveDataSet(){
		return (SimtimeCurveValueSet)this.plotDataSet ;
	}
	
	public BaseXYValuePair[] getTimeCurveValuePairs(){
		return ((SimtimeCurveValueSet)getTimeCurveDataSet()).getSimtimeCurveValuePairs();
	}
	public void addTimeCurveValuePairs(BaseXYValuePair[] dataset){
		((SimtimeCurveValueSet)this.plotDataSet).addTimeValuePairs( dataset );
	}
	
	public void addTimeCurveValuePair(BaseXYValuePair data){
		((SimtimeCurveValueSet)this.plotDataSet).addTimeValuePair( data );
	}	
	
}

