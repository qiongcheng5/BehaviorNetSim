/*
 * Created on Jan 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.analysis.handlers.dataobj;

/**
 * @author Qiong Cheng
 *
 * The object of data which are not related with DEVS viewable activity
 */
public class AbstractHandledDataObj {
	//id is the unique identity that represents the activity(behavior in behaviornet) and its output
	private int id;

	//the state which handle data; the default value is zero. ("new state")
	private int state = 0;

	//the timestamp of millisecond unit
	private Object timestamp;

	//the value of output
	private Object outValue;

	public AbstractHandledDataObj( ){
	}

	public AbstractHandledDataObj( int id, Object timestamp, Object value ){
		this.timestamp = timestamp;
		this.outValue = value;
		this.id = id;
	}

	public int getID(){
		return this.id;
	}

	public void setID( int id ){
		this.id = id;
	}

	public int getState(){
		return this.state;
	}

	public void setState( int state ){
		this.state = state;
	}

	protected Object getOutTimestamp(){
		return this.timestamp;
	}

	protected void setOutTimestamp( Object timestamp ){
		this.timestamp = timestamp;
	}

	//TODO the function of outValue
	// They will be implemented by the sub classes
	protected Object getOutValue(){
		return this.outValue;
	}

	protected void setOutValue( Object value ){
		this.outValue = value;
	}

}
