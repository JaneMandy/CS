package socks;

import common.CommonUtils;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SocksProxy {
   protected int id = 0;
   protected List clients = new LinkedList();
   protected List listeners = new LinkedList();
   protected LinkedList reads = new LinkedList();
   protected int readq = 0;
   protected String error = "";
   public static final int SOCKS_MAX_CLIENTS = 67108864;

   public boolean hasSpace() {
      synchronized(this.reads) {
         return this.readq < 1048576;
      }
   }

   public void read(byte[] var1) {
      SocksProxy._A var2 = new SocksProxy._A();
      var2.A = var1;
      synchronized(this.reads) {
         this.reads.add(var2);
         this.readq += var1.length;
      }
   }

   public byte[] grab(int var1) {
      if (var1 <= 0) {
         return new byte[0];
      } else {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream(var1);
         int var3 = 0;
         synchronized(this.reads) {
            while(var3 < var1) {
               SocksProxy._A var5 = (SocksProxy._A)this.reads.peek();
               if (var5 == null || var3 + var5.A.length >= var1) {
                  break;
               }

               this.reads.removeFirst();
               var2.write(var5.A, 0, var5.A.length);
               var3 += var5.A.length;
               this.readq -= var5.A.length;
            }
         }

         return var2.toByteArray();
      }
   }

   public void fireEvent(ProxyEvent var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ProxyListener var3 = (ProxyListener)var2.next();
         var3.proxyEvent(this, var1);
      }

   }

   public void addClient(BasicClient var1) {
      synchronized(this) {
         Iterator var3 = this.clients.iterator();

         while(var3.hasNext()) {
            BasicClient var4 = (BasicClient)var3.next();
            if (!var4.isAlive()) {
               var3.remove();
            }
         }

         this.clients.add(var1);
      }
   }

   public void cleanup() {
      synchronized(this) {
         Iterator var2 = this.clients.iterator();

         while(var2.hasNext()) {
            BasicClient var3 = (BasicClient)var2.next();
            if (!var3.isAlive()) {
               var2.remove();
            }
         }

      }
   }

   private BasicClient A(int var1, String var2) {
      synchronized(this) {
         Iterator var4 = this.clients.iterator();

         while(var4.hasNext()) {
            BasicClient var5 = (BasicClient)var4.next();
            if (!var5.isAlive()) {
               var4.remove();
            }
         }

         Iterator var9 = this.clients.iterator();

         while(true) {
            if (!var9.hasNext()) {
               break;
            }

            BasicClient var6 = (BasicClient)var9.next();
            if (var6.isAlive() && var6.chid == var1) {
               return var6;
            }
         }
      }

      CommonUtils.print_warn("-- Could not find chid " + var1 + " for " + var2 + " (closing)");
      Thread.currentThread();
      Thread.dumpStack();
      this.fireEvent(ProxyEvent.EVENT_CLOSE(var1));
      return null;
   }

   public void addProxyListener(ProxyListener var1) {
      this.listeners.add(var1);
   }

   public void resume(int var1) {
      BasicClient var2 = this.A(var1, "resume");
      if (var2 != null) {
         var2.start();
      }

   }

   public void write(int var1, byte[] var2) {
      BasicClient var3 = this.A(var1, "write");
      if (var3 != null) {
         var3.getQueue().write(var2);
      }

   }

   public void die(int var1) {
      BasicClient var2 = this.A(var1, "die");
      if (var2 != null) {
         var2.die();
      }

   }

   public int nextId() {
      boolean var1 = false;
      synchronized(this) {
         int var5 = this.id;
         this.id = (this.id + 1) % 67108864;
         return var5;
      }
   }

   private static final class _A {
      public byte[] A;

      private _A() {
      }

      // $FF: synthetic method
      _A(Object var1) {
         this();
      }
   }
}
