import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetClient {

  private static int UDP_PORT_START = 2230;

  private int        udpPort;

  TankClient         tc             = null;
  DatagramSocket     ds             = null;

  public NetClient (TankClient tc) {
    this.udpPort = UDP_PORT_START++;
    this.tc = tc;
    try {
      ds = new DatagramSocket (udpPort);
    } catch (SocketException e) {
      e.printStackTrace ();
    }
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
      int id = dataInputstream.readInt ();
      tc.myTank.setId (id);

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

    TankNewMsg tnm = new TankNewMsg (tc);
    send (tnm);

    new Thread (new UDPRevThread ()).start ();
  }

  public void send (Msg msg) {
    msg.send (ds, "127.0.0.1", TankServer.UDP_PORT);
  }

  public class UDPRevThread implements Runnable {

    byte[] buf = new byte[1024];

    @Override
    public void run () {

      while (ds != null) {
        DatagramPacket dp = new DatagramPacket (buf, buf.length);
        try {
          ds.receive (dp);
          System.out.println ("client at port" + udpPort + " received a packet from port " + dp.getPort ());
          parse (dp);

        } catch (IOException e) {
          e.printStackTrace ();
        }
      }

    }

    private void parse (DatagramPacket dp) {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream (buf, 0, dp.getLength ());
      DataInputStream dis = new DataInputStream (byteArrayInputStream);

      Msg msg = null;
      int msgType = 0;
      try {
        msgType = dis.readInt ();
      } catch (IOException e) {
        e.printStackTrace ();
      }

      switch (msgType) {
        case Msg.TANK_NEW_MSG:
          msg = new TankNewMsg (tc);
          break;
        case Msg.TANK_MOVE_MSG:
          msg = new TankMoveMsg (tc);
          break;
        default:
          break;
      }

      msg.parse (dis);

    }

  }

}
