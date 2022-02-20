package beacon.bof;

import aggressor.AggressorClient;
import beacon.PostExInlineObject;
import common.SleevedResource;

public class GetSystem extends PostExInlineObject {
   public GetSystem(AggressorClient var1) {
      super(var1);
   }

   public byte[] getObjectFile(String var1) {
      return SleevedResource.readResource("resources/getsystem." + var1 + ".o");
   }
}
