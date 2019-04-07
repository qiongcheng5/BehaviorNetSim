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
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractDatasetPlot {
	private int objIndex;
	protected AbstractValueSet plotDataSet = null;
	
	private String xName;
	private String yName;
	private String label;

	//To be reserved. ??
	private String type; 
	
	public AbstractDatasetPlot( int objIndex, String xName, String yName, String label ){
		this.objIndex = objIndex;
		this.xName = xName;
		this.yName = yName;
		this.label = label;
	}
	
	public int getIndex(){
		return this.objIndex ;
	}
	
	public String getXName(){
		return this.xName ;
	}
	
	public String getYName(){
		return this.yName ;
	}
	
	public String getLabel(){
		return this.label ;
	}
	
	public void setDataSet( AbstractValueSet plotDataset ){
		this.plotDataSet = plotDataset;
	}
	
	public AbstractValueSet getDataSet(){
		return this.plotDataSet ;
	}
	
	public void addDataSet( BaseXYValuePair[] dataset ){
		this.plotDataSet.addTimeValuePair( dataset );
	}
	
	public abstract AbstractValueSet getTimeCurveDataSet();
	
	public abstract BaseXYValuePair[] getTimeCurveValuePairs();
	
	public abstract void addTimeCurveValuePairs(BaseXYValuePair[] dataset);
	
	public abstract void addTimeCurveValuePair(BaseXYValuePair data);
}
