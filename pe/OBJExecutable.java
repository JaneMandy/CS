package pe;

import common.CommonUtils;
import common.MudgeSanity;
import common.Packer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OBJExecutable {
   protected OBJParser info;
   protected byte[] data;
   protected List errors;
   protected String epfunc;
   protected String exesect;

   public boolean hasErrors() {
      return this.errors.size() > 0;
   }

   public String getErrors() {
      StringBuffer var1 = new StringBuffer();
      Iterator var2 = this.errors.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.append(var3);
         var1.append("\n");
      }

      return var1.toString();
   }

   public void error(String var1) {
      this.errors.add(var1);
   }

   public OBJExecutable(byte[] var1, String var2) {
      this.info = null;
      this.errors = new LinkedList();
      this.epfunc = "";
      this.exesect = "";
      this.data = var1;
      this.epfunc = var2;
   }

   public OBJExecutable(byte[] var1) {
      this(var1, "go");
   }

   public OBJParser getInfo() {
      return this.info;
   }

   public void parse() {
      try {
         this.info = OBJParser.load(this.data);
         if (this.info.hasSection(".text")) {
            this.exesect = ".text";
         } else {
            this.exesect = this.info.getExeSect();
         }

      } catch (Exception var2) {
         this.error(var2.getMessage());
         MudgeSanity.logException("Error parsing object file", var2, false);
      }
   }

   public int getEntryPoint() {
      int var1 = this.info.get("NumberOfSymbols");

      for(int var2 = 0; var2 < var1; ++var2) {
         String var3 = this.info.getString("Symbol." + var2 + ".e");
         if (("_" + this.epfunc).equals(var3) || this.epfunc.equals(var3)) {
            return this.info.get("Symbol." + var2 + ".e_value");
         }
      }

      this.error("Entry function '" + this.epfunc + "' is not defined.");
      return 0;
   }

   public byte[] getCode() {
      int var1 = this.info.sectionStart(this.exesect);
      int var2 = this.info.sectionSize(this.exesect);
      if (var2 == 0) {
         this.error("No .text section in object file");
      }

      return Arrays.copyOfRange(this.data, var1, var1 + var2);
   }

   public byte[] getRData() {
      int var1 = this.info.sectionStart(".rdata");
      int var2 = this.info.sectionSize(".rdata");
      return Arrays.copyOfRange(this.data, var1, var1 + var2);
   }

   public byte[] getData() {
      int var1 = this.info.sectionStart(".data");
      int var2 = this.info.sectionSize(".data");
      return Arrays.copyOfRange(this.data, var1, var1 + var2);
   }

   public byte[] getRelocations() {
      Packer var1 = new Packer();
      var1.little();

      for(int var2 = 0; var2 < this.info.relocationsCount(this.exesect); ++var2) {
         Relocation var3 = this.info.getRelocation(this.exesect, var2);
         if (".rdata".equals(var3.getSection())) {
            var1.addShort(var3.getType());
            var1.addShort(1024);
            var1.addInt(var3.getOffset());
            var1.addInt(var3.getOffsetInSection());
         } else if (".data".equals(var3.getSection())) {
            var1.addShort(var3.getType());
            var1.addShort(1025);
            var1.addInt(var3.getOffset());
            var1.addInt(var3.getOffsetInSection());
         } else if (this.exesect.equals(var3.getSection())) {
            var1.addShort(var3.getType());
            var1.addShort(1026);
            var1.addInt(var3.getOffset());
            var1.addInt(var3.getOffsetInSection());
         } else if (BOFUtils.isInternalFunction(var3.getSymbol())) {
            var1.addShort(var3.getType());
            var1.addShort(BOFUtils.findInternalFunction(var3.getSymbol()));
            var1.addInt(var3.getOffset());
            var1.addInt(0);
         } else if (BOFUtils.isDynamicFunction(var3.getSymbol())) {
            var1.addShort(var3.getType());
            var1.addShort(1027);
            var1.addInt(var3.getOffset());
            var1.addInt(0);
            var1.big();
            var1.addLengthAndStringASCIIZ(BOFUtils.getModule(var3.getSymbol()));
            var1.addLengthAndStringASCIIZ(BOFUtils.getFunction(var3.getSymbol()));
            var1.little();
         } else {
            this.error("Unknown symbol '" + var3.getSymbol() + "'");
         }
      }

      var1.addShort(0);
      var1.addShort(1028);
      var1.addInt(0);
      var1.addInt(0);
      return var1.getBytes();
   }

   public void walkRelocations() {
      for(int var1 = 0; var1 < this.info.relocationsCount(this.exesect); ++var1) {
         System.out.println(this.info.getRelocation(this.exesect, var1));
      }

   }

   public static void main(String[] var0) throws Exception {
      byte[] var1 = CommonUtils.readFile(var0[0]);
      OBJExecutable var2 = new OBJExecutable(var1);
      var2.parse();
      OBJParser.dump(var0);
      CommonUtils.print_stat("RELOCATIONS");
      var2.walkRelocations();
      CommonUtils.print_stat("SIZE: " + (var2.getRelocations().length + var2.getData().length + var2.getCode().length) + " bytes");
   }
}
