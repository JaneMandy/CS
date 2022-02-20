package common;

import java.util.Calendar;

public class Authorization {
   protected int watermark = 0;
   protected String validto = "";
   protected String error = null;
   protected boolean valid = false;

   public Authorization() {
      try {
         byte[] decrypt = new byte[]{1, -55, -61, 127, 0, 0, 0, 0, 100, 1, 0, 27, -27, -66, 82, -58, 37, 92, 51, 85, -114, -118, 28, -74, 103, -53, 6};
         DataParser dataParser = new DataParser(decrypt);
         dataParser.big();
         int int1 = dataParser.readInt();
         this.watermark = dataParser.readInt();
         if (dataParser.readByte() < 41) {
            this.error = "Authorization file is not for CSer 4.1+";
            return;
         }

         int i1 = dataParser.readByte();
         dataParser.readBytes(i1);
         byte[] bytes = dataParser.readBytes(dataParser.readByte());
         if (29999999 == int1) {
            this.validto = "forever";
            MudgeSanity.systemDetail("valid to", "perpetual");
         } else {
            this.validto = "20" + int1;
            MudgeSanity.systemDetail("valid to", CommonUtils.formatDateAny("MMMMM d, YYYY", this.getExpirationDate()));
         }

         this.valid = true;
         MudgeSanity.systemDetail("id", this.watermark.makeConcatWithConstants<invokedynamic>(this.watermark));
         SleevedResource.Setup(bytes);
      } catch (Exception var6) {
         MudgeSanity.logException("auth file parsing", var6, false);
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
      return this.watermark.makeConcatWithConstants<invokedynamic>(this.watermark);
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
