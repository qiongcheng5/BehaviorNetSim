package BehaviorNetwork;

import java.awt.*;
import javax.swing.*;

import java.util.*;
import java.awt.geom.*;

public class Display extends Thread{

private double DisplayRatio = 1;  // amplify the pictures by this ratio
protected robotFrame rFrame=null;

public Display(Environment env, int WallX, int WallY){
rFrame = new robotFrame(env, (int)(WallX*DisplayRatio), (int)(WallY*DisplayRatio));
rFrame.show();
}

//<-- Added by Qiong Cheng 
protected robotPanel rPanel;
public Display(Environment env){
	rPanel = new robotPanel(env,-1,-1);
	
	//rFrame.show();
	//start();
}

public robotFrame getDisplayFrame(){
	return this.rFrame;
}

public JPanel getDisplayPanel(){
	if ( this.rFrame == null )
		return this.rPanel;
	else
		return this.rFrame.rPanel;
}
//Added by Qiong Cheng -->

public void run(){
while (true){
if ( rFrame != null ) rFrame.repaint();

if ( rPanel != null ) rPanel.repaint();
try{Thread.sleep(10000);} catch(Exception e){}
}
}

class robotFrame extends JFrame{
protected robotPanel rPanel;

public robotFrame(Environment env, int WallX, int WallY){
//dispose();
//setTitle("A Generic Multi-Robot Simulation Environment");

//<-- Added by Qiong Cheng 
if ( WallX != -1 && WallY != -1 )
//Added by Qiong Cheng --> 
setSize(WallX+60, WallY+40);

setBackground(new Color(255,255,255));
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
rPanel = new robotPanel(env,WallX,WallY);
Container contentPane = getContentPane();
contentPane.add(rPanel);
}
}

class robotPanel extends JPanel{
    private Image robotImage, obstacleImage;
    private Image predatorImage;
    public Environment Env;
    int i;
    int RN, ON;
    public int WallX=430, WallY=330;

  public robotPanel(Environment env,int wx, int wy) {
  	setBackground(new Color(255,255,255));
    Env = env;
    if ( wx != -1 )
    	WallX = wx;
    
    if ( wy != -1 )
    	WallY = wy;
    
    robotImage =  Toolkit.getDefaultToolkit().getImage("crayfish.gif");
    predatorImage =  Toolkit.getDefaultToolkit().getImage("fish.gif");
    obstacleImage =  Toolkit.getDefaultToolkit().getImage("rock.gif");
   }

 public void paintObject(Graphics2D g2, Image objectImage, int index, double Width, double Length, double X, double Y, double Angle){
      // set transform, first toCenter then rotate
      AffineTransform at = new AffineTransform();
      //at.rotate(Math.toRadians(Angle));
      at.rotate(Angle);
      AffineTransform toCenterAt = new AffineTransform();
      toCenterAt.translate (X, WallY-Y);   // assume the the (0,0) is at left bottom cornor
      toCenterAt.concatenate(at);
      // save old transform
      AffineTransform saveXform = g2.getTransform ();
      // transform
      g2.transform(toCenterAt);
      // draw
      g2.drawImage(objectImage, (int)(-1*Width/2), (int)(-1*Length/2), (int)Width, (int)Length, this);
      g2.drawString(Integer.toString(index),(int)(Length/3),0);
      // restore old transform
      g2.setTransform(saveXform);
 }

 public void paintComponent( Graphics g)
  {
    Graphics2D g2 = (Graphics2D)g;
    // clear this panel
    this.setLocation(0,0);
    this.setSize(WallX,WallY);
    Dimension d = getSize();
    g.clearRect(0, 0, d.width, d.height);

    ON = Env.SON;
    for(i=0;i<ON;i++)
      paintObject(g2, obstacleImage, i, Env.StaticObj[i].Width, Env.StaticObj[i].Length,
                      Env.StaticObj[i].X, Env.StaticObj[i].Y, -1*Env.StaticObj[i].Angle+Math.PI/2);
    RN = Env.MON;
    for(i=0;i<RN;i++){
      if (i == 0)
        if (Env.MovingObj[i].alive)
          paintObject(g2, robotImage, i, Env.MovingObj[i].Width,
                      Env.MovingObj[i].Length,
                      Env.MovingObj[i].X, Env.MovingObj[i].Y,
                      -1 * Env.MovingObj[i].Angle + Math.PI / 2);
      if (i == 1)
        if (Env.MovingObj[i].alive)
          paintObject(g2, predatorImage, i, Env.MovingObj[i].Width,
                      Env.MovingObj[i].Length,
                      Env.MovingObj[i].X, Env.MovingObj[i].Y,
                      -1 * Env.MovingObj[i].Angle + Math.PI / 2);
    }
 }
}

}