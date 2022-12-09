package view;

import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JButton;

public class BarChart extends JPanel {

  String[] dates;
  Integer[] value;
  String title;

  private JButton close;

  public BarChart(Map<LocalDate, String> chart) {
    dates = new String[chart.size()];
    value = new Integer[chart.size()];
    title = "Chart";
    int i = 0;
    for (Map.Entry<LocalDate, String> entry : chart.entrySet()) {
      dates[i] = String.valueOf(entry.getKey());
      value[i] = Integer.valueOf(entry.getValue().length());
      i++;
    }

  }

  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    if (value == null || value.length == 0) {
      return;
    }
    int minValue = 0;
    int maxValue = 0;
    for (int i = 0; i < value.length; i++) {
      if (value[i] == null) {
        value[i] = 0;
      }
      if (minValue > value[i]) {
        minValue = value[i];
      }
      if (maxValue < value[i]) {
        maxValue = value[i];
      }
    }
    Dimension dim = getSize();
    int clientWidth = dim.width;
    int clientHeight = dim.height;
    int barWidth = clientWidth / value.length;
    Font titleFont = new Font("Book Antiqua", Font.BOLD, 15);
    FontMetrics titleFontMetrics = graphics.getFontMetrics(titleFont);
    Font labelFont = new Font("Book Antiqua", Font.PLAIN, 10);
    FontMetrics labelFontMetrics = graphics.getFontMetrics(labelFont);
    int titleWidth = titleFontMetrics.stringWidth(title);
    int q = titleFontMetrics.getAscent();
    int p = (clientWidth - titleWidth) / 2;
    graphics.setFont(titleFont);
    graphics.drawString(title, p, q);
    int top = titleFontMetrics.getHeight();
    int bottom = labelFontMetrics.getHeight();
    if (maxValue == minValue) {
      return;
    }
    double scale = (clientHeight - top - bottom) / (maxValue - minValue);
    q = clientHeight - labelFontMetrics.getDescent();
    graphics.setFont(labelFont);
    for (int j = 0; j < value.length; j++) {
      int valueP = j * barWidth + 1;
      int valueQ = top;
      int height = (int) (value[j] * scale);
      if (value[j] >= 0) {
        valueQ += (int) ((maxValue - value[j]) * scale);
      } else {
        valueQ += (int) (maxValue * scale);
        height = -height;
      }
      graphics.setColor(Color.blue);
      graphics.fillRect(valueP, valueQ, barWidth - 2, height);
      graphics.setColor(Color.black);
      graphics.drawRect(valueP, valueQ, barWidth - 2, height);
      if (dates[j] == null) {
        return;
      }
      int labelWidth = labelFontMetrics.stringWidth(dates[j]);
      p = j * barWidth + (barWidth - labelWidth) / 2;
      graphics.drawString(dates[j], p, q);
    }

  }
}
