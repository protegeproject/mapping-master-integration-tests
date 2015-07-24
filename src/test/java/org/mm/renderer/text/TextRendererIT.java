package org.mm.renderer.text;

import junit.framework.Assert;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WriteException;
import org.junit.Test;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.ParseException;
import org.mm.rendering.text.TextRendering;
import org.mm.ss.SpreadsheetLocation;
import org.mm.test.IntegrationTestBase;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public class TextRendererIT extends IntegrationTestBase
{
  private static final String SHEET_NAME1 = "Sheet1";
  private SpreadsheetLocation currentLocation = new SpreadsheetLocation(SHEET_NAME1, 1, 1);

  @Test public void TestAbsoluteReference()
    throws WriteException, BiffException, MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
      expression);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(textRendering.get().getTextRendering(), expectedRendering);
  }

	@Test public void TestColumnWildcardReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @*1";
		String expectedRendering = "Class: Car";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		SpreadsheetLocation currentLocation = new SpreadsheetLocation(SHEET_NAME1, 1, 1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(textRendering.get().getTextRendering(), expectedRendering);
	}

	@Test public void TestRowWildcardReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A*";
		String expectedRendering = "Class: Car";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		SpreadsheetLocation currentLocation = new SpreadsheetLocation(SHEET_NAME1, 1, 1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(textRendering.get().getTextRendering(), expectedRendering);
	}
}
