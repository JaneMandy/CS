package cortana.core;

import common.MudgeSanity;
import java.util.LinkedList;
import java.util.Stack;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class EventQueue implements Runnable {
   protected EventManager manager;
   protected LinkedList queue = new LinkedList();
   protected boolean run = true;

   public EventQueue(EventManager var1) {
      this.manager = var1;
      (new Thread(this, "Aggressor Script Event Queue")).start();
   }

   public void add(String var1, Stack var2) {
      EventQueue._A var3 = new EventQueue._A();
      var3.B = var1;
      var3.A = var2;
      synchronized(this) {
         if (this.manager.hasWildcardListener()) {
            this.queue.add(var3.A());
         }

         this.queue.add(var3);
      }
   }

   protected EventQueue._A grabEvent() {
      synchronized(this) {
         return (EventQueue._A)this.queue.pollFirst();
      }
   }

   public void stop() {
      this.run = false;
   }

   public void run() {
      while(this.run) {
         EventQueue._A var1 = this.grabEvent();

         try {
            if (var1 != null) {
               this.manager.fireEventNoQueue(var1.B, var1.A, (ScriptInstance)null);
            } else {
               Thread.sleep(25L);
            }
         } catch (Exception var3) {
            if (var1 != null) {
               MudgeSanity.logException("event: " + var1.B + "/" + SleepUtils.describe(var1.A), var3, false);
            } else {
               MudgeSanity.logException("event (none)", var3, false);
            }
         }
      }

   }

   private static class _A {
      public String B;
      public Stack A;

      private _A() {
      }

      public EventQueue._A A() {
         EventQueue._A var1 = new EventQueue._A();
         var1.B = "*";
         var1.A = new Stack();
         var1.A.addAll(this.A);
         var1.A.push(SleepUtils.getScalar(this.B));
         return var1;
      }

      _A(Object var1) {
         this();
      }
   }
}
