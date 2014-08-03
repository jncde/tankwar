import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankClient extends Frame {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  public static final int   GAME_HEIGHT      = 600;
  public static final int   GAME_WIDTH       = 800;

  Tank                      myTank           = new Tank (50, 50, true, this);
  Tank                      enemyTank        = new Tank (50, 50, false, this);
  List<Missile>             missiles         = new ArrayList<Missile> ();

  Image                     offScreenImage   = null;

  public static void main (String[] args) {

    TankClient tankClient = new TankClient ();
    tankClient.lauchFrame ();

  }

  public void lauchFrame () {
    this.setLocation (400, 300);
    this.setSize (GAME_WIDTH, GAME_HEIGHT);

    this.addWindowListener (new WindowAdapter () {

      @Override
      public void windowClosing (WindowEvent e) {
        super.windowClosing (e);
        System.exit (0);
      }

    });
    this.setTitle ("Tank War");
    this.setResizable (false);
    this.setBackground (Color.GREEN);
    this.addKeyListener (new KeyMonitor ());
    setVisible (true);
    new Thread (new PaintThread ()).start ();
  }

  @Override
  public void paint (Graphics g) {

    g.drawString ("Missiles counts: " + missiles.size (), 10, 50);

    myTank.draw (g);
    enemyTank.draw (g);
    for (int i = 0; i < missiles.size (); i++) {
      missiles.get (i).draw (g);
    }

  }

  @Override
  public void update (Graphics g) {
    if (offScreenImage == null) {
      offScreenImage = this.createImage (GAME_WIDTH, GAME_HEIGHT);
    }
    Graphics offG = offScreenImage.getGraphics ();
    Color c = offG.getColor ();
    offG.setColor (Color.GREEN);
    offG.fillRect (0, 0, GAME_WIDTH, GAME_WIDTH);
    offG.setColor (c);
    paint (offG);

    g.drawImage (offScreenImage, 0, 0, null);

  }

  private class PaintThread implements Runnable {

    @Override
    public void run () {

      while (true) {
        //        System.out.println ("PaintThread print-----y: " + y);
        repaint ();
        try {
          Thread.sleep (50);
        } catch (InterruptedException e) {
          e.printStackTrace ();
        }

      }
    }

  }

  private class KeyMonitor extends KeyAdapter {

    @Override
    public void keyPressed (KeyEvent e) {
      myTank.keyPressed (e);

    }

    @Override
    public void keyReleased (KeyEvent e) {
      myTank.keyReleased (e);
    }

  }

}
