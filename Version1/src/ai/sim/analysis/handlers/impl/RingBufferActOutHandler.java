/*
 * Created on Jan 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.analysis.handlers.impl;

import ai.sim.analysis.AnalysisModule;
import ai.sim.analysis.handlers.AbstractCollectedDataHandler;
import ai.sim.analysis.handlers.dataobj.AbstractHandledDataObj;
import ai.sim.analysis.handlers.dataobj.impl.RealtimeExcitHandledDataObj;
import ai.sim.analysis.util.ConfigFile;
import ai.sim.diagram.frame.SimGraphFrame;
import ai.sim.diagram.frame.SimGraphPanel;
/**
 * @author Qiong Cheng
 *
 * Using fixed length array as the structure of the collected data
 */
public class RingBufferActOutHandler extends AbstractCollectedDataHandler {
	private static int buflen = ConfigFile.getRingBufferSize();
	
	//ring buffer
	private AbstractHandledDataObj[] ringbuf = null;
	
	private static AnalysisModule amodule;
	
	//header & tail; According to the FIFO rule of queue
	private int ihead = 0;
	private int itail = 0;
	
	public RingBufferActOutHandler(){
		ringbuf = new AbstractHandledDataObj[ buflen ];
		for ( int i = 0; i < buflen ; i++ ){
			ringbuf[i] = null;
		}
	}
	
	SimGraphPanel simgraphP;
	public RingBufferActOutHandler( AnalysisModule amodule){
		this();
		this.amodule = amodule;

	}
	
	/* (non-Javadoc)
	 * @see ai.sim.handlers.AbstractCollectedDataHandler#handleOutput()
	 */
	public void handleOutput() {
		//while ( ihead == itail && ringbuf[ihead] != null ){
		while ( ringbuf[ihead] != null ){
			System.out.println( "---Ready to write simulation data-------");
			// To do display
			displayData( (RealtimeExcitHandledDataObj)ringbuf[ihead] );
			
			// To write file or DB
			saveHistory();
		
			// To update the state
			// If all steps are finished, delete these records
			// To move the point of ihead and itail
			remove();
			
	        //try{Thread.sleep(2000);} //time granule --- wait 
            //catch (Exception e){}
		}
	}
	
	public void saveHistory(){
		
	}
	
	public void displayData(RealtimeExcitHandledDataObj obj){
		if ( this.amodule == null ) return;
		
		SimGraphFrame simgraph = this.amodule.getAnalysisGraphFrame();
		if ( simgraph != null ){
			simgraph.addAnalysisPair( obj.getID(), obj.getTimestamp(), new Double(obj.getValue()) );
		}else{
			SimGraphPanel simgraphP = this.amodule.getAnalysisGraphPanel();
			if ( simgraphP != null )
				simgraphP.addAnalysisPair( obj.getID(), obj.getTimestamp(), new Double(obj.getValue()) );
			else
				System.out.println( " SimGraph null in RingBufferActOutHandler object");
		}
	}
	
	public boolean isFilled(){
		if ( itail != ihead )
			return true;
		
		return false;
	}
	public boolean isFull(){
		if ( itail == ihead && ringbuf[itail] != null )
			return true;
		
		return false;
	}
	
	public boolean isEmpty(){
		if ( itail == ihead && ringbuf[itail] == null )
			return true;
		
		return false;
	}
	/**
	 * Add
	 * @param obj
	 */
	public synchronized void add( AbstractHandledDataObj obj ){
		ringbuf[itail] = obj;
		
		itail++;
		System.out.println( "--add to Ringbuffer" + itail + " : " + (itail % buflen) + " : " + (itail / buflen) + " : " + ihead );
		if ( itail % buflen == 0 )
			itail = 0;
		
		if ( itail == ihead ){
			//two cases, full or empty
			if ( ringbuf[ itail ] != null ){ //Full
				System.out.println( "---RingBuffer, Full, To handle output" );
				handleOutput();
			}
		}else{
			System.out.println( "--wait for the full buffer");
		}
		
	}
	
	/**
	 * Remove
	 */
	public synchronized void remove(){
		System.out.println( "--remove from Ringbuffer");
		if ( itail == ihead && ringbuf[ ihead ] == null ){
			System.out.println( "The queue is empty." );
		}else{
			ringbuf[ ihead ] = null;
			
			ihead++;
			if ( ihead % buflen == 0 )
				ihead = 0;
		}
	}
	
	public static void main(String[] args) {
	}
}
