import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Tank {

  private static final int FIRE_RANDOM_INTERVAL = 50;
  public static final int  HEIGHT               = 30;
  public static final int  WIDTH                = 30;
  public static final int  XSPEED               = 5;
  public static final int  YSPEED               = 5;

  private boolean          good;
  private boolean          live                 = true;

  private int              x, y;
  private boolean          bL                   = false, bR = false, bU = false, bD = false;

  private TankClient       tankClient;
  private int              step                 = random.nextInt (12) + 3;

  private Dir              dir                  = Dir.STOP;

  private Dir              ptDir                = Dir.D;

  public static Random     random               = new Random ();

  public Tank (int x,
               int y,
               boolean good,
               Dir dir) {
    this.x = x;
    this.y = y;
    this.good = good;
    this.dir = dir;
  }

  public Tank (int x,
               int y,
               boolean good,
               Dir dir,
               TankClient tankClient) {
    this (x, y, good, dir);
    this.tankClient = tankClient;
  }

  public void draw (Graphics g) {

    if (!live) {
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

    if (this.dir != Dir.STOP) {
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
      Dir[] dirs = Dir.values ();
      int randomInt = random.nextInt (dirs.length);
      this.dir = dirs[randomInt];
      step = 5 + random.nextInt (12);

    }

    /**
     * fire
     */
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
      dir = Dir.L;
    } else if (bL && bU && !bR && !bD) {
      dir = Dir.LU;
    } else if (!bL && bU && !bR && !bD) {
      dir = Dir.U;
    } else if (!bL && bU && bR && !bD) {
      dir = Dir.RU;
    } else if (!bL && !bU && bR && !bD) {
      dir = Dir.R;
    } else if (!bL && !bU && bR && bD) {
      dir = Dir.RD;
    } else if (!bL && !bU && !bR && bD) {
      dir = Dir.D;
    } else if (bL && !bU && !bR && bD) {
      dir = Dir.LD;
    } else if (!bL && !bU && !bR && !bD) {
      dir = Dir.STOP;
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

      default:
        break;
    }

    locateDirection ();
  }

  public Missile fire () {
    if (!live) {
      return null;
    }
    int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
    int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
    Missile m = new Missile (x, y, good, ptDir, tankClient);
    tankClient.missiles.add (m);
    return m;
  }

  public Rectangle getRect () {
    return new Rectangle (x, y, WIDTH, HEIGHT);
  }

  public void setLive (boolean live) {
    this.live = live;
  }

  public boolean isLive () {
    return live;
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

}
