package BehaviorNetwork;

import ai.sim.analysis.AnalysisModule;
import genDevs.modeling.*;

import simView.*;

public class Behavior extends ViewableAtomic{//atomic{

	public double myExcitation;
	public int myID, id;
	public double inhibition;
	
	EnvInterface envI;
	centralRepository cRps;
	
	//<-- Added by Qiong Cheng 
	int iTimestep = 0 ;
	ai.sim.analysis.AnalysisModule aRps;
	//Added by Qiong Cheng --> 
	
	public Behavior(String nm){ super(nm); }
	public Behavior(){ this("Behavior");  }
	
	
	public void initialize(){
		addInport("inhibitions");
		addInport("selected");
		addOutport("excitation");
		addOutport("out");
	
		// the interface to the environment model and central repository
		envI = (EnvInterface)getModelWithName("EnvInterface");
		cRps = (centralRepository)getModelWithName("centralRepository");
	  
		//<-- Added by Qiong Cheng 
		aRps = (AnalysisModule)getModelWithName("AnalysisModule");
		//Added by Qiong Cheng -->  
	
	  holdIn("nextStep",0);
	}

public void deltext(double e,message x){
  Continue(e);
  if(phaseIs("waitInhibition")){// wait for inhibition from other models
    for (int i = 0; i < x.getLength(); i++) {
      if (messageOnPort(x, "inhibitions", i)) {
        id = ( (int_double_Ent) x.getValOnPort("inhibitions", i)).getX();
        inhibition = ( (int_double_Ent) x.getValOnPort("inhibitions", i)).getY();
        reCalcuExcitation(id, inhibition);
      }
    }
    UpdateToGui();
    holdIn("sendOutput",0);
  }
  else if(phaseIs("nextStep")){ //waiting for next step after sending output
    for (int i = 0; i < x.getLength(); i++) { // there should be only one message
      if (messageOnPort(x, "selected", i)){
          doAction();
          cRps.setCurrentBehavior(myID);
      }
    }
  }
}

public void deltint(){
  if(phaseIs("sendInhibition")) passivateIn("waitInhibition"); // wait for inhibition from other models
  else if(phaseIs("sendOutput")) holdIn("nextStep",configData.timeStep);
  else if(phaseIs("nextStep")){
    calcuExcitation();
    capStrength();
    holdIn("sendInhibition",0);
  }
}

public message out(){
message m = new message();
if(phaseIs("sendInhibition"))
    m.add(makeContent("excitation", new int_double_Ent(myID,myExcitation)));
else if(phaseIs("sendOutput"))
    m.add(makeContent("out", new int_double_Ent(myID,myExcitation)));
return m;
}


//-----------------------------------------------------------------------
public void reCalcuExcitation(int id, double inhibition){
  if(inhibition>=1.0)
    myExcitation = myExcitation - configData.KIN[myID][id]*inhibition;
}

protected void UpdateToGui(){
	//<-- Added by Qiong Cheng 
  	double currentTime=this.getSimulationTime();
  	System.out.println( " step1 -> new data -> Behavior : " + this.getName() + " : " + iTimestep + " : " + myExcitation );
	aRps.writeBuffer( this.getName(), currentTime, myExcitation );
	//Added by Qiong Cheng --
}

public void capStrength(){ // Cap strength of excitation
   if (myExcitation > 20) myExcitation = 20;
}

public void updateEnergy(){
// Update energy
double energy = cRps.Energy + configData.ECOST[myID];
cRps.setEnergy(energy);  // update energy to the central repository
if (energy <= 0){
        System.out.println("crayfish starved at time:"+this.getSimulationTime());
        envI.deActivateCrayfish();
}
  }

public void calcuExcitation(){}  // to be implemented by the child model

public void doAction(){}  // to be implemented by the child model

}