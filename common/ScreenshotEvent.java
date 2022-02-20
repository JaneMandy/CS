package common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class ScreenshotEvent implements Serializable, Loggable {
   protected String when;
   protected String bid;
   protected String user;
   protected int desktop;
   protected String computer;
   protected String title;
   protected String id;
   protected String file;

   public ScreenshotEvent(Screenshot var1) {
      this.title = var1.title;
      this.bid = var1.bid;
      this.user = var1.user;
      this.desktop = var1.desktop;
      this.computer = var1.computer;
      this.title = var1.title;
      this.id = var1.id;
      this.file = var1.getLogFile();
      this.when = var1.when;
   }

   public String toString() {
      return "screenshot: " + this.title;
   }

   public String getBeaconId() {
      return this.bid;
   }

   public void formatEvent(DataOutputStream var1) throws IOException {
      StringBuffer var2 = new StringBuffer();
      var2.append(CommonUtils.formatLogDate(Long.parseLong(this.when)));
      var2.append("\t");
      var2.append(this.computer);
      var2.append("\t");
      var2.append(this.desktop);
      var2.append("\t");
      var2.append(this.user);
      var2.append("\t");
      var2.append(this.file);
      var2.append("\t");
      var2.append(this.title);
      var2.append("\n");
      CommonUtils.writeUTF8(var1, var2.toString());
   }

   public String getLogFile() {
      return "screenshots.log";
   }

   public String getLogFolder() {
      return null;
   }

   public long getLogLimit() {
      return 0L;
   }

   public String getLogEventName() {
      return "Screenshot Event";
   }
}
