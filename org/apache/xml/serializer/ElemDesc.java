package org.apache.xml.serializer;

import org.apache.xml.utils.StringToIntTable;

public final class ElemDesc {
   int m_flags;
   StringToIntTable m_attrs = null;
   public static final int EMPTY = 2;
   public static final int FLOW = 4;
   public static final int BLOCK = 8;
   public static final int BLOCKFORM = 16;
   public static final int BLOCKFORMFIELDSET = 32;
   public static final int CDATA = 64;
   public static final int PCDATA = 128;
   public static final int RAW = 256;
   public static final int INLINE = 512;
   public static final int INLINEA = 1024;
   public static final int INLINELABEL = 2048;
   public static final int FONTSTYLE = 4096;
   public static final int PHRASE = 8192;
   public static final int FORMCTRL = 16384;
   public static final int SPECIAL = 32768;
   public static final int ASPECIAL = 65536;
   public static final int HEADMISC = 131072;
   public static final int HEAD = 262144;
   public static final int LIST = 524288;
   public static final int PREFORMATTED = 1048576;
   public static final int WHITESPACESENSITIVE = 2097152;
   public static final int HEADELEM = 4194304;
   public static final int ATTRURL = 2;
   public static final int ATTREMPTY = 4;

   public ElemDesc(int flags) {
      this.m_flags = flags;
   }

   public boolean is(int flags) {
      return (this.m_flags & flags) != 0;
   }

   public int getFlags() {
      return this.m_flags;
   }

   public void setAttr(String name, int flags) {
      if (null == this.m_attrs) {
         this.m_attrs = new StringToIntTable();
      }

      this.m_attrs.put(name, flags);
   }

   public boolean isAttrFlagSet(String name, int flags) {
      return null != this.m_attrs ? (this.m_attrs.getIgnoreCase(name) & flags) != 0 : false;
   }
}
