package c2profile;

import beacon.setup.ProcessInject;
import cloudstrike.NanoHTTPD;
import cloudstrike.Response;
import common.Authorization;
import common.CodeSigner;
import common.CommonUtils;
import common.License;
import common.MudgeSanity;
import common.SleevedResource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;
import net.jsign.DigestAlgorithm;
import net.jsign.timestamp.TimestampingMode;
import pe.BeaconRDLL;
import pe.MalleablePE;
import pe.PEParser;

public class Lint {
   public static final int PROGRAM_TRANSFORM = 0;
   public static final int PROGRAM_RECOVER = 1;
   protected Profile prof;
   protected String uri = "";
   protected Map headers = new HashMap();
   protected int errorCount = 0;
   protected int warningCount = 0;

   public Lint(Profile var1) {
      this.prof = var1;
   }

   protected void logError(String var1) {
      ++this.errorCount;
      CommonUtils.print_error(var1);
   }

   protected void logWarning(String var1) {
      ++this.warningCount;
      CommonUtils.print_warn(var1);
   }

   public int getErrorCount() {
      return this.errorCount;
   }

   public int getWarningCount() {
      return this.warningCount;
   }

   public void bounds(String var1, int var2, int var3) {
      int var4 = CommonUtils.toNumber(this.prof.getString(var1), 0);
      if (var4 < var2) {
         this.logError("Option " + var1 + " is " + var4 + "; less than lower bound of " + var2);
      }

      if (var4 > var3) {
         this.logError("Option " + var1 + " is " + var4 + "; greater than upper bound of " + var3);
      }

   }

   public void boundsLen(String var1, int var2) throws Exception {
      String var3 = this.prof.getString(var1);
      if (var3.length() > var2) {
         this.logError("Length of option " + var1 + " is " + var3.length() + "; greater than upper bound of " + var2);
      }

   }

   public void checkPipeValue(String var1, String var2, int var3, boolean var4) {
      if (!var4) {
         if (var2.length() > var3) {
            this.logError("Length of " + var1 + " option \"" + var2 + "\" is " + var2.length() + "; greater than upper bound of " + var3);
         }

         if (CommonUtils.toSet(var2).size() > 1) {
            this.logError("Option " + var1 + " only allows one value. You have " + CommonUtils.toSet(var2).size());
         }

         if ("".equals(var2)) {
            this.logError("Woah! " + var1 + " is empty. That's going to break anything that tries to use this pipe value.");
         }

         if (var2.startsWith("\\\\.\\pipe\\")) {
            this.logError(var1 + " should not start with \\\\.\\pipe\\. You're specifying the stuff after this path.");
         }
      } else {
         Iterator var5 = CommonUtils.toSet(var2).iterator();

         while(var5.hasNext()) {
            this.checkPipeValue(var1, (String)var5.next(), var3, false);
         }
      }

   }

   public void checkPipe(String var1, int var2, boolean var3) {
      this.checkPipeValue(var1, this.prof.getString(var1), var2, var3);
   }

   public byte[] randomData(int var1) {
      Random var2 = new Random();
      byte[] var3 = new byte[var1];
      var2.nextBytes(var3);
      return var3;
   }

   public void verb_compatability() {
      if ("GET".equals(this.prof.getString(".http-get.verb")) && this.prof.posts(".http-get.client.metadata")) {
         this.logError(".http-get.verb is GET, but .http-get.client.metadata needs POST");
      }

      if ("GET".equals(this.prof.getString(".http-post.verb"))) {
         if (this.prof.posts(".http-post.client.id")) {
            this.logError(".http-post.verb is GET, but .http-post.client.id needs POST");
         }

         if (this.prof.posts(".http-post.client.output")) {
            this.logError(".http-post.verb is GET, but .http-post.client.output needs POST");
         }
      }

   }

   public void safetylen(String var1, String var2, Map var3) {
      Iterator var4 = var3.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         String var6 = ((Class)var5.getValue()).makeConcatWithConstants<invokedynamic>(var5.getValue());
         if (var6.length() > 1024) {
            this.logError(var2 + " " + var1 + " '" + var5.getKey() + "' is " + var6.length() + " bytes [should be <1024 bytes]");
         }
      }

   }

   public void safetyuri(String var1, String var2, Map var3) {
      StringBuffer var4 = new StringBuffer();
      var4.append(this.uri + var2 + "?");
      Iterator var5 = var3.entrySet().iterator();

      while(var5.hasNext()) {
         Entry var6 = (Entry)var5.next();
         String var7 = ((Class)var6.getKey()).makeConcatWithConstants<invokedynamic>(var6.getKey());
         String var8 = ((Class)var6.getValue()).makeConcatWithConstants<invokedynamic>(var6.getValue());
         var4.append(var7 + "=" + var8);
      }

      if (var4.toString().length() > 1024) {
         this.logError(var1 + " URI line (uri + parameters) is " + var4.toString().length() + " bytes [should be <1024 bytes]");
      }

   }

   public void testuri(String var1, String var2, int var3) {
      if (var2.length() > var3) {
         this.logError(var1 + " is too long! " + var2.length() + " bytes [should be <=" + var3 + " bytes]");
      }

      if (var2.indexOf("?") >= 0) {
         this.logError(var1 + " '" + var2 + "' should not contain a ?");
      }

      if (!var2.startsWith("/")) {
         this.logError(var1 + " '" + var2 + "' must start with a /");
      }

   }

   public void testuri_stager(String var1) {
      String var2 = this.prof.getString(var1);
      String var3 = this.prof.getQueryString(".http-stager.client");
      if (!"".equals(var2)) {
         this.testuri(var1, var2, 79);
      } else {
         var2 = CommonUtils.MSFURI();
      }

      if (!"".equals(var3)) {
         var2 = var2 + "?" + var3;
         if (var2.length() > 79) {
            this.logError(var1 + " URI line (uri + parameters) is " + var2.toString().length() + " bytes [should be <80 bytes]");
         }
      }

   }

   public void testuri(String var1) {
      int var2 = 0;
      String[] var3 = this.prof.getString(var1 + ".uri").split(" ");

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].length() > var2) {
            this.uri = var3[var4];
            var2 = var3[var4].length();
         }

         this.testuri(var1 + ".uri", var3[var4], 63);
      }

   }

   public void testuriCompare(String var1, String var2) {
      LintURI var3 = new LintURI();
      var3.add_split(var1, this.prof.getString(var1));
      var3.add_split(var2, this.prof.getString(var2));
      var3.add(".http-stager.uri_x86", this.prof.getString(".http-stager.uri_x86"));
      var3.add(".http-stager.uri_x64", this.prof.getString(".http-stager.uri_x64"));
      var3.checks();
      Iterator var4 = var3.getErrors().iterator();

      while(var4.hasNext()) {
         this.logError((String)var4.next());
      }

      Iterator var5 = var3.getWarnings().iterator();

      while(var5.hasNext()) {
         this.logWarning((String)var5.next());
      }

   }

   public boolean test(String var1, String var2, int var3) throws Exception {
      return this.test(var1, var2, var3, false);
   }

   public boolean test(String var1, String var2, int var3, boolean var4) throws Exception {
      Response var5 = new Response("200 OK", (String)null, (InputStream)null);
      byte[] var6 = this.randomData(var3);
      byte[] var7 = Arrays.copyOf(var6, var6.length);
      if (var2.equals(".id")) {
         var6 = "1234".getBytes("UTF-8");
         var7 = Arrays.copyOf(var6, var6.length);
      }

      if (var4) {
         this.prof.apply(var1, var5, var7);
      } else {
         this.prof.apply(var1 + var2, var5, var7);
      }

      byte[] var8;
      if (var5.data != null) {
         var8 = new byte[var5.data.available()];
         var5.data.read(var8, 0, var8.length);
      } else {
         var8 = new byte[0];
      }

      this.safetyuri(var1, var5.uri, var5.params);
      this.safetylen("parameter", var1, var5.params);
      this.safetylen("header", var1, var5.header);
      String var9 = this.prof.recover(var1 + var2, var5.header, var5.params, new String(var8, "ISO8859-1"), var5.uri);
      byte[] var10 = Program.toBytes(var9);
      if (!Arrays.equals(var10, var6)) {
         this.logError(var1 + var2 + " transform+recover FAILED (" + var3 + " byte[s])");
         return false;
      } else {
         Iterator var11 = var5.params.entrySet().iterator();

         Entry var12;
         String var13;
         String var14;
         while(var11.hasNext()) {
            var12 = (Entry)var11.next();
            var13 = ((Class)var12.getKey()).makeConcatWithConstants<invokedynamic>(var12.getKey());
            var14 = ((Class)var12.getValue()).makeConcatWithConstants<invokedynamic>(var12.getValue());
            var12.setValue(URLEncoder.encode(((Class)var12.getValue()).makeConcatWithConstants<invokedynamic>(var12.getValue()), "UTF-8"));
         }

         var11 = var5.header.entrySet().iterator();

         do {
            if (!var11.hasNext()) {
               var9 = this.prof.recover(var1 + var2, var5.header, var5.params, new String(var8, "ISO8859-1"), var5.uri);
               var10 = Program.toBytes(var9);
               if (!Arrays.equals(var10, var6)) {
                  this.logError(var1 + var2 + " transform+mangle+recover FAILED (" + var3 + " byte[s]) - encode your data!");
                  return false;
               }

               CommonUtils.print_good(var1 + var2 + " transform+mangle+recover passed (" + var3 + " byte[s])");
               return true;
            }

            var12 = (Entry)var11.next();
            var13 = ((Class)var12.getKey()).makeConcatWithConstants<invokedynamic>(var12.getKey());
            var14 = ((Class)var12.getValue()).makeConcatWithConstants<invokedynamic>(var12.getValue());
            var12.setValue(var14.replaceAll("\\P{Graph}", ""));
            if (".http-get.server".equals(var1)) {
               this.headers.put(var13.toLowerCase(), var14.toLowerCase());
            }
         } while(!var1.endsWith(".client") || !"cookie".equals(var13.toLowerCase()) || !this.prof.option(".http_allow_cookies"));

         this.logError(var1 + var2 + " uses HTTP cookie header, but http_allow_cookies is set to true.");
         return false;
      }
   }

   public boolean checkProgramSizes(String var1, int var2, int var3) throws IOException {
      byte[] var4;
      if (var3 == 0) {
         var4 = this.prof.apply_binary(var1);
      } else {
         var4 = this.prof.recover_binary(var1);
      }

      if (var4.length < var2) {
         return true;
      } else {
         this.logError("Program " + var1 + " size check failed.\n\tProgram " + var1 + " must have a compiled size less than " + var2 + " bytes. Current size is: " + var4.length);
         return false;
      }
   }

   public boolean checkPost3x() throws IOException {
      int var1 = this.prof.size(".http-post.client.output", 2097152);
      if (var1 < 6291456) {
         return true;
      } else {
         this.logError("POST 3x check failed.\n\tEncoded HTTP POST must be less than 3x size of non-encoded post. Tested: 2097152 bytes; received " + var1 + " bytes");
         return false;
      }
   }

   public void checkHeaders() {
      if ("chunked".equals(this.headers.get("transfer-encoding"))) {
         this.logError("Remove 'Transfer-Encoding: chunked' header. It will interfere with C2.");
      }

   }

   public void checkHttpConfig() {
      Map var1 = this.prof.getHeadersAsMap(".http-config");
      Set var2 = CommonUtils.toSet("Content-Length, Date, Content-Type");
      Iterator var3 = var2.iterator();

      String var4;
      while(var3.hasNext()) {
         var4 = (String)var3.next();
         if (var1.containsKey(var4)) {
            this.logError(".http-config should not set header '" + var4 + "'. Let the web server set the value for this field.");
         }
      }

      var4 = this.prof.getString(".http-config.block_useragents");
      int var9;
      if (!"".equals(var4)) {
         String[] var5 = var4.split(",");
         int var6 = 0;
         String[] var7 = var5;
         int var8 = var5.length;

         for(var9 = 0; var9 < var8; ++var9) {
            String var10 = var7[var9];
            ++var6;
            var10 = var10.trim();
            if (var10.length() == 0) {
               this.logError(".http-config.block_useragents (comma separated list) has a blank value for item #" + var6);
            }
         }
      }

      String var12 = this.prof.getString(".http-config.allow_useragents");
      if (!"".equals(var12)) {
         String[] var13 = var12.split(",");
         int var14 = 0;
         String[] var15 = var13;
         var9 = var13.length;

         for(int var16 = 0; var16 < var9; ++var16) {
            String var11 = var15[var16];
            ++var14;
            var11 = var11.trim();
            if (var11.length() == 0) {
               this.logError(".http-config.allow_useragents (comma separated list) has a blank value for item #" + var14);
            }
         }
      }

      if (!"".equals(var4) && !"".equals(var12)) {
         this.logWarning(".http-config.block_useragents (" + var4 + ") and .http-config.allow_useragents (" + var12 + ") are both specified.");
      }

   }

   public void checkCollissions(String var1) {
      Program var2 = this.prof.getProgram(var1);
      if (var2 != null) {
         List var3 = var2.collissions(this.prof);
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            this.logError(var1 + " collission for " + var5);
         }
      }

   }

   public void checkKeystore() {
      try {
         KeyStore var1 = KeyStore.getInstance("JKS");
         var1.load(this.prof.getSSLKeystore(), this.prof.getSSLPassword().toCharArray());
         KeyManagerFactory var2 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
         var2.init(var1, this.prof.getSSLPassword().toCharArray());
         SSLContext var3 = SSLContext.getInstance("SSL");
         var3.init(var2.getKeyManagers(), new TrustManager[]{new NanoHTTPD.TrustEverything()}, new SecureRandom());
         SSLServerSocketFactory var4 = var3.getServerSocketFactory();
      } catch (Exception var5) {
         this.logError("Could not load SSL keystore: " + var5.getMessage());
      }

   }

   public void checkCodeSigner() {
      int var1 = this.errorCount;
      if ("".equals(this.prof.getString(".code-signer.alias"))) {
         this.logError(".code-signer.alias is empty. This is the keystore alias for your imported code signing cert");
      }

      if ("".equals(this.prof.getString(".code-signer.password"))) {
         this.logError(".code-signer.password is empty. This is the keystore password");
      }

      String var2;
      if (!"".equals(this.prof.getString(".code-signer.digest_algorithm"))) {
         var2 = this.prof.getString(".code-signer.digest_algorithm");

         try {
            DigestAlgorithm.valueOf(var2);
         } catch (Exception var11) {
            this.logError(".code-sign.digest_algorithm '" + var2 + "' is not valid. (Acceptable values: " + CommonUtils.joinObjects(DigestAlgorithm.values(), ", ") + ")");
         }
      }

      if (!"".equals(this.prof.getString(".code-signer.timestamp_mode"))) {
         var2 = this.prof.getString(".code-signer.timestamp_mode");

         try {
            TimestampingMode.valueOf(var2);
         } catch (Exception var10) {
            this.logError(".code-sign.timestamp_mode '" + var2 + "' is not valid. (Acceptable values: " + CommonUtils.joinObjects(TimestampingMode.values(), ", ") + ")");
         }
      }

      var2 = this.prof.getString(".code-signer.keystore");
      String var3 = this.prof.getString(".code-signer.password");
      String var4 = this.prof.getString(".code-signer.alias");

      try {
         KeyStore var5 = KeyStore.getInstance("JKS");
         var5.load(new FileInputStream(var2), var3.toCharArray());
         PrivateKey var6 = (PrivateKey)var5.getKey(var4, var3.toCharArray());
         if (var6 == null) {
            this.logError(".code-signer.keystore does not have a private key with the name: " + var4);
         }
      } catch (Exception var9) {
         this.logError(".code-signer.keystore failed to load keystore: " + var9.getMessage());
      }

      if (var1 == this.errorCount) {
         try {
            byte[] var12 = SleevedResource.readResource("resources/beacon.dll");
            CodeSigner var13 = new CodeSigner(this.prof);
            byte[] var7 = var13.sign(var12);
            if (Arrays.equals(var12, var7)) {
               this.logError("Failed to sign a dll with the .code-signer configuration.");
            } else {
               CommonUtils.print_good("sign executables and DLLs passed");
            }
         } catch (Exception var8) {
            this.logError("Failed to sign a dll with the .code-signer configuration. " + var8.getMessage());
         }
      }

   }

   public void checkMZ(String var1) {
      BeaconRDLL var2 = new BeaconRDLL(this.prof, var1);
      int var3 = var2.getPatch(0).length;
      if (var3 >= 60) {
         this.logError(".stage.magic_mz_" + var1 + " and patch is " + var3 + " bytes. Reduce its size to <60b.");
      }

      byte[] var4 = this.prof.getStringAsBytes(".stage.magic_mz_" + var1);
      if (var4.length < 2) {
         this.logError(".stage.magic_mz_" + var1 + " is <2 bytes. :( What am I supposed to use to identify the beginning of the DLL?");
      }

   }

   public void checkPE() {
      try {
         PEParser var1 = PEParser.load(SleevedResource.readResource("resources/beacon.dll"));
         int var2 = this.prof.getInt(".stage.image_size_x86");
         int var3 = var1.get("SizeOfImage");
         if (var2 > 0 && var2 < var3) {
            this.logError(".stage.image_size_x86 must be larger than " + var3 + " bytes");
         }

         PEParser var4 = PEParser.load(SleevedResource.readResource("resources/beacon.x64.dll"));
         int var5 = this.prof.getInt(".stage.image_size_x64");
         int var6 = var4.get("SizeOfImage");
         if (var5 > 0 && var5 < var6) {
            this.logError(".stage.image_size_x64 must be larger than " + var6 + " bytes");
         }

         MalleablePE var7 = new MalleablePE(this.prof);
         byte[] var8 = var7.process(SleevedResource.readResource("resources/beacon.dll"), "x86");
         if (var8.length > 271000) {
            this.logError(".stage.transform-x86 results in a stage that's too large");
         } else if (var8.length == 0) {
            this.logError(".stage.transform-x86 failed (unknown reason)");
         }

         MalleablePE var9 = new MalleablePE(this.prof);
         byte[] var10 = var9.process(SleevedResource.readResource("resources/beacon.x64.dll"), "x64");
         if (var10.length > 271000) {
            this.logError(".stage.transform-x64 results in a stage that's too large");
         } else if (var10.length == 0) {
            this.logError(".stage.transform-x86 failed (unknown reason)");
         }

         String var11 = this.prof.getString(".stage.rich_header");
         if (var11.length() > 256) {
            this.logError(".stage.rich_header is too big. Reduce to <=256 bytes");
         }

         byte[] var12 = this.prof.getToString(".stage").getBytes();
         if (var12.length > 4096) {
            this.logError(".stage added " + var12.length + " bytes of strings. Reduce to <=4096");
         }

         Set var13 = CommonUtils.toSetLC(CommonUtils.readResourceAsString("resources/dlls.x86.txt").split("\n"));
         String var14 = this.prof.getString(".stage.module_x86").toLowerCase();
         if (!"".equals(var14) && var13.contains(var14)) {
            this.logError(".stage.module_x86 stomps '" + var14 + "' needed by x86 Beacon DLL.");
         }

         Set var15 = CommonUtils.toSetLC(CommonUtils.readResourceAsString("resources/dlls.x64.txt").split("\n"));
         String var16 = this.prof.getString(".stage.module_x64").toLowerCase();
         if (!"".equals(var16) && var15.contains(var16)) {
            this.logError(".stage.module_x64 stomps '" + var16 + "' needed by x64 Beacon DLL.");
         }

         if (!"".equals(var14) && var2 > var3) {
            this.logWarning(".stage.module_x86 AND .stage.image_size_x86 are defined. Risky! Will " + var14 + " hold ~" + var2 * 2 + " bytes?");
         }

         if (!"".equals(var16) && var5 > var6) {
            this.logWarning(".stage.module_x64 AND .stage.image_size_x64 are defined. Risky! Will " + var16 + " hold ~" + var5 * 2 + " bytes?");
         }

         String var17 = this.prof.getString(".stage.allocator");
         boolean var18 = this.prof.option(".stage.userwx");
         if ((!"".equals(var14) || !"".equals(var16)) && !"VirtualAlloc".equals(var17)) {
            this.logError("Set .stage.allocator to VirtualAlloc to module stomp. module_x86/module_x64 ignored otherwise.");
         }

         if (!var18 && "HeapAlloc".equals(var17)) {
            this.logError(".stage.allocator is HeapAlloc AND .stage.userwx=false. HeapAlloc will allocate RWX memory");
         }

         byte[] var19 = this.prof.getStringAsBytes(".stage.magic_pe");
         if (var19.length != 2) {
            this.logError(".stage.magic_pe " + CommonUtils.toHexString(var19) + " must have exactly two characters.");
         }

         this.checkMZ("x86");
         this.checkMZ("x64");
      } catch (Exception var20) {
         MudgeSanity.logException("pe check", var20, false);
         this.logError("Failed to validate PE information.");
      }

   }

   public void sleepMaskCodeCaveCheck(String var1) {
      PEParser var2 = PEParser.load(SleevedResource.readResource(var1));
      int var3 = var2.sectionEnd(".text");
      int var4 = var2.sectionAddress(".rdata") - var3;
      if (var4 < 304) {
         this.logError(".stage.sleep_mask is true; nook space in " + var1 + " is " + var4 + " bytes. Beacon will crash.");
      }

   }

   public void sleepMaskCodeCaveCheck() {
      if (this.prof.option(".stage.sleep_mask")) {
         this.sleepMaskCodeCaveCheck("resources/beacon.dll");
         this.sleepMaskCodeCaveCheck("resources/beacon.x64.dll");
         this.sleepMaskCodeCaveCheck("resources/dnsb.dll");
         this.sleepMaskCodeCaveCheck("resources/dnsb.x64.dll");
         this.sleepMaskCodeCaveCheck("resources/extc2.dll");
         this.sleepMaskCodeCaveCheck("resources/extc2.x64.dll");
         this.sleepMaskCodeCaveCheck("resources/pivot.dll");
         this.sleepMaskCodeCaveCheck("resources/pivot.x64.dll");
      }

   }

   public void checkProcessInject() {
      this.bounds(".process-inject.min_alloc", 0, 268435455);
      ProcessInject var1 = (new ProcessInject(this.prof)).check();
      Iterator var2 = var1.getErrors().iterator();

      while(var2.hasNext()) {
         this.logError((String)var2.next());
      }

      Iterator var3 = var1.getWarnings().iterator();

      while(var3.hasNext()) {
         this.logWarning((String)var3.next());
      }

   }

   public void setupProcessInject(String var1) {
      byte[] var2 = this.prof.getPrependedData(".process-inject.transform-" + var1);
      byte[] var3 = this.prof.getAppendedData(".process-inject.transform-" + var1);
      int var4 = var2.length + var3.length;
      if (var4 > 252) {
         this.logError(".process-inject.transform-" + var1 + " is " + var4 + " bytes. Reduce to <=252 bytes");
      }

   }

   public void checkSpawnTo(String var1, String var2, String var3) {
      String var4 = this.prof.getString(var1);
      if (var4.length() > 63) {
         this.logError(var1 + " is too long. Limit to 63 characters");
      }

      if (var4.indexOf("\\") == -1) {
         this.logError(var1 + " should refer to a full path.");
      }

      if (var4.toLowerCase().indexOf("\\system32\\") > -1) {
         this.logError(var1 + " references system32. This will break x86->x64 and x64->x86 spawns");
      }

      if (var4.indexOf(var2) > -1) {
         this.logError(var1 + " references " + var2 + ". For this architecture, probably not what you want");
      }

      if (var4.indexOf(var3) == -1 && var4.toLowerCase().indexOf(var3) > -1) {
         int var5 = var4.toLowerCase().indexOf(var3);
         String var6 = var4.substring(var5, var5 + var3.length());
         this.logError(var1 + ": lowercase '" + var6 + "'. This allows runtime adjustments to work");
      }

      if (var4.indexOf("rundll32.exe") > -1) {
         CommonUtils.print_opsec("[OPSEC] " + var1 + " is '" + var4 + "'. This is a *really* bad OPSEC choice.");
      }

   }

   public static void main(String[] var0) {
      if (var0.length == 0) {
         CommonUtils.print_error("Please specify a Beacon profile file\n\t./c2lint my.profile");
         System.exit(2);
      }

      License.checkLicenseConsole(new Authorization());
      Profile var1 = Loader.LoadProfile(var0[0]);
      if (var1 == null) {
         CommonUtils.print_error("Unable to load the Beacon profile\n\t" + var0[0]);
         System.exit(2);
      }

      int var2 = 0;
      int var3 = 0;
      String[] var4 = var1.getVariants();

      int var5;
      for(var5 = 0; var5 < var4.length; ++var5) {
         Lint var6 = new Lint(var1.getVariantProfile(var4[var5]));
         var6.checkProfile(var4[var5]);
         var2 += var6.getWarningCount();
         var3 += var6.getErrorCount();
      }

      var5 = 0;
      if (var2 > 0) {
         CommonUtils.print_warn("Detected " + var2 + " warning" + (var2 == 1 ? "." : "s."));
         ++var5;
      }

      if (var3 > 0) {
         CommonUtils.print_error("Detected " + var3 + " error" + (var3 == 1 ? "." : "s."));
         var5 += 2;
      }

      System.exit(var5);
   }

   public void checkProfile(String var1) {
      Lint var2 = this;
      Profile var3 = this.prof;
      if (var3 == null) {
         this.logError("Profile is set to null for: " + var1);
      } else {
         try {
            StringBuffer var4 = new StringBuffer();
            var4.append("\n\u001b[01;30m");
            var4.append("===============\n");
            var4.append(var1);
            var4.append("\n===============");
            var4.append("\u001b[0m\n\n");
            var4.append("http-get");
            var4.append("\n\u001b[01;30m");
            var4.append("--------");
            var4.append("\n\u001b[01;31m");
            var4.append(var3.getPreview().getClientSample(".http-get"));
            var4.append("\u001b[01;34m");
            var4.append(var3.getPreview().getServerSample(".http-get"));
            var4.append("\u001b[0m\n\n");
            var4.append("http-post");
            var4.append("\n\u001b[01;30m");
            var4.append("---------");
            var4.append("\n\u001b[01;31m");
            var4.append(var3.getPreview().getClientSample(".http-post"));
            var4.append("\u001b[01;34m");
            var4.append(var3.getPreview().getServerSample(".http-post"));
            var4.append("\u001b[0m\n\n");
            if (var3.getProgram(".http-stager") != null) {
               var4.append("http-stager");
               var4.append("\n\u001b[01;30m");
               var4.append("-----------");
               var4.append("\n\u001b[01;31m");
               var4.append(var3.getPreview().getClientSample(".http-stager"));
               var4.append("\u001b[01;34m");
               var4.append(var3.getPreview().getServerSample(".http-stager"));
               var4.append("\u001b[0m\n\n");
            }

            String var5 = ".dns-beacon.dns_stager_subhost";
            String var6 = ".dns-beacon.dns_stager_prepend";
            String var7;
            if (!"".equals(var3.getString(var5))) {
               var7 = var3.getString(var5);
               var4.append("\ndns staging host");
               var4.append("\n\u001b[01;30m");
               var4.append("----------------");
               var4.append("\n\u001b[01;31m");
               var4.append("aaa" + var7 + "<domain>");
               if (var3.hasString(var6)) {
                  var4.append(" = ");
                  var4.append(var3.getString(var6));
                  var4.append("[...]");
               }

               var4.append("\n");
               var4.append("bdc" + var7 + "<domain>");
               var4.append("\u001b[0m\n");
            }

            System.out.println(var4.toString());
            this.A(Lint._A.C, var2, var3);
            if (var2.checkPost3x()) {
               CommonUtils.print_good("POST 3x check passed");
            }

            if (var2.checkProgramSizes(".http-get.server.output", 252, 1)) {
               CommonUtils.print_good(".http-get.server.output size is good");
            }

            if (var2.checkProgramSizes(".http-get.client", 508, 0)) {
               CommonUtils.print_good(".http-get.client size is good");
            }

            if (var2.checkProgramSizes(".http-post.client", 508, 0)) {
               CommonUtils.print_good(".http-post.client size is good");
            }

            var2.testuri(".http-get");
            var2.test(".http-get.client", ".metadata", 1, true);
            var2.test(".http-get.client", ".metadata", 100, true);
            var2.test(".http-get.client", ".metadata", 128, true);
            var2.test(".http-get.client", ".metadata", 256, true);
            var2.test(".http-get.server", ".output", 0, true);
            var2.test(".http-get.server", ".output", 1, true);
            var2.test(".http-get.server", ".output", 48248, true);
            var2.test(".http-get.server", ".output", 1048576, true);
            var2.testuri(".http-post");
            var2.test(".http-post.client", ".id", 4);
            var2.test(".http-post.client", ".output", 0);
            var2.test(".http-post.client", ".output", 1);
            if (var3.shouldChunkPosts()) {
               CommonUtils.print_good(".http-post.client.output chunks results");
               var2.test(".http-post.client", ".output", 33);
               var2.test(".http-post.client", ".output", 128);
            } else {
               CommonUtils.print_good(".http-post.client.output POSTs results");
               var2.test(".http-post.client", ".output", 48248);
               var2.test(".http-post.client", ".output", 1048576);
            }

            if (Profile.usesCookieBeacon(var3)) {
               CommonUtils.print_good("Beacon profile specifies an HTTP Cookie header. Will tell WinINet to allow this.");
            }

            if (var3.usesCookie(".http-stager.client")) {
               CommonUtils.print_good("Stager profile specifies an HTTP Cookie header. Will tell WinINet to allow this.");
            }

            if (Profile.usesHostBeacon(var3)) {
               this.logWarning("Profile uses HTTP Host header for C&C. Will ignore Host header specified in payload config.");
            }

            var2.verb_compatability();
            var2.testuri_stager(".http-stager.uri_x86");
            var2.testuri_stager(".http-stager.uri_x64");
            var7 = var3.getHeaders(".http-stager.client", "");
            if (var7.length() > 303) {
               this.logError(".http-stager.client headers are " + var7.length() + " bytes. Max length is 303 bytes");
            }

            int var8 = (int)var3.getHTTPContentOffset(".http-stager.server");
            if (var8 > 0) {
               if ("".equals(var3.getString(".http-stager.uri_x86"))) {
                  this.logError(".http-stager.uri_x86 is not defined.");
               }

               if ("".equals(var3.getString(".http-stager.uri_x64"))) {
                  this.logError(".http-stager.uri_x64 is not defined.");
               }
            }

            if (var8 > 65535) {
               this.logError(".http-stager.server.output prepend value is " + var8 + " bytes. Max is 65535. HTTP/S Stagers will crash");
            }

            var2.bounds(".sleeptime", 1, Integer.MAX_VALUE);
            var2.bounds(".jitter", 0, 99);
            int var9 = var3.getInt(".data_jitter");
            if (var9 != 0) {
               var2.bounds(".data_jitter", 10, Integer.MAX_VALUE);
            }

            short var10 = 10000;
            if (var9 > var10) {
               this.logWarning(".data_jitter value (" + var9 + ") exceeds recommended limit (" + var10 + "). Excessive jitter could impact HTTP beacon latency and general team server performance.");
            }

            var2.testuriCompare(".http-get.uri", ".http-post.uri");
            var2.boundsLen(".spawnto", 63);
            var2.boundsLen(".useragent", 255);
            var2.checkPipe(".pipename", 64, false);
            var2.checkPipe(".pipename_stager", 64, false);
            var2.checkPipe(".ssh_pipename", 64, true);
            var2.checkPipe(".post-ex.pipename", 48, true);
            var2.boundsLen(".ssh_banner", 120);
            var2.boundsLen(".smb_frame_header", 124);
            var2.boundsLen(".tcp_frame_header", 124);
            if (var3.getString(".pipename").equals(var3.getString(".pipename_stager"))) {
               this.logError(".pipename and .pipename_stager are the same. Make these different strings.");
            }

            var2.checkHeaders();
            var2.checkHttpConfig();
            var3.getHeadersToRemove();
            var2.checkCollissions(".http-get.client");
            var2.checkCollissions(".http-get.server");
            var2.checkCollissions(".http-post.client");
            var2.checkCollissions(".http-post.server");
            var2.checkCollissions(".http-stager.client");
            var2.checkCollissions(".http-stager.server");
            if (!var3.option(".host_stage")) {
               this.logWarning(".host_stage is FALSE. This will break staging over HTTP, HTTPS, and DNS!");
            } else {
               CommonUtils.print_opsec("[OPSEC] .host_stage is true. Your Beacon payload is available to anyone that connects to your server to request it. Are you OK with this? ");
            }

            if (!"rundll32.exe".equals(var3.getString(".spawnto"))) {
               this.logError(".spawnto is deprecated and has no effect. Set .post-ex.spawnto_x86 and .post-ex.spawnto_x64 instead.");
            }

            if (!"%windir%\\syswow64\\rundll32.exe".equals(var3.getString(".spawnto_x86"))) {
               this.logError(".spawnto_x86 is deprecated and has no effect. Set .post-ex.spawnto_x86 instead.");
            }

            if (!"%windir%\\sysnative\\rundll32.exe".equals(var3.getString(".spawnto_x64"))) {
               this.logError(".spawnto_x64 is deprecated and has no effect. Set .post-ex.spawnto_x64 instead.");
            }

            if (var3.option(".amsi_disable")) {
               this.logError(".amsi_disable is deprecated and has no effect. Set .post-ex.amsi_disable instead.");
            }

            var2.checkSpawnTo(".post-ex.spawnto_x86", "sysnative", "syswow64");
            var2.checkSpawnTo(".post-ex.spawnto_x64", "syswow64", "sysnative");
            if (var3.isFile(".code-signer.keystore")) {
               CommonUtils.print_good("Found code-signing configuration. Will sign executables and DLLs");
               var2.checkCodeSigner();
            } else {
               this.logWarning(".code-signer.keystore is missing. Will not sign executables and DLLs");
            }

            var2.boundsLen(".post-ex.thread_hint", 58);
            if (!var3.isFile(".https-certificate.keystore")) {
               if (var3.regenerateKeystore()) {
                  if (var3.getSSLKeystore() != null) {
                     CommonUtils.print_good("SSL certificate generation OK");
                     var2.checkKeystore();
                  } else {
                     this.logError("SSL certificate generation failed");
                  }
               } else {
                  CommonUtils.print_opsec("[OPSEC] .https-certificate options are missing [will use built-in SSL cert]");
               }
            } else {
               CommonUtils.print_good("Found SSL certificate keystore");
               if (var3.getSSLPassword() != null && var3.getSSLPassword().length() != 0) {
                  if ("123456".equals(var3.getSSLPassword())) {
                     this.logWarning(".https-certificate.password is the default '123456'. Is this really your keystore password?");
                  }
               } else {
                  this.logError(".https-certificate.password is empty. A password is required for your keystore.");
               }

               var2.checkKeystore();
            }

            var2.checkPE();
            var2.sleepMaskCodeCaveCheck();
            if (!"".equals(var3.getString(".maxdns"))) {
               this.logError(".maxdns is deprecated and has no effect. Set .dns-beacon.maxdns instead.");
            }

            if (!"".equals(var3.getString(".dns_max_txt"))) {
               this.logError(".dns_max_txt is deprecated and has no effect. Set .dns-beacon.dns_max_txt instead.");
            }

            if (!"".equals(var3.getString(".dns_ttl"))) {
               this.logError(".dns_ttl is deprecated and has no effect. Set .dns-beacon.dns_ttl instead.");
            }

            if (!"".equals(var3.getString(".dns_sleep"))) {
               this.logError(".dns_sleep is deprecated and has no effect. Set .dns-beacon.dns_sleep instead.");
            }

            if (!"".equals(var3.getString(".dns_idle"))) {
               this.logError(".dns_idle is deprecated and has no effect. Set .dns-beacon.dns_idle instead.");
            }

            if (!"".equals(var3.getString(".dns_stager_subhost"))) {
               this.logError(".dns_stager_subhost is deprecated and has no effect. Set .dns-beacon.dns_stager_subhost instead.");
            }

            if (!"".equals(var3.getString(".dns_stager_prepend"))) {
               this.logError(".dns_stager_prepend is deprecated and has no effect. Set .dns-beacon.dns_stager_prepend instead.");
            }

            this.A(Lint._A.B, var2, var3);
            if (!var3.option(".create_remote_thread")) {
               this.logWarning(".create_remote_thread is deprecated and has no effect.");
            }

            if (!var3.option(".hijack_remote_thread")) {
               this.logWarning(".hijack_remote_thread is deprecated and has no effect.");
            }

            var2.setupProcessInject("x86");
            var2.setupProcessInject("x64");
            var2.checkProcessInject();
         } catch (Exception var11) {
            var11.printStackTrace();
            this.logError("Failed to validate profile: " + var1);
         }
      }

   }

   private void A(Lint._A var1, Lint var2, Profile var3) throws Exception {
      String var4 = ".dns-beacon";
      var2.bounds(var4 + ".maxdns", 1, 255);
      var2.bounds(var4 + ".dns_max_txt", 4, 252);
      var2.bounds(var4 + ".dns_ttl", 1, Integer.MAX_VALUE);
      if (var3.getInt(var4 + ".dns_ttl") >= 30) {
         this.logWarning(var4 + ".dns_ttl is " + var3.getInt(var4 + ".dns_ttl") + " seconds. Each of your Beacon checkins will cache/delay for this time.");
      }

      int var5 = Integer.parseInt(var3.getString(var4 + ".dns_max_txt"));
      if (var5 % 4 != 0) {
         this.logError(var4 + ".dns_max_txt value (" + var5 + ") must be divisible by four.");
      }

      String var6 = var4 + ".dns_stager_subhost";
      String var7;
      if (!"".equals(var3.getString(var6))) {
         var7 = var3.getString(var6);
         if (!var7.endsWith(".")) {
            this.logError(var6 + " must end with a '.' (it's prepended to a parent domain)");
         }

         if (var7.length() > 32) {
            this.logError(var6 + " is too long. Keep it under 32 characters.");
         }

         if (var7.indexOf("..") > -1) {
            this.logError(var6 + " contains '..'. This is not valid in a hostname");
         }
      }

      var7 = var4 + ".ns_response";
      String var8 = var3.getString(var7);
      if (!"".equals(var8) && !"drop".equals(var8) && !"idle".equals(var8) && !"zero".equals(var8)) {
         this.logError(var7 + " must be blank, 'drop', 'idle', or 'zero'.");
      }

      String var9 = var4 + ".beacon";
      String var10 = var3.getString(var9);
      String var11 = var4 + ".get_A";
      String var12 = var3.getString(var11);
      String var13 = var4 + ".get_AAAA";
      String var14 = var3.getString(var13);
      String var15 = var4 + ".get_TXT";
      String var16 = var3.getString(var15);
      String var17 = var4 + ".put_metadata";
      String var18 = var3.getString(var17);
      String var19 = var4 + ".put_output";
      String var20 = var3.getString(var19);
      switch(var1) {
      case B:
         if (!"".equals(var10)) {
            this.A(".", var9, var10);
            this.B(".", var9, var10);
            this.E(var9, var10);
            this.B(var9, var10);
            this.C(var9, var10);
            this.D(var9, var10);
            var2.boundsLen(var9, 32);
            this.A(var9, var10, 32);
            this.A(var9, var10, 0, 8);
         }

         this.A(var2, var3, var11, var12, var9, var10);
         this.A(var2, var3, var13, var14, var9, var10);
         this.A(var2, var3, var15, var16, var9, var10);
         this.A(var2, var3, var17, var18);
         this.A(var2, var3, var19, var20);
         String var21 = A(var12, "cdn.");
         String var22 = A(var14, "www6.");
         String var23 = A(var16, "api.");
         String var24 = A(var18, "www.");
         String var25 = A(var20, "post.");
         this.A(var11, var21, var13, var22);
         this.A(var11, var21, var15, var23);
         this.A(var11, var21, var17, var24);
         this.A(var11, var21, var19, var25);
         this.A(var13, var22, var15, var23);
         this.A(var13, var22, var17, var24);
         this.A(var13, var22, var19, var25);
         this.A(var15, var23, var17, var24);
         this.A(var15, var23, var19, var25);
         this.A(var17, var24, var19, var25);
         break;
      case C:
         StringBuffer var26 = new StringBuffer();
         String var27 = "freepics.losenolove.com.";
         String var28 = "aaaaaaaa.";
         String var29 = "bbbbbbbbb.";
         String var30 = "(data).(data).(data).(data).";
         if (!"".equals(var10)) {
            var26.append(F(var9, var10));
            var26.append("\n" + A(var9) + var28 + var27);
            var26.append("\n" + var10 + var28 + var27);
            var26.append(A());
            System.out.println(var26.toString());
         }

         if (!"".equals(var12)) {
            var26.delete(0, var26.length());
            var26.append(F(var11, var12));
            var26.append("\n" + A(var11) + var29 + var28 + var27);
            var26.append("\n" + var12 + var29 + var28 + var27);
            var26.append(A());
            System.out.println(var26.toString());
         }

         if (!"".equals(var14)) {
            var26.delete(0, var26.length());
            var26.append(F(var13, var14));
            var26.append("\n" + A(var13) + var29 + var28 + var27);
            var26.append("\n" + var14 + var29 + var28 + var27);
            var26.append(A());
            System.out.println(var26.toString());
         }

         if (!"".equals(var16)) {
            var26.delete(0, var26.length());
            var26.append(F(var15, var16));
            var26.append("\n" + A(var15) + var29 + var28 + var27);
            var26.append("\n" + var16 + var29 + var28 + var27);
            var26.append(A());
            System.out.println(var26.toString());
         }

         if (!"".equals(var18)) {
            var26.delete(0, var26.length());
            var26.append(F(var17, var18));
            var26.append("\n" + A(var17) + var30 + var29 + var28 + var27);
            var26.append("\n" + var18 + var30 + var29 + var28 + var27);
            var26.append(A());
            System.out.println(var26.toString());
         }

         if (!"".equals(var20)) {
            var26.delete(0, var26.length());
            var26.append(F(var19, var20));
            var26.append("\n" + A(var19) + var30 + var29 + var28 + var27);
            var26.append("\n" + var20 + var30 + var29 + var28 + var27);
            var26.append(A());
            System.out.println(var26.toString());
         }
      }

   }

   private static String A(String var0, String var1) {
      return "".equals(var0) ? var1 : var0;
   }

   private void A(Lint var1, Profile var2, String var3, String var4, String var5, String var6) throws Exception {
      if (!"".equals(var4)) {
         this.A(".", var3, var4);
         this.B(".", var3, var4);
         this.E(var3, var4);
         this.B(var3, var4);
         this.C(var3, var4);
         this.D(var3, var4);
         var1.boundsLen(var3, 32);
         this.A(var3, var4, 32);
         this.A(var3, var4, 2, 8);
      }

   }

   private static String A(String var0) {
      return "[" + var0 + "]";
   }

   private static String F(String var0, String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("\u001b[0m");
      var2.append(var0 + " = '" + var1 + "'");
      var2.append("\u001b[01;30m");
      var2.append("\n--------------------------------------------------------");
      var2.append("\u001b[01;31m");
      return var2.toString();
   }

   private static String A() {
      return "\u001b[0m\n";
   }

   private void A(Lint var1, Profile var2, String var3, String var4) throws Exception {
      if (!"".equals(var4)) {
         this.A(".", var3, var4);
         this.B(".", var3, var4);
         this.E(var3, var4);
         this.B(var3, var4);
         this.C(var3, var4);
         this.D(var3, var4);
         var1.boundsLen(var3, 32);
         this.A(var3, var4, 6);
         this.A(var3, var4, 2, 8);
      }

   }

   private void A(String var1, String var2, String var3) {
      if (var3.equalsIgnoreCase(var1)) {
         this.logError(var2 + " cannot be '" + var1 + "'");
      }

   }

   private void B(String var1, String var2, String var3) {
      if (var3.startsWith(var1)) {
         this.logError(var2 + " cannot start with '" + var1 + "'");
      }

   }

   private void E(String var1, String var2) {
      if (var2.indexOf("..") > -1) {
         this.logError(var1 + " must not have adjacent periods ('..')");
      }

   }

   private void B(String var1, String var2) {
      if (var2.matches(".*\\s.*")) {
         this.logError(var1 + " value contains one or more spaces or other whitespace characters");
      }

   }

   private void C(String var1, String var2) {
      Pattern var3 = Pattern.compile("^[a-zA-Z0-9._-]*$");
      if (!var3.matcher(var2).find()) {
         this.logError(var1 + " contains invalid characters. Valid characters are alphabetic (a-z and A-Z), numeric(0-9), dash (-), period (.), and underscore (_).");
      }

   }

   private void A(String var1, String var2, int var3) {
      if (!var2.endsWith(".")) {
         String[] var4 = var2.split("\\.");
         if (var4.length > 0 && var4[var4.length - 1].length() > var3) {
            this.logError(var1 + " last segment value (" + var4[var4.length - 1] + ") is longer than " + var3 + " characters (appending with other data may exceed hostname syntax rules)");
         }
      }

   }

   private void D(String var1, String var2) {
      byte var3 = 63;
      int var4 = 0;
      String[] var5 = var2.split("\\.");
      String[] var6 = var5;
      int var7 = var5.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String var9 = var6[var8];
         ++var4;
         if (var9.length() > var3) {
            this.logError(var1 + " segment #" + var4 + " is longer than " + var3 + " characters");
         }
      }

   }

   private void A(String var1, String var2, int var3, int var4) {
      if (var2.length() < var3) {
         this.logWarning(var1 + " is shorter than recommended " + var3 + " character minimum. This prefix value is intended to distinguish CS requests from other data.");
      }

      if (var2.length() > var4) {
         this.logWarning(var1 + " is longer than recommended " + var4 + " character maximum. The more indicator characters added, the less data space available...");
      }

   }

   private void A(String var1, String var2, String var3, String var4) {
      if (var2.length() != 0 && var4.length() != 0) {
         if (var2.equalsIgnoreCase(var4)) {
            this.logError(var1 + " value (" + var2 + ") matches " + var3 + " value");
         } else if (var2.startsWith(var4)) {
            this.logError(var1 + " value (" + var2 + ") starts with " + var3 + " value (" + var4 + "). Overlapping values can cause DNS requests to fail.");
         } else if (var4.startsWith(var2)) {
            this.logError(var3 + " value (" + var4 + ") starts with " + var1 + " value (" + var2 + "). Overlapping values can cause DNS requests to fail.");
         }
      }

   }

   private static enum _A {
      C,
      B;
   }
}
