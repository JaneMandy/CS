package pe;

import common.Manipulator;
import common.MudgeSanity;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OBJExecutableSimple {
   protected OBJParser info = null;
   protected Manipulator data;
   protected List errors = new LinkedList();
   protected String exesect = "";

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

   public OBJExecutableSimple(byte[] var1) {
      this.data = new Manipulator(var1);
      this.data.little();
   }

   public OBJParser getInfo() {
      return this.info;
   }

   public void parse() {
      try {
         this.info = OBJParser.load(this.data.getBytes());
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

   public byte[] getCode() {
      int var1 = this.info.sectionStart(this.exesect);
      int var2 = this.info.sectionSize(this.exesect);
      if (var2 == 0) {
         this.error("No .text section in object file");
      }

      return Arrays.copyOfRange(this.data.getBytes(), var1, var1 + var2);
   }

   public int getCodeSize() {
      return this.info.sectionSize(this.exesect);
   }

   public void processRelocations() {
      for(int var1 = 0; var1 < this.info.relocationsCount(this.exesect); ++var1) {
         Relocation var2 = this.info.getRelocation(this.exesect, var1);
         if (this.exesect.equals(var2.getSection())) {
            if (var2.getType() != 20 && var2.getType() != 4) {
               this.error("Unknown relocation type: " + var2.getType() + " for " + var2.getSymbol() + " in " + var2.getSection());
            } else {
               int var3 = this.info.sectionStart(this.exesect);
               int var4 = this.data.getInt(var3 + var2.getOffset());
               int var5 = var2.getOffsetInSection() - (var2.getOffset() + 4);
               this.data.setLong(var3 + var2.getOffset(), (long)var5);
            }
         } else {
            this.error("Unknown symbol '" + var2.getSymbol() + "' from: " + var2.getSection());
         }
      }

   }
}
