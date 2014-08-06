import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class TankServer {

  public static final int TCP_PORT = 8888;
  private static int      UUID     = 100;
  static final int        UDP_PORT = 6666;

  List<Client>            clients  = new ArrayList<Client> ();

  public static void main (String[] args) {

    new TankServer ().start ();

  }

  private void start () {
    new Thread (new UDPThread ()).start ();
    ServerSocket ss = null;
    try {
      ss = new ServerSocket (TCP_PORT);

    } catch (IOException e) {
      e.printStackTrace ();
    }

    while (true) {
      Socket s = null;

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

  private class UDPThread implements Runnable {

    byte[]               buf   = new byte[1024];
    List<DatagramPacket> newts = new ArrayList<DatagramPacket> ();

    @Override
    public void run () {

      DatagramSocket ds = null;
      try {
        ds = new DatagramSocket (UDP_PORT);
      } catch (SocketException e) {
        e.printStackTrace ();
      }

      System.out.println ("UDP thread started at port:" + UDP_PORT);

      while (ds != null) {
        DatagramPacket dp = new DatagramPacket (buf, buf.length);
        try {
          ds.receive (dp);
          //          newts.add (dp);
          System.out.println ("a data packet received!");

          for (int i = 0; i < clients.size (); i++) {
            Client c = clients.get (i);
            System.out.println ("upd packet is sent to client with udp port " + c.udpPort);
            dp.setSocketAddress (new InetSocketAddress (c.IP, c.udpPort));
            ds.send (dp);
          }
          //          sendDpToEveryClient (ds, dp);
        } catch (IOException e) {
          e.printStackTrace ();
        }

      }

    }

    private void sendDpToEveryClient (DatagramSocket ds,
                                      DatagramPacket dp) throws IOException {

      for (int i = 0; i < clients.size (); i++) {
        Client client = clients.get (i);
        //        for (Iterator<DatagramPacket> iterator = dp2.iterator (); iterator.hasNext ();) {
        //          DatagramPacket dp = iterator.next ();
        dp.setSocketAddress (new InetSocketAddress (client.IP, client.udpPort));
        System.out.println ("upd packet is sent to client with udp port " + client.udpPort);
        ds.send (dp);
        //        }

      }

    }
  }

}
