/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.info;

import java.util.ArrayList;

/**
 * @author Qiong Cheng
 *
 * AbstractValueSet's subclass, just for simulation-time and value pair
 */
public class SimtimeCurveValueSet extends AbstractValueSet {
	private ArrayList pairSet = null;

	public SimtimeCurveValueSet( int objIndex ){
		super(objIndex);
		this.pairSet = new ArrayList();
	}
	
	public void addTimeValuePair( Object valuePair ){
		if ( valuePair != null )
			this.pairSet.add( valuePair );
	}
	
	public void addTimeValuePairs( Object[] valuePair ){
		for ( int i = 0; valuePair != null && i < valuePair.length ; i++ ){
			this.pairSet.add( valuePair[i] );
		}
	}
	
	public void addTimeValuePairs( SimtimeCurveValuePair[] valuePair ){
		addTimeValuePairs( valuePair );
	}
	
	public void cleanPairSet(){
		if ( this.pairSet != null ){
			this.pairSet = null;
		}
	}
	
	public void fifoReplacePairSet( Object[] valuePair ){
		for ( int i = 0; valuePair != null && i < valuePair.length ; i++ ){
			this.pairSet.remove(0);
			this.pairSet.add( valuePair[i] );
		}
	}
	
	public SimtimeCurveValuePair[] getSimtimeCurveValuePairs(){
		return (SimtimeCurveValuePair[])this.pairSet.toArray();
	}
}
