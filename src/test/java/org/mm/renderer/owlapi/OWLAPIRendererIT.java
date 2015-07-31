package org.mm.renderer.owlapi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.ParseException;
import org.mm.rendering.owlapi.OWLAPIRendering;
import org.mm.test.IntegrationTestBase;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;

public class OWLAPIRendererIT extends IntegrationTestBase
{
	private OWLOntology ontology;
	
	@Before
	public void setUp() throws OWLOntologyCreationException
	{
		ontology = createOWLOntology();
	}

	@Test
	public void TestClassDeclaration() throws WriteException, BiffException, MappingMasterException, ParseException,
			IOException
	{
		declareOWLClasses(ontology, "Car");
		String expression = "Class: Car";
		
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		
		System.out.println(owlapiRendering.get().getOWLAxioms());
	}
	
	@Test
	public void TestSubClassOf() throws WriteException, BiffException, MappingMasterException, ParseException,
			IOException
	{
		declareOWLClasses(ontology, "Car", "Vehicle");
		String expression = "Class: Car SubClassOf: Vehicle";
		
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));
		
		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(1));
		
		System.out.println(owlapiRendering.get().getOWLAxioms());
	}

	@Test
	public void TestMultipleSubClassOf() throws WriteException, BiffException, MappingMasterException, ParseException,
			IOException
	{
		declareOWLClasses(ontology, "Car", "Vehicle", "Device");
		String expression = "Class: Car SubClassOf: Vehicle, Device";
		
		Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);
		assertThat(owlapiRendering.isPresent(), is(true));

		Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		assertThat(axioms, hasSize(2));
		
		System.out.println(owlapiRendering.get().getOWLAxioms());
	}

	@Test
	public void TestAbsoluteReference() throws OWLOntologyCreationException, WriteException,
	        MappingMasterException, ParseException, IOException
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

  // TODO Different rdfs:label and rdf:id, e.g., Class: @A5(rdf:ID=@B5 rdfs:label=@A5)
	// TODO Tests for the following directives:
	// mm:Location, mm:Prefix, mm:Namespace
	// mm:ResolveIfOWLEntityExists, mm:SkipIfOWLEntityExists, mm:WarningIfOWLEntityExists,
	// mm:ErrorIfOWLEntityExists, mm:CreateIfOWLEntityDoesNotExist, mm:SkipIfOWLEntityDoesNotExist,
	// mm:WarningIfOWLEntityDoesNotExist, mm:ErrorIfOWLEntityDoesNotExist
}
