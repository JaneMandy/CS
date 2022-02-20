package common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RangeList {
   protected List results = null;
   protected String targets;
   protected boolean hasError = false;
   protected String description = "";
   public static final int ENTRY_BARE = 1;
   public static final int ENTRY_RANGE = 2;

   public boolean hasError() {
      return this.hasError;
   }

   public String getError() {
      return this.description;
   }

   public RangeList._A Bare(String var1) {
      RangeList._A var2 = new RangeList._A();
      var2.B = 1;
      var2.C = (long)CommonUtils.toNumber(var1, 0);
      return var2;
   }

   public RangeList._A Range(long var1, long var3) {
      RangeList._A var5 = new RangeList._A();
      var5.B = 2;
      var5.D = var1;
      var5.A = var3;
      return var5;
   }

   public LinkedList parse() {
      LinkedList var1 = new LinkedList();
      String[] var2 = this.targets.split(",");

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = var2[var3].trim();
         String[] var4;
         long var5;
         long var7;
         if (var2[var3].matches("\\d+-\\d+")) {
            var4 = var2[var3].split("-");
            var5 = (long)CommonUtils.toNumber(var4[0], 0);
            var7 = (long)CommonUtils.toNumber(var4[1], 0);
            var1.add(this.Range(var5, var7));
         } else if (var2[var3].matches("\\d++\\d+")) {
            var4 = var2[var3].split("+");
            var5 = (long)CommonUtils.toNumber(var4[0], 0);
            var7 = (long)CommonUtils.toNumber(var4[1], 0);
            var1.add(this.Range(var5, var5 + var7));
         } else {
            var1.add(this.Bare(var2[var3]));
         }
      }

      return var1;
   }

   public RangeList(String var1) {
      this.targets = var1;
      this.results = this.parse();
   }

   public Iterator iterator() {
      return this.results.iterator();
   }

   public List toList() {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.iterator();

      while(true) {
         while(var2.hasNext()) {
            RangeList._A var3 = (RangeList._A)var2.next();
            if (var3.B == 1) {
               var1.add(new Long(var3.C));
            } else if (var3.B == 2) {
               for(long var4 = var3.D; var4 < var3.A; ++var4) {
                  var1.add(new Long(var4));
               }
            }
         }

         return var1;
      }
   }

   public int random() {
      LinkedList var1 = new LinkedList();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         RangeList._A var3 = (RangeList._A)var2.next();
         if (var3.B == 1) {
            var1.add(new Integer((int)var3.C));
         } else if (var3.B == 2) {
            var1.add(new Integer((int)var3.D + CommonUtils.rand((int)var3.A - (int)var3.D)));
         }
      }

      return (Integer)CommonUtils.pick((List)var1);
   }

   public boolean hit(long var1) {
      Iterator var3 = this.iterator();

      while(var3.hasNext()) {
         RangeList._A var4 = (RangeList._A)var3.next();
         if (var4.B == 1) {
            if (var4.C == var1) {
               return true;
            }
         } else if (var4.B == 2 && var1 >= var4.D && var1 < var4.A) {
            return true;
         }
      }

      return false;
   }

   private static class _A {
      public int B;
      public long C;
      public long D;
      public long A;

      private _A() {
      }

      _A(Object var1) {
         this();
      }
   }
}
