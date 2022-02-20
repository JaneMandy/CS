package org.apache.batik.ext.awt.image.rendered;

import org.apache.batik.util.DoublyLinkedList;

public class LRUCache {
   private DoublyLinkedList free = null;
   private DoublyLinkedList used = null;
   private int maxSize = 0;

   public LRUCache(int var1) {
      if (var1 <= 0) {
         var1 = 1;
      }

      this.maxSize = var1;
      this.free = new DoublyLinkedList();

      for(this.used = new DoublyLinkedList(); var1 > 0; --var1) {
         this.free.add(new LRUCache.LRUNode());
      }

   }

   public int getUsed() {
      return this.used.getSize();
   }

   public synchronized void setSize(int var1) {
      int var2;
      if (this.maxSize < var1) {
         for(var2 = this.maxSize; var2 < var1; ++var2) {
            this.free.add(new LRUCache.LRUNode());
         }
      } else if (this.maxSize > var1) {
         for(var2 = this.used.getSize(); var2 > var1; --var2) {
            LRUCache.LRUNode var3 = (LRUCache.LRUNode)this.used.getTail();
            this.used.remove(var3);
            var3.setObj((LRUCache.LRUObj)null);
         }
      }

      this.maxSize = var1;
   }

   public synchronized void flush() {
      while(this.used.getSize() > 0) {
         LRUCache.LRUNode var1 = (LRUCache.LRUNode)this.used.pop();
         var1.setObj((LRUCache.LRUObj)null);
         this.free.add(var1);
      }

   }

   public synchronized void remove(LRUCache.LRUObj var1) {
      LRUCache.LRUNode var2 = var1.lruGet();
      if (var2 != null) {
         this.used.remove(var2);
         var2.setObj((LRUCache.LRUObj)null);
         this.free.add(var2);
      }
   }

   public synchronized void touch(LRUCache.LRUObj var1) {
      LRUCache.LRUNode var2 = var1.lruGet();
      if (var2 != null) {
         this.used.touch(var2);
      }
   }

   public synchronized void add(LRUCache.LRUObj var1) {
      LRUCache.LRUNode var2 = var1.lruGet();
      if (var2 != null) {
         this.used.touch(var2);
      } else {
         if (this.free.getSize() > 0) {
            var2 = (LRUCache.LRUNode)this.free.pop();
            var2.setObj(var1);
            this.used.add(var2);
         } else {
            var2 = (LRUCache.LRUNode)this.used.getTail();
            var2.setObj(var1);
            this.used.touch(var2);
         }

      }
   }

   protected synchronized void print() {
      System.out.println("In Use: " + this.used.getSize() + " Free: " + this.free.getSize());
      LRUCache.LRUNode var1 = (LRUCache.LRUNode)this.used.getHead();
      if (var1 != null) {
         do {
            System.out.println(var1.getObj());
            var1 = (LRUCache.LRUNode)var1.getNext();
         } while(var1 != this.used.getHead());

      }
   }

   public class LRUNode extends DoublyLinkedList.Node {
      private LRUCache.LRUObj obj = null;

      public LRUCache.LRUObj getObj() {
         return this.obj;
      }

      protected void setObj(LRUCache.LRUObj var1) {
         if (this.obj != null) {
            this.obj.lruRemove();
         }

         this.obj = var1;
         if (this.obj != null) {
            this.obj.lruSet(this);
         }

      }
   }

   public interface LRUObj {
      void lruSet(LRUCache.LRUNode var1);

      LRUCache.LRUNode lruGet();

      void lruRemove();
   }
}
