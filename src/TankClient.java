import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame {

  int x = 50;
  int y = 50;

  public static void main (String[] args) {

    TankClient tankClient = new TankClient ();
    tankClient.lauchFrame ();

    for (int i = 0; i < 100; i++) {
      System.out.println ("Main print----- " + i);
    }

  }

  public void lauchFrame () {
    this.setLocation (400, 300);
    this.setSize (800, 600);

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
    setVisible (true);
    new Thread (new PaintThread ()).start ();
  }

  @Override
  public void paint (Graphics g) {
    Color color = g.getColor ();
    g.setColor (Color.RED);
    g.fillOval (x, y, 30, 30);
    g.setColor (color);

    y += 5;
  }

  private class PaintThread implements Runnable {

    @Override
    public void run () {

      while (true) {
        System.out.println ("PaintThread print-----y: " + y);
        repaint ();
        try {
          Thread.sleep (100);
        } catch (InterruptedException e) {
          e.printStackTrace ();
        }

      }
    }

  }

}
