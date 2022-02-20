package cortana.support;

import common.MudgeSanity;
import cortana.Cortana;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Heartbeat implements Runnable {
   protected Cortana engine;
   protected List beats;

   public Heartbeat(Cortana var1) {
      this.engine = var1;
      this.beats = new LinkedList();
      this.beats.add(new Heartbeat._A("heartbeat_1s", 1000L));
      this.beats.add(new Heartbeat._A("heartbeat_5s", 5000L));
      this.beats.add(new Heartbeat._A("heartbeat_10s", 10000L));
      this.beats.add(new Heartbeat._A("heartbeat_15s", 15000L));
      this.beats.add(new Heartbeat._A("heartbeat_30s", 30000L));
      this.beats.add(new Heartbeat._A("heartbeat_1m", 60000L));
      this.beats.add(new Heartbeat._A("heartbeat_5m", 300000L));
      this.beats.add(new Heartbeat._A("heartbeat_10m", 600000L));
      this.beats.add(new Heartbeat._A("heartbeat_15m", 900000L));
      this.beats.add(new Heartbeat._A("heartbeat_20m", 1200000L));
      this.beats.add(new Heartbeat._A("heartbeat_30m", 1800000L));
      this.beats.add(new Heartbeat._A("heartbeat_60m", 3600000L));
   }

   public void start() {
      (new Thread(this, "heartbeat thread")).start();
   }

   public void run() {
      while(this.engine.isActive()) {
         try {
            long var1 = System.currentTimeMillis();
            Iterator var3 = this.beats.iterator();

            while(var3.hasNext()) {
               Heartbeat._A var4 = (Heartbeat._A)var3.next();
               var4.A(var1);
            }

            Thread.sleep(1000L);
         } catch (Exception var5) {
            MudgeSanity.logException("heartbeat error", var5, false);
         }
      }

      this.engine = null;
   }

   private class _A {
      protected long B = 0L;
      protected long D;
      protected String C;

      public _A(String var2, long var3) {
         this.D = var3;
         this.C = var2;
         this.B = System.currentTimeMillis() + var3;
      }

      public void A(long var1) {
         if (this.B <= var1) {
            this.B = System.currentTimeMillis() + this.D;
            Heartbeat.this.engine.getEventManager().fireEvent(this.C, new Stack());
         }

      }
   }
}
