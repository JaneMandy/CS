package beacon.bof;

import aggressor.AggressorClient;
import beacon.PostExInlineObject;
import common.Packer;
import common.SleevedResource;

public class Timestomp extends PostExInlineObject {
   protected String dest;
   protected String src;

   public Timestomp(AggressorClient var1, String var2, String var3) {
      super(var1);
      this.dest = var2;
      this.src = var3;
   }

   public byte[] getArguments(String var1) {
      Packer var2 = new Packer();
      var2.addLengthAndEncodedStringASCIIZ(this.client, var1, this.src);
      var2.addLengthAndEncodedStringASCIIZ(this.client, var1, this.dest);
      return var2.getBytes();
   }

   public byte[] getObjectFile(String var1) {
      return SleevedResource.readResource("resources/timestomp." + var1 + ".o");
   }
}
