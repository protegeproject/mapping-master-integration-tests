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
import java.util.Optional;
import java.util.Set;

public class TextRendererIT extends IntegrationTestBase
{
	private static final String SHEET_NAME1 = "Sheet1";
	private static final Set<Label> emptyCellSet = Collections.emptySet();
	private final SpreadsheetLocation currentLocation = new SpreadsheetLocation(SHEET_NAME1, 1, 1);

	@Rule public final ExpectedException thrown = ExpectedException.none();

	@Test public void TestAbsoluteReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1";
		String expectedRendering = "Class: Car";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, this.currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestLiteralReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @\"Car\"";
		String expectedRendering = "Class: Car";
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, emptyCellSet,
				this.currentLocation, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, this.currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestColumnWildcardInReference()
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

	@Test public void TestRowWildcardInReference()
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

	@Test public void TestXSDBooleanInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasSSN @A1(xsd:boolean)";
		String expectedRendering = "Individual: Fred Facts: hasSSN true";
		Label cellA1 = createCell("true", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, this.currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestXSDByteInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasSalary @A1(xsd:byte)";
		String expectedRendering = "Individual: Fred Facts: hasSalary 34";
		Label cellA1 = createCell("34", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, this.currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestXSDShortInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasSalary @A1(xsd:short)";
		String expectedRendering = "Individual: Fred Facts: hasSalary 34";
		Label cellA1 = createCell("34", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, this.currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestXSDIntInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasSalary @A1(xsd:int)";
		String expectedRendering = "Individual: Fred Facts: hasSalary 34";
		Label cellA1 = createCell("34", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, this.currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestXSDFloatInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasSalary @A1(xsd:float)";
		String expectedRendering = "Individual: Fred Facts: hasSalary 34000.0";
		Label cellA1 = createCell("34000.0", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, this.currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestXSDStringInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasName @A1(xsd:string)";
		String expectedRendering = "Individual: Fred Facts: hasName \"Fred\"";
		Label cellA1 = createCell("Fred", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, this.currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestXSDDateInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasDOB @A1(xsd:date)";
		String expectedRendering = "Individual: Fred Facts: hasDOB \"1999-01-01\"";
		Label cellA1 = createCell("1999-01-01", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, this.currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestXSDDateTimeInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasDOB @A1(xsd:dateTime)";
		String expectedRendering = "Individual: Fred Facts: hasDOB \"1999-01-01T10:10:10\"";
		Label cellA1 = createCell("1999-01-01T10:10:10", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, this.currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestXSDTimeInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasBedTime @A1(xsd:time)";
		String expectedRendering = "Individual: Fred Facts: hasBedTime \"21:00\"";
		Label cellA1 = createCell("21:00", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, this.currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestRDFSLabelAssignmentInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(rdfs:label=(\"Big\"))";
		String expectedRendering = "Class: Big";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, this.currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestRDFSLabelAssignmentWithConcatenatedParametersInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(rdfs:label=(\"Big\", \"Car\"))";
		String expectedRendering = "Class: BigCar";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, this.currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestRDFSLabelAssignmentWithReferenceParameterInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(rdfs:label=(\"Big\", @A1))";
		String expectedRendering = "Class: BigCar";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, this.currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestRDFSLabelPrependInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(rdfs:label=mm:prepend(\"Big\"))";
		String expectedRendering = "Class: BigCar";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestDefaultPrependInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(mm:prepend(\"Big\"))";
		String expectedRendering = "Class: BigCar";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestToUpperCaseLiteralInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(mm:toUpperCase(\"car\"))";
		String expectedRendering = "Class: CAR";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestToUpperCaseInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(mm:toUpperCase(@A1))";
		String expectedRendering = "Class: CAR";
		Label cellA1 = createCell("car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestToUpperCaseImplicitInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(mm:toUpperCase)";
		String expectedRendering = "Class: CAR";
		Label cellA1 = createCell("car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestReplaceAllInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasItem @A1(mm:replaceAll(\"[^a-zA-Z0-9]\",\"\"))";
		String expectedRendering = "Individual: Fred Facts: hasItem \"bag\"";
		Label cellA1 = createCell(")(*bag%^&$#@!", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestCapturingExpressionInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(rdfs:label=[\":(\\S+)\"])";
		String expectedRendering = "Class: Zyvox";
		Label cellA1 = createCell("Pfizer:Zyvox", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET_NAME1, cells, currentLocation,
				expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestErrorIfEmptyLocationDirectiveInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		this.thrown.expect(RendererException.class);
		this.thrown.expectMessage("empty location");

		String expression = "Class: @A1(mm:ErrorIfEmptyLocation)";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		createTextRendering(SHEET_NAME1, cells, this.currentLocation, expression);
	}

	@Test public void TestErrorIfEmptyRDFSLabelDirectiveInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		this.thrown.expect(RendererException.class);
		this.thrown.expectMessage("empty RDFS label in reference");

		String expression = "Class: @A1(mm:ErrorIfEmptyLabel)";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		createTextRendering(SHEET_NAME1, cells, this.currentLocation, expression);
	}

	@Test public void TestErrorIfEmptyRDFIDDirectiveInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		this.thrown.expect(RendererException.class);
		this.thrown.expectMessage("empty RDF ID in reference");

		String expression = "Class: @A1(rdf:ID=@A1 mm:ErrorIfEmptyID)";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		createTextRendering(SHEET_NAME1, cells, this.currentLocation, expression);
	}

	@Test public void TestOutOfRangeColumnInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		this.thrown.expect(RendererException.class);
		this.thrown.expectMessage("invalid source specification @D1 - column D out of range");

		String expression = "Class: @D1";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		createTextRendering(SHEET_NAME1, cells, this.currentLocation, expression);
	}

	@Test public void TestOutOfRangeRowInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		this.thrown.expect(RendererException.class);
		this.thrown.expectMessage("invalid source specification @A3 - row 3 out of range");

		String expression = "Class: @A3";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		createTextRendering(SHEET_NAME1, cells, this.currentLocation, expression);
	}

	@Test public void TestInvalidSheetNameInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		this.thrown.expect(RendererException.class);
		this.thrown.expectMessage("invalid sheet name fff");

		String expression = "Class: @'fff'!A3";
		createTextRendering(SHEET_NAME1, emptyCellSet, this.currentLocation, expression);
	}

	@Test public void TestParseException()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		this.thrown.expect(ParseException.class);

		String expression = "Class: @";
		createTextRendering(SHEET_NAME1, emptyCellSet, this.currentLocation, expression);
	}
}
