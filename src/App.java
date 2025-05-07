import java.io.Serializable;
import java.util.Scanner;

public class App implements Serializable {

    public static void main(String[] args) {
        try {
            Backend backend = new Backend();
            Scanner scanner = new Scanner(System.in);
            String selection;
            do {
                System.out.println("[A] \uD83D\uDD11 Login\n[B] \uD83D\uDC64 " +
                        "Register\n[Q] \uD83D\uDEAA Quit\n");
                System.out.print("⏎ Please select an option: ");
                selection = scanner.nextLine().toUpperCase();
                switch (selection) {
                    case "A":
                        System.out.println();
                        System.out.print("\uD83D\uDCE7 Enter Email: ");
                        String email = scanner.nextLine();
                        System.out.print("\uD83D\uDD11 Enter Password: ");
                        String password = scanner.nextLine();
                        if (backend.userExists(email, password)) {
                            System.out.println();
                            System.out.println("\uD83D\uDC4B Hello, " + backend.loggedInUser.getName());
                            String secondSelection;
                            do {
                                System.out.println();
                                System.out.println("[C] \uD83D\uDCB2 Pay\n[D]" +
                                        " \uD83D\uDE4F\uD83C\uDFFB Request " +
                                        "Money");
                                if (backend.isPinSet()) {
                                    System.out.println("[E] ⚙️ Reset Pin");
                                } else {
                                    System.out.println("[E] ⚙️ Set Pin");
                                }
                                System.out.println("[F] \uD83D\uDCB5 View " +
                                        "Balance\n[G] \uD83D\uDC40 View " +
                                        "Transactions\n[H] \uD83D\uDC40 View " +
                                        "Rewards Amount\n[I] \uD83E\uDD11 " +
                                        "Withdraw Rewards Amount");
                                if (backend.loggedInUser.isCardIssued()) {
                                    System.out.println("[J] \uD83D\uDCB3 View" +
                                            " Card Details");
                                } else {
                                    System.out.println("[J] ✨ Issue Card");
                                }
                                System.out.println("[K] ❤️ Add Favourite " +
                                        "IDs\n[L] ↗️ Pay From Favourite " +
                                        "IDs\n" + "[M] \uD83D\uDCB0 Add " +
                                        "Money");
                                if (backend.loggedInUser.isCardIssued()) {
                                    System.out.println("[N] \uD83D\uDD17 " +
                                            "Share card with trusted account");
                                }
                                System.out.println("[O] \uD83D\uDCB3 " +
                                        "Display cards shared with you\n" +
                                        "[P] \uD83D\uDCB8 Pay with card " +
                                        "shared with you");
                                if (backend.loggedInUser.isCardIssued()) {
                                    System.out.println("[R] \uD83D\uDCB2 Pay " +
                                            "Account With Your Card CVV");
                                }
                                System.out.println("[S] \uD83D\uDE4F Money " +
                                        "Requests (" +
                                        backend.loggedInUser.getPayRequesters().size() + ")");
                                System.out.println("[T] \uD83D\uDE4F Reset " +
                                        "Password\n[U] \uD83D\uDC40 View " +
                                        "Account Details\n[V] \uD83D\uDD19 " +
                                        "Logout\n[Q] \uD83D\uDEAA Quit");
                                System.out.print("\n⏎ Please select an " +
                                        "option: ");
                                secondSelection = scanner.nextLine().toUpperCase();
                                System.out.println();
                                switch (secondSelection) {
                                    case "C":
                                        if (backend.isPinSet()) {
                                            System.out.print("⏎ Enter account" +
                                                    " number to pay to: ");
                                            long accountIDtoPay =
                                                    scanner.nextLong();
                                            scanner.nextLine();
                                            System.out.print("⏎ Enter amount:" +
                                                    " ");
                                            double amountToPay =
                                                    scanner.nextDouble();
                                            scanner.nextLine();
                                            System.out.print("⏎ Enter " +
                                                    "description: ");
                                            String payDescription =
                                                    scanner.nextLine();
                                            String pin;
                                            int failedAttempts = 0;
                                            do {
                                                System.out.print("⏎ Enter " +
                                                        "your pin: ");
                                                pin = scanner.nextLine();
                                                if (!backend.pinMatches(pin)) {
                                                    failedAttempts++;
                                                }
                                                if (failedAttempts >= 3) {
                                                    System.out.println(
                                                            "Forgot" +
                                                                    " your pin? Try " +
                                                                    "resetting your " +
                                                                    "pin or pay using" +
                                                                    " card!");
                                                }
                                            } while (!backend.pinMatches(pin));
                                            backend.payUser(accountIDtoPay,
                                                    amountToPay,
                                                    payDescription);
                                        } else {
                                            System.out.println("Please set " +
                                                    "your pin first!");
                                        }
                                        break;
                                    case "D":
                                        System.out.print("⏎ Enter account " +
                                                "number to request from: ");
                                        long accountIDtoRequest =
                                                scanner.nextLong();
                                        scanner.nextLine();
                                        System.out.print("⏎ Enter amount: ");
                                        double amount = scanner.nextDouble();
                                        scanner.nextLine();
                                        System.out.print("⏎ Enter " +
                                                "description: ");
                                        String requestDescription =
                                                scanner.nextLine();
                                        backend.requestMoney(accountIDtoRequest, amount, requestDescription);
                                        break;
                                    case "E":
                                        System.out.print("⏎ Enter current " +
                                                "password: ");
                                        String passwordForPinReset =
                                                scanner.nextLine();
                                        if (backend.passwordMatches(passwordForPinReset)) {
                                            System.out.print("⏎ Enter new " +
                                                    "pin: ");
                                            String newPin = scanner.nextLine();
                                            backend.setPin(newPin);
                                            System.out.println();
                                            System.out.println("✔️ Your new " +
                                                    "pin " +
                                                    "has been set! You can " +
                                                    "now use your new pin to " +
                                                    "make transactions.");
                                        } else {
                                            System.out.println("🚫 Your " +
                                                    "password is incorrect. " +
                                                    "Can't reset your pin!");
                                        }
                                        break;
                                    case "F":
                                        System.out.print("\uD83D\uDCB8 Your" +
                                                " balance: ");
                                        System.out.println(backend.loggedInUser.getBalance());
                                        break;
                                    case "G":
                                        backend.viewTransactions();
                                        break;
                                    case "H":
                                        System.out.print("\uD83D\uDCB8 " +
                                                "Earned Reward " +
                                                "Amount from last withdrawal:" +
                                                " ");
                                        System.out.println(backend.loggedInUser.getRewardAmount());
                                        break;
                                    case "I":
                                        if (backend.loggedInUser.getRewardAmount() > 0) {
                                            backend.withdrawRewardsAmount();
                                            System.out.println("✔️ Rewards " +
                                                    "amount has been added to" +
                                                    " your account! The " +
                                                    "balance will be updated " +
                                                    "soon!");
                                        } else {
                                            System.out.println("⛔ Whoops! You" +
                                                    " " +
                                                    "have either already " +
                                                    "withdrawn your rewards " +
                                                    "amount or not earned any" +
                                                    " rewards yet!");
                                        }
                                        break;
                                    case "J":
                                        if (backend.loggedInUser.isCardIssued()) {
                                            backend.viewCardDetails();
                                        } else {
                                            backend.issueCard();
                                            System.out.println("✔️ Your new " +
                                                    "Card" +
                                                    " has been issued!\n");
                                            backend.viewCardDetails();
                                        }
                                        break;
                                    case "K":
                                        System.out.print("⏎ Enter account " +
                                                "number: ");
                                        long favouriteID = scanner.nextLong();
                                        scanner.nextLine();
                                        backend.addFavouriteID(favouriteID);
                                        break;
                                    case "L":
                                        if (!backend.loggedInUser.getFavouriteIDs().isEmpty()) {
                                            backend.displayFavouriteIDs();
                                            System.out.print("\n⏎ Enter the " +
                                                    "position of your " +
                                                    "Favourite ID you want to" +
                                                    " with: ");
                                            int positionOfID =
                                                    scanner.nextInt();
                                            scanner.nextLine();
                                            if (positionOfID > 0 && positionOfID <= backend.loggedInUser.getFavouriteIDs().size()) {
                                                System.out.print("⏎ Enter " +
                                                        "amount: ");
                                                double amountToPay =
                                                        scanner.nextDouble();
                                                scanner.nextLine();
                                                System.out.print("⏎ Enter " +
                                                        "description: ");
                                                String description =
                                                        scanner.nextLine();
                                                backend.payFavouriteID(positionOfID - 1, amountToPay, description);
                                            } else {
                                                System.out.println();
                                                System.out.println("❌ Invalid" +
                                                        " position");
                                                System.out.println();
                                            }
                                        } else {
                                            System.out.println();
                                            System.out.println("⛔ Oops! You " +
                                                    "don't have any " +
                                                    "favorites!");
                                            System.out.println();
                                        }
                                        break;
                                    case "M":
                                        System.out.print("Enter amount to " +
                                                "add: ");
                                        double balance = scanner.nextDouble();
                                        scanner.nextLine();
                                        backend.addBalance(balance);
                                        System.out.println();
                                        System.out.println("✔️ Successfully " +
                                                "added money to your account!" +
                                                " The money should reflect in" +
                                                " your account soon!");
                                        break;
                                    case "N":
                                        if (backend.loggedInUser.isCardIssued()) {
                                            System.out.print("Enter the " +
                                                    "Account ID of the person" +
                                                    " you" +
                                                    " want to share your card" +
                                                    " with: ");
                                            long accountID = scanner.nextLong();
                                            scanner.nextLine();
                                            System.out.print("Enter how " +
                                                    "many times you want the " +
                                                    "person to use your" +
                                                    " card: ");
                                            int cardUsageLimit =
                                                    scanner.nextInt();
                                            scanner.nextLine();
                                            backend.shareCard(accountID,
                                                    cardUsageLimit);
                                            System.out.println();
                                            System.out.println("✔️ Card " +
                                                    "shared successfully!");
                                        } else {
                                            System.out.println("🚫 Card not " +
                                                    "issued yet!");
                                        }
                                        break;
                                    case "O":
                                        if (!backend.loggedInUser.getSharedCards().isEmpty()) {
                                            backend.displaySharedCards();
                                        } else {
                                            System.out.println("🥹 Oops! No " +
                                                    "cards " +
                                                    "are being shared with " +
                                                    "you!");
                                        }
                                        break;
                                    case "P":
                                        if (!backend.loggedInUser.getSharedCards().isEmpty()) {
                                            backend.displaySharedCards();
                                            System.out.print("\nEnter the " +
                                                    "position of the card you" +
                                                    " want to pay with: ");
                                            int position = scanner.nextInt();
                                            scanner.nextLine();
                                            if (position > 0 && position <= backend.loggedInUser.getSharedCards().size()) {
                                                System.out.print("Enter " +
                                                        "Account ID to pay " +
                                                        "to: ");
                                                long accountID =
                                                        scanner.nextLong();
                                                scanner.nextLine();
                                                System.out.print("Enter " +
                                                        "amount: ");
                                                double cardAmount =
                                                        scanner.nextDouble();
                                                scanner.nextLine();
                                                System.out.print("Enter " +
                                                        "description: ");
                                                String cardDescription =
                                                        scanner.nextLine();
                                                backend.payWithSharedCard(position - 1, cardAmount, accountID, cardDescription);
                                            } else {
                                                System.out.println("Invalid " +
                                                        "position");
                                            }
                                        } else {
                                            System.out.println("🥹 Oops! No " +
                                                    "cards " +
                                                    "are being shared with " +
                                                    "you!");
                                        }
                                        break;
                                    case "R":
                                        System.out.print("Enter Account ID to" +
                                                " pay: ");
                                        long accountIDToPay =
                                                scanner.nextLong();
                                        scanner.nextLine();
                                        System.out.print("Enter amount to " +
                                                "pay:  ");
                                        double amountToPay =
                                                scanner.nextDouble();
                                        scanner.nextLine();
                                        System.out.print("Enter description: ");
                                        String description = scanner.nextLine();
                                        System.out.print("Enter your card " +
                                                "cvv: ");
                                        String cvvToPay = scanner.nextLine();
                                        if (backend.cvvMatches(cvvToPay)) {
                                            backend.payUser(accountIDToPay,
                                                    amountToPay, description);
                                        } else {
                                            System.out.println("Invalid cvv! " +
                                                    "Can't pay!");
                                        }
                                        break;
                                    case "S":
                                        if (!backend.loggedInUser.getPayRequesters().isEmpty()) {
                                            backend.viewMoneyRequests();
                                            System.out.print("Do you want " +
                                                    "to " +
                                                    "pay any request? (Y/N): ");
                                            selection =
                                                    scanner.nextLine().toUpperCase();
                                            if (selection.equals("Y") || selection.equals("YES")) {
                                                System.out.print("Enter " +
                                                        "money " +
                                                        "request position: ");
                                                int requestPosition =
                                                        scanner.nextInt();
                                                scanner.nextLine();
                                                if (requestPosition > 0 && requestPosition <= backend.loggedInUser.getPayRequesters().size()) {
                                                    backend.payMoneyRequest(requestPosition - 1);
                                                } else {
                                                    System.out.println(
                                                            "Invalid position");
                                                }
                                            } else if (selection.equals("N") || selection.equals("NO")) {
                                                System.out.println("Returning" +
                                                        " to main menu");
                                            } else {
                                                System.out.println("Invalid " +
                                                        "selection");
                                            }
                                        } else {
                                            System.out.println("No requests " +
                                                    "yet!");
                                        }
                                        break;
                                    case "T":
                                        String passwordToReset =
                                                scanner.nextLine();
                                        if (backend.passwordMatches(passwordToReset)) {
                                            String newPassword =
                                                    scanner.nextLine();
                                            backend.resetPassword(newPassword);
                                        } else {
                                            System.out.println("Invalid " +
                                                    "password! Please try " +
                                                    "again!");
                                        }
                                        break;
                                    case "U":
                                        System.out.println(backend.loggedInUser.toString());
                                        break;
                                    case "V":
                                        System.out.println("Logging out of " +
                                                "the App!\n");
                                        break;
                                    case "Q":
                                        System.out.println("Exiting the App...");
                                        selection = "Q";
                                        break;
                                    default:
                                        System.out.println();
                                        System.out.println("Invalid Selection");
                                        System.out.println();
                                        break;
                                }
                            } while (!secondSelection.equals("V") && !selection.equals("Q"));
                        } else {
                            System.out.println();
                            System.out.println("Incorrect email or password " +
                                    "or " +
                                    "account not found!");
                            System.out.println();
                        }
                        break;
                    case "B":
                        System.out.println();
                        System.out.print("\uD83D\uDDB9 Enter name: ");
                        String name = scanner.nextLine();
                        System.out.print("\uD83D\uDCE7 Enter email: ");
                        String registerEmail = scanner.nextLine();
                        System.out.print("\uD83D\uDD11 Enter password: ");
                        String registerPassword = scanner.nextLine();
                        if (backend.registerUser(name, registerEmail,
                                registerPassword)) {
                            System.out.println();
                            System.out.println("Registered Successfully! You " +
                                    "can now login!");
                            System.out.println();
                        } else {
                            System.out.println();
                            System.out.println("Registration failed!");
                            System.out.println();
                        }
                        break;
                    case "Q":
                        System.out.println();
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println();
                        System.out.println("Invalid Selection");
                        System.out.println();
                        break;
                }
            } while (!selection.equals("Q"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}