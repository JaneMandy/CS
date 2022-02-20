package ui;

import common.CommonUtils;
import common.MudgeSanity;
import graph.Route;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Sorters {
   private static Set B = new HashSet();
   private static Set A = new HashSet();

   public static Comparator getStringSorter() {
      return new Sorters._C();
   }

   public static Comparator getHostSorter() {
      return new Sorters._A();
   }

   public static Comparator getNumberSorter() {
      return new Sorters._B();
   }

   public static Comparator getDateSorter(String var0) {
      return new Sorters._D(var0);
   }

   public static Comparator getProperSorter(String var0) {
      if (B.contains(var0)) {
         return getHostSorter();
      } else {
         return A.contains(var0) ? getNumberSorter() : null;
      }
   }

   static {
      B.add("external");
      B.add("host");
      B.add("Host");
      B.add("internal");
      B.add("session_host");
      B.add("address");
      A.add("when");
      A.add("last");
      A.add("pid");
      A.add("port");
      A.add("Port");
      A.add("sid");
      A.add("when");
      A.add("date");
      A.add("size");
      A.add("PID");
      A.add("PPID");
      A.add("Session");
   }

   private static class _C implements Comparator {
      private _C() {
      }

      public int compare(Object var1, Object var2) {
         if (var1 == null && var2 == null) {
            return this.compare("", "");
         } else if (var1 == null) {
            return this.compare("", var2);
         } else {
            return var2 == null ? this.compare(var1, "") : var1.toString().compareTo(var2.toString());
         }
      }

      // $FF: synthetic method
      _C(Object var1) {
         this();
      }
   }

   private static class _B implements Comparator {
      private _B() {
      }

      public int compare(Object var1, Object var2) {
         String var3 = var1.toString();
         String var4 = var2.toString();
         long var5 = CommonUtils.toLongNumber(var3, 0L);
         long var7 = CommonUtils.toLongNumber(var4, 0L);
         if (var5 == var7) {
            return 0;
         } else {
            return var5 > var7 ? 1 : -1;
         }
      }

      // $FF: synthetic method
      _B(Object var1) {
         this();
      }
   }

   private static class _A implements Comparator {
      private _A() {
      }

      public int compare(Object var1, Object var2) {
         String var3 = var1.toString();
         String var4 = var2.toString();
         if (var3.equals("unknown")) {
            return this.compare("0.0.0.0", var2);
         } else if (var4.equals("unknown")) {
            return this.compare(var1, "0.0.0.0");
         } else {
            long var5 = Route.ipToLong(var3);
            long var7 = Route.ipToLong(var4);
            if (var5 == var7) {
               return 0;
            } else {
               return var5 > var7 ? 1 : -1;
            }
         }
      }

      // $FF: synthetic method
      _A(Object var1) {
         this();
      }
   }

   private static class _D implements Comparator {
      protected SimpleDateFormat A = null;

      public _D(String var1) {
         try {
            this.A = new SimpleDateFormat(var1);
         } catch (Exception var3) {
            MudgeSanity.logException("Parser: " + var1, var3, false);
         }

      }

      public int compare(Object var1, Object var2) {
         String var3 = var1.toString();
         String var4 = var2.toString();

         long var5;
         try {
            var5 = this.A.parse(var3).getTime();
         } catch (Exception var11) {
            var5 = 0L;
         }

         long var7;
         try {
            var7 = this.A.parse(var4).getTime();
         } catch (Exception var10) {
            var7 = 0L;
         }

         if (var5 == var7) {
            return 0;
         } else {
            return var5 > var7 ? 1 : -1;
         }
      }
   }
}
