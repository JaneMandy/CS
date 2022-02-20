package aggressor;

import common.AObject;
import common.Accent;
import common.CommonUtils;
import common.HasUUID;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ui.ATable;
import ui.GenericTableModel;

public class TranscriptEditor extends AObject {
   protected Map accents = new HashMap();
   protected Set removed = new HashSet();
   protected GenericTableModel model = null;
   protected ATable table = null;

   private static final boolean A(Accent var0) {
      return "remove".equals(var0.getValue());
   }

   public boolean isRemoved(HasUUID var1) {
      String var2 = var1.ID();
      synchronized(this) {
         return this.removed.contains(var2);
      }
   }

   public Map decorate(HasUUID var1, Map var2) {
      String var3 = var1.ID();
      synchronized(this) {
         if (!this.accents.containsKey(var3)) {
            return var2;
         } else {
            var2.put("_accent", this.accents.get(var3));
            return var2;
         }
      }
   }

   public boolean processTranscriptFormat(String var1, Object var2) {
      if (!"accents".equals(var1)) {
         return false;
      } else {
         Accent var3 = (Accent)var2;
         synchronized(this) {
            if (A(var3)) {
               this.removed.add(var3.getKey());
            } else {
               this.accents.put(var3.getKey(), var3.getValue());
            }

            return true;
         }
      }
   }

   protected void remove(final String var1) {
      if (this.table != null && this.model != null) {
         CommonUtils.runSafe(new Runnable() {
            public void run() {
               TranscriptEditor.this.table.markSelections();
               if (TranscriptEditor.this.model.removeRowWithValueAtColumn("id", var1)) {
                  TranscriptEditor.this.model.fireListeners();
                  TranscriptEditor.this.table.restoreSelections();
               }

            }
         });
      }

   }

   protected void setAccent(final String var1, final String var2) {
      if (this.table != null && this.model != null) {
         CommonUtils.runSafe(new Runnable() {
            public void run() {
               TranscriptEditor.this.table.markSelections();
               if (TranscriptEditor.this.model.setValueForKeyAtColumn("id", var1, "_accent", var2)) {
                  TranscriptEditor.this.model.fireListeners();
                  TranscriptEditor.this.table.restoreSelections();
               }

            }
         });
      }

   }

   public void setTable(ATable var1, GenericTableModel var2) {
      this.table = var1;
      this.model = var2;
   }

   public boolean processTranscriptResult(String var1, Object var2) {
      if (!"accents".equals(var1)) {
         return false;
      } else {
         this.processTranscriptFormat(var1, var2);
         Accent var3 = (Accent)var2;
         if (A(var3)) {
            this.remove(var3.getKey());
         } else {
            this.setAccent(var3.getKey(), var3.getValue());
         }

         return true;
      }
   }
}
