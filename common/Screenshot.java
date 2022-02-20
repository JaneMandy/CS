package common;

import aggressor.TeamServerProps;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import sleep.runtime.Scalar;

public class Screenshot implements Serializable, Transcript, Loggable, ToScalar, HasUUID {
   protected String when;
   protected String bid;
   protected byte[] data;
   protected String user;
   protected int desktop;
   protected String computer;
   protected String title;
   protected String id;
   private static final SimpleDateFormat B = new SimpleDateFormat("hhmmss");

   public Screenshot(String var1, byte[] var2, String var3, String var4, int var5, String var6) {
      this.bid = var1;
      this.data = var2;
      this.user = var3;
      this.computer = var4;
      this.desktop = var5;
      this.title = var6;
      this.when = System.currentTimeMillis() + "";
      this.id = CommonUtils.ID();
   }

   public String ID() {
      return this.id;
   }

   public String getWindowTitle() {
      return this.title;
   }

   public String getUser() {
      return this.user;
   }

   public int getDesktopSession() {
      return this.desktop;
   }

   public String toString() {
      return "screenshot from beacon id: " + this.bid;
   }

   public String time() {
      return this.when;
   }

   public Icon getImage() {
      return new ImageIcon(this.data);
   }

   public String getBeaconId() {
      return this.bid;
   }

   public void formatEvent(DataOutputStream var1) throws IOException {
      var1.write(this.data);
   }

   public String getLogFile() {
      return "screen_" + this.id.substring(0, 8) + "_" + this.bid + ".jpg";
   }

   public String getLogFolder() {
      return "screenshots";
   }

   public long getLogLimit() {
      long var1 = TeamServerProps.getPropsFile().getLongNumber("limits.screenshot_diskused_percent", 95L);
      return var1;
   }

   public String getLogEventName() {
      return "Screenshot";
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

   static {
      B.setTimeZone(TimeZone.getTimeZone("UTC"));
   }
}
