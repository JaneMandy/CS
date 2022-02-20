package beacon.bof;

import aggressor.AggressorClient;
import beacon.PostExInlineObject;

public class UserSpecifiedFull extends PostExInlineObject {
   protected byte[] bofcontent;
   protected String entrypoint;
   protected byte[] args;

   public UserSpecifiedFull(AggressorClient var1, byte[] var2, String var3, byte[] var4) {
      super(var1);
      this.bofcontent = var2;
      this.entrypoint = var3;
      this.args = var4;
   }

   public void error(String var1, String var2) {
      throw new RuntimeException("can't run BOF: " + var2);
   }

   public String getFunction() {
      return this.entrypoint;
   }

   public byte[] getArguments(String var1) {
      return this.args;
   }

   public byte[] getObjectFile(String var1) {
      return this.bofcontent;
   }
}
