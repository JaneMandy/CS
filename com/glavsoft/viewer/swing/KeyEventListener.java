package com.glavsoft.viewer.swing;

import com.glavsoft.rfb.client.KeyEventMessage;
import com.glavsoft.rfb.protocol.ProtocolContext;
import com.glavsoft.utils.Keymap;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEventListener implements KeyListener {
   private ModifierButtonEventListener modifierButtonListener;
   private boolean convertToAscii;
   private final ProtocolContext context;
   private KeyboardConvertor convertor;

   public KeyEventListener(ProtocolContext context) {
      this.context = context;
      this.convertToAscii = false;
   }

   private void processKeyEvent(KeyEvent e) {
      if (!this.processModifierKeys(e) && !this.processSpecialKeys(e) && !this.processActionKey(e)) {
         int keyChar = e.getKeyChar();
         int location = e.getKeyLocation();
         if (65535 == keyChar) {
            keyChar = this.convertToAscii ? this.convertor.convert(keyChar, e) : 0;
         }

         if (keyChar < 32) {
            if (e.isControlDown()) {
               keyChar += 96;
            } else {
               switch(keyChar) {
               case 8:
                  keyChar = 65288;
                  break;
               case 9:
                  keyChar = 65289;
                  break;
               case 10:
                  keyChar = 4 == location ? 'ﾍ' : '－';
                  break;
               case 27:
                  keyChar = 65307;
               }
            }
         } else if (127 == keyChar) {
            keyChar = 65535;
         } else if (this.convertToAscii) {
            keyChar = this.convertor.convert(keyChar, e);
         } else {
            keyChar = Keymap.unicode2keysym(keyChar);
         }

         this.sendKeyEvent(keyChar, e);
      }

   }

   private boolean processSpecialKeys(KeyEvent e) {
      int nkeyCode = e.getKeyCode();
      if (65406 == nkeyCode) {
         this.sendKeyEvent(65507, e);
         this.sendKeyEvent(65513, e);
         return true;
      } else {
         char var3;
         switch(nkeyCode) {
         case 96:
            var3 = 'ﾰ';
            break;
         case 97:
            var3 = 'ﾱ';
            break;
         case 98:
            var3 = 'ﾲ';
            break;
         case 99:
            var3 = 'ﾳ';
            break;
         case 100:
            var3 = 'ﾴ';
            break;
         case 101:
            var3 = 'ﾵ';
            break;
         case 102:
            var3 = 'ﾶ';
            break;
         case 103:
            var3 = 'ﾷ';
            break;
         case 104:
            var3 = 'ﾸ';
            break;
         case 105:
            var3 = 'ﾹ';
            break;
         case 106:
            var3 = 'ﾪ';
            break;
         case 107:
            var3 = 'ﾫ';
            break;
         case 108:
            var3 = 'ﾬ';
            break;
         case 109:
            var3 = 'ﾭ';
            break;
         case 110:
            var3 = 'ﾮ';
            break;
         case 111:
            var3 = 'ﾯ';
            break;
         default:
            return false;
         }

         this.sendKeyEvent(nkeyCode, e);
         return true;
      }
   }

   private boolean processActionKey(KeyEvent e) {
      int keyCode = e.getKeyCode();
      int location = e.getKeyLocation();
      if (e.isActionKey()) {
         switch(keyCode) {
         case 33:
            keyCode = 4 == location ? 'ﾚ' : 'ｕ';
            break;
         case 34:
            keyCode = 4 == location ? 'ﾛ' : 'ｖ';
            break;
         case 35:
            keyCode = 4 == location ? 'ﾜ' : 'ｗ';
            break;
         case 36:
            keyCode = 4 == location ? 'ﾕ' : 'ｐ';
            break;
         case 37:
            keyCode = 4 == location ? 'ﾖ' : 'ｑ';
            break;
         case 38:
            keyCode = 4 == location ? 'ﾗ' : 'ｒ';
            break;
         case 39:
            keyCode = 4 == location ? 'ﾘ' : 'ｓ';
            break;
         case 40:
            keyCode = 4 == location ? 'ﾙ' : 'ｔ';
            break;
         case 112:
            keyCode = 65470;
            break;
         case 113:
            keyCode = 65471;
            break;
         case 114:
            keyCode = 65472;
            break;
         case 115:
            keyCode = 65473;
            break;
         case 116:
            keyCode = 65474;
            break;
         case 117:
            keyCode = 65475;
            break;
         case 118:
            keyCode = 65476;
            break;
         case 119:
            keyCode = 65477;
            break;
         case 120:
            keyCode = 65478;
            break;
         case 121:
            keyCode = 65479;
            break;
         case 122:
            keyCode = 65480;
            break;
         case 123:
            keyCode = 65481;
            break;
         case 155:
            keyCode = 4 == location ? 'ﾞ' : '｣';
            break;
         case 224:
            keyCode = 65431;
            break;
         case 225:
            keyCode = 65433;
            break;
         case 226:
            keyCode = 65430;
            break;
         case 227:
            keyCode = 65432;
            break;
         default:
            return false;
         }

         this.sendKeyEvent(keyCode, e);
         return true;
      } else {
         return false;
      }
   }

   private boolean processModifierKeys(KeyEvent e) {
      int nkeyCode = e.getKeyCode();
      char keyCode;
      switch(nkeyCode) {
      case 16:
         keyCode = '￡';
         break;
      case 17:
         keyCode = '￣';
         break;
      case 18:
         keyCode = '￩';
         break;
      case 157:
         keyCode = '\uffe7';
         break;
      case 524:
         keyCode = '￫';
         break;
      case 525:
         keyCode = '￭';
         break;
      default:
         return false;
      }

      if (this.modifierButtonListener != null) {
         this.modifierButtonListener.fireEvent(e);
      }

      this.sendKeyEvent(keyCode + (e.getKeyLocation() == 3 ? 1 : 0), e);
      return true;
   }

   private void sendKeyEvent(int keyChar, KeyEvent e) {
      this.context.sendMessage(new KeyEventMessage(keyChar, e.getID() == 401));
   }

   public void keyTyped(KeyEvent e) {
      e.consume();
   }

   public void keyPressed(KeyEvent e) {
      this.processKeyEvent(e);
      e.consume();
   }

   public void keyReleased(KeyEvent e) {
      this.processKeyEvent(e);
      e.consume();
   }

   public void addModifierListener(ModifierButtonEventListener modifierButtonListener) {
      this.modifierButtonListener = modifierButtonListener;
   }

   public void setConvertToAscii(boolean convertToAscii) {
      this.convertToAscii = convertToAscii;
      if (convertToAscii && null == this.convertor) {
         this.convertor = new KeyboardConvertor();
      }

   }
}
