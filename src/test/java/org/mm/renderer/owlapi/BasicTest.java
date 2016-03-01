package org.mm.renderer.owlapi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ClassAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataAllValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataExactCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataHasValue;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataMaxCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataMinCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataPropertyAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataSomeValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Datatype;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DifferentIndividuals;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.EquivalentClasses;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.IRI;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.NamedIndividual;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.OWLThing;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectAllValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectComplementOf;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectExactCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectHasValue;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectIntersectionOf;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectMaxCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectMinCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectOneOf;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectSomeValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectUnionOf;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SameIndividual;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SubClassOf;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.mm.core.settings.ReferenceSettings;
import org.mm.core.settings.ValueEncodingSetting;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.ParseException;
import org.mm.renderer.IntegrationTestBase;
import org.mm.renderer.owlapi.AllRenderingTestSuite.ClassExpressionTest;
import org.mm.renderer.owlapi.AllRenderingTestSuite.ClassTest;
import org.mm.renderer.owlapi.AllRenderingTestSuite.NamedIndividualTest;
import org.mm.rendering.owlapi.OWLRendering;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.vocab.Namespaces;

public class BasicTest extends IntegrationTestBase
{
   private OWLOntology ontology;
   private ReferenceSettings settings;

   private static final OWLClass CAR = Class(IRI(ONTOLOGY_ID, "Car"));
   private static final OWLClass CATAMARAN = Class(IRI(ONTOLOGY_ID, "Catamaran"));
   private static final OWLClass VEHICLE = Class(IRI(ONTOLOGY_ID, "Vehicle"));
   private static final OWLClass AUTOMOBILE = Class(IRI(ONTOLOGY_ID, "Automobile"));
   private static final OWLClass AUTO = Class(IRI(ONTOLOGY_ID, "Auto"));
   private static final OWLClass PHYSICIAN = Class(IRI(ONTOLOGY_ID, "Physician"));
   private static final OWLClass CHILD_OF_DOCTOR = Class(IRI(ONTOLOGY_ID, "ChildOfDoctor"));
   private static final OWLClass PERSON = Class(IRI(ONTOLOGY_ID, "Person"));
   private static final OWLClass HUMAN = Class(IRI(ONTOLOGY_ID, "Human"));
   private static final OWLClass A = Class(IRI(ONTOLOGY_ID, "A"));
   private static final OWLObjectProperty HAS_ENGINE = ObjectProperty(IRI(ONTOLOGY_ID, "hasEngine"));
   private static final OWLObjectProperty HAS_HULL = ObjectProperty(IRI(ONTOLOGY_ID, "hasHull"));
   private static final OWLObjectProperty HAS_PARENT = ObjectProperty(IRI(ONTOLOGY_ID, "hasParent"));
   private static final OWLObjectProperty HAS_GENDER = ObjectProperty(IRI(ONTOLOGY_ID, "hasGender"));
   private static final OWLObjectProperty HAS_P1 = ObjectProperty(IRI(ONTOLOGY_ID, "hasP1"));
   private static final OWLObjectProperty HAS_P2 = ObjectProperty(IRI(ONTOLOGY_ID, "hasP2"));
   private static final OWLDataProperty HAS_P3 = DataProperty(IRI(ONTOLOGY_ID, "hasP3"));
   private static final OWLDataProperty HAS_P4 = DataProperty(IRI(ONTOLOGY_ID, "hasP4"));
   private static final OWLDataProperty HAS_SSN = DataProperty(IRI(ONTOLOGY_ID, "hasSSN"));
   private static final OWLDataProperty HAS_ORIGIN = DataProperty(IRI(ONTOLOGY_ID, "hasOrigin"));
   private static final OWLDataProperty HAS_NAME = DataProperty(IRI(ONTOLOGY_ID, "hasName"));
   private static final OWLDataProperty HAS_AGE = DataProperty(IRI(ONTOLOGY_ID, "hasAge"));
   private static final OWLNamedIndividual DOUBLE_HULL = NamedIndividual(IRI(ONTOLOGY_ID, "double-hull"));
   private static final OWLNamedIndividual MALE = NamedIndividual(IRI(ONTOLOGY_ID, "male"));
   private static final OWLNamedIndividual FEMALE = NamedIndividual(IRI(ONTOLOGY_ID, "female"));
   private static final OWLNamedIndividual OTHER = NamedIndividual(IRI(ONTOLOGY_ID, "other"));
   private static final OWLNamedIndividual FRED = NamedIndividual(IRI(ONTOLOGY_ID, "fred"));
   private static final OWLNamedIndividual FREDDY = NamedIndividual(IRI(ONTOLOGY_ID, "freddy"));
   private static final OWLNamedIndividual ALFRED = NamedIndividual(IRI(ONTOLOGY_ID, "alfred"));
   private static final OWLNamedIndividual BOB = NamedIndividual(IRI(ONTOLOGY_ID, "bob"));
   private static final OWLNamedIndividual BOBBY = NamedIndividual(IRI(ONTOLOGY_ID, "bobby"));
   private static final OWLDatatype RDFS_LITERAL = Datatype(IRI(Namespaces.RDFS + "Literal"));
   private static final OWLDatatype XSD_STRING = Datatype(IRI(Namespaces.XSD + "string"));
   private static final OWLDatatype XSD_INTEGER = Datatype(IRI(Namespaces.XSD + "integer"));

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
    * - Precondition: The target class must be predefined in the ontology
    */
   @Test
   @Category(ClassTest.class)
   public void TestClassDeclaration() throws Exception
   {
      declareOWLClasses(ontology, "Car");

      String expression = "Class: Car";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
   }

   /*
    * Test the sub-class axiom.
    * - Precondition: The target classes must be predefined in the ontology
    */
   @Test
   @Category(ClassTest.class)
   public void TestSubClassOf() throws Exception
   {
      declareOWLClasses(ontology, "Car", "Vehicle");

      String expression = "Class: Car SubClassOf: Vehicle";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            SubClassOf(CAR, VEHICLE)));
   }

   /*
    * Test the sub-class expression axiom.
    * - Precondition: The target class must be predefined in the ontology,
    *                 The target property must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestSubClassOfClassExpression() throws Exception
   {
      declareOWLClasses(ontology, "Car");
      declareOWLObjectProperties(ontology, "hasEngine");

      String expression = "Class: Car SubClassOf: hasEngine EXACTLY 1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            SubClassOf(CAR, ObjectExactCardinality(1, HAS_ENGINE, OWLThing()))));
   }

   /*
    * Test multiple sub-class axioms.
    * - Preconditions: The target classes must be predefined in the ontology
    */
   @Test
   @Category(ClassTest.class)
   public void TestMultipleSubClassOf() throws Exception
   {
      declareOWLClasses(ontology, "Car", "Vehicle", "Auto");

      String expression = "Class: Car SubClassOf: Vehicle, Auto";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(3));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            SubClassOf(CAR, VEHICLE),
            SubClassOf(CAR, AUTO)));
   }

   /*
    * Test negated sub-class axiom.
    * - Precondition: The target classes must be predefined in the ontology.
    */
   @Test
   @Category(ClassTest.class)
   public void TestSubClassOfNegatedClass() throws Exception
   {
      declareOWLClasses(ontology, "Car", "Person");

      String expression = "Class: Car SubClassOf: NOT Person";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            SubClassOf(CAR, ObjectComplementOf(PERSON))));
   }

   /*
    * Test class equivalent axiom.
    * - Precondition: The target classes must be predefined in the ontology
    */
   @Test
   @Category(ClassTest.class)
   public void TestEquivalentToClass() throws Exception
   {
      declareOWLClasses(ontology, "Car", "Automobile");

      String expression = "Class: Car EquivalentTo: Automobile";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            EquivalentClasses(CAR, AUTOMOBILE)));
   }

   /*
    * Test multiple class equivalent axioms.
    * - Precondition: The target classes must be predefined in the ontology.
    */
   @Test
   @Category(ClassTest.class)
   public void TestMultipleEquivalentClasses() throws Exception
   {
      declareOWLClasses(ontology, "Car", "Automobile", "Auto");

      String expression = "Class: Car EquivalentTo: Automobile, Auto";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(3));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            EquivalentClasses(CAR, AUTOMOBILE),
            EquivalentClasses(CAR, AUTO)));
   }

   /*
    * Test negated class equivalent axiom.
    * - Precondition: The target classes must be predefined in the ontology.
    */
   @Test
   @Category(ClassTest.class)
   public void TestEquivalentToNegatedClass() throws Exception
   {
      declareOWLClasses(ontology, "Car", "Catamaran");

      String expression = "Class: Car EquivalentTo: NOT Catamaran";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            EquivalentClasses(CAR, ObjectComplementOf(CATAMARAN))));
   }

   /*
    * Test class expression equivalent axiom.
    * - Precondition: The target class and property must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestEquivalentClassExpression() throws Exception
   {
      declareOWLClasses(ontology, "Car");
      declareOWLObjectProperties(ontology, "hasEngine");

      String expression = "Class: Car EquivalentTo: hasEngine EXACTLY 1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            EquivalentClasses(CAR, ObjectExactCardinality(1, HAS_ENGINE, OWLThing()))));
   }

   /*
    * Test object maximum cardinality restriction axiom.
    * - Precondition: The target class and property must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestObjectMaxCardinalityRestriction() throws Exception
   {
      declareOWLClasses(ontology, "Car");
      declareOWLObjectProperties(ontology, "hasEngine");

      String expression = "Class: Car SubClassOf: hasEngine MAX 1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            SubClassOf(CAR, ObjectMaxCardinality(1, HAS_ENGINE, OWLThing()))));
   }

   /*
    * Test object minimum cardinality restriction axiom.
    * - Precondition: The target class and property must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestObjectMinCardinalityRestriction() throws Exception
   {
      declareOWLClasses(ontology, "Car");
      declareOWLObjectProperties(ontology, "hasEngine");

      String expression = "Class: Car SubClassOf: hasEngine MIN 1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            SubClassOf(CAR, ObjectMinCardinality(1, HAS_ENGINE, OWLThing()))));
   }

   /*
    * Test object exact cardinality restriction axiom.
    * - Precondition: The target class and property must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestObjectExactCardinalityRestriction() throws Exception
   {
      declareOWLClasses(ontology, "Car");
      declareOWLObjectProperties(ontology, "hasEngine");

      String expression = "Class: Car SubClassOf: hasEngine EXACTLY 1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            SubClassOf(CAR, ObjectExactCardinality(1, HAS_ENGINE, OWLThing()))));
   }

   /*
    * Test data maximum cardinality restriction axiom.
    * - Precondition: The target class and property must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TesDatatMaxCardinalityRestriction() throws Exception
   {
      declareOWLClasses(ontology, "Car");
      declareOWLDataProperties(ontology, "hasSSN");

      String expression = "Class: Car SubClassOf: hasSSN MAX 1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            SubClassOf(CAR, DataMaxCardinality(1, HAS_SSN, RDFS_LITERAL))));
   }

   /*
    * Test data minimum cardinality restriction axiom.
    * - Precondition: The target class and property must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TesDatatMinCardinalityRestriction() throws Exception
   {
      declareOWLClasses(ontology, "Car");
      declareOWLDataProperties(ontology, "hasSSN");

      String expression = "Class: Car SubClassOf: hasSSN MIN 1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            SubClassOf(CAR, DataMinCardinality(1, HAS_SSN, RDFS_LITERAL))));
   }

   /*
    * Test data exact cardinality restriction axiom.
    * - Precondition: The target class and property must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestDataExactCardinalityRestriction() throws Exception
   {
      declareOWLClasses(ontology, "Car");
      declareOWLDataProperties(ontology, "hasSSN");

      String expression = "Class: Car SubClassOf: hasSSN EXACTLY 1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            SubClassOf(CAR, DataExactCardinality(1, HAS_SSN, RDFS_LITERAL))));
   }

   /*
    * Test object has value restriction axiom.
    * - Precondition: The target class, object property and individual must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestObjectHasValueRestriction() throws Exception
   {
      declareOWLClasses(ontology, "Catamaran");
      declareOWLObjectProperties(ontology, "hasHull");
      declareOWLNamedIndividual(ontology, "double-hull"); // TODO MM must check this entity

      String expression = "Class: Catamaran SubClassOf: hasHull VALUE double-hull";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CATAMARAN),
            SubClassOf(CATAMARAN, ObjectHasValue(HAS_HULL, DOUBLE_HULL))));
   }

   /*
    * Test data has value restriction axiom.
    * - Precondition: The target class and data property must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestDataHasValueRestriction() throws MappingMasterException, IOException, ParseException
   {
      declareOWLClasses(ontology, "Catamaran");
      declareOWLDataProperties(ontology, "hasOrigin");

      String expression = "Class: Catamaran SubClassOf: hasOrigin VALUE \"Germany\"";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CATAMARAN),
            SubClassOf(CATAMARAN, DataHasValue(HAS_ORIGIN, Literal("Germany")))));
   }

   /*
    * Test object some value restriction axiom.
    * - Precondition: The target classes and property must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestObjectSomeValueRestriction() throws Exception
   {
      declareOWLClasses(ontology, "ChildOfDoctor", "Physician");
      declareOWLObjectProperties(ontology, "hasParent");

      String expression = "Class: ChildOfDoctor SubClassOf: hasParent SOME Physician";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CHILD_OF_DOCTOR),
            SubClassOf(CHILD_OF_DOCTOR, ObjectSomeValuesFrom(HAS_PARENT, PHYSICIAN))));
   }

   /*
    * Test data some value restriction axiom.
    * - Precondition: The target classes and property must be predefined in the ontology. In addition,
    *   only a limited set of XSD datatypes is supported (see Wiki page).
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestDataSomeValueRestriction() throws Exception
   {
      declareOWLClasses(ontology, "Car");
      declareOWLDataProperties(ontology, "hasName");

      String expression = "Class: Car SubClassOf: hasName SOME xsd:string";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(CAR),
            SubClassOf(CAR, DataSomeValuesFrom(HAS_NAME, XSD_STRING))));
   }

   /*
    * Test object all values restriction axiom.
    * - Precondition: The target classes and property must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestObjectAllValuesFromRestriction() throws Exception
   {
      declareOWLClasses(ontology, "Person", "Human");
      declareOWLObjectProperties(ontology, "hasParent");

      String expression = "Class: Person SubClassOf: hasParent ONLY Human";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(PERSON),
            SubClassOf(PERSON, ObjectAllValuesFrom(HAS_PARENT, HUMAN))));
   }

   /*
    * Test data all values restriction axiom.
    * - Precondition: The target classes and property must be predefined in the ontology. In addition,
    *   only a limited set of XSD datatypes is supported (see Wiki page).
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestDataAllValuesFromRestriction() throws Exception
   {
      declareOWLClasses(ontology, "Person");
      declareOWLDataProperties(ontology, "hasSSN");

      String expression = "Class: Person SubClassOf: hasSSN ONLY xsd:string";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(PERSON),
            SubClassOf(PERSON, DataAllValuesFrom(HAS_SSN, XSD_STRING))));
   }

   /*
    * Test object one of restriction axiom.
    * - Precondition: The target classes, object property and individuals must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestOWLObjectOneOf() throws Exception
   {
      declareOWLClasses(ontology, "Person");
      declareOWLObjectProperties(ontology, "hasGender");
      declareOWLNamedIndividual(ontology, "male");
      declareOWLNamedIndividual(ontology, "female");
      declareOWLNamedIndividual(ontology, "other"); // TODO MM must check this entities in the ontology

      String expression = "Class: Person SubClassOf: hasGender ONLY {male, female, other}";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(PERSON),
            SubClassOf(PERSON, ObjectAllValuesFrom(HAS_GENDER, ObjectOneOf(MALE, FEMALE, OTHER)))));
   }

   /*
    * Test sub-class to negated class expression axiom.
    * - Precondition: The target class and property must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestSubClassNegatedClassExpression() throws Exception
   {
      declareOWLClasses(ontology, "A");
      declareOWLObjectProperties(ontology, "hasP1");

      String expression = "Class: A SubClassOf: NOT hasP1 EXACTLY 1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(A),
            SubClassOf(A, ObjectComplementOf(ObjectExactCardinality(1, HAS_P1, OWLThing())))));
   }

   /*
    * Test sub-class to union of class expressions axiom.
    * - Precondition: The target class and properties must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestSubClassOfUnionClassExpression() throws Exception
   {
      declareOWLClasses(ontology, "A");
      declareOWLObjectProperties(ontology, "hasP1", "hasP2");

      String expression = "Class: A SubClassOf: hasP1 EXACTLY 2 OR hasP2 EXACTLY 3";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(A),
            SubClassOf(A, ObjectUnionOf(
                  ObjectExactCardinality(2, HAS_P1, OWLThing()),
                  ObjectExactCardinality(3, HAS_P2, OWLThing())))));
   }

   /*
    * Test sub-class to intersection of class expressions axiom.
    * - Precondition: The target class and properties must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestSubClassOfIntersectionClassExpression() throws Exception
   {
      declareOWLClasses(ontology, "A");
      declareOWLObjectProperties(ontology, "hasP1", "hasP2");

      String expression = "Class: A SubClassOf: hasP1 EXACTLY 2 AND hasP2 EXACTLY 3";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(A),
            SubClassOf(A, ObjectIntersectionOf(
                  ObjectExactCardinality(2, HAS_P1, OWLThing()),
                  ObjectExactCardinality(3, HAS_P2, OWLThing())))));
   }

   /*
    * Test sub-class to complex boolean of class expressions axiom.
    * - Precondition: The target class and properties must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestSubClassOfComplexBooleanClassExpression() throws Exception
   {
      declareOWLClasses(ontology, "A");
      declareOWLObjectProperties(ontology, "hasP1", "hasP2");
      declareOWLDataProperties(ontology, "hasP3", "hasP4");

      String expression = "Class: A SubClassOf: NOT hasP1 EXACTLY 2 AND ((hasP2 EXACTLY 3 AND hasP4 MAX 5) OR hasP3 MIN 4)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(A),
            SubClassOf(A,
                  ObjectIntersectionOf(
                        ObjectComplementOf(ObjectExactCardinality(2, HAS_P1, OWLThing())),
                        ObjectUnionOf(ObjectIntersectionOf(
                              ObjectExactCardinality(3, HAS_P2, OWLThing()),
                              DataMaxCardinality(5, HAS_P4, RDFS_LITERAL)),
                        DataMinCardinality(4, HAS_P3, RDFS_LITERAL))))));
   }

   /*
    * Test negated class equivalent to class expression axiom.
    * - Precondition: The target class and property must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestEquivalentToNegatedClassExpression() throws Exception
   {
      declareOWLClasses(ontology, "A");
      declareOWLObjectProperties(ontology, "hasP1");

      String expression = "Class: A EquivalentTo: NOT hasP1 EXACTLY 1";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(A),
            EquivalentClasses(A, ObjectComplementOf(ObjectExactCardinality(1, HAS_P1, OWLThing())))));
   }

   /*
    * Test class equivalent to union of class expressions axiom.
    * - Precondition: The target class and properties must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestEquivalentToUnionClassExpression() throws Exception
   {
      declareOWLClasses(ontology, "A");
      declareOWLObjectProperties(ontology, "hasP1", "hasP2"); // TODO Check the error message

      String expression = "Class: A EquivalentTo: hasP1 EXACTLY 2 OR hasP2 EXACTLY 3";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(A),
            EquivalentClasses(A, ObjectUnionOf(
                  ObjectExactCardinality(2, HAS_P1, OWLThing()),
                  ObjectExactCardinality(3, HAS_P2, OWLThing())))));
   }

   /*
    * Test class equivalent to intersection of class expressions axiom.
    * - Precondition: The target class and properties must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestEquivalentToIntersectionClassExpression() throws Exception
   {
      declareOWLClasses(ontology, "A");
      declareOWLObjectProperties(ontology, "hasP1", "hasP2");

      String expression = "Class: A EquivalentTo: hasP1 EXACTLY 2 AND hasP2 EXACTLY 3";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(A),
            EquivalentClasses(A, ObjectIntersectionOf(
                  ObjectExactCardinality(2, HAS_P1, OWLThing()),
                  ObjectExactCardinality(3, HAS_P2, OWLThing())))));
   }

   /*
    * Test class equivalent to complex boolean class expressions axiom.
    * - Precondition: The target class and properties must be predefined in the ontology.
    */
   @Test
   @Category(ClassExpressionTest.class)
   public void TestEquivalentToComplexBooleanClassExpression() throws Exception
   {
      declareOWLClasses(ontology, "A");
      declareOWLObjectProperties(ontology, "hasP1", "hasP2");
      declareOWLDataProperties(ontology, "hasP3", "hasP4");

      String expression = "Class: A EquivalentTo: NOT hasP1 EXACTLY 2 AND ((hasP2 EXACTLY 3 AND hasP4 MAX 5) OR hasP3 MIN 4)";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(A),
            EquivalentClasses(A,
                  ObjectIntersectionOf(
                        ObjectComplementOf(ObjectExactCardinality(2, HAS_P1, OWLThing())),
                        ObjectUnionOf(ObjectIntersectionOf(
                              ObjectExactCardinality(3, HAS_P2, OWLThing()),
                              DataMaxCardinality(5, HAS_P4, RDFS_LITERAL)),
                        DataMinCardinality(4, HAS_P3, RDFS_LITERAL))))));
   }

   /*
    * Test named individual declaration
    * - Precondition: The target individual must be predefined in the ontology.
    */
   @Test
   @Category(NamedIndividualTest.class)
   public void TestIndividualDeclaration() throws Exception
   {
      declareOWLNamedIndividual(ontology, "fred"); // TODO MM must check the individual

      String expression = "Individual: fred";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(1));
      assertThat(axioms, containsInAnyOrder(Declaration(FRED)));
   }

   /*
    * Test named individual declaration with specified type.
    * - Precondition: The target individual and class must be predefined in the ontology.
    */
   @Test
   @Category(NamedIndividualTest.class)
   public void TestIndividualDeclarationWithTypes() throws Exception
   {
      declareOWLClasses(ontology, "Person");
      declareOWLNamedIndividual(ontology, "fred"); // TODO MM must check the individual

      String expression = "Individual: fred Types: Person";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            ClassAssertion(PERSON, FRED)));
   }

   /*
    * Test named individual declaration with multiple specified types.
    * - Precondition: The target individual, classes and property must be predefined in the ontology.
    */
   @Test
   @Category(NamedIndividualTest.class)
   public void TestIndividualDeclarationWithMultipleTypes() throws Exception
   {
      declareOWLClasses(ontology, "Person", "Human");
      declareOWLObjectProperty(ontology, "hasParent");
      declareOWLNamedIndividual(ontology, "fred"); // TODO MM must check the individual

      String expression = "Individual: fred Types: Person, hasParent ONLY Human";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(3));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            ClassAssertion(PERSON, FRED),
            ClassAssertion(ObjectAllValuesFrom(HAS_PARENT, HUMAN), FRED)));
   }

   /*
    * Test named individual declaration with fact.
    * - Precondition: The target individual and property must be predefined in the ontology.
    */
   @Test
   @Category(NamedIndividualTest.class)
   public void TestIndividualDeclarationWithFact() throws Exception
   {
      declareOWLDataProperty(ontology, "hasName");
      declareOWLNamedIndividual(ontology, "fred"); // TODO MM must check the individual

      String expression = "Individual: fred Facts: hasName \"Fred\"";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_NAME, FRED, Literal("Fred", XSD_STRING))));
   }

   /*
    * Test named individual declaration with multiple facts.
    * - Precondition: The target individual and properties must be predefined in the ontology.
    */
   @Test
   @Category(NamedIndividualTest.class)
   public void TestIndividualDeclarationWithMultipleFacts() throws Exception
   {
      declareOWLDataProperties(ontology, "hasName", "hasAge");
      declareOWLNamedIndividual(ontology, "fred"); // TODO MM must check the individual

      String expression = "Individual: fred Facts: hasName \"Fred\", hasAge 23";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(3));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DataPropertyAssertion(HAS_NAME, FRED, Literal("Fred", XSD_STRING)),
            DataPropertyAssertion(HAS_AGE, FRED, Literal("23", XSD_INTEGER))));
   }

   /*
    * Test same as named individual axiom.
    * - Precondition: The target individuals must be predefined in the ontology.
    */
   @Test
   @Category(NamedIndividualTest.class)
   public void TestIndividualDeclarationWithSameIndividual() throws Exception
   {
      declareOWLNamedIndividual(ontology, "fred", "freddy"); // TODO MM must check the individual

      String expression = "Individual: fred SameAs: freddy";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            SameIndividual(FRED, FREDDY)));
   }

   /*
    * Test same as multiple named individuals axiom.
    * - Precondition: The target individuals must be predefined in the ontology.
    */
   @Test
   @Category(NamedIndividualTest.class)
   public void TestIndividualDeclarationWithMultipleSameIndividual() throws Exception
   {
      declareOWLNamedIndividuals(ontology, "fred", "freddy", "alfred"); // TODO MM must check the individual

      String expression = "Individual: fred SameAs: freddy, alfred";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(3));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            SameIndividual(FRED, FREDDY),
            SameIndividual(FRED, ALFRED)));
   }

   /*
    * Test different from named individual axiom.
    * - Precondition: The target individuals must be predefined in the ontology.
    */
   @Test
   @Category(NamedIndividualTest.class)
   public void TestIndividualDeclarationWithDifferentIndividuals() throws Exception
   {
      declareOWLNamedIndividual(ontology, "fred", "bob"); // TODO MM must check the individual

      String expression = "Individual: fred DifferentFrom: bob";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(2));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DifferentIndividuals(FRED, BOB)));
   }

   /*
    * Test different from named individual axiom.
    * - Precondition: The target individuals must be predefined in the ontology.
    */
   @Test
   @Category(NamedIndividualTest.class)
   public void TestIndividualDeclarationWithMultipleDifferentIndividuals() throws Exception
   {
      declareOWLNamedIndividuals(ontology, "fred", "bob", "bobby"); // TODO MM must check the individual

      String expression = "Individual: fred DifferentFrom: bob, bobby";

      Optional<? extends OWLRendering> result = createOWLAPIRendering(ontology, expression, settings);
      assertThat(result.isPresent(), is(true));

      Set<OWLAxiom> axioms = result.get().getOWLAxioms();
      assertThat(axioms, hasSize(3));
      assertThat(axioms, containsInAnyOrder(
            Declaration(FRED),
            DifferentIndividuals(FRED, BOB),
            DifferentIndividuals(FRED, BOBBY)));
   }
}
