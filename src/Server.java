import exceptions.UserAlreadyExistException;
import exceptions.WrongInfoException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class Server {
    public static Customer login(String username, String password) throws WrongInfoException {
        Customer customer = Database.customerList.get(username);

        if(customer!=null && password.equals(customer.getPassword()))
            return customer;
        else
            throw new WrongInfoException();
    }

    public static void register(String name, String username,String password) throws UserAlreadyExistException {
        if(Database.customerList.get(username)!=null)
            throw new UserAlreadyExistException();
        Customer.createUser(name, username, password, 1000);
    }

    public static void printAllMyTransaction(Customer customer){
        ArrayList<Transaction> collection = new ArrayList<>(Database.transactionsList.values());
        collection.removeIf(transaction -> {
            boolean b = !transaction.getSender().getID().equals(customer.getID());
            if (transaction.getReceiver() != null) {
                b = b && !transaction.getReceiver().getID().equals(customer.getID());
            }
            return b;
        }

        );

        for(Transaction transaction: collection){
            System.out.println("Process ID\t\t\t\t\t\t\t\t:" + transaction.getID());
            System.out.println("Process\t\t\t\t\t\t\t\t\t:" + transaction.getTransactionType());
            System.out.println("Amount\t\t\t\t\t\t\t\t\t:" + transaction.getAmount());
            System.out.println("Data\t\t\t\t\t\t\t\t\t:" + transaction.getData().toString());
            switch (transaction.getTransactionType()){
                case Transaction.TRANSFER -> {
                    System.out.println("Sender ID\t\t\t\t\t\t\t\t:" + transaction.getSender().getID());
                    System.out.println("Sender username\t\t\t\t\t\t\t:" + transaction.getSender().getUsername());
                    System.out.println("Receiver ID\t\t\t\t\t\t\t\t:" + transaction.getReceiver().getID());
                    System.out.println("Receiver username\t\t\t\t\t\t:" + transaction.getReceiver().getUsername());
                    System.out.println("Sender's amount before transaction\t\t:" + transaction.getSenderPrevAmount());
                    System.out.println("Sender's amount after transaction\t\t:" + transaction.getSenderCurrentAmount());
                    System.out.println("Receiver's amount before transaction\t:" + transaction.getReceiverPrevAmount());
                    System.out.println("Receiver's amount after transaction\t\t:" + transaction.getReceiverCurrentAmount());
                }
                case Transaction.DEPOSIT, Transaction.WITH_DRAW -> {
                    System.out.println("Customer's amount before the process\t:" + transaction.getSenderPrevAmount());
                    System.out.println("Customer's amount after the process\t\t:" + transaction.getSenderCurrentAmount());
                }
            }
            System.out.println();
        }

        System.out.println("Total transactions: " + collection.size());
        System.out.println();
        System.out.println();
    }
}
