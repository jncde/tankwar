import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Blood {

  int        x, y, w, h;
  TankClient tankClient;

  public boolean isAlive () {
    return alive;
  }

  public void setAlive (boolean alive) {
    this.alive = alive;
  }

  private boolean alive     = true;

  private int[][] positions = { {150, 300}, {200, 300}, {375, 375}, {300, 400}, {260, 370}, {235, 390}, {230, 340},
      {200, 200}            };

  private int     step      = 0;

  public Blood () {
    x = positions[0][0];
    y = positions[0][1];
    w = 10;
    h = 10;
  };

  public void draw (Graphics g) {
    if (!alive) {
      return;
    }
    Color c = g.getColor ();
    g.setColor (Color.MAGENTA);
    g.fillRect (x, y, w, h);
    g.setColor (c);
    move ();
  }

  private void move () {
    step++;
    if (step == positions.length) {
      step = 0;
    }

    x = positions[step][0];
    y = positions[step][1];
  }

  public Rectangle getRect () {
    return new Rectangle (x, y, w, h);
  }

}
