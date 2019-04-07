/*
 * Created on Dec 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.diagram.info;


/**
 * @author Qiong Cheng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractValueSet {
	private int objIndex;
	
	public AbstractValueSet( int objIndex ){
		this.objIndex = objIndex;
	}
	
	public abstract void addTimeValuePair( Object valuePair );
	
	public abstract void addTimeValuePairs( Object[] valuePair );
	
	public abstract void cleanPairSet();
	
	public abstract void fifoReplacePairSet( Object[] valuePair );

}
