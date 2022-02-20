package socks;

import common.MudgeSanity;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ReversePortForwardClient extends BasicClient {
   protected String fhost;
   protected int fport;
   protected int lport;

   public ReversePortForwardClient(SocksProxy var1, int var2, int var3, String var4, int var5) {
      this.client = null;
      this.parent = var1;
      this.chid = var2;
      this.fhost = var4;
      this.fport = var5;
      this.lport = var3;
      this.queue = new BasicQueue(this);
   }

   public void start() {
      try {
         this.client = new Socket();
         this.client.connect(new InetSocketAddress(this.fhost, this.fport), 10000);
         this.setup();
         super.start();
      } catch (IOException var2) {
         MudgeSanity.logException("[rportfwd] Could not connect to " + this.fhost + ":" + this.fport, var2, true);
         this.die();
      }

   }

   public void run() {
   }
}
