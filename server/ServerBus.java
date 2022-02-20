package server;

import common.CommonUtils;
import common.MudgeSanity;
import common.Reply;
import common.Request;
import java.util.LinkedList;
import java.util.Map;

public class ServerBus implements Runnable {
   protected LinkedList requests = new LinkedList();
   protected Map calls;

   protected ServerBus._A grabRequest() {
      synchronized(this) {
         return (ServerBus._A)this.requests.pollFirst();
      }
   }

   protected void addRequest(ManageUser var1, Request var2) {
      synchronized(this) {
         while(this.requests.size() > 100000) {
            this.requests.removeFirst();
         }

         this.requests.add(new ServerBus._A(var1, var2));
      }
   }

   public ServerBus(Map var1) {
      this.calls = var1;
      (new Thread(this, "server call bus")).start();
   }

   public void run() {
      try {
         while(true) {
            ServerBus._A var1 = this.grabRequest();
            if (var1 != null) {
               Request var2 = var1.B;
               if (this.calls.containsKey(var2.getCall())) {
                  ServerHook var3 = (ServerHook)this.calls.get(var2.getCall());
                  var3.call(var2, var1.A);
               } else if (var1.A != null) {
                  var1.A.write(new Reply("server_error", 0L, var2 + ": unknown call [or bad arguments]"));
               } else {
                  CommonUtils.print_error("server_error " + var1 + ": unknown call " + var2.getCall() + " [or bad arguments]");
               }

               Thread.yield();
            } else {
               Thread.sleep(25L);
            }
         }
      } catch (Exception var4) {
         MudgeSanity.logException("server call bus loop", var4, false);
      }
   }

   private static class _A {
      public ManageUser A;
      public Request B;

      public _A(ManageUser var1, Request var2) {
         this.A = var1;
         this.B = var2;
      }
   }
}
