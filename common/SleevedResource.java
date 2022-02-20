package common;

import dns.SleeveSecurity;

public class SleevedResource {
   private static SleevedResource B;
   private SleeveSecurity A = new SleeveSecurity();

   protected static void Setup(byte[] var0) {
      byte[] authFileResource = CommonUtils.readResource("resources/cobaltstrike.auth");
      B = new SleevedResource(authFileResource);
   }

   public static byte[] readResource(String var0) {
      return B.A(var0);
   }

   private SleevedResource(byte[] var1) {
      this.A.registerKey(var1);
   }

   private byte[] A(String var1) {
      String var2 = CommonUtils.strrep(var1, "resources/", "sleeve/");
      byte[] var3 = CommonUtils.readResource(var2);
      if (var3.length > 0) {
         long var7 = System.currentTimeMillis();
         byte[] var6 = this.A.decrypt(var3);
         return var6;
      } else {
         byte[] var4 = CommonUtils.readResource(var1);
         if (var4.length == 0) {
            CommonUtils.print_error("Could not find sleeved resource: " + var1 + " [ERROR]");
         } else {
            CommonUtils.print_stat("Used internal resource: " + var1);
         }

         return var4;
      }
   }
}
