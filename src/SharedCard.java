import java.io.Serializable;

public class SharedCard implements Serializable {
    private String cardOwnerName;
    private long cardOwnerAccountID;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private int cardUsageLimit;
    private long sharedToAccountID;

    public SharedCard(String cardOwnerName, long cardOwnerAccountID, String cardNumber, String expiryDate, String cvv, int cardUsageLimit, long sharedToAccountID) {
        this.cardOwnerName = cardOwnerName;
        this.cardOwnerAccountID = cardOwnerAccountID;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.cardUsageLimit = cardUsageLimit;
        this.sharedToAccountID = sharedToAccountID;
    }

    public String getCardOwnerName() {
        return cardOwnerName;
    }

    public void setCardOwnerName(String cardOwnerName) {
        this.cardOwnerName = cardOwnerName;
    }

    public long getCardOwnerAccountID() {
        return cardOwnerAccountID;
    }

    public void setCardOwnerAccountID(long cardOwnerAccountID) {
        this.cardOwnerAccountID = cardOwnerAccountID;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public int getCardUsageLimit() {
        return cardUsageLimit;
    }

    public void setCardUsageLimit(int cardUsageLimit) {
        this.cardUsageLimit = cardUsageLimit;
    }

    public long getSharedToAccountID() {
        return sharedToAccountID;
    }

    public void setSharedToAccountID(long sharedToAccountID) {
        this.sharedToAccountID = sharedToAccountID;
    }

    @Override
    public String toString() {
        return "Card Owner: " + cardOwnerName + ", Account ID: " + cardOwnerAccountID + ", Usage Remaining: " + cardUsageLimit;
    }

}
