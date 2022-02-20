package common;

import aggressor.TeamServerProps;
import java.io.PrintStream;
import java.util.zip.Adler32;

public class DevLog {
   private static boolean A = false;

   public static boolean isEnabled() {
      return A;
   }

   public static final void log(DevLog.STORY var0, Class var1, String var2) {
      if (A) {
         log(var0, var1, "", var2);
      }

   }

   public static void log(DevLog.STORY var0, Class var1, String var2, String var3) {
      if (A) {
         A(var0.toString(), var1, var2, var3);
      }

   }

   private static void A(String var0, Class var1, String var2, String var3) {
      String var4 = var1.getSimpleName();
      if (var2 != null && var2.length() > 0) {
         var4 = var4 + "." + var2;
      }

      A(var0, var4, var3);
   }

   private static void A(String var0, String var1, String var2) {
      A(var0 + " [" + var1 + "] " + var2);
   }

   private static void A(String var0) {
      PrintStream var10000 = System.out;
      String var10001 = CommonUtils.formatLogDate(System.currentTimeMillis());
      var10000.println("*** " + var10001 + ": " + var0);
   }

   public static final long checksumByteArray(byte[] var0) {
      Adler32 var1 = new Adler32();
      var1.update(var0, 0, var0.length);
      return var1.getValue();
   }

   static {
      if ("true".equalsIgnoreCase(TeamServerProps.getPropsFile().getString("logging.DevLog", "false"))) {
         A = true;
      }

   }

   public static enum STORY {
      CS0215,
      CS0215_TEST_EXPORT,
      CS0215_TEST_EXPORTLOCAL,
      CS0216,
      CS0216_TEST,
      CS0217,
      CS0218;
   }
}
