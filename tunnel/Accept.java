package tunnel;

public class Accept extends TunnelMessage {
   protected String host;
   protected int port;

   public Accept(String var1, int var2, String var3, int var4) {
      super(var1, var2);
      this.host = var3;
      this.port = var4;
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public String toString() {
      return this.toString("Accept " + this.host + ":" + this.port);
   }
}
