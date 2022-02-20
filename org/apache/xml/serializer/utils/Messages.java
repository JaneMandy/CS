package org.apache.xml.serializer.utils;

import java.text.MessageFormat;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Messages {
   private final Locale m_locale = Locale.getDefault();
   private ListResourceBundle m_resourceBundle;
   private String m_resourceBundleName;

   Messages(String resourceBundle) {
      this.m_resourceBundleName = resourceBundle;
   }

   private Locale getLocale() {
      return this.m_locale;
   }

   private ListResourceBundle getResourceBundle() {
      return this.m_resourceBundle;
   }

   public final String createMessage(String msgKey, Object[] args) {
      if (this.m_resourceBundle == null) {
         this.m_resourceBundle = this.loadResourceBundle(this.m_resourceBundleName);
      }

      return this.m_resourceBundle != null ? this.createMsg(this.m_resourceBundle, msgKey, args) : "Could not load the resource bundles: " + this.m_resourceBundleName;
   }

   private final String createMsg(ListResourceBundle fResourceBundle, String msgKey, Object[] args) {
      String fmsg = null;
      boolean throwex = false;
      String msg = null;
      if (msgKey != null) {
         msg = fResourceBundle.getString(msgKey);
      } else {
         msgKey = "";
      }

      if (msg == null) {
         throwex = true;

         try {
            msg = MessageFormat.format("BAD_MSGKEY", msgKey, this.m_resourceBundleName);
         } catch (Exception var10) {
            msg = "The message key '" + msgKey + "' is not in the message class '" + this.m_resourceBundleName + "'";
         }
      } else if (args != null) {
         try {
            int n = args.length;

            for(int i = 0; i < n; ++i) {
               if (null == args[i]) {
                  args[i] = "";
               }
            }

            fmsg = MessageFormat.format(msg, args);
         } catch (Exception var11) {
            throwex = true;

            try {
               fmsg = MessageFormat.format("BAD_MSGFORMAT", msgKey, this.m_resourceBundleName);
               fmsg = fmsg + " " + msg;
            } catch (Exception var9) {
               fmsg = "The format of message '" + msgKey + "' in message class '" + this.m_resourceBundleName + "' failed.";
            }
         }
      } else {
         fmsg = msg;
      }

      if (throwex) {
         throw new RuntimeException(fmsg);
      } else {
         return fmsg;
      }
   }

   private ListResourceBundle loadResourceBundle(String resourceBundle) throws MissingResourceException {
      this.m_resourceBundleName = resourceBundle;
      Locale locale = this.getLocale();

      ListResourceBundle lrb;
      try {
         ResourceBundle rb = ResourceBundle.getBundle(this.m_resourceBundleName, locale);
         lrb = (ListResourceBundle)rb;
      } catch (MissingResourceException var7) {
         try {
            lrb = (ListResourceBundle)ResourceBundle.getBundle(this.m_resourceBundleName, new Locale("en", "US"));
         } catch (MissingResourceException var6) {
            throw new MissingResourceException("Could not load any resource bundles." + this.m_resourceBundleName, this.m_resourceBundleName, "");
         }
      }

      this.m_resourceBundle = lrb;
      return lrb;
   }

   private static String getResourceSuffix(Locale locale) {
      String suffix = "_" + locale.getLanguage();
      String country = locale.getCountry();
      if (country.equals("TW")) {
         suffix = suffix + "_" + country;
      }

      return suffix;
   }
}
