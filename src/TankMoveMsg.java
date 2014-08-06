import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankMoveMsg implements Msg {

  int        msgType = TANK_MOVE_MSG;
  int        id;
  Dir        dir;
  TankClient tankClient;

  public TankMoveMsg (int id,
                      Dir dir) {
    this.id = id;
    this.dir = dir;

  }

  public TankMoveMsg (TankClient tankClient) {
    this.tankClient = tankClient;
  }

  @Override
  public void send (DatagramSocket ds,
                    String IP,
                    int udpPort) {

    ByteArrayOutputStream baos = new ByteArrayOutputStream ();
    DataOutputStream dos = new DataOutputStream (baos);
    try {
      dos.writeInt (msgType);
      dos.writeInt (id);
      dos.writeInt (dir.ordinal ());
    } catch (IOException e) {
      e.printStackTrace ();
    }

    byte[] buf = baos.toByteArray ();
    try {
      DatagramPacket dp = new DatagramPacket (buf, buf.length, new InetSocketAddress (IP, udpPort));
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

      Dir dir = Dir.values ()[dis.readInt ()];

      System.out.println ("client received udp data: id" + id + " dir:" + dir);

      for (int i = 0; i < tankClient.enemies.size (); i++) {
        Tank tank = tankClient.enemies.get (i);
        if (tank.getId () == id) {
          tank.setDir (dir);
        }
      }

    } catch (IOException e) {
      e.printStackTrace ();
    }

  }

}
