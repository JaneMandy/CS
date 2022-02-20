package common;

import aggressor.TeamServerProps;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import sleep.runtime.Scalar;

public class Keystrokes implements Serializable, Transcript, Loggable, ToScalar, HasUUID {
   protected String when;
   protected String bid;
   protected String data;
   protected String user;
   protected int desktop;
   protected String computer;
   protected String title;
   protected String id;

   public Keystrokes(String var1, String var2, String var3, String var4, int var5, String var6) {
      this.bid = var1;
      this.data = var2;
      this.user = var3;
      this.computer = var4;
      this.desktop = var5;
      this.title = var6;
      this.when = System.currentTimeMillis() + "";
      this.id = CommonUtils.toHex(CommonUtils.MD5(CommonUtils.toBytes(var1 + var5 + var3)));
   }

   public Map toMap() {
      HashMap var1 = new HashMap();
      var1.put("id", this.id);
      var1.put("when", this.when);
      var1.put("title", this.title);
      var1.put("user", this.user);
      var1.put("session", this.desktop);
      var1.put("bid", this.bid);
      var1.put("computer", this.computer);
      return var1;
   }

   public Scalar toScalar() {
      Map var1 = this.toMap();
      var1.put("data", this.data);
      return ScriptUtils.convertAll(var1);
   }

   public String ID() {
      return this.id;
   }

   public String toString() {
      return "keystrokes from beacon id: " + this.bid;
   }

   public String time() {
      return this.when;
   }

   public String getKeystrokes() {
      return this.data;
   }

   public String getBeaconId() {
      return this.bid;
   }

   public void formatEvent(DataOutputStream var1) throws IOException {
      var1.writeBytes(CommonUtils.formatLogDate(Long.parseLong(this.when)) + " Received keystrokes from " + this.user + " in desktop " + this.desktop);
      var1.writeBytes("\n\n");
      var1.writeBytes(CommonUtils.strip(this.getKeystrokes()));
      var1.writeBytes("\n");
   }

   public String getLogFile() {
      return "keystrokes_" + this.bid + "." + this.desktop + ".txt";
   }

   public String getLogFolder() {
      return "keystrokes";
   }

   public long getLogLimit() {
      long var1 = TeamServerProps.getPropsFile().getLongNumber("limits.keystrokes_diskused_percent", 95L);
      return var1;
   }

   public String getLogEventName() {
      return "Keystrokes";
   }
}
