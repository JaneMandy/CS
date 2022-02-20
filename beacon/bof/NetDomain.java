package beacon.bof;

import aggressor.AggressorClient;
import beacon.PostExInlineObject;
import common.SleevedResource;

public class NetDomain extends PostExInlineObject {
   public NetDomain(AggressorClient var1) {
      super(var1);
   }

   public byte[] getObjectFile(String var1) {
      return SleevedResource.readResource("resources/net_domain." + var1 + ".o");
   }
}
