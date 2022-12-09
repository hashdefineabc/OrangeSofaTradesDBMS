import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.GUIController;
import view.HomeView;

public class OrangeSofaTradeDriver {

  // name of the MySQL account
  private String mysqlUserName = "";
  // password of the MySQL account
  private String mysqlPassword = "";
  private final String dbName = "orange_sofa_trade";
  Connection conn = null;
  PreparedStatement ps;
  public int loggedInUserId;
  public int loggedInManagerId;

  Scanner scanner = new Scanner(System.in);


  private void initUserInfo() {
    System.out.print("What is your Mysql Username:");
    mysqlUserName = (new Scanner(System.in)).nextLine();

    System.out.print("What is your Mysql Password:");
    mysqlPassword = (new Scanner(System.in)).nextLine();
  }


  private Connection getConnection() throws SQLException {
    // enter the mysql username and password
    initUserInfo();
    return DriverManager.getConnection(
        "jdbc:mysql://localhost/orange_sofa_trade?" + "user=" + this.mysqlUserName + "&password="
            + mysqlPassword);
  }

  void mainMenu() {
    System.out.println("Pick an option");
    System.out.println("1. Login");
    System.out.println("2. Create Account");
    System.out.println("3. Exit from application");

    String userOption = scanner.next();
    try {
      int option = Integer.parseInt(userOption);
      if (option == 1) {
        login();
      } else if (option == 2) {
        createAccount();
      } else if (option == 3) {
        System.out.println("Quitting application");
        System.exit(0);
      } else {
        System.out.println("Please enter a valid option");
      }
    } catch (NumberFormatException e) {
      System.out.println("Please enter a valid option");
    }
  }

  public void run() {
    //First connect to the database
    // ask user to enter their mysql username and password, then connect to the database,
    try {
      conn = this.getConnection();
      System.out.println("Connected to database: " + this.dbName);
    } catch (SQLException SQLe) {
      System.out.println("ERROR: Could not connect to database. " + SQLe.getMessage());
      SQLe.printStackTrace();
      return;
    }

    mainMenu();
  }

  private void login() {
    String userName = "";
    String password = "";

    System.out.println("1. User Login");
    System.out.println("2. Manager Login");
    System.out.println("3. Main menu");
    System.out.println("4. Quit Application");
    String option = scanner.next();
    try {
      int optionInt = Integer.parseInt(option);
      if (optionInt == 1 || optionInt == 2) {
        System.out.println("Enter Username");
        userName = scanner.next();
        System.out.println("Enter Password");
        password = scanner.next();
      }
      switch (optionInt) {
        case 1:
          if (validateUserLogin(userName, password)) {
            System.out.println("User Login Successful");
            HomeView vGUI = new HomeView("Home");
            GUIController controller = new GUIController(loggedInUserId, loggedInManagerId, vGUI,
                    conn);
          } else {
            System.out.println("Invalid credentials");
            login();
          }
          break;
        case 2:
          if (validateManagerLogin(userName, password)) {
            managerLoggedIn();
          } else {
            System.out.println("Invalid credentials");
            login();
          }
          break;
        case 3:
          mainMenu();
          break;
        case 4:
          System.out.println("Quitting application");
          System.exit(0);
      }
    } catch (Exception e) {
      System.out.println("Enter a valid option");
      login();
    }
  }

  private void managerLoggedIn() throws SQLException {
    System.out.println("Logged In as manager");
    System.out.println("Please pick an option");
    System.out.println("1. Insert stock data");
    System.out.println("2. Update stock data");
    System.out.println("3. Add new broker");
    System.out.println("4. View broker information");
    System.out.println("5. Delete broker");
    System.out.println("6. Logout");
    System.out.println("7. Quit");
    switch (scanner.nextInt()) {
      case 1:
        insertStockData();
        managerLoggedIn();
        break;
      case 2:
        updateStockData();
        managerLoggedIn();
      case 3:
        addNewBroker();
        break;
      case 4:
        brokerDetails();
        break;
      case 5:
        deleteBroker();
        break;
      case 6:
        System.out.println("Manager logout Successful");
        mainMenu();
        break;
      case 7:
        System.exit(0);
    }
  }

  private void insertStockData() throws SQLException {
    System.out.print("Pick a Ticker Name - ");
    List<String> tickerNamesFromDB = new ArrayList<>();

//    CALL getAvailableTickerNames();
    String getTickerNamequery = "CALL getAvailableTickerNames()";
    PreparedStatement ps = conn.prepareStatement(getTickerNamequery);
    ps.executeQuery();
    ResultSet rs = ps.getResultSet();
    while (rs.next()) {
      tickerNamesFromDB.add(rs.getString(1));
    }
    System.out.println(tickerNamesFromDB);

    String tickerName = scanner.next();

    try {
      String validateTickerNameQuery = "CALL validateTickerName('" + tickerName + "')";
      PreparedStatement p = conn.prepareStatement(validateTickerNameQuery);
      p.executeQuery();
      ResultSet r = p.getResultSet();
      if (!r.next()) {
        System.out.println("Enter a valid ticker name.");
        insertStockData();
        return;
      }
    } catch (SQLException e) {
      System.out.println("Enter a valid ticker name");
      insertStockData();
      return;
    }


    Date date;
    while (true) {
      try {
        System.out.println("Enter date (yyyy-mm-dd)");
        String d = scanner.next();
        LocalDate localDate = LocalDate.parse(d);
        date = Date.valueOf(localDate);
        break;
      } catch (Exception e) {
        System.out.println("Invalid date entered");
      }
    }

    System.out.println("Enter open price on this date");
    Double openPrice = scanner.nextDouble();
    System.out.println("Enter close price on this date");
    Double closePrice = scanner.nextDouble();
    System.out.println("Enter low price on this date");
    Double lowPrice = scanner.nextDouble();
    System.out.println("Enter high price on this date");
    Double highPrice = scanner.nextDouble();
    String query = "CALL insertStockData('" + tickerName +
            "'," + this.loggedInManagerId +
            "," + openPrice +
            "," + closePrice +
            "," + lowPrice +
            "," + highPrice +
            ",'" + date + "')";
    try {
      ps = conn.prepareStatement(query);
      ps.executeUpdate();
      System.out.println("Stock data inserted successfully!!!");
      ps.close();
    } catch (SQLException e) {
      System.out.println("Error inserting stock data, please try again...");
      System.out.println(e);
      insertStockData();
    }

  }

  private void updateStockData() throws SQLException {
    System.out.print("Pick a Ticker Name - ");
    List<String> tickerNamesFromDB = new ArrayList<>();

//    CALL getAvailableTickerNames();
    String getTickerNamequery = "CALL getAvailableTickerNames()";
    PreparedStatement ps = conn.prepareStatement(getTickerNamequery);
    ps.executeQuery();
    ResultSet rs = ps.getResultSet();
    while (rs.next()) {
      tickerNamesFromDB.add(rs.getString(1));
    }
    System.out.println(tickerNamesFromDB);

    String tickerName = scanner.next();

    try {
      String validateTickerNameQuery = "CALL validateTickerName('" + tickerName + "')";
      PreparedStatement p = conn.prepareStatement(validateTickerNameQuery);
      p.executeQuery();
      ResultSet r = p.getResultSet();
      if (!r.next()) {
        System.out.println("Enter a valid ticker name.");
        insertStockData();
        return;
      }
    } catch (SQLException e) {
      System.out.println("Enter a valid ticker name");
      insertStockData();
      return;
    }


    Date date;
    while (true) {
      try {
        System.out.println("Enter date (yyyy-mm-dd)");
        String d = scanner.next();
        LocalDate localDate = LocalDate.parse(d);
        date = Date.valueOf(localDate);
        break;
      } catch (Exception e) {
        System.out.println("Invalid date entered");
      }
    }


    System.out.println("Enter open price on this date");
    Double openPrice = scanner.nextDouble();
    System.out.println("Enter close price on this date");
    Double closePrice = scanner.nextDouble();
    System.out.println("Enter low price on this date");
    Double lowPrice = scanner.nextDouble();
    System.out.println("Enter high price on this date");
    Double highPrice = scanner.nextDouble();
    String query = "CALL updateStockData('" + tickerName +
            "'," + openPrice +
            "," + closePrice +
            "," + lowPrice +
            "," + highPrice +
            ",'" + date + "')";
    try {
      ps = conn.prepareStatement(query);
      ps.executeUpdate();
      System.out.println("Stock data updated successfully!!!");
      ps.close();
    } catch (SQLException e) {
      System.out.println("Error updating stock data, please try again...");
      System.out.println(e);
      insertStockData();
    }

  }

  void createNewUser() {
    System.out.println("Enter Username");
    String userName = scanner.next();
    System.out.println("Enter Password");
    String password = scanner.next();
    if (addNewUser(userName, password)) {
      System.out.println("User created successfully!!!!");
      mainMenu();
    } else {
      System.out.println("UserName already exists, please try a different username");
      createNewUser();
    }
  }

  void createNewManager() {
    System.out.println("Enter Manager Username");
    String userName = scanner.next();
    System.out.println("Enter Password");
    String password = scanner.next();
    if (addNewManager(userName, password)) {
      System.out.println("Manager created successfully!!!!");
      mainMenu();
    } else {
      System.out.println("Manager UserName already exists, please try a different name");
      createNewManager();
    }
  }

  private void createAccount() {

    System.out.println("1. Create New User");
    System.out.println("2. Create New Manager");
    System.out.println("3. Main menu");
    System.out.println("4. Quit Application");
    String option = scanner.next();
    try {
      int optionInt = Integer.parseInt(option);
      if (optionInt == 1) {
        createNewUser();
      } else if (optionInt == 2) {
        createNewManager();
      } else if (optionInt == 3) {
        mainMenu();
      } else if (optionInt == 4) {
        System.out.println("Quiting Application");
        System.exit(0);
      } else {
        System.out.println("Enter a valid option");
        login();
      }
    } catch (Exception e) {
      System.out.println("Enter a valid option");
      login();
    }
  }

  private boolean addNewUser(String userName, String password) {
    String query = "CALL addNewUser('" + userName + "','" + password + "')";
    try {
      ps = conn.prepareStatement(query);
      ps.executeQuery();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  private boolean addNewManager(String userName, String password) {

    String query = "CALL addNewManager('" + userName + "','" + password + "')";
    try {
      ps = conn.prepareStatement(query);
      ps.executeQuery();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  private void addNewBroker() {
    System.out.println("Enter broker name");
    String brokerName = scanner.next();
    System.out.println("Enter commission rate for this broker (in decimal /Ex: 0.02 for 2%)");
    Double commRate = scanner.nextDouble();
    String query = "CALL insertNewBroker('" + brokerName + "'," + commRate + "," + loggedInManagerId + ")";
    try {
      ps = conn.prepareStatement(query);
      ps.executeUpdate();
      ps.close();
      System.out.println("Successfully added the broker");
      managerLoggedIn();
    } catch (SQLException e) {
      System.out.println("Error creating broker, try again...");
      addNewBroker();
    }
  }

  private void brokerDetails() throws SQLException {
    System.out.println("BROKER'S DETAILS");
    String query = "SELECT * FROM broker";
    List<List<String>> brokers = new ArrayList<>();
    try {
      ps = conn.prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      List<String> brokerLabel = new ArrayList<>();
      brokerLabel.add("Broker Id");
      brokerLabel.add("Broker Name");
      brokerLabel.add("Commission Rate");
      int stride1 = brokerLabel.size() / 3;
      System.out.println(String.format("%20s %20s %12s", brokerLabel.get(0),
              brokerLabel.get(0 + stride1), brokerLabel.get(0 + stride1 * 2)));
      while (rs.next()) {
        List<String> eachBroker = new ArrayList<>();
        eachBroker.add(rs.getString(1));
        eachBroker.add(rs.getString(2));
        eachBroker.add(rs.getString(3));
        eachBroker.add(rs.getString(4));
        brokers.add(eachBroker);
        int stride = eachBroker.size() / 3;
        for (int row = 0; row < eachBroker.size() / 3; row++) {
          System.out.println(String.format("%20s %20s %12s", eachBroker.get(row),
                  eachBroker.get(row + stride), eachBroker.get(row + stride * 2)));
        }
      }

      ps.close();
      managerLoggedIn();
    } catch (SQLException e) {
      System.out.println("Error viewing broker's information, try again...");
      managerLoggedIn();
    }
  }

  private void deleteBroker() {
    System.out.println("Enter broker id");
    String inp = scanner.next();
    int brokerId;
    try {
      brokerId = Integer.parseInt(inp);
    } catch (NumberFormatException e) {
      System.out.println("Please enter a valid integer");
      deleteBroker();
      return;
    }
    // CALL deleteBroker(3);
    String query = "CALL deleteBroker('" + brokerId + "')";
    try {
      ps = conn.prepareStatement(query);
      int deleted = ps.executeUpdate();
      ps.close();
      if (deleted == 0) {
        System.out.println("brokerId doesn't exists");
      } else
        System.out.println("Successfully deleted");
      managerLoggedIn();
    } catch (SQLException e) {
      System.out.println("Error deleting, try again...");
      addNewBroker();
    }
  }

  private boolean validateUserLogin(String userName, String password) throws SQLException {
    String query = "CALL validateUserCredentials('" + userName + "','" + password + "')";
    PreparedStatement p1 = conn.prepareStatement(query);
    ResultSet rs = p1.executeQuery();
    if (rs.next()) {
      loggedInUserId = rs.getInt(1);
      return true;
    } else {
      return false;
    }
  }

  private boolean validateManagerLogin(String userName, String password) throws SQLException {
    String query = "CALL validateManagerCredentials('" + userName + "','" + password + "')";
    PreparedStatement p1 = conn.prepareStatement(query);
    ResultSet rs = p1.executeQuery();
    if (rs.next()) {
      loggedInManagerId = rs.getInt(1);
      return true;
    } else {
      return false;
    }
  }

  public static void main(String[] args) {
    OrangeSofaTradeDriver orangeSofaTradeDriver = new OrangeSofaTradeDriver();
    orangeSofaTradeDriver.run();
  }
}
