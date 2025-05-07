//import org.json.simple.parser.ParseException;
//
//import java.io.*;
//import java.util.Scanner;
//
//public class Main implements Serializable {
//
//    public static void main(String[] args) throws IOException, ParseException {
//
//        Scanner scanner = new Scanner(System.in);
//        Backend backend;
//        String selection;
//
//        try {
//            FileInputStream file = new FileInputStream("user_data.obj");
//            ObjectInputStream fin  = new ObjectInputStream(file);
//            backend = (Backend) fin.readObject();
//            fin.close();
//        } catch(IOException | ClassNotFoundException e) {
//            backend = new Backend();
//        }
//
//        if (backend.loggedInUser != null) {
//            System.out.println("\n👋 Welcome back, " + backend.loggedInUser.getName() + "!");
//            showMainMenu(backend, scanner);
//            return;
//        }
//
//        do {
//            System.out.println("[A] 🔑 Login\n[B] 👤 Register\n[Q] 🚪 Quit\n");
//            System.out.print("⏎ Please select an option: ");
//            selection = scanner.nextLine().toUpperCase();
//
//            switch (selection) {
//                case "A":
//                    System.out.println();
//                    System.out.print("📧 Enter Email: ");
//                    String email = scanner.nextLine();
//                    System.out.print("🔑 Enter Password: ");
//                    String password = scanner.nextLine();
//                    if (backend.userExists(email, password)) {
//                        System.out.println();
//                        System.out.println("👋 Hello, " + backend.loggedInUser.getName());
//                        backend.saveSession();
//                        showMainMenu(backend, scanner);
//                    } else {
//                        System.out.println("\nIncorrect email or password or account not found!\n");
//                    }
//                    break;
//                case "B":
//                    System.out.println();
//                    System.out.print("🖹 Enter name: ");
//                    String name = scanner.nextLine();
//                    System.out.print("📧 Enter email: ");
//                    String registerEmail = scanner.nextLine();
//                    System.out.print("🔑 Enter password: ");
//                    String registerPassword = scanner.nextLine();
//                    if (backend.registerUser(name, registerEmail, registerPassword)) {
//                        System.out.println("\nRegistered Successfully! You can now login!\n");
//                    } else {
//                        System.out.println("\nRegistration failed!\n");
//                    }
//                    break;
//                case "Q":
//                    System.out.println("\nExiting...");
//                    break;
//                default:
//                    System.out.println("\nInvalid Selection\n");
//                    break;
//            }
//        } while (!selection.equals("Q"));
//    }
//
//    public static void showMainMenu(Backend backend, Scanner scanner) throws IOException {
//        String secondSelection;
//        String selection = "";
//        do {
//            System.out.println();
//            System.out.println("[C] 💲 Pay\n[D] 🙏🏻 Request Money");
//            if (backend.isPinSet()) {
//                System.out.println("[E] ⚙️ Reset Pin");
//            } else {
//                System.out.println("[E] ⚙️ Set Pin");
//            }
//            System.out.println("[F] 💵 View Balance\n[G] 👀 View Transactions\n[H] 👀 View Rewards Amount\n[I] 🤑 Withdraw Rewards Amount");
//            if (backend.loggedInUser.isCardIssued()) {
//                System.out.println("[J] 💳 View Card Details");
//            } else {
//                System.out.println("[J] ✨ Issue Card");
//            }
//            System.out.println("[K] ❤️ Add Favourite IDs\n[L] ↗️ Pay From Favourite IDs\n[M] 💰 Add Money");
//            if (backend.loggedInUser.isCardIssued()) {
//                System.out.println("[N] 🔗 Share card with trusted account");
//            }
//            System.out.println("[O] 💳 Display cards shared with you\n[P] 💸 Pay with card shared with you");
//            if (backend.loggedInUser.isCardIssued()) {
//                System.out.println("[R] 💲 Pay Account With Your Card CVV");
//            }
//            System.out.println("[S] 🙏 Money Requests (" + backend.loggedInUser.getPayRequesters().size() + ")");
//            System.out.println("[T] 🙏 Reset Password\n[U] 👀 View Account Details\n[V] 🔙 Logout\n[Q] 🚪 Quit");
//            System.out.print("\n⏎ Please select an option: ");
//            secondSelection = scanner.nextLine().toUpperCase();
//            System.out.println();
//
//            switch (secondSelection) {
//                case "C":
//                    if (backend.isPinSet()) {
//                        System.out.print("Enter account number to pay to: ");
//                        long accountIDtoPay = scanner.nextLong();
//                        scanner.nextLine();
//                        System.out.print("Enter amount: ");
//                        double amountToPay = scanner.nextDouble();
//                        scanner.nextLine();
//                        System.out.print("Enter description: ");
//                        String payDescription = scanner.nextLine();
//                        System.out.print("Enter your pin: ");
//                        String pin = scanner.nextLine();
//                        if (backend.pinMatches(pin)) {
//                            backend.payUser(accountIDtoPay, amountToPay, payDescription);
//                        } else {
//                            System.out.println("Invalid pin.");
//                        }
//                    } else {
//                        System.out.println("Please set your pin first!");
//                    }
//                    break;
//                case "D":
//                    System.out.print("Enter account number to request from: ");
//                    long requestID = scanner.nextLong();
//                    scanner.nextLine();
//                    System.out.print("Enter amount: ");
//                    double reqAmount = scanner.nextDouble();
//                    scanner.nextLine();
//                    System.out.print("Enter description: ");
//                    String reqDesc = scanner.nextLine();
//                    backend.requestMoney(requestID, reqAmount, reqDesc);
//                    break;
//                case "E":
//                    System.out.print("Enter current password: ");
//                    String currentPass = scanner.nextLine();
//                    if (backend.passwordMatches(currentPass)) {
//                        System.out.print("Enter new pin: ");
//                        String newPin = scanner.nextLine();
//                        backend.setPin(newPin);
//                        System.out.println("Pin set successfully!");
//                    } else {
//                        System.out.println("Incorrect password. Cannot set/reset pin.");
//                    }
//                    break;
//                case "F":
//                    System.out.println("💰 Balance: " + backend.loggedInUser.getBalance());
//                    break;
//                case "G":
//                    backend.viewTransactions();
//                    break;
//                case "H":
//                    System.out.println("💰 Reward Amount: " + backend.loggedInUser.getRewardAmount());
//                    break;
//                case "I":
//                    if (backend.loggedInUser.getRewardAmount() > 0) {
//                        backend.withdrawRewardsAmount();
//                        System.out.println("✔️ Rewards amount withdrawn to your balance!");
//                    } else {
//                        System.out.println("⛔ No rewards available to withdraw.");
//                    }
//                    break;
//                case "J":
//                    if (backend.loggedInUser.isCardIssued()) {
//                        backend.viewCardDetails();
//                    } else {
//                        backend.issueCard();
//                        System.out.println("Card issued successfully.");
//                        backend.viewCardDetails();
//                    }
//                    break;
//                case "K":
//                    System.out.print("Enter account ID to add as favourite: ");
//                    long favID = scanner.nextLong();
//                    scanner.nextLine();
//                    backend.addFavouriteID(favID);
//                    break;
//                case "L":
//                    if (!backend.loggedInUser.getFavouriteIDs().isEmpty()) {
//                        backend.displayFavouriteIDs();
//                        System.out.print("Enter favourite position: ");
//                        int pos = scanner.nextInt();
//                        scanner.nextLine();
//                        System.out.print("Enter amount: ");
//                        double amt = scanner.nextDouble();
//                        scanner.nextLine();
//                        System.out.print("Enter description: ");
//                        String desc = scanner.nextLine();
//                        backend.payFavouriteID(pos - 1, amt, desc);
//                    } else {
//                        System.out.println("No favourites found.");
//                    }
//                    break;
//                case "M":
//                    System.out.print("Enter amount to add: ");
//                    double addBal = scanner.nextDouble();
//                    scanner.nextLine();
//                    backend.addBalance(addBal);
//                    System.out.println("Money added to your balance.");
//                    break;
//                case "N":
//                    System.out.print("Enter account ID to share card with: ");
//                    long shareID = scanner.nextLong();
//                    scanner.nextLine();
//                    System.out.print("Enter usage limit: ");
//                    int limit = scanner.nextInt();
//                    scanner.nextLine();
//                    backend.shareCard(shareID, limit);
//                    break;
//                case "O":
//                    backend.displaySharedCards();
//                    break;
//                case "P":
//                    backend.displaySharedCards();
//                    System.out.print("Enter card index to use: ");
//                    int cardIndex = scanner.nextInt();
//                    scanner.nextLine();
//                    System.out.print("Enter account ID to pay: ");
//                    long payTo = scanner.nextLong();
//                    scanner.nextLine();
//                    System.out.print("Enter amount: ");
//                    double amount = scanner.nextDouble();
//                    scanner.nextLine();
//                    System.out.print("Enter description: ");
//                    String descCard = scanner.nextLine();
//                    backend.payWithSharedCard(cardIndex - 1, amount, payTo, descCard);
//                    break;
//                case "R":
//                    System.out.print("Enter account ID to pay: ");
//                    long accID = scanner.nextLong();
//                    scanner.nextLine();
//                    System.out.print("Enter amount: ");
//                    double amtCVV = scanner.nextDouble();
//                    scanner.nextLine();
//                    System.out.print("Enter description: ");
//                    String descCVV = scanner.nextLine();
//                    System.out.print("Enter CVV: ");
//                    String cvv = scanner.nextLine();
//                    if (backend.cvvMatches(cvv)) {
//                        backend.payUser(accID, amtCVV, descCVV);
//                    } else {
//                        System.out.println("Invalid CVV.");
//                    }
//                    break;
//                case "S":
//                    backend.viewMoneyRequests();
//                    System.out.print("Pay any request? (Y/N): ");
//                    selection = scanner.nextLine().toUpperCase();
//                    if (selection.equals("Y") || selection.equals("YES")) {
//                        System.out.print("Enter request index: ");
//                        int reqIndex = scanner.nextInt();
//                        scanner.nextLine();
//                        backend.payMoneyRequest(reqIndex - 1);
//                    }
//                    break;
//                case "T":
//                    System.out.print("Enter current password: ");
//                    String currPass = scanner.nextLine();
//                    if (backend.passwordMatches(currPass)) {
//                        System.out.print("Enter new password: ");
//                        String newPass = scanner.nextLine();
//                        backend.resetPassword(newPass);
//                    } else {
//                        System.out.println("Incorrect password.");
//                    }
//                    break;
//                case "U":
//                    System.out.println(backend.loggedInUser);
//                    break;
//                case "V":
//                    backend.clearSession();
//                    System.out.println("Logging out of the App!\n");
//                    break;
//                case "Q":
//                    backend.saveSession();
//                    System.out.println("Exiting the App...");
//                    selection = "Q";
//                    FileOutputStream file = new FileOutputStream("user_data" +
//                            ".obj");
//                    ObjectOutputStream fout = new ObjectOutputStream(file);
//                    fout.writeObject(backend);
//                    fout.close();
//                    break;
//                default:
//                    System.out.println("⚠️ Invalid selection.");
//                    break;
//            }
//
//        } while (!secondSelection.equals("V") && !secondSelection.equals("Q"));
//    }
//}