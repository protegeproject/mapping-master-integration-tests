package org.mm.renderer.text;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mm.core.settings.ReferenceSettings;
import org.mm.core.settings.ValueEncodingSetting;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.ParseException;
import org.mm.renderer.IntegrationTestBase;
import org.mm.renderer.RendererException;
import org.mm.rendering.text.TextRendering;
import org.mm.ss.SpreadsheetLocation;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public class TextRendererIT extends IntegrationTestBase
{
  private ReferenceSettings settings;

  @Rule public final ExpectedException thrown = ExpectedException.none();

  @Before public void setUp() throws OWLOntologyCreationException
  {
    settings = new ReferenceSettings();
    settings.setValueEncodingSetting(ValueEncodingSetting.RDFS_LABEL);
  }

  @Test public void TestClassDeclaration() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Car";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestSubClassOf() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Car SubClassOf: Vehicle";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestMultipleSubClassOf() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Car SubClassOf: Vehicle, Device";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestEquivalentToClass() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Car EquivalentTo: Automobile";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestEquivalentToClassExpression() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Car EquivalentTo: (hasEngine EXACTLY 1)";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestMultipleEquivalentClass() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Car EquivalentTo: Automobile, Auto, (hasEngine EXACTLY 1)";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestClassDeclarationWithAnnotations() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Car Annotations: hasAuthor Bob";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestClassDeclarationWithMultipleAnnotations()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Car Annotations: hasAuthor Bob, hasDate \"1990-10-10\"";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestMaxCardinalityRestriction() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Car SubClassOf: (hasEngine MAX 1)";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestMinCardinalityRestriction() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Car SubClassOf: (hasSSN MIN 1)";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestExactCardinalityRestriction() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Car SubClassOf: (hasSSN EXACTLY 1)";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestObjectHasValueRestriction() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Catamaran SubClassOf: (hasHull VALUE 2)"; // XXX: Should be Individual
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestDataHasValueRestriction() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: BMW SubClassOf: (hasOrigin VALUE \"Germany\")";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestObjectSomeValueRestriction() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: ChildOfDoctor SubClassOf: (hasParent SOME Physician)";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestDataSomeValueRestriction() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Car SubClassOf: (hasName SOME xsd:string)";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestObjectAllValuesFromRestriction() // XXX: Minor mistake on naming method
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Person SubClassOf: (hasSSN ONLY xsd:string)";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestDataAllValuesFromRestriction() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Person SubClassOf: (hasParent ONLY Human)";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  // TODO The grammar is incorrect here - parenthesis should not be required around the {}.
  @Test public void TestOWLObjectOneOf() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Person SubClassOf: (hasGender ONLY {Male, Female, Other})";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestNegatedClassExpression() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: Car EquivalentTo: NOT (hasEngine EXACTLY 2)";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestUnionClassExpression() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: A SubClassOf: ((hasP1 EXACTLY 2) OR (hasP2 EXACTLY 3))";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestIntersectionClassExpression() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: A SubClassOf: ((hasP1 EXACTLY 2) AND (hasP2 EXACTLY 3))";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestIndividualDeclaration() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestIndividualDeclarationWithTypes() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Types: Person";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestIndividualDeclarationWithMultipleTypes()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Types: Person, (hasParent ONLY Human)";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestIndividualDeclarationWithFacts() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasName \"Fred\"";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestIndividualDeclarationWithMultipleFacts()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasName \"Fred\", hasAge 23";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestIndividualDeclarationWithAnnotations()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Annotations: hasName \"Fred\"";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestIndividualDeclarationWithMultipleAnnotations()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Annotations: hasName \"Fred\", hasAge 23";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestIndividualDeclarationWithSameIndividual()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred SameAs: Freddy";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestIndividualDeclarationWithMultipleSameIndividual()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred SameAs: Freddy, F";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestIndividualDeclarationWithDifferentIndividuals()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred DifferentFrom: Bob";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestIndividualDeclarationWithMultipleDifferentIndividuals()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred DifferentFrom: Bob,  Bobby";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expression, clean(textRendering.get().getRendering()));
  }

  @Test public void TestAbsoluteClassReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestAbsoluteIndividualReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: @A1";
    String expectedRendering = "Individual: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestAbsoluteObjectPropertyReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: @A1 Bob";
    String expectedRendering = "Individual: Fred Facts: hasUncle Bob";
    Label cellA1 = createCell("hasUncle", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestAbsoluteDataPropertyReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: @A1 23";
    String expectedRendering = "Individual: Fred Facts: hasAge 23";
    Label cellA1 = createCell("hasAge", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestAbsoluteAnnotationPropertyReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Annotations: @A1 23";
    String expectedRendering = "Individual: Fred Annotations: hasAge 23";
    Label cellA1 = createCell("hasAge", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestLiteralReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @\"Car\"";
    String expectedRendering = "Class: Car";
    Optional<? extends TextRendering> textRendering = createTextRendering(expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestAbsoluteReferenceWithSheetName() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @'" + SHEET1 + "'!A1";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestColumnWildcardInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @*1";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    SpreadsheetLocation currentLocation = new SpreadsheetLocation(SHEET1, 1, 1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, currentLocation, expression,
        settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestRowWildcardInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A*";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    SpreadsheetLocation currentLocation = new SpreadsheetLocation(SHEET1, 1, 1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, currentLocation, expression,
        settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestClassQualifiedInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(Class)";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestIndividualInQualifiedReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: @A1(mm:Location)";
    String expectedRendering = "Individual: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    //    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestObjectPropertyInQualifiedReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: @A1(ObjectProperty) Bob";
    String expectedRendering = "Individual: Fred Facts: hasUncle Bob";
    Label cellA1 = createCell("hasUncle", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestDataPropertyQualifiedInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: @A1(DataProperty) 23";
    String expectedRendering = "Individual: Fred Facts: hasAge 23";
    Label cellA1 = createCell("hasAge", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestAnnotationPropertyQualifiedReference()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Annotations: @A1(AnnotationProperty) 23";
    String expectedRendering = "Individual: Fred Annotations: hasAge 23";
    Label cellA1 = createCell("hasAge", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestXSDBooleanInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasSSN @A1(xsd:boolean)";
    String expectedRendering = "Individual: Fred Facts: hasSSN true";
    Label cellA1 = createCell("true", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestXSDByteInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasSalary @A1(xsd:byte)";
    String expectedRendering = "Individual: Fred Facts: hasSalary 34";
    Label cellA1 = createCell("34", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestXSDShortInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasSalary @A1(xsd:short)";
    String expectedRendering = "Individual: Fred Facts: hasSalary 34";
    Label cellA1 = createCell("34", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestXSDIntInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasSalary @A1(xsd:integer)";
    String expectedRendering = "Individual: Fred Facts: hasSalary 34";
    Label cellA1 = createCell("34", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestXSDFloatInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasSalary @A1(xsd:float)";
    String expectedRendering = "Individual: Fred Facts: hasSalary 34000.0";
    Label cellA1 = createCell("34000.0", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestXSDStringInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasName @A1(xsd:string)";
    String expectedRendering = "Individual: Fred Facts: hasName \"Fred\"";
    Label cellA1 = createCell("Fred", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestXSDDateInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasDOB @A1(xsd:date)";
    String expectedRendering = "Individual: Fred Facts: hasDOB \"1999-01-01\"";
    Label cellA1 = createCell("1999-01-01", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestXSDDateTimeInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasDOB @A1(xsd:dateTime)";
    String expectedRendering = "Individual: Fred Facts: hasDOB \"1999-01-01T10:10:10\"";
    Label cellA1 = createCell("1999-01-01T10:10:10", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestXSDTimeInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasBedTime @A1(xsd:time)";
    String expectedRendering = "Individual: Fred Facts: hasBedTime \"21:00\"";
    Label cellA1 = createCell("21:00", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestRDFSLabelAssignmentInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(rdfs:label=(\"Big\"))";
    String expectedRendering = "Class: Big";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestRDFSLabelAssignmentWithConcatenatedParametersInReference()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(rdfs:label=(\"Big\", \"Car\"))";
    String expectedRendering = "Class: BigCar";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestRDFSLabelAssignmentWithReferenceParameterInReference()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(rdfs:label=(\"Big\", @A1))";
    String expectedRendering = "Class: BigCar";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestRDFSLabelAppendInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(rdfs:label=mm:append(\"Big\"))";
    String expectedRendering = "Class: CarBig";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestRDFSLabelPrependInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(rdfs:label=mm:prepend(\"Big\"))";
    String expectedRendering = "Class: BigCar";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestDefaultPrependInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:prepend(\"Big\"))";
    String expectedRendering = "Class: BigCar";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestShiftUpInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A4(mm:Location)";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Label cellA2 = createCell("", 1, 2);
    Label cellA3 = createCell("", 1, 3);
    Label cellA4 = createCell("", 1, 4);
    Set<Label> cells = createCells(cellA1, cellA2, cellA3, cellA4);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    //    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestShiftDownInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:ShiftDown)";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("", 1, 1);
    Label cellA2 = createCell("", 1, 2);
    Label cellA3 = createCell("", 1, 3);
    Label cellA4 = createCell("Car", 1, 4);
    Set<Label> cells = createCells(cellA1, cellA2, cellA3, cellA4);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestReferencesInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A*(mm:append(@B*(mm:ShiftDown), @C*(mm:ShiftRight)))";
    String expectedRendering = "Class: CarBMWGermany";
    Label cellA1 = createCell("Car", 1, 1);
    Label cellB1 = createCell("", 2, 1);
    Label cellB2 = createCell("", 2, 2);
    Label cellB3 = createCell("", 2, 3);
    Label cellB4 = createCell("BMW", 2, 4);
    Label cellC1 = createCell("", 3, 1);
    Label cellD1 = createCell("", 4, 1);
    Label cellE1 = createCell("", 5, 1);
    Label cellF1 = createCell("Germany", 6, 1);
    Set<Label> cells = createCells(cellA1, cellB1, cellB2, cellB3, cellB4, cellC1, cellD1, cellE1, cellF1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestShiftRightInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:ShiftRight)";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("", 1, 1);
    Label cellB1 = createCell("", 2, 1);
    Label cellC1 = createCell("", 3, 1);
    Label cellD1 = createCell("Car", 4, 1);
    Set<Label> cells = createCells(cellA1, cellB1, cellC1, cellD1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestShiftLeftInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @D1(mm:ShiftLeft)";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Label cellB1 = createCell("", 2, 1);
    Label cellC1 = createCell("", 3, 1);
    Label cellD1 = createCell("", 4, 1);
    Set<Label> cells = createCells(cellA1, cellB1, cellC1, cellD1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestToLowerCaseInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:toLowerCase(\"CAR\"))";
    String expectedRendering = "Class: car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestTrimInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:trim(\"  Car  \"))";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestPrintfInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:printf(\"A_%s\", @A1))";
    String expectedRendering = "Class: A_Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestPrintfWithImplicitParameterInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:printf(\"A_%s\"))";
    String expectedRendering = "Class: A_Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestPrintfInWithBadFormatInReference() throws MappingMasterException, ParseException, IOException
  {
    this.thrown.expect(RendererException.class);

    String expression = "Class: @A1(mm:printf(\"%a\", @A1))";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    createTextRendering(SHEET1, cells, expression, settings);
  }

  @Test public void TestDecimalFormatInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasSalary @A1(mm:decimalFormat(\"###,###.00\", @A1))";
    String expectedRendering = "Individual: Fred Facts: hasSalary \"23,000.20\"";
    Label cellA1 = createCell("23000.2", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestDecimalFormatWithImplicitParameterInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasSalary @A1(mm:decimalFormat(\"###,###.00\"))";
    String expectedRendering = "Individual: Fred Facts: hasSalary \"23,000.20\"";
    Label cellA1 = createCell("23000.2", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestDecimalFormatWithBadFormatInReference() throws MappingMasterException, ParseException, IOException
  {
    this.thrown.expect(RendererException.class);
    this.thrown.expectMessage("function mm:decimalFormat supplied with illegal pattern ..");

    String expression = "Individual: Fred Facts: hasSalary @A1(mm:decimalFormat(\"..\", @A1))";
    Label cellA1 = createCell("23000.2", 1, 1);
    Set<Label> cells = createCells(cellA1);
    createTextRendering(SHEET1, cells, expression, settings);
  }

  @Test public void TestReverseInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:reverse(\"raC\"))";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestToUpperCaseInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:toUpperCase(@A1))";
    String expectedRendering = "Class: CAR";
    Label cellA1 = createCell("car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestToUpperCaseImplicitInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:toUpperCase)";
    String expectedRendering = "Class: CAR";
    Label cellA1 = createCell("car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestReplaceAllInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasItem @A1(mm:replaceAll(\"[^a-zA-Z0-9]\",\"\"))";
    String expectedRendering = "Individual: Fred Facts: hasItem \"bag\"";
    Label cellA1 = createCell(")(*bag%^&$#@!", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestCapturingExpressionInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(rdfs:label=[\":(\\S+)\"])";
    String expectedRendering = "Class: Zyvox";
    Label cellA1 = createCell("Pfizer:Zyvox", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestCapturingExpressionStandaloneInReference()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1([\":(\\S+)\"])";
    String expectedRendering = "Class: Zyvox";
    Label cellA1 = createCell("Pfizer:Zyvox", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestCapturingExpressionMethodInReference()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(rdfs:label=mm:capturing(\":(\\S+)\"))";
    String expectedRendering = "Class: Zyvox";
    Label cellA1 = createCell("Pfizer:Zyvox", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestMultipleCapturingExpressionsInReference()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred  " +
        "Facts: " +
        "hasMin @A1(xsd:integer [\"(\\d+)\\s+\"]), " +
        "hasMax @A1(xsd:integer [\"\\s+(\\d+)\"])" +
        "Types: Person";
    String expectedRendering = "Individual: Fred Facts: hasMin 23, hasMax 44 Types: Person";
    Label cellA1 = createCell("23 44", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestDefaultLocationValueInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:DefaultLocationValue=\"Unknown\")";
    String expectedRendering = "Class: Unknown";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestDefaultLabelInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(rdfs:label mm:DefaultLabel=\"Unknown\")";
    String expectedRendering = "Class: Unknown";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestResolveIfOWLEntityExistsInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:ResolveIfOWLEntityExists)";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestSkipIfOWLEntityExistsInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:SkipIfOWLEntityExists)";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestWarningIfOWLEntityExistsInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:WarningIfOWLEntityExists)";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestErrorIfOWLEntityExistsInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:ErrorIfOWLEntityExists)";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestCreateIfOWLEntityDoesNotExistInReference()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:CreateIfOWLEntityDoesNotExist)";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestSkipIfOWLEntityDoesNotExistInReference()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:SkipIfOWLEntityDoesNotExist)";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestWarningIfOWLEntityDoesNotExistInReference()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:WarningIfOWLEntityDoesNotExist)";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestErrorIfOWLEntityDoesNotExistInReference()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:ErrorIfOWLEntityDoesNotExist)";
    String expectedRendering = "Class: Car";
    Label cellA1 = createCell("Car", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestErrorIfEmptyLocationDirectiveInReference()
      throws MappingMasterException, ParseException, IOException
  {
    this.thrown.expect(RendererException.class);

    String expression = "Class: @A1(mm:ErrorIfEmptyLocation)";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    createTextRendering(SHEET1, cells, expression, settings);
  }

  @Test public void TestErrorIfEmptyLiteralDirectiveInReference()
      throws MappingMasterException, ParseException, IOException
  {
    this.thrown.expect(RendererException.class);

    String expression = "Individual: Fred Facts: hasName @A1(xsd:string mm:ErrorIfEmptyLiteral)";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    createTextRendering(SHEET1, cells, expression, settings);
  }

  @Test public void TestErrorIfEmptyRDFSLabelDirectiveInReference()
      throws MappingMasterException, ParseException, IOException
  {
    this.thrown.expect(RendererException.class);

    String expression = "Class: @A1(mm:ErrorIfEmptyLabel)";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    createTextRendering(SHEET1, cells, expression, settings);
  }

  @Test public void TestErrorIfEmptyRDFIDDirectiveInReference()
      throws MappingMasterException, ParseException, IOException
  {
    this.thrown.expect(RendererException.class);

    String expression = "Class: @A1(rdf:ID=@A1 mm:ErrorIfEmptyID)";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    createTextRendering(SHEET1, cells, expression, settings);
  }

  @Test public void TestSkipIfEmptyLocationInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasName @A1(xsd:string mm:SkipIfEmptyLocation)";
    String expectedRendering = "Individual: Fred";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestSkipIfEmptyLiteralInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasName @A1(xsd:string mm:SkipIfEmptyLiteral)";
    String expectedRendering = "Individual: Fred";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestSkipIfEmptyRDFSLabelDirectiveInReference()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:SkipIfEmptyLabel)";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertFalse(textRendering.isPresent());
  }

  @Test public void TestSkipIfEmptyRDFIDDirectiveInReference()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(rdf:ID mm:SkipIfEmptyID)";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(!textRendering.isPresent());
  }

  @Test public void TestWarningIfEmptyLocationInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasName @A1(xsd:string mm:WarningIfEmptyLocation)";
    String expectedRendering = "Individual: Fred";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestWarningIfEmptyLiteralInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasName @A1(xsd:string mm:WarningIfEmptyLiteral)";
    String expectedRendering = "Individual: Fred";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  @Test public void TestWarningIfEmptyRDFSLabelDirectiveInReference()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(mm:WarningIfEmptyLabel)";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertFalse(textRendering.isPresent());
  }

  @Test public void TestWarningIfEmptyRDFIDDirectiveInReference()
      throws MappingMasterException, ParseException, IOException
  {
    String expression = "Class: @A1(rdf:ID mm:WarningIfEmptyID)";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(!textRendering.isPresent());
  }

  @Test public void TestProcessIfEmptyLiteralInReference() throws MappingMasterException, ParseException, IOException
  {
    String expression = "Individual: Fred Facts: hasName @A1(xsd:string mm:ProcessIfEmptyLiteral)";
    String expectedRendering = "Individual: Fred Facts: hasName \"\"";
    Label cellA1 = createCell("", 1, 1);
    Set<Label> cells = createCells(cellA1);
    Optional<? extends TextRendering> textRendering = createTextRendering(SHEET1, cells, expression, settings);

    Assert.assertTrue(textRendering.isPresent());
    Assert.assertEquals(expectedRendering, clean(textRendering.get().getRendering()));
  }

  // TODO: Fix later
//  @Test public void TestOutOfRangeColumnInReference() throws MappingMasterException, ParseException, IOException
//  {
//    this.thrown.expect(RendererException.class);
//    this.thrown.expectMessage("Invalid source specification @'Sheet1'!D1 - column D is out of range");
//
//    String expression = "Class: @D1";
//    Label cellA1 = createCell("Car", 1, 1);
//    Set<Label> cells = createCells(cellA1);
//    createTextRendering(SHEET1, cells, expression, settings);
//  }

  // TODO: Fix later
//  @Test public void TestOutOfRangeRowInReference() throws MappingMasterException, ParseException, IOException
//  {
//    this.thrown.expect(RendererException.class);
//    this.thrown.expectMessage("Invalid source specification @'Sheet1'!A3 - row 3 is out of range");
//
//    String expression = "Class: @A3";
//    Label cellA1 = createCell("Car", 1, 1);
//    Set<Label> cells = createCells(cellA1);
//    createTextRendering(SHEET1, cells, expression, settings);
//  }

  @Test public void TestInvalidSheetNameInReference() throws MappingMasterException, ParseException, IOException
  {
    this.thrown.expect(RendererException.class);
    this.thrown.expectMessage("Sheet name 'fff' does not exist");

    String expression = "Class: @'fff'!A3";
    createTextRendering(SHEET1, expression, settings);
  }

  @Test public void TestParseException() throws MappingMasterException, ParseException, IOException
  {
    this.thrown.expect(ParseException.class);

    String expression = "Class: @";
    createTextRendering(expression, settings);
  }

  public static String clean(String text)
  {
    return text.replaceAll("\n\\s*", " ").trim();
  }
}
