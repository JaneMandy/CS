package pe;

import c2profile.Profile;
import common.CommonUtils;
import common.Packer;

public class ThreadFix {
   protected Profile profile;
   public static final String marker = "$$$THREAD.C$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$";

   public ThreadFix(Profile var1) {
      this.profile = var1;
   }

   public byte[] apply(byte[] var1) {
      if (!CommonUtils.isin("$$$THREAD.C$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$", var1)) {
         return var1;
      } else if (this.profile.isEmptyString(".post-ex.thread_hint")) {
         return CommonUtils.patch(var1, "$$$THREAD.C$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$", CommonUtils.bString(new byte[64]));
      } else {
         String[] var2 = this.profile.getString(".post-ex.thread_hint").split(" ");
         String var3 = var2[0];
         String var4 = var2[1];
         int var5 = CommonUtils.toNumber(var2[2], 0);
         Packer var6 = new Packer();
         var6.little();
         var6.addString(var3);
         var6.addString("!");
         var6.addString(var4);
         var6.addString("+");
         var6.addInt(var5);
         var6.padTo('\u0000', 64);
         return CommonUtils.patch(var1, "$$$THREAD.C$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$", CommonUtils.bString(var6.getBytes()));
      }
   }
}
