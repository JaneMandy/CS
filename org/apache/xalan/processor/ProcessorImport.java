package org.apache.xalan.processor;

class ProcessorImport extends ProcessorInclude {
   protected int getStylesheetType() {
      return 3;
   }

   protected String getStylesheetInclErr() {
      return "ER_IMPORTING_ITSELF";
   }
}
