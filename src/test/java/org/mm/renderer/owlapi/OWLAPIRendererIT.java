package org.mm.renderer.owlapi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataAllValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataExactCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataHasValue;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataMinCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataSomeValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Datatype;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.EquivalentClasses;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.IRI;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.NamedIndividual;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.OWLThing;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectAllValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectExactCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectHasValue;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectMaxCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectSomeValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SubClassOf;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.ParseException;
import org.mm.rendering.owlapi.OWLAPIRendering;
import org.mm.test.IntegrationTestBase;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.vocab.Namespaces;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;

public class OWLAPIRendererIT extends IntegrationTestBase
{
	private OWLOntology ontology;

	private static final OWLClass BMW = Class(IRI("BMW"));
	private static final OWLClass CAR = Class(IRI("Car"));
	private static final OWLClass CATAMARAN = Class(IRI("Catamaran"));
	private static final OWLClass VEHICLE = Class(IRI("Vehicle"));
	private static final OWLClass DEVICE = Class(IRI("Device"));
	private static final OWLClass AUTOMOBILE = Class(IRI("Automobile"));
	private static final OWLClass AUTO = Class(IRI("Auto"));
	private static final OWLClass PHYSICIAN = Class(IRI("Physician"));
	private static final OWLClass CHILD_OF_DOCTOR = Class(IRI("ChildOfDoctor"));
	private static final OWLClass PERSON = Class(IRI("Person"));
	private static final OWLClass HUMAN = Class(IRI("Human"));
	private static final OWLObjectProperty HAS_ENGINE = ObjectProperty(IRI("hasEngine"));
	private static final OWLObjectProperty HAS_HULL = ObjectProperty(IRI("hasHull"));
	private static final OWLObjectProperty HAS_PARENT = ObjectProperty(IRI("hasParent"));
	private static final OWLDataProperty HAS_SSN = DataProperty(IRI("hasSSN"));
	private static final OWLDataProperty HAS_ORIGIN = DataProperty(IRI("hasOrigin"));
	private static final OWLDataProperty HAS_NAME = DataProperty(IRI("hasName"));
	private static final OWLIndividual DOUBLE_HULL = NamedIndividual(IRI("double-hull"));
	
	private static final OWLAnnotationSubject CAR_ANNOTATION = IRI("Car");
	private static final OWLAnnotationProperty HAS_AUTHOR_ANNOTATION = AnnotationProperty(IRI("hasAuthor"));
	private static final OWLAnnotationProperty HAS_DATE_ANNOTATION = AnnotationProperty(IRI("hasDate"));
	private static final OWLAnnotationValue IRI_VALUE = IRI("Bob");
	private static final OWLAnnotationValue DATE_VALUE = Literal("1990-10-10");
	private static final OWLLiteral GERMANY_LITERAL = Literal("Germany");
	
	private static final OWLDatatype RDFS_LITERAL = Datatype(IRI(Namespaces.RDFS + "Literal"));
	private static final OWLDatatype XSD_STRING = Datatype(IRI(Namespaces.XSD + "string"));
	
	private static final OWLObjectExactCardinality HAS_ENGINE_EXACT = ObjectExactCardinality(1, HAS_ENGINE, OWLThing());
	private static final OWLObjectMaxCardinality HAS_ENGINE_MAX = ObjectMaxCardinality(1, HAS_ENGINE, OWLThing());
	private static final OWLDataMinCardinality HAS_SSN_MIN = DataMinCardinality (1, HAS_SSN, RDFS_LITERAL);
	private static final OWLDataExactCardinality HAS_SSN_EXACT = DataExactCardinality(1, HAS_SSN, RDFS_LITERAL);
	
	private static final OWLObjectHasValue HAS_HAUL_HAS_VALUE = ObjectHasValue(HAS_HULL, DOUBLE_HULL);
	private static final OWLDataHasValue HAS_ORIGIN_HAS_VALUE = DataHasValue(HAS_ORIGIN, GERMANY_LITERAL);
	private static final OWLObjectSomeValuesFrom HAS_PARENT_SOME_VALUE = ObjectSomeValuesFrom(HAS_PARENT, PHYSICIAN);
	private static final OWLDataSomeValuesFrom HAS_NAME_SOME_VALUE = DataSomeValuesFrom(HAS_NAME, XSD_STRING);
	private static final OWLObjectAllValuesFrom HAS_PARENT_ALL_VALUE = ObjectAllValuesFrom(HAS_PARENT, HUMAN);
	private static final OWLDataAllValuesFrom HAS_SSN_ALL_VALUE = DataAllValuesFrom(HAS_SSN, XSD_STRING);
	
	private static final OWLAxiom BMW_DECLARATION = Declaration(BMW);
	private static final OWLAxiom CAR_DECLARATION = Declaration(CAR);
	private static final OWLAxiom CATAMARAN_DECLARATION = Declaration(CATAMARAN);
	private static final OWLAxiom CHILD_OF_DOCTOR_DECLARATION = Declaration(CHILD_OF_DOCTOR);
	private static final OWLAxiom PERSON_DECLARATION = Declaration(PERSON);
	private static final OWLAxiom CAR_SUBCLASS_VEHICLE = SubClassOf(CAR, VEHICLE);
	private static final OWLAxiom CAR_SUBCLASS_DEVICE = SubClassOf(CAR, DEVICE);
	private static final OWLAxiom CAR_SUBCLASS_MAX = SubClassOf(CAR, HAS_ENGINE_MAX);
	private static final OWLAxiom CAR_SUBCLASS_MIN = SubClassOf(CAR, HAS_SSN_MIN);
	private static final OWLAxiom CAR_SUBCLASS_EXACT = SubClassOf(CAR, HAS_SSN_EXACT);
	private static final OWLAxiom CATAMARAN_SUBCLASS_HAS_VALUE = SubClassOf(CATAMARAN, HAS_HAUL_HAS_VALUE);
	private static final OWLAxiom BMW_SUBCLASS_HAS_VALUE = SubClassOf(BMW, HAS_ORIGIN_HAS_VALUE);
	private static final OWLAxiom CHILD_OF_DOCTOR_SUBCLASS_OBJECT_SOME_VALUE = SubClassOf(CHILD_OF_DOCTOR, HAS_PARENT_SOME_VALUE);
	private static final OWLAxiom CAR_SUBCLASS_DATA_SOME_VALUE = SubClassOf(CAR, HAS_NAME_SOME_VALUE);
	private static final OWLAxiom PERSON_SUBCLASS_OBJECT_ALL_VALUE = SubClassOf(PERSON, HAS_PARENT_ALL_VALUE);
	private static final OWLAxiom PERSON_SUBCLASS_DATA_ALL_VALUE = SubClassOf(PERSON, HAS_SSN_ALL_VALUE);
	private static final OWLAxiom CAR_EQUIVALENT_AUTOMOBILE = EquivalentClasses(CAR, AUTOMOBILE);
	private static final OWLAxiom CAR_EQUIVALENT_AUTO = EquivalentClasses(CAR, AUTO);
	
	private static final OWLAnnotationAssertionAxiom CAR_ANNOTATION_IRI = AnnotationAssertion(HAS_AUTHOR_ANNOTATION, CAR_ANNOTATION, IRI_VALUE);
	private static final OWLAnnotationAssertionAxiom CAR_ANNOTATION_DATE = AnnotationAssertion(HAS_DATE_ANNOTATION, CAR_ANNOTATION, DATE_VALUE);

	@Before
	public void setUp() throws OWLOntologyCreationException
	{
		ontology = createOWLOntology();
	}

	@Test
	public void TestClassDeclaration()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car");
		String expression = "Class: Car";

		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		assertThat(axioms, containsInAnyOrder(CAR_DECLARATION));
	}

	@Test
	public void TestSubClassOf()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car", "Vehicle");
		String expression = "Class: Car SubClassOf: Vehicle";

		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(CAR_DECLARATION, CAR_SUBCLASS_VEHICLE));
	}

	@Test
	public void TestMultipleSubClassOf()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car", "Vehicle", "Device");
		String expression = "Class: Car SubClassOf: Vehicle, Device";

		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(3));
		assertThat(axioms, containsInAnyOrder(CAR_DECLARATION, CAR_SUBCLASS_VEHICLE, CAR_SUBCLASS_DEVICE));
	}

	@Test
	public void TestEquivalentToClass()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car", "Automobile");
		String expression = "Class: Car EquivalentTo: Automobile";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));

		assertThat(axioms, containsInAnyOrder(CAR_DECLARATION, CAR_EQUIVALENT_AUTOMOBILE));
	}

	@Test
	public void TestEquivalentToClassExpression()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car");
		declareOWLObjectProperties(ontology, "hasEngine");
		String expression = "Class: Car EquivalentTo: hasEngine EXACTLY 1";

		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));

		assertThat(axioms, containsInAnyOrder(CAR_DECLARATION, HAS_ENGINE_EXACT));
	}

	@Test
	public void TestMultipleEquivalentClass()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car", "Automobile", "Auto");
		declareOWLObjectProperties(ontology, "hasEngine");
		String expression = "Class: Car EquivalentTo: Automobile, Auto, hasEngine EXACTLY 1";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(4));
		assertThat(axioms, containsInAnyOrder(CAR_DECLARATION, CAR_EQUIVALENT_AUTOMOBILE, CAR_EQUIVALENT_AUTO, HAS_ENGINE_EXACT));
	}

	@Test
	public void TestClassDeclarationWithAnnotations()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car");
		declareOWLAnnotationProperties(ontology, "hasAuthor");
		String expression = "Class: Car Annotations: hasAuthor Bob";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(CAR_DECLARATION, CAR_ANNOTATION_IRI));
	}

	@Test
	public void TestClassDeclarationWithMultipleAnnotations()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car");
		declareOWLAnnotationProperties(ontology, "hasAuthor", "hasDate");
		String expression = "Class: Car Annotations: hasAuthor Bob, hasDate \"1990-10-10\"";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(3));
		assertThat(axioms, containsInAnyOrder(CAR_DECLARATION, CAR_ANNOTATION_IRI, CAR_ANNOTATION_DATE));
	}

	@Test
	public void TestMaxCardinalityRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car");
		declareOWLObjectProperties(ontology, "hasEngine");
		String expression = "Class: Car SubClassOf: hasEngine MAX 1";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(CAR_DECLARATION, CAR_SUBCLASS_MAX));
	}

	@Test
	public void TestMinCardinalityRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car");
		declareOWLDataProperties(ontology, "hasAuthor", "hasSSN");
		String expression = "Class: Car SubClassOf: hasSSN MIN 1";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(CAR_DECLARATION, CAR_SUBCLASS_MIN));
	}

	@Test
	public void TestExactCardinalityRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car");
		declareOWLDataProperties(ontology, "hasAuthor", "hasSSN");
		String expression = "Class: Car SubClassOf: hasSSN EXACTLY 1";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(CAR_DECLARATION, CAR_SUBCLASS_EXACT));
	}

	@Test
	public void TestObjectHasValueRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Catamaran", "DoubleHull");
		declareOWLObjectProperties(ontology, "hasHull");
		declareOWLNamedIndividual(ontology, "double-hull");
		String expression = "Class: Catamaran SubClassOf: hasHull VALUE double-hull";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(CATAMARAN_DECLARATION, CATAMARAN_SUBCLASS_HAS_VALUE));
	}

	@Test
	public void TestDataHasValueRestriction()
			throws WriteException, BiffException, MappingMasterException, IOException, ParseException
	{
		declareOWLClasses(ontology, "BMW");
		declareOWLDataProperties(ontology, "hasOrigin");
		String expression = "Class: BMW SubClassOf: hasOrigin VALUE \"Germany\"";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(BMW_DECLARATION, BMW_SUBCLASS_HAS_VALUE));
	}

	@Test
	public void TestObjectSomeValueRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "ChildOfDoctor", "Physician");
		declareOWLObjectProperties(ontology, "hasParent");
		String expression = "Class: ChildOfDoctor SubClassOf: hasParent SOME Physician";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(CHILD_OF_DOCTOR_DECLARATION, CHILD_OF_DOCTOR_SUBCLASS_OBJECT_SOME_VALUE));
	}

	@Test
	public void TestDataSomeValueRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car");
		declareOWLDataProperties(ontology, "hasName");
		String expression = "Class: Car SubClassOf: hasName SOME xsd:string";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(CAR_DECLARATION, CAR_SUBCLASS_DATA_SOME_VALUE));
	}

	@Test
	public void TestObjectAllValuesFromRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Person", "Human");
		declareOWLObjectProperties(ontology, "hasParent");
		String expression = "Class: Person SubClassOf: hasParent ONLY Human";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(PERSON_DECLARATION, PERSON_SUBCLASS_OBJECT_ALL_VALUE));
	}

	@Test
	public void TestDataAllValuesFromRestriction()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Person");
		declareOWLDataProperties(ontology, "hasSSN");
		String expression = "Class: Person SubClassOf: hasSSN ONLY xsd:string";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(PERSON_DECLARATION, PERSON_SUBCLASS_DATA_ALL_VALUE));
	}

	@Test
	public void TestAbsoluteReference()
			throws OWLOntologyCreationException, WriteException, MappingMasterException, ParseException, IOException
	{
		OWLOntology ontology = createOWLOntology();
		String expression = "Class: @A1";
		// Optional<? extends OWLAPIRendering> owlapiRendering =
		// createOWLAPIRendering(ontology, SHEET1, cells, expression);

		// Assert.assertTrue(owlapiRendering.isPresent());
		// Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		// TODO Test that we have the expected declaration axiom for the class
		// Car
	}

	// TODO Different rdfs:label and rdf:id, e.g., Class: @A5(rdf:ID=@B5
	// rdfs:label=@A5)
	// TODO Tests for the following directives:
	// mm:Location, mm:Prefix, mm:Namespace
	// mm:ResolveIfOWLEntityExists, mm:SkipIfOWLEntityExists,
	// mm:WarningIfOWLEntityExists,
	// mm:ErrorIfOWLEntityExists, mm:CreateIfOWLEntityDoesNotExist,
	// mm:SkipIfOWLEntityDoesNotExist,
	// mm:WarningIfOWLEntityDoesNotExist, mm:ErrorIfOWLEntityDoesNotExist
}
