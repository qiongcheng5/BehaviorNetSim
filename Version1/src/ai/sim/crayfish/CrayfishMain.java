/*
 * Created on Feb 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.crayfish;

import genDevs.modeling.digraph;
import genDevs.simulation.realTime.TunableCoordinator;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ai.sim.diagram.frame.SimGraphFrame;

import BehaviorNetwork.Environment;
import BehaviorNetwork.crayfishSys;

/**
 * @author Qiong Cheng
 *
 * The interface to provide simulation graph and simulation anlysis diagram
 *
 * ?? TabbedPane  or InternalPanel ??
 * But I found it is not easy to retrieve the reference of Display panel and analysis panel
 * So the version is about buttons to control the TunableCoordinator. ??
 *
 */
public class CrayfishMain extends JFrame implements ActionListener {
    private static JButton jbstart, jbstop, jbpause, jbresume;
    private static String startComm = "Start";
    private static String stopComm = "Stop";
    private static String pauseComm = "Pause";
    private static String resumeComm = "Resume";

	// External resource
	protected static ResourceBundle resources;
	static {
    	try {
	        resources = ResourceBundle.getBundle( "resources.crayfishgui", Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("resources/crayfishgui.properties not found");
			System.exit(1);
		}
	}
	/**
	 * Actions defined by the Graphpad class
	 */
	//protected Action[] defaultActions ={

	//}

    JPanel controller = null;

    TunableCoordinator cs = null;
    //RTcoupledCoordinator cs = null;
    protected static digraph testDig = null;
    /**
     * Constructor
     */
    public CrayfishMain(){
    	super(ConfigFile.getTitle());
    	init();
    }

    /**
     * Initiate
     */
    ai.sim.analysis.AnalysisModule ana;
    BehaviorNetwork.Environment env;
    private void init(){
    	//create menu bars

    	//create main panel including Display and analysis diagram

    	//create controller buttons
    	controller = new JPanel( new FlowLayout() );

    	/**
    	 * Add buttons
    	 */
    	jbstart = new JButton(startComm);
    	jbstart.setActionCommand(startComm);
    	jbstart.addActionListener(this);
    	jbstart.setEnabled( false );
    	controller.add(jbstart);

    	jbstop = new JButton(stopComm);
    	jbstop.setActionCommand(stopComm);
    	jbstop.addActionListener(this);
    	jbstop.setEnabled( false );
    	controller.add(jbstop);

    	jbpause = new JButton(pauseComm);
    	jbpause.setActionCommand(pauseComm);
    	jbpause.addActionListener(this);
    	jbpause.setEnabled( false );
    	controller.add(jbpause);

    	jbresume = new JButton(resumeComm);
    	jbresume.setActionCommand(resumeComm);
    	jbresume.addActionListener(this);
    	jbresume.setEnabled( false );
    	controller.add(jbresume);

        this.getContentPane().add( controller );

        testDig = new crayfishSys("crayfishSys");

		ana = (ai.sim.analysis.AnalysisModule)((crayfishSys)testDig).withName( "AnalysisModule" );
		env = (BehaviorNetwork.Environment)((crayfishSys)testDig).withName( "Environment" );

        cs = new TunableCoordinator(testDig,0.04);//new RTcoupledCoordinator(testDig);

        /*
        cs.initialize();
        cs.simulate(8000);
        */
        jbstart.setEnabled( true );
        jbstop.setEnabled( false );
        jbpause.setEnabled( false );
        jbresume.setEnabled( false );

    }

	/**
	 * Just for test, to simulate the realtime data generation
	 */
    SimGraphFrame frame;
    public void actionPerformed(ActionEvent actionevent)
    {
    	if(actionevent.getActionCommand().equalsIgnoreCase( startComm )){
    		if ( cs != null ){
				cs.initialize();
				//cs.startSimulator(8000);
				cs.simulate( 8000 );
				frame = ana.getAnalysisGraphFrame();
				frame.cShow();
				frame.pack();
				env.startDisplay() ;
				ana.startThread();
    		}

    		jbstart.setEnabled( false );
    		jbstop.setEnabled( true );
    		jbpause.setEnabled( true );
    		jbresume.setEnabled( false );
    	}else if(actionevent.getActionCommand().equalsIgnoreCase( stopComm )){
			//cs.interrupt();
    		//cs.stopSimulator();
    		cs.interrupt();
    		env.destroyDisplay();
    		ana.destroy();
			cs = null;

    		jbstart.setEnabled( true );
    		jbstop.setEnabled( false );
    		jbpause.setEnabled( false );
    		jbresume.setEnabled( false );
    	}else if(actionevent.getActionCommand().equalsIgnoreCase( pauseComm )){
    		//cs.pauseSimulator();
    		cs.pause();

    		jbstart.setEnabled( false );
    		jbstop.setEnabled( true );
    		jbpause.setEnabled( false );
    		jbresume.setEnabled( true );
    	}else if(actionevent.getActionCommand().equalsIgnoreCase( resumeComm )){
    		//cs.resumeSimulator();
    		cs.resume();

    		jbstart.setEnabled( false );
    		jbstop.setEnabled( true );
    		jbpause.setEnabled( true );
    		jbresume.setEnabled( false );
    	}

    }

	public static void main(String[] args) {
		CrayfishMain crayMain = new CrayfishMain();
		crayMain.setBounds( 0, 0, 400, 80 );
		crayMain.show();

	}
}
