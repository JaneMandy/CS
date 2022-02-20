package pe;

import common.CommonUtils;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BOFUtils {
   protected static List functions = null;

   public static void setup() {
      if (functions == null) {
         functions = new LinkedList(CommonUtils.toList((Object[])CommonUtils.readResourceAsString("resources/bapi.txt").trim().split("\n")));
      }

   }

   protected BOFUtils() {
   }

   public static int findInternalFunction(String var0) {
      setup();
      var0 = var0.split("@")[0];
      Iterator var1 = functions.iterator();

      for(int var2 = 0; var1.hasNext(); ++var2) {
         String var3 = (String)var1.next();
         String var4 = "__imp__" + var3;
         String var5 = "__imp_" + var3;
         if (var4.equals(var0) || var5.equals(var0)) {
            return var2;
         }
      }

      return -1;
   }

   public static String getModule(String var0) {
      if (var0.startsWith("__imp__")) {
         return getModule(var0.substring(7));
      } else {
         return var0.startsWith("__imp_") ? getModule(var0.substring(6)) : var0.split("\\$")[0];
      }
   }

   public static String getFunction(String var0) {
      return var0.split("\\$")[1].split("@")[0];
   }

   public static boolean isDynamicFunction(String var0) {
      if (!var0.startsWith("__imp__") && !var0.startsWith("__imp_")) {
         return false;
      } else {
         String[] var1 = var0.split("\\$");
         if (var1.length != 2) {
            return false;
         } else {
            return var1[0].length() > 0 && var1[1].length() > 0;
         }
      }
   }

   public static boolean isInternalFunction(String var0) {
      return findInternalFunction(var0) >= 0;
   }
}
