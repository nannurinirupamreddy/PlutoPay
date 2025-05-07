import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Backend implements Serializable {

    private FileInputStream fis = new FileInputStream("users.json");
    private InputStreamReader isr = new InputStreamReader(fis);
    private JSONParser parser = new JSONParser();
    private JSONArray objs;
    private static final String SESSION_FILE = "session.ser";


    private List<User> users;
    User loggedInUser;
    private FileWriter writer;

    public Backend() throws IOException, ParseException {
        users = getUsers();
    }

    public boolean userExists(String email, String password) {
        if (!users.isEmpty()) {
            for (User user : users) {
                if (user.getEmail().equals(email)){
                    if (user.getPassword().equals(password)){
                        loggedInUser = user;
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public boolean registerUser(String name, String email, String password) throws IOException {
        if (userExists(email, password)) {
            return false;
        }
        loggedInUser = new User(name, email, password);
        users.add(loggedInUser);
        writer = new FileWriter("latestAccountID.txt");
        writer.write(String.valueOf(loggedInUser.getAccountID()));
        writer.flush();
        writer.close();
        saveData();
        return true;
    }

    public void payUser(long accountID, double amount, String description) {
        if (accountID == loggedInUser.getAccountID()) {
            System.out.println();
            System.out.println("You cannot pay yourself!");
            return;
        }
        if (amount > loggedInUser.getBalance()) {
            System.out.println();
            System.out.println("Insufficient balance!");
            return;
        }
        for (User user : users) {
            if (user.getAccountID() == accountID) {
                loggedInUser.setBalance(loggedInUser.getBalance() - amount);
                user.setBalance(user.getBalance() + amount);
                Transaction paidTransaction =
                        new Transaction(loggedInUser.getAccountID(),
                                loggedInUser.getName(), user.getAccountID(),
                                user.getName(), amount, description);
                paidTransaction.setTransactionType("PAID");
                loggedInUser.addTransaction(paidTransaction);
                Transaction receivedTransaction =
                        new Transaction(loggedInUser.getAccountID(),
                                loggedInUser.getName(), user.getAccountID(),
                                user.getName(), amount, description);
                receivedTransaction.setTransactionType("RECEIVED");
                receivedTransaction.setTransactionId(paidTransaction.getTransactionId());
                user.addTransaction(receivedTransaction);
                loggedInUser.setRewardAmount(loggedInUser.getRewardAmount() + rewardsCalculator(amount));
                System.out.println("\n⚡ Payment successful!");
                saveData();
                return;
            }
        }
        System.out.println("\uD83E\uDD79 Account Not Found!");
    }

    private List<User> getUsers() throws IOException, ParseException {
        List<User> users = new ArrayList<>();
        File file = new File("users.json");
        if (!file.exists() || file.length() == 0) {
            return users;
        }
        objs = (JSONArray) parser.parse(isr);
        if (objs != null) {
            for (int i = 0; i < objs.size(); i++) {
                JSONObject obj = (JSONObject) objs.get(i);
                User user = new User((String) obj.get("name"), (String) obj.get("email"), (String) obj.get("password"));
                user.setAccountID((long) obj.get("accountID"));
                user.setPin((String) obj.get("pin"));
                user.setBalance((double) obj.get("balance"));
                user.setCardIssued((boolean) obj.get("cardIssued"));
                user.setCardNumber((String) obj.get("cardNumber"));
                user.setExpiryDate((String) obj.get("expiryDate"));
                user.setCvv((String) obj.get("cvv"));
                user.setRewardAmount((double) obj.get("rewardAmount"));
                List<FavouriteID> favouriteUsers = new ArrayList<>();
                JSONArray favourites = (JSONArray) obj.get("favouriteIDs");
                if (favourites != null) {
                    for (int j = 0; j < favourites.size(); j++) {
                        JSONObject favourite = (JSONObject) favourites.get(j);
                        FavouriteID favouriteUser =
                                new FavouriteID((long) favourite.get("accountID"), (String) favourite.get("name"));
                        favouriteUsers.add(favouriteUser);
                    }
                }
                user.setFavouriteIDs(favouriteUsers);
                List<MoneyRequests> payRequesters = new ArrayList<>();
                JSONArray payRequests = (JSONArray) obj.get("payRequesters");
                if (payRequests != null) {
                    for (int j = 0; j < payRequests.size(); j++) {
                        JSONObject payReq = (JSONObject) payRequests.get(j);
                        MoneyRequests moneyRequest =
                                new MoneyRequests((long) payReq.get("accountID"),
                                        (String) payReq.get("name"),
                                        (double) payReq.get("amount"),
                                        (String) payReq.get("description"));
                        payRequesters.add(moneyRequest);
                    }
                }
                user.setPayRequesters(payRequesters);
                List<Transaction> transactions = new ArrayList<>();
                JSONArray transaction = (JSONArray) obj.get("transactions");
                if (transaction != null) {
                    for (int j = 0; j < transaction.size(); j++) {
                        JSONObject transactionObj = (JSONObject) transaction.get(j);
                        Transaction userTransaction = new Transaction();
                        userTransaction.setTransactionId((long) transactionObj.get("transactionID"));
                        userTransaction.setTransactionType((String) transactionObj.get("transactionType"));
                        userTransaction.setSenderID((long) transactionObj.get("senderID"));
                        userTransaction.setSenderName((String) transactionObj.get("senderName"));
                        userTransaction.setReceiverID((long) transactionObj.get("receiverID"));
                        userTransaction.setReceiverName((String) transactionObj.get("receiverName"));
                        userTransaction.setAmount((double) transactionObj.get("amount"));
                        userTransaction.setDescription((String) transactionObj.get("description"));
                        transactions.add(userTransaction);
                    }
                }
                user.setTransactions(transactions);
                JSONArray sharedCards = (JSONArray) obj.get("sharedCards");
                List<SharedCard> sharedCardList = new ArrayList<>();
                if (sharedCards != null) {
                    for (int j = 0; j < sharedCards.size(); j++) {
                        JSONObject sharedCard = (JSONObject) sharedCards.get(j);
                        SharedCard sharedCardObj =
                                new SharedCard((String) sharedCard.get("cardOwnerName"),
                                        (long) sharedCard.get("cardOwnerAccountID"),
                                        (String) sharedCard.get("cardNumber"),
                                        (String) sharedCard.get("expiryDate"),
                                        (String) sharedCard.get("cvv"),
                                        ((Long) sharedCard.get("cardUsageLimit")).intValue(),
                                        (long) sharedCard.get("sharedToAccountID"));
                        sharedCardList.add(sharedCardObj);
                    }
                }
                user.setSharedCards(sharedCardList);
                users.add(user);
            }
        }
        return users;
    }

    private void saveData() {
        JSONArray userArray = new JSONArray();
        for (User user : users) {
            JSONObject obj = new JSONObject();
            obj.put("name", user.getName());
            obj.put("email", user.getEmail());
            obj.put("password", user.getPassword());
            obj.put("accountID", user.getAccountID());
            obj.put("pin", user.getPin());
            obj.put("balance", user.getBalance());
            obj.put("cardIssued", user.isCardIssued());
            obj.put("cardNumber", user.getCardNumber());
            obj.put("expiryDate", user.getExpiryDate());
            obj.put("cvv", user.getCvv());
            obj.put("rewardAmount", user.getRewardAmount());
            JSONArray favouritesArray = new JSONArray();
            for (FavouriteID favouriteID : user.getFavouriteIDs()) {
                JSONObject favouriteObj = new JSONObject();
                favouriteObj.put("accountID", favouriteID.getAccountID());
                favouriteObj.put("name", favouriteID.getName());
                favouritesArray.add(favouriteObj);
            }
            obj.put("favouriteIDs", favouritesArray);
            JSONArray payRequests = new JSONArray();
            if (user.getPayRequesters() != null) {
                for (MoneyRequests moneyRequest : user.getPayRequesters()) {
                    JSONObject payRequestObj = new JSONObject();
                    payRequestObj.put("accountID", moneyRequest.getAccountID());
                    payRequestObj.put("name", moneyRequest.getName());
                    payRequestObj.put("amount", moneyRequest.getAmount());
                    payRequestObj.put("description", moneyRequest.getDescription());
                    payRequests.add(payRequestObj);
                }
            }
            obj.put("payRequesters", payRequests);
            JSONArray transactionsArray = new JSONArray();
            if (user.getTransactions() != null) {
                for (Transaction transaction : user.getTransactions()) {
                    JSONObject transactionObj = new JSONObject();
                    transactionObj.put("transactionID", transaction.getTransactionId());
                    transactionObj.put("transactionType", transaction.getTransactionType());
                    transactionObj.put("senderID", transaction.getSenderID());
                    transactionObj.put("senderName", transaction.getSenderName());
                    transactionObj.put("receiverID", transaction.getReceiverID());
                    transactionObj.put("receiverName", transaction.getReceiverName());
                    transactionObj.put("amount", transaction.getAmount());
                    transactionObj.put("description", transaction.getDescription());
                    transactionsArray.add(transactionObj);
                }
            }
            obj.put("transactions", transactionsArray);
            JSONArray sharedCardsList = new JSONArray();
            if (user.getSharedCards() != null) {
                for (SharedCard sharedCard : user.getSharedCards()) {
                    JSONObject sharedCardObj = new JSONObject();
                    sharedCardObj.put("cardOwnerName", sharedCard.getCardOwnerName());
                    sharedCardObj.put("cardOwnerAccountID", sharedCard.getCardOwnerAccountID());
                    sharedCardObj.put("cardNumber", sharedCard.getCardNumber());
                    sharedCardObj.put("expiryDate", sharedCard.getExpiryDate());
                    sharedCardObj.put("cvv", sharedCard.getCvv());
                    sharedCardObj.put("cardUsageLimit", sharedCard.getCardUsageLimit());
                    sharedCardObj.put("sharedToAccountID",
                            sharedCard.getSharedToAccountID());
                    sharedCardsList.add(sharedCardObj);
                }
            }
            obj.put("sharedCards", sharedCardsList);
            userArray.add(obj);
        }
        try {
            writer = new FileWriter("users.json");
            writer.write(userArray.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void requestMoney(long requesterID, double amount, String description) {
        // check no elements in list
        if (requesterID == loggedInUser.getAccountID()) {
            System.out.println();
            System.out.println("You cannot request yourself!");
            return;
        }
        for (User user : users) {
            if (user.getAccountID() == requesterID) {
                MoneyRequests moneyRequest =
                        new MoneyRequests(loggedInUser.getAccountID(),
                                loggedInUser.getName(), amount, description);
                user.addPaymentRequest(moneyRequest);
                System.out.println();
                System.out.println("Requested " + requesterID + " for " + amount);
                saveData();
                return;
            }
        }
    }

    public void addFavouriteID(long favouriteID) {
        if (favouriteID == loggedInUser.getAccountID()) {
            System.out.println();
            System.out.println("You cannot add yourself as favorite!");
            return;
        }
        for (FavouriteID favorites : loggedInUser.getFavouriteIDs()) {
            if (favorites.getAccountID() == favouriteID) {
                System.out.println();
                System.out.println("You already have this ID added as " +
                        "favorites!");
                return;
            }
        }
        for (User user : users) {
            if (user.getAccountID() == favouriteID) {
                loggedInUser.addFavouriteID(new FavouriteID(favouriteID, user.getName()));
                System.out.println("\n" + user.getName() + " added to " +
                        "favourites");
                saveData();
                return;
            }
        }
    }

    public void withdrawRewardsAmount() {
        double rewardsAmount = loggedInUser.getRewardAmount();
        loggedInUser.setBalance(loggedInUser.getBalance() + rewardsAmount);
        loggedInUser.setRewardAmount(0);
        Transaction transaction = new Transaction();
        transaction.setReceiverID(loggedInUser.getAccountID());
        transaction.setReceiverName(loggedInUser.getName());
        transaction.setAmount(rewardsAmount);
        transaction.setDescription("Rewards Withdrawal Amount");
        transaction.setTransactionType("RECEIVED");
        transaction.setSenderID(0);
        transaction.setSenderName("PlutoPay✅");
        loggedInUser.addTransaction(transaction);
        saveData();
    }

    public void issueCard() {
        if (!loggedInUser.isCardIssued()) {
            loggedInUser.setCardIssued(true);
        }
        saveData();
    }

    public void addBalance(double amount) {
        loggedInUser.setBalance(loggedInUser.getBalance() + amount);
        saveData();
    }

    public void resetPassword(String newPassword) {
        loggedInUser.setPassword(newPassword);
        saveData();
    }

    public boolean passwordMatches(String password) {
        return loggedInUser.getPassword().equals(password);
    }

    public void viewCardDetails() {
        if (loggedInUser.isCardIssued()) {
            System.out.println("Here are your Card Details ⬇️");
            System.out.println("Card Number: " + loggedInUser.getCardNumber());
            System.out.println("Expiry Date: " + loggedInUser.getExpiryDate());
            System.out.println("CVV: " + loggedInUser.getCvv());
            return;
        }
        System.out.println("You haven't issued a card yet!");
    }

    public void viewBalance() {
        System.out.println("Balance: " + loggedInUser.getBalance());
    }

    public void viewTransactions() {
        List<Transaction> transactions = loggedInUser.getTransactions();

        if (transactions == null || transactions.isEmpty()) {
            System.out.println("🛈 You don't have any transactions yet!");
            return;
        }
        System.out.println("Your transactions: \n");
        transactionsTable();
        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }
    }


    public void transactionsTable() {
        System.out.println(String.format("%-23s%-28s%-19s%-21s%-14s%-19s",
                "Transaction ID", "Transaction Type", "ID",
                "Name", "Amount", "Description"));
        System.out.println("-".repeat(124));
    }

    private double rewardsCalculator(double amount) {
        return amount * 0.05;
    }

    public boolean pinMatches(String pin) {
        return loggedInUser.getPin().equals(pin);
    }

    public boolean isPinSet() {
        return loggedInUser.isPinSet();
    }

    public void setPin(String pin) {
        loggedInUser.setPin(pin);
        saveData();
    }

    public boolean cvvMatches(String cvv) {
        return loggedInUser.getCvv().equals(cvv);
    }

    public void shareCard(long accountID, int cardUsageLimit) {
        for (User user : users) {
            if (user.getAccountID() == accountID) {
                SharedCard sharedCard = new SharedCard(loggedInUser.getName(), loggedInUser.getAccountID(), loggedInUser.getCardNumber(), loggedInUser.getExpiryDate(), loggedInUser.getCvv(), cardUsageLimit, accountID);
                user.addSharedCard(sharedCard);
                saveData();
                return;
            }
        }
    }

    public void payWithSharedCard(int index, double amount,
                                  long toPayToAccountID, String description) {
        payWithSharedCard(loggedInUser.getSharedCards().get(index), amount, toPayToAccountID, description);
    }

    private void payWithSharedCard(SharedCard sharedCard, double amount,
                                   long toPayToAccountID, String description) {
        User payingUser = null;
        User receivingUser = null;
        for (User user : users) {
            if (sharedCard.getCardNumber().equals(user.getCardNumber())) {
                payingUser = user;
            }
            if (user.getAccountID() == toPayToAccountID) {
                receivingUser = user;
            }
        }
        if (payingUser != null && receivingUser != null) {
            if (sharedCard.getCardOwnerAccountID() == receivingUser.getAccountID()) {
                System.out.println();
                System.out.println("😏 Can't pay the same account as the " +
                        "shared card owner account!");
                return;
            }
            if (payingUser.getBalance() >= amount) {
                payingUser.setBalance(payingUser.getBalance() - amount);
                payingUser.setRewardAmount(payingUser.getRewardAmount() + rewardsCalculator(amount));
                receivingUser.setBalance(receivingUser.getBalance() + amount);
                sharedCard.setCardUsageLimit(sharedCard.getCardUsageLimit() - 1);
                if (sharedCard.getCardUsageLimit() <= 0) {
                    loggedInUser.getSharedCards().remove(sharedCard);
                }
                Transaction cardOwnerTransaction =
                        new Transaction(loggedInUser.getAccountID(),
                                loggedInUser.getName(), receivingUser.getAccountID(),
                                receivingUser.getName(), amount, description);
                cardOwnerTransaction.setTransactionType("PAID By " + loggedInUser.getName());
                payingUser.addTransaction(cardOwnerTransaction);
                Transaction paidTransaction =
                        new Transaction(loggedInUser.getAccountID(),
                                loggedInUser.getName(), receivingUser.getAccountID(),
                                receivingUser.getName(), amount, description);
                paidTransaction.setTransactionType("PAID Using " + payingUser.getName() + "'s card");
                paidTransaction.setTransactionId(cardOwnerTransaction.getTransactionId());
                loggedInUser.addTransaction(paidTransaction);
                Transaction receivedTransaction =
                        new Transaction(loggedInUser.getAccountID(),
                                loggedInUser.getName(), receivingUser.getAccountID(),
                                receivingUser.getName(), amount, description);
                receivedTransaction.setTransactionType("RECEIVED");
                receivedTransaction.setTransactionId(paidTransaction.getTransactionId());
                receivingUser.addTransaction(receivedTransaction);
                System.out.println();
                System.out.println("⚡ Paid successfully!");
            } else {
                System.out.println();
                System.out.println("Insufficient balance in shared card!");
            }
        } else {
            System.out.println("Invalid account ID!");
        }
        saveData();
    }

    public void displaySharedCards() {
        System.out.println("💌 Cards being shared with you: \n");
        for(int i = 0; i < loggedInUser.getSharedCards().size(); i++) {
            SharedCard sharedCard = loggedInUser.getSharedCards().get(i);
            System.out.println((i + 1) + ". " + sharedCard.toString());
        }
    }

    public void viewMoneyRequests() {
        for (MoneyRequests moneyRequests : loggedInUser.getPayRequesters()) {
            System.out.println(moneyRequests.toString());
        }
        saveData();
    }

    public void payMoneyRequest(int index) {
        MoneyRequests moneyRequest = loggedInUser.getPayRequesters().get(index);
        payUser(moneyRequest.getAccountID(), moneyRequest.getAmount(), moneyRequest.getDescription());
        List<MoneyRequests> newList = loggedInUser.getPayRequesters();
        newList.remove(index);
        loggedInUser.setPayRequesters(newList);
        saveData();
    }

    public void displayFavouriteIDs() {
        for(int i = 0; i < loggedInUser.getFavouriteIDs().size(); i++) {
            FavouriteID favouriteID = loggedInUser.getFavouriteIDs().get(i);
            System.out.println((i + 1) + ". " + favouriteID.toString());
        }
    }

    public void payFavouriteID(int index, double amount, String description) {
        FavouriteID favouriteID = loggedInUser.getFavouriteIDs().get(index);
        payUser(favouriteID.getAccountID(), amount, description);
    }

}
