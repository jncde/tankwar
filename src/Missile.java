import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Missile {

  private static final int XSPEED = 10;
  private static final int YSPEED = 10;
  int                      x, y;
  Tank.Direction           dir;

  public static final int  HEIGHT = 10;
  public static final int  WIDTH  = 10;

  private boolean          live   = true;

  private boolean          good;
  private TankClient       tankClient;

  public Missile (int x,
                  int y,
                  boolean good,
                  Tank.Direction dir) {
    this.x = x;
    this.y = y;
    this.dir = dir;
    this.good = good;
  }

  public Missile (int x,
                  int y,
                  boolean good,
                  Tank.Direction dir,
                  TankClient tc) {
    this (x, y, good, dir);
    this.tankClient = tc;
  }

  public void draw (Graphics g) {
    if (!live) {
      tankClient.missiles.remove (this);
      return;
    }

    Color c = g.getColor ();
    g.setColor (Color.BLACK);
    g.fillOval (x, y, HEIGHT, WIDTH);
    g.setColor (c);

    move ();
  }

  private void move () {
    switch (dir) {
      case L:
        x -= XSPEED;
        break;
      case LU:
        x -= XSPEED;
        y -= YSPEED;
        break;
      case U:
        y -= YSPEED;
        break;
      case RU:
        x += XSPEED;
        y -= YSPEED;
        break;
      case R:
        x += XSPEED;
        break;
      case RD:
        x += XSPEED;
        y += YSPEED;
        break;
      case D:
        y += YSPEED;
        break;
      case LD:
        x -= XSPEED;
        y += YSPEED;
        break;
    }

    if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
      live = false;

    }
  }

  public boolean isLive () {
    return live;
  }

  public Rectangle getRect () {
    return new Rectangle (x, y, WIDTH, HEIGHT);
  }

  public boolean hitTank (Tank t) {

    if (this.live && this.good != t.isGood () && this.getRect ().intersects (t.getRect ()) && t.isLive ()) {
      t.setLive (false);
      live = false;
      tankClient.explodes.add (new Explode (x, y, true, tankClient));
      return true;
    }
    return false;
  }

  public boolean hitTanks (List<Tank> tanks) {

    for (int i = 0; i < tanks.size (); i++) {
      Tank tank = tanks.get (i);
      if (hitTank (tank)) {
        tanks.remove (tank);
        return true;
      }
    }

    return false;

  }

  public boolean hitWall (Wall w) {
    if (this.live && this.getRect ().intersects (w.getRect ())) {
      this.live = false;
      tankClient.missiles.remove (this);
      return true;
    }
    return false;
  }

  public boolean hitWalls (List<Wall> walls) {

    for (int i = 0; i < walls.size (); i++) {
      if (hitWall (walls.get (i))) {
        return true;
      }
    }

    return false;
  }
}
