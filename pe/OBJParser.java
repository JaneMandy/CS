package pe;

import common.AssertUtils;
import common.CommonUtils;
import common.MudgeSanity;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Map.Entry;

public class OBJParser {
   protected DataInputStream content;
   protected byte[] bdata;
   protected ByteBuffer buffer;
   protected Map values;
   protected byte[] original;
   protected Stack frames;
   protected Map locations;
   protected LinkedList strings;
   protected boolean procassembly;
   protected String exesect;

   public boolean isProcessAssembly() {
      return this.procassembly;
   }

   protected void parseDirectory(int var1) throws IOException {
      long var2 = this.readLong();
      long var4 = this.readLong();
      this.put("DataDirectory." + var1 + ".VirtualAddress", var2);
      this.put("DataDirectory." + var1 + ".Size", var4);
   }

   protected void parseFunctionNameHint(int var1, int var2, int var3, List var4) throws IOException {
      this.jump((long)(var3 - var2 + var1));
      int var5 = this.readShort();
      String var6 = this.readString();
      var4.add(var6 + "@" + var5);
      this.complete();
   }

   protected List parseFunctionNameList(int var1, int var2, int var3) throws IOException {
      LinkedList var4 = new LinkedList();
      this.jump((long)(var1 - var2 + var3));

      while(true) {
         if (this.is64()) {
            long var7 = this.readQWord();
            if ((var7 & Long.MIN_VALUE) == Long.MIN_VALUE) {
               var7 &= Long.MAX_VALUE;
               var4.add("<ordinal>@" + var7);
            } else {
               if (var7 <= 0L) {
                  break;
               }

               this.parseFunctionNameHint(var3, var2, (int)var7, var4);
            }
         } else {
            int var5 = this.readInt();
            if ((var5 & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
               var5 &= Integer.MAX_VALUE;
               var4.add("<ordinal>@" + var5);
            } else {
               if (var5 <= 0) {
                  break;
               }

               this.parseFunctionNameHint(var3, var2, var5, var4);
            }
         }
      }

      this.complete();
      return var4;
   }

   protected boolean parseImport(int var1) throws IOException {
      int var2 = this.dirEntry(1);
      int var3 = this.get("DataDirectory.1.VirtualAddress");
      long var4 = (long)this.readInt();
      this.consume(8);
      long var6 = (long)this.readInt();
      long var8 = (long)this.readInt();
      if (var4 == 0L && var6 == 0L && var8 == 0L) {
         return false;
      } else {
         this.put("Import." + var1 + ".RVAFunctionNameList", var4);
         this.put("Import." + var1 + ".RVAFunctionNameList.X", this.parseFunctionNameList((int)var4, var3, var2));
         this.put("Import." + var1 + ".RVAModuleName", var6);
         this.put("Import." + var1 + ".RVAModuleName.X", this.getStringFromPointer((int)var6, var3, var2));
         this.put("Import." + var1 + ".RVAFunctionAddressList", var8);
         return true;
      }
   }

   public int getPointerForLocation(int var1, int var2) {
      int var3 = this.dirEntry(var1);
      int var4 = this.get("DataDirectory." + var1 + ".VirtualAddress");
      return var2 - var3 + var4;
   }

   protected String getStringFromPointer(int var1, int var2, int var3) throws IOException {
      this.jump((long)(var1 - var2 + var3));
      String var4 = this.readString();
      this.complete();
      return var4;
   }

   public List getExportedFunctions() {
      return (List)this.values.get("Export.FunctionNames");
   }

   public List getSectionsTable() {
      return (List)this.values.get("SectionsTable");
   }

   public byte[] carveExportedFunction(String var1) {
      int var2 = this.getFunctionOffset(var1);
      int var3 = this.getNextFunctionOffset(var1);
      if (var2 == -1) {
         CommonUtils.print_error("Could not find '" + var1 + "' in DLL");
         return new byte[0];
      } else {
         return Arrays.copyOfRange(this.original, var2, var3);
      }
   }

   public int getFunctionOffset(String var1) {
      List var2 = this.getExportedFunctions();
      List var3 = (List)this.values.get("Export.FunctionAddressesFixed");
      Iterator var4 = var2.iterator();
      Iterator var5 = var3.iterator();

      while(var4.hasNext() && var5.hasNext()) {
         String var6 = (String)var4.next();
         Long var7 = (Long)var5.next();
         if (var1.equals(var6)) {
            return var7.intValue();
         }
      }

      return -1;
   }

   public int getNextFunctionOffset(String var1) {
      long var2 = (long)this.getFunctionOffset(var1);
      List var4 = this.getExportedFunctions();
      List var5 = (List)this.values.get("Export.FunctionAddressesFixed");
      Iterator var6 = var4.iterator();
      Iterator var7 = var5.iterator();

      while(var6.hasNext() && var7.hasNext()) {
         Long var8 = (Long)var7.next();
         if (var8 > var2) {
            return var8.intValue();
         }
      }

      int var10 = this.get(".text.PointerToRawData");
      int var9 = this.get(".text.SizeOfRawData");
      return var10 + var9;
   }

   protected void printSymbol(int var1) {
      int var2 = this.get("Symbol." + var1 + ".e_value");
      int var3 = this.get("Symbol." + var1 + ".e_scnum");
      int var4 = this.get("Symbol." + var1 + ".e_type");
      int var5 = this.get("Symbol." + var1 + ".e_sclass");
      int var6 = this.get("Symbol." + var1 + ".e_numaux");
      String var7 = this.getString("Symbol." + var1 + ".e_auxval");
      CommonUtils.print_stat("Symbol " + var1 + ": " + this.getString("Symbol." + var1 + ".e") + "\n\te_value:  " + var2 + "\n\te_scnum:  " + var3 + "\n\te_sclass: " + var5 + "\n\te_type:   " + var4 + "\n\te_numaux: " + var6 + "\n\te_auxval: " + var7);
   }

   public String readStringTableOffset(int var1) throws IOException {
      this.jump((long)(this.get("PointerToSymbolTable") + this.get("NumberOfSymbols") * 18));
      this.consume(var1);
      String var2 = this.readString();
      this.complete();
      return var2;
   }

   protected String readSymbolName() throws IOException {
      this.jump((long)this.here());
      long var1 = this.readLong();
      this.complete();
      if (var1 == 0L) {
         this.consume(4);
         int var3 = this.readInt();
         return this.readStringTableOffset(var3);
      } else {
         return this.readString(8);
      }
   }

   protected void parseSymbol(int var1) throws IOException {
      this.put("Symbol." + var1 + ".e", this.readSymbolName());
      this.put("Symbol." + var1 + ".e_value", this.readLong());
      this.put("Symbol." + var1 + ".e_scnum", (long)this.readShort());
      this.put("Symbol." + var1 + ".e_type", (long)this.readShort());
      this.put("Symbol." + var1 + ".e_sclass", (long)this.readByte());
      this.put("Symbol." + var1 + ".e_numaux", (long)this.readByte());
      if (this.get("Symbol." + var1 + ".e_sclass") == 103) {
         this.put("Symbol." + var1 + ".e_auxval", this.readString(18));
      } else if (this.get("Symbol." + var1 + ".e_numaux") > 0) {
         this.consume(18 * this.get("Symbol." + var1 + ".e_numaux"));
      }

   }

   protected void parseSymbols() throws IOException {
      int var1 = this.get("NumberOfSymbols");

      for(int var2 = 0; var2 < var1; ++var2) {
         this.parseSymbol(var2);
         var2 += this.get("Symbol." + var2 + ".e_numaux");
      }

   }

   protected void parseExport() throws IOException {
      int var1 = this.dirEntry(0);
      int var2 = this.get("DataDirectory.0.VirtualAddress");
      this.consume(12);
      this.report("Export.Name");
      this.put("Export.Name", this.getStringFromPointer(this.readInt(), var2, var1));
      this.put("Export.Base", (long)this.readInt());
      this.put("Export.NumberOfFunctions", (long)this.readInt());
      this.put("Export.NumberOfNames", (long)this.readInt());
      this.put("Export.AddressOfFunctions", (long)this.readInt());
      this.put("Export.AddressOfNames", (long)this.readInt());
      this.put("Export.AddressOfNameOridinals", (long)this.readInt());
      this.jump((long)(this.get("Export.AddressOfNames") - var2 + var1));
      this.jump((long)(this.readInt() - var2 + var1));
      LinkedList var3 = new LinkedList();

      int var4;
      for(var4 = 0; var4 < this.get("Export.NumberOfNames"); ++var4) {
         this.report("Export.AddressOfName." + var4);
         var3.add(this.readString());
      }

      this.put("Export.FunctionNames", (List)var3);
      this.complete();
      this.complete();
      this.jump((long)(this.get("Export.AddressOfFunctions") - var2 + var1));
      var3 = new LinkedList();

      for(var4 = 0; var4 < this.get("Export.NumberOfNames"); ++var4) {
         var3.add(this.readLong());
      }

      this.put("Export.FunctionAddresses", (List)var3);
      this.complete();
      this.jump((long)(this.get("Export.AddressOfFunctions") - var2 + var1));
      var3 = new LinkedList();

      for(var4 = 0; var4 < this.get("Export.NumberOfNames"); ++var4) {
         var3.add(new Long(this.fixAddress(this.readLong())));
      }

      this.put("Export.FunctionAddressesFixed", (List)var3);
      this.complete();
   }

   public long fixAddress(long var1) {
      Iterator var3 = this.SectionsTable().iterator();

      while(var3.hasNext()) {
         String var4 = ((Class)var3.next()).makeConcatWithConstants<invokedynamic>(var3.next());
         if (this.inSection(var4, var1)) {
            return var1 - (long)this.sectionAddress(var4) + (long)this.sectionStart(var4);
         }
      }

      return -1L;
   }

   public static OBJParser load(InputStream var0) throws Exception {
      return new OBJParser(var0);
   }

   public static OBJParser load(byte[] var0) throws Exception {
      return new OBJParser(var0);
   }

   protected OBJParser(InputStream var1) throws Exception {
      this(CommonUtils.readAll(var1));
   }

   protected void jump(long var1) throws IOException {
      this.frames.push(this.content);
      this.content = new DataInputStream(new ByteArrayInputStream(this.original));
      if (var1 > 0L) {
         this.consume((int)var1);
      }

   }

   protected void complete() throws IOException {
      this.content.close();
      this.content = (DataInputStream)this.frames.pop();
   }

   protected OBJParser(byte[] var1) throws Exception {
      this.content = null;
      this.bdata = new byte[8];
      this.buffer = null;
      this.values = new HashMap();
      this.frames = new Stack();
      this.locations = new HashMap();
      this.strings = new LinkedList();
      this.procassembly = false;
      this.exesect = "";
      this.original = var1;
      this.buffer = ByteBuffer.wrap(this.bdata);
      this.buffer.order(ByteOrder.LITTLE_ENDIAN);
      this.content = new DataInputStream(new ByteArrayInputStream(var1));
      this.parse();
   }

   protected void consume(int var1) throws IOException {
      this.content.skipBytes(var1);
   }

   protected int readInt() throws IOException {
      CommonUtils.clearBuffer(this.buffer);
      this.content.read(this.bdata, 0, 4);
      return (int)this.buffer.getLong(0);
   }

   protected long readLong() throws IOException {
      CommonUtils.clearBuffer(this.buffer);
      this.content.read(this.bdata, 0, 4);
      return this.buffer.getLong(0);
   }

   protected long readQWord() throws IOException {
      CommonUtils.clearBuffer(this.buffer);
      this.content.read(this.bdata, 0, 8);
      return this.buffer.getLong(0);
   }

   protected byte readByte() throws IOException {
      return this.content.readByte();
   }

   protected char readChar() throws IOException {
      return (char)this.content.readByte();
   }

   protected char readChar(DataInputStream var1) throws IOException {
      return (char)var1.readByte();
   }

   protected int readShort() throws IOException {
      this.content.read(this.bdata, 0, 2);
      return this.buffer.getShort(0) & '\uffff';
   }

   protected String readString() throws IOException {
      this.string();
      StringBuffer var1 = new StringBuffer();

      while(true) {
         char var2 = this.readChar();
         if (var2 <= 0) {
            if (var1.toString().startsWith("_ReflectiveLoader") || var1.toString().startsWith("ReflectiveLoader")) {
               this.strings.removeLast();
            }

            if (var1.toString().contains("CorExeMain")) {
               this.procassembly = true;
            }

            return var1.toString();
         }

         var1.append(var2);
      }
   }

   protected String readString(int var1) throws IOException {
      this.string();
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1; ++var3) {
         char var4 = this.readChar();
         if (var4 > 0) {
            var2.append(var4);
         }
      }

      if (var2.toString().startsWith("_ReflectiveLoader") || var2.toString().startsWith("ReflectiveLoader")) {
         this.strings.removeLast();
      }

      if (var2.toString().contains("CorExeMain")) {
         this.procassembly = true;
      }

      return var2.toString();
   }

   protected void put(String var1, long var2) {
      this.values.put(var1, new Long(var2));
   }

   protected void put(String var1, List var2) {
      this.values.put(var1, var2);
   }

   protected void put(String var1, String var2) {
      this.values.put(var1, var2);
   }

   protected void put(String var1, Date var2) {
      this.values.put(var1, var2);
   }

   protected void error(String var1) {
      throw new RuntimeException(var1);
   }

   protected void header(String var1, int var2) throws Exception {
      this.report("header." + var1);
      int var3 = this.readShort();
      if (var3 != var2) {
         this.error("Header " + var1 + " Magic Failed: " + var3 + " expected (" + var2 + ")");
      }

   }

   public int get(String var1) {
      Long var2 = (Long)this.values.get(var1);
      return var2 == null ? 0 : var2.intValue();
   }

   public Date getDate(String var1) {
      return (Date)this.values.get(var1);
   }

   public String getString(String var1) {
      return (String)this.values.get(var1);
   }

   public String getDLLName() {
      return this.getString("Export.Name");
   }

   protected void put(String var1, String var2, long var3) {
      this.values.put(var1 + "." + var2, new Long(var3));
   }

   protected void put(String var1, String var2, String var3) {
      this.values.put(var1 + "." + var2, new Long(var3));
   }

   protected void readCharacteristics(String var1) throws IOException {
      long var2 = this.readLong();
      LinkedList var4 = new LinkedList();
      if ((var2 & 32L) == 32L) {
         var4.add("Code");
      }

      if ((var2 & 64L) == 64L) {
         var4.add("Initialized Data");
      }

      if ((var2 & 128L) == 128L) {
         var4.add("Uninitialized Data");
      }

      if ((var2 & 67108864L) == 67108864L) {
         var4.add("Section cannot be cached");
      }

      if ((var2 & 134217728L) == 134217728L) {
         var4.add("Section is not pageable");
      }

      if ((var2 & 268435456L) == 268435456L) {
         var4.add("Section is shared");
      }

      if ((var2 & 536870912L) == 536870912L) {
         var4.add("Executable");
      }

      if ((var2 & 1073741824L) == 1073741824L) {
         var4.add("Readable");
      }

      if ((var2 & 2147483648L) == 2147483648L) {
         var4.add("Writable");
      }

      var4.add("0x" + Long.toString(var2, 16));
      this.values.put(var1 + ".Characteristics", var4);
      if ((var2 & 536870912L) == 536870912L) {
         this.exesect = var1;
      }

   }

   public String getExeSect() {
      return this.exesect;
   }

   protected void readRelocation(String var1, int var2) throws IOException {
      long var3 = this.readLong();
      long var5 = this.readLong();
      long var7 = (long)this.readShort();
      this.put(var1, "Relocation." + var2 + ".r_vaddr", var3);
      this.put(var1, "Relocation." + var2 + ".r_symndx", var5);
      this.put(var1, "Relocation." + var2 + ".r_type", var7);
   }

   protected void parseRelocations(String var1) throws IOException {
      int var2 = this.get(var1 + ".NumberOfRelocations");
      int var3 = this.get(var1 + ".PointerToRelocations");
      this.jump((long)var3);

      for(int var4 = 0; var4 < var2; ++var4) {
         this.readRelocation(var1, var4);
      }

      this.complete();
   }

   protected Date readDate() throws IOException {
      return new Date(this.readLong() * 1000L);
   }

   public boolean hasSection(String var1) {
      if (this.values.containsKey("SectionsTable")) {
         HashSet var2 = new HashSet(this.SectionsTable());
         return var2.contains(var1);
      } else {
         return false;
      }
   }

   protected void parseSection(int var1) throws Exception {
      this.report("Sections.AddressOfName." + var1);
      String var2 = this.readString(8);
      if (this.hasSection(var2)) {
         var2 = var2 + "$" + var1;
      }

      this.append("SectionsTable", var2);
      this.put(var2, "VirtualSize", (long)this.readInt());
      this.put(var2, "VirtualAddress", (long)this.readInt());
      this.put(var2, "SizeOfRawData", (long)this.readInt());
      this.put(var2, "PointerToRawData", (long)this.readInt());
      this.put(var2, "PointerToRelocations", (long)this.readInt());
      this.put(var2, "PointerToLinenumbers", (long)this.readInt());
      this.report(var2 + ".NumberOfRelocations");
      this.put(var2, "NumberOfRelocations", (long)this.readShort());
      this.put(var2, "NumberOfLinenumbers", (long)this.readShort());
      this.readCharacteristics(var2);
      this.parseRelocations(var2);
   }

   protected void append(String var1, String var2) {
      if (this.values.get(var1) == null) {
         this.values.put(var1, new LinkedList());
      }

      LinkedList var3 = (LinkedList)this.values.get(var1);
      var3.add(var2);
   }

   public int sectionStart(String var1) {
      return this.get(var1 + ".PointerToRawData");
   }

   public int sectionSize(String var1) {
      return this.get(var1 + ".SizeOfRawData");
   }

   public int sectionAddress(String var1) {
      return this.get(var1 + ".VirtualAddress");
   }

   public int sectionEnd(String var1) {
      return this.get(var1 + ".VirtualAddress") + this.get(var1 + ".VirtualSize");
   }

   protected boolean inSection(String var1, long var2) {
      long var4 = (long)this.sectionAddress(var1);
      long var6 = (long)this.get(var1 + ".VirtualSize");
      return var2 >= var4 && var2 < var4 + var6;
   }

   public List SectionsTable() {
      return (List)this.values.get("SectionsTable");
   }

   protected int dirEntry(int var1) {
      int var2 = this.get("DataDirectory." + var1 + ".VirtualAddress");
      Iterator var3 = this.SectionsTable().iterator();

      String var4;
      int var5;
      int var6;
      do {
         if (!var3.hasNext()) {
            throw new RuntimeException("Directory entry: " + var1 + "@" + var2 + " not found");
         }

         var4 = (String)var3.next();
         var5 = this.sectionAddress(var4);
         var6 = this.sectionSize(var4);
      } while(var2 < var5 || var2 >= var5 + var6);

      return this.sectionStart(var4) + (var2 - var5);
   }

   public boolean is64() {
      return this.get("Machine") == 34404;
   }

   public boolean is86() {
      return this.get("Machine") == 332;
   }

   public int relocationsCount(String var1) {
      return this.get(var1 + ".NumberOfRelocations");
   }

   public Relocation getRelocation(String var1, int var2) {
      return new Relocation(this, var1, var2);
   }

   public int here() throws IOException {
      return this.original.length - this.content.available();
   }

   public void string() {
      try {
         int var1 = this.here();
         this.strings.add(new Integer(var1));
      } catch (Exception var2) {
         MudgeSanity.logException("string", var2, false);
      }

   }

   public void report(String var1) {
      try {
         int var2 = this.here();
         this.locations.put(var1, new Integer(var2));
      } catch (Exception var3) {
         MudgeSanity.logException("report: " + var1, var3, false);
      }

   }

   public Iterator stringIterator() {
      return this.strings.iterator();
   }

   public int getLocation(String var1) {
      if (!this.locations.containsKey(var1)) {
         throw new IllegalArgumentException("No location for '" + var1 + "'");
      } else {
         int var2 = (Integer)this.locations.get(var1);
         AssertUtils.Test(var2 >= 60, var1 + " (offset: " + var2 + ") Reflective Loader bootstrap region");
         return var2;
      }
   }

   protected void parse() throws Exception {
      this.put("Machine", (long)this.readShort());
      if (!this.is86() && !this.is64()) {
         this.error("Invalid Machine header value '" + this.get("Machine"));
      }

      this.put("Sections", (long)this.readShort());
      this.report("TimeDateStamp");
      this.put("TimeDateStamp", this.readDate());
      this.put("PointerToSymbolTable", (long)this.readInt());
      this.report("NumberOfSymbols");
      this.put("NumberOfSymbols", (long)this.readInt());
      this.put("SizeOfOptionalHeader", (long)this.readShort());
      this.report("Characteristics");
      this.put("Characteristics", (long)this.readShort());

      int var1;
      for(var1 = 0; var1 < this.get("NumberOfRvaAndSizes"); ++var1) {
         this.parseDirectory(var1);
      }

      for(var1 = 0; var1 < this.get("Sections"); ++var1) {
         this.parseSection(var1);
      }

      this.report("HeaderSlack");
      if (this.get("DataDirectory.1.VirtualAddress") != 0) {
         this.jump((long)this.dirEntry(1));

         for(var1 = 0; this.parseImport(var1); ++var1) {
         }

         this.complete();
      }

      if (this.get("DataDirectory.0.VirtualAddress") != 0) {
         this.jump((long)this.dirEntry(0));
         this.parseExport();
         this.complete();
      }

      this.jump((long)this.get("PointerToSymbolTable"));
      this.parseSymbols();
      this.complete();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Key                                Value\n");
      var1.append("---                                -----\n");
      Iterator var2 = (new TreeMap(this.values)).entrySet().iterator();

      while(true) {
         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();

            String var4;
            for(var4 = (String)var3.getKey(); var4.length() < 35; var4 = var4 + " ") {
            }

            var1.append(var4);
            if (var3.getValue() instanceof Long) {
               Long var9 = (Long)var3.getValue();
               long var6 = var9;

               String var8;
               for(var8 = "0x" + Long.toString(var6, 16); var8.length() < 12; var8 = var8 + " ") {
               }

               var8 = var8 + var6;
               var1.append(var8);
               var1.append("\n");
            } else if (var3.getValue() instanceof String) {
               var1.append(var3.getValue() + "\n");
            } else if (var3.getValue() instanceof List) {
               var1.append(var3.getValue() + "\n");
            } else if (var3.getValue() instanceof Date) {
               long var5 = ((Date)var3.getValue()).getTime() / 1000L;

               String var7;
               for(var7 = "0x" + Long.toString(var5, 16); var7.length() < 12; var7 = var7 + " ") {
               }

               for(var7 = var7 + var5; var7.length() < 32; var7 = var7 + " ") {
               }

               var7 = var7 + CommonUtils.formatDateAny("dd MMM yyyy HH:mm:ss", var5 * 1000L);
               var1.append(var7);
               var1.append("\n");
            }
         }

         return var1.toString();
      }
   }

   public String getStringAt(int var1) {
      StringBuffer var2;
      for(var2 = new StringBuffer(); this.original[var1] != 0; ++var1) {
         var2.append((char)this.original[var1]);
      }

      return var2.toString();
   }

   public void stringWalk() {
      Iterator var1 = this.stringIterator();

      while(var1.hasNext()) {
         int var2 = (Integer)var1.next();
         CommonUtils.print_stat("[" + var2 + "] " + this.getStringAt(var2));
      }

   }

   public static void dump(String[] var0) throws Exception {
      File var1 = new File(var0[0]);
      OBJParser var2 = load((InputStream)(new FileInputStream(var1)));
      System.out.println(var2.toString());
      System.out.println("\n\nLocations:\n----------");
      Iterator var3 = var2.locations.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();

         String var5;
         for(var5 = (String)var4.getKey(); var5.length() < 31; var5 = var5 + " ") {
         }

         System.out.println(var5 + " " + var4.getValue());
      }

   }

   public static void main(String[] var0) throws Exception {
      dump(var0);
   }
}
