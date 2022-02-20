package beacon.bof;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.PostExInlineObject;
import beacon.TaskBeacon;
import common.CommonUtils;

public class UserSpecified extends PostExInlineObject {
   protected String file;
   protected String args;
   protected TaskBeacon tasker;

   public UserSpecified(TaskBeacon var1, AggressorClient var2, String var3, String var4) {
      super(var2);
      this.tasker = var1;
      this.file = var3;
      this.args = var4;
   }

   public void error(String var1, String var2) {
      this.tasker.error(var1, var2);
   }

   public byte[] getArguments(String var1) {
      return DataUtils.encodeForBeacon(this.client.getData(), var1, this.args + '\u0000');
   }

   public byte[] getObjectFile(String var1) {
      return CommonUtils.readFile(this.file);
   }
}
