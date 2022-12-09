package view;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;

public class HomeView extends JFrame {

  private JButton create;
  private JButton exit;
  private JButton buyStocks;
  private JButton sellStocks;
  private JButton costBasis;
  private JButton value;
  private JButton composition;
  private JButton displayChart;


  public HomeView(String s) {
    super(s);
    this.setSize(400, 400);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(7, 1));

    // Create a new Portfolio
    create = new JButton("Create a new Portfolio");
    create.setActionCommand("create");
    panel.add(create);


    // Buy stocks
    buyStocks = new JButton("Buy Stocks");
    buyStocks.setActionCommand("buyStocks");
    panel.add(buyStocks);

    // Sell stocks
    sellStocks = new JButton("Sell Stocks");
    sellStocks.setActionCommand("sellStocks");
    panel.add(sellStocks);


    // cost basis
    costBasis = new JButton("Check Cost Basis");
    costBasis.setActionCommand("costBasis");
    panel.add(costBasis);

    // value
    value = new JButton("Check Value");
    value.setActionCommand("valueButtonHome");
    panel.add(value);


    // composition
    composition = new JButton("View Composition");
    composition.setActionCommand("compositionButtonHome");
    panel.add(composition);

    // chart
    displayChart = new JButton("Display Performance Chart");
    displayChart.setActionCommand("displayChartHome");
    panel.add(displayChart);


    // Exit
    JPanel exitbuttonPanel = new JPanel();

    exit = new JButton("Exit");
    exit.setActionCommand("exit");
    exitbuttonPanel.add(exit);

    this.getContentPane().add(panel);
    this.add(exitbuttonPanel, BorderLayout.PAGE_END);
    this.setVisible(true);
//    this.pack();
  }

  public void addActionListener(ActionListener listener) {
    create.addActionListener(listener);
    exit.addActionListener(listener);
    buyStocks.addActionListener(listener);
    sellStocks.addActionListener(listener);
    costBasis.addActionListener(listener);
    value.addActionListener(listener);
    composition.addActionListener(listener);
    displayChart.addActionListener(listener);
  }
}
