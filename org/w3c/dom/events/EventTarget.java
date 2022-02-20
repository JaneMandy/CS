package org.w3c.dom.events;

import org.w3c.dom.DOMException;

public interface EventTarget {
   void addEventListener(String var1, EventListener var2, boolean var3);

   void removeEventListener(String var1, EventListener var2, boolean var3);

   boolean dispatchEvent(Event var1) throws EventException, DOMException;

   void addEventListenerNS(String var1, String var2, EventListener var3, boolean var4, Object var5);

   void removeEventListenerNS(String var1, String var2, EventListener var3, boolean var4);
}
