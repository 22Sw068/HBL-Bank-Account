import java.io.*;
import java.util.Scanner;

public class Bank_Account {
    private String name;
    private String accountNumber;
    private String password;
    private double balance;

    public Bank_Account(String name, String accountNumber, String password, double balance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public static void Main_Menu() {
        System.out.println("|-----------------------------------------------|");
        System.out.println("|-------------- BANK MENU ----------------------|");
        System.out.println("|--------------1: Register Account--------------|");
        System.out.println("|--------------2: Login to Account--------------|");
        System.out.println("|--------------3: Display Saved Accounts--------|");
        System.out.println("|-----------------------------------------------|");
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Bank_Account account = null;

        while (true) {
            System.out.println("---------------Welcome to HBL Bank Account---------------");
            Main_Menu();
            System.out.println("Enter your Choice 1, 2, or 3: ");
            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("---------------Welcome to the HBL Bank Account---------------");
                    account = registration(input);
                    saveAccountToFile(account);
                    System.out.println("Do you want to continue (y/n)?");
                    String cont = input.nextLine();
                    if (cont.equalsIgnoreCase("n")) {
                        System.out.println("Thank you for using HBL Bank.");
                        return;
                    }
                    break;

                case 2:
                    System.out.println("Login to your Account ");
                    account = login(input);
                    if (account != null) {
                        System.out.println("Login successful.");
                        while (true) {
                            System.out.println("|----------------------------|");
                            System.out.println("|   1: Account Details       |");
                            System.out.println("|   2: Transfer Balance      |");
                            System.out.println("|   3: Withdraw Balance      |");
                            System.out.println("|   4: Logout                |");
                            System.out.println("|----------------------------|");
                            System.out.println("Enter your choice...: ");
                            int loginChoice = input.nextInt();
                            input.nextLine();

                            switch (loginChoice) {
                                case 1:
                                    checkBalance(account);
                                    break;
                                case 2:
                                    transferMoney(input, account);
                                    break;
                                case 3:
                                    withdrawBalance(input, account);
                                    break;
                                case 4:
                                    System.out.println("Logging out...");
                                    account = null;
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please select 1, 2, 3, or 4.");
                                    break;
                            }

                            if (loginChoice == 4) break;
                        }
                    }
                    break;

                case 3:
                    displaySavedAccounts();
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
                    break;
            }
        }
    }

    private static Bank_Account registration(Scanner input) {
        System.out.println("Enter Name: ");
        String name = input.nextLine();
        System.out.println("Enter Account Number: ");
        String accNum = input.nextLine();
        System.out.println("Enter Password: ");
        String pass = input.nextLine();
        System.out.println("Enter Initial Balance: ");
        double bal = input.nextDouble();
        input.nextLine(); // consume the newline character

        Bank_Account account = new Bank_Account(name, accNum, pass, bal);
        System.out.println("Account successfully created.");
        return account;
    }

    private static void saveAccountToFile(Bank_Account account) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(account.getAccountNumber() + ".txt"))) {
            writer.write(account.getName() + "," + account.getAccountNumber() + "," + account.getPassword() + "," + account.getBalance());
        } catch (IOException e) {
            System.out.println("An error occurred while saving the account.");
        }
    }

    private static Bank_Account login(Scanner input) {
        System.out.println("Enter Account Number: ");
        String accNum = input.nextLine();
        System.out.println("Enter Password: ");
        String pass = input.nextLine();

        Bank_Account account = readAccountFromFile(accNum);
        if (account != null && account.getPassword().equals(pass)) {
            return account;
        } else {
            System.out.println("Invalid account number or password.");
            return null;
        }
    }

    private static Bank_Account readAccountFromFile(String accNum) {
        try (BufferedReader reader = new BufferedReader(new FileReader(accNum + ".txt"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    return new Bank_Account(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
                } else {
                    System.out.println("Invalid account data format.");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the account.");
        }
        return null;
    }

    private static void checkBalance(Bank_Account account) {
        System.out.println("Account Holder: " + account.getName());
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Current Balance: " + account.getBalance());
    }

    private static void transferMoney(Scanner input, Bank_Account account) {
        System.out.println("Enter the Account Number to transfer to: ");
        String toAccNum = input.nextLine();
        Bank_Account toAccount = readAccountFromFile(toAccNum);

        if (toAccount != null) {
            System.out.println("Account found. Account holder: " + toAccount.getName() + ", Balance: " + toAccount.getBalance());
            System.out.println("Do you want to transfer money to this account? (y/n): ");
            String choice = input.nextLine();

            if (choice.equalsIgnoreCase("y")) {
                System.out.println("Enter amount to transfer: ");
                double amount = input.nextDouble();
                input.nextLine();

                if (account.getBalance() >= amount) {
                    account.setBalance(account.getBalance() - amount);
                    toAccount.setBalance(toAccount.getBalance() + amount);

                    saveAccountToFile(account);
                    saveAccountToFile(toAccount);

                    System.out.println("Transfer successful. Your new balance is: " + account.getBalance());
                } else {
                    System.out.println("Insufficient balance.");
                }
            } else {
                System.out.println("Transfer canceled.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void withdrawBalance(Scanner input, Bank_Account account) {
        System.out.println("Enter amount to withdraw: ");
        double amount = input.nextDouble();
        input.nextLine();

        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            saveAccountToFile(account);
            System.out.println("Withdrawal successful. Your new balance is: " + account.getBalance());
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    private static void displaySavedAccounts() {
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));

        if (files != null && files.length > 0) {
            System.out.println("Saved Accounts:");
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line = reader.readLine();
                    if (line != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 4) {
                            System.out.println("Name: " + parts[0] + ", Account Number: " + parts[1]);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("An error occurred while reading the account file: " + file.getName());
                }
            }
        } else {
            System.out.println("No saved accounts found.");
        }
    }
}