package view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

public class CreateNewPortfolioView extends JFrame {

  private JTextField portfolioName;
  private JButton createPortfolio;
  private JButton home;
  private JPanel panel;
  JPanel buttonsPanel = new JPanel();

  public CreateNewPortfolioView(String title) {
    super(title);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());

    panel = new JPanel();

    JLabel label = new JLabel("Name of Portfolio: ");
    portfolioName = new JTextField(15);

    createPortfolio = new JButton("Create Portfolio");
    createPortfolio.setActionCommand("createPortfolio");

    home = new JButton("Home");
    home.setActionCommand("homeFromCreatePortfolio");

    panel.add(label);
    panel.add(portfolioName);
    panel.add(createPortfolio);

    buttonsPanel.add(createPortfolio);
//    buttonsPanel.add(buyStocks);
    buttonsPanel.add(home);

    this.add(panel, BorderLayout.CENTER);
    this.add(buttonsPanel, BorderLayout.PAGE_END);

    this.add(panel);
    this.setVisible(true);
//    this.pack();
    this.setSize(400, 150);

  }


  public void addActionListener(ActionListener listener) {
    createPortfolio.addActionListener(listener);
    home.addActionListener(listener);
//    buyStocks.addActionListener(listener);
  }

  public List<String> getInput() {
    List<String> inp = new ArrayList<>();
    inp.add(portfolioName.getText());
    return inp;
  }

  public void setPopUp(String message) {
    JOptionPane.showMessageDialog(CreateNewPortfolioView.this, message, "Warning", JOptionPane.WARNING_MESSAGE);
  }

  public void clearField() {
    portfolioName.setText("");
  }
}
