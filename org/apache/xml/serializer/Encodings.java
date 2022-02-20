package org.apache.xml.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.xml.utils.WrappedRuntimeException;

public class Encodings {
   static final int m_defaultLastPrintable = 127;
   static final String ENCODINGS_FILE = "org/apache/xml/serializer/Encodings.properties";
   static final String ENCODINGS_PROP = "org.apache.xalan.serialize.encodings";
   private static final java.lang.reflect.Method SUN_CHAR2BYTE_CONVERTER_METHOD = findCharToByteConverterMethod();
   public static final String DEFAULT_MIME_ENCODING = "UTF-8";
   private static final Hashtable _encodingTableKeyJava = new Hashtable();
   private static final Hashtable _encodingTableKeyMime = new Hashtable();
   private static final EncodingInfo[] _encodings = loadEncodingInfo();

   private static java.lang.reflect.Method findCharToByteConverterMethod() {
      try {
         AccessController.doPrivileged(new PrivilegedAction() {
            // $FF: synthetic field
            static Class class$java$lang$String;

            public Object run() {
               try {
                  Class charToByteConverterClass = Class.forName("sun.io.CharToByteConverter");
                  Class[] argTypes = new Class[]{class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String};
                  return charToByteConverterClass.getMethod("getConverter", argTypes);
               } catch (Exception var3) {
                  throw new RuntimeException(var3.toString());
               }
            }

            // $FF: synthetic method
            static Class class$(String x0) {
               try {
                  return Class.forName(x0);
               } catch (ClassNotFoundException var2) {
                  throw new NoClassDefFoundError(var2.getMessage());
               }
            }
         });
      } catch (Exception var1) {
         System.err.println("Warning: Could not get charToByteConverterClass!");
      }

      return null;
   }

   public static Writer getWriter(OutputStream output, String encoding) throws UnsupportedEncodingException {
      for(int i = 0; i < _encodings.length; ++i) {
         if (_encodings[i].name.equalsIgnoreCase(encoding)) {
            try {
               return new OutputStreamWriter(output, _encodings[i].javaName);
            } catch (IllegalArgumentException var6) {
            } catch (UnsupportedEncodingException var7) {
            }
         }
      }

      try {
         return new OutputStreamWriter(output, encoding);
      } catch (IllegalArgumentException var5) {
         throw new UnsupportedEncodingException(encoding);
      }
   }

   public static Object getCharToByteConverter(String encoding) {
      if (SUN_CHAR2BYTE_CONVERTER_METHOD == null) {
         return null;
      } else {
         Object[] args = new Object[1];

         for(int i = 0; i < _encodings.length; ++i) {
            if (_encodings[i].name.equalsIgnoreCase(encoding)) {
               try {
                  args[0] = _encodings[i].javaName;
                  Object converter = SUN_CHAR2BYTE_CONVERTER_METHOD.invoke((Object)null, args);
                  if (null != converter) {
                     return converter;
                  }
               } catch (Exception var4) {
               }
            }
         }

         return null;
      }
   }

   public static int getLastPrintable(String encoding) {
      String normalizedEncoding = encoding.toUpperCase();
      EncodingInfo ei = (EncodingInfo)_encodingTableKeyJava.get(normalizedEncoding);
      if (ei == null) {
         ei = (EncodingInfo)_encodingTableKeyMime.get(normalizedEncoding);
      }

      return ei != null ? ei.lastPrintable : 127;
   }

   public static int getLastPrintable() {
      return 127;
   }

   public static String getMimeEncoding(String encoding) {
      if (null == encoding) {
         try {
            encoding = System.getProperty("file.encoding", "UTF8");
            if (null != encoding) {
               String jencoding = !encoding.equalsIgnoreCase("Cp1252") && !encoding.equalsIgnoreCase("ISO8859_1") && !encoding.equalsIgnoreCase("8859_1") && !encoding.equalsIgnoreCase("UTF8") ? convertJava2MimeEncoding(encoding) : "UTF-8";
               encoding = null != jencoding ? jencoding : "UTF-8";
            } else {
               encoding = "UTF-8";
            }
         } catch (SecurityException var2) {
            encoding = "UTF-8";
         }
      } else {
         encoding = convertJava2MimeEncoding(encoding);
      }

      return encoding;
   }

   public static String convertJava2MimeEncoding(String encoding) {
      EncodingInfo enc = (EncodingInfo)_encodingTableKeyJava.get(encoding.toUpperCase());
      return null != enc ? enc.name : encoding;
   }

   public static String convertMime2JavaEncoding(String encoding) {
      for(int i = 0; i < _encodings.length; ++i) {
         if (_encodings[i].name.equalsIgnoreCase(encoding)) {
            return _encodings[i].javaName;
         }
      }

      return encoding;
   }

   private static EncodingInfo[] loadEncodingInfo() {
      URL url = null;

      try {
         String urlString = null;
         InputStream is = null;

         try {
            urlString = System.getProperty("org.apache.xalan.serialize.encodings", "");
         } catch (SecurityException var18) {
         }

         if (urlString != null && urlString.length() > 0) {
            url = new URL(urlString);
            is = url.openStream();
         }

         if (is == null) {
            SecuritySupport ss = SecuritySupport.getInstance();
            is = ss.getResourceAsStream(ObjectFactory.findClassLoader(), "org/apache/xml/serializer/Encodings.properties");
         }

         Properties props = new Properties();
         if (is != null) {
            props.load(is);
            is.close();
         }

         int totalEntries = props.size();
         int totalMimeNames = 0;
         Enumeration keys = props.keys();

         int i;
         for(int i = 0; i < totalEntries; ++i) {
            String javaName = (String)keys.nextElement();
            String val = props.getProperty(javaName);
            ++totalMimeNames;
            i = val.indexOf(32);

            for(int j = 0; j < i; ++j) {
               if (val.charAt(j) == ',') {
                  ++totalMimeNames;
               }
            }
         }

         EncodingInfo[] ret = new EncodingInfo[totalMimeNames];
         int j = 0;
         keys = props.keys();

         for(i = 0; i < totalEntries; ++i) {
            String javaName = (String)keys.nextElement();
            String val = props.getProperty(javaName);
            int pos = val.indexOf(32);
            if (pos < 0) {
               boolean var25 = true;
            } else {
               int lastPrintable = Integer.decode(val.substring(pos).trim());
               StringTokenizer st = new StringTokenizer(val.substring(0, pos), ",");

               for(boolean first = true; st.hasMoreTokens(); first = false) {
                  String mimeName = st.nextToken();
                  ret[j] = new EncodingInfo(mimeName, javaName, lastPrintable);
                  _encodingTableKeyMime.put(mimeName.toUpperCase(), ret[j]);
                  if (first) {
                     _encodingTableKeyJava.put(javaName.toUpperCase(), ret[j]);
                  }

                  ++j;
               }
            }
         }

         return ret;
      } catch (MalformedURLException var19) {
         throw new WrappedRuntimeException(var19);
      } catch (IOException var20) {
         throw new WrappedRuntimeException(var20);
      }
   }
}
