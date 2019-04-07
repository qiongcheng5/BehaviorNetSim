/*
 * Created on Feb 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.crayfish.command;

import genDevs.simulation.realTime.TunableCoordinator;


/**
 * @author Qiong Cheng
 *
 * Mediator Pattern
 * The mediator of buttons and simulators
 */
public class Mediator {
	private BasicCommandButton startSimB;
	private BasicCommandButton stopSimB;
	private BasicCommandButton pauseSimB;
	private BasicCommandButton resumeSimB;

	private TunableCoordinator sim; //RTCoupledCoordinator
	public Mediator( TunableCoordinator sim ){
		this.sim = sim;
	}

	/**
	 * Register
	 * @param startSim
	 */
	public void registerStartSim(BasicCommandButton startSim){
		this.startSimB = startSim;

		startSim.setEnabled( true );
	}
	/**
	 * doStartSim
	 *
	 */
	public void doStartSim(){
		//start simulators
		if ( sim != null ){
			//sim.startSimulator(8000);
			sim.simulate( 8000 );
		}

		//select enable buttons
		startSimB.setEnabled( false );
		stopSimB.setEnabled( true );
		pauseSimB.setEnabled( true );
		resumeSimB.setEnabled( false );
	}

	/**
	 * Register
	 * @param startSim
	 */
	public void registerStopSim(BasicCommandButton stopSim){
		this.stopSimB = stopSim;
	}
	/**
	 * doStartSim
	 *
	 */
	public void doStopSim(){
		//stop simulators
		if ( sim != null ){
			//sim.stopSimulate();
			sim.interrupt();
		}

		//select enable buttons
		startSimB.setEnabled( true );
		stopSimB.setEnabled( false );
		pauseSimB.setEnabled( false );
		resumeSimB.setEnabled( false );
	}

	/**
	 * Register
	 * @param startSim
	 */
	public void registerPauseSim(BasicCommandButton pauseSim){
		this.pauseSimB = pauseSim;
	}
	/**
	 * doStartSim
	 *
	 */
	public void doPauseSim(){
		//Pause simulators
		if ( sim != null )
			//sim.pauseSimulator();
			sim.pause();

		//select enable buttons
		startSimB.setEnabled( false );
		stopSimB.setEnabled( true );
		pauseSimB.setEnabled( false );
		resumeSimB.setEnabled( true );
	}

	/**
	 * Register
	 * @param startSim
	 */
	public void registerResumeSim(BasicCommandButton resumeSim){
		this.resumeSimB = resumeSim;
	}
	/**
	 * doStartSim
	 *
	 */
	public void doResumeSim(){
		//resume simulators
		if ( sim != null )
			//sim.resumeSimulator();
			sim.resume();

		//select enable buttons
		startSimB.setEnabled( false );
		stopSimB.setEnabled( true );
		pauseSimB.setEnabled( true );
		resumeSimB.setEnabled( false );
	}

	public static void main(String[] args) {
	}
}
