/*
 * Created on Dec 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.info;

import java.util.ArrayList;

/**
 * @author Qiong Cheng
 *
 * AbstractValueSet's subclass, just for realtime and value pair
 */
public class TimeCurveValueSet extends AbstractValueSet{
	private ArrayList pairSet = null;

	public TimeCurveValueSet( int objIndex ){
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
	
	public void addTimeValuePairs( TimeCurveValuePair[] valuePair ){
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
	
	public TimeCurveValuePair[] getTimeCurveValuePairs(){
		return (TimeCurveValuePair[])this.pairSet.toArray();
	}
}
