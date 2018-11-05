package org.folio.oaipmh.mappers;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XSLTMapperTest {

  private static final Logger logger = Logger.getLogger(XSLTMapperTest.class);

  private static final String SCHEMA_FILE_PATH = "ramls/schemas/oai_dc.xsd";
  private static final String INCORRECT_STYLESHEET_XSL = "incorrectStylesheet.xsl";
  private static final String XSLT_MARC21SLIM2_OAIDC_XSL = "xslt/MARC21slim2OAIDC.xsl";

  @Test
  void correctJsonConvertingValidationTest() throws IOException {
    logger.info("=== Test correct json file converting ===");
    String input = MapperTestHelper.getStringFromFile(StaticTestRecords
      .RESOURCES_CORRECT_JSON_MARC);
    byte[] result = new XSLTMapper(XSLT_MARC21SLIM2_OAIDC_XSL).convert(input);
    assertThat(MapperTestHelper.validateDocumentAgainstSchema(result, SCHEMA_FILE_PATH), is(true));
  }

  @Test
  void incorrectJsonConvertingToDCValidationTest() throws IOException {
    int titlePosition = 320;
    int emptyTitleLength = 11;
    String expextedTitle = "<dc:title/>";
    logger.info("=== Test incorrect json file converting to Dublin Core ===");
    String input = MapperTestHelper.getStringFromFile(StaticTestRecords.RESOURCES_INCORRECT_JSON_MARC);
    byte[] result = new XSLTMapper(XSLT_MARC21SLIM2_OAIDC_XSL).convert(input);
    byte[] titleSubArray = new byte[emptyTitleLength];
    System.arraycopy(result, titlePosition, titleSubArray, 0, emptyTitleLength);
    String title = new String(titleSubArray);
    assertThat(title, equalTo(expextedTitle));
  }

  @Test
  void incorrectSchemaConvertingTest() throws IOException {
    logger.info("=== Test incorrect schema file converting ===");
    String input = MapperTestHelper.getStringFromFile(StaticTestRecords.RESOURCES_INCORRECT_JSON_MARC);
    assertThrows(IllegalStateException.class, () -> new XSLTMapper(INCORRECT_STYLESHEET_XSL).convert(input));
  }
}
