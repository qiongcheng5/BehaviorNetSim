/*
 * Created on Jan 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.analysis;

import java.util.Iterator;

import org.jfree.data.time.Millisecond;

import simView.ViewableAtomic;
import simView.ViewableComponent;
import simView.ViewableDigraph;

import ai.sim.analysis.handlers.dataobj.impl.RealtimeExcitHandledDataObj;
import ai.sim.analysis.handlers.impl.RingBufferActOutHandler;
import ai.sim.diagram.frame.SimGraphFrame;
import ai.sim.diagram.frame.SimGraphPanel;
import ai.sim.diagram.util.ConfigFile;

import BehaviorNetwork.BehaviorNet;
import BehaviorNetwork.Crayfish;
import BehaviorNetwork.Environment;
import BehaviorNetwork.Predator;
import genDevs.modeling.ComponentsInterface;
import genDevs.modeling.IOBasicDevs;
import genDevs.modeling.abstractActivity;
import genDevs.modeling.componentIterator;

/**
 * @author Qiong Cheng
 *
 * From the modeling point of view, i think, it is necessary to provide the connection
 * between behavior's outport and AnalysisModule inport.
 *
 * In the future version, different modes will be provided to meet different analysis needs.
 */
public class AnalysisModule extends abstractActivity {
	Environment env;
	Predator predator;
	BehaviorNet behaviorNet;
	Crayfish crayfish;

	//For CrayfishMain
	private boolean isGui = true;
	SimGraphFrame analysisGraph;
	SimGraphPanel analysisGraphP;

	RingBufferActOutHandler outhandler = new RingBufferActOutHandler( this );
	public AnalysisModule(String nm){
		super(nm);
    }

	/**
	 * start thread
	 */
	public void startThread(){
		outhandler.start();
	}

	public void destroy(){
		outhandler.interrupt();
		outhandler = null;
		if ( ! isGui )
			analysisGraph = null;
		else
			analysisGraphP = null;
	}

	/**
	 * Constructor Function
	 *
	 */
    public AnalysisModule(){
    	this("AnalysisModule");
    }

    /**
     * datainit
     *
     */
    private void datainit(){
    	if (env == null) env = (Environment)getModelWithName("Environment");
    	if (predator == null) predator = (Predator)getModelWithName("predator");
    	if (crayfish == null) crayfish = (Crayfish)getModelWithName("crayfish");

    	if (behaviorNet == null) behaviorNet = ( BehaviorNet )getChildComponentByName( crayfish, "BehaviorNet" );

    	if ( ! isGui ){
    		analysisGraph = new SimGraphFrame(getAllConnBehaviorsSize(), getAllBehaviorNetNames(),  getAllInputMessageNames());
    	}else{
    		analysisGraphP = new SimGraphPanel(getAllConnBehaviorsSize(), getAllBehaviorNetNames(),  getAllInputMessageNames());
    	}


    }

    /**
     * getAnalysisGraphFrame
     * @return
     */
	//Deleted 02.19
    /*
    public SimGraphPanel getAnalysisGraphPanel(){
    	if (analysisGraphP == null){
    		analysisGraphP = new SimGraphPanel(getAllConnBehaviorsSize(), getAllBehaviorNetNames(),  getAllInputMessageNames());
    		analysisGraphP.show();
    	}
    	System.out.println( "getAnalysisGraphPanel" + analysisGraphP.getGraphModel().getName());
    	return analysisGraphP;
    }

    public SimGraphFrame getAnalysisGraphFrame(){
    	if (analysisGraph == null) {
    		analysisGraph = new SimGraphFrame(getAllConnBehaviorsSize(), getAllBehaviorNetNames(),  getAllInputMessageNames());
    		analysisGraph.show();
    		analysisGraph.pack();
		}
    	System.out.println( "getAnalysisGraphFrame" + analysisGraph.getName());
    	return analysisGraph;
    }*/

    /**
     *
     * @return
     */
	//Added 02.19
    public SimGraphFrame getAnalysisGraphFrame(){
    	return this.analysisGraph;
    }

    public SimGraphPanel getAnalysisGraphPanel(){
    	return this.analysisGraphP ;
    }

    /**
     * initialize
     */
    public void initialize(){
    	datainit();

    	passivate();

    	System.out.println("---AnalysisModule -- finish initiation");
    }

    /**
     * Complement function.
     * ?? Location ??
     * @param parent
     * @param modelName
     * @return
     */
    public ViewableComponent getChildComponentByName( ViewableDigraph parent, String modelName ){
		if ( parent == null || modelName == null ) return null;

    	try{

    		ComponentsInterface setInterface = parent.getComponents();

    		Iterator set = setInterface.iterator();

    		while ( set.hasNext() ){
    			Object obj = set.next();
    			if ( obj != null && obj instanceof ViewableDigraph ){
    				if ( modelName.equalsIgnoreCase( ( (ViewableDigraph)obj ).getName() ) ){
    					return ( ViewableDigraph )obj;
    				}else{
    					return getChildComponentByName( ( ViewableDigraph )obj, modelName );
    				}
    			}else if ( obj != null && obj instanceof ViewableAtomic ){
    				if ( modelName.equalsIgnoreCase( ( (ViewableAtomic)obj ).getName() ) ){
    					return ( ViewableDigraph )obj;
    				}
    			}
    		}

    	}catch ( Exception ex ){
    		ex.printStackTrace() ;

    		int i = 0;
    	}

    	return parent;
    }

    /**
     * ?? To be discussed ??
     * The function will be changed if addcoupling would be admitted
     * between AnalysisModule and behaviors
     * @return
     */
    public String[] getAllBehaviorNetNames(){
    	if ( behaviorNet == null ) return null;

    	ComponentsInterface compset = behaviorNet.getComponents();
    	int iSize = behaviorNet.getComponents().size();
    	//ArrayList names = new ArrayList();
    	String[] names = new String[iSize];
    	int i = 0;
    	componentIterator iterator = compset.cIterator();
    	//?? It is not to walk through the hierarchy tree structure
    	//?? Seemly It is supposed as a parallel structure
    	while ( iterator.hasNext() ){
    		IOBasicDevs baseD = iterator.nextComponent();
    		names[ i ] = (baseD.getName());
    		i++;
    	}
    	return ( names != null && names.length > 0 ?  names : null );
    }

    /**
     * ?? To be discussed ??
     * The function will be changed if addcoupling would be admitted
     * between AnalysisModule and behaviors
     * @return
     */
    public int getAllConnBehaviorsSize(){
       	if ( behaviorNet == null ) return 0;

       	//behavior names
    	String[] bnames = getAllBehaviorNetNames();

    	//message names
    	if ( bnames == null ) return 0;
    	else return bnames.length;
    }

    /**
     * ?? To be discussed ??
     * The function will be changed if addcoupling would be admitted
     * between AnalysisModule and behaviors
     * @return
     */
    public String[] getAllInputMessageNames(){
       	if ( behaviorNet == null ) return null;

       	//behavior names
    	String[] bnames = getAllBehaviorNetNames();

    	//message names
    	if ( bnames == null ) return null;

    	String[] mnames = new String[ bnames.length ];
    	for ( int i = 0 ; i < bnames.length; i++ ){
    		mnames[i] = (bnames[i].length()> 2 ? bnames[i].substring( 0, 2 ): bnames[i]) + ai.sim.diagram.util.ConfigFile.getExitYAxisLabel();
    	}
    	return mnames;
    }

    /**
     * Get the index
     *
     * @param behaviorName
     * @return
     */
    public int getIndex( String behaviorName ){
       	if ( behaviorName == null || behaviorNet == null ) return -1;

       	//behavior names
    	String[] bnames = getAllBehaviorNetNames();

    	if ( bnames == null ) return -1;
    	for ( int i = 0 ; i < bnames.length; i++ ){
    		if ( behaviorName.equalsIgnoreCase( bnames[i] ) ){
    			return i;
    		}
    	}
    	return -1;
    }

    /**
     * Only invoked by behavior object
     * @param myname
     * @param myvalue
     */
    public void writeBuffer( String myname, double iTimestep, double myvalue ){
    	if ( (analysisGraphP == null &&  analysisGraph == null ) || myname == null || outhandler == null ){
    		System.out.println( "Null pointer");
    		return;
    	}

    	int index = getIndex( myname );
    	if ( index < 0 ){
    		System.out.println( "index <0" );
    		return ;
    	}

		if ( ai.sim.diagram.util.ConfigFile.isSimTimeOfX() ){
			outhandler.add( new RealtimeExcitHandledDataObj( index, new Double( iTimestep ), myvalue));
		}else
			outhandler.add( new RealtimeExcitHandledDataObj( index, new Millisecond(), myvalue));
    }

	public static void main(String[] args) {
	}
}
