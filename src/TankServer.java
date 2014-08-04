import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TankServer {

  public static final int TCP_PORT = 8888;
  private static int      UUID     = 100;

  List<Client>            clients  = new ArrayList<Client> ();

  public static void main (String[] args) {

    new TankServer ().start ();

  }

  private void start () {
    ServerSocket ss = null;
    try {
      ss = new ServerSocket (TCP_PORT);

    } catch (IOException e) {
      e.printStackTrace ();
    }

    Socket s = null;
    while (true) {

      try {
        s = ss.accept ();
        System.out.println ("A Client Conected! Addr-" + s.getInetAddress () + " port-" + s.getPort ());

        DataInputStream dis = new DataInputStream (s.getInputStream ());
        String ip = s.getInetAddress ().getHostAddress ();
        int clientUdpPort = dis.readInt ();
        clients.add (new Client (ip, clientUdpPort));

        DataOutputStream dos = new DataOutputStream (s.getOutputStream ());
        dos.writeInt (UUID++);

      } catch (IOException e) {
        e.printStackTrace ();
      } finally {
        if (s != null) {
          try {
            s.close ();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
          }
          s = null;
        }
      }

    }

  }

  private class Client {

    String IP;
    int    udpPort;

    public Client (String ip,
                   int clientUdpPort) {
      this.IP = ip;
      this.udpPort = clientUdpPort;
    }

  }

}
