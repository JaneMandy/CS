package org.apache.avalon.framework;

import java.util.Map;

public abstract class ValuedEnum extends Enum {
   private final int m_value;

   protected ValuedEnum(String name, int value) {
      this(name, value, (Map)null);
   }

   protected ValuedEnum(String name, int value, Map map) {
      super(name, map);
      this.m_value = value;
   }

   public final int getValue() {
      return this.m_value;
   }

   public final boolean isEqualTo(ValuedEnum other) {
      return this.m_value == other.m_value;
   }

   public final boolean isGreaterThan(ValuedEnum other) {
      return this.m_value > other.m_value;
   }

   public final boolean isGreaterThanOrEqual(ValuedEnum other) {
      return this.m_value >= other.m_value;
   }

   public final boolean isLessThan(ValuedEnum other) {
      return this.m_value < other.m_value;
   }

   public final boolean isLessThanOrEqual(ValuedEnum other) {
      return this.m_value <= other.m_value;
   }

   public boolean equals(Object o) {
      boolean prelim = super.equals(o);
      if (!prelim) {
         return false;
      } else if (!(o instanceof ValuedEnum)) {
         return false;
      } else {
         ValuedEnum enumerated = (ValuedEnum)o;
         return this.m_value == enumerated.m_value;
      }
   }

   public int hashCode() {
      int hash = super.hashCode();
      hash ^= this.m_value;
      hash >>>= this.m_value & 31;
      return hash;
   }

   public String toString() {
      return this.getClass().getName() + "[" + this.getName() + "=" + this.m_value + "]";
   }
}
