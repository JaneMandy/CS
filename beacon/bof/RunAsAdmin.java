package beacon.bof;

import aggressor.AggressorClient;
import beacon.PostExInlineObject;
import common.CommonUtils;
import common.SleevedResource;

public class RunAsAdmin extends PostExInlineObject {
   protected String command;

   public RunAsAdmin(AggressorClient var1, String var2) {
      super(var1);
      this.command = var2;
   }

   public byte[] getArguments(String var1) {
      return CommonUtils.toBytes(this.command + '\u0000', "UTF-16LE");
   }

   public String getFunction() {
      return "RunAsAdmin";
   }

   public byte[] getObjectFile(String var1) {
      return SleevedResource.readResource("resources/uactoken." + var1 + ".o");
   }
}
