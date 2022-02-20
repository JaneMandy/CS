package cloudstrike;

public class CSUtils {
   public static boolean matchesSimpleGeneric(String value, String pattern) {
      if (pattern != null && pattern.length() != 0) {
         if (!"*".equals(pattern) && !"**".equals(pattern)) {
            if (value == null) {
               return false;
            } else {
               String suffix;
               if (pattern.startsWith("*") && pattern.endsWith("*")) {
                  suffix = pattern.substring(1);
                  suffix = suffix.substring(0, suffix.length() - 1);
                  return value.contains(suffix);
               } else if (pattern.endsWith("*")) {
                  suffix = pattern.substring(0, pattern.length() - 1);
                  return value.startsWith(suffix);
               } else if (pattern.startsWith("*")) {
                  suffix = pattern.substring(1);
                  return value.endsWith(suffix);
               } else {
                  return value.equals(pattern);
               }
            }
         } else {
            return true;
         }
      } else {
         return value == null || value.length() == 0;
      }
   }
}
