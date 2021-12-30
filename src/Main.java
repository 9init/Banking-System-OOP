import exceptions.NoEnoughCurrency;
import exceptions.UserAlreadyExistException;
import exceptions.UserNotFoundException;
import exceptions.WrongInfoException;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static String BoldOn = "\033[1m";
    static String BoldOff = "\033[0m";

    public static void main(String[] args) {
        init();
        while(true) {
            System.out.println();
            System.out.println(BoldOn + "Welcome to El-Ghalaba Bank" + BoldOff);
            System.out.println("1) Login");
            System.out.println("2) Register");
            System.out.println();
            System.out.print("Select an action [1-2]: ");
            switch (scanner.nextLine()){
                case "1" -> loginHandler();
                case "2" -> registerHandler();
                default -> System.out.println("Invalid input. Try again");
            }
        }

    }

    private static void loginHandler(){
        Customer user;
        while(true){
            try {
                String username, password;
                System.out.println();
                System.out.println(BoldOn + "Login." + BoldOff);
                System.out.print("Enter username: ");
                username = scanner.nextLine();
                System.out.print("Enter password: ");
                password = scanner.nextLine();
                user = Server.login(username, password);
                homePageHandler(user);
                break;
            }catch (WrongInfoException e) {
                System.out.print("No user found!. Try again? (y/yes, n/no): ");
                String input = scanner.nextLine().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    return;
            }
            catch (Exception e){
                System.out.print("Invalid input. Try again? (y/yes, n/no): ");
                String input = scanner.nextLine().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    return;
            }
        }

    }

    private static void registerHandler(){
        while(true){
            try {
                String name, username, password;
                System.out.println();
                System.out.println(BoldOn + "Registering." + BoldOff);
                System.out.print("Enter name: ");
                name = scanner.nextLine();
                System.out.print("Enter username: ");
                username = scanner.nextLine();
                System.out.print("Enter password: ");
                password = scanner.nextLine();
                Server.register(name, username, password);
                System.out.println("Registration done");
                break;
            } catch (UserAlreadyExistException e){
                System.out.println("Username already exist. Try again (y/yes, n/no): ");
                String input = scanner.nextLine().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    break;
            }
        }

    }

    private static void homePageHandler(Customer user){
        loop: while (true){
            try {
                System.out.println();
                System.out.println(BoldOn + "Welcome " + user.getName() + BoldOff);
                System.out.println("1) Deposit money");
                System.out.println("2) Withdraw money");
                System.out.println("3) Transfer to another account");
                System.out.println("4) See my account's Balance");
                System.out.println("5) See my transactions");
                System.out.println("6) Change account's data");
                System.out.println("7) Logout");
                System.out.println();
                System.out.print("Enter Option [1-7]: ");
                int input = scanner.nextInt();
                scanner.nextLine();
                switch (input){
                    case 1 -> depositHandler(user);
                    case 2 -> withDrawHandler(user);
                    case 3 -> transferHandler(user);
                    case 4 -> accountInfoHandler(user);
                    case 5 -> viewTransactionHandler(user);
                    case 6 -> {
                        checkCurrentPassword(user);
                        changeDataHAndler(user);
                    }
                    case 7 -> { break loop; }
                    default -> throw new Exception();
                }
            }catch (WrongInfoException e){
                System.out.println("Wrong password. Try again");
            } catch (Exception e) {
                System.out.println("Invalid input. Try again");
            }
        }

    }

    private static void depositHandler(Customer user){
        while (true){
            try {
                System.out.println();
                System.out.println(BoldOn + "Depositing." + BoldOff);
                System.out.print("How much would you like to deposit? : ");
                int amount = Math.abs(scanner.nextInt());
                scanner.nextLine();
                Transaction.doTransaction(user, amount, Transaction.DEPOSIT);
                System.out.println("You have deposited " + amount);
                System.out.println("Your new Balance is " +  user.getCurrency());
                System.out.println();
                System.out.println();
                System.out.println("Thank you, " + user.getName() + "! ");
                break;
            } catch (NoEnoughCurrency e) {
                System.out.print("No enough balance in your account. Try again? (y/yes, n/no): ");
                String input = scanner.nextLine().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    break;
            }
            catch (Exception e){
                System.out.print("Invalid input. Try again? (y/yes, n/no): ");
                String input = scanner.nextLine().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    break;
            }
        }

    }

    private static void withDrawHandler(Customer user){
        while (true){
            try {
                System.out.println();
                System.out.println(BoldOn + "With drawing." + BoldOff);
                System.out.print("How much would you like to withdraw? : ");
                int amount = Math.abs(scanner.nextInt());
                scanner.nextLine();
                Transaction.doTransaction(user, amount, Transaction.WITH_DRAW);
                System.out.println("You have withdrew " + amount);
                System.out.println("Your new Balance is " +  user.getCurrency());
                System.out.println();
                System.out.println();
                System.out.println("Thank you, " + user.getName() + "!");
                break;
            } catch (NoEnoughCurrency e) {
                System.out.print("The selected amount exceeds your balance. Try again? (y/yes, n/no): ");
                String input = scanner.nextLine().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    break;
            } catch (Exception e){
                System.out.print("Invalid input. Try again? (y/yes, n/no): ");
                String input = scanner.nextLine().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    break;
            }
        }

    }

    private static void transferHandler(Customer user){
        while (true){
            try {
                System.out.println();
                System.out.println(BoldOn + "Transferring." + BoldOff);
                System.out.print("Enter the account username you would like to transfer money to : ");
                String accountUsername = scanner.nextLine();
                System.out.print("Please enter the amount to transfer to " + accountUsername + " : ");
                int amount = Math.abs(scanner.nextInt());
                scanner.nextLine();
                Transaction.doTransaction(user, accountUsername, amount);
                System.out.println("You have transferred " + amount + " to " + accountUsername);
                System.out.println("Your new Balance is " +  user.getCurrency());
                System.out.println();
                System.out.println();
                System.out.println("Thank you, " + user.getName() + "! ");
                break;
            } catch (UserNotFoundException e) {
                System.out.print("The selected username not found. Try again? (y/yes, n/no): ");
                String input = scanner.nextLine().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    break;
            } catch (NoEnoughCurrency e) {
                System.out.print("The selected amount exceeds your balance. Try again? (y/yes, n/no): ");
                String input = scanner.nextLine().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    break;
            } catch (Exception e){
                System.out.print("Invalid input. Try again? (y/yes, n/no): ");
                String input = scanner.nextLine().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    break;
            }

        }
    }

    private static void viewTransactionHandler(Customer user){
        System.out.println();
        System.out.println(BoldOn + "Viewing transactions." + BoldOff);
        Server.printAllMyTransaction(user);
    }

    private static void accountInfoHandler(Customer user){
        System.out.println();
        System.out.println(BoldOn + "Your Account Information." + BoldOff);
        System.out.println("ID                       : " + user.getID());
        System.out.println("Name                     : " + user.getName());
        System.out.println("Username                 : " + user.getUsername());
        System.out.println("Your current balance  is : " + user.getCurrency());
        System.out.println();
        System.out.println();
        System.out.println("Thank you, " + user.getName() + "! ");
    }

    private static void changeDataHAndler(Customer user){
        System.out.println();
        System.out.println(BoldOn + "Changing data." + BoldOff);
        System.out.println("1) Change name");
        System.out.println("1) Change username");
        System.out.println("3) Change password");
        System.out.println("4) Back");
        System.out.println();
        System.out.print("Select an action [1-4]: ");
        switch (scanner.nextLine()){
            case "1" -> changeNameHandler(user);
            case "2" -> changeUsernameHandler(user);
            case "3" -> changePasswordHandler(user);
            case "4" -> {}
            default -> System.out.println("Invalid input. Try again");
        }
    }

    private static void checkCurrentPassword(Customer user) throws WrongInfoException{
        System.out.print("Enter your password: ");
        String currentPassword = scanner.nextLine();
        if(!user.getPassword().equals(currentPassword))
            throw new WrongInfoException();
    }

    private static void changeNameHandler(Customer user){
        while (true){
            try {
                String newName1, newName2;
                System.out.println();
                System.out.println(BoldOn + "Changing name." + BoldOff);
                System.out.println("Your current name is: " + user.getName());
                System.out.print("Enter your new name: ");
                newName1 = scanner.nextLine();
                System.out.print("Enter your new name again: ");
                newName2 = scanner.nextLine();
                if (!newName1.equals(newName2)) throw new WrongInfoException();
                user.setName(newName1);
                System.out.println("Data changed successfully. Thank you " + user.getName() + "! ");
                break;
            } catch (WrongInfoException e) {
                System.out.println("Entered name not equal repeated name. Try again? (y/yes, n/no): ");
                String input = scanner.nextLine().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    break;
            }
        }
    }

    private static void changeUsernameHandler(Customer user){
        while (true){
            try {
                String newName1, newName2;
                System.out.println();
                System.out.println(BoldOn + "Changing username." + BoldOff);
                System.out.println("Your current username is: " + user.getUsername());
                System.out.print("Enter your new username: ");
                newName1 = scanner.nextLine();
                System.out.print("Enter your new username again: ");
                newName2 = scanner.nextLine();
                if (!newName1.equals(newName2)) throw new WrongInfoException();
                user.setUsername(newName1);
                System.out.println("Data changed successfully. Thank you " + user.getName() + "! ");
                break;
            } catch (WrongInfoException e) {
                System.out.println("Entered username not equal repeated name. Try again? (y/yes, n/no): ");
                String input = scanner.nextLine().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    break;
            } catch (UserAlreadyExistException e) {
                System.out.println("Username already exist. Try again? (y/yes, n/no): ");
                String input = scanner.nextLine().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    break;
            }
        }
    }

    private static void changePasswordHandler(Customer user){
        while (true){
            try {
                String newName1, newName2;
                System.out.println();
                System.out.println(BoldOn + "Changing password." + BoldOff);
                System.out.print("Enter your new password: ");
                newName1 = scanner.nextLine();
                System.out.print("Enter your new password again: ");
                newName2 = scanner.nextLine();
                if (!newName1.equals(newName2)) throw new WrongInfoException();
                user.setPassword(newName1);
                System.out.println("Data changed successfully. Thank you " + user.getName() + "! ");
                break;
            } catch (WrongInfoException e) {
                System.out.println("Entered password not equal repeated name. Try again? (y/yes, n/no): ");
                String input = scanner.nextLine().toLowerCase();
                if(!(input.equals("y") || input.equals("yes")))
                    break;
            }
        }
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