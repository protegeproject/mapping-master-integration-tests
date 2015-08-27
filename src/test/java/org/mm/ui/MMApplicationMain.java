package org.mm.ui;

import javax.swing.SwingUtilities;

public class MMApplicationMain
{
	public static void main(String[] args)
	{
		SimpleMMApplication gui = new SimpleMMApplication();
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				try {
					gui.createAndShowGUI();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
