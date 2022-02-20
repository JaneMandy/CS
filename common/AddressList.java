package common;

import graph.Route;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AddressList {
   protected List results = null;
   protected String targets;
   protected boolean hasError;
   protected String description;
   public static final int ENTRY_BARE = 1;
   public static final int ENTRY_RANGE = 2;

   public boolean hasError() {
      return this.hasError;
   }

   public String getError() {
      return this.description;
   }

   public AddressList._A Bare(String var1) {
      AddressList._A var2 = new AddressList._A();
      var2.C = 1;
      var2.B = var1;
      return var2;
   }

   public AddressList._A Range(long var1, long var3) {
      AddressList._A var5 = new AddressList._A();
      var5.C = 2;
      var5.D = var1;
      var5.A = var3;
      return var5;
   }

   public String check(String var1) {
      String[] var2 = var1.split("\\.");
      int var3 = CommonUtils.toNumber(var2[0], -1);
      int var4 = CommonUtils.toNumber(var2[1], -1);
      int var5 = CommonUtils.toNumber(var2[2], -1);
      int var6 = CommonUtils.toNumber(var2[3], -1);
      if (var3 >= 0 && var4 >= 0 && var5 >= 0 && var6 >= 0 && var3 < 256 && var4 < 256 && var5 < 256 && var6 < 256) {
         return var1;
      } else {
         this.hasError = true;
         this.description = var1 + " is not an IPv4 address";
         return var1;
      }
   }

   public LinkedList parse() {
      LinkedList var1 = new LinkedList();
      String[] var2 = this.targets.split(",");

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = CommonUtils.trim(var2[var3]);
         String[] var4;
         String var5;
         long var7;
         long var9;
         if (var2[var3].matches("\\d+\\.\\d+\\.\\d+\\.\\d+/\\d+")) {
            var4 = var2[var3].split("/");
            var5 = this.check(var4[0]);
            int var12 = CommonUtils.toNumber(var4[1], 0);
            if (var12 >= 0 && var12 <= 32) {
               var7 = Route.ipToLong(var5);
               var9 = var7 + CommonUtils.lpow(2L, (long)(32 - var12));
               var1.add(this.Range(var7, var9));
            } else {
               this.hasError = true;
               this.description = var2[var3] + " has invalid CIDR notation " + var12;
            }
         } else {
            long var6;
            long var8;
            if (var2[var3].matches("\\d+\\.\\d+\\.\\d+\\.\\d+-\\d+")) {
               var4 = var2[var3].split("-");
               var5 = this.check(var4[0]);
               var6 = (long)CommonUtils.toNumber(var4[1], 0);
               var8 = Route.ipToLong(var5);
               var6 -= var8 & 255L;
               if (var6 <= 0L) {
                  this.hasError = true;
                  this.description = "Invalid range: " + var6 + " is less than " + (var8 & 255L);
               } else {
                  var1.add(this.Range(var8, var8 + var6));
               }
            } else if (var2[var3].matches("\\d+\\.\\d+\\.\\d+\\.\\d++\\d+")) {
               var4 = var2[var3].split("+");
               var5 = this.check(var4[0]);
               var6 = (long)CommonUtils.toNumber(var4[1], 0);
               var8 = Route.ipToLong(var5);
               var1.add(this.Range(var8, var8 + var6));
            } else if (var2[var3].matches("\\d+\\.\\d+\\.\\d+\\.\\d+-\\d+\\.\\d+\\.\\d+\\.\\d+")) {
               var4 = var2[var3].split("-");
               var5 = this.check(var4[0]);
               String var11 = this.check(var4[1]);
               var7 = Route.ipToLong(var5);
               var9 = Route.ipToLong(var11);
               if (var7 >= var9) {
                  this.hasError = true;
                  this.description = "Invalid range: " + var5 + " is greater than " + var11;
               } else {
                  var1.add(this.Range(var7, var9));
               }
            } else {
               var1.add(this.Bare(var2[var3]));
            }
         }
      }

      return var1;
   }

   public AddressList(String var1) {
      this.targets = var1;
      this.results = this.parse();
      if (this.export().length > 2000) {
         this.hasError = true;
         this.description = "target list is too long";
      }

   }

   public Iterator iterator() {
      return this.results.iterator();
   }

   public static String toIP(long var0) {
      long var2 = (var0 & -16777216L) >> 24;
      long var4 = (var0 & 16711680L) >> 16;
      long var6 = (var0 & 65280L) >> 8;
      long var8 = var0 & 255L;
      return var2 + "." + var4 + "." + var6 + "." + var8;
   }

   public List toList() {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.iterator();

      while(true) {
         while(var2.hasNext()) {
            AddressList._A var3 = (AddressList._A)var2.next();
            if (var3.C == 1) {
               var1.add(var3.B);
            } else if (var3.C == 2) {
               for(long var4 = var3.D; var4 < var3.A; ++var4) {
                  var1.add(toIP(var4));
               }
            }
         }

         return var1;
      }
   }

   public boolean hit(String var1) {
      long var2 = Route.ipToLong(var1);
      Iterator var4 = this.iterator();

      while(var4.hasNext()) {
         AddressList._A var5 = (AddressList._A)var4.next();
         if (var5.C == 1) {
            if (var1.equals(var5.B)) {
               return true;
            }
         } else if (var5.C == 2 && var2 >= var5.D && var2 < var5.A) {
            return true;
         }
      }

      return false;
   }

   public byte[] export() {
      Packer var1 = new Packer();
      var1.little();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         AddressList._A var3 = (AddressList._A)var2.next();
         var1.addInt(var3.C);
         if (var3.C == 1) {
            var1.addInt(var3.B.length());
            var1.addString(var3.B);
         } else if (var3.C == 2) {
            var1.addInt(8);
            var1.addInt((int)var3.D);
            var1.addInt((int)var3.A);
         }
      }

      return var1.getBytes();
   }

   private static class _A {
      public int C;
      public String B;
      public long D;
      public long A;

      private _A() {
      }

      // $FF: synthetic method
      _A(Object var1) {
         this();
      }
   }
}
