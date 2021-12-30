import exceptions.NoEnoughCurrency;
import exceptions.UserNotFoundException;

import java.util.Date;
import java.util.UUID;

public class Transaction {
    private final String ID = UUID.randomUUID().toString();
    private final Date data;
    private final int amount;
    private final Customer sender;
    private final Customer receiver;
    private final int senderCurrentAmount;
    private final int receiverCurrentAmount;
    private final int senderPrevAmount;
    private final int receiverPrevAmount;
    private final String transactionType;

    private Transaction(Customer sender, Customer receiver, int amount) throws NoEnoughCurrency {
        if(sender.getCurrency() < amount)
            throw new NoEnoughCurrency();

        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.senderPrevAmount = sender.getCurrency();
        this.receiverPrevAmount = receiver.getCurrency();

        this.senderCurrentAmount = sender.getCurrency() - amount;
        this.receiverCurrentAmount = receiver.getCurrency() + amount;
        sender.setCurrency(this.senderCurrentAmount);
        receiver.setCurrency(this.receiverCurrentAmount);
        this.transactionType = TRANSFER;
        this.data = new Date();
    }

    private Transaction(Customer sender, int amount, String transactionType) throws NoEnoughCurrency{
        this.sender = sender;
        this.amount = amount;
        this.senderPrevAmount = sender.getCurrency();

        switch (transactionType){
            case WITH_DRAW -> {
                if(sender.getCurrency() < amount)
                    throw new NoEnoughCurrency();
                sender.setCurrency(sender.getCurrency() - amount);
            }
            case DEPOSIT -> sender.setCurrency(sender.getCurrency() + amount);
        }

        this.senderCurrentAmount = sender.getCurrency();
        this.transactionType = transactionType;
        this.data = new Date();

        receiver = null;
        receiverCurrentAmount = 0;
        receiverPrevAmount = 0;
    }

    public String getID() {
        return ID;
    }

    public Date getData() {
        return data;
    }

    public int getAmount() {
        return amount;
    }

    public Customer getSender() {
        return sender;
    }

    public Customer getReceiver() {
        return receiver;
    }

    public int getSenderCurrentAmount() {
        return senderCurrentAmount;
    }

    public int getReceiverCurrentAmount() {
        return receiverCurrentAmount;
    }

    public int getSenderPrevAmount() {
        return senderPrevAmount;
    }

        public int getReceiverPrevAmount() {
        return receiverPrevAmount;
    }


    // Static method
    public static void doTransaction(Customer sender, String receiverUsername, int amount) throws NoEnoughCurrency, UserNotFoundException {
        Customer receiverCustomer = Database.customerList.get(receiverUsername);
        if (receiverCustomer==null) throw new UserNotFoundException();
        Transaction transaction = new Transaction(sender, receiverCustomer, amount);
        Database.transactionsList.put(transaction.getID(), transaction);
    }

    public static void doTransaction(Customer sender, int amount, String transactionType) throws NoEnoughCurrency {
        Transaction transaction = new Transaction(sender, amount, transactionType);
        Database.transactionsList.put(transaction.getID(), transaction);
    }

    public static final String DEPOSIT = "DEPOSIT";
    public static final String WITH_DRAW = "WITH_DRAW";
    public static final String TRANSFER = "TRANSFER";

    public String getTransactionType() {
        return transactionType;
    }
}
