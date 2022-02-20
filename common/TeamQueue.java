package common;

import aggressor.Prefs;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TeamQueue {
   protected TeamSocket socket;
   protected Map callbacks = new HashMap();
   protected long reqno = 0L;
   protected TeamQueue._B reader = null;
   protected TeamQueue._A writer = null;
   protected Callback subscriber = null;

   public TeamQueue(TeamSocket var1) {
      this.socket = var1;
      int var2 = CommonUtils.toNumber(Prefs.getPreferences().getString("connection.client.timeout", "30000"), 30000);

      try {
         this.socket.client.setSoTimeout(var2);
      } catch (Exception var4) {
         MudgeSanity.logException("Unable to update the client's connection timeout to " + var2, var4, false);
      }

      this.reader = new TeamQueue._B();
      this.writer = new TeamQueue._A();
      (new Thread(this.writer, "TeamQueue Writer")).start();
      (new Thread(this.reader, "TeamQueue Reader")).start();
   }

   public void call(String var1, Callback var2) {
      this.call(var1, (Object[])null, var2);
   }

   public void call(String var1) {
      this.call(var1, (Object[])null, (Callback)null);
   }

   public void call(String var1, Object[] var2) {
      this.call(var1, var2, (Callback)null);
   }

   public void call(String var1, Object[] var2, Callback var3) {
      if (var3 == null) {
         Request var4 = new Request(var1, var2, 0L);
         this.writer.A(var4);
      } else {
         synchronized(this.callbacks) {
            ++this.reqno;
            this.callbacks.put(new Long(this.reqno), var3);
            Request var5 = new Request(var1, var2, this.reqno);
            this.writer.A(var5);
         }
      }

   }

   public boolean isConnected() {
      return this.socket.isConnected();
   }

   public void close() {
      this.socket.close();
   }

   public void addDisconnectListener(DisconnectListener var1) {
      this.socket.addDisconnectListener(var1);
   }

   public void setSubscriber(Callback var1) {
      synchronized(this) {
         this.subscriber = var1;
      }
   }

   protected void processRead(Reply var1) {
      Callback var2 = null;
      if (var1.hasCallback()) {
         synchronized(this.callbacks) {
            var2 = (Callback)this.callbacks.get(var1.getCallbackReference());
            this.callbacks.remove(var1.getCallbackReference());
         }

         if (var2 != null) {
            var2.result(var1.getCall(), var1.getContent());
         }
      } else {
         synchronized(this) {
            if (this.subscriber != null) {
               this.subscriber.result(var1.getCall(), var1.getContent());
            }
         }
      }

   }

   private class _A implements Runnable {
      protected LinkedList B = new LinkedList();

      protected Request A() {
         synchronized(this) {
            return (Request)this.B.pollFirst();
         }
      }

      protected void A(Request var1) {
         synchronized(this) {
            if (var1.size() > 100000) {
               this.B.removeFirst();
            }

            this.B.add(var1);
         }
      }

      public _A() {
      }

      public void run() {
         while(TeamQueue.this.socket.isConnected()) {
            Request var1 = this.A();
            if (var1 != null) {
               TeamQueue.this.socket.writeObject(var1);
               Thread.yield();
            } else {
               try {
                  Thread.sleep(25L);
               } catch (InterruptedException var3) {
                  MudgeSanity.logException("teamwriter sleep", var3, false);
               }
            }
         }

      }
   }

   private class _B implements Runnable {
      public _B() {
      }

      public void run() {
         while(true) {
            try {
               if (TeamQueue.this.socket.isConnected()) {
                  Reply var1 = (Reply)TeamQueue.this.socket.readObject();
                  if (var1 != null) {
                     TeamQueue.this.processRead(var1);
                     Thread.yield();
                     continue;
                  }

                  TeamQueue.this.close();
               }
            } catch (Exception var2) {
               MudgeSanity.logException("team reader", var2, false);
               TeamQueue.this.close();
            }

            return;
         }
      }
   }
}
