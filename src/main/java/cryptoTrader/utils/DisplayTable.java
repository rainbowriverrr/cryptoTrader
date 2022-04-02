package cryptoTrader.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

import java.awt.Dimension;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import cryptoTrader.gui.MainUI;

public class DisplayTable implements Observer {
	
	private TraderActionLog subject;

	public DisplayTable(TraderActionLog subject) {
		this.subject = subject;
		subject.attach(this);
	}

	@Override
	public void update(Subject changedSubject) {
		if (changedSubject.equals(subject)) display();
	}

	private void display() {
		Object[] columnNames = {"Trader","Strategy","CryptoCoin","Action","Quantity","Price","Date"};
			
		LogItem i1 = new LogItem("Strategy-A", "BTC", "Buy", 200, 101.60, new Date());
		i1.setTrader("Bob");
		LogItem i2 = new LogItem("Strategy-B", "ETH", "Sell", 300, 69.69, new Date());
		i2.setTrader("Sally");
		LogItem i3 = new LogItem("Strategy-C", "DOGE", "Buy", 150, 94.22, new Date());
		i3.setTrader("Jimmy");
		
		//ArrayList<LogItem> log = TraderActionLog.getInstance().getLog();
		ArrayList<LogItem> log = new ArrayList<LogItem>();
		log.add(i1);
		log.add(i2);
		log.add(i3);
		Object[][] data = new String[log.size()][7];
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < log.size(); i++) {
			String[] row = new String[7];
			LogItem item = log.get(i);
			row[0] = item.getTrader();
			row[1] = item.getStrategy();
			row[2] = item.getCoin();
			row[3] = item.getAction();
			row[4] = String.valueOf(item.getQuantity());
			row[5] = String.valueOf(item.getPrice());
			
			// Date
			cal.setTime(item.getDate());
			String dateStr = "";
			dateStr += cal.get(Calendar.DAY_OF_MONTH);
			dateStr += "-" + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			dateStr += "-" + cal.get(Calendar.YEAR);
			row[6] = dateStr;
			
			data[i] = row;
		}
		
		JTable table = new JTable(data, columnNames);
		table.setEnabled(false);
		table.setGridColor(Color.LIGHT_GRAY);
		table.setPreferredSize(new Dimension(600, 300));
		table.setFillsViewportHeight(true);
			
		JScrollPane tablePane = new JScrollPane(table);
		tablePane.setBorder (BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Trader Actions",
				TitledBorder.CENTER,
				TitledBorder.TOP));
		tablePane.setPreferredSize(new Dimension(800, 300));
		
		MainUI.getInstance().updateLogTable(tablePane);
	}
}
