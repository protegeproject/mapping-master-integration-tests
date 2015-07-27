package org.mm.renderer.owlapi;

import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WriteException;
import org.junit.Test;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.ParseException;
import org.mm.rendering.owlapi.OWLAPIRendering;
import org.mm.test.IntegrationTestBase;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

// TODO Take integration tests from text render and repurpose for OWLAPI renderer

public class OWLAPIRendererIT extends IntegrationTestBase
{
	@Test public void TestSubClassOf()
			throws WriteException, BiffException, MappingMasterException, ParseException, IOException,
			OWLOntologyCreationException
	{
		OWLOntology ontology = createOWLOntology();
		declareOWLClasses(ontology, "Car", "Vehicle");
		String expression = "Class: Car SubClassOf: Vehicle";
		//    Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, expression);

		// Assert.assertTrue(owlapiRendering.isPresent());
		// Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		// TODO Test that we have the expected subclass of axiom
	}

	@Test public void TestAbsoluteReference()
			throws OWLOntologyCreationException, WriteException, BiffException, MappingMasterException, ParseException,
			IOException
	{
		OWLOntology ontology = createOWLOntology();
		String expression = "Class: @A1";
		Label cellA1 = createCell("Car", 1, 1);
		Set<Label> cells = createCells(cellA1);
		//Optional<? extends OWLAPIRendering> owlapiRendering = createOWLAPIRendering(ontology, SHEET1, cells, expression);

		// Assert.assertTrue(owlapiRendering.isPresent());
		// Set<OWLAxiom> axioms = owlapiRendering.get().getOWLAxioms();
		// TODO Test that we have the expected declaration axiom for the class Car
	}

	// TODO Tests for the following directives:
	// mm:Location, mm:ResolveIfOWLEntityExists, mm:SkipIfOWLEntityExists, mm:WarningIfOWLEntityExists,
	// mm:ErrorIfOWLEntityExists, mm:CreateIfOWLEntityDoesNotExist, mm:SkipIfOWLEntityDoesNotExist,
	// mm:WarningIfOWLEntityDoesNotExist, mm:ErrorIfOWLEntityDoesNotExist
}
