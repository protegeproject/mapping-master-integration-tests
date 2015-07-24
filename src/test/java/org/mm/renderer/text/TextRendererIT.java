package org.mm.renderer.text;

import junit.framework.Assert;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WriteException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.ParseException;
import org.mm.renderer.RendererException;
import org.mm.rendering.text.TextRendering;
import org.mm.ss.SpreadsheetLocation;
import org.mm.test.IntegrationTestBase;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TextRendererIT extends IntegrationTestBase
{
	private static final String SHEET_NAME1 = "Sheet1";
	private SpreadsheetLocation currentLocation = new SpreadsheetLocation(SHEET_NAME1, 1, 1);
	private Set<Label> emptyCellSet = Collections.emptySet();

	@Rule public ExpectedException thrown = ExpectedException.none();

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
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestAbsoluteReferenceWithSheetName()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @'" + SHEET_NAME1 + "'!A1";
		String expectedRendering = "Class: Car";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
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
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
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
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestXSDBooleanReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasSSN @A1(xsd:boolean)";
		String expectedRendering = "Individual: Fred Facts: hasSSN true";
		Label cellA1 = createCell("true", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestXSDByteReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasSalary @A1(xsd:byte)";
		String expectedRendering = "Individual: Fred Facts: hasSalary 34";
		Label cellA1 = createCell("34", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestXSDShortReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasSalary @A1(xsd:short)";
		String expectedRendering = "Individual: Fred Facts: hasSalary 34";
		Label cellA1 = createCell("34", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestXSDIntReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasSalary @A1(xsd:int)";
		String expectedRendering = "Individual: Fred Facts: hasSalary 34";
		Label cellA1 = createCell("34", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestXSDFloatReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasSalary @A1(xsd:float)";
		String expectedRendering = "Individual: Fred Facts: hasSalary 34000.0";
		Label cellA1 = createCell("34000.0", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestReferenceRDFSLabelAssignment()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(rdfs:label=(\"Big\"))";
		String expectedRendering = "Class: Big";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestReferenceRDFSLabelAssignmentWithConcatenatedParameters()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(rdfs:label=(\"Big\", \"Car\"))";
		String expectedRendering = "Class: BigCar";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestReferenceRDFSLabelAssignmentWithReferenceParameter()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(rdfs:label=(\"Big\", @A1))";
		String expectedRendering = "Class: BigCar";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestOutOfRangeColumnReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		thrown.expect(RendererException.class);
		thrown.expectMessage("invalid source specification @D1 - column D out of range");

		String expression = "Class: @D1";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		createTextRendering(SHEET_NAME1, cells, currentLocation, expression);
	}

	@Test public void TestOutOfRangeRowReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		thrown.expect(RendererException.class);
		thrown.expectMessage("invalid source specification @A3 - row 3 out of range");

		String expression = "Class: @A3";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		createTextRendering(SHEET_NAME1, cells, currentLocation, expression);
	}

	@Test public void TestInvalidSheetNameReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		thrown.expect(RendererException.class);
		thrown.expectMessage("invalid sheet name fff");

		String expression = "Class: @'fff'!A3";
		createTextRendering(SHEET_NAME1, emptyCellSet, currentLocation, expression);
	}

	@Test public void TestParseException()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		thrown.expect(ParseException.class);

		String expression = "Class: @";
		createTextRendering(SHEET_NAME1, emptyCellSet, currentLocation, expression);
	}
}
