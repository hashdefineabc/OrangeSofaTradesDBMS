package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDate;
import java.util.Map;

import javax.swing.*;

public class PerformanceChartGUIView extends JFrame {
  private JButton back;

  public PerformanceChartGUIView(Map<LocalDate, String> chart) {
    this.setSize(1050, 1000);
    this.getContentPane().add(new BarChart(chart), BorderLayout.CENTER);

    WindowListener winListener = new WindowAdapter() {
      public void windowClosing(WindowEvent event) {
        System.exit(0);
      }
    };
    this.addWindowListener(winListener);

    JPanel buttonPanel = new JPanel();

    back = new JButton("Back");
    back.setActionCommand("backFromPerformanceChart");
    buttonPanel.add(back);

    this.add(buttonPanel, BorderLayout.PAGE_END);

    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
  }

  public void addActionListener(ActionListener listener) {
    back.addActionListener(listener);
  }
}
