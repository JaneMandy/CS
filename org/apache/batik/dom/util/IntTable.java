package org.apache.batik.dom.util;

import java.io.Serializable;

public class IntTable implements Serializable {
   protected static final int INITIAL_CAPACITY = 11;
   protected IntTable.Entry[] table;
   protected int count;

   public IntTable() {
      this.table = new IntTable.Entry[11];
   }

   public IntTable(int var1) {
      this.table = new IntTable.Entry[var1];
   }

   public IntTable(IntTable var1) {
      this.count = var1.count;
      this.table = new IntTable.Entry[var1.table.length];

      for(int var2 = 0; var2 < this.table.length; ++var2) {
         IntTable.Entry var3 = var1.table[var2];
         IntTable.Entry var4 = null;
         if (var3 != null) {
            var4 = new IntTable.Entry(var3.hash, var3.key, var3.value, (IntTable.Entry)null);
            this.table[var2] = var4;

            for(var3 = var3.next; var3 != null; var3 = var3.next) {
               var4.next = new IntTable.Entry(var3.hash, var3.key, var3.value, (IntTable.Entry)null);
               var4 = var4.next;
            }
         }
      }

   }

   public int size() {
      return this.count;
   }

   protected IntTable.Entry find(Object var1) {
      return null;
   }

   public int get(Object var1) {
      int var2 = var1 == null ? 0 : var1.hashCode() & Integer.MAX_VALUE;
      int var3 = var2 % this.table.length;

      for(IntTable.Entry var4 = this.table[var3]; var4 != null; var4 = var4.next) {
         if (var4.hash == var2 && (var4.key == null && var1 == null || var4.key != null && var4.key.equals(var1))) {
            return var4.value;
         }
      }

      return 0;
   }

   public int put(Object var1, int var2) {
      int var3 = var1 == null ? 0 : var1.hashCode() & Integer.MAX_VALUE;
      int var4 = var3 % this.table.length;

      for(IntTable.Entry var5 = this.table[var4]; var5 != null; var5 = var5.next) {
         if (var5.hash == var3 && (var5.key == null && var1 == null || var5.key != null && var5.key.equals(var1))) {
            int var6 = var5.value;
            var5.value = var2;
            return var6;
         }
      }

      int var7 = this.table.length;
      if (this.count++ >= var7 - (var7 >> 2)) {
         this.rehash();
         var4 = var3 % this.table.length;
      }

      this.table[var4] = new IntTable.Entry(var3, var1, var2, this.table[var4]);
      return 0;
   }

   public int inc(Object var1) {
      int var2 = var1 == null ? 0 : var1.hashCode() & Integer.MAX_VALUE;
      int var3 = var2 % this.table.length;

      for(IntTable.Entry var4 = this.table[var3]; var4 != null; var4 = var4.next) {
         if (var4.hash == var2 && (var4.key == null && var1 == null || var4.key != null && var4.key.equals(var1))) {
            return var4.value++;
         }
      }

      int var5 = this.table.length;
      if (this.count++ >= var5 - (var5 >> 2)) {
         this.rehash();
         var3 = var2 % this.table.length;
      }

      this.table[var3] = new IntTable.Entry(var2, var1, 1, this.table[var3]);
      return 0;
   }

   public int dec(Object var1) {
      int var2 = var1 == null ? 0 : var1.hashCode() & Integer.MAX_VALUE;
      int var3 = var2 % this.table.length;

      for(IntTable.Entry var4 = this.table[var3]; var4 != null; var4 = var4.next) {
         if (var4.hash == var2 && (var4.key == null && var1 == null || var4.key != null && var4.key.equals(var1))) {
            return var4.value--;
         }
      }

      int var5 = this.table.length;
      if (this.count++ >= var5 - (var5 >> 2)) {
         this.rehash();
         var3 = var2 % this.table.length;
      }

      this.table[var3] = new IntTable.Entry(var2, var1, -1, this.table[var3]);
      return 0;
   }

   public int remove(Object var1) {
      int var2 = var1 == null ? 0 : var1.hashCode() & Integer.MAX_VALUE;
      int var3 = var2 % this.table.length;
      IntTable.Entry var4 = null;

      for(IntTable.Entry var5 = this.table[var3]; var5 != null; var5 = var5.next) {
         if (var5.hash == var2 && (var5.key == null && var1 == null || var5.key != null && var5.key.equals(var1))) {
            int var6 = var5.value;
            if (var4 == null) {
               this.table[var3] = var5.next;
            } else {
               var4.next = var5.next;
            }

            --this.count;
            return var6;
         }

         var4 = var5;
      }

      return 0;
   }

   public void clear() {
      for(int var1 = 0; var1 < this.table.length; ++var1) {
         this.table[var1] = null;
      }

      this.count = 0;
   }

   protected void rehash() {
      IntTable.Entry[] var1 = this.table;
      this.table = new IntTable.Entry[var1.length * 2 + 1];

      IntTable.Entry var4;
      int var5;
      for(int var2 = var1.length - 1; var2 >= 0; --var2) {
         for(IntTable.Entry var3 = var1[var2]; var3 != null; this.table[var5] = var4) {
            var4 = var3;
            var3 = var3.next;
            var5 = var4.hash % this.table.length;
            var4.next = this.table[var5];
         }
      }

   }

   protected static class Entry implements Serializable {
      public int hash;
      public Object key;
      public int value;
      public IntTable.Entry next;

      public Entry(int var1, Object var2, int var3, IntTable.Entry var4) {
         this.hash = var1;
         this.key = var2;
         this.value = var3;
         this.next = var4;
      }
   }
}
