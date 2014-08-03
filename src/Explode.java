import java.awt.Color;
import java.awt.Graphics;

public class Explode {

  private int        x, y;
  private boolean    live       = true;

  private int[]      steps      = {5, 12, 15, 20, 30, 39, 20, 15, 3};
  private int        curentStep = 0;
  private TankClient tankClient;

  public Explode (int x,
                  int y,
                  boolean live,
                  TankClient tc) {
    super ();
    this.x = x;
    this.y = y;
    this.live = live;
    this.tankClient = tc;
  }

  public void draw (Graphics g) {

    if (!live) {
      tankClient.explodes.remove (this);
      return;
    }
    if (curentStep == steps.length - 1) {
      live = false;
      return;
    }

    Color c = g.getColor ();
    g.setColor (Color.ORANGE);
    g.fillOval (x, y, steps[curentStep], steps[curentStep]);
    curentStep++;
    g.setColor (c);

  }

}
