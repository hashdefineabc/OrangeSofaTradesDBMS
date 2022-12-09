package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;

public class ValueGUIView extends JFrame {
  private JComboBox portfolioNameComboBox;
  private JLabel popUpMsg;
  private JButton viewValueButton;
  private JButton cancelButton;
  private String selectedPortfolioName;

  public ValueGUIView(String title) {
    super(title);
    this.setSize(400, 150);
    //Portfolio name panel
    JPanel portfolioNamePanel = new JPanel();
    JLabel portfolioNameLabel = new JLabel("Portfolio Name: ");

    portfolioNameComboBox = new JComboBox();
    portfolioNamePanel.add(portfolioNameLabel);
    portfolioNamePanel.add(portfolioNameComboBox);

    ActionListener actionListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        selectedPortfolioName = (String) portfolioNameComboBox.getSelectedItem();
      }
    };
    portfolioNameComboBox.addActionListener(actionListener);

    JPanel buttonPanel = new JPanel();
    viewValueButton = new JButton("View Value");
    viewValueButton.setActionCommand("viewValueButton");

    cancelButton = new JButton("Cancel");
    cancelButton.setActionCommand("cancelFromValue");

    buttonPanel.add(viewValueButton);
    buttonPanel.add(cancelButton);

    popUpMsg = new JLabel("");
    JPanel popUpPanel = new JPanel();
    popUpPanel.add(popUpMsg);

    JPanel valueWholePanel = new JPanel();
    valueWholePanel.setLayout(new GridLayout(2, 1));
    valueWholePanel.add(portfolioNamePanel);

    this.add(valueWholePanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.PAGE_END);

    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
//    this.pack();


  }

  public void setPopUp(String message) {
    JOptionPane.showMessageDialog(ValueGUIView.this, message, "Value",
            JOptionPane.INFORMATION_MESSAGE);
    popUpMsg.setText(message);
  }

  public void addActionListener(ActionListener listener) {
    viewValueButton.addActionListener(listener);
    cancelButton.addActionListener(listener);
  }


  public void setErrorPopUp(String message) {
    JOptionPane.showMessageDialog(ValueGUIView.this, message, "Value", JOptionPane.ERROR_MESSAGE);
  }

  public void updateExistingPortfoliosList(List<String> existingPortfolios) {
    DefaultComboBoxModel tempComboBox = new DefaultComboBoxModel();
    for (String portfolio : existingPortfolios) {
      tempComboBox.addElement(portfolio);
    }

    this.portfolioNameComboBox.setModel(tempComboBox);
  }

  public String getSelectedPortfolioName() {
    return selectedPortfolioName;
  }
}
