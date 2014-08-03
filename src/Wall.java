import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall {

  int        x, y, height, width;
  TankClient tankClient;

  public Wall (int x,
               int y,
               int height,
               int width,
               TankClient tankClient) {
    this.x = x;
    this.y = y;
    this.height = height;
    this.width = width;
    this.tankClient = tankClient;
  }

  public void draw (Graphics g) {
    g.fillRect (x, y, width, height);
  }

  public Rectangle getRect () {
    return new Rectangle (x, y, width, height);
  }

}
