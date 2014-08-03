import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {

  public int getLife () {
    return life;
  }

  public void setLife (int life) {
    this.life = life;
  }

  private static final int FIRE_RANDOM_INTERVAL = 50;
  public static final int  HEIGHT               = 30;
  public static final int  WIDTH                = 30;
  public static final int  XSPEED               = 5;
  public static final int  YSPEED               = 5;
  private final int        initLife;
  private int              lastX, lastY;

  private boolean          good;
  private boolean          alive                = true;

  private int              x, y;
  private boolean          bL                   = false, bR = false, bU = false, bD = false;

  private TankClient       tankClient;
  private int              step                 = random.nextInt (12) + 3;

  private int              life;
  private final BloodBar   bb;

  public enum Direction {
    L,
    LU,
    U,
    RU,
    R,
    RD,
    D,
    LD,
    STOP
  };

  private Direction    dir    = Direction.STOP;

  private Direction    ptDir  = Direction.D;

  public static Random random = new Random ();

  public Tank (int x,
               int y,
               boolean good,
               Direction dir,
               int life) {
    this.x = x;
    this.y = y;
    this.lastX = x;
    this.lastY = y;
    this.good = good;
    this.dir = dir;
    this.life = life;
    initLife = life;

    bb = new BloodBar (good ? Color.RED : Color.BLUE);

  }

  public Tank (int x,
               int y,
               boolean good,
               Direction dir,
               int life,
               TankClient tankClient) {
    this (x, y, good, dir, life);
    this.tankClient = tankClient;
  }

  public void draw (Graphics g) {

    if (!alive) {
      if (!good) {
        tankClient.enemies.remove (this);
      }
      return;
    }
    Color color = g.getColor ();
    if (good) {
      g.setColor (Color.RED);
    } else {
      g.setColor (Color.BLUE);
    }
    g.fillOval (x, y, WIDTH, HEIGHT);
    bb.draw (g);
    g.setColor (color);

    switch (ptDir) {
      case L:
        g.drawLine (x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT / 2);
        break;
      case LU:
        g.drawLine (x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y);
        break;
      case U:
        g.drawLine (x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y);
        break;
      case RU:
        g.drawLine (x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y);
        break;
      case R:
        g.drawLine (x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT / 2);
        break;
      case RD:
        g.drawLine (x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT);
        break;
      case D:
        g.drawLine (x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y + Tank.HEIGHT);
        break;
      case LD:
        g.drawLine (x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT);
        break;

    }

    lastX = x;
    lastY = y;

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
      case STOP:
        break;

    }

    if (this.dir != Direction.STOP) {
      this.ptDir = dir;
    }

    if (x < 0) {
      x = 0;
    }
    if (y < 30) {
      y = 30;
    }
    if (x + Tank.WIDTH > TankClient.GAME_WIDTH) {
      x = TankClient.GAME_WIDTH - Tank.WIDTH;
    }
    if (y + Tank.HEIGHT > TankClient.GAME_HEIGHT) {
      y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
    }

    if (!good & step == 0) {
      Direction[] dirs = Direction.values ();
      int randomInt = random.nextInt (dirs.length);
      this.dir = dirs[randomInt];
      step = 5 + random.nextInt (12);

    }

    if (!good & random.nextInt (FIRE_RANDOM_INTERVAL) > 48) {
      fire ();
    }

    step--;

  }

  public void keyPressed (KeyEvent e) {
    int key = e.getKeyCode ();
    switch (key) {
      case KeyEvent.VK_LEFT:
        bL = true;
        break;
      case KeyEvent.VK_RIGHT:
        bR = true;
        break;
      case KeyEvent.VK_UP:
        bU = true;
        break;
      case KeyEvent.VK_DOWN:
        bD = true;
        break;

    }

    locateDirection ();

  }

  private void locateDirection () {
    if (bL && !bU && !bR && !bD) {
      dir = Direction.L;
    } else if (bL && bU && !bR && !bD) {
      dir = Direction.LU;
    } else if (!bL && bU && !bR && !bD) {
      dir = Direction.U;
    } else if (!bL && bU && bR && !bD) {
      dir = Direction.RU;
    } else if (!bL && !bU && bR && !bD) {
      dir = Direction.R;
    } else if (!bL && !bU && bR && bD) {
      dir = Direction.RD;
    } else if (!bL && !bU && !bR && bD) {
      dir = Direction.D;
    } else if (bL && !bU && !bR && bD) {
      dir = Direction.LD;
    } else if (!bL && !bU && !bR && !bD) {
      dir = Direction.STOP;
    }
  }

  public void keyReleased (KeyEvent e) {
    int key = e.getKeyCode ();
    switch (key) {
      case KeyEvent.VK_LEFT:
        bL = false;
        break;
      case KeyEvent.VK_RIGHT:
        bR = false;
        break;
      case KeyEvent.VK_UP:
        bU = false;
        break;
      case KeyEvent.VK_DOWN:
        bD = false;
        break;
      case KeyEvent.VK_CONTROL:
        fire ();
        break;
      case KeyEvent.VK_A:
        supperfire ();
        break;

      default:
        break;
    }

    locateDirection ();
  }

  private void supperfire () {
    for (int i = 0; i < 8; i++) {
      fireToDirection (Direction.values ()[i]);
    }

  }

  public Missile fire () {
    return fireToDirection (ptDir);
  }

  private Missile fireToDirection (Direction d) {
    if (!alive) {
      return null;
    }
    int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
    int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
    Missile m = new Missile (x, y, good, d, tankClient);
    tankClient.missiles.add (m);
    return m;
  }

  public boolean collideWithWall (Wall w) {

    if (this.alive & this.getRect ().intersects (w.getRect ())) {
      stay ();
      return true;
    }
    return false;
  }

  public boolean collideWithWalls (List<Wall> walls) {

    if (!this.alive) {
      return false;
    }

    for (int i = 0; i < walls.size (); i++) {
      if (collideWithWall (walls.get (i))) {
        return true;
      }

    }

    return false;

  }

  public boolean collidesWithTanks (List<Tank> tanks) {

    if (!this.alive) {
      return false;
    }
    for (Tank tank: tanks) {
      if (tank != this) {
        if (collidesWithTank (tank)) {
          return true;
        }
      }
    }

    return false;

  }

  private boolean collidesWithTank (Tank tank) {

    if (tank.getRect ().intersects (this.getRect ())) {
      stay ();
      return true;
    }
    return false;
  }

  private void stay () {
    this.x = lastX;
    this.y = lastY;
  }

  public Rectangle getRect () {
    return new Rectangle (x, y, WIDTH, HEIGHT);
  }

  public void setAlive (boolean alive) {
    this.alive = alive;
  }

  public boolean isAlive () {
    return alive;
  }

  public int getX () {
    return x;
  }

  public int getY () {
    return y;
  }

  public boolean isGood () {
    return good;
  }

  public void eat (Blood b) {
    if (alive && b.isAlive () && this.getRect ().intersects (b.getRect ())) {
      this.life = initLife;
      b.setAlive (false);
    }
  }

  private class BloodBar {

    private final Color bbColor;

    public BloodBar (Color bbColor) {
      this.bbColor = bbColor;
    }

    public void draw (Graphics g) {
      Color c = g.getColor ();
      g.setColor (bbColor);
      g.drawRect (x, y - 15, WIDTH, 10);
      int w = WIDTH * life / initLife;
      g.fillRect (x, y - 15, w, 10);
      g.setColor (c);
    }

  }

}
