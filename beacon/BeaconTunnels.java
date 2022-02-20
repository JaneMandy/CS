package beacon;

import common.CommonUtils;
import common.MudgeSanity;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import server.ManageUser;
import socks.BeaconProxyListener;
import socks.ReversePortForward;
import tunnel.BeaconTunnelClient;
import tunnel.TunnelReversePortForward;

public class BeaconTunnels {
   protected List tunnels = new LinkedList();
   protected BeaconSocks socks = null;
   protected BeaconProxyListener messages = new BeaconProxyListener();

   public BeaconTunnels(BeaconSocks var1) {
      this.socks = var1;
   }

   protected void kill(String var1, int var2) {
      try {
         this.socks.task(var1, this.messages.closeMessage(var2));
      } catch (IOException var4) {
         MudgeSanity.logException("kill tunnel", var4, false);
      }

   }

   public void deregister(ManageUser var1) {
      Iterator var2 = this.tunnels.iterator();

      while(var2.hasNext()) {
         BeaconTunnelClient var3 = (BeaconTunnelClient)var2.next();
         if (var3.is(var1)) {
            CommonUtils.print_stat("Closing: " + var3);
            this.kill(var3.getBeaconID(), var3.getSocketID());
            var2.remove();
         }
      }

   }

   public boolean accept(String var1, int var2, int var3, ReversePortForward var4) {
      if (!(var4 instanceof TunnelReversePortForward)) {
         return false;
      } else {
         TunnelReversePortForward var5 = (TunnelReversePortForward)var4;
         if (!var5.isValid()) {
            CommonUtils.print_warn("Refusing rportfwd connection on port " + var2 + " for " + var1 + " as associated client is dead.");
            this.kill(var1, var3);
            return true;
         } else {
            this.tunnels.add(var5.acceptTunnel(var3));
            return true;
         }
      }
   }

   public ReversePortForward createReversePortForward(ManageUser var1, String var2, int var3, String var4, int var5) {
      return new TunnelReversePortForward(var1, var2, var3, var4, var5);
   }

   public boolean die(String var1, int var2) {
      Iterator var3 = this.tunnels.iterator();

      BeaconTunnelClient var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (BeaconTunnelClient)var3.next();
      } while(!var4.is(var1, var2));

      var4.die();
      var3.remove();
      return true;
   }

   public boolean write(String var1, int var2, byte[] var3) {
      Iterator var4 = this.tunnels.iterator();

      BeaconTunnelClient var5;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         var5 = (BeaconTunnelClient)var4.next();
      } while(!var5.is(var1, var2));

      var5.write(var3);
      return true;
   }
}
