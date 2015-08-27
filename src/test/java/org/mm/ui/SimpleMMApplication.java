package org.mm.ui;

import javax.swing.JFrame;

import org.mm.ui.dialog.SimpleDialogManager;
import org.mm.ui.view.ApplicationView;

public class SimpleMMApplication
{
	public void createAndShowGUI() throws Exception
	{
		ApplicationView mainView = new ApplicationView(new SimpleDialogManager());
		mainView.updateOntologyDocument(classloader("org/mm/ui/ipps.owl"));
		mainView.setDividerLocation(500);
		mainView.setResizeWeight(0.8);
		
		JFrame frame = new JFrame("Mapping Master");
		frame.setContentPane(mainView);
		frame.setSize(1100, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static String classloader(String value)
	{
		return ClassLoader.getSystemClassLoader().getResource(value).getPath();
	}
}
