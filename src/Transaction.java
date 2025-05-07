import java.io.Serializable;

public class Transaction implements Serializable {
    private long transactionId;
    private String transactionType;
    private long senderID;
    private String senderName;
    private long receiverID;
    private String receiverName;
    private double amount;
    private String description;

    public Transaction() {

    }

    public Transaction(long senderID, String senderName,
                       long receiverID, String receiverName, double amount, String description) {
        transactionIDGenerator();
        this.transactionId = getTransactionId();
        this.senderID = senderID;
        this.senderName = senderName;
        this.receiverID = receiverID;
        this.receiverName = receiverName;
        this.amount = amount;
        this.description = description;
    }

    public long getTransactionId() {
        return transactionId;
    }

    private void transactionIDGenerator() {
        long transactionID = (long) (Math.random() * 10000000000000L);
        setTransactionId(transactionID);
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getSenderID() {
        return senderID;
    }

    public void setSenderID(long senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public long getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(long receiverID) {
        this.receiverID = receiverID;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        if (transactionType.contains("PAID")) {
            return String.format("%-23s", transactionId + "\uD83E\uDEAA") +
                    String.format("%-28s", transactionType + "↗️") +
                    String.format("%-19s", receiverID + "\uD83E\uDEAA") +
                    String.format("%-21s", receiverName + "\uD83D\uDDB9") +
                    String.format("%-14s", amount + "\uD83D\uDCB8") +
                    String.format("%-19s", description + "\uD83D\uDDB9");
        } else {
            return String.format("%-23s", transactionId + "\uD83E\uDEAA") +
                    String.format("%-28s", transactionType + "↙️") +
                    String.format("%-19s", senderID + "\uD83E\uDEAA") +
                    String.format("%-21s", senderName + "\uD83D\uDDB9") +
                    String.format("%-14s", amount + "\uD83D\uDCB8") +
                    String.format("%-19s", description + "\uD83D\uDDB9");
        }
    }

}