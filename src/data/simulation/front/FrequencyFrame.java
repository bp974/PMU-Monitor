package data.simulation.front;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Stroke;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

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
 * Creates the panel that contains the graph for Frequency and adds it to
 * innerFrame
 * 
 * @author Stefan Ceballos
 * @date 7-14-15
 */
public class FrequencyFrame extends JPanel {

	private static final long serialVersionUID = 1L;
	private static ChartPanel cp; // panel that contains graph
	private int x = 0; // counter to control adding data to the series
	public static Timer t; // timer used to add data to series
	public static XYSeriesCollection ds; // a collection to hold all the series

	// final numbers for some initial set up of the chart panel
	private static final double INITIAL_LOWER_Y = 59.9;
	private static final double INITIAL_UPPER_Y = 60.1;
	private static final double X_TICK_UNIT = 0.5;
	private static final double X_RANGE = 5;

	// static variables for scheduled executions
	public static Runnable runnable;
	public static ScheduledExecutorService scheduledExecutorService;
	public static ScheduledFuture<?> future;
	
	public static JFreeChart freqChart;
	public static XYPlot freqPlot;

	// private double time = 0.0;

	/**
	 * Creates an instance of FrequencyFrame
	 * 
	 * @param pos
	 *            the GridBagConstraint location for placement in the innerFrame
	 */
	public FrequencyFrame(int pos) {
		if (ToolbarListener.flag)
			System.out.println("FrequencyFrame redrawn");

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
				if (x < 3600) {
					switch (ToolbarListener.getPMUnumber()) {
					case 1:
						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(1).getData().get(x));
						break;
					case 2:
						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(2).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(3).getData().get(x));
						break;
					case 3:
						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(3).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(4).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(5).getData().get(x));
						break;
					case 4:
						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(4).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(5).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(6).getData().get(x));
						series4.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(7).getData().get(x));
						break;
					case 5:
						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(5).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(6).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(7).getData().get(x));
						series4.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(8).getData().get(x));
						series5.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(9).getData().get(x));
						break;
					case 6:
						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(6).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(7).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(8).getData().get(x));
						series4.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(9).getData().get(x));
						series5.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(10).getData().get(x));
						series6.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(11).getData().get(x));

						break;
					case 7:
						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(7).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(8).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(9).getData().get(x));
						series4.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(10).getData().get(x));
						series5.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(11).getData().get(x));
						series6.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(12).getData().get(x));
						series7.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(13).getData().get(x));

						break;
					case 8:
						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(8).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(9).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(10).getData().get(x));
						series4.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(11).getData().get(x));
						series5.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(12).getData().get(x));
						series6.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(13).getData().get(x));
						series7.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(14).getData().get(x));
						series8.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(15).getData().get(x));
						break;
					case 9:
						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(9).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(10).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(11).getData().get(x));
						series4.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(12).getData().get(x));
						series5.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(13).getData().get(x));
						series6.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(14).getData().get(x));
						series7.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(15).getData().get(x));
						series8.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(16).getData().get(x));
						series9.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(17).getData().get(x));

						break;
					case 10:
						series1.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(10).getData().get(x));
						series2.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(11).getData().get(x));
						series3.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(12).getData().get(x));
						series4.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(13).getData().get(x));
						series5.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(14).getData().get(x));
						series6.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(15).getData().get(x));
						series7.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(16).getData().get(x));
						series8.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(17).getData().get(x));
						series9.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(18).getData().get(x));
						series10.add(TimeConstants.timePublic[x],
								GetDBData.pmuList.get(19).getData().get(x));
						break;
					}
					x++;
				} else if (x == 3600) {
					scheduledExecutorService.shutdown();
					// future.cancel(true);
					x = 0;
				}
			}

		};

		// pass null as first param to remove title
		// otherwise pass string to get title
		// first bool param is for legend
		freqChart = ChartFactory.createXYLineChart(null,
				"time (in seconds)", "Frequency", ds, PlotOrientation.VERTICAL,
				true, true, false);

		// set the starting value of the chart
		freqPlot = freqChart.getXYPlot();
		// get range axis is y
		final ValueAxis yAxis = freqPlot.getRangeAxis();
		// get domain axis is x
		final ValueAxis xAxis = freqPlot.getDomainAxis();
		xAxis.setAutoRange(true);
		xAxis.setFixedAutoRange(X_RANGE);
		// set the tick units for the x axis
		final NumberAxis nXAxis = (NumberAxis) freqPlot.getDomainAxis();
		nXAxis.setTickUnit(new NumberTickUnit(X_TICK_UNIT));
		// set range for y axis
		yAxis.setRange(INITIAL_LOWER_Y, INITIAL_UPPER_Y);

		// create dashed stroke
		Stroke dashedStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 0, new float[] { 2, 2 }, 0);

		// Axis a = p.getRangeAxis();
		freqPlot.setDomainCrosshairStroke(dashedStroke);
		freqPlot.setRangeCrosshairStroke(dashedStroke);
		freqPlot.setDomainCrosshairPaint(Color.WHITE);
		freqPlot.setRangeCrosshairPaint(Color.WHITE);
		freqPlot.setDomainCrosshairVisible(true);
		freqPlot.setRangeCrosshairVisible(true);
		// set color of line
		// lines are set at 0 index
		freqPlot.getRenderer().setSeriesPaint(0, TimeConstants.heatMap.get(0));
		freqPlot.getRenderer().setSeriesPaint(1, TimeConstants.heatMap.get(1));
		freqPlot.getRenderer().setSeriesPaint(2, TimeConstants.heatMap.get(2));
		freqPlot.getRenderer().setSeriesPaint(3, TimeConstants.heatMap.get(3));
		freqPlot.getRenderer().setSeriesPaint(4, TimeConstants.heatMap.get(4));
		freqPlot.getRenderer().setSeriesPaint(5, TimeConstants.heatMap.get(5));
		freqPlot.getRenderer().setSeriesPaint(6, TimeConstants.heatMap.get(6));
		freqPlot.getRenderer().setSeriesPaint(7, TimeConstants.heatMap.get(7));
		freqPlot.getRenderer().setSeriesPaint(8, TimeConstants.heatMap.get(8));
		freqPlot.getRenderer().setSeriesPaint(9, TimeConstants.heatMap.get(9));
		// p.getRenderer().setSeriesPaint(1, Color.YELLOW);

		cp = new ChartPanel(freqChart, true) {
			private static final long serialVersionUID = 1L;

			// restoreAutoBounds() will zoom the graph back to the original
			// scaling when the mouse is clicked and dragged to the left
			@Override
			public void restoreAutoBounds() {
				if (GetDBData.vMin != 0.0 && GetDBData.vMax != 0.0) {
					yAxis.setRange(new Range(GetDBData.fMin, GetDBData.fMax),
							true, true);
					xAxis.setAutoRange(true);
					xAxis.setFixedAutoRange(X_RANGE);
				}
			}
		};
		cp.getChart().removeLegend();
		cp.setDoubleBuffered(true);
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
	 * Returns the chart panel for the frequency graph
	 * 
	 * @return cp the chart panel
	 */
	public static ChartPanel getCp() {
		return cp;
	}

	/**
	 * Sets the chart panel of the frequency graph
	 * 
	 * @param cp
	 *            the chart panel to set
	 */
	public void setCp(ChartPanel cp) {
		FrequencyFrame.cp = cp;
	}
}