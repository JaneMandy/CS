package beacon.bof;

import aggressor.AggressorClient;
import beacon.PostExInlineObject;
import common.Packer;
import common.SleevedResource;

public class PsExecCommand extends PostExInlineObject {
   protected String target;
   protected String sname;
   protected String command;

   public PsExecCommand(AggressorClient var1, String var2, String var3, String var4) {
      super(var1);
      this.target = var2;
      this.sname = var3;
      this.command = var4;
   }

   public byte[] getArguments(String var1) {
      Packer var2 = new Packer();
      var2.addLengthAndEncodedStringASCIIZ(this.client, var1, this.target);
      var2.addLengthAndEncodedStringASCIIZ(this.client, var1, this.sname);
      var2.addLengthAndEncodedStringASCIIZ(this.client, var1, this.command);
      return var2.getBytes();
   }

   public byte[] getObjectFile(String var1) {
      return SleevedResource.readResource("resources/psexec_command." + var1 + ".o");
   }
}
