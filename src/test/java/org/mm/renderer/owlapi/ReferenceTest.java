package org.mm.renderer.owlapi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataPropertyAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Datatype;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.IRI;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.NamedIndividual;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectPropertyAssertion;

import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.mm.core.settings.ReferenceSettings;
import org.mm.core.settings.ValueEncodingSetting;
import org.mm.renderer.IntegrationTestBase;
import org.mm.renderer.RendererException;
import org.mm.renderer.owlapi.AllRenderingTestSuite.CellProcessingTest;
import org.mm.renderer.owlapi.AllRenderingTestSuite.DirectiveTest;
import org.mm.renderer.owlapi.AllRenderingTestSuite.NameResolutionTest;
import org.mm.rendering.owlapi.OWLRendering;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.vocab.Namespaces;

public class ReferenceTest extends IntegrationTestBase
{
   private OWLOntology ontology;
   private ReferenceSettings settings;

   private static final OWLClass BMW = Class(IRI(ONTOLOGY_ID, "BMW"));
   private static final OWLClass CAR = Class(IRI(ONTOLOGY_ID, "Car"));
   private static final OWLClass CAR_LOWERCASE = Class(IRI(ONTOLOGY_ID, "car"));
   private static final OWLClass CAR_UPPERCASE = Class(IRI(ONTOLOGY_ID, "CAR"));
   private static final OWLClass BMW_CAR = Class(IRI(ONTOLOGY_ID, "BMW_Car"));
   private static final OWLClass CAR_BMW = Class(IRI(ONTOLOGY_ID, "Car_BMW"));
   private static final OWLClass BAYERISCHE_MOTOREN_WERKE = Class(IRI(ONTOLOGY_ID, "Bayerische_Motoren_Werke"));
   private static final OWLClass ZYVOX = Class(IRI(ONTOLOGY_ID, "Zyvox"));
   private static final OWLClass DEFAULT_NAME = Class(IRI(ONTOLOGY_ID, "Default_Name"));
   private static final OWLObjectProperty HAS_PARENT = ObjectProperty(IRI(ONTOLOGY_ID, "hasParent"));
   private static final OWLDataProperty HAS_SSN = DataProperty(IRI(ONTOLOGY_ID, "hasSSN"));
   private static final OWLDataProperty HAS_NAME = DataProperty(IRI(ONTOLOGY_ID, "hasName"));
   private static final OWLDataProperty HAS_AGE = DataProperty(IRI(ONTOLOGY_ID, "hasAge"));
   private static final OWLDataProperty HAS_SALARY = DataProperty(IRI(ONTOLOGY_ID, "hasSalary"));
   private static final OWLDataProperty HAS_DOB = DataProperty(IRI(ONTOLOGY_ID, "hasDOB"));
   private static final OWLDataProperty HAS_BEDTIME = DataProperty(IRI(ONTOLOGY_ID, "hasBedTime"));
   private static final OWLNamedIndividual FRED = NamedIndividual(IRI(ONTOLOGY_ID, "fred"));
   private static final OWLNamedIndividual BOB = NamedIndividual(IRI(ONTOLOGY_ID, "bob"));
   private static final OWLAnnotationProperty HAS_AGE_ANNOTATION = AnnotationProperty(IRI(ONTOLOGY_ID, "hasAge"));
   private static final OWLAnnotationProperty RDFS_LABEL = AnnotationProperty(IRI(Namespaces.RDFS + "label"));
   private static final OWLDatatype RDFS_LITERAL = Datatype(IRI(Namespaces.RDFS + "Literal"));
   private static final OWLDatatype XSD_STRING = Datatype(IRI(Namespaces.XSD + "string"));
   private static final OWLDatatype XSD_BOOLEAN = Datatype(IRI(Namespaces.XSD + "boolean"));
   private static final OWLDatatype XSD_DOUBLE = Datatype(IRI(Namespaces.XSD + "double"));
   private static final OWLDatatype XSD_FLOAT = Datatype(IRI(Namespaces.XSD + "float"));
   private static final OWLDatatype XSD_LONG = Datatype(IRI(Namespaces.XSD + "long"));
   private static final OWLDatatype XSD_INTEGER = Datatype(IRI(Namespaces.XSD + "integer"));
   private static final OWLDatatype XSD_SHORT = Datatype(IRI(Namespaces.XSD + "short"));
   private static final OWLDatatype XSD_BYTE = Datatype(IRI(Namespaces.XSD + "byte"));
   private static final OWLDatatype XSD_DECIMAL = Datatype(IRI(Namespaces.XSD + "decimal"));
   private static final OWLDatatype XSD_DATETIME = Datatype(IRI(Namespaces.XSD + "dateTime"));
   private static final OWLDatatype XSD_TIME = Datatype(IRI(Namespaces.XSD + "time"));
   private static final OWLDatatype XSD_DATE = Datatype(IRI(Namespaces.XSD + "date"));

   @Rule
   public final ExpectedException thrown = ExpectedException.none();

   @Before
   public void setUp() throws OWLOntologyCreationException
   {
      ontology = createOWLOntology();
      settings = new ReferenceSettings();
      settings.setValueEncodingSetting(ValueEncodingSetting.RDF_ID);
   }

   /*
    * Test the class declaration.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestAbsoluteReference() throws Exception
   {
      Label cellA1 = createCell("Car", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
   }

   /*
    * Test the class declaration with fully-qualified reference.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(NameResolutionTest.class)   public void TestAbsoluteReferenceWithSheetName() throws Exception
   {
      Label cellA1 = createCell("Car", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @'Sheet1'!A1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
   }

   /*
    * Test the individual declaration.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined individuals in the ontology.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestAbsoluteIndividualReference() throws Exception
   {
      Label cellA1 = createCell("fred", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Individual: @A1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(FRED)));
   }

   /*
    * Test the data property reference in individual assertion. Note the individual entity is a constant.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + The target individual must be predefined in the ontology,
    *    + No necessary predefined properties in the ontology.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestAbsoluteDataPropertyReference() throws Exception
   {
      declareOWLNamedIndividual(ontology, "fred");

      Label cellA1 = createCell("hasAge", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Individual: fred Facts: @A1 23";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_AGE, FRED, Literal("23", XSD_INTEGER))));
   }

   /*
    * Test the object property reference in individual assertion. By default, the system will recognize the property
    * declaration as data property. To override the behavior use the (ObjectProperty) directive.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + The target individuals must be predefined in the ontology,
    *    + No necessary predefined properties in the ontology.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestAbsoluteObjectPropertyReference() throws Exception
   {
      declareOWLNamedIndividuals(ontology, "fred", "bob");

      Label cellA1 = createCell("hasParent", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Individual: fred Facts: @A1(ObjectProperty) bob";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            ObjectPropertyAssertion(HAS_PARENT, FRED, BOB)));
   }

   /*
    * Test the annotation property reference in individual assertion. By default, the system will recognize the property
    * declaration as data property. To override the behavior use the (AnnotationProperty) directive.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + The target individuals must be predefined in the ontology,
    *    + No necessary predefined properties in the ontology.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestAnnotationPropertyQualifiedReference() throws Exception
   {
      declareOWLNamedIndividual(ontology, "fred");

      Label cellA1 = createCell("hasAge", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Individual: fred Annotations: @A1(AnnotationProperty) 23";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            AnnotationAssertion(HAS_AGE_ANNOTATION, IRI(ONTOLOGY_ID, "fred"), Literal("23", XSD_INTEGER))));
   }

   /*
    * Test literal reference, i.e., reference with enclosed quote signs (e.g., @"Car").
    * - Precondition:
    *    + No necessary predefined classes in the ontology. // TODO Should it be predefined?
    * - Expected results:
    *    + Creating a class declaration axiom,
    *    + Creating a rdfs:label annotation axiom. Both will use the quoted value.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestLiteralReference() throws Exception
   {
      String expression = "Class: @\"Car\"";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            AnnotationAssertion(RDFS_LABEL, IRI(ONTOLOGY_ID, "Car"), Literal("Car", XSD_STRING))
      ));
   }

   /*
    * Test column-wildcard reference for declaring a class.
    * - Precondition:
    *    + The target sheet cell must not be empty
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestColumnWildcardInReference() throws Exception
   {
      Label cellA1 = createCell("Car", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @*1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
   }

   /*
    * Test row-wildcard reference for declaring a class.
    * - Precondition:
    *    + The target sheet cell must not be empty
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestRowWildcardInReference() throws Exception
   {
      Label cellA1 = createCell("Car", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A*";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
   }

   /*
    * Test individual data property assertion with input datatype xsd:string. Note that the (xsd:string)
    * is optional, by default.
    * - Precondition:
    *    + The target sheet cells must not be empty,
    *    + The target property must be predefined in the ontology,
    *    + No necessary predefined individuals in the ontology.
    * - Expected results:
    *    + Creating an individual declaration axiom,
    *    + Creating a data property assertion axiom.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestXSDStringInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasName");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("Alfred", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasName @B1(xsd:string)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_NAME, FRED, Literal("Alfred", XSD_STRING))));
   }

   /*
    * Test individual data property assertion with input datatype xsd:boolean.
    * - Precondition:
    *    + The target sheet cells must not be empty,
    *    + The target property must be predefined in the ontology,
    *    + No necessary predefined individuals in the ontology.
    * - Expected results:
    *    + Creating an individual declaration axiom,
    *    + Creating a data property assertion axiom.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestXSDBooleanInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasSSN");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("true", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasSSN @B1(xsd:boolean)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_SSN, FRED, Literal("true", XSD_BOOLEAN))));
   }

   /*
    * Test individual data property assertion with input datatype xsd:doublt.
    * - Precondition:
    *    + The target sheet cells must not be empty,
    *    + The target property must be predefined in the ontology,
    *    + No necessary predefined individuals in the ontology.
    * - Expected results:
    *    + Creating an individual declaration axiom,
    *    + Creating a data property assertion axiom.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestXSDDoubleInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasSalary");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("34000.09999", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasSalary @B1(xsd:double)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_SALARY, FRED, Literal("34000.09999", XSD_DOUBLE))));
   }

   /*
    * Test individual data property assertion with input datatype xsd:float.
    * - Precondition:
    *    + The target sheet cells must not be empty,
    *    + The target property must be predefined in the ontology,
    *    + No necessary predefined individuals in the ontology.
    * - Expected results:
    *    + Creating an individual declaration axiom,
    *    + Creating a data property assertion axiom.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestXSDFloatInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasSalary");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("34000.09999", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasSalary @B1(xsd:float)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_SALARY, FRED, Literal("34000.09999", XSD_FLOAT))));
   }

   /*
    * Test individual data property assertion with input datatype xsd:long.
    * - Precondition:
    *    + The target sheet cells must not be empty,
    *    + The target property must be predefined in the ontology,
    *    + No necessary predefined individuals in the ontology.
    * - Expected results:
    *    + Creating an individual declaration axiom,
    *    + Creating a data property assertion axiom.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestXSDLongInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasAge");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("23", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasAge @B1(xsd:long)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_AGE, FRED, Literal("23", XSD_LONG))));
   }

   /*
    * Test individual data property assertion with input datatype xsd:integer.
    * - Precondition:
    *    + The target sheet cells must not be empty,
    *    + The target property must be predefined in the ontology,
    *    + No necessary predefined individuals in the ontology.
    * - Expected results:
    *    + Creating an individual declaration axiom,
    *    + Creating a data property assertion axiom.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestXSDIntegerInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasAge");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("23", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasAge @B1(xsd:int)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_AGE, FRED, Literal("23", XSD_INTEGER))));
   }

   /*
    * Test individual data property assertion with input datatype xsd:short.
    * - Precondition:
    *    + The target sheet cells must not be empty,
    *    + The target property must be predefined in the ontology,
    *    + No necessary predefined individuals in the ontology.
    * - Expected results:
    *    + Creating an individual declaration axiom,
    *    + Creating a data property assertion axiom.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestXSDShortInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasAge");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("23", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasAge @B1(xsd:short)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_AGE, FRED, Literal("23", XSD_SHORT))));
   }

   /*
    * Test individual data property assertion with input datatype xsd:byte.
    * - Precondition:
    *    + The target sheet cells must not be empty,
    *    + The target property must be predefined in the ontology,
    *    + No necessary predefined individuals in the ontology.
    * - Expected results:
    *    + Creating an individual declaration axiom,
    *    + Creating a data property assertion axiom.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestXSDByteInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasAge");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("23", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasAge @B1(xsd:byte)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_AGE, FRED, Literal("23", XSD_BYTE))));
   }

   /*
    * Test individual data property assertion with input datatype xsd:decimal.
    * - Precondition:
    *    + The target sheet cells must not be empty,
    *    + The target property must be predefined in the ontology,
    *    + No necessary predefined individuals in the ontology.
    * - Expected results:
    *    + Creating an individual declaration axiom,
    *    + Creating a data property assertion axiom.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestXSDDecimalInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasAge");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("23.00", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasAge @B1(xsd:decimal)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_AGE, FRED, Literal("23.00", XSD_DECIMAL))));
   }

   /*
    * Test individual data property assertion with input datatype xsd:dateTime.
    * - Precondition:
    *    + The target sheet cells must not be empty,
    *    + The target property must be predefined in the ontology,
    *    + No necessary predefined individuals in the ontology.
    * - Expected results:
    *    + Creating an individual declaration axiom,
    *    + Creating a data property assertion axiom.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestXSDDateTimeInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasDOB");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("1999-01-01T10:10:10", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasDOB @B1(xsd:dateTime)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_DOB, FRED, Literal("1999-01-01T10:10:10", XSD_DATETIME))));
   }

   /*
    * Test individual data property assertion with input datatype xsd:date.
    * - Precondition:
    *    + The target sheet cells must not be empty,
    *    + The target property must be predefined in the ontology,
    *    + No necessary predefined individuals in the ontology.
    * - Expected results:
    *    + Creating an individual declaration axiom,
    *    + Creating a data property assertion axiom.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestXSDDateInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasDOB");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("1999-01-01", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasDOB @B1(xsd:date)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_DOB, FRED, Literal("1999-01-01", XSD_DATE))));
   }

   /*
    * Test individual data property assertion with input datatype xsd:time.
    * - Precondition:
    *    + The target sheet cells must not be empty,
    *    + The target property must be predefined in the ontology,
    *    + No necessary predefined individuals in the ontology.
    * - Expected results:
    *    + Creating an individual declaration axiom,
    *    + Creating a data property assertion axiom.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestXSDTimeInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasBedTime");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("21:00", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasBedTime @B1(xsd:time)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_BEDTIME, FRED, Literal("21:00", XSD_TIME))));
   }

   /*
    * Test (rdfs:label) directive in class declaration.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    * - Expected results:
    *    + Creating a class declaration axiom with name using the (rdfs:label) value, instead of the cell value,
    *    + Creating a rdfs:label annotation using the given (rdfs:label) value.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestRDFSLabelAssignmentInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label=(\"Bayerische Motoren Werke\"))";
   
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(BAYERISCHE_MOTOREN_WERKE),
            AnnotationAssertion(RDFS_LABEL, IRI(ONTOLOGY_ID, "Bayerische_Motoren_Werke"), Literal("Bayerische Motoren Werke", XSD_STRING))
      ));
   }

   /*
    * Test (rdf:id) and (rdfs:label) directives in class declaration.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    * - Expected results:
    *    + Creating a class declaration axiom with name using the (rdf:id) value,
    *    + Creating a rdfs:label annotation using the given (rdfs:label) value.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestRDFIDAndRDFSLabelAssignmentInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdf:id=@A1 rdfs:label=(\"Bayerische Motoren Werke\"))";
 
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(BMW),
            AnnotationAssertion(RDFS_LABEL, IRI(ONTOLOGY_ID, "BMW"), Literal("Bayerische Motoren Werke", XSD_STRING))
      ));
   }

   /*
    * Test setting the default label for (rdfs:label) directives in class declaration.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    * - Expected results:
    *    + Creating a class declaration axiom with name using default label, instead of the cell value,
    *    + Creating a rdfs:label annotation using the given (rdfs:label) value.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestDefaultLabelInReference() throws Exception
   {
      Label cellA1 = createCell("Some Name", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label mm:DefaultLabel=\"Default Name\")";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(DEFAULT_NAME),
            AnnotationAssertion(RDFS_LABEL, IRI(ONTOLOGY_ID, "Default_Name"), Literal("Default Name", XSD_STRING))
      ));
   }

   /*
    * Test (mm:append) function in class declaration.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    * - Expected results:
    *    + Creating a class declaration axiom with name using the appended value, instead of the cell value,
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestStringAppendInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:append(\" Car\"))";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(BMW_CAR)));
   }

   /*
    * Test explicit append function for (rdfs:label) directive in class declaration.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    * - Expected results:
    *    + Creating a class declaration axiom with name using the appended value, instead of the cell value,
    *    + Creating a rdfs:label annotation using the given (rdfs:label) value.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestExplicitStringAppendInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label=mm:append(\" Car\"))";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(BMW_CAR),
            AnnotationAssertion(RDFS_LABEL, IRI(ONTOLOGY_ID, "BMW_Car"), Literal("BMW Car", XSD_STRING))));
   }

   /*
    * Test implicit append function for (rdfs:label) directive in class declaration.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    * - Expected results:
    *    + Creating a class declaration axiom with name using the appended value, instead of the cell value,
    *    + Creating a rdfs:label annotation using the given (rdfs:label) value.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestImplicitStringAppendInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label=(@A1, \" Car\"))";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(BMW_CAR),
            AnnotationAssertion(RDFS_LABEL, IRI(ONTOLOGY_ID, "BMW_Car"), Literal("BMW Car", XSD_STRING))));
   }

   /*
    * Test (mm:prepend) function in class declaration.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    * - Expected results:
    *    + Creating a class declaration axiom with name using the prepended value, instead of the cell value,
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestStringPrependInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:prepend(\"Car \"))";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR_BMW)));
   }

   /*
    * Test explicit prepend function for (rdfs:label) directive in class declaration.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    * - Expected results:
    *    + Creating a class declaration axiom with name using the prepended value, instead of the cell value,
    *    + Creating a rdfs:label annotation using the given (rdfs:label) value.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestExplicitStringPrependInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label=mm:prepend(\"Car \"))";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR_BMW),
            AnnotationAssertion(RDFS_LABEL, IRI(ONTOLOGY_ID, "Car_BMW"), Literal("Car BMW", XSD_STRING))
      ));
   }

   /*
    * Test implicit prepend function for (rdfs:label) directive in class declaration.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    * - Expected results:
    *    + Creating a class declaration axiom with name using the prepended value, instead of the cell value,
    *    + Creating a rdfs:label annotation using the given (rdfs:label) value.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestImplicitStringPrependInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label=(\"Car \", @A1))";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR_BMW),
            AnnotationAssertion(RDFS_LABEL, IRI(ONTOLOGY_ID, "Car_BMW"), Literal("Car BMW", XSD_STRING))
      ));
   }

   /*
    * Test (mm:ShiftUp) function.
    * - Precondition:
    *    + At least one of the target sheet cells must not be empty,
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestShiftUpInReference() throws Exception
   {
      Label cellA1 = createCell("Car", 1, 1);
      Label cellA2 = createCell("", 1, 2);
      Label cellA3 = createCell("", 1, 3);
      Label cellA4 = createCell("", 1, 4);
      Set<Label> cells = createCells(cellA1, cellA2, cellA3, cellA4);

      String expression = "Class: @A4(mm:ShiftUp)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
   }

   /*
    * Test (mm:ShiftDown) function.
    * - Precondition:
    *    + At least one of the target sheet cells must not be empty,
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestShiftDownInReference() throws Exception
   {
      declareOWLClasses(ontology, "Car");

      Label cellA1 = createCell("", 1, 1);
      Label cellA2 = createCell("", 1, 2);
      Label cellA3 = createCell("", 1, 3);
      Label cellA4 = createCell("Car", 1, 4);
      Set<Label> cells = createCells(cellA1, cellA2, cellA3, cellA4);

      String expression = "Class: @A1(mm:ShiftDown)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
   }

   /*
    * Test (mm:ShiftRight) function.
    * - Precondition:
    *    + At least one of the target sheet cells must not be empty,
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestShiftRightInReference() throws Exception
   {
      declareOWLClasses(ontology, "Car");

      Label cellA1 = createCell("", 1, 1);
      Label cellB1 = createCell("", 2, 1);
      Label cellC1 = createCell("", 3, 1);
      Label cellD1 = createCell("Car", 4, 1);
      Set<Label> cells = createCells(cellA1, cellB1, cellC1, cellD1);

      String expression = "Class: @A1(mm:ShiftRight)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
   }

   /*
    * Test (mm:ShiftLeft) function.
    * - Precondition:
    *    + At least one of the target sheet cells must not be empty,
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestShiftLeftInReference() throws Exception
   {
      declareOWLClasses(ontology, "Car");

      Label cellA1 = createCell("Car", 1, 1);
      Label cellB1 = createCell("", 2, 1);
      Label cellC1 = createCell("", 3, 1);
      Label cellD1 = createCell("", 4, 1);
      Set<Label> cells = createCells(cellA1, cellB1, cellC1, cellD1);

      String expression = "Class: @D1(mm:ShiftLeft)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
   }

   /*
    * Test multiple shift functions and appending the values.
    * - Precondition:
    *    + At least one of the target sheet cells must not be empty,
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestReferencesInReference() throws Exception
   {
      Label cellA1 = createCell("B", 1, 1);
      Label cellB1 = createCell("", 2, 1);
      Label cellB2 = createCell("", 2, 2);
      Label cellB3 = createCell("", 2, 3);
      Label cellB4 = createCell("M", 2, 4);
      Label cellC1 = createCell("", 3, 1);
      Label cellD1 = createCell("", 4, 1);
      Label cellE1 = createCell("", 5, 1);
      Label cellF1 = createCell("W", 6, 1);
      Set<Label> cells = createCells(cellA1, cellB1, cellB2, cellB3, cellB4, cellC1, cellD1, cellE1, cellF1);

      String expression = "Class: @A*(mm:append(@B*(mm:ShiftDown), @C*(mm:ShiftRight)))";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(BMW)));
   }

   /*
    * Test (mm:toLowerCase) function.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestToLowerCaseInReference() throws Exception
   {
      Label cellA1 = createCell("CAR", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:toLowerCase)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR_LOWERCASE)));
   }

   /*
    * Test (mm:toUpperCase) function.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestToUpperCaseInReference() throws Exception
   {
      Label cellA1 = createCell("Car", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:toUpperCase)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR_UPPERCASE)));
   }

   /*
    * Test (mm:reverse) function.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestReverseInReference() throws Exception
   {
      Label cellA1 = createCell("raC", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:reverse)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
   }

   /*
    * Test (mm:trim) function.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestTrimInReference() throws Exception
   {
      Label cellA1 = createCell("  Car  ", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:trim)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
   }

   /*
    * Test (mm:printf) function.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestPrintfInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:printf(\"%s Car\"))";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(BMW_CAR)));
   }

   /*
    * Test (mm:decimalFormat) function.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + The target data property must be predefined in the ontology,
    *    + No necessary predefined individuals in the ontology.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestDecimalFormatInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasSalary");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("23000.2", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasSalary @B1(xsd:decimal mm:decimalFormat(\"###,###.00\"))";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_SALARY, FRED, Literal("23,000.20", XSD_DECIMAL))));
   }

   /*
    * Test (mm:replaceAll) function.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + The target data property must be predefined in the ontology,
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestReplaceAllInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasName");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell(")(*Alfred%^&$#@!", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasName @B1(mm:replaceAll(\"[^a-zA-Z0-9]\",\"\"))";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_NAME, FRED, Literal("Alfred", XSD_STRING))));
   }

   /*
    * Test (mm:capturing) function in class declaration.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    * - Expected results:
    *    + Creating a class declaration axiom with name using the prepended value, instead of the cell value,
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestCapturingInReference() throws Exception
   {
      Label cellA1 = createCell("Pfizer:Zyvox", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:capturing(\":(\\S+)\"))";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(ZYVOX)));
   }

   /*
    * Test implicit (mm:capturing) function in class declaration.
    * - Precondition:
    *    + The target sheet cell must not be empty,
    *    + No necessary predefined classes in the ontology.
    * - Expected results:
    *    + Creating a class declaration axiom with name using the prepended value, instead of the cell value,
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestImplicitCapturingInReference() throws Exception
   {
      Label cellA1 = createCell("Pfizer:Zyvox", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1([\":(\\S+)\"])";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(ZYVOX)));
   }

   /*
    * Test (mm:SkipIfOWLEntityDoesNotExist) directive.
    * Expected results:
    *    + No class declaration should be created.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestSkipIfOWLEntityDoesNotExistInReference() throws Exception
   {
      Label cellA1 = createCell("Car", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:SkipIfOWLEntityDoesNotExist)";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /*
    * Test (mm:WarningIfOWLEntityDoesNotExist) directive.
    * Expected results:
    *    + No class declaration axiom should be created and a warning log should appear.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestWarningIfOWLEntityDoesNotExistInReference() throws Exception
   {
      Label cellA1 = createCell("Car", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:WarningIfOWLEntityDoesNotExist)";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /*
    * Test (mm:ErrorIfOWLEntityDoesNotExist) directive.
    * Expected results:
    *    + No class declaration axiom should be created and an error should be thrown.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestErrorIfOWLEntityDoesNotExistInReference() throws Exception
   {
      thrown.expect(RendererException.class);

      Label cellA1 = createCell("Car", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:ErrorIfOWLEntityDoesNotExist)";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /*
    * Test (mm:CreateIfOWLEntityDoesNotExist) directive. This is the default setting for resolving reference values.
    * Expected results:
    *    + Creating a class declaration axiom.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestCreateIfOWLEntityDoesNotExistInReference() throws Exception
   {
      Label cellA1 = createCell("Car", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:CreateIfOWLEntityDoesNotExist)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
   }

   /*
    * Test (mm:ErrorIfEmptyLocation) directive. Note that the sheet cell is empty
    * Expected results:
    *    + No class declaration axiom should be created.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestSkipIfEmptyLocationDirectiveInReference() throws Exception
   {
      Label cellA1 = createCell("", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:SkipIfEmptyLocation)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }


   /*
    * Test (mm:WarningIfEmptyLocation) directive. Note that the sheet cell is empty
    * Expected results:
    *    + No class declaration axiom should be created and a warning log should appear.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestWarningIfEmptyLocationDirectiveInReference() throws Exception
   {
      Label cellA1 = createCell("", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:WarningIfEmptyLocation)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /*
    * Test (mm:ErrorIfEmptyLocation) directive. Note that the sheet cell is empty
    * Expected results:
    *    + No class declaration axiom should be created and an error should be thrown.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestErrorIfEmptyLocationDirectiveInReference() throws Exception
   {
      thrown.expect(RendererException.class);

      Label cellA1 = createCell("", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:ErrorIfEmptyLocation)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /*
    * Test (mm:SkipIfEmptyID) directive. Note that the sheet cell is empty
    * Expected results:
    *    + No class declaration axiom should be created.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestSkipIfEmptyIDDirectiveInReference() throws Exception
   {
      Label cellA1 = createCell("", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:SkipIfEmptyID)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }


   /*
    * Test (mm:WarningIfEmptyID) directive. Note that the sheet cell is empty
    * Expected results:
    *    + No class declaration axiom should be created and a warning log should appear.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestWarningIfEmptyIDDirectiveInReference() throws Exception
   {
      Label cellA1 = createCell("", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:WarningIfEmptyID)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /*
    * Test (mm:ErrorIfEmptyID) directive. Note that the sheet cell is empty
    * Expected results:
    *    + No class declaration axiom should be created and an error should be thrown.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestErrorIfEmptyIDDirectiveInReference() throws Exception
   {
      thrown.expect(RendererException.class);

      Label cellA1 = createCell("", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:ErrorIfEmptyID)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /*
    * Test (mm:ProcessIfEmptyLiteral) directive.
    * Expected results:
    *    + Creating an individual declaration and with empty value for the data property assertion axiom.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestProcessIfEmptyLiteralDirectiveInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasName");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasName @B1(mm:ProcessIfEmptyLiteral)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_NAME, FRED, Literal("", XSD_STRING))));
   }

   /*
    * Test (mm:SkipIfEmptyLiteral) directive.
    * Expected results:
    *    + Creating an individual declaration but without data property assertion axiom.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestSkipIfEmptyLiteralDirectiveInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasName");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasName @B1(mm:SkipIfEmptyLiteral)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(FRED)));
   }

   /*
    * Test (mm:WarningIfEmptyLiteral) directive.
    * Expected results:
    *    + Creating an individual declaration but without data property assertion axiom,
    *    + A warning log should appear.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestWarningIfEmptyLiteralDirectiveInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasName");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasName @B1(mm:WarningIfEmptyLiteral)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(FRED)));
   }

   /*
    * Test (mm:ErrorIfEmptyLiteral) directive.
    * Expected results:
    *    + No individual declaration and data property assertion axiom should be created,
    *    + An error should be thrown.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestErrorIfEmptyLiteralDirectiveInReference() throws Exception
   {
      thrown.expect(RendererException.class);

      declareOWLDataProperty(ontology, "hasName");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasName @B1(mm:ErrorIfEmptyLiteral)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /*
    * Test (mm:SkipIfEmptyLabel) directive.
    * Expected results:
    *    + Creating a class declaration but without the rdfs:label annotation.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestSkipIfEmptyLabelDirectiveInReference() throws Exception
   {
      Label cellA1 = createCell("", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label mm:SkipIfEmptyLabel)";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /*
    * Test (mm:WarningIfEmptyLabel) directive.
    * Expected results:
    *    + Creating a class declaration but without the rdfs:label annotation,
    *    + A warning log should appear.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestWarningIfEmptyLabelDirectiveInReference() throws Exception
   {
      Label cellA1 = createCell("", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label mm:WarningIfEmptyLabel)";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /*
    * Test (mm:ErrorIfEmptyLabel) directive.
    * Expected results:
    *    + No class declaration and rdfs:label annotation should be created,
    *    + An error should be thrown.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestErrorIfEmptyLabelDirectiveInReference() throws Exception
   {
      this.thrown.expect(RendererException.class);

      Label cellA1 = createCell("", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label mm:ErrorIfEmptyLabel)";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /*
    * Test when the decimal format is invalid.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestDecimalFormatWithBadFormatInReference() throws Exception
   {
      thrown.expect(RendererException.class);

      declareOWLDataProperty(ontology, "hasSalary");

      Label cellA1 = createCell("23000.2", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Individual: Fred Facts: hasSalary @A1(mm:decimalFormat(\"..\", @A1))";
      createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
   }

   /*
    * Test when the column is out of range in the specified cell reference. TODO: Fix later
    */
//   @Test
//   @Category(CellProcessingTest.class)
//   public void TestOutOfRangeColumnInReference() throws Exception
//   {
//      thrown.expect(RendererException.class);
//
//      Label cellA1 = createCell("Car", 1, 1);
//      Set<Label> cells = createCells(cellA1);
//
//      String expression = "Class: @D1";
//
//      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
//      assertThat(result.isPresent(), is(false));
//   }

   /*
    * Test when the row is out of range in the specified cell reference. TODO: Fix later
    */
//   @Test
//   @Category(CellProcessingTest.class)
//   public void TestOutOfRangeRowInReference() throws Exception
//   {
//      thrown.expect(RendererException.class);
//
//      Label cellA1 = createCell("Car", 1, 1);
//      Set<Label> cells = createCells(cellA1);
//
//      String expression = "Class: @A3";
//
//      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
//      assertThat(result.isPresent(), is(false));
//   }
}
