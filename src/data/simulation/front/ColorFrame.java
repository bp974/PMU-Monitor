package data.simulation.front;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * ColorFrame will show the color legend for the pmus that are being displayed
 * in the charts
 * 
 * @author Brett Pelkey
 * @author Stefan Ceballos
 */
public class ColorFrame extends JPanel {
	private static final long serialVersionUID = 1L;
	// array list of the colors that will be displayed
	private static List<Color> heatMap;

	/**
	 * Constructor of ColorFrame that will set up the frame and add it to the
	 * innerframe
	 * 
	 * @param pos
	 *            the position in which to put the frame in the gridbaglayout of
	 *            innerframe
	 */
	public ColorFrame(int pos) {

		JLabel frameLabel = new JLabel("Color Legend");
		this.add(frameLabel);
		// JButton button = new JButton();
		// //button.setSize(300, 15);
		// button.setPreferredSize(new Dimension(300, 15));
		// button.setBackground(TimeConstants.heatMap.get(0));
		// this.add(button);
		this.setPreferredSize(new Dimension(380, 370));
		this.repaint();

		// add component to innerframe
		Main.addComponent(Main.getInnerFrame(), this, 1, 1, 1, 1, pos,
				GridBagConstraints.NONE, 0, 0);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawColorBox d = new drawColorBox(this);

		heatMap = Arrays.asList(
		// https://web.njit.edu/~kevin/rgb.txt.html
		// Heat map scale for intensity of disturbance (high to low)
				new Color(205, 0, 0), // red2
				new Color(205, 133, 0), // orange3
				Color.orange, new Color(173, 255, 47), // green yellow
				new Color(50, 205, 50), // lime green
				new Color(0, 113/* 205 */, 56/* 102 */), // spring green3
				new Color(64, 224, 208), // turquoise
				Color.blue, new Color(255, 0, 255), // magenta
				new Color(255, 187, 255) // plum
				);

		if (!ToolbarListener.flag) {
			d.draw(g);
		} else
			d.reDraw(g);
	}

	/**
	 * Gets the color from the array with an index
	 * 
	 * @param index
	 *            the position in the array to get the color
	 * @return the color
	 */
	public static Color getHeatMap(int index) {
		return heatMap.get(index);
	}
}

/**
 * drawColorBox is responsible for drawing the legend on the frame
 *
 */
class drawColorBox {
	private int labelPos;
	Graphics g;

	public drawColorBox(ColorFrame colorFrame) {
		g = colorFrame.getGraphics();
		colorFrame.setBackground(new Color(140, 140, 140));
	}

	public void draw(Graphics g) {
		g.setColor(new Color(204, 204, 204));
		g.fillRect(0, 0, 380, 370);
	}

	public void reDraw(Graphics g) {
		System.out.println("reDraw() CALLED in ColorFrame");
		g.setColor(new Color(204, 204, 204));
		g.fillRect(0, 0, 380, 370);
		g.setFont(new Font("Arial", Font.BOLD, 16));

		labelPos = 40;
		// for loop to add items to the legend depending upon the number of
		// pmu's selected in the toolbar
		for (int x = 0; x < ToolbarListener.getPMUnumber(); x++) {
			g.setColor(Color.BLACK);
			g.drawString("PMU " + (x + 1) + "  ", 15, labelPos);
			g.setColor(ColorFrame.getHeatMap(VoltageFrame.pmuOrder[x]));
			g.fillRect(100, (labelPos - 8), 100, 5);
			labelPos = labelPos + 35;
		}
	}
}