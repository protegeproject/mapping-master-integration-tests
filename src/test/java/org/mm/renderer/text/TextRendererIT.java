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
import java.util.Optional;
import java.util.Set;

public class TextRendererIT extends IntegrationTestBase
{
	@Rule public final ExpectedException thrown = ExpectedException.none();

	@Test public void TestClassDeclaration()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Car";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestClassDeclarationWithAnnotations()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Car Annotations: hasAuthor Bob";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestClassDeclarationWithMultipleAnnotations()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Car Annotations: hasAuthor Bob, hasDate \"1990-10-10\"";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestSubClassOf()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Car SubClassOf: Vehicle";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestMultipleSubClassOf()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Car SubClassOf: Vehicle, Device";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestEquivalentToClass()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Car EquivalentTo: Automobile";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestEquivalentToClassExpression()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Car EquivalentTo: (hasEngine EXACTLY 1)";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestMultipleEquivalentClass()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Car EquivalentTo: Automobile, Auto, (hasEngine EXACTLY 1)";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestMaxCardinalityRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Car SubClassOf: (hasEngine MAX 1)";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestMinCardinalityRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Car SubClassOf: (hasSSN MIN 1)";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestExactCardinalityRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Car SubClassOf: (hasSSN EXACTLY 1)";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestObjectHasValueRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Catamaran SubClassOf: (hasHull VALUE 2)";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestDataHasValueRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: BMW SubClassOf: (hasOrigin VALUE \"Germany\")";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestObjectSomeValueRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: ChildOfDoctor SubClassOf: (hasParent SOME Physician)";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestDataSomeValueRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Car SubClassOf: (hasName SOME xsd:string)";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestObjectAllValuesFromRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Person SubClassOf: (hasSSN ONLY xsd:string)";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestDataAllValuesFromRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Person SubClassOf: (hasParent ONLY Human)";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	// TODO The grammar is incorrect here - parenthesis should not be required around the {}.
	@Test public void TestOWLObjectOneOf()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Person SubClassOf: (hasGender ONLY ({Male, Female, Other}))";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestNegatedClassExpression()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: Car EquivalentTo: NOT (hasEngine EXACTLY 2)";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestUnionClassExpression()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: A SubClassOf: ((hasP1 EXACTLY 2) OR (hasP2 EXACTLY 3))";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestIntersectionClassExpression()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: A SubClassOf: ((hasP1 EXACTLY 2) AND (hasP2 EXACTLY 3))";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expression, textRendering.get().getTextRendering());
	}

	@Test public void TestAbsoluteReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1";
		String expectedRendering = "Class: Car";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestLiteralReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @\"Car\"";
		String expectedRendering = "Class: Car";
		Optional<? extends TextRendering> textRendering = createTextRendering(expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestAbsoluteReferenceWithSheetName()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @'" + SHEET1 + "'!A1";
		String expectedRendering = "Class: Car";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		SpreadsheetLocation currentLocation = new SpreadsheetLocation(SHEET1, 1, 1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, currentLocation, expression);

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
		SpreadsheetLocation currentLocation = new SpreadsheetLocation(SHEET1, 1, 1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, currentLocation, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestRDFSLabelAppendInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(rdfs:label=mm:append(\"Big\"))";
		String expectedRendering = "Class: CarBig";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestShiftUpInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A4(mm:ShiftUp)";
		String expectedRendering = "Class: Car";
		Label cellA1 = createCell("Car", 1, 1);
		Label cellA2 = createCell("", 1, 2);
		Label cellA3 = createCell("", 1, 3);
		Label cellA4 = createCell("", 1, 4);
		Set<Label> cells = createCells(cellA1, cellA2, cellA3, cellA4);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestShiftDownInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(mm:ShiftDown)";
		String expectedRendering = "Class: Car";
		Label cellA1 = createCell("", 1, 1);
		Label cellA2 = createCell("", 1, 2);
		Label cellA3 = createCell("", 1, 3);
		Label cellA4 = createCell("Car", 1, 4);
		Set<Label> cells = createCells(cellA1, cellA2, cellA3, cellA4);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestShiftRightInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(mm:ShiftRight)";
		String expectedRendering = "Class: Car";
		Label cellA1 = createCell("", 1, 1);
		Label cellB1 = createCell("", 2, 1);
		Label cellC1 = createCell("", 3, 1);
		Label cellD1 = createCell("Car", 4, 1);
		Set<Label> cells = createCells(cellA1, cellB1, cellC1, cellD1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestShiftLeftInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @D1(mm:ShiftLeft)";
		String expectedRendering = "Class: Car";
		Label cellA1 = createCell("Car", 1, 1);
		Label cellB1 = createCell("", 2, 1);
		Label cellC1 = createCell("", 3, 1);
		Label cellD1 = createCell("", 4, 1);
		Set<Label> cells = createCells(cellA1, cellB1, cellC1, cellD1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestToLowerCaseInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(mm:toLowerCase(\"CAR\"))";
		String expectedRendering = "Class: car";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestTrimInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(mm:trim(\"  Car  \"))";
		String expectedRendering = "Class: Car";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestReverseInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(mm:reverse(\"raC\"))";
		String expectedRendering = "Class: Car";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestCapturingExpressionMethodInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(rdfs:label=mm:capturing(\":(\\S+)\"))";
		String expectedRendering = "Class: Zyvox";
		Label cellA1 = createCell("Pfizer:Zyvox", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestDefaultLocationValueInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(mm:DefaultLocationValue=\"Unknown\")";
		String expectedRendering = "Class: Unknown";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestDefaultLabelInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(rdfs:label mm:DefaultLabel=\"Unknown\")";
		String expectedRendering = "Class: Unknown";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

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
		createTextRendering(SHEET1, cells, expression);
	}

	@Test public void TestErrorIfEmptyLiteralDirectiveInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		this.thrown.expect(RendererException.class);
		this.thrown.expectMessage("empty literal in reference");

		String expression = "Individual: Fred Facts: hasName @A1(xsd:string mm:ErrorIfEmptyLiteral)";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		createTextRendering(SHEET1, cells, expression);
	}

	@Test public void TestErrorIfEmptyRDFSLabelDirectiveInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		this.thrown.expect(RendererException.class);
		this.thrown.expectMessage("empty RDFS label in reference");

		String expression = "Class: @A1(mm:ErrorIfEmptyLabel)";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		createTextRendering(SHEET1, cells, expression);
	}

	@Test public void TestErrorIfEmptyRDFIDDirectiveInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		this.thrown.expect(RendererException.class);
		this.thrown.expectMessage("empty RDF ID in reference");

		String expression = "Class: @A1(rdf:ID=@A1 mm:ErrorIfEmptyID)";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		createTextRendering(SHEET1, cells, expression);
	}

	@Test public void TestSkipIfEmptyLocationInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasName @A1(xsd:string mm:SkipIfEmptyLocation)";
		String expectedRendering = "Individual: Fred";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestSkipIfEmptyLiteralInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasName @A1(xsd:string mm:SkipIfEmptyLiteral)";
		String expectedRendering = "Individual: Fred";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestSkipIfEmptyRDFSLabelDirectiveInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(mm:SkipIfEmptyLabel)";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertFalse(textRendering.isPresent());
	}

	@Test public void TestSkipIfEmptyRDFIDDirectiveInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(rdf:ID mm:SkipIfEmptyID)";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertFalse(textRendering.isPresent());
	}

	@Test public void TestWarningIfEmptyLocationInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasName @A1(xsd:string mm:WarningIfEmptyLocation)";
		String expectedRendering = "Individual: Fred";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestWarningIfEmptyLiteralInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasName @A1(xsd:string mm:WarningIfEmptyLiteral)";
		String expectedRendering = "Individual: Fred";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestWarningIfEmptyRDFSLabelDirectiveInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(mm:WarningIfEmptyLabel)";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertFalse(textRendering.isPresent());
	}

	@Test public void TestWarningIfEmptyRDFIDDirectiveInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @A1(rdf:ID mm:WarningIfEmptyID)";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertFalse(textRendering.isPresent());
	}

	@Test public void TestProcessIfEmptyLiteralInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred Facts: hasName @A1(xsd:string mm:ProcessIfEmptyLiteral)";
		String expectedRendering = "Individual: Fred Facts: hasName \"\"";
		Label cellA1 = createCell("", 1, 1);
		Set<Label> cells = createCells(cellA1);
		Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression);

		Assert.assertTrue(textRendering.isPresent());
		Assert.assertEquals(expectedRendering, textRendering.get().getTextRendering());
	}

	@Test public void TestOutOfRangeColumnInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		this.thrown.expect(RendererException.class);
		this.thrown.expectMessage("invalid source specification @D1 - column D out of range");

		String expression = "Class: @D1";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		createTextRendering(SHEET1, cells, expression);
	}

	@Test public void TestOutOfRangeRowInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		this.thrown.expect(RendererException.class);
		this.thrown.expectMessage("invalid source specification @A3 - row 3 out of range");

		String expression = "Class: @A3";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		createTextRendering(SHEET1, cells, expression);
	}

	@Test public void TestInvalidSheetNameInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		this.thrown.expect(RendererException.class);
		this.thrown.expectMessage("invalid sheet name fff");

		String expression = "Class: @'fff'!A3";
		createTextRendering(SHEET1, expression);
	}

	@Test public void TestParseException()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		this.thrown.expect(ParseException.class);

		String expression = "Class: @";
		createTextRendering(expression);
	}
}
