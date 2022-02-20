package org.apache.xalan.xsltc.dom;

import java.util.Vector;
import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xml.dtm.DTMAxisIterator;

public abstract class NodeCounter implements Axis {
   public static final int END = -1;
   protected int _node = -1;
   protected int _nodeType = -1;
   protected int _value = Integer.MIN_VALUE;
   public final DOM _document;
   public final DTMAxisIterator _iterator;
   public final Translet _translet;
   protected String _format;
   protected String _lang;
   protected String _letterValue;
   protected String _groupSep;
   protected int _groupSize;
   private boolean separFirst = true;
   private boolean separLast = false;
   private Vector separToks = null;
   private Vector formatToks = null;
   private int nSepars = 0;
   private int nFormats = 0;
   private static String[] Thousands = new String[]{"", "m", "mm", "mmm"};
   private static String[] Hundreds = new String[]{"", "c", "cc", "ccc", "cd", "d", "dc", "dcc", "dccc", "cm"};
   private static String[] Tens = new String[]{"", "x", "xx", "xxx", "xl", "l", "lx", "lxx", "lxxx", "xc"};
   private static String[] Ones = new String[]{"", "i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix"};

   protected NodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
      this._translet = translet;
      this._document = document;
      this._iterator = iterator;
   }

   public abstract NodeCounter setStartNode(int var1);

   public NodeCounter setValue(int value) {
      this._value = value;
      return this;
   }

   protected void setFormatting(String format, String lang, String letterValue, String groupSep, String groupSize) {
      this._lang = lang;
      this._format = format;
      this._groupSep = groupSep;
      this._letterValue = letterValue;

      try {
         this._groupSize = Integer.parseInt(groupSize);
      } catch (NumberFormatException var11) {
         this._groupSize = 0;
      }

      int length = this._format.length();
      boolean isFirst = true;
      this.separFirst = true;
      this.separLast = false;
      this.separToks = new Vector();
      this.formatToks = new Vector();
      int j = false;
      int i = 0;

      while(i < length) {
         char c = this._format.charAt(i);

         int j;
         for(j = i; Character.isLetterOrDigit(c); c = this._format.charAt(i)) {
            ++i;
            if (i == length) {
               break;
            }
         }

         if (i > j) {
            if (isFirst) {
               this.separToks.addElement(".");
               isFirst = this.separFirst = false;
            }

            this.formatToks.addElement(this._format.substring(j, i));
         }

         if (i == length) {
            break;
         }

         c = this._format.charAt(i);

         for(j = i; !Character.isLetterOrDigit(c); isFirst = false) {
            ++i;
            if (i == length) {
               break;
            }

            c = this._format.charAt(i);
         }

         if (i > j) {
            this.separToks.addElement(this._format.substring(j, i));
         }
      }

      this.nSepars = this.separToks.size();
      this.nFormats = this.formatToks.size();
      if (this.nSepars > this.nFormats) {
         this.separLast = true;
      }

      if (this.separFirst) {
         --this.nSepars;
      }

      if (this.separLast) {
         --this.nSepars;
      }

      if (this.nSepars == 0) {
         this.separToks.insertElementAt(".", 1);
         ++this.nSepars;
      }

      if (this.separFirst) {
         ++this.nSepars;
      }

   }

   public NodeCounter setDefaultFormatting() {
      this.setFormatting("1", "en", "alphabetic", (String)null, (String)null);
      return this;
   }

   public abstract String getCounter();

   public String getCounter(String format, String lang, String letterValue, String groupSep, String groupSize) {
      this.setFormatting(format, lang, letterValue, groupSep, groupSize);
      return this.getCounter();
   }

   public boolean matchesCount(int node) {
      return this._nodeType == this._document.getExpandedTypeID(node);
   }

   public boolean matchesFrom(int node) {
      return false;
   }

   protected String formatNumbers(int value) {
      return this.formatNumbers(new int[]{value});
   }

   protected String formatNumbers(int[] values) {
      int nValues = values.length;
      int length = this._format.length();
      boolean isEmpty = true;

      for(int i = 0; i < nValues; ++i) {
         if (values[i] != Integer.MIN_VALUE) {
            isEmpty = false;
         }
      }

      if (isEmpty) {
         return "";
      } else {
         boolean isFirst = true;
         int t = 0;
         int n = 0;
         int s = 1;
         StringBuffer buffer = new StringBuffer();
         if (this.separFirst) {
            buffer.append((String)this.separToks.elementAt(0));
         }

         for(; n < nValues; ++n) {
            int value = values[n];
            if (value != Integer.MIN_VALUE) {
               if (!isFirst) {
                  buffer.append((String)this.separToks.elementAt(s++));
               }

               this.formatValue(value, (String)this.formatToks.elementAt(t++), buffer);
               if (t == this.nFormats) {
                  --t;
               }

               if (s >= this.nSepars) {
                  --s;
               }

               isFirst = false;
            }
         }

         if (this.separLast) {
            buffer.append((String)this.separToks.lastElement());
         }

         return buffer.toString();
      }
   }

   private void formatValue(int value, String format, StringBuffer buffer) {
      char c = format.charAt(0);
      if (Character.isDigit(c)) {
         char zero = (char)(c - Character.getNumericValue(c));
         StringBuffer temp = buffer;
         if (this._groupSize > 0) {
            temp = new StringBuffer();
         }

         String s = "";

         for(int n = value; n > 0; n /= 10) {
            s = (char)(zero + n % 10) + s;
         }

         for(int i = 0; i < format.length() - s.length(); ++i) {
            temp.append(zero);
         }

         temp.append(s);
         if (this._groupSize > 0) {
            for(int i = 0; i < temp.length(); ++i) {
               if (i != 0 && (temp.length() - i) % this._groupSize == 0) {
                  buffer.append(this._groupSep);
               }

               buffer.append(temp.charAt(i));
            }
         }
      } else if (c == 'i' && !this._letterValue.equals("alphabetic")) {
         buffer.append(this.romanValue(value));
      } else if (c == 'I' && !this._letterValue.equals("alphabetic")) {
         buffer.append(this.romanValue(value).toUpperCase());
      } else {
         int max = c;
         if (c >= 945 && c <= 969) {
            max = 969;
         } else {
            while(Character.isLetterOrDigit((char)(max + 1))) {
               ++max;
            }
         }

         buffer.append(this.alphaValue(value, c, max));
      }

   }

   private String alphaValue(int value, int min, int max) {
      if (value <= 0) {
         return "" + value;
      } else {
         int range = max - min + 1;
         char last = (char)((value - 1) % range + min);
         return value > range ? this.alphaValue((value - 1) / range, min, max) + last : "" + last;
      }
   }

   private String romanValue(int n) {
      return n > 0 && n <= 4000 ? Thousands[n / 1000] + Hundreds[n / 100 % 10] + Tens[n / 10 % 10] + Ones[n % 10] : "" + n;
   }
}
