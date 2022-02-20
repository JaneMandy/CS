package tunnel;

import aggressor.AggressorClient;
import common.Callback;
import common.CommonUtils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TunnelManager implements Callback {
   protected AggressorClient client;
   protected List clients;
   protected Set allowlist;

   public void allow(String var1, int var2) {
      synchronized(this) {
         this.allowlist.add(var1 + ":" + var2);
      }
   }

   public boolean isAllowListed(String var1, int var2) {
      synchronized(this) {
         return this.allowlist.contains(var1 + ":" + var2);
      }
   }

   public TunnelManager(AggressorClient var1) {
      this.client = var1;
      this.clients = new LinkedList();
      this.allowlist = new HashSet();
   }

   public void init() {
      this.client.getData().subscribe("tunnel", this);
   }

   public void result(String var1, Object var2) {
      if (var2 instanceof Accept) {
         Accept var3 = (Accept)var2;
         if (!this.isAllowListed(var3.getHost(), var3.getPort())) {
            CommonUtils.print_error("Received accept command for " + var3.getHost() + ":" + var3.getPort() + " tunnel to " + var3.getBeaconID() + ". Not in allow list. Rejecting.");
            return;
         }

         TunnelClient var4 = new TunnelClient(this.client, var3.getBeaconID(), var3.getSocketID());
         var4.start(var3.getHost(), var3.getPort());
         this.clients.add(var4);
      } else {
         TunnelMessage var7 = (TunnelMessage)var2;
         Iterator var8 = this.clients.iterator();

         while(var8.hasNext()) {
            TunnelClient var5 = (TunnelClient)var8.next();
            if (var5.isTarget(var7)) {
               if (var7 instanceof Write) {
                  byte[] var6 = ((Write)var7).getData();
                  var5.write(var6);
               } else if (var7 instanceof Die) {
                  var5.die();
                  var8.remove();
               }
            }
         }
      }

      Thread.yield();
   }
}
