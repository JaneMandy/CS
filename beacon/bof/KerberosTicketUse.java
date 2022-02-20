package beacon.bof;

import aggressor.AggressorClient;
import beacon.PostExInlineObject;
import common.SleevedResource;

public class KerberosTicketUse extends PostExInlineObject {
   protected byte[] ticket;

   public KerberosTicketUse(AggressorClient var1, byte[] var2) {
      super(var1);
      this.ticket = var2;
   }

   public byte[] getArguments(String var1) {
      return this.ticket;
   }

   public String getFunction() {
      return "KerberosTicketUse";
   }

   public byte[] getObjectFile(String var1) {
      return SleevedResource.readResource("resources/kerberos." + var1 + ".o");
   }
}
