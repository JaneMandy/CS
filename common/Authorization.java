package common;

import java.io.File;
import java.util.Calendar;

public class Authorization {
   protected int watermark = 0;
   protected String validto = "";
   protected String error = null;
   protected boolean valid = false;

   public Authorization() {
      String var1 = CommonUtils.canonicalize("cobaltstrike.auth");
      if (!(new File(var1)).exists()) {
         try {
            File var2 = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            if (var2.getName().toLowerCase().endsWith(".jar")) {
               var2 = var2.getParentFile();
            }

            var1 = (new File(var2, "cobaltstrike.auth")).getAbsolutePath();
         } catch (Exception var19) {
            MudgeSanity.logException("trouble locating auth file", var19, false);
         }
      }

      byte[] var20 = CommonUtils.readFile(var1);
      if (var20.length == 0) {
         this.error = "Could not read " + var1;
      } else {
         AuthCrypto var3 = new AuthCrypto();
         byte[] var4 = var3.decrypt(var20);
         if (var4.length == 0) {
            this.error = var3.error();
         } else {
            try {
               DataParser var5 = new DataParser(var4);
               var5.big();
               int var6 = var5.readInt();
               this.watermark = var5.readInt();
               byte var7 = var5.readByte();
               if (var7 < 44) {
                  this.error = "Authorization file is not for Cobalt Strike 4.4+";
                  return;
               }

               byte var8 = var5.readByte();
               var5.readBytes(var8);
               byte var10 = var5.readByte();
               var5.readBytes(var10);
               byte var12 = var5.readByte();
               var5.readBytes(var12);
               byte var14 = var5.readByte();
               var5.readBytes(var14);
               byte var16 = var5.readByte();
               byte[] var17 = var5.readBytes(var16);
               if (29999999 == var6) {
                  this.validto = "forever";
                  MudgeSanity.systemDetail("valid to", "perpetual");
               } else {
                  if (!this.A(var6)) {
                     this.error = "Valid to date (" + var6 + ") is invalid";
                     return;
                  }

                  this.validto = "20" + var6;
                  MudgeSanity.systemDetail("valid to", CommonUtils.formatDateAny("MMMMM d, YYYY", this.getExpirationDate()));
               }

               this.valid = true;
               MudgeSanity.systemDetail("id", this.watermark + "");
               SleevedResource.Setup(var17);
            } catch (Exception var18) {
               MudgeSanity.logException("auth file parsing", var18, false);
            }

         }
      }
   }

   private final boolean A(int var1) {
      if (var1 > 999999) {
         return false;
      } else {
         int var2 = 2000 + var1 / 10000;
         int var3 = var1 % 10000 / 100;
         int var4 = var1 % 100;
         byte var5 = 10;
         int var6 = Calendar.getInstance().get(1) + var5;
         if (var2 > var6) {
            return false;
         } else {
            Calendar var7 = Calendar.getInstance();
            var7.clear();
            var7.setLenient(false);

            try {
               var7.set(var2, var3 - 1, var4);
               var7.getTime();
               return true;
            } catch (Throwable var9) {
               return false;
            }
         }
      }
   }

   public boolean isPerpetual() {
      return "forever".equals(this.validto);
   }

   public boolean isValid() {
      return this.valid;
   }

   public String getError() {
      return this.error;
   }

   public String getWatermark() {
      return this.watermark + "";
   }

   public long getExpirationDate() {
      return CommonUtils.parseDate(this.validto, "yyyyMMdd");
   }

   public boolean isExpired() {
      return System.currentTimeMillis() > this.getExpirationDate() + B(1);
   }

   public String whenExpires() {
      long var1 = (this.getExpirationDate() + B(1) - System.currentTimeMillis()) / B(1);
      if (var1 == 1L) {
         return "1 day (" + CommonUtils.formatDateAny("MMMMM d, YYYY", this.getExpirationDate()) + ")";
      } else {
         return var1 <= 0L ? "TODAY (" + CommonUtils.formatDateAny("MMMMM d, YYYY", this.getExpirationDate()) + ")" : var1 + " days (" + CommonUtils.formatDateAny("MMMMM d, YYYY", this.getExpirationDate()) + ")";
      }
   }

   public boolean isAlmostExpired() {
      long var1 = System.currentTimeMillis() + B(30);
      return var1 > this.getExpirationDate();
   }

   private static final long B(int var0) {
      return 86400000L * (long)var0;
   }
}
