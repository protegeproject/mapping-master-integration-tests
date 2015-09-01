package org.mm.ui;

import java.io.File;

import javax.swing.JFrame;

import org.mm.ui.dialog.SimpleDialogManager;
import org.mm.ui.view.ApplicationView;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class SimpleMMApplication
{
	public void createAndShowGUI() throws Exception
	{
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology currentOntology = manager.loadOntologyFromOntologyDocument(getOntologyFile("org/mm/ui/ipps.owl"));
		ApplicationView mainView = new ApplicationView(currentOntology, new SimpleDialogManager());
		
		JFrame frame = new JFrame("Mapping Master");
		frame.setContentPane(mainView);
		frame.setSize(1100, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static File getOntologyFile(String value)
	{
		String path = ClassLoader.getSystemClassLoader().getResource(value).getPath();
		return new File(path);
	}
}
