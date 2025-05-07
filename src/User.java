import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User implements Serializable {

    private static final long DEFAULT_ACCOUNT_ID = 700000000000L;

    private String name;
    private String email;
    private String password;
    private long accountID;
    private String pin;
    private double balance;
    private boolean cardIssued;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private double rewardAmount;
    private List<FavouriteID> favouriteIDs;
    private List<MoneyRequests> payRequesters;
    private List<Transaction> transactions;
    private List<SharedCard> sharedCards;

    public User(String name, String email, String password) throws IOException {
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountID = latestAccountID() + 1;
        this.balance = 0;
        this.pin = "";
        this.cardIssued = false;
        this.cardNumber = "";
        this.expiryDate = "";
        this.cvv = "";
        this.rewardAmount = 0;
        this.favouriteIDs = new ArrayList<>();
        this.payRequesters = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.sharedCards = new ArrayList<>();
    }

    private long latestAccountID() throws IOException {
        File file = new File("latestAccountID.txt");
        Scanner sc = new Scanner(file);
        long id = 0;
        if (file.exists()) {
            if (sc.hasNext()) {
                id = Long.parseLong(sc.nextLine());
                sc.close();
            } else {
                id = DEFAULT_ACCOUNT_ID;
            }
        }
        return id;
    }

    public double getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(double rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    private void generateCardNumber() {
        int[] cardDigits = new int[16];
        cardDigits[0] = 4;
        for (int i = 1; i < 15; i++) {
            cardDigits[i] = (int) (Math.random() * 10);
        }

        int sum = 0;
        for (int i = 0; i < 15; i++) {
            int digit = cardDigits[i];
            if (i % 2 == 0) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
        }

        cardDigits[15] = (10 - (sum % 10)) % 10;

        StringBuilder card = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            card.append(cardDigits[i]);
        }
        setCardNumber(card.toString());

    }

    private void generateExpiryDate() {
        int month = (int) ((Math.random() * 11) + 1);
        int year = Year.now().getValue() % 100;
        year += 4;
        String expiry = String.format("%02d/%02d", month, year);
        setExpiryDate(expiry);
    }

    private void generateCvv() {
        int cvv = (int) (Math.random() * 1000);
        String cvvString = String.format("%03d", cvv);
        setCvv(cvvString);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isCardIssued() {
        return cardIssued;
    }

    public void setCardIssued(boolean cardIssued) {
        this.cardIssued = cardIssued;
        if (cardIssued) {
            generateCardNumber();
            generateExpiryDate();
            generateCvv();
        }
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

    public List<FavouriteID> getFavouriteIDs() {
        return favouriteIDs;
    }

    public void setFavouriteIDs(List<FavouriteID> favouriteIDs) {
        this.favouriteIDs = favouriteIDs;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public List<MoneyRequests> getPayRequesters() {
        return payRequesters;
    }

    public void setPayRequesters(List<MoneyRequests> payRequesters) {
        this.payRequesters = payRequesters;
    }

    public void addPaymentRequest(MoneyRequests paymentRequest) {
        this.payRequesters.add(paymentRequest);
    }

    public void addFavouriteID(FavouriteID favouriteID) {
        this.favouriteIDs.add(favouriteID);
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean isPinSet() {
        return pin != null && !pin.isEmpty();
    }

    public List<SharedCard> getSharedCards() {
        return sharedCards;
    }

    public void setSharedCards(List<SharedCard> sharedCards) {
        this.sharedCards = sharedCards;
    }

    public void addSharedCard(SharedCard sharedCard) {
        this.sharedCards.add(sharedCard);
    }

    @Override
    public String toString() {
        return "Name: " + name +
                "\nEmail: " + email +
                "\nPassword: " + password +
                "\nAccount ID: " + accountID;
    }
}
