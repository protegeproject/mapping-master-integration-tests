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
   private static final OWLClass BARBARA_PUFFINS_CAMELCASE = Class(IRI(ONTOLOGY_ID, "Barbara'sPuffinsHoney-RiceCereal-10.5ozBox"));
   private static final OWLClass BARBARA_PUFFINS_SNAKECASE = Class(IRI(ONTOLOGY_ID, "Barbara's_Puffins_Honey-Rice_Cereal_-_10.5oz_box"));
   private static final OWLClass BARBARA_PUFFINS_MD5 = Class(IRI(ONTOLOGY_ID, "290d31fea17d405a10b7a025dde55111"));
   private static final OWLClass CAR_LOWERCASE = Class(IRI(ONTOLOGY_ID, "car"));
   private static final OWLClass CAR_UPPERCASE = Class(IRI(ONTOLOGY_ID, "CAR"));
   private static final OWLClass BMW_CAR = Class(IRI(ONTOLOGY_ID, "BMWCar"));
   private static final OWLClass CAR_BMW = Class(IRI(ONTOLOGY_ID, "CarBMW"));
   private static final OWLClass BAYERISCHE_MOTOREN_WERKE = Class(IRI(ONTOLOGY_ID, "BayerischeMotorenWerke"));
   private static final OWLClass ZYVOX = Class(IRI(ONTOLOGY_ID, "Zyvox"));
   private static final OWLClass DEFAULT_NAME = Class(IRI(ONTOLOGY_ID, "DefaultName"));
   private static final OWLObjectProperty HAS_PARENT = ObjectProperty(IRI(ONTOLOGY_ID, "hasParent"));
   private static final OWLDataProperty HAS_SSN = DataProperty(IRI(ONTOLOGY_ID, "hasSSN"));
   private static final OWLDataProperty HAS_NAME = DataProperty(IRI(ONTOLOGY_ID, "hasName"));
   private static final OWLDataProperty HAS_AGE = DataProperty(IRI(ONTOLOGY_ID, "hasAge"));
   private static final OWLDataProperty HAS_SALARY = DataProperty(IRI(ONTOLOGY_ID, "hasSalary"));
   private static final OWLDataProperty HAS_DOB = DataProperty(IRI(ONTOLOGY_ID, "hasDOB"));
   private static final OWLDataProperty HAS_BEDTIME = DataProperty(IRI(ONTOLOGY_ID, "hasBedTime"));
   private static final OWLNamedIndividual P1 = NamedIndividual(IRI(ONTOLOGY_ID, "p1"));
   private static final OWLNamedIndividual FRED = NamedIndividual(IRI(ONTOLOGY_ID, "fred"));
   private static final OWLNamedIndividual BOB = NamedIndividual(IRI(ONTOLOGY_ID, "bob"));
   private static final OWLAnnotationProperty HAS_AGE_ANNOTATION = AnnotationProperty(IRI(ONTOLOGY_ID, "hasAge"));
   private static final OWLAnnotationProperty RDFS_LABEL = AnnotationProperty(IRI(Namespaces.RDFS + "label"));
   private static final OWLAnnotationProperty RDFS_COMMENT = AnnotationProperty(IRI(Namespaces.RDFS + "comment"));
   private static final OWLAnnotationProperty SKOS_PREFLABEL = AnnotationProperty(IRI(Namespaces.SKOS + "prefLabel"));
   private static final OWLAnnotationProperty FOAF_DEPICTION = AnnotationProperty(IRI(Namespaces.FOAF + "depiction"));
   private static final OWLDatatype RDF_PLAINLITERAL = Datatype(IRI(Namespaces.RDF + "PlainLiteral"));
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

   /**
    * Test the class declaration.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
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

   /**
    * Test the class declaration with fully-qualified reference.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
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

   /**
    * Test the individual declaration.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
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

   /**
    * Test the data property reference in individual assertion. Note the individual entity is a constant.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + The target individual must be predefined in the ontology,<br />
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

   /**
    * Test the object property reference in individual assertion. By default, the system will recognize the property
    * declaration as data property. To override the behavior use the (ObjectProperty) directive.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + The target individuals must be predefined in the ontology,<br />
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

   /**
    * Test the annotation property reference in individual assertion. By default, the system will recognize the property
    * declaration as data property. To override the behavior use the (AnnotationProperty) directive.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + The target individuals must be predefined in the ontology,<br />
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

   /**
    * Test the IRI annotation value in individual assertion. By default, the system will recognize the annotation
    * value as literal. To override the behavior use the (mm:IRI) directive.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + The target individuals must be predefined in the ontology,<br />
    *    + The target annotation property must be predefined in the ontology.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestIRIReferenceType() throws Exception
   {
      setPrefix(ontology, "foaf", "http://xmlns.com/foaf/0.1/");
      declareOWLNamedIndividual(ontology, "fred");
      declareOWLAnnotationProperty(ontology, "foaf:depiction");

      Label cellA1 = createCell("https://image.freepik.com/free-icon/user-male-silhouette_318-55563.png", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Individual: fred Annotations: foaf:depiction @A1(mm:IRI)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            AnnotationAssertion(FOAF_DEPICTION, IRI(ONTOLOGY_ID, "fred"), IRI("https://image.freepik.com/free-icon/user-male-silhouette_318-55563.png"))));
   }

   /**
    * Test a user-specified annotation property in individual assertion.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + The target annotation property must be predefined in the ontology,<br />
    *    + The target prefix must be predefined in the ontology, if any,
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestUserSpecifiedAnnotationProperty() throws Exception
   {
      setPrefix(ontology, "skos", "http://www.w3.org/2004/02/skos/core#");
      declareOWLAnnotationProperty(ontology, "skos:prefLabel");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("Alfred", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Annotations: skos:prefLabel @B1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            AnnotationAssertion(SKOS_PREFLABEL, IRI(ONTOLOGY_ID, "fred"), Literal("Alfred", XSD_STRING))));
   }

   /**
    * Test literal reference, i.e., reference with enclosed quote signs (e.g., @"Car").
    * <p>
    * - Precondition:<br />
    *    + No necessary predefined classes in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating a class declaration axiom,<br />
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

   /**
    * Test column-wildcard reference for declaring a class.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty<br />
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

   /**
    * Test row-wildcard reference for declaring a class.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
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

   /**
    * Test individual data property assertion with input datatype rdf:PlainLiteral.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cells must not be empty,<br />
    *    + The target property must be predefined in the ontology,<br />
    *    + No necessary predefined individuals in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating an individual declaration axiom,<br />
    *    + Creating a data property assertion axiom.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestRDFPlainLiteralInReference() throws Exception
   {
      declareOWLDataProperty(ontology, "hasName");

      Label cellA1 = createCell("fred", 1, 1);
      Label cellB1 = createCell("Alfred", 2, 1);
      Set<Label> cells = createCells(cellA1, cellB1);

      String expression = "Individual: @A1 Facts: hasName @B1(rdf:PlainLiteral)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_NAME, FRED, Literal("Alfred", RDF_PLAINLITERAL))));
   }

   /**
    * Test individual data property assertion with input datatype xsd:string. Note that the (xsd:string)
    * is optional, by default.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cells must not be empty,<br />
    *    + The target property must be predefined in the ontology,<br />
    *    + No necessary predefined individuals in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating an individual declaration axiom,<br />
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

   /**
    * Test individual data property assertion with input datatype xsd:boolean.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cells must not be empty,<br />
    *    + The target property must be predefined in the ontology,<br />
    *    + No necessary predefined individuals in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating an individual declaration axiom,<br />
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

   /**
    * Test individual data property assertion with input datatype xsd:double.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cells must not be empty,<br />
    *    + The target property must be predefined in the ontology,<br />
    *    + No necessary predefined individuals in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating an individual declaration axiom,<br />
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

   /**
    * Test individual data property assertion with input datatype xsd:float.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cells must not be empty,<br />
    *    + The target property must be predefined in the ontology,<br />
    *    + No necessary predefined individuals in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating an individual declaration axiom,<br />
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

   /**
    * Test individual data property assertion with input datatype xsd:long.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cells must not be empty,<br />
    *    + The target property must be predefined in the ontology,<br />
    *    + No necessary predefined individuals in the ontology.
    * <p>
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

   /**
    * Test individual data property assertion with input datatype xsd:integer.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cells must not be empty,<br />
    *    + The target property must be predefined in the ontology,<br />
    *    + No necessary predefined individuals in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating an individual declaration axiom,<br />
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

      String expression = "Individual: @A1 Facts: hasAge @B1(xsd:integer)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_AGE, FRED, Literal("23", XSD_INTEGER))));
   }

   /**
    * Test individual data property assertion with input datatype xsd:short.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cells must not be empty,<br />
    *    + The target property must be predefined in the ontology,<br />
    *    + No necessary predefined individuals in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating an individual declaration axiom,<br />
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

   /**
    * Test individual data property assertion with input datatype xsd:byte.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cells must not be empty,<br />
    *    + The target property must be predefined in the ontology,<br />
    *    + No necessary predefined individuals in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating an individual declaration axiom,<br />
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

   /**
    * Test individual data property assertion with input datatype xsd:decimal.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cells must not be empty,<br />
    *    + The target property must be predefined in the ontology,<br />
    *    + No necessary predefined individuals in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating an individual declaration axiom,<br />
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

   /**
    * Test individual data property assertion with input datatype xsd:dateTime.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cells must not be empty,<br />
    *    + The target property must be predefined in the ontology,<br />
    *    + No necessary predefined individuals in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating an individual declaration axiom,<br />
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

   /**
    * Test individual data property assertion with input datatype xsd:date.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cells must not be empty,<br />
    *    + The target property must be predefined in the ontology,<br />
    *    + No necessary predefined individuals in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating an individual declaration axiom,<br />
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

   /**
    * Test individual data property assertion with input datatype xsd:time.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cells must not be empty,<br />
    *    + The target property must be predefined in the ontology,<br />
    *    + No necessary predefined individuals in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating an individual declaration axiom,<br />
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

   /**
    * Test (rdfs:label) directive in class declaration. Notice the (mm:camelCaseEncode) directive is added to encode
    * the entity IRI naming.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + No necessary predefined classes in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating a class declaration axiom with name using the (rdfs:label) value, instead of the cell value,<br />
    *    + Creating a rdfs:label annotation using the given (rdfs:label) value.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestRDFSLabelAssignmentInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label=(\"Bayerische Motoren Werke\") mm:camelCaseEncode)";
   
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(BAYERISCHE_MOTOREN_WERKE),
            AnnotationAssertion(RDFS_LABEL, IRI(ONTOLOGY_ID, "BayerischeMotorenWerke"), Literal("Bayerische Motoren Werke", XSD_STRING))
      ));
   }

   /**
    * Test (rdf:id) and (rdfs:label) directives in class declaration.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + No necessary predefined classes in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating a class declaration axiom with name using the (rdf:id) value,<br />
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

   /**
    * Test setting the default label for (rdfs:label) directives in class declaration. Notice the (mm:camelCaseEncode) directive
    * is added to encode the entity IRI naming.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + No necessary predefined classes in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating a class declaration axiom with name using default label, instead of the cell value,<br />
    *    + Creating a rdfs:label annotation using the given (rdfs:label) value.
    */
   @Test
   @Category(NameResolutionTest.class)
   public void TestDefaultLabelInReference() throws Exception
   {
      Label cellA1 = createCell("Some Name", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label mm:DefaultLabel=\"Default Name\" mm:camelCaseEncode)";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(DEFAULT_NAME),
            AnnotationAssertion(RDFS_LABEL, IRI(ONTOLOGY_ID, "DefaultName"), Literal("Default Name", XSD_STRING))
      ));
   }

   /**
    * Test (mm:append) function in class declaration. Notice the (mm:camelCaseEncode) directive is added to encode
    * the entity IRI naming.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + No necessary predefined classes in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating a class declaration axiom with name using the appended value, instead of the cell value,
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestStringAppendInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:append(\" Car\") mm:camelCaseEncode)";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(BMW_CAR)));
   }

   /**
    * Test explicit append function for (rdfs:label) directive in class declaration. Notice the (mm:camelCaseEncode) directive
    * is added to encode the entity IRI naming.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + No necessary predefined classes in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating a class declaration axiom with name using the appended value, instead of the cell value,<br />
    *    + Creating a rdfs:label annotation using the given (rdfs:label) value.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestExplicitStringAppendInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label=mm:append(\" Car\") mm:camelCaseEncode)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(BMW_CAR),
            AnnotationAssertion(RDFS_LABEL, IRI(ONTOLOGY_ID, "BMWCar"), Literal("BMW Car", XSD_STRING))));
   }

   /**
    * Test implicit append function for (rdfs:label) directive in class declaration. Notice the (mm:camelCaseEncode) directive
    * is added to encode the entity IRI naming.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + No necessary predefined classes in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating a class declaration axiom with name using the appended value, instead of the cell value,<br />
    *    + Creating a rdfs:label annotation using the given (rdfs:label) value.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestImplicitStringAppendInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label=(@A1, \" Car\") mm:camelCaseEncode)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(BMW_CAR),
            AnnotationAssertion(RDFS_LABEL, IRI(ONTOLOGY_ID, "BMWCar"), Literal("BMW Car", XSD_STRING))));
   }

   /**
    * Test (mm:prepend) function in class declaration. Notice the (mm:camelCaseEncode) directive is added to encode
    * the entity IRI naming.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + No necessary predefined classes in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating a class declaration axiom with name using the prepended value, instead of the cell value,
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestStringPrependInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:prepend(\"Car \") mm:camelCaseEncode)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR_BMW)));
   }

   /**
    * Test explicit prepend function for (rdfs:label) directive in class declaration. Notice the (mm:camelCaseEncode) directive
    * is added to encode the entity IRI naming.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + No necessary predefined classes in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating a class declaration axiom with name using the prepended value, instead of the cell value,<br />
    *    + Creating a rdfs:label annotation using the given (rdfs:label) value.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestExplicitStringPrependInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label=mm:prepend(\"Car \") mm:camelCaseEncode)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR_BMW),
            AnnotationAssertion(RDFS_LABEL, IRI(ONTOLOGY_ID, "CarBMW"), Literal("Car BMW", XSD_STRING))
      ));
   }

   /**
    * Test implicit prepend function for (rdfs:label) directive in class declaration. Notice the (mm:camelCaseEncode) directive
    * is added to encode the entity IRI naming.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + No necessary predefined classes in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating a class declaration axiom with name using the prepended value, instead of the cell value,<br />
    *    + Creating a rdfs:label annotation using the given (rdfs:label) value.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestImplicitStringPrependInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(rdfs:label=(\"Car \", @A1) mm:camelCaseEncode)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR_BMW),
            AnnotationAssertion(RDFS_LABEL, IRI(ONTOLOGY_ID, "CarBMW"), Literal("Car BMW", XSD_STRING))
      ));
   }

   /**
    * Test (mm:ShiftUp) function.
    * <p>
    * - Precondition:<br />
    *    + At least one of the target sheet cells must not be empty,<br />
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

   /**
    * Test (mm:ShiftDown) function.
    * <p>
    * - Precondition:<br />
    *    + At least one of the target sheet cells must not be empty,<br />
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

   /**
    * Test (mm:ShiftRight) function.
    * <p>
    * - Precondition:<br />
    *    + At least one of the target sheet cells must not be empty,<br />
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

   /**
    * Test (mm:ShiftLeft) function.
    * <p>
    * - Precondition:<br />
    *    + At least one of the target sheet cells must not be empty,<br />
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

   /**
    * Test multiple shift functions and appending the values.
    * <p>
    * - Precondition:<br />
    *    + At least one of the target sheet cells must not be empty,<br />
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

   /**
    * Test (mm:toLowerCase) function.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
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

   /**
    * Test (mm:toUpperCase) function.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
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

   /**
    * Test (mm:reverse) function.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
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

   /**
    * Test (mm:trim) function.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
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

   /**
    * Test (mm:printf) function.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + No necessary predefined classes in the ontology.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestPrintfInReference() throws Exception
   {
      Label cellA1 = createCell("BMW", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:printf(\"%sCar\"))";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(BMW_CAR)));
   }

   /**
    * Test (mm:decimalFormat) function.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + The target data property must be predefined in the ontology,<br />
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

   /**
    * Test (mm:replaceAll) function.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + The target data property must be predefined in the ontology.
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

   /**
    * Test (mm:capturing) function in class declaration.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + No necessary predefined classes in the ontology.
    * <p>
    * - Expected results:<br />
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

   /**
    * Test implicit (mm:capturing) function in class declaration.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + No necessary predefined classes in the ontology.
    * <p>
    * - Expected results:<br />
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

   /**
    * Test (xml:lang) function.
    * <p>
    * - Precondition:<br />
    *    + The target sheet cell must not be empty,<br />
    *    + The target annotation property must not be empty,<br />
    *    + No necessary predefined individuals in the ontology.
    * <p>
    * - Expected results:<br />
    *    + Creating an individual declaration axiom with a language tag in its annotation assertion.
    */
   @Test
   @Category(CellProcessingTest.class)
   public void TestLanguageTagInReference() throws Exception
   {
      declareOWLAnnotationProperty(ontology, "rdfs:comment");

      Label cellA1 = createCell("p1", 1, 1);
      Label cellB1 = createCell("Familia Swiss Muesli Mixed Cereals - 32 oz box", 2, 1);

      Set<Label> cells = createCells(cellA1, cellB1);

      /**
       * Individual: @A1
       * Annotations: skos:prefLabel @B1(xml:lang="en")
       */
      String expression = "Individual: @A1 Annotations: rdfs:comment @B1(xml:lang=\"en\")";
 
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(P1),
            AnnotationAssertion(RDFS_COMMENT, IRI(ONTOLOGY_ID, "p1"), Literal("Familia Swiss Muesli Mixed Cereals - 32 oz box", "en"))));
   }

   /**
    * Test (mm:camelCaseEncode) directive.
    * <p>
    * Expected results:<br />
    *    + Creating a class declaration axiom in CamelCase naming.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestCamelCaseEncodeInReference() throws Exception
   {
      Label cellA1 = createCell("Barbara's Puffins Honey-Rice Cereal - 10.5oz box", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:camelCaseEncode)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(BARBARA_PUFFINS_CAMELCASE)));
   }

   /**
    * Test (mm:snakeCaseEncode) directive.
    * <p>
    * Expected results:<br />
    *    + Creating a class declaration axiom in Snake_Case naming.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestSnakeCaseEncodeInReference() throws Exception
   {
      Label cellA1 = createCell("Barbara's Puffins Honey-Rice Cereal - 10.5oz box", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:snakeCaseEncode)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(BARBARA_PUFFINS_SNAKECASE)));
   }

   /**
    * Test (mm:hashEncode) directive.
    * <p>
    * Expected results:<br />
    *    + Creating a class declaration axiom in Snake_Case naming.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestHashEncodeInReference() throws Exception
   {
      Label cellA1 = createCell("Barbara's Puffins Honey-Rice Cereal - 10.5oz box", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:hashEncode)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(BARBARA_PUFFINS_MD5)));
   }

   /**
    * Test (mm:SkipIfOWLEntityExists) directive.
    * <p>
    * - Precondition:<br />
    *    + The target class exists already in the ontology.
    * <p>
    * - Expected results:<br />
    *    + No class declaration should be created.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestSkipIfOWLEntityExistsInReference() throws Exception
   {
      declareOWLClass(ontology, "Car");

      Label cellA1 = createCell("Car", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:SkipIfOWLEntityExists)";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /**
    * Test (mm:WarningIfOWLEntityExists) directive.
    * <p>
    * - Precondition:<br />
    *    + The target class exists already in the ontology.
    * <p>
    * Expected results:<br />
    *    + No class declaration axiom should be created and a warning log should appear.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestWarningIfOWLEntityExistsInReference() throws Exception
   {
      declareOWLClass(ontology, "Car");

      Label cellA1 = createCell("Car", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:WarningIfOWLEntityExists)";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /**
    * Test (mm:ErrorIfOWLEntityExists) directive.
    * <p>
    * - Precondition:<br />
    *    + The target class exists already in the ontology.
    * <p>
    * Expected results:<br />
    *    + No class declaration axiom should be created and an error should be thrown.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestErrorIfOWLEntityExistsInReference() throws Exception
   {
      thrown.expect(RendererException.class);

      declareOWLClass(ontology, "Car");

      Label cellA1 = createCell("Car", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:ErrorIfOWLEntityExists)";
      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /**
    * Test (mm:ResolveIfOWLEntityExists) directive. This is the default setting for resolving existed values.
    * <p>
    * - Precondition:<br />
    *    + The target class exists already in the ontology.
    * <p>
    * Expected results:<br />
    *    + Creating a class declaration axiom.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestResolveIfOWLEntityExistsInReference() throws Exception
   {
      declareOWLClass(ontology, "Car");

      Label cellA1 = createCell("Car", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Class: @A1(mm:ResolveIfOWLEntityExists)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
   }

   /**
    * Test mismatch entity type when the OWL entity exists already in the ontology.
    * <p>
    * - Precondition:<br />
    *    + The target data property exists already in the ontology.
    * <p>
    * Expected results:<br />
    *    + No individual assertion axiom should be created and an error should be thrown.
    */
   @Test
   @Category(DirectiveTest.class)
   public void TestMismatchOWLEntityExistsInReference() throws Exception
   {
      thrown.expect(RendererException.class);

      declareOWLObjectProperty(ontology, "hasParent");
      declareOWLNamedIndividuals(ontology, "fred", "bob");

      Label cellA1 = createCell("hasParent", 1, 1);
      Set<Label> cells = createCells(cellA1);

      String expression = "Individual: fred Facts: @A1(DataProperty) bob";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, SHEET1, cells, expression, settings);
      assertThat(result.isPresent(), is(false));
   }

   /**
    * Test (mm:SkipIfOWLEntityDoesNotExist) directive.
    * <p>
    * Expected results:<br />
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

   /**
    * Test (mm:WarningIfOWLEntityDoesNotExist) directive.
    * <p>
    * Expected results:<br />
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

   /**
    * Test (mm:ErrorIfOWLEntityDoesNotExist) directive.
    * <p>
    * Expected results:<br />
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

   /**
    * Test (mm:CreateIfOWLEntityDoesNotExist) directive. This is the default setting for resolving reference values.
    * <p>
    * Expected results:<br />
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

   /**
    * Test (mm:ErrorIfEmptyLocation) directive. Note that the sheet cell is empty.
    * <p>
    * Expected results:<br />
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


   /**
    * Test (mm:WarningIfEmptyLocation) directive. Note that the sheet cell is empty.
    * <p>
    * Expected results:<br />
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

   /**
    * Test (mm:ErrorIfEmptyLocation) directive. Note that the sheet cell is empty.
    * <p>
    * Expected results:<br />
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

   /**
    * Test (mm:SkipIfEmptyID) directive. Note that the sheet cell is empty.
    * <p>
    * Expected results:<br />
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


   /**
    * Test (mm:WarningIfEmptyID) directive. Note that the sheet cell is empty.
    * <p>
    * Expected results:<br />
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

   /**
    * Test (mm:ErrorIfEmptyID) directive. Note that the sheet cell is empty.
    * <p>
    * Expected results:<br />
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

   /**
    * Test (mm:ProcessIfEmptyLiteral) directive.
    * <p>
    * Expected results:<br />
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

   /**
    * Test (mm:SkipIfEmptyLiteral) directive.
    * <p>
    * Expected results:<br />
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

   /**
    * Test (mm:WarningIfEmptyLiteral) directive.
    * <p>
    * Expected results:<br />
    *    + Creating an individual declaration but without data property assertion axiom,<br />
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

   /**
    * Test (mm:ErrorIfEmptyLiteral) directive.
    * <p>
    * Expected results:<br />
    *    + No individual declaration and data property assertion axiom should be created,<br />
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

   /**
    * Test (mm:SkipIfEmptyLabel) directive.
    * <p>
    * Expected results:<br />
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

   /**
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

   /**
    * Test (mm:ErrorIfEmptyLabel) directive.
    * <p>
    * Expected results:<br />
    *    + No class declaration and rdfs:label annotation should be created,<br />
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

   /**
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

   /**
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

   /**
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
