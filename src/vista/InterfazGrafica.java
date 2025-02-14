package vista;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class InterfazGrafica extends JFrame {
	GridLayout experimentLayout = new GridLayout(8, 1);
	JFrame principal = new JFrame();
	
	public InterfazGrafica() {
		principal.setLayout(experimentLayout);
		
	}
	
}
