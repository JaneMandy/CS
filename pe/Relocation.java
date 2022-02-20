package pe;

import common.CommonUtils;
import java.util.Iterator;

public class Relocation {
   protected OBJParser info;
   protected String section;
   protected int index;
   public static final int RELOC_REL32 = 20;
   public static final int RELOC_ADDR32 = 6;
   public static final int RELOC64_REL32 = 4;

   public Relocation(OBJParser var1, String var2, int var3) {
      this.info = var1;
      this.index = var3;
      this.section = var2;
   }

   public int getOffset() {
      return this.info.get(this.section + ".Relocation." + this.index + ".r_vaddr");
   }

   public int getType() {
      return this.info.get(this.section + ".Relocation." + this.index + ".r_type");
   }

   public String getSymbol() {
      int var1 = this.info.get(this.section + ".Relocation." + this.index + ".r_symndx");
      return this.info.getString("Symbol." + var1 + ".e");
   }

   public String getSection() {
      int var1 = this.info.get(this.section + ".Relocation." + this.index + ".r_symndx");
      int var2 = this.info.get("Symbol." + var1 + ".e_scnum");
      if (var2 == 0) {
         return "";
      } else if (var2 == -1) {
         return "<e_value is a constant>";
      } else if (var2 == -2) {
         return "<debug symbol>";
      } else {
         Iterator var3 = this.info.getSectionsTable().iterator();

         for(int var4 = 1; var3.hasNext(); ++var4) {
            String var5 = (String)var3.next();
            if (var4 == var2) {
               return var5;
            }
         }

         return "<unknown>";
      }
   }

   public int getOffsetInSection() {
      int var1 = this.info.get(this.section + ".Relocation." + this.index + ".r_symndx");
      return this.info.get("Symbol." + var1 + ".e_value");
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.getType() == 6) {
         var1.append("RELOC_ADDR32");
      } else if (this.getType() == 20) {
         var1.append("RELOC_REL32");
      } else if (this.getType() == 4) {
         var1.append("RELOC64_REL32");
      } else {
         var1.append("RELOC_UNK_" + this.getType());
      }

      var1.append(" ");
      var1.append(this.getOffset());
      var1.append(" 0x");
      var1.append(CommonUtils.toHex((long)this.getOffset()));
      var1.append(" ");
      var1.append(this.getSymbol());
      var1.append(" (");
      var1.append(this.getSection());
      if ("".equals(this.getSection())) {
         var1.append(")");
      } else {
         var1.append(",");
         var1.append(this.getOffsetInSection());
         var1.append(" 0x");
         var1.append(CommonUtils.toHex((long)this.getOffsetInSection()));
         var1.append(")");
      }

      return var1.toString();
   }
}
