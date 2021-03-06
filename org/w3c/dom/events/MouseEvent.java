package org.w3c.dom.events;

import org.w3c.dom.views.AbstractView;

public interface MouseEvent extends UIEvent {
   int getScreenX();

   int getScreenY();

   int getClientX();

   int getClientY();

   boolean getCtrlKey();

   boolean getShiftKey();

   boolean getAltKey();

   boolean getMetaKey();

   short getButton();

   EventTarget getRelatedTarget();

   void initMouseEvent(String var1, boolean var2, boolean var3, AbstractView var4, int var5, int var6, int var7, int var8, int var9, boolean var10, boolean var11, boolean var12, boolean var13, short var14, EventTarget var15);

   boolean getModifierState(String var1);

   void initMouseEventNS(String var1, String var2, boolean var3, boolean var4, AbstractView var5, int var6, int var7, int var8, int var9, int var10, short var11, EventTarget var12, String var13);
}
