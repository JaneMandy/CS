package beacon.bof;

import aggressor.AggressorClient;
import beacon.PostExInlineObject;
import common.SleevedResource;

public class KerberosTicketPurge extends PostExInlineObject {
   public KerberosTicketPurge(AggressorClient var1) {
      super(var1);
   }

   public String getFunction() {
      return "KerberosTicketPurge";
   }

   public byte[] getObjectFile(String var1) {
      return SleevedResource.readResource("resources/kerberos." + var1 + ".o");
   }
}
