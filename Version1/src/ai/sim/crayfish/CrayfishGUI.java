/*
 * Created on Feb 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ai.sim.crayfish;

import genDevs.modeling.IOBasicDevs;
import genDevs.modeling.digraph;
import genDevs.simulation.realTime.TunableCoordinator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import BehaviorNetwork.Environment;
import BehaviorNetwork.crayfishSys;
import ai.sim.analysis.AnalysisModule;
import ai.sim.crayfish.command.BasicCommandButton;
import ai.sim.crayfish.command.ICommand;
import ai.sim.crayfish.command.Mediator;
import ai.sim.crayfish.command.PauseSimCommand;
import ai.sim.crayfish.command.ResumeSimCommand;
import ai.sim.crayfish.command.StartSimCommand;
import ai.sim.crayfish.command.StopSimCommand;
import ai.sim.crayfish.common.CommonDefine;
import ai.sim.diagram.frame.SimGraphFrame;

/**
 * @author Qiong Cheng
 *
 * CrayfishGUI
 */
public class CrayfishGUI extends JFrame implements IGUI{
    //Modeling & Simulation
    private static digraph model = null;
    private TunableCoordinator sim = null;

    //Mediator
    private Mediator med = null;

	private BasicCommandButton startSimB, stopSimB, pauseSimB, resumeSimB;
	/**
	 * @throws java.awt.HeadlessException
	 */
	public CrayfishGUI(){
		super(CommonDefine.getResourceString(CommonDefine.title));
		init();
	}

	private void init(){
		//Initiate Modeling & Simulation
        model = new crayfishSys("crayfishSys");
        sim = new TunableCoordinator(model);
        sim.initialize();

        med = new Mediator( sim );

		//Build JMenuItems To be left

		JPanel gui = new JPanel(new BorderLayout());

		//Build Buttons
		gui.add( getButtonsPanel(), BorderLayout.NORTH );

		//Build Center Panel -- Display Panel + Analysis Diagram Panel
		gui.add(BorderLayout.CENTER, new JPanel(null).add(createCenterComponent()));

		//Build Status Panel To be left

		getContentPane().add( gui );


	}

	private JPanel buttonsP = null;
	private ICommand simStart, simStop, simPause, simResume;
	protected ICommand[] defaultCommands = {
			simStart,
			simStop,
			simPause,
			simResume
	};

	/**
	 * Buttons Group -- To integrate ICommand, ICommandHolder, Mediator, and IGUI
	 * When event coming, ICommandHolder -> ICommand -> (Execute) > IGUI -> Mediator -> IGUI.Simulator
	 * @return
	 */
	private JPanel getButtonsPanel(){
		if ( buttonsP == null ){
			buttonsP = new JPanel( new FlowLayout());
			startSimB = new BasicCommandButton("Start");
			simStart = new StartSimCommand( this );
			startSimB.setCommand( simStart );
			med.registerStartSim( startSimB );
			buttonsP.add( startSimB );

			stopSimB = new BasicCommandButton("Stop");
			simStop = new StopSimCommand( this );
			stopSimB.setCommand( simStop );
			med.registerStopSim( stopSimB );
			buttonsP.add( stopSimB );

			pauseSimB = new BasicCommandButton("Pause");
			simPause = new PauseSimCommand( this );
			pauseSimB.setCommand( simPause );
			med.registerPauseSim( pauseSimB );
			buttonsP.add( pauseSimB );

			resumeSimB = new BasicCommandButton("Resume");
			simResume = new ResumeSimCommand( this );
			resumeSimB.setCommand( simResume );
			med.registerResumeSim( resumeSimB );
			buttonsP.add( resumeSimB );

	        // add the real-time-factor value label
			URL url = this.getClass().getClassLoader().getResource("timescale");

			if ( url != null )
				realTimeFactorLabel = new JLabel("Time Scale :", new ImageIcon(url),JLabel.RIGHT);
	        else
	        	realTimeFactorLabel = new JLabel("Time Scale :");
			realTimeFactorLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
			realTimeFactorLabel.setForeground(Color.BLUE);
			realTimeFactorLabel.setAlignmentX(0.5f);
	        buttonsP.add(realTimeFactorLabel);

	        // add the real-time-factor slider
	        realTimeFactorSlider = new JSlider();
	        realTimeFactorSlider.setMaximumSize(new Dimension(100, 20));
	        realTimeFactorSlider.setAlignmentX(0.5f);
	        realTimeFactorSlider.setMinimum(0);
	        realTimeFactorSlider.setMaximum(realTimeFactors.length - 1);
	        buttonsP.add(realTimeFactorSlider);

	        // set the real-time-factor slider to the notch that corresponds
	        // with the factor's value
	        for (int i = 0; i < realTimeFactors.length; i++) {
	            if (realTimeFactors[i] == realTimeFactor.get()) {
	            	realTimeFactorSlider.setValue(i);
	                break;
	            }
	        }

	        realTimeScaler = new JTextField( 6 );
	        realTimeScaler.setText(new Double(realTimeFactor.get()).toString() );
	        realTimeScaler.setFont(new Font("SansSerif", Font.PLAIN, 13));
	        realTimeScaler.setForeground(Color.RED);
	        realTimeScaler.setAlignmentX(0.5f);
	        buttonsP.add(realTimeScaler);

	        // this will get the initial real-time-factor value shown by the label
	        realTimeFactor.set(realTimeFactor.get());

	        // when the real-time-factor slider is adjusted
	        realTimeFactorSlider.addChangeListener(new ChangeListener() {
	            public void stateChanged(ChangeEvent e) {
	                // store the new real-time-factor value
	                JSlider slider1 = realTimeFactorSlider;
	                realTimeFactor.set(realTimeFactors[slider1.getValue()]);

	                // if the slider knob isn't still being dragged
	                if (!slider1.getValueIsAdjusting() && sim != null) {
	                    // have the coodinator halt its current sleep period, so
	                    // the new value may take effect right away
	                    sim.interrupt();
	                }
	            }
	        });

		}

		return buttonsP;
	}

	private JSplitPane splitPane;
	JScrollPane left,right;
	protected Component createCenterComponent() {
		left = createLeft();
		right = createRight();

		splitPane =
			new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				left,
				right);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(280);

		return splitPane;
	}

	/**
	 * createLeft
	 */
	//private Display display;
	Environment env;
	JPanel displayP;
	protected JScrollPane createLeft() {
		env = (Environment)((crayfishSys)this.getModel()).withName( "Environment" );

		displayP = env.startDisplayPanel();
		displayP.setVisible( true );
		displayP.show();
		return new JScrollPane( displayP ) ;
	}

	/**
	 * createRight
	 */
	private JPanel diagramP;
	private AnalysisModule ana;
	SimGraphFrame frame;
	protected JScrollPane createRight() {
		ana = (AnalysisModule)((crayfishSys)this.getModel()).withName( "AnalysisModule" );
		diagramP = ana.getAnalysisGraphPanel();
		diagramP.setVisible( true );
		diagramP.show();
		return new JScrollPane( diagramP );
	}

	//<---------To Implement IGUI --------------
	/**
	 * getModel
	 * @return
	 */
	public IOBasicDevs getModel(){
		return (IOBasicDevs)(this.model);
	}

	/**
	 * getSimulator
	 * @return
	 */
	public TunableCoordinator getSimulator(){
		return this.sim;
	}

	/**
	 * getMediator
	 */
	public Mediator getMediator(){
		return this.med;
	}
	//---------To Implement IGUI -------------->

    //<---------To Add TimeScale slidebar ------
	public double getTimeScale() {return sim.getTimeScale();}

    protected double[] realTimeFactors = {.0001, .001, .01, .1, .5, 1, 5, 10, 100, 1000, 10000};
    protected RealTimeFactor realTimeFactor = new RealTimeFactor();
    protected JLabel realTimeFactorLabel;
    protected JTextField realTimeScaler;
    protected JSlider realTimeFactorSlider;
    protected class RealTimeFactor
    {
        /**
         * The value being wrapped.
         */
        private double realTimeFactor =
            realTimeFactors[realTimeFactors.length / 2];

        public double get() {return realTimeFactor;}

        /**
         * Updates the wrapped variable, and performs resulting side effects.
         */
        public void set(double realTimeFactor_)
        {
            realTimeFactor = realTimeFactor_;

            // update the speed label
            if (realTimeFactorLabel != null) {
            	realTimeScaler.setText("" + realTimeFactor);
            }

            // tell the coordinator to use the new speed
            if (sim != null) {
                sim.setTimeScale(realTimeFactor);
            }
        }
    }
	//---------To Add TimeScale slidebar ------>

	public static void main(String[] args) {
		CrayfishGUI crayGui = new CrayfishGUI();
		crayGui.setBounds( 0, 0, 1100, 700 );
		crayGui.show();
		crayGui.ana.startThread() ;

	}
}
