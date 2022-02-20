package org.apache.batik.svggen.font.table;

import java.io.IOException;
import java.io.RandomAccessFile;

public class LangSysRecord {
   private int tag;
   private int offset;

   public LangSysRecord(RandomAccessFile var1) throws IOException {
      this.tag = var1.readInt();
      this.offset = var1.readUnsignedShort();
   }

   public int getTag() {
      return this.tag;
   }

   public int getOffset() {
      return this.offset;
   }
}
