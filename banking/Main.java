package banking;
import org.sqlite.SQLiteDataSource;
import java.sql.*;
import java.util.Scanner;


public class Main {
    private static SQLiteDataSource table;
    static final  Scanner scanner = new Scanner(System.in);
     static long card;
     static int pin;
     static long enter;

    public static void main(String[] args) {
        String url = "jdbc:sqlite:" + args[1];
        table = new SQLiteDataSource();
        table.setUrl(url);

        try(Connection con = table.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (id INTEGER PRIMARY KEY, number TEXT NOT NULL, pin TEXT NOT NULL, balance INTEGER DEFAULT 0)");

            } catch (Exception e) {e.printStackTrace();
            }
        } catch (Exception e) {e.printStackTrace();
        }
        Menu();
    }

    public static void Menu() {
        while (true) {
            System.out.println("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");
            int number = scanner.nextInt();
            switch (number) {
                case 1:
                    Code code1 = new Code();
                    code1.PrintCard();
                    card = code1.numberCard;
                    Pin pin1 = new Pin();
                    pin1.PrintPin();
                    pin = pin1.randomPin;
                    try(Connection con = table.getConnection()) {
                        try (Statement statement = con.createStatement()) {

                            statement.executeUpdate("INSERT INTO card (number, pin) VALUES (" + "'" + card + "', '" + pin + "')");

                        } catch (Exception e) {e.printStackTrace();
                        }
                    } catch (Exception e) {e.printStackTrace();
                    }
                    break;
                case 2:
                    System.out.println("Enter your card number:");
                    enter = scanner.nextLong();
                    System.out.println("Enter your PIN:");
                    int enterPin = scanner.nextInt();
                    boolean isUserExists = false;
                    try(Connection con = table.getConnection()) {
                    try (PreparedStatement ps = con.prepareStatement("select 1 from `card` where `number` = ? and `pin` = ?")) {
                        ps.setString(1, String.valueOf(enter));
                        ps.setString(2, String.valueOf(enterPin));
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                isUserExists = true;
                            }
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    } catch (Exception e) {e.printStackTrace();
                    }

                    if (isUserExists) {
                        System.out.println("You have successfully logged in!");
                        logInto();
                        break;
                    } else {
                        System.out.println("Wrong card number or PIN!");
                    }
                    break;
                case 0:
                    exitAccount();
                    break;
                default:
                    System.out.println("Error input");
                    return;
            }
        }

    }

    public static void logInto() {
        while (true) {
            System.out.println("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit");
            int number = scanner.nextInt();
            switch (number) {
                case 1:
                    System.out.println("Balance: " + 10000);
                    break;
                case 2:
                    System.out.println("Enter income:");
                    int income = scanner.nextInt();
                    try(Connection con = table.getConnection()) {
                        try (Statement statement = con.createStatement()) {

                            statement.executeUpdate("UPDATE card SET balance = balance + " + income + " WHERE " + "number = '" + enter + "'");

                        } catch (Exception e) {e.printStackTrace();
                        }
                    } catch (Exception e) {e.printStackTrace();
                    }
                    System.out.println("Income was added!");
                    break;
                case 3:
                    System.out.println("Transfer\n" +
                            "Enter card number:");
                    long cardNumberTrans = scanner.nextLong();
                    String temp = Long.toString(cardNumberTrans);
                    String prob = Luhn(cardNumberTrans);
                    if (temp.charAt(15) == prob.charAt(0)) {
                        boolean isUserExists = false;
                        try(Connection con = table.getConnection()) {
                            try (PreparedStatement ps = con.prepareStatement("select 1 from `card` where `number` = ?")) {
                                ps.setString(1, String.valueOf(cardNumberTrans));
                                try (ResultSet rs = ps.executeQuery()) {
                                    if (rs.next()) {
                                        isUserExists = true;
                                    }
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        } catch (Exception e) {e.printStackTrace();
                        }

                        if (isUserExists) {
                            System.out.println("Enter how much money you want to transfer:");
                            int transfer = scanner.nextInt();
                            boolean money = false;
                            try(Connection con = table.getConnection()) {
                                try (PreparedStatement ps = con.prepareStatement("select 1 from `card` where `balance` >= ?")) {
                                    ps.setString(1, String.valueOf(transfer));
                                    try (ResultSet rs = ps.executeQuery()) {
                                        if (rs.next()) {
                                            money = true;
                                        }
                                    }
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                            } catch (Exception e) {e.printStackTrace();
                            }
                            if (money) {
                                try(Connection con = table.getConnection()) {
                                    try (Statement statement = con.createStatement()) {

                                        statement.executeUpdate("UPDATE card SET balance = balance + " + transfer + " WHERE " + "number = '" + cardNumberTrans + "'");
                                        statement.executeUpdate("UPDATE card SET balance = balance - " + transfer + " WHERE " + "number = '" + enter + "'");

                                    } catch (Exception e) {e.printStackTrace();
                                    }
                                } catch (Exception e) {e.printStackTrace();
                                }
                                System.out.println("Success!");
                                break;
                            }
                            else {
                                System.out.println("Not enough money!");
                                break;
                            }
                        }
                        else {
                            System.out.println("Probably you made a mistake in the card number. Please try again!");
                            break;
                        }

                    }
                    else {
                       System.out.println("Such a card does not exist");
                    }
                    break;
                case 4:
                    try(Connection con = table.getConnection()) {
                        try (Statement statement = con.createStatement()) {
                            
                            statement.executeUpdate("DELETE FROM card WHERE " + "number = '" + enter + "'");

                        } catch (Exception e) {e.printStackTrace();
                        }
                    } catch (Exception e) {e.printStackTrace();
                    }
                    System.out.println("The account has been closed!");
                    Menu();
                    break;
                case 5:
                    System.out.println("You have successfully logged out!");
                    Menu();
                    break;
                case 0:
                    try(Connection con = table.getConnection()) {
                        try (Statement statement = con.createStatement()) {
                            statement.close();
                            con.close();

                        } catch (Exception e) {e.printStackTrace();
                        }
                    } catch (Exception e) {e.printStackTrace();
                    }
                    exitAccount();
                default:
                    System.out.println("Error input");
                    return;
            }
        }
    }


    public static void exitAccount() {
        System.out.println("Bye!");
        System.exit(0);
    }

    public static String Luhn(long cardNumberTrans) {
        long checksum;
        String str = "";
        String temp = Long.toString(cardNumberTrans);
        int[] newGuess = new int[temp.length()];
        int sum = 0;
        for (int i = 0; i < temp.length() - 1; i++) {
            newGuess[i] = temp.charAt(i) - '0';
            if (i % 2 == 0)  {
                newGuess[0] = 8;
                newGuess[i] = newGuess[i] * 2;
            } if (newGuess[i] > 9) {
                newGuess[i] = newGuess[i] - 9;
            }
            sum = sum + newGuess[i];
        }
        sum = sum + 1;
        if (sum % 10 == 0) {
            return "0";
        } else {
            checksum = 10 - (sum % 10);
           return str = Long.toString(checksum);
        }
    }
}
