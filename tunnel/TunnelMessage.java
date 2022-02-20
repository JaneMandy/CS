package tunnel;

import common.Directive;
import java.io.Serializable;

public class TunnelMessage implements Directive, Serializable {
   protected String bid;
   protected int chid;

   public TunnelMessage(String var1, int var2) {
      this.bid = var1;
      this.chid = var2;
   }

   public String getBeaconID() {
      return this.bid;
   }

   public int getSocketID() {
      return this.chid;
   }

   public String toString(String var1) {
      return "[" + this.getClass().getName() + "] " + var1 + " (chid: " + this.chid + ", bid: " + this.bid + ")";
   }

   public String toString() {
      return this.toString("");
   }
}
