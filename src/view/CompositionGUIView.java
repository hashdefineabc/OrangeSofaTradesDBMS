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


public class CompositionGUIView extends JFrame {
  private JComboBox portfolioNameComboBox;
  private JButton viewCompositionButton;
  private JButton cancelButton;
  private String selectedPortfolioName;


  public CompositionGUIView(String title) {
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
    viewCompositionButton = new JButton("View Composition");
    viewCompositionButton.setActionCommand("viewCompositionButton");

    cancelButton = new JButton("Cancel");
    cancelButton.setActionCommand("cancelFromComposition");

    buttonPanel.add(viewCompositionButton);
    buttonPanel.add(cancelButton);

    JPanel compositionWholePanel = new JPanel();
    compositionWholePanel.setLayout(new GridLayout(2, 1));
    compositionWholePanel.add(portfolioNamePanel);

    this.add(compositionWholePanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.PAGE_END);

    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);

  }

  public void setPopUp(String message) {
    JOptionPane.showMessageDialog(CompositionGUIView.this, message,
            "Composition", JOptionPane.INFORMATION_MESSAGE);
  }

  public void addActionListener(ActionListener listener) {
    viewCompositionButton.addActionListener(listener);
    cancelButton.addActionListener(listener);
  }


  public void setErrorPopUp(String message) {
    JOptionPane.showMessageDialog(CompositionGUIView.this, message,
            "Composition", JOptionPane.ERROR_MESSAGE);
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
