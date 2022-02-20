package tunnel;

import java.util.Map;
import server.ManageUser;
import socks.ReversePortForward;
import socks.SocksProxy;

public class TunnelReversePortForward extends ReversePortForward {
   protected String bid;
   protected ManageUser user;

   public void die() {
   }

   public Map toMap() {
      Map var1 = super.toMap();
      var1.put("type", "reverse port forward (local)");
      if (this.isValid()) {
         var1.put("client", this.user.getNick());
      } else {
         var1.put("client", "<html><body><font color=\"#8b0000\"><strong>DISCONNECTED!</strong></font> " + this.user.getNick() + "</body></html>");
      }

      return var1;
   }

   public boolean isValid() {
      return this.user.isConnected();
   }

   public TunnelReversePortForward(ManageUser var1, String var2, int var3, String var4, int var5) {
      super((SocksProxy)null, var3, var4, var5);
      this.user = var1;
      this.bid = var2;
   }

   public void accept(int var1) {
   }

   public BeaconTunnelClient acceptTunnel(int var1) {
      BeaconTunnelClient var2 = new BeaconTunnelClient(this.user, this.bid, var1);
      var2.accept(this.fhost, this.fport);
      return var2;
   }

   public void go(int var1) {
   }
}
