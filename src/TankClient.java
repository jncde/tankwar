import java.awt.Frame;

public class TankClient extends Frame {

  public static void main (String[] args) {

    TankClient tankClient = new TankClient ();
    tankClient.lauchFrame ();
  }

  public void lauchFrame () {
    this.setLocation (400, 300);
    this.setSize (800, 600);
    setVisible (true);
  }
}
