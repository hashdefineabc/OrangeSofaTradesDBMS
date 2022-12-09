package view;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;

public class BuySellStocksView extends JFrame {

  private JComboBox tickerNameComboBox;
  private JTextField numUnitsTextField;
  private JComboBox portfolioNameComboBox;
  private JComboBox dateComboBox;
  private JComboBox brokerIdComboBox;
  private JButton save;
  private JButton cancel;
  private JLabel popUpMsg;
  private int selectedPortfolioIndex;
  private String selectedPortfolioName;
  private String selectedDate;
  private String selectedTickerName;
  int selectedBrokerId;

  private Boolean buyOrSell;


  public BuySellStocksView(String title) {
    super(title);
    this.setSize(400, 400);
    popUpMsg = new JLabel("");
    JPanel popUpPanel = new JPanel();
    popUpPanel.add(popUpMsg);

    //Portfolio name panel
    JPanel portfolioNamePanel = new JPanel();
    JLabel portfolioNameLabel = new JLabel("Portfolio Name: ");

    portfolioNameComboBox = new JComboBox();
    portfolioNamePanel.add(portfolioNameLabel);
    portfolioNamePanel.add(portfolioNameComboBox);

    ActionListener actionListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selectedPortfolioIndex = portfolioNameComboBox.getSelectedIndex();
        selectedPortfolioName = (String) portfolioNameComboBox.getSelectedItem();
      }
    };
    portfolioNameComboBox.addActionListener(actionListener);

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

    // num of units panel

    JPanel numUnitsPanel = new JPanel();
    JLabel numUnitsLabel = new JLabel("Number of Units: ");
    numUnitsTextField = new JTextField(5);
    numUnitsPanel.add(numUnitsLabel);
    numUnitsPanel.add(numUnitsTextField);

    //broker id combo box
    JPanel brokerIdPanel = new JPanel();
    JLabel brokerIdLabel = new JLabel("Broker: ");
    brokerIdComboBox = new JComboBox();
    brokerIdPanel.add(brokerIdLabel);
    brokerIdPanel.add(brokerIdComboBox);
    ActionListener brokerActionListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selectedBrokerId = (int) brokerIdComboBox.getSelectedItem();
      }
    };
    brokerIdComboBox.addActionListener(brokerActionListener);

    //date of transaction panel

    JPanel datePanel = new JPanel();
    JLabel dateOfTransactionLabel = new JLabel("Date of Transaction: ");

    dateComboBox = new JComboBox();

    ActionListener dateListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selectedDate = (String) dateComboBox.getSelectedItem();
      }
    };
    dateComboBox.addActionListener(dateListener);


    datePanel.add(dateOfTransactionLabel);
    datePanel.add(dateComboBox);


    JPanel buttonPanel = new JPanel();
    save = new JButton("Save");
    save.setActionCommand("saveStock");

    cancel = new JButton("Cancel");
    cancel.setActionCommand("cancelFromBuy");

    buttonPanel.add(save);
    buttonPanel.add(cancel);

    JPanel buyStocksWholePanel = new JPanel();
    buyStocksWholePanel.setLayout(new GridLayout(6, 1));
    buyStocksWholePanel.add(portfolioNamePanel);
    buyStocksWholePanel.add(tickerNamePanel);
    buyStocksWholePanel.add(numUnitsPanel);
    buyStocksWholePanel.add(brokerIdPanel);
    buyStocksWholePanel.add(datePanel);

    this.add(buyStocksWholePanel, BorderLayout.PAGE_START);
    this.add(buttonPanel, BorderLayout.PAGE_END);

    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
//    this.pack();
  }

  public String[] getInput() {
    String[] input = new String[4];
    input[0] = (String) tickerNameComboBox.getSelectedItem();
    input[1] = numUnitsTextField.getText();

    return input;
  }

  public void addActionListener(ActionListener listener) {
    save.addActionListener(listener);
    cancel.addActionListener(listener);
  }

  public void setPopUp(String message) {
    JOptionPane.showMessageDialog(BuySellStocksView.this, message, "Warning",
            JOptionPane.WARNING_MESSAGE);
    popUpMsg.setText(message);
  }

  public void displaySuccess(String message) {
    JOptionPane.showMessageDialog(BuySellStocksView.this, message, "Yayyyy",
            JOptionPane.INFORMATION_MESSAGE);
  }

  public void updateExistingPortfoliosList(List<String> existingPortfolios) {
    DefaultComboBoxModel tempComboBox = new DefaultComboBoxModel();
    for (String portfolio : existingPortfolios) {
      tempComboBox.addElement(portfolio);
    }
    this.portfolioNameComboBox.setModel(tempComboBox);
  }

  public void setTickerNameComboBox(List<String> tickerNames) {
    DefaultComboBoxModel tempComboBox = new DefaultComboBoxModel();
    for (String tickers : tickerNames) {
      tempComboBox.addElement(tickers);
    }
    this.tickerNameComboBox.setModel(tempComboBox);
  }

  public void updateExistingBrokersList(List<Integer> existingBrokers) {
    DefaultComboBoxModel tempComboBox = new DefaultComboBoxModel();
    for (int broker : existingBrokers) {
      tempComboBox.addElement(broker);
    }
    this.brokerIdComboBox.setModel(tempComboBox);
  }


  public void updateExistingDatesList(List<String> availableDates) {
    DefaultComboBoxModel tempComboBox = new DefaultComboBoxModel();
    for (String date : availableDates) {
      tempComboBox.addElement(date);
    }
    this.dateComboBox.setModel(tempComboBox);
  }

  public void clear() {
    numUnitsTextField.setText("");
  }

  public int getSelectedPortfolioIndex() {
    return selectedPortfolioIndex;
  }

  public String getSelectedPortfolioName() {
    return selectedPortfolioName;
  }

  public int getSelectedBrokerId() {
    return selectedBrokerId;
  }

  public String getSelectedDate() {
    return selectedDate;
  }

  public Boolean getBuyOrSell() {
    return buyOrSell;
  }

  public void setBuyOrSell(Boolean buyOrSell) {
    this.buyOrSell = buyOrSell;
  }
}
