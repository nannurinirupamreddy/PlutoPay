import java.io.Serializable;

public class MoneyRequests implements Serializable {
    private long accountID;
    private String name;
    private double amount;
    private String description;

    public MoneyRequests(long accountID, String name, double amount,
                         String description) {
        this.accountID = accountID;
        this.name = name;
        this.amount = amount;
        this.description = description;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Name: " + name + ", Amount: " + amount + ", Description: " + description;
    }

}
