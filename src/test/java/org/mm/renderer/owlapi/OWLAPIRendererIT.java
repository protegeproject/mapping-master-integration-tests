package org.mm.renderer.owlapi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ClassAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataAllValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataExactCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataHasValue;
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
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectOneOf;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectPropertyAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectSomeValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectUnionOf;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SameIndividual;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SubClassOf;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.ParseException;
import org.mm.rendering.owlapi.OWLAPIRendering;
import org.mm.rendering.text.TextRendering;
import org.mm.test.IntegrationTestBase;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.vocab.Namespaces;

import junit.framework.Assert;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WriteException;

public class OWLAPIRendererIT extends IntegrationTestBase
{
	private OWLOntology ontology;

	private static final OWLClass BMW = Class(IRI("BMW"));
	private static final OWLClass CAR = Class(IRI("Car"));
	private static final OWLClass BIG_CAR = Class(IRI("BigCar"));
	private static final OWLClass CAR_BIG = Class(IRI("CarBig"));
	private static final OWLClass CATAMARAN = Class(IRI("Catamaran"));
	private static final OWLClass VEHICLE = Class(IRI("Vehicle"));
	private static final OWLClass DEVICE = Class(IRI("Device"));
	private static final OWLClass AUTOMOBILE = Class(IRI("Automobile"));
	private static final OWLClass AUTO = Class(IRI("Auto"));
	private static final OWLClass PHYSICIAN = Class(IRI("Physician"));
	private static final OWLClass CHILD_OF_DOCTOR = Class(IRI("ChildOfDoctor"));
	private static final OWLClass PERSON = Class(IRI("Person"));
	private static final OWLClass HUMAN = Class(IRI("Human"));
	private static final OWLClass A = Class(IRI("A"));
	private static final OWLObjectProperty HAS_ENGINE = ObjectProperty(IRI("hasEngine"));
	private static final OWLObjectProperty HAS_HULL = ObjectProperty(IRI("hasHull"));
	private static final OWLObjectProperty HAS_PARENT = ObjectProperty(IRI("hasParent"));
	private static final OWLObjectProperty HAS_GENDER = ObjectProperty(IRI("hasGender"));
	private static final OWLObjectProperty HAS_P1 = ObjectProperty(IRI("hasP1"));
	private static final OWLObjectProperty HAS_P2 = ObjectProperty(IRI("hasP2"));
	private static final OWLDataProperty HAS_SSN = DataProperty(IRI("hasSSN"));
	private static final OWLDataProperty HAS_ORIGIN = DataProperty(IRI("hasOrigin"));
	private static final OWLDataProperty HAS_NAME = DataProperty(IRI("hasName"));
	private static final OWLDataProperty HAS_AGE = DataProperty(IRI("hasAge"));
	private static final OWLDataProperty HAS_SALARY = DataProperty(IRI("hasSalary"));
	private static final OWLDataProperty HAS_DOB = DataProperty(IRI("hasDOB"));
	private static final OWLDataProperty HAS_BEDTIME = DataProperty(IRI("hasBedTime"));
	private static final OWLNamedIndividual DOUBLE_HULL = NamedIndividual(IRI("double-hull"));
	private static final OWLNamedIndividual MALE = NamedIndividual(IRI("male"));
	private static final OWLNamedIndividual FEMALE = NamedIndividual(IRI("female"));
	private static final OWLNamedIndividual OTHER = NamedIndividual(IRI("other"));
	private static final OWLNamedIndividual FRED = NamedIndividual(IRI("Fred"));
	private static final OWLNamedIndividual FREDDY = NamedIndividual(IRI("Freddy"));
	private static final OWLNamedIndividual F = NamedIndividual(IRI("F"));
	private static final OWLNamedIndividual BOB = NamedIndividual(IRI("Bob"));
	private static final OWLNamedIndividual BOBBY = NamedIndividual(IRI("Bobby"));
	private static final OWLAnnotationSubject CAR_ANNOTATION = IRI("Car");
	private static final OWLAnnotationSubject FRED_ANNOTATION = IRI("Fred");
	private static final OWLAnnotationProperty HAS_AUTHOR_ANNOTATION = AnnotationProperty(IRI("hasAuthor"));
	private static final OWLAnnotationProperty HAS_DATE_ANNOTATION = AnnotationProperty(IRI("hasDate"));
	private static final OWLAnnotationProperty HAS_NAME_ANNOTATION = AnnotationProperty(IRI("hasName"));
	private static final OWLAnnotationProperty HAS_AGE_ANNOTATION = AnnotationProperty(IRI("hasAge"));
	private static final OWLDatatype RDFS_LITERAL = Datatype(IRI(Namespaces.RDFS + "Literal"));
	private static final OWLDatatype XSD_STRING = Datatype(IRI(Namespaces.XSD + "string"));
	private static final OWLDatatype XSD_FLOAT = Datatype(IRI(Namespaces.XSD + "float"));
	private static final OWLDatatype XSD_INT = Datatype(IRI(Namespaces.XSD + "int"));
	private static final OWLDatatype XSD_BYTE = Datatype(IRI(Namespaces.XSD + "byte"));
	private static final OWLDatatype XSD_SHORT = Datatype(IRI(Namespaces.XSD + "short"));
	private static final OWLDatatype XSD_DATE = Datatype(IRI(Namespaces.XSD + "date"));
	private static final OWLDatatype XSD_DATETIME = Datatype(IRI(Namespaces.XSD + "dateTime"));
	private static final OWLDatatype XSD_TIME = Datatype(IRI(Namespaces.XSD + "time"));

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
		assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
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
		assertThat(axioms, containsInAnyOrder(
				Declaration(CAR),
				SubClassOf(CAR, VEHICLE)
		));
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
		assertThat(axioms, containsInAnyOrder(
				Declaration(CAR),
				SubClassOf(CAR, VEHICLE),
				SubClassOf(CAR, DEVICE)
		));
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

		assertThat(axioms, containsInAnyOrder(
				Declaration(CAR),
				EquivalentClasses(CAR, AUTOMOBILE)
		));
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

		assertThat(axioms, containsInAnyOrder(
				Declaration(CAR),
				EquivalentClasses(CAR, ObjectExactCardinality(1, HAS_ENGINE, OWLThing()))
		));
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
		assertThat(axioms, containsInAnyOrder(
				Declaration(CAR),
				EquivalentClasses(CAR, AUTOMOBILE),
				EquivalentClasses(CAR, AUTO),
				EquivalentClasses(CAR, ObjectExactCardinality(1, HAS_ENGINE, OWLThing()))
		));
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
		assertThat(axioms, containsInAnyOrder(
				Declaration(CAR),
				AnnotationAssertion(HAS_AUTHOR_ANNOTATION, CAR_ANNOTATION, IRI("Bob"))
		));
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
		assertThat(axioms, containsInAnyOrder(Declaration(CAR),
				AnnotationAssertion(HAS_AUTHOR_ANNOTATION, CAR_ANNOTATION, IRI("Bob")),
				AnnotationAssertion(HAS_DATE_ANNOTATION, CAR_ANNOTATION, Literal("1990-10-10"))
		));
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
		assertThat(axioms, containsInAnyOrder(
				Declaration(CAR),
				SubClassOf(CAR, ObjectMaxCardinality(1, HAS_ENGINE, OWLThing()))
		));
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
		assertThat(axioms, containsInAnyOrder(
				Declaration(CAR),
				SubClassOf(CAR, DataMinCardinality (1, HAS_SSN, RDFS_LITERAL))
		));
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
		assertThat(axioms, containsInAnyOrder(
				Declaration(CAR),
				SubClassOf(CAR, DataExactCardinality(1, HAS_SSN, RDFS_LITERAL))
		));
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
		assertThat(axioms, containsInAnyOrder(
				Declaration(CATAMARAN),
				SubClassOf(CATAMARAN, ObjectHasValue(HAS_HULL, DOUBLE_HULL))
		));
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
		assertThat(axioms, containsInAnyOrder(
				Declaration(BMW),
				SubClassOf(BMW, DataHasValue(HAS_ORIGIN, Literal("Germany")))
		));
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
		assertThat(axioms, containsInAnyOrder(
				Declaration(CHILD_OF_DOCTOR),
				SubClassOf(CHILD_OF_DOCTOR, ObjectSomeValuesFrom(HAS_PARENT, PHYSICIAN))
		));
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
		assertThat(axioms, containsInAnyOrder(
				Declaration(CAR),
				SubClassOf(CAR, DataSomeValuesFrom(HAS_NAME, XSD_STRING))));
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
		assertThat(axioms, containsInAnyOrder(
				Declaration(PERSON),
				SubClassOf(PERSON, ObjectAllValuesFrom(HAS_PARENT, HUMAN))
		));
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
		assertThat(axioms, containsInAnyOrder(
				Declaration(PERSON), SubClassOf(PERSON, DataAllValuesFrom(HAS_SSN, XSD_STRING))
		));
	}

	@Test
	public void TestOWLObjectOneOf()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Person");
		declareOWLObjectProperties(ontology, "hasGender");
		declareOWLNamedIndividual(ontology, "male");
		declareOWLNamedIndividual(ontology, "female");
		declareOWLNamedIndividual(ontology, "other");
		String expression = "Class: Person SubClassOf: hasGender ONLY {male, female, other}";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(PERSON), SubClassOf(PERSON, ObjectSomeValuesFrom(HAS_GENDER, ObjectOneOf(MALE, FEMALE, OTHER)))
		));
	}

	@Test
	public void TestNegatedClassExpression()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car");
		declareOWLObjectProperties(ontology, "hasEngine");
		String expression = "Class: Car EquivalentTo: NOT hasEngine EXACTLY 2";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(CAR),
				EquivalentClasses(CAR, ObjectComplementOf(ObjectExactCardinality(2, HAS_ENGINE, OWLThing())))));
	}

	@Test
	public void TestUnionClassExpression()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "A");
		declareOWLObjectProperties(ontology, "hasP1", "hasP2");
		String expression = "Class: A SubClassOf: hasP1 EXACTLY 2 OR hasP2 EXACTLY 3";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(A),
				SubClassOf(A, ObjectUnionOf(ObjectExactCardinality(2, HAS_P1, OWLThing()), ObjectExactCardinality(3, HAS_P2, OWLThing())))
		));
	}

	@Test
	public void TestIntersectionClassExpression()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "A");
		declareOWLObjectProperties(ontology, "hasP1", "hasP2");
		String expression = "Class: A SubClassOf: hasP1 EXACTLY 2 AND hasP2 EXACTLY 3";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(A),
				SubClassOf(A, ObjectIntersectionOf(ObjectExactCardinality(2, HAS_P1, OWLThing()), ObjectExactCardinality(3, HAS_P2, OWLThing())))
		));
	}

	@Test
	public void TestIndividualDeclaration()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED)
		));
	}

	@Test
	public void TestIndividualDeclarationWithTypes()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Person");
		String expression = "Individual: Fred Types: Person";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				ClassAssertion(PERSON, FRED)
		));
	}

	@Test
	public void TestIndividualDeclarationWithMultipleTypes()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Person", "Human");
		declareOWLObjectProperty(ontology, "hasParent");
		String expression = "Individual: Fred Types: Person, (hasParent ONLY Human)";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(3));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				ClassAssertion(PERSON, FRED),
				ClassAssertion(ObjectAllValuesFrom(HAS_PARENT, HUMAN), FRED)
		));
	}

	@Test
	public void TestIndividualDeclarationWithFacts()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLDataProperty(ontology, "hasName");
		String expression = "Individual: Fred Facts: hasName \"Fred\"";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DataPropertyAssertion(HAS_NAME, FRED, Literal("Fred"))
		));
	}

	@Test
	public void TestIndividualDeclarationWithMultipleFacts()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLDataProperties(ontology, "hasName", "hasAge");
		String expression = "Individual: Fred Facts: hasName \"Fred\", hasAge 23";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(3));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DataPropertyAssertion(HAS_NAME, FRED, Literal("Fred")),
				DataPropertyAssertion(HAS_AGE, FRED, Literal("23", XSD_INT))
		));
	}

	@Test
	public void TestIndividualDeclarationWithAnnotations()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLAnnotationProperty(ontology, "hasName");
		String expression = "Individual: Fred Annotations: hasName \"Fred\"";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				AnnotationAssertion(HAS_NAME_ANNOTATION, FRED_ANNOTATION, Literal("Fred"))
		));
	}

	@Test
	public void TestIndividualDeclarationWithMultipleAnnotations()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLAnnotationProperties(ontology, "hasName", "hasAge");
		String expression = "Individual: Fred Annotations: hasName \"Fred\", hasAge 23";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(3));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				AnnotationAssertion(HAS_NAME_ANNOTATION, FRED_ANNOTATION, Literal("Fred")),
				AnnotationAssertion(HAS_AGE_ANNOTATION, FRED_ANNOTATION, Literal("23", XSD_INT))
		));
	}

	@Test
	public void TestIndividualDeclarationWithSameIndividual()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred SameAs: Freddy";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				SameIndividual(FRED, FREDDY)
		));
	}

	@Test
	public void TestIndividualDeclarationWithMultipleSameIndividual()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred SameAs: Freddy, F";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(3));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				SameIndividual(FRED, FREDDY),
				SameIndividual(FRED, F)
		));
	}

	@Test
	public void TestIndividualDeclarationWithDifferentIndividuals()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred DifferentFrom: Bob";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DifferentIndividuals(FRED, BOB)
		));
	}

	@Test
	public void TestIndividualDeclarationWithMultipleDifferentIndividuals()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Individual: Fred DifferentFrom: Bob, Bobby";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(3));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DifferentIndividuals(FRED, BOB),
				DifferentIndividuals(FRED, BOBBY)
		));
	}

	@Test
	public void TestAbsoluteReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car");
		
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Class: @A1";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
	}

	@Test
	public void TestAbsoluteIndividualReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		Label cellA1 = createCell("Fred", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: @A1";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		assertThat(axioms, containsInAnyOrder(Declaration(FRED)));
	}

	@Test
	public void TestAbsoluteObjectPropertyReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLObjectProperty(ontology, "hasParent");
		
		Label cellA1 = createCell("hasParent", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Facts: @A1 Bob";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				ObjectPropertyAssertion(HAS_PARENT, FRED, BOB)
		));
	}

	@Test
	public void TestAbsoluteDataPropertyReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLDataProperty(ontology, "hasAge");
		
		Label cellA1 = createCell("hasAge", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Facts: @A1 23";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DataPropertyAssertion(HAS_AGE, FRED, Literal("23", XSD_INT))
		));
	}

	@Test
	public void TestAbsoluteAnnotationPropertyReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLAnnotationProperty(ontology, "hasAge");
		
		Label cellA1 = createCell("hasAge", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Annotations: @A1 23";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				AnnotationAssertion(HAS_AGE_ANNOTATION, FRED_ANNOTATION, Literal("23", XSD_INT))
		));
	}

	@Test
	public void TestLiteralReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		String expression = "Class: @\"Car\"";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
	}

	@Test
	public void TestAbsoluteReferenceWithSheetName()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car");
		
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Class: @'" + SHEET1 + "'!A1";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
	}

	@Test
	public void TestColumnWildcardInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car");
		
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Class: @*1";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
	}

	@Test
	public void TestRowWildcardInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car");
		
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Class: @A*";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
	}

	@Test
	public void TestClassQualifiedInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "Car");
		
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Class: @A1(Class)";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		assertThat(axioms, containsInAnyOrder(Declaration(CAR)));
	}

	@Test
	public void TestIndividualInQualifiedReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		Label cellA1 = createCell("Fred", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: @A1(Individual)";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		assertThat(axioms, containsInAnyOrder(Declaration(FRED)));
	}

	@Test
	public void TestObjectPropertyInQualifiedReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLObjectProperty(ontology, "hasParent");
		
		Label cellA1 = createCell("hasParent", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Facts: @A1(ObjectProperty) Bob";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				ObjectPropertyAssertion(HAS_PARENT, FRED, BOB)
		));
	}

	@Test
	public void TestDataPropertyQualifiedInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLDataProperty(ontology, "hasAge");
		
		Label cellA1 = createCell("hasAge", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Facts: @A1(DataProperty) 23";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DataPropertyAssertion(HAS_AGE, FRED, Literal("23", XSD_INT))
		));
	}

	@Test
	public void TestAnnotationPropertyQualifiedReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLAnnotationProperty(ontology, "hasAge");
		
		Label cellA1 = createCell("hasAge", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Annotations: @A1(AnnotationProperty) 23";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				AnnotationAssertion(HAS_AGE_ANNOTATION, FRED_ANNOTATION, Literal("23", XSD_INT))
		));
	}

	@Test
	public void TestXSDBooleanInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLDataProperty(ontology, "hasSSN");
		
		Label cellA1 = createCell("true", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Facts: hasSSN @A1(xsd:boolean)";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DataPropertyAssertion(HAS_SSN, FRED, Literal(true))
		));
	}

	@Test
	public void TestXSDByteInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLDataProperty(ontology, "hasSalary");
		
		Label cellA1 = createCell("34", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Facts: hasSalary @A1(xsd:byte)";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DataPropertyAssertion(HAS_SALARY, FRED, Literal("34", XSD_BYTE))
		));
	}

	@Test
	public void TestXSDShortInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLDataProperty(ontology, "hasSalary");
		
		Label cellA1 = createCell("34", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Facts: hasSalary @A1(xsd:short)";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DataPropertyAssertion(HAS_SALARY, FRED, Literal("34", XSD_SHORT))
		));
	}

	@Test
	public void TestXSDIntInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLDataProperty(ontology, "hasSalary");
		
		Label cellA1 = createCell("34", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Facts: hasSalary @A1(xsd:int)";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DataPropertyAssertion(HAS_SALARY, FRED, Literal("34", XSD_INT))
		));
	}

	@Test
	public void TestXSDFloatInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLDataProperty(ontology, "hasSalary");
		
		Label cellA1 = createCell("34000.0", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Facts: hasSalary @A1(xsd:float)";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DataPropertyAssertion(HAS_SALARY, FRED, Literal("34000.0", XSD_FLOAT))
		));
	}

	@Test
	public void TestXSDStringInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLDataProperty(ontology, "hasName");
		
		Label cellA1 = createCell("Fred", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Facts: hasName @A1(xsd:string)";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DataPropertyAssertion(HAS_NAME, FRED, Literal("Fred"))
		));
	}

	@Test
	public void TestXSDDateInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLDataProperty(ontology, "hasDOB");
		
		Label cellA1 = createCell("1999-01-01", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Facts: hasDOB @A1(xsd:date)";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DataPropertyAssertion(HAS_DOB, FRED, Literal("1999-01-01", XSD_DATE))
		));
	}

	@Test
	public void TestXSDDateTimeInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLDataProperty(ontology, "hasDOB");
		
		Label cellA1 = createCell("1999-01-01T10:10:10", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Facts: hasDOB @A1(xsd:dateTime)";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DataPropertyAssertion(HAS_DOB, FRED, Literal("1999-01-01T10:10:10", XSD_DATETIME))
		));
	}

	@Test
	public void TestXSDTimeInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLDataProperty(ontology, "hasBedTime");
		
		Label cellA1 = createCell("21:00", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Individual: Fred Facts: hasBedTime @A1(xsd:time)";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		assertThat(axioms, containsInAnyOrder(
				Declaration(FRED),
				DataPropertyAssertion(HAS_BEDTIME, FRED, Literal("21:00", XSD_TIME))
		));
	}

	@Test
	public void TestRDFSLabelAssignmentInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "BMW");
		
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Class: @A1(rdfs:label=(\"BMW\"))";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		assertThat(axioms, containsInAnyOrder(Declaration(BMW)));
	}

	@Test
	public void TestRDFSLabelAssignmentWithConcatenatedParametersInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "BigCar");
		
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Class: @A1(rdfs:label=(\"Big\", \"Car\"))";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		assertThat(axioms, containsInAnyOrder(Declaration(BIG_CAR)));
	}

	@Test
	public void TestRDFSLabelAssignmentWithReferenceParameterInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "BigCar");
		
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Class: @A1(rdfs:label=(\"Big\", @A1))";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		assertThat(axioms, containsInAnyOrder(Declaration(BIG_CAR)));
	}

	@Test
	public void TestRDFSLabelAppendInReference()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException
	{
		declareOWLClasses(ontology, "CarBig");
		
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		
		String expression = "Class: @A1(rdfs:label=mm:append(\"Big\"))";
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		assertThat(axioms, containsInAnyOrder(Declaration(CAR_BIG)));
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
