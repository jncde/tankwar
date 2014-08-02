import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame {

  public static void main (String[] args) {

    TankClient tankClient = new TankClient ();

    tankClient.lauchFrame ();
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
  }

  @Override
  public void paint (Graphics g) {
    Color color = g.getColor ();
    g.setColor (Color.RED);
    g.fillOval (50, 50, 30, 30);
    g.setColor (color);

  }
}
