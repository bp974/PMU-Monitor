package data.simulation.front;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * DisturbanceFrame will show relevant information of the pmu that has the
 * largest event detected
 * 
 * @author Brett Pelkey
 * @author Stefan Ceballos
 */
public class DisturbanceFrame extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of DisturbanceFrame that will set up the frame and add it to
	 * the innerframe
	 * 
	 * @param pos
	 *            the position in which to put the frame in the gridbaglayout of
	 *            innerframe
	 */
	public DisturbanceFrame(int pos) {

		JLabel frameLabel = new JLabel("PMU Event Information");
		this.add(frameLabel);
		this.setPreferredSize(new Dimension(380, 370));
		this.repaint();
		// add component to innerframe
		Main.addComponent(Main.getInnerFrame(), this, 1, 1, 1, 1, pos, GridBagConstraints.NONE, 0, 0);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawDisturbanceBox d = new drawDisturbanceBox(this);
		if (!ToolbarListener.disturbanceFlag) {
			d.draw(g);
		} else
			d.reDraw(g);
	}
}

class drawDisturbanceBox {
	Graphics g;

	public drawDisturbanceBox(DisturbanceFrame disturbanceFrame) {
		g = disturbanceFrame.getGraphics();
		disturbanceFrame.setBackground(new Color(140, 140, 140));
	}

	public void draw(Graphics g) {
		g.setColor(new Color(204, 204, 204));
		g.fillRect(0, 0, 380, 370);
	}

	public void reDraw(Graphics g) {
		if (VoltageFrame.knnResults != null) {
			System.out.println("reDraw() CALLED in DisturbanceFrame");
			g.setColor(new Color(204, 204, 204));
			g.fillRect(0, 0, 380, 370);

			// draw red outline around box to signify a disturbance has happened
			g.setColor(Color.RED);
			// top
			g.fillRect(0, 0, 375, 5);
			// left
			g.fillRect(0, 0, 5, 370);
			// right
			g.fillRect(375, 0, 5, 370);
			// bottom
			g.fillRect(0, 365, 375, 5);

			// set info on frame
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 16));
			g.drawString("Name: " + GetDBData.pmuList.get(VoltageFrame.strongestPMU - 1).getPmuName(), 15, 50);
			g.drawString("Area: " + GetDBData.pmuList.get(VoltageFrame.strongestPMU - 1).getAreaName(), 15, 90);
			g.drawString("Area Id: " + GetDBData.pmuList.get(VoltageFrame.strongestPMU - 1).getAreaId(), 15, 130);
			g.drawString("State: " + GetDBData.pmuList.get(VoltageFrame.strongestPMU - 1).getState(), 15, 170);
			g.drawString("Bus Id: " + GetDBData.pmuList.get(VoltageFrame.strongestPMU - 1).getBusId(), 15, 210);
			g.drawString("Zone Id: " + GetDBData.pmuList.get(VoltageFrame.strongestPMU - 1).getZoneId(), 15, 250);
			String eventType = GetDBData.pmuList.get(VoltageFrame.strongestPMU - 1).getEventType();
			// check what type of event it is and display the full name of the
			// event
			if (eventType.equals("FLT"))
				eventType = "Fault";
			else if (eventType.equals("GNL"))
				eventType = "Generation Loss";
			else if (eventType.equals("LSF"))
				eventType = "Load Switch Off";
			else if (eventType.equals("LSN"))
				eventType = "Load Switch On";
			else if (eventType.equals("SHF"))
				eventType = "Reactive Power Excluded";
			else if (eventType.equals("SHN"))
				eventType = "Reactive Power Introduced";
			else if (eventType.equals("SMS"))
				eventType = "Synchronous Motor Switching Off";
			else if (eventType.equals("SRF"))
				eventType = "Series Capacitor Switching Off";
			else if (eventType.equals("SRN"))
				eventType = "Series Capacitor Switching On";
			g.drawString("Event Type: " + eventType, 15, 290);
			String predEventType = null;
			switch (VoltageFrame.knnResults[VoltageFrame.strongestPMU - 1]) {
			case 1:
				predEventType = "Fault";
				break;
			case 2:
				predEventType = "Generation Loss";
				break;
			case 3:
				predEventType = "Load Switch Off";
				break;
			case 4:
				predEventType = "Load Switch On";
				break;
			case 5:
				predEventType = "Reactive Power Excluded";
				break;
			case 6:
				predEventType = "Reactive Power Introduced";
				break;
			case 7:
				predEventType = "Synchronous Motor Switching Off";
				break;
			case 8:
				predEventType = "Series Capacitor Switching Off";
				break;
			case 9:
				predEventType = "Series Capacitor Switching On";
				break;
			}
			g.drawString("Pred Event: " + predEventType, 15, 330);
		} else if (VoltageFrame.eventDetected) {
			g.setColor(new Color(204, 204, 204));
			g.fillRect(0, 0, 380, 370);
			// draw red outline around box to signify a disturbance has happened
			g.setColor(Color.RED);
			// top
			g.fillRect(0, 0, 375, 5);
			// left
			g.fillRect(0, 0, 5, 370);
			// right
			g.fillRect(375, 0, 5, 370);
			// bottom
			g.fillRect(0, 365, 375, 5);
		} else {
			g.setColor(new Color(204, 204, 204));
			g.fillRect(0, 0, 380, 370);

		}
	}
}