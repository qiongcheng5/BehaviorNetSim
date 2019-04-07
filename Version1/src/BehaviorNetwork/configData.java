package BehaviorNetwork;

/**
 * Constants shared by the classes in this package.
 */
public interface configData
{
  double timeStep = 0.5;

  public static final double DEF_ENERGY = 2;

  public static final int numOfBehavior =8;

  public static final int REST = 0;
  public static final int ESCAPE = 1;
  public static final int RETREAT = 2;
  public static final int DEFEND = 3;
  public static final int HIDE = 4;
  public static final int EAT = 5;
  public static final int FORAGE = 6;
  public static final int SWIM = 7;
  public static final int APPROACH = 8;
  public static final int ATTACK = 9;
  public static final int DEAD = -1;

  public static final String[] BEHAVIOUR_NAMES = {
          "Rest", "Escape", "Retreat", "Defend", "Hide",
          "Eat", "Forage", "Swim", "Approach", "Attack"
  };

  public static final double NIBBLE_AMT = 0.05;
  public static final double KLENGTH_DEF = 135;
  public static final double KLENGTH_RET = 45;
  public static final double KLENGTH_ESC = 15;
  public static final double RETREAT_SENSITIVITY = 0.1;
  public static final double MINTHRESHHOLD = 4.0;

  // Default behaviour strengths, energy, movement rates etc.
  public static final double[] MOVE = { 0, 50, 2, 0, 0, 0, 2, 25, 2, 0 }; //move speed
  public static final double[] ECOST =   // energy cost
          { -0.002, -0.02, -0.004, -0.002, -0.002,
             NIBBLE_AMT, -0.004, -0.01, -0.004, -0.02 };
  public static final double[] KEX = { 0, 45, 15, 8, 6, 500, 800, 1, 20, 20};
  public static final double[][] KIN = {
          {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0},
          {0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.2, 1.0, 0, 0},
          {0.0, 1.0, 0.0, 0.5, 0.5, 0.5, 0.2, 0.5, 0.5, 0},
          {0.0, 1.0, 0.5, 0.0, 0.0, 0.5, 0.2, 0.5, 0.5, 0},
          {0.0, 1.0, 0.0, 0.5, 0.0, 0.0, 0.5, 0.0, 0.5, 0.5},
          {0.0, 1.0, 0.5, 0.5, 0.0, 0.0, 0.0, 0.5, 0, 0},
          {0.0, 1.0, 0.5, 0.5, 0.5, 1.0, 0.0, 0.5, 0, 0},
          {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.2, 0.0, 0, 0},
          {0.0, 1.0, 0.5, 0.5, 0.2, 0.5, 0.5, 0.0, 0, 1.0},
          {0.0, 1.0, 0.0, 0.0, 0.2, 0.5, 0.5, 0.0, 0, 0}
  };


}
