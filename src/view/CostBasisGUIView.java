package view;

import java.awt.GridLayout;
import java.awt.BorderLayout;
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

public class CostBasisGUIView extends JFrame {
  private JComboBox portfolioNameComboBox;
  private String selectedPortfolioName;
  private JLabel popUpMsg;
  private JButton viewCostBasisButton;
  private JButton cancelButton;


  public CostBasisGUIView(String title) {
    super(title);
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
    viewCostBasisButton = new JButton("View Cost Basis");
    viewCostBasisButton.setActionCommand("viewCostBasisButton");

    cancelButton = new JButton("Cancel");
    cancelButton.setActionCommand("cancelFromCostBasis");

    buttonPanel.add(viewCostBasisButton);
    buttonPanel.add(cancelButton);

    popUpMsg = new JLabel("");
    JPanel popUpPanel = new JPanel();
    popUpPanel.add(popUpMsg);

    JPanel costBasisWholePanel = new JPanel();
    costBasisWholePanel.setLayout(new GridLayout(2, 1));
    this.add(portfolioNamePanel);
//    costBasisWholePanel.add(portfolioNameLabel);
//    costBasisWholePanel.add(portfolioNameComboBox);

    this.add(costBasisWholePanel, BorderLayout.PAGE_START);
    this.add(buttonPanel, BorderLayout.PAGE_END);

    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
//    this.pack();
    this.setSize(400, 150);

  }

  public void setPopUp(String message) {
    JOptionPane.showMessageDialog(CostBasisGUIView.this, message,
            "Cost Basis", JOptionPane.INFORMATION_MESSAGE);
    popUpMsg.setText(message);
  }

  public void addActionListener(ActionListener listener) {
    viewCostBasisButton.addActionListener(listener);
    cancelButton.addActionListener(listener);
  }


  public void setErrorPopUp(String message) {
    JOptionPane.showMessageDialog(CostBasisGUIView.this, message,
            "Cost Basis", JOptionPane.ERROR_MESSAGE);
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
