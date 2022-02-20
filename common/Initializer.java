package common;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Initializer {
   public static final boolean isOK(Class var0, Class var1, String var2, long var3, boolean var5) {
      long[] var6 = new long[]{var3};
      return isOK(var0, var1, var2, var6, var5);
   }

   public static final boolean isOK(Class var0, Class var1, String var2, long[] var3, boolean var4) {
      return A(var0, var1, var2, var3, var4);
   }

   public static final boolean isFileOK(Class var0, String var1, long var2, boolean var4) {
      long[] var5 = new long[]{var2};
      return isFileOK(var0, var1, var5, var4);
   }

   public static final boolean isFileOK(Class var0, String var1, long[] var2, boolean var3) {
      return A(var0, var1, var2, var3);
   }

   private static final boolean A(Class var0, Class var1, String var2, long[] var3, boolean var4) {
      ZipFile var5 = null;

      boolean var6;
      try {
         var5 = A(var0, var1);
         if (var5 == null) {
            var6 = !var4;
            boolean var7 = var6;
            return var7;
         }

         var6 = A(var5, var2, var3);
      } finally {
         try {
            if (var5 != null) {
               var5.close();
            }
         } catch (Throwable var15) {
         }

      }

      return var6;
   }

   private static final boolean A(Class var0, String var1, long[] var2, boolean var3) {
      ZipFile var4 = null;

      boolean var5;
      try {
         var4 = A(var0);
         if (var4 == null) {
            var5 = !var3;
            boolean var6 = var5;
            return var6;
         }

         var5 = A(var4, var1, var2);
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (Throwable var14) {
         }

      }

      return var5;
   }

   private static final ZipFile A(Class var0, Class var1) {
      try {
         String var2 = var0.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
         String var3 = var1.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
         return var2.equals(var3) ? new ZipFile(var2) : null;
      } catch (Throwable var4) {
         return null;
      }
   }

   private static final ZipFile A(Class var0) {
      try {
         String var1 = var0.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
         return new ZipFile(var1);
      } catch (Throwable var2) {
         return null;
      }
   }

   private static final boolean A(ZipFile var0, String var1, long[] var2) {
      try {
         ZipEntry var3 = var0.getEntry(var1);
         return var3 == null ? false : A(var3.getCrc(), var2);
      } catch (Throwable var4) {
         return true;
      }
   }

   private static final boolean A(long var0, long[] var2) {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var0 == var2[var3]) {
            return true;
         }
      }

      return false;
   }
}
