package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import view.BuySellStocksView;
import view.CompositionGUIView;
import view.CostBasisGUIView;
import view.CreateNewPortfolioView;
import view.DisplayChartGUIView;
import view.DisplayStocks;
import view.HomeView;
import view.PerformanceChartGUIView;
import view.ValueGUIView;

public class GUIController implements ActionListener {

  private HomeView home;
  private CreateNewPortfolioView createNewPortfolioView;
  private Map<String, Runnable> actionMap;

  private BuySellStocksView buySellStock;
  private CostBasisGUIView costBasisView;
  private ValueGUIView valueView;
  private CompositionGUIView compositionView;
  private DisplayStocks displayComposition;
  private DisplayChartGUIView displayChartHome;
  private PerformanceChartGUIView chart;
  private Double scale = 0.0;


  List<String[]> stockList;
  List<String> existingPortfolios;
  List<String> availableDates;
  List<String> tickerNames = new ArrayList<>();
  List<Integer> existingBrokers;

  public int loggedInUserId;
  public int loggedInManagerId;
  public Connection conn;

  public GUIController(int loggedInUserId, int loggedInManagerId, HomeView view, Connection conn) {
    this.home = view;
    home.addActionListener(this);
    this.loggedInUserId = loggedInUserId;
    this.loggedInManagerId = loggedInManagerId;
    this.conn = conn;
    actionMap = initializeMap();
    tickerNames.add("GOOG");
    tickerNames.add("AAPL");
    tickerNames.add("MSFT");
    tickerNames.add("AMZN");
    tickerNames.add("TSLA");
  }

  private void creatingPortfolio(Map<String, Runnable> actionMap) {
    stockList = new ArrayList<>();
    actionMap.put("create", () -> {
      createNewPortfolioView = new CreateNewPortfolioView("create portfolio");
      createNewPortfolioView.addActionListener(this);
      createNewPortfolioView.setLocation(home.getLocation());
      home.dispose();
    });

    actionMap.put("createPortfolio", () -> {
      String portfolioName = createNewPortfolioView.getInput().get(0);
      if (portfolioName.length() == 0) {
        createNewPortfolioView.setPopUp("Enter portfolio name");
        return;
      }
      String query = "CALL insertPortfolio('" + portfolioName + "'," + loggedInUserId + ")";
      try {
        PreparedStatement ps = conn.prepareStatement(query);
        ps.executeUpdate();
        String message = "Portfolio " + portfolioName + " created successfully!!!";
        createNewPortfolioView.setPopUp(message);
        createNewPortfolioView.clearField();
        ps.close();
      } catch (SQLException e) {
        createNewPortfolioView.setPopUp("Portfolio name already exists, please try again...");
      }

    });
    actionMap.put("homeFromCreatePortfolio", () -> {
      home = new HomeView("Home");
      home.addActionListener(this);
      home.setLocation(createNewPortfolioView.getLocation());
      createNewPortfolioView.dispose();
    });

  }

  private void buyingStocks(Map<String, Runnable> actionMap) {
    actionMap.put("buyStocks", () -> {
      buySellStock = new BuySellStocksView("Buy Stocks");
      buySellStock.setBuyOrSell(true); // buy operation

      getExistingPortfolioNames();
      if (existingPortfolios.isEmpty()) {
        buySellStock.setPopUp("No portfolios exists for this user, please create a portfolio to buy stocks");
//        return;
      }
      buySellStock.updateExistingPortfoliosList(existingPortfolios);
      buySellStock.setTickerNameComboBox(tickerNames);

      //broker combobox
      getExistingBrokers();
      if (existingBrokers.isEmpty()) {
        buySellStock.setPopUp("No brokers exists!!! Sorry, can't make a transaction!!!");
//        return;
      }
      buySellStock.updateExistingBrokersList(existingBrokers);


      getExistingDates();
      buySellStock.updateExistingDatesList(availableDates);

      //hide home and display buystock
      buySellStock.addActionListener(this);
      buySellStock.setLocation(home.getLocation());
      buySellStock.setSize(300, 300);
      home.dispose();
    });
  }

  private void sellingStocks(Map<String, Runnable> actionMap) {
    actionMap.put("sellStocks", () -> {
      buySellStock = new BuySellStocksView("Sell Stocks");
      buySellStock.setBuyOrSell(false); // sell operation

      getExistingPortfolioNames();
      if (existingPortfolios.isEmpty()) {
        buySellStock.setPopUp("No portfolios exists for this user, please create a portfolio to sell stocks");
//        return;
      }
      buySellStock.updateExistingPortfoliosList(existingPortfolios);
      buySellStock.setTickerNameComboBox(tickerNames);

      //broker combobox
      getExistingBrokers();
      if (existingBrokers.isEmpty()) {
        buySellStock.setPopUp("No brokers exists!!! Sorry, can't make a transaction!!!");
//        return;
      }
      buySellStock.updateExistingBrokersList(existingBrokers);


      getExistingDates();
      buySellStock.updateExistingDatesList(availableDates);

      //hide home and display sellStock
      buySellStock.addActionListener(this);
      buySellStock.setLocation(home.getLocation());
      buySellStock.setSize(300, 300);
      home.dispose();
    });
  }

  private void getExistingPortfolioNames() {
    List<String> portfolioNames = new ArrayList<>();
    String query = "CALL getPortfolioNames(" + loggedInUserId + ")";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        portfolioNames.add(rs.getString(1));
      }
    } catch (SQLException e) {
      //
    }
    existingPortfolios = portfolioNames;
  }

  private void getExistingDates() {
    List<String> dates = new ArrayList<>();
    String query = "CALL getAvailableDates()";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        dates.add(rs.getString(1));
      }
    } catch (SQLException e) {
      //
    }
    availableDates = dates;
  }

  private void getExistingBrokers() {
    List<Integer> brokerIds = new ArrayList<>();
    String query = "CALL getBrokerId()";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        brokerIds.add(rs.getInt(1));
      }
    } catch (SQLException e) {
      //
    }
    existingBrokers = brokerIds;
  }

  private void saveStock(Map<String, Runnable> actionMap) {
    actionMap.put("saveStock", () -> {
      String[] stockDetails = new String[7];
      stockDetails = this.takeStockInput();
      if (stockDetails == null) {
        return;
      }
      stockDetails[4] = buySellStock.getBuyOrSell() ? Operation.BUY.toString() :
              Operation.SELL.toString();

      getExistingPortfolioNames();
      buySellStock.updateExistingPortfoliosList(existingPortfolios);
      buySellStock.setTickerNameComboBox(tickerNames);

      //broker combobox

      if (existingBrokers.isEmpty()) {
        buySellStock.setPopUp("No brokers exists!!! Sorry, can't make a transaction!!!");
//        return;
      }
      buySellStock.updateExistingBrokersList(existingBrokers);

      // dates combobox
      buySellStock.updateExistingDatesList(availableDates);

      String selectedPortfolioName = buySellStock.getSelectedPortfolioName();
      int selectedBrokerId = buySellStock.getSelectedBrokerId();
      stockDetails[5] = selectedPortfolioName;
      String selectedDate = buySellStock.getSelectedDate();
      if (selectedDate == null)
        selectedDate = "2022-11-28";

      String portfolioName = stockDetails[5];
      String stockName = stockDetails[0];
      Double numUnits = Double.valueOf(stockDetails[1]);
      int buyOrSell = stockDetails[4] == "BUY" ? 1 : 2;
      int brokerId = 1;
      if (Objects.equals(stockDetails[6], "0"))
        brokerId = 1;
      else
        brokerId = Integer.parseInt(stockDetails[6]);

      int portfolioId = 1;
      String queryToGetPortfolioId = "CALL getPortfolioId( " + loggedInUserId +
              ", '" + portfolioName + "')";
      try {
        PreparedStatement p1 = conn.prepareStatement(queryToGetPortfolioId);
        ResultSet rs = p1.executeQuery();
        if (rs.next())
          portfolioId = rs.getInt(1);
      } catch (SQLException e) {
        buySellStock.setPopUp("error getting portfolio id, please try again");
        return;
      }

      String query = "CALL insertTransaction('" + portfolioId + "','" +
              stockName + "'," +
              numUnits + ",'" +
              selectedDate + "'," +
              buyOrSell + "," +
              brokerId + "," +
              loggedInUserId + ")";

//      CALL insertTransaction('test portfolio1','GOOG',20.0,'2022-01-01',1,2, 2);

      try {
        PreparedStatement ps = conn.prepareStatement(query);
        ps.executeUpdate();
        buySellStock.displaySuccess("Transaction Success");
        buySellStock.clear();
        ps.close();
      } catch (SQLException e) {
        buySellStock.setPopUp("Error making the transaction, please try again");
      }
    });
  }

  private void cancelFromBuy(Map<String, Runnable> actionMap) {
    actionMap.put("cancelFromBuy", () -> {
      home = new HomeView("Home");

      //hide buy and display home
      home.addActionListener(this);
      home.setLocation(buySellStock.getLocation());
      this.buySellStock.dispose();
    });
  }

  private void cancelFromCostBasis(Map<String, Runnable> actionMap) {
    actionMap.put("cancelFromCostBasis", () -> {
      home = new HomeView("Home");

      //hide cost basis window and display home
      home.addActionListener(this);
      home.setLocation(costBasisView.getLocation());
      this.costBasisView.dispose();
    });
  }

  private void cancelFromValue(Map<String, Runnable> actionMap) {
    actionMap.put("cancelFromValue", () -> {
      home = new HomeView("Home");

      //hide cost basis window and display home
      home.addActionListener(this);
      home.setLocation(valueView.getLocation());
      this.valueView.dispose();
    });
  }

  private void cancelFromComposition(Map<String, Runnable> actionMap) {
    actionMap.put("cancelFromComposition", () -> {
      home = new HomeView("Home");

      //hide cost basis window and display home
      home.addActionListener(this);
      home.setLocation(compositionView.getLocation());
      this.compositionView.dispose();
    });
  }

  private void okFromDisplayStocks(Map<String, Runnable> actionMap) {
    actionMap.put("okFromDisplayStocks", () -> {
      compositionView = new CompositionGUIView("Composition");
      compositionView.updateExistingPortfoliosList(existingPortfolios);

      //hide cost basis window and display pick portfolio for composition view
      compositionView.addActionListener(this);
      compositionView.setLocation(displayComposition.getLocation());
      this.displayComposition.dispose();
    });
  }

  private Map<String, Runnable> initializeMap() {
    Map<String, Runnable> actionMap = new HashMap<>();

    actionMap.put("exit", () -> {
      System.exit(0);
    });

    creatingPortfolio(actionMap);
    buyingStocks(actionMap);
    cancelFromBuy(actionMap);
    saveStock(actionMap);
    sellingStocks(actionMap);
    costBasisFromHomeButton(actionMap);
    viewCostBasisButton(actionMap);
    cancelFromCostBasis(actionMap);
    valueButtonHome(actionMap);
    viewValue(actionMap);
    cancelFromValue(actionMap);
    compositionButtonHome(actionMap);
    viewCompositionButton(actionMap);
    cancelFromComposition(actionMap);
    okFromDisplayStocks(actionMap);
    displayChartHome(actionMap);
    backFromDisplayChart(actionMap);
    performance(actionMap);
    closeFromChart(actionMap);
    backFromPerformanceChart(actionMap);

    return actionMap;
  }

  private void viewCostBasisButton(Map<String, Runnable> actionMap) {
    actionMap.put("viewCostBasisButton", () -> {

      getExistingPortfolioNames();
      costBasisView.updateExistingPortfoliosList(existingPortfolios);

      try {
        // calculateCostBasis(portfolioId INT, userId INT)
        int portfolioId = 1;
        String portfolioName = costBasisView.getSelectedPortfolioName();
        String queryToGetPortfolioId = "CALL getPortfolioId( " + loggedInUserId +
                ", '" + portfolioName + "')";

        try {
          PreparedStatement p1 = conn.prepareStatement(queryToGetPortfolioId);
          ResultSet rs = p1.executeQuery();
          if (rs.next())
            portfolioId = rs.getInt(1);
        } catch (SQLException e) {
          costBasisView.setPopUp("error getting portfolio id, please try again");
          return;
        }

        String query = "SELECT calculateCostBasis( " + portfolioId + "," + loggedInUserId + ")";
        Double costBasis = 0.0;
        PreparedStatement p = conn.prepareStatement(query);
        ResultSet rs = p.executeQuery();
        if (rs.next())
          costBasis = rs.getDouble(1);
        costBasisView.setPopUp("Cost Basis  = " +
                String.format("%.2f", costBasis) + " USD");
      } catch (Exception e) {
        costBasisView.setErrorPopUp("Cost Basis couldn't be calculated!!!\nPlease try again...");
      }
    });
  }

  private void viewValue(Map<String, Runnable> actionMap) {
    actionMap.put("viewValueButton", () -> {

      getExistingPortfolioNames();
      valueView.updateExistingPortfoliosList(existingPortfolios);

      try {
        // calculateValue(4);
        int portfolioId = 1;
        String portfolioName = valueView.getSelectedPortfolioName();
        String queryToGetPortfolioId = "CALL getPortfolioId( " + loggedInUserId +
                ", '" + portfolioName + "')";

        try {
          PreparedStatement p1 = conn.prepareStatement(queryToGetPortfolioId);
          ResultSet rs = p1.executeQuery();
          if (rs.next())
            portfolioId = rs.getInt(1);
        } catch (SQLException e) {
          valueView.setPopUp("error getting portfolio id, please try again");
          return;
        }

        String query = "SELECT calculateValue( " + portfolioId + ")";
        Double value = 0.0;
        PreparedStatement p = conn.prepareStatement(query);
        ResultSet rs = p.executeQuery();
        if (rs.next())
          value = rs.getDouble(1);
        valueView.setPopUp("Value = " +
                String.format("%.2f", value) + " USD");
      } catch (Exception e) {
        valueView.setErrorPopUp("Value couldn't be calculated!!!\nPlease try again...");
      }
    });
  }

  private void viewCompositionButton(Map<String, Runnable> actionMap) {
    actionMap.put("viewCompositionButton", () -> {

      getExistingPortfolioNames();
      compositionView.updateExistingPortfoliosList(existingPortfolios);

      try {
        // calculateValue(4);
        int portfolioId = 1;
        String portfolioName = compositionView.getSelectedPortfolioName();
        String queryToGetPortfolioId = "CALL getPortfolioId( " + loggedInUserId +
                ", '" + portfolioName + "')";

        PreparedStatement p1 = conn.prepareStatement(queryToGetPortfolioId);
        ResultSet rs = p1.executeQuery();
        if (rs.next())
          portfolioId = rs.getInt(1);

//       call getComposition(4);
        String query = "CALL getComposition( " + portfolioId + ")";

        PreparedStatement p = conn.prepareStatement(query);
        rs = p.executeQuery();
        List<String[]> toDisplay = new ArrayList<>();
        while (rs.next()) {
          int i = 1;
          String[] oneStock = new String[5];
          int j = 0;
          while (i <= 5) {
            oneStock[j++] = rs.getString(i++);
          }
          toDisplay.add(oneStock);
        }

        if (toDisplay.size() == 0) {
          compositionView.setPopUp("No stocks are present in the portfolio at this date");
        } else {
          displayComposition = new DisplayStocks(toDisplay);
          displayComposition.addActionListener(this);
          displayComposition.setLocation(compositionView.getLocation());
          this.compositionView.dispose();
        }

      } catch (Exception e) {
        compositionView.setErrorPopUp("Can't get the composition at the moment!!!\nPlease try again later...");
      }

    });
  }

  private void costBasisFromHomeButton(Map<String, Runnable> actionMap) {
    actionMap.put("costBasis", () -> {
      costBasisView = new CostBasisGUIView("Cost Basis");

      getExistingPortfolioNames();
      costBasisView.updateExistingPortfoliosList(existingPortfolios);

      //hide home and display costBasis
      costBasisView.addActionListener(this);
      costBasisView.setLocation(home.getLocation());
      home.dispose();
    });
  }

  private void valueButtonHome(Map<String, Runnable> actionMap) {
    actionMap.put("valueButtonHome", () -> {
      valueView = new ValueGUIView("Value");

      getExistingPortfolioNames();
      valueView.updateExistingPortfoliosList(existingPortfolios);

      //hide home and display costBasis
      valueView.addActionListener(this);
      valueView.setLocation(home.getLocation());
      home.dispose();
    });
  }

  private void compositionButtonHome(Map<String, Runnable> actionMap) {
    actionMap.put("compositionButtonHome", () -> {
      compositionView = new CompositionGUIView("Composition");

      getExistingPortfolioNames();
      compositionView.updateExistingPortfoliosList(existingPortfolios);

      //hide home and display composition
      compositionView.addActionListener(this);
      compositionView.setLocation(home.getLocation());
      home.dispose();
    });
  }

  private String[] takeStockInput() {
    String[] inputFromUser = buySellStock.getInput();
    String tickerNameFromUser = inputFromUser[0];
    if (Objects.equals(tickerNameFromUser, "")) {
      buySellStock.setPopUp("Enter ticker name");
      return null;
    }
    if (Objects.equals(inputFromUser[1], "")) {
      buySellStock.setPopUp("Enter number of units");
      return null;
    }
    try {
      Double.parseDouble(inputFromUser[1]);
    } catch (NumberFormatException e) {
      buySellStock.setPopUp("Please enter a valid number of units");
      return null;
    }


    String[] s = new String[7];
    s[0] = tickerNameFromUser;
    s[1] = inputFromUser[1]; //num units
//    s[2] = inputFromUser[2]; //transactionDate
    s[2] = buySellStock.getSelectedDate(); //transactionDate

    s[4] = String.valueOf(Operation.BUY); //indicates shares are bought
    s[5] = buySellStock.getSelectedPortfolioName(); //portfolio name
    s[6] = String.valueOf(buySellStock.getSelectedBrokerId());

    return s;
  }


  private void displayChartHome(Map<String, Runnable> actionMap) {
    actionMap.put("displayChartHome", () -> {
      displayChartHome = new DisplayChartGUIView();
      String getTickerNamesQuery = "CALL getTickerNames()";
      List<String> tickerNamesFromDB = new ArrayList<>();
      try {
        PreparedStatement ps = conn.prepareStatement(getTickerNamesQuery);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          tickerNamesFromDB.add(rs.getString(1));
        }
      } catch (SQLException e) {
        displayChartHome.showPopUp("Error!!!!!!!!");
      }

      displayChartHome.updateExistingPortfoliosList(tickerNamesFromDB);
      String tickerNameSelected = displayChartHome.getSelectedTickerName();

      //hide home and display chart
      displayChartHome.addActionListener(this);
      displayChartHome.setLocation(home.getLocation());
      home.dispose();
    });
  }

  private void backFromDisplayChart(Map<String, Runnable> actionMap) {
    actionMap.put("backFromDisplayChart", () -> {
      home = new HomeView("Home");

      //hide cost basis window and display home
      home.addActionListener(this);
      home.setLocation(displayChartHome.getLocation());
      this.displayChartHome.dispose();
    });
  }

  private void backFromPerformanceChart(Map<String, Runnable> actionMap) {
    actionMap.put("backFromPerformanceChart", () -> {
      home = new HomeView("Home");

      home.addActionListener(this);
      chart.dispose();
    });
  }


  private void performance(Map<String, Runnable> actionMap) {
    actionMap.put("performance", () -> {
      String selectedTicker = displayChartHome.getSelectedTickerName();
      if (selectedTicker == null)
        selectedTicker = "AAPL";
      try {
        chart = new PerformanceChartGUIView(calculateChart(selectedTicker));
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

      chart.addActionListener(this);
      chart.setLocation(home.getLocation());
      home.dispose();
    });
  }

  private Map<LocalDate, String> calculateChart(String selectedTicker) throws SQLException {
    Map<LocalDate, String> chart = new LinkedHashMap<>();
    List<Double> values = new ArrayList<>();
    LocalDate startDate = LocalDate.parse("2022-11-28");
    LocalDate endDate = LocalDate.parse("2022-12-02");
    endDate = endDate.plusDays(1);
    for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
      values.add(this.getValue(date, selectedTicker));
    }

    Double maxValue = Collections.max(values);
    this.scale = maxValue / 20;
    LocalDate date = LocalDate.parse("2022-11-28");
    for (int i = 0; i < values.size(); i++) {
      int numStars = (int) (values.get(i) / scale);
      String stars = "";
      if (numStars == 0) {
        stars = "<*";
      } else {
        for (int j = 0; j < numStars; j++) {
          stars = stars + "*";
        }
      }
      chart.put(date, stars);
      date = date.plusDays(1);

    }
    return chart;
  }

  private Double getValue(LocalDate date, String selectedTicker) throws SQLException {
    String query = "SELECT getValue('" + date + "', '" + selectedTicker + "')";
    PreparedStatement ps = conn.prepareStatement(query);
    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
      return rs.getDouble(1);
    }
    return 0.0;
  }

  private void closeFromChart(Map<String, Runnable> actionMap) {
    actionMap.put("closeFromChart", () -> {
      displayChartHome = new DisplayChartGUIView();

      //hide chart window and display display chart options
      displayChartHome.addActionListener(this);
      displayChartHome.setLocation(chart.getLocation());
      this.chart.dispose();
    });
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    actionMap.get(e.getActionCommand()).run();
  }
}
