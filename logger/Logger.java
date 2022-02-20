package logger;

import common.BeaconEntry;
import common.CommonUtils;
import common.Loggable;
import common.MudgeSanity;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import server.Resources;
import server.ServerUtils;

public class Logger extends ProcessBackend {
   protected Resources r;
   private static final SimpleDateFormat A = new SimpleDateFormat("yyMMdd");

   public Logger(Resources var1) {
      this.r = var1;
      this.start("logger");
   }

   protected File base(String var1) {
      Date var2 = new Date(System.currentTimeMillis());
      File var3 = new File("logs");
      var3 = CommonUtils.SafeFile(var3, A.format(var2));
      if (var1 != null) {
         var3 = CommonUtils.SafeFile(var3, var1);
      }

      if (!var3.exists()) {
         var3.mkdirs();
      }

      return var3;
   }

   protected File beacon(String var1, String var2) {
      File var3 = this.base((String)null);
      BeaconEntry var5 = ServerUtils.getBeacon(this.r, var1);
      File var4;
      if (var5 != null && !"".equals(var5.getInternal())) {
         var4 = CommonUtils.SafeFile(var3, var5.getInternal());
      } else {
         var4 = CommonUtils.SafeFile(var3, "unknown");
      }

      if (var2 != null) {
         var4 = CommonUtils.SafeFile(var4, var2);
      }

      if (!var4.exists()) {
         var4.mkdirs();
      }

      return var4;
   }

   public void process(Object var1) {
      Loggable var2 = (Loggable)var1;
      String var3 = var2.getBeaconId();
      File var4 = null;
      File var5 = null;
      if (var3 != null) {
         var5 = this.beacon(var3, var2.getLogFolder());
         var4 = CommonUtils.SafeFile(var5, var2.getLogFile());
      } else {
         var5 = this.base(var2.getLogFolder());
         var4 = CommonUtils.SafeFile(var5, var2.getLogFile());
      }

      try {
         long var6 = var2.getLogLimit();
         if (var6 > 99L) {
            CommonUtils.print_warn("Invalid max percent used log limit - value (" + String.valueOf(var6) + ") exceeds 99%.");
         } else if (var6 > 0L) {
            File var8 = var4.getParentFile();
            long var9 = var8.getTotalSpace();
            if (var9 > 0L) {
               long var11 = var8.getUsableSpace();
               long var13 = (var9 - var11) * 100L / var9;
               if (var13 >= var6) {
                  String var15 = "Loggable event (" + var2.getLogEventName() + ") ignored. Used disk space (" + var13 + "%) must be less than " + var6 + "%.";
                  CommonUtils.print_warn(var15);
                  return;
               }
            }
         }

         FileOutputStream var17 = new FileOutputStream(var4, true);
         DataOutputStream var18 = new DataOutputStream(var17);
         var2.formatEvent(var18);
         var18.flush();
         var18.close();
      } catch (IOException var16) {
         MudgeSanity.logException("Writing to: " + var4, var16, false);
      }

   }

   static {
      A.setTimeZone(TimeZone.getTimeZone("UTC"));
   }
}
