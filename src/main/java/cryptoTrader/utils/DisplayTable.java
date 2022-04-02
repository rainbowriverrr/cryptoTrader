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
	
	  private static DisplayTable instance;
	  private TraderActionLog subject;
	  
	  public static DisplayTable getInstance() {
			if (instance == null) instance = new DisplayTable(TraderActionLog.getInstance());
			return instance;
	  }

	  public DisplayTable(TraderActionLog subject) {
	    this.subject = subject;
	    subject.attach(this);
	  }

	  @Override
	  public void update(Subject changedSubject) {
	    if (changedSubject.equals(subject))
	      display();
	  }

	  private void display() {
		  System.out.print("Display table");
		// Dummy dates for demo purposes. These should come from selection menu
			Object[] columnNames = {"Trader","Strategy","CryptoCoin","Action","Quantity","Price","Date"};
			
			LogItem i1 = new LogItem("Strategy-A", "BTC", "Buy", 200, 101.60, new Date());
			i1.setTrader("Bob");
			LogItem i2 = new LogItem("Strategy-B", "ETH", "Sell", 300, 69.69, new Date());
			i2.setTrader("Sally");
			LogItem i3 = new LogItem("Strategy-C", "DOGE", "Buy", 150, 94.22, new Date());
			i3.setTrader("Jimmy");
			
			ArrayList<LogItem> log = TraderActionLog.getInstance().getLog();
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
			
			/*
			// Dummy data for demo purposes. These should come from actual fetcher
			Object[][] data = {
					{"Trader-1", "Strategy-A", "ETH", "Buy", "500", "150.3","13-January-2022"},
					{"Trader-2", "Strategy-B", "BTC", "Sell", "200", "50.2","13-January-2022"},
					{"Trader-3", "Strategy-C", "USDT", "Buy", "1000", "2.59","15-January-2022"},
					{"Trader-1", "Strategy-A", "USDC", "Buy", "500", "150.3","16-January-2022"},
					{"Trader-2", "Strategy-B", "ADA", "Sell", "200", "50.2","16-January-2022"},
					{"Trader-3", "Strategy-C", "SOL", "Buy", "1000", "2.59","17-January-2022"},
					{"Trader-1", "Strategy-A", "ONE", "Buy", "500", "150.3","17-January-2022"},
					{"Trader-2", "Strategy-B", "MANA", "Sell", "200", "50.2","17-January-2022"},
					{"Trader-3", "Strategy-C", "AVAX", "Buy", "1000", "2.59","19-January-2022"},
					{"Trader-1", "Strategy-A", "LUNA", "Buy", "500", "150.3","19-January-2022"},
					{"Trader-2", "Strategy-B", "FTM", "Sell", "200", "50.2","19-January-2022"},
					{"Trader-3", "Strategy-C", "HNT", "Buy", "1000", "2.59","20-January-2022"}
			};
			*/

			JTable table = new JTable(data, columnNames);
			table.setEnabled(false);
			table.setGridColor(Color.LIGHT_GRAY);
			//table.setPreferredSize(new Dimension(600, 300));
			
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
	                "Trader Actions",
	                TitledBorder.CENTER,
	                TitledBorder.TOP));
			
		
			
			scrollPane.setPreferredSize(new Dimension(800, 300));
			table.setFillsViewportHeight(true);;
			
			MainUI.getInstance().updateStats(scrollPane);


	  }
	}

