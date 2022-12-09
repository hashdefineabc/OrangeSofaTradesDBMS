package view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

public class DisplayStocks extends JFrame {
  private JButton okButton;

  public DisplayStocks(List<String[]> stocksToDisplay) {
    super("Stocks");
    this.setSize(400, 400);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());


    int size = stocksToDisplay.size();
    String[] columnNames = {"Stock Name",
            "Number of Units",
            "Buy/Sell",
            "Date of Transaction",
            "Price"};

    String[][] data = new String[size][5];
    int i = 0;

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    for (String[] stock : stocksToDisplay) {
      String[] singleStock = new String[6];
      singleStock[0] = stock[0];
      singleStock[1] = stock[1];
      singleStock[2] = stock[2];
      singleStock[3] = stock[3];
      singleStock[4] = stock[4];

      data[i++] = singleStock;
    }

    JTable table = new JTable(data, columnNames);

    this.add(table.getTableHeader(), BorderLayout.PAGE_START);
    this.add(table, BorderLayout.CENTER);

    //ok button

    okButton = new JButton("Ok");
    okButton.setActionCommand("okFromDisplayStocks");

    JPanel buttonsPanel = new JPanel();
    buttonsPanel.add(okButton);
    this.add(buttonsPanel, BorderLayout.PAGE_END);

    this.setVisible(true);
    this.pack();
  }

  public void addActionListener(ActionListener listener) {
    okButton.addActionListener(listener);
  }
}
