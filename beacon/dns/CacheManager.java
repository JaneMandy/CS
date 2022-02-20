package beacon.dns;

import dns.DNSServer;

public class CacheManager {
   protected GeneralCache cache = new GeneralCache();

   private static final String A(String var0, String var1) {
      return var0 + "." + var1;
   }

   public boolean contains(String var1, String var2) {
      return this.cache.contains(A(var1, var2));
   }

   public DNSServer.Response get(String var1, String var2) {
      DNSServer.Response var3 = (DNSServer.Response)this.cache.get(A(var1, var2));
      return var3 == null ? DNSServer.A(0L) : var3;
   }

   public void add(String var1, String var2, DNSServer.Response var3) {
      this.cache.add(A(var1, var2), var3);
   }

   public void purge() {
      this.cache.purge("DNS responses");
   }
}
