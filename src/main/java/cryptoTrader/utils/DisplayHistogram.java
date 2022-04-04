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
import org.jfree.data.category.DefaultCategoryDataset;

import cryptoTrader.gui.MainUI;

/**
 * Used to generate the histogram.
 */
public class DisplayHistogram implements Observer {
	
	private TraderActionLog subject; // DisplayHistogram is observer to TraderActionLog.
	private DefaultCategoryDataset data; // Data of the histogram
	private CategoryPlot plot; // The plot with dataset data
	private JFreeChart hist; // The histogram made from plot
	private ChartPanel chartPanel; // The panel that holds the histogram

	/**
	 * Constructs a DisplayHistogram that observes the given subject. Initializes the histogram and chartPanel.
	 * @param subject
	 */
	public DisplayHistogram(TraderActionLog subject) {
		this.subject = subject;
		subject.attach(this);
		
		// Initialize histogram
		data = new DefaultCategoryDataset(); 
		plot = new CategoryPlot();
		plot.setDataset(data);
		plot.setRenderer(new BarRenderer());
		plot.setDomainAxis(new CategoryAxis("Strategy"));
		NumberAxis yAxis = new NumberAxis("Actions Performed (Buy or Sell)");
		yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // Integer ticks for y-axis
		plot.setRangeAxis(yAxis);
		hist = new JFreeChart("Actions Performed By Traders So Far", new Font("Serif", java.awt.Font.BOLD, 18), plot, true);
		
		// Initialize panel
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
		ArrayList<LogItem> log = subject.getLog(); // Log of trader actions
		
		// Iterates the log items and adds the data to the histogram.
		for (int i = 0; i < log.size(); i++) {
			LogItem item = log.get(i); // Current log item
			// Fail actions are not included in the histogram.
			if (!item.getAction().equals("FAIL")) {
				String trader = item.getTrader();
				String strat = item.getStrategy();
				// Check for if the trader and strategy keys exist to determine how to add the data.
				boolean isExistingRowKey = false;
				List<String> rowKeys = data.getRowKeys(); // data should only ever be Strings
				for (String key : rowKeys) {
					if (key.equals(trader)) isExistingRowKey = true;
				}
				boolean isExistingColKey = false;
				List<String> colKeys = data.getColumnKeys();// data should only ever be Strings
				for (String key : colKeys) {
					if (key.equals(strat)) isExistingColKey = true;
				}
				// If trader and strategy exist, increment the existing values.
				if (isExistingRowKey && isExistingColKey) data.incrementValue(1, item.getTrader(), item.getStrategy());
				// If trader does not exist, add it with all strategies set to 0, then increment the correct strategy.
				// This makes the histogram display all strategies, not just the ones with existing values.
				else if (!isExistingRowKey) {
					data.addValue(0, trader, "BTC Strategy A");
					data.addValue(0, trader, "ADA Strategy B");
					data.addValue(0, trader, "ETH Strategy C");
					data.addValue(0, trader, "DOGE Strategy D");
					data.addValue(0, trader, "ADA Strategy E");
					data.incrementValue(1, trader, strat);
				}
				// Else trader exists but strategy does not (0 actions for that strategy). Add the new data and set to 1.
				else {
					data.addValue(1, trader, strat);
				}
			}
		}
		
		MainUI.getInstance().updateHist(chartPanel); // Adds the new histogram to the UI.
		
	}
}
