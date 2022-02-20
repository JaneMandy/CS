package socks;

import common.CommonUtils;
import java.util.LinkedList;

public class BasicQueue implements Runnable {
   protected BasicClient client = null;
   protected LinkedList requests = new LinkedList();

   protected byte[] grabTask() {
      synchronized(this) {
         return (byte[])((byte[])this.requests.pollFirst());
      }
   }

   protected void addTask(byte[] var1) {
      synchronized(this) {
         if (this.requests.size() > 2500) {
            CommonUtils.print_error("queue for " + this.client.toString() + " has 2,500 accumulated tasks. Probably dead. Closing.");
            this.client.die();
         } else {
            this.requests.add(var1);
         }
      }
   }

   public void write(byte[] var1) {
      this.addTask(var1);
   }

   public BasicQueue(BasicClient var1) {
      this.client = var1;
   }

   public void run() {
      while(this.client.isAlive()) {
         byte[] var1 = this.grabTask();
         if (var1 != null) {
            this.client.write(var1);
            Thread.yield();
         } else {
            CommonUtils.sleep(25L);
         }
      }

   }
}
