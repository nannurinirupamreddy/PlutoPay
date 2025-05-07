import java.io.Serializable;

public class FavouriteID implements Serializable {
    private long accountID;
    private String name;

    public FavouriteID(long accountID, String name) {
        this.accountID = accountID;
        this.name = name;
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

    @Override
    public String toString() {
        return "Name: " + name + ", Account ID: " + accountID;
    }
}
