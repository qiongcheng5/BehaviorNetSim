package GenCol;

class coordTimer extends Thread{
Thread c;

public coordTimer (Thread C){
c = C;
}

public void run(){
while(c.isAlive()){
  try
        {
          Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
        //System.out.println(e);
        }
}
c.interrupt();
}
}