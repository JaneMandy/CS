package common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Timers implements Runnable {
   private static Timers A = null;
   protected List timers = new LinkedList();

   public static synchronized Timers getTimers() {
      if (A == null) {
         A = new Timers();
      }

      return A;
   }

   public void every(long var1, String var3, Do var4) {
      synchronized(this) {
         this.timers.add(new Timers._A(var4, var3, var1));
      }
   }

   private Timers() {
      (new Thread(this, "global timer")).start();
   }

   public void fire(Timers._A var1) {
      var1.A();
   }

   public void run() {
      LinkedList var1 = null;

      while(true) {
         synchronized(this) {
            var1 = new LinkedList(this.timers);
         }

         long var2 = System.currentTimeMillis();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            Timers._A var5 = (Timers._A)var4.next();
            if (var5.A(var2)) {
               this.fire(var5);
            }
         }

         synchronized(this) {
            Iterator var6 = this.timers.iterator();

            while(true) {
               if (!var6.hasNext()) {
                  break;
               }

               Timers._A var7 = (Timers._A)var6.next();
               if (!var7.B()) {
                  var6.remove();
               }
            }
         }

         CommonUtils.sleep(1000L);
      }
   }

   private static class _A {
      public Do C;
      public long E;
      public long B;
      public boolean A = true;
      public String D;

      public _A(Do var1, String var2, long var3) {
         this.C = var1;
         this.E = var3;
         this.B = 0L;
         this.D = var2;
      }

      public boolean A(long var1) {
         long var3 = var1 - this.B;
         if (var3 < -1000L) {
            CommonUtils.print_warn("Detected clock change (" + (var1 - this.B) + "ms). Adjusting '" + this.D + "' timer!");
            this.B = System.currentTimeMillis();
            return false;
         } else {
            return var3 >= this.E;
         }
      }

      public void A() {
         try {
            this.B = System.currentTimeMillis();
            this.A = this.C.moment(this.D);
         } catch (Exception var2) {
            MudgeSanity.logException("timer to " + this.C.getClass() + "/" + this.D + " every " + this.E + "ms", var2, false);
            this.A = false;
         }

      }

      public boolean B() {
         return this.A;
      }
   }
}
