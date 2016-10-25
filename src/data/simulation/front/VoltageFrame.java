package data.simulation.front;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Stroke;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Creates the panel that contains the graph for Voltage and adds it to
 * innerFrame
 * 
 * @author Stefan Ceballos
 * @date 7-14-15
 */
public class VoltageFrame extends JPanel {

	private static final long serialVersionUID = 1L;
	private static ChartPanel cp; // panel that contains graph
	private int x = 0; // counter to control adding data to the series
	public static Timer t; // timer used to add data to series
	public static XYSeriesCollection ds; // a collection to hold all the series
	// private double time = 0.0;

	// final numbers for some initial set up of the chart panel
	private static final double INITIAL_LOWER_Y = 0.9;
	private static final double INITIAL_UPPER_Y = 1.1;
	private static final double X_TICK_UNIT = 0.5;
	private static final double X_RANGE = 5;

	// static variables for scheduled executions
	public static Runnable runnable;
	public static ScheduledExecutorService scheduledExecutorService;
	public static ScheduledFuture<?> future;

	// variables for event detection and classification
	private double[] sdList = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	public static boolean eventDetected = false;
	public static boolean classified = false;
	public static int[] knnResults;
	private Classify classify;
	private GetStrongest getStrongest;
	private SlopeFeature slopeFeature;
	public static int strongestPMU;
	private int eventPointCount = 0;
	private List<double[]> eventList = new ArrayList<double[]>();
	public static boolean eventListInitialized = false;
	private final int NUM_POINTS_TO_COLLECT = 360;
	// pmuOrder used to control the color frame colors and will re organize once
	// a disturbance is detected
	public static int[] pmuOrder = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	private JFreeChart chart;
	private XYPlot p;

	/* FOR TESTING ONLY */
	// private boolean test = true;

	/**
	 * Creates an instance of VoltageFrame
	 * 
	 * @param pos
	 *            the GridBagConstraint location for placement in the innerFrame
	 */
	public VoltageFrame(int pos) {
		if (ToolbarListener.flag)
			System.out.println("reDraw VoltageFrame panel");
		this.setBackground(new Color(140, 140, 140));

		final XYSeries series1 = new XYSeries("PMU 1");
		final XYSeries series2 = new XYSeries("PMU 2");
		final XYSeries series3 = new XYSeries("PMU 3");
		final XYSeries series4 = new XYSeries("PMU 4");
		final XYSeries series5 = new XYSeries("PMU 5");
		final XYSeries series6 = new XYSeries("PMU 6");
		final XYSeries series7 = new XYSeries("PMU 7");
		final XYSeries series8 = new XYSeries("PMU 8");
		final XYSeries series9 = new XYSeries("PMU 9");
		final XYSeries series10 = new XYSeries("PMU 10");
		ds = new XYSeriesCollection(series1);
		ds.addSeries(series2);
		ds.addSeries(series3);
		ds.addSeries(series4);
		ds.addSeries(series5);
		ds.addSeries(series6);
		ds.addSeries(series7);
		ds.addSeries(series8);
		ds.addSeries(series9);
		ds.addSeries(series10);

		runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// if (test) {
				// testing();
				// }
				// init the event list if it is not already
				if (!eventListInitialized)
					initEventList();
				if (eventDetected && !classified && eventPointCount == 360) {
					System.out
							.println("classify and strongest if ran with bool classified="
									+ classified
									+ " bool eventDetected="
									+ eventDetected);
					classified = true;
					classify = new Classify();
					Thread t = new Thread(classify);
					getStrongest = new GetStrongest(sdList);
					Thread t1 = new Thread(getStrongest);
					t.start();
					t1.start();
				}
				if (x < 3600) {
					switch (ToolbarListener.getPMUnumber()) {
					case 1:
						if (x > 4 && eventDetected == false)
							detectEvent(x, 1);
						else if (eventDetected
								&& eventPointCount < NUM_POINTS_TO_COLLECT) {
							for (int i = 0; i < ToolbarListener.getPMUnumber(); i++) {
								eventList.get(i)[eventPointCount] = GetDBData.pmuList
										.get(i).getData().get(x - 1);
							}
							eventPointCount++;
						}

						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(0).getData().get(x));
						break;
					case 2:
						if (x > 4 && eventDetected == false)
							detectEvent(x, 2);
						else if (eventDetected
								&& eventPointCount < NUM_POINTS_TO_COLLECT) {
							for (int i = 0; i < ToolbarListener.getPMUnumber(); i++) {
								eventList.get(i)[eventPointCount] = GetDBData.pmuList
										.get(i).getData().get(x - 1);
							}
							eventPointCount++;
						}

						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(0).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(1).getData().get(x));
						break;
					case 3:
						if (x > 4 && eventDetected == false)
							detectEvent(x, 3);
						else if (eventDetected
								&& eventPointCount < NUM_POINTS_TO_COLLECT) {
							for (int i = 0; i < ToolbarListener.getPMUnumber(); i++) {
								eventList.get(i)[eventPointCount] = GetDBData.pmuList
										.get(i).getData().get(x - 1);
							}
							eventPointCount++;
						}

						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(0).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(1).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(2).getData().get(x));
						break;
					case 4:
						if (x > 4 && eventDetected == false)
							detectEvent(x, 4);
						else if (eventDetected
								&& eventPointCount < NUM_POINTS_TO_COLLECT) {
							for (int i = 0; i < ToolbarListener.getPMUnumber(); i++) {
								eventList.get(i)[eventPointCount] = GetDBData.pmuList
										.get(i).getData().get(x - 1);
							}
							eventPointCount++;
						}

						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(0).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(1).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(2).getData().get(x));
						series4.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(3).getData().get(x));
						break;
					case 5:
						if (x > 4 && eventDetected == false)
							detectEvent(x, 5);
						else if (eventDetected
								&& eventPointCount < NUM_POINTS_TO_COLLECT) {
							for (int i = 0; i < ToolbarListener.getPMUnumber(); i++) {
								eventList.get(i)[eventPointCount] = GetDBData.pmuList
										.get(i).getData().get(x - 1);
							}
							eventPointCount++;
						}

						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(0).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(1).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(2).getData().get(x));
						series4.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(3).getData().get(x));
						series5.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(4).getData().get(x));
						break;
					case 6:
						if (x > 4 && eventDetected == false)
							detectEvent(x, 6);
						else if (eventDetected
								&& eventPointCount < NUM_POINTS_TO_COLLECT) {
							for (int i = 0; i < ToolbarListener.getPMUnumber(); i++) {
								eventList.get(i)[eventPointCount] = GetDBData.pmuList
										.get(i).getData().get(x - 1);
							}
							eventPointCount++;
						}

						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(0).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(1).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(2).getData().get(x));
						series4.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(3).getData().get(x));
						series5.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(4).getData().get(x));
						series6.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(5).getData().get(x));

						break;
					case 7:
						if (x > 4 && eventDetected == false)
							detectEvent(x, 7);
						else if (eventDetected
								&& eventPointCount < NUM_POINTS_TO_COLLECT) {
							for (int i = 0; i < ToolbarListener.getPMUnumber(); i++) {
								eventList.get(i)[eventPointCount] = GetDBData.pmuList
										.get(i).getData().get(x - 1);
							}
							eventPointCount++;
						}

						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(0).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(1).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(2).getData().get(x));
						series4.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(3).getData().get(x));
						series5.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(4).getData().get(x));
						series6.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(5).getData().get(x));
						series7.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(6).getData().get(x));

						break;
					case 8:
						if (x > 4 && eventDetected == false)
							detectEvent(x, 8);
						else if (eventDetected
								&& eventPointCount < NUM_POINTS_TO_COLLECT) {
							for (int i = 0; i < ToolbarListener.getPMUnumber(); i++) {
								eventList.get(i)[eventPointCount] = GetDBData.pmuList
										.get(i).getData().get(x - 1);
							}
							eventPointCount++;
						}

						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(0).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(1).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(2).getData().get(x));
						series4.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(3).getData().get(x));
						series5.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(4).getData().get(x));
						series6.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(5).getData().get(x));
						series7.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(6).getData().get(x));
						series8.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(7).getData().get(x));
						break;
					case 9:
						if (x > 4 && eventDetected == false)
							detectEvent(x, 9);
						else if (eventDetected
								&& eventPointCount < NUM_POINTS_TO_COLLECT) {
							for (int i = 0; i < ToolbarListener.getPMUnumber(); i++) {
								eventList.get(i)[eventPointCount] = GetDBData.pmuList
										.get(i).getData().get(x - 1);
							}
							eventPointCount++;
						}

						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(0).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(1).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(2).getData().get(x));
						series4.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(3).getData().get(x));
						series5.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(4).getData().get(x));
						series6.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(5).getData().get(x));
						series7.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(6).getData().get(x));
						series8.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(7).getData().get(x));
						series9.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(8).getData().get(x));

						break;
					case 10:
						if (x > 4 && eventDetected == false)
							detectEvent(x, 10);
						else if (eventDetected
								&& eventPointCount < NUM_POINTS_TO_COLLECT) {
							for (int i = 0; i < ToolbarListener.getPMUnumber(); i++) {
								eventList.get(i)[eventPointCount] = GetDBData.pmuList
										.get(i).getData().get(x - 1);
							}
							eventPointCount++;
						}

						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(0).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(1).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(2).getData().get(x));
						series4.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(3).getData().get(x));
						series5.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(4).getData().get(x));
						series6.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(5).getData().get(x));
						series7.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(6).getData().get(x));
						series8.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(7).getData().get(x));
						series9.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(8).getData().get(x));
						series10.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(9).getData().get(x));
						break;
					}
					x++;
				} else if (x == 3600) {
					scheduledExecutorService.shutdown();
					// future.cancel(true);
					JButton stopButton = (JButton) ToolbarListener.tools
							.getComponent(Toolbar.STOP_BUTTON);
					stopButton.setText("Stop");
					x = 0;
				}
			}

			/* FOR TESTING ONLY */
			// private void testing() {
			// // TODO Auto-generated method stub
			// // testing print the values at the trigger point to examine
			// test = false;
			// for (int i = 0; i < 10; i++) {
			// for (int j = 1200; j < 1208; j++) {
			// System.out.print(GetDBData.pmuList.get(i).getData()
			// .get(j)
			// + ", ");
			// }
			// System.out.println();
			// }
			//
			// }

			private void initEventList() {
				for (int i = 0; i < ToolbarListener.getPMUnumber(); i++) {
					eventList.add(new double[360]);
				}
				eventListInitialized = true;
			}

			private void detectEvent(int x, int numPmu) {
				// System.out.println("detectEvent called");

				for (int i = 0; i < numPmu; i++) {
					double sd = 0;
					// System.out.println("for loop called and i = " + i);
					// double cur = (GetDBData.pmuList.get(i).getData().get(x));
					// double prevSd = 0.0;
					double mean = 0;
					double sum = 0;
					int count = 0;
					for (int k = x - 5; k < x; k++) {
						sum += GetDBData.pmuList.get(i).getData().get(k);
						count++;
					}
					mean = sum / count;
					for (int j = x - 5; j < x; j++) {
						sd = sd
								+ Math.pow(GetDBData.pmuList.get(i).getData()
										.get(j)
										- mean, 2);
					}
					String s_prevSd;
					DecimalFormat df = new DecimalFormat(
							"#.####################");
					s_prevSd = df.format(sdList[i]);
					// System.out.println(s_prevSd);
					sdList[i] = Double.parseDouble(s_prevSd);
					// System.out.println("cur=" + cur);
					// System.out.println("sd=" + sd);
					// System.out.println("prevSd=" + prevSd);
					if (sdList[i] != 0)
						if (sd > sdList[i] * 20) {
							eventDetected = true;
							// System.out.println("cur=" + cur);
							// System.out.printf("%.20f\n",
							// Math.abs(sdList[i] - sd));

							System.out.print("event detected in pmu " + (i + 1)
									+ "...");

							// for testing the values of the voltage in the
							// pmulist
							for (int k = x - 3; k < x + 4; k++) {
								System.out.print(GetDBData.pmuList.get(i)
										.getData().get(k)
										+ ", ");
							}
							System.out.println();
							// System.out.printf("%.20f\n", sd);
							System.out.println("i was " + x);
						}
					sdList[i] = sd;
				}

				// return sd;
			}

		};

		// pass null as first param to remove title
		// otherwise pass string to get title
		// first bool param is for legend
		chart = ChartFactory.createXYLineChart(null, "time (in seconds)",
				"Voltage", ds, PlotOrientation.VERTICAL, true, true, false);

		// set the starting value of the chart
		p = chart.getXYPlot();
		// set the tick units for the x axis
		// tick units should stay at .5 and not fluctuate
		final NumberAxis nXAxis = (NumberAxis) p.getDomainAxis();
		nXAxis.setTickUnit(new NumberTickUnit(X_TICK_UNIT));
		// nXAxis.setLowerBound(0.0);
		// get range axis is y
		final ValueAxis yAxis = p.getRangeAxis();
		// get domain axis is x
		final ValueAxis xAxis = p.getDomainAxis();
		// xAxis.setLowerBound(-0.5);
		xAxis.setAutoRange(true);
		xAxis.setFixedAutoRange(5);

		// create dashed stroke
		Stroke dashedStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 0, new float[] { 2, 2 }, 0);

		// Axis a = p.getRangeAxis();
		p.setDomainCrosshairStroke(dashedStroke);
		p.setRangeCrosshairStroke(dashedStroke);
		p.setDomainCrosshairPaint(Color.WHITE);
		p.setRangeCrosshairPaint(Color.WHITE);
		p.setDomainCrosshairVisible(true);
		p.setRangeCrosshairVisible(true);
		yAxis.setRange(new Range(INITIAL_LOWER_Y, INITIAL_UPPER_Y), true, true);

		// p.getRangeAxis().setAutoRangeMinimumSize(1.1);
		// axis.setAutoRange(false);

		// axis.zoomRange(0.9, 1.1);
		// axis.setUpperBound(1.1);
		// axis.setLowerBound(0.9);
		// axis.setDefaultAutoRange(new Range(0.9, 1.1));
		// axis.setFixedAutoRange(0.9);

		// chart.setPadding(new RectangleInsets(0, 0, 0, 0));
		// set color of line
		// lines are set at 0 index
		p.getRenderer().setSeriesPaint(0, TimeConstants.heatMap.get(0));
		p.getRenderer().setSeriesPaint(1, TimeConstants.heatMap.get(1));
		p.getRenderer().setSeriesPaint(2, TimeConstants.heatMap.get(2));
		p.getRenderer().setSeriesPaint(3, TimeConstants.heatMap.get(3));
		p.getRenderer().setSeriesPaint(4, TimeConstants.heatMap.get(4));
		p.getRenderer().setSeriesPaint(5, TimeConstants.heatMap.get(5));
		p.getRenderer().setSeriesPaint(6, TimeConstants.heatMap.get(6));
		p.getRenderer().setSeriesPaint(7, TimeConstants.heatMap.get(7));
		p.getRenderer().setSeriesPaint(8, TimeConstants.heatMap.get(8));
		p.getRenderer().setSeriesPaint(9, TimeConstants.heatMap.get(9));

		cp = new ChartPanel(chart, true) {
			private static final long serialVersionUID = 1L;

			// restoreAutoBounds() will zoom the graph back to the original
			// scaling when the mouse is clicked and dragged to the left
			@Override
			public void restoreAutoBounds() {
				if (GetDBData.vMin != 0.0 && GetDBData.vMax != 0.0) {
					yAxis.setRange(new Range(GetDBData.vMin, GetDBData.vMax),
							true, true);
					xAxis.setAutoRange(true);
					xAxis.setFixedAutoRange(X_RANGE);
				}
			}
		};

		cp.getChart().removeLegend();
		cp.setDoubleBuffered(true);
		// cp.getChart().getLegend().setBackgroundPaint(new Color(204, 204,
		// 204));
		// next line sets background of chart
		cp.getChart().setBackgroundPaint(new Color(204, 204, 204));
		// next line sets background of plot
		cp.getChart().getPlot().setBackgroundPaint(new Color(88, 88, 88));

		cp.setPreferredSize(new Dimension(820, 370));
		cp.setMouseWheelEnabled(true);

		Main.addComponent(Main.getInnerFrame(), getCp(), 1, 1, 1, 1, pos,
				GridBagConstraints.NONE, 1.0, 1.0);

	}

	/**
	 * Returns the chart panel for the voltage graph
	 * 
	 * @return cp the chart panel
	 */
	public static ChartPanel getCp() {
		return cp;
	}

	/**
	 * Sets the chart panel of the votage graph
	 * 
	 * @param cp
	 *            the chart panel to set
	 */
	public static void setCp(ChartPanel cp) {
		VoltageFrame.cp = cp;
	}

	class Classify implements Runnable {

		private Shapelet shapelet;
		private KNN knn;

		public Classify() {
			// create shapelet and knn objects
			shapelet = new Shapelet();
			// slopeFeature = new SlopeFeature();
			knn = new KNN();

		}

		@Override
		public void run() {

			List<double[]> shapeList = shapelet
					.genShapeletList(GetDBData.pmuList);
			// List<double[]> shapeList = slopeFeature.calLineSlope(eventList);
			knnResults = knn.runKNN(shapeList);

			// display the disturbance frame with the info from
			// the pmu with the greatest disturbance
			Main.getInnerFrame().remove(Main.getdFrame());
			Main.addComponent(Main.getInnerFrame(), Main.getdFrame(), 1, 1, 1,
					1, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
					0, 0);
			// revalidate and repaint the innerframe
			Main.getInnerFrame().revalidate();
			Main.getInnerFrame().repaint();

		}

	}

	class GetStrongest implements Runnable {

		private double[] list;

		public GetStrongest(double[] _list) {
			// create shapelet and knn objects
			list = _list;

		}

		@Override
		public void run() {
			for (int i = 0; i < sdList.length; i++) {
				System.out.println(sdList[i]);
			}
			// default to first pmu and then compare with rest of the pmu in
			// list
			// strongestPMU;
			strongestPMU = 1;
			double cur = list[0];
			for (int i = 1; i < list.length; i++) {
				if (list[i] > cur) {
					strongestPMU = i + 1;
					cur = list[i];
				}
			}
			int tmp = pmuOrder[0];
			pmuOrder[0] = strongestPMU - 1;
			pmuOrder[strongestPMU - 1] = tmp;
			System.out.print("pmu order is ");
			for (int i = 0; i < pmuOrder.length; i++) {
				System.out.print(pmuOrder[i] + ", ");
			}
			System.out.println();
			System.out.println("Strongest pmu number: " + strongestPMU);
			// set the color of strongest pmu to red and the current red pmu to
			// the color the strongest pmu was by default
			p.getRenderer().setSeriesPaint(strongestPMU - 1,
					TimeConstants.heatMap.get(0));
			p.getRenderer().setSeriesPaint(0,
					TimeConstants.heatMap.get(strongestPMU - 1));
			// increase the line width so that it stands out
			p.getRenderer().setSeriesStroke(strongestPMU - 1,
					new BasicStroke(3));
			FrequencyFrame.freqPlot.getRenderer().setSeriesPaint(
					strongestPMU - 1, TimeConstants.heatMap.get(0));
			FrequencyFrame.freqPlot.getRenderer().setSeriesPaint(0,
					TimeConstants.heatMap.get(strongestPMU - 1));
			FrequencyFrame.freqPlot.getRenderer().setSeriesStroke(
					strongestPMU - 1, new BasicStroke(3));

			Main.getInnerFrame().remove(Main.getColorFrame());
			Main.addComponent(Main.getInnerFrame(), Main.getColorFrame(), 1, 1,
					1, 1, GridBagConstraints.NORTHEAST,
					GridBagConstraints.NONE, 0, 0);

			// revalidate and repaint the innerframe
			Main.getInnerFrame().revalidate();
			Main.getInnerFrame().repaint();
		}
	}
}
