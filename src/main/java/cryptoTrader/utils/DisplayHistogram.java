package cryptoTrader.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;

import cryptoTrader.gui.MainUI;

/**
 * Used to generate the histogram.
 */
public class DisplayHistogram implements Observer {
	
	private TraderActionLog subject; // DisplayHistogram is observer to TraderActionLog.
	private DefaultCategoryDataset data;
	private CategoryPlot plot;
	private JFreeChart hist;
	private ChartPanel chartPanel;

	/**
	 * Constructs a DisplayHistogram that observes the given subject.
	 * @param subject
	 */
	public DisplayHistogram(TraderActionLog subject) {
		this.subject = subject;
		subject.attach(this);
		
		data = new DefaultCategoryDataset();
		plot = new CategoryPlot();
		plot.setDataset(data);
		plot.setRenderer(new BarRenderer());
		plot.setDomainAxis(new CategoryAxis("Strategy"));
		NumberAxis yAxis = new NumberAxis("Actions Performed (Buy or Sell)");
		yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		plot.setRangeAxis(yAxis);
		hist = new JFreeChart("Actions Performed By Traders So Far", new Font("Serif", java.awt.Font.BOLD, 18), plot, true);
		chartPanel = new ChartPanel(hist);
		chartPanel.setPreferredSize(new Dimension(650, 300));
		chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		chartPanel.setBackground(Color.white);
	}

	/**
	 * Triggered when the subject is changed. Calls method to create the histogram.
	 */
	@Override
	public void update(Subject changedSubject) {
		if (changedSubject.equals(subject)) createHistogram();
	}

	/**
	 * Gets the log from TraderActionLog, creates the histogram, and passes it to MainUI.
	 */
	private void createHistogram() {
		ArrayList<LogItem> log = subject.getLog();
		
		for (int i = 0; i < log.size(); i++) {
			LogItem item = log.get(i);
			String trader = item.getTrader();
			String strat = item.getStrategy();
			boolean isExistingRowKey = false;
			List<String> rowKeys = data.getRowKeys();
			for (String key : rowKeys) {
				if (key.equals(trader)) isExistingRowKey = true;
			}
			boolean isExistingColKey = false;
			List<String> colKeys = data.getColumnKeys();
			for (String key : colKeys) {
				if (key.equals(strat)) isExistingColKey = true;
			}
			if (isExistingRowKey && isExistingColKey) data.incrementValue(1, item.getTrader(), item.getStrategy());
			else data.addValue(1, trader, strat);
		}
		
		/* Example code
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		// Those are hard-coded values!!!! 
		// You will have to come up with a proper datastructure to populate the BarChart with live data!
		dataset.setValue(6, "Trader-1", "Strategy-A");
		dataset.setValue(5, "Trader-2", "Strategy-B");
		dataset.setValue(0, "Trader-3", "Strategy-E");
		dataset.setValue(1, "Trader-4", "Strategy-C");
		dataset.setValue(10, "Trader-5", "Strategy-D");

		CategoryPlot plot = new CategoryPlot();
		BarRenderer barrenderer1 = new BarRenderer();

		plot.setDataset(0, dataset);
		plot.setRenderer(0, barrenderer1);
		CategoryAxis domainAxis = new CategoryAxis("Strategy");
		plot.setDomainAxis(domainAxis);
		LogAxis rangeAxis = new LogAxis("Actions(Buys or Sells)");
		rangeAxis.setRange(new Range(1.0, 20.0));
		plot.setRangeAxis(rangeAxis);
	
		// plot.mapDatasetToRangeAxis(0, 0);// 1st dataset to 1st y-axis
		// plot.mapDatasetToRangeAxis(1, 1); // 2nd dataset to 2nd y-axis
		
		// End of example code*/
		
		MainUI.getInstance().updateHist(chartPanel); // Adds the new histogram to the UI.
		
	}
}
