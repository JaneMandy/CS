package org.apache.xml.serializer;

import java.util.Hashtable;
import java.util.Properties;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.WrappedRuntimeException;
import org.xml.sax.ContentHandler;

public abstract class SerializerFactory {
   private static Hashtable m_formats = new Hashtable();

   public static Serializer getSerializer(Properties format) {
      try {
         String method = format.getProperty("method");
         if (method == null) {
            throw new IllegalArgumentException("The output format has a null method name");
         } else {
            String className = format.getProperty("{http://xml.apache.org/xalan}content-handler");
            if (null == className) {
               Properties methodDefaults = OutputPropertiesFactory.getDefaultMethodProperties(method);
               className = methodDefaults.getProperty("{http://xml.apache.org/xalan}content-handler");
               if (null == className) {
                  throw new IllegalArgumentException("The output format must have a '{http://xml.apache.org/xalan}content-handler' property!");
               }
            }

            ClassLoader loader = ObjectFactory.findClassLoader();
            Class cls = ObjectFactory.findProviderClass(className, loader, true);
            Object obj = cls.newInstance();
            Object ser;
            if (obj instanceof SerializationHandler) {
               ser = (Serializer)cls.newInstance();
               ((Serializer)ser).setOutputFormat(format);
            } else {
               if (!(obj instanceof ContentHandler)) {
                  throw new Exception(XMLMessages.createXMLMessage("ER_SERIALIZER_NOT_CONTENTHANDLER", new Object[]{className}));
               }

               className = "org.apache.xml.serializer.ToXMLSAXHandler";
               cls = ObjectFactory.findProviderClass(className, loader, true);
               SerializationHandler sh = (SerializationHandler)cls.newInstance();
               sh.setContentHandler((ContentHandler)obj);
               sh.setOutputFormat(format);
               ser = sh;
            }

            return (Serializer)ser;
         }
      } catch (Exception var8) {
         throw new WrappedRuntimeException(var8);
      }
   }
}
