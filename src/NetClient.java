import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetClient {

  private static int UDP_PORT_START = 2223;

  private int        udpPort;

  private int        clientID;

  private TankClient tc             = null;

  public NetClient (TankClient tc) {
    this.udpPort = UDP_PORT_START++;
    this.tc = tc;
  }

  public void connect (String IP,
                       int port) {
    Socket s = null;

    try {
      s = new Socket (IP, port);
      System.out.println ("connect to server....");

      DataOutputStream outToServer = new DataOutputStream (s.getOutputStream ());
      outToServer.writeInt (udpPort);
      DataInputStream dataInputstream = new DataInputStream (s.getInputStream ());
      clientID = dataInputstream.readInt ();
      tc.myTank.setId (clientID);

    } catch (UnknownHostException e) {

      e.printStackTrace ();
    } catch (IOException e) {
      e.printStackTrace ();
    } finally {
      try {
        if (s != null) {
          s.close ();
          s = null;
        }
      } catch (IOException e) {
        e.printStackTrace ();
      }
    }

  }

}
