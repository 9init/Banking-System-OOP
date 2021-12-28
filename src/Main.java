import exceptions.NoEnoughCurrency;
import exceptions.UserAlreadyExistException;
import exceptions.UserNotFoundException;
import exceptions.WrongInfoException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        init();
        System.out.println("Welcome to El-Ghalaba Bank");
        while(true) {
            System.out.println("\nChoose an action");
            System.out.println("1) Login");
            System.out.println("2) Register");
            System.out.print("Your action: ");
            switch (scanner.nextInt()){
                case 1 -> loginHandler();
                case 2 -> registerHandler();
            }
        }

    }

    private static void loginHandler(){
        Customer user;
        while(true){
            try {
                String username, password;
                System.out.println();
                System.out.println("Login.");
                System.out.print("Enter username: ");
                username = scanner.next();
                System.out.print("Enter password: ");
                password = scanner.next();
                user = Server.login(username, password);
                System.out.println("\nWelcome " + user.getName());
                homePageHandler(user);
                break;
            }catch (WrongInfoException e) {
                System.out.print("No user found!. Try again? (y/yes, n/no): ");
                String input = scanner.next().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    return;
            }
        }

    }

    private static void registerHandler(){
        while(true){
            try {
                String name, username, password;
                System.out.print("Enter name: ");
                name = scanner.next();
                System.out.print("Enter username: ");
                username = scanner.next();
                System.out.print("Enter password: ");
                password = scanner.next();
                Server.register(name, username, password);
                break;
            }catch (UserAlreadyExistException e){
                System.out.println("\nUsername already exist. Try again");
            }
        }

        System.out.println("Registration done");
    }

    private static void homePageHandler(Customer user){
        loop: while (true){
            try {
                System.out.println("1) Deposit money");
                System.out.println("2) Withdraw money");
                System.out.println("3) Transfer to another account");
                System.out.println("4) See my account's Balance");
                System.out.println("5) See my transactions");
                System.out.println("6) Logout");
                System.out.println();
                System.out.print("Enter Option [1-6]: ");
                int input = scanner.nextInt();
                if(!(input >=1 && input <= 6))
                    throw new Exception();

                switch (input){
                    case 1 -> depositHandler(user);
                    case 2 -> withDrawHandler(user);
                    case 3 -> transferHandler(user);
                    case 4 -> accountInfoHandler(user);
                    case 5 -> viewTransactionHandler(user);
                    case 6 -> { break loop; }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
//                System.out.print("Invalid input. Try again? (y/yes, n/no): ");
//                String input = scanner.next().toLowerCase();
//                if(!(input.equals("y") || input.equals("yes")))
//                    return;
            }
        }

    }

    private static void depositHandler(Customer user){
        while (true){
            try {
                System.out.print("How much would you like to deposit? : ");
                int amount = Math.abs(scanner.nextInt());
                Transaction.doTransaction(user, amount, Transaction.DEPOSIT);
                System.out.println("You have deposited " + amount);
                System.out.println("Your new Balance is " +  user.getCurrency());
                System.out.println();
                System.out.println();
                System.out.println("Thank you, " + user.getName() + "! ");
                break;
            } catch (NoEnoughCurrency e) {
                System.out.print("No enough balance in your account. Try again? (y/yes, n/no): ");
                String input = scanner.next().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    return;
            }
        }

    }

    private static void withDrawHandler(Customer user){
        while (true){
            try {
                System.out.print("How much would you like to withdraw? : ");
                int amount = Math.abs(scanner.nextInt());
                Transaction.doTransaction(user, amount, Transaction.WITH_DRAW);
                System.out.println("You have withdrew " + amount);
                System.out.println("Your new Balance is " +  user.getCurrency());
                System.out.println();
                System.out.println();
                System.out.println("Thank you, " + user.getName() + "!");
                break;
            } catch (NoEnoughCurrency e) {
                System.out.print("The selected amount exceeds your balance. Try again? (y/yes, n/no): ");
                String input = scanner.next().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    return;
            }
        }

    }

    private static void transferHandler(Customer user){
        while (true){
            try {
                System.out.print("Enter the account username you would like to transfer money to : ");
                String accountUsername = scanner.next();
                System.out.print("Please enter the amount to transfer to " + accountUsername + " : ");
                int amount = Math.abs(scanner.nextInt());
                Transaction.doTransaction(user, accountUsername, amount);
                System.out.println("You have transferred " + amount + " to " + accountUsername);
                System.out.println("Your new Balance is " +  user.getCurrency());
                System.out.println();
                System.out.println();
                System.out.println("Thank you, " + user.getName() + "! ");
                break;
            } catch (UserNotFoundException e) {
                System.out.print("The selected username not found. Try again? (y/yes, n/no): ");
                String input = scanner.next().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    return;
            } catch (NoEnoughCurrency e) {
                System.out.print("The selected amount exceeds your balance. Try again? (y/yes, n/no): ");
                String input = scanner.next().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    return;
            } catch (InputMismatchException e){
                System.out.print("Invalid input. Try again? (y/yes, n/no): ");
                String input = scanner.next().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    return;
            }
        }
    }

    private static void viewTransactionHandler(Customer user){
        Server.printAllMyTransaction(user);
    }

    private static void accountInfoHandler(Customer user){
        System.out.println("Your Account Information: ");
        System.out.println("ID : " + user.getID());
        System.out.println("Name : " + user.getName());
        System.out.println("Username : " + user.getUsername());
        System.out.println("Your current balance  is : " + user.getCurrency());
        System.out.println();
        System.out.println();
        System.out.println("Thank you, " + user.getName() + "! ");
    }

    private static void init(){
        System.out.println("Initializing the app\n");
        System.out.println("Creating user");
        Customer.createUser("Ahmed Hesham", "aHesham", "123456",  1_000_000 );
        Customer.createUser("Aya Bahaa Eldin", "ayaBahaa", "123456", 1_000_000 );
        Customer.createUser("Alaa Mohamed", "alaaMohamed", "123456",  1_000_000);
        Customer.createUser("Arwa Mohamed", "arwaMohamed", "123456", 1_000_000 );
        Customer.createUser("Sara Mohamed", "saraMohamed", "123456", 1_000_000 );
        System.out.println("Creation done\n");
    }
}
