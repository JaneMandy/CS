package org.apache.xalan.xsltc.compiler.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

class ObjectFactory {
   private static final String DEFAULT_PROPERTIES_FILENAME = "xalan.properties";
   private static final String SERVICES_PATH = "META-INF/services/";
   private static final boolean DEBUG = false;
   private static Properties fXalanProperties = null;
   private static long fLastModified = -1L;
   // $FF: synthetic field
   static Class class$org$apache$xalan$xsltc$compiler$util$ObjectFactory;

   static Object createObject(String factoryId, String fallbackClassName) throws ObjectFactory.ConfigurationError {
      return createObject(factoryId, (String)null, fallbackClassName);
   }

   static Object createObject(String factoryId, String propertiesFilename, String fallbackClassName) throws ObjectFactory.ConfigurationError {
      Class factoryClass = lookUpFactoryClass(factoryId, propertiesFilename, fallbackClassName);
      if (factoryClass == null) {
         throw new ObjectFactory.ConfigurationError("Provider for " + factoryId + " cannot be found", (Exception)null);
      } else {
         try {
            Object instance = factoryClass.newInstance();
            debugPrintln("created new instance of factory " + factoryId);
            return instance;
         } catch (Exception var5) {
            throw new ObjectFactory.ConfigurationError("Provider for factory " + factoryId + " could not be instantiated: " + var5, var5);
         }
      }
   }

   static Class lookUpFactoryClass(String factoryId) throws ObjectFactory.ConfigurationError {
      return lookUpFactoryClass(factoryId, (String)null, (String)null);
   }

   static Class lookUpFactoryClass(String factoryId, String propertiesFilename, String fallbackClassName) throws ObjectFactory.ConfigurationError {
      String factoryClassName = lookUpFactoryClassName(factoryId, propertiesFilename, fallbackClassName);
      ClassLoader cl = findClassLoader();
      if (factoryClassName == null) {
         factoryClassName = fallbackClassName;
      }

      try {
         Class providerClass = findProviderClass(factoryClassName, cl, true);
         debugPrintln("created new instance of " + providerClass + " using ClassLoader: " + cl);
         return providerClass;
      } catch (ClassNotFoundException var7) {
         throw new ObjectFactory.ConfigurationError("Provider " + factoryClassName + " not found", var7);
      } catch (Exception var8) {
         throw new ObjectFactory.ConfigurationError("Provider " + factoryClassName + " could not be instantiated: " + var8, var8);
      }
   }

   static String lookUpFactoryClassName(String factoryId, String propertiesFilename, String fallbackClassName) {
      SecuritySupport ss = SecuritySupport.getInstance();

      String factoryClassName;
      try {
         factoryClassName = ss.getSystemProperty(factoryId);
         if (factoryClassName != null) {
            debugPrintln("found system property, value=" + factoryClassName);
            return factoryClassName;
         }
      } catch (SecurityException var13) {
      }

      factoryClassName = null;
      if (propertiesFilename == null) {
         File propertiesFile = null;
         boolean propertiesFileExists = false;

         try {
            String javah = ss.getSystemProperty("java.home");
            propertiesFilename = javah + File.separator + "lib" + File.separator + "xalan.properties";
            propertiesFile = new File(propertiesFilename);
            propertiesFileExists = ss.getFileExists(propertiesFile);
         } catch (SecurityException var12) {
            fLastModified = -1L;
            fXalanProperties = null;
         }

         Class var18 = class$org$apache$xalan$xsltc$compiler$util$ObjectFactory == null ? (class$org$apache$xalan$xsltc$compiler$util$ObjectFactory = class$("org.apache.xalan.xsltc.compiler.util.ObjectFactory")) : class$org$apache$xalan$xsltc$compiler$util$ObjectFactory;
         synchronized(var18) {
            boolean loadProperties = false;

            try {
               if (fLastModified >= 0L) {
                  if (propertiesFileExists && fLastModified < (fLastModified = ss.getLastModified(propertiesFile))) {
                     loadProperties = true;
                  } else if (!propertiesFileExists) {
                     fLastModified = -1L;
                     fXalanProperties = null;
                  }
               } else if (propertiesFileExists) {
                  loadProperties = true;
                  fLastModified = ss.getLastModified(propertiesFile);
               }

               if (loadProperties) {
                  fXalanProperties = new Properties();
                  FileInputStream fis = ss.getFileInputStream(propertiesFile);
                  fXalanProperties.load(fis);
                  fis.close();
               }
            } catch (Exception var14) {
               fXalanProperties = null;
               fLastModified = -1L;
            }
         }

         if (fXalanProperties != null) {
            factoryClassName = fXalanProperties.getProperty(factoryId);
         }
      } else {
         try {
            FileInputStream fis = ss.getFileInputStream(new File(propertiesFilename));
            Properties props = new Properties();
            props.load(fis);
            fis.close();
            factoryClassName = props.getProperty(factoryId);
         } catch (Exception var11) {
         }
      }

      if (factoryClassName != null) {
         debugPrintln("found in " + propertiesFilename + ", value=" + factoryClassName);
         return factoryClassName;
      } else {
         return findJarServiceProviderName(factoryId);
      }
   }

   private static void debugPrintln(String msg) {
   }

   static ClassLoader findClassLoader() throws ObjectFactory.ConfigurationError {
      SecuritySupport ss = SecuritySupport.getInstance();
      ClassLoader context = ss.getContextClassLoader();
      ClassLoader system = ss.getSystemClassLoader();

      ClassLoader chain;
      for(chain = system; context != chain; chain = ss.getParentClassLoader(chain)) {
         if (chain == null) {
            return context;
         }
      }

      ClassLoader current = (class$org$apache$xalan$xsltc$compiler$util$ObjectFactory == null ? (class$org$apache$xalan$xsltc$compiler$util$ObjectFactory = class$("org.apache.xalan.xsltc.compiler.util.ObjectFactory")) : class$org$apache$xalan$xsltc$compiler$util$ObjectFactory).getClassLoader();

      for(chain = system; current != chain; chain = ss.getParentClassLoader(chain)) {
         if (chain == null) {
            return current;
         }
      }

      return system;
   }

   static Object newInstance(String className, ClassLoader cl, boolean doFallback) throws ObjectFactory.ConfigurationError {
      try {
         Class providerClass = findProviderClass(className, cl, doFallback);
         Object instance = providerClass.newInstance();
         debugPrintln("created new instance of " + providerClass + " using ClassLoader: " + cl);
         return instance;
      } catch (ClassNotFoundException var5) {
         throw new ObjectFactory.ConfigurationError("Provider " + className + " not found", var5);
      } catch (Exception var6) {
         throw new ObjectFactory.ConfigurationError("Provider " + className + " could not be instantiated: " + var6, var6);
      }
   }

   static Class findProviderClass(String className, ClassLoader cl, boolean doFallback) throws ClassNotFoundException, ObjectFactory.ConfigurationError {
      SecurityManager security = System.getSecurityManager();

      try {
         if (security != null) {
            security.checkPackageAccess(className);
         }
      } catch (SecurityException var7) {
         throw var7;
      }

      Class providerClass;
      if (cl == null) {
         providerClass = Class.forName(className);
      } else {
         try {
            providerClass = cl.loadClass(className);
         } catch (ClassNotFoundException var8) {
            if (!doFallback) {
               throw var8;
            }

            ClassLoader current = (class$org$apache$xalan$xsltc$compiler$util$ObjectFactory == null ? (class$org$apache$xalan$xsltc$compiler$util$ObjectFactory = class$("org.apache.xalan.xsltc.compiler.util.ObjectFactory")) : class$org$apache$xalan$xsltc$compiler$util$ObjectFactory).getClassLoader();
            if (current == null) {
               providerClass = Class.forName(className);
            } else {
               if (cl == current) {
                  throw var8;
               }

               providerClass = current.loadClass(className);
            }
         }
      }

      return providerClass;
   }

   private static String findJarServiceProviderName(String factoryId) {
      SecuritySupport ss = SecuritySupport.getInstance();
      String serviceId = "META-INF/services/" + factoryId;
      InputStream is = null;
      ClassLoader cl = findClassLoader();
      is = ss.getResourceAsStream(cl, serviceId);
      if (is == null) {
         ClassLoader current = (class$org$apache$xalan$xsltc$compiler$util$ObjectFactory == null ? (class$org$apache$xalan$xsltc$compiler$util$ObjectFactory = class$("org.apache.xalan.xsltc.compiler.util.ObjectFactory")) : class$org$apache$xalan$xsltc$compiler$util$ObjectFactory).getClassLoader();
         if (cl != current) {
            cl = current;
            is = ss.getResourceAsStream(current, serviceId);
         }
      }

      if (is == null) {
         return null;
      } else {
         debugPrintln("found jar resource=" + serviceId + " using ClassLoader: " + cl);

         BufferedReader rd;
         try {
            rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
         } catch (UnsupportedEncodingException var9) {
            rd = new BufferedReader(new InputStreamReader(is));
         }

         String factoryClassName = null;

         try {
            factoryClassName = rd.readLine();
            rd.close();
         } catch (IOException var8) {
            return null;
         }

         if (factoryClassName != null && !"".equals(factoryClassName)) {
            debugPrintln("found in resource, value=" + factoryClassName);
            return factoryClassName;
         } else {
            return null;
         }
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

   static class ConfigurationError extends Error {
      private Exception exception;

      ConfigurationError(String msg, Exception x) {
         super(msg);
         this.exception = x;
      }

      Exception getException() {
         return this.exception;
      }
   }
}
