import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankNewMsg implements Msg {

  int        msgType = TANK_NEW_MSG;
  TankClient tankClient;

  public TankNewMsg (TankClient tankClient) {
    this.tankClient = tankClient;
  }

  @Override
  public void send (DatagramSocket ds,
                    String IP,
                    int udpport) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream ();
    DataOutputStream dos = new DataOutputStream (baos);
    try {
      dos.writeInt (msgType);
      dos.writeInt (tankClient.myTank.getId ());
      dos.writeInt (tankClient.myTank.getX ());
      dos.writeInt (tankClient.myTank.getY ());
      dos.writeInt (tankClient.myTank.getDir ().ordinal ());
      dos.writeBoolean (tankClient.myTank.isGood ());
    } catch (IOException e) {
      e.printStackTrace ();
    }

    byte[] buf = baos.toByteArray ();
    try {
      DatagramPacket dp = new DatagramPacket (buf, buf.length, new InetSocketAddress (IP, udpport));
      ds.send (dp);
    } catch (SocketException e) {
      e.printStackTrace ();
    } catch (IOException e) {
      e.printStackTrace ();
    }

  }

  @Override
  public void parse (DataInputStream dis) {

    try {
      int id = dis.readInt ();
      if (tankClient.myTank.getId () == id) {
        System.out.println ("tak with id " + tankClient.myTank.getId () + " get id per udp " + id);
        return;
      }
      int x = dis.readInt ();
      int y = dis.readInt ();
      Dir dir = Dir.values ()[dis.readInt ()];
      boolean good = dis.readBoolean ();

      System.out.println ("client received udp data: id" + id + "x: " + x + " y:" + y + " dir:" + dir + " good:" + good);

      Tank tank = new Tank (x, y, good, dir);
      tank.setId (id);
      tankClient.enemies.add (tank);

    } catch (IOException e) {
      e.printStackTrace ();
    }

  }

}
