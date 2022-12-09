package view;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

public class DisplayChartGUIView extends JFrame {
  private JButton back;
  private JButton viewPerformanceButton;
  private JComboBox tickerNameComboBox;
  private String selectedTickerName;

  public DisplayChartGUIView() {
    super("Display Chart");

    //Ticker Name panel

    //ticker Name panel

    JPanel tickerNamePanel = new JPanel();
    JLabel tickerNameLabel = new JLabel("Ticker Name: ");
    tickerNameComboBox = new JComboBox();
    tickerNamePanel.add(tickerNameLabel);
    tickerNamePanel.add(tickerNameComboBox);

    ActionListener tickerNameActionListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selectedTickerName = (String) tickerNameComboBox.getSelectedItem();
      }
    };
    tickerNameComboBox.addActionListener(tickerNameActionListener);

    // prev performance
    viewPerformanceButton = new JButton("View Performance");

    viewPerformanceButton.setActionCommand("performance");

    JPanel buttonPanel = new JPanel();

    back = new JButton("Back");
    back.setActionCommand("backFromDisplayChart");
    buttonPanel.add(viewPerformanceButton);
    buttonPanel.add(back);

    this.add(tickerNamePanel, BorderLayout.PAGE_START);
    this.add(buttonPanel, BorderLayout.PAGE_END);

    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
    this.pack();
  }

  public void addActionListener(ActionListener listener) {
    back.addActionListener(listener);
    viewPerformanceButton.addActionListener(listener);
  }

  public String getSelectedTickerName() {
    return selectedTickerName;
  }

  public void updateExistingPortfoliosList(List<String> availableTickerNames) {
    DefaultComboBoxModel tempComboBox = new DefaultComboBoxModel();
    for (String portfolio : availableTickerNames) {
      tempComboBox.addElement(portfolio);
    }
    this.tickerNameComboBox.setModel(tempComboBox);
  }

  public void showPopUp(String message) {
    JOptionPane.showMessageDialog(DisplayChartGUIView.this, message, "Warning",
            JOptionPane.WARNING_MESSAGE);
  }
}
