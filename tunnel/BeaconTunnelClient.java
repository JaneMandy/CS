package tunnel;

import common.Reply;
import server.ManageUser;

public class BeaconTunnelClient {
   protected ManageUser client;
   protected String bid;
   protected int chid;

   public BeaconTunnelClient(ManageUser var1, String var2, int var3) {
      this.client = var1;
      this.bid = var2;
      this.chid = var3;
   }

   public String getBeaconID() {
      return this.bid;
   }

   public int getSocketID() {
      return this.chid;
   }

   public boolean is(String var1) {
      return this.bid.equals(var1);
   }

   public boolean is(String var1, int var2) {
      return this.bid.equals(var1) && this.chid == var2;
   }

   public boolean is(ManageUser var1) {
      return this.client == var1;
   }

   public void accept(String var1, int var2) {
      this.push(new Accept(this.bid, this.chid, var1, var2));
   }

   public void write(byte[] var1) {
      this.push(new Write(this.bid, this.chid, var1));
   }

   public void die() {
      this.push(new Die(this.bid, this.chid));
   }

   public void push(TunnelMessage var1) {
      this.client.write(new Reply("tunnel", 0L, var1));
      Thread.yield();
   }

   public String toString() {
      return "Tunnel client for " + this.client.getNick() + ", bid: " + this.bid + ", chid: " + this.chid;
   }
}
