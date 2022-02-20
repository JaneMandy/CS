package beacon.bof;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.PostExInlineObject;
import beacon.Registry;
import common.BeaconEntry;
import common.Packer;
import common.SleevedResource;

public class RegistryQuery extends PostExInlineObject {
   protected Registry reg;

   public RegistryQuery(AggressorClient var1, Registry var2) {
      super(var1);
      this.reg = var2;
   }

   public byte[] getArguments(String var1) {
      BeaconEntry var2 = DataUtils.getBeacon(this.client.getData(), var1);
      Packer var3 = new Packer();
      var3.addShort(this.reg.getFlags(var2));
      var3.addShort(this.reg.getHive());
      var3.addLengthAndEncodedStringASCIIZ(this.client, var1, this.reg.getPath());
      var3.addLengthAndEncodedStringASCIIZ(this.client, var1, this.reg.getValue());
      return var3.getBytes();
   }

   public byte[] getObjectFile(String var1) {
      return SleevedResource.readResource("resources/registry." + var1 + ".o");
   }
}
