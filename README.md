# 💳 PlutoPay - Java Console-Based Payment App

PlutoPay is a console-based Java application that simulates a secure peer-to-peer payment platform. It supports user registration, login, money transfers, card issuance and sharing, favorite contacts, rewards tracking, and transaction management — all via a terminal interface.

---

## 🚀 Features

- 🔐 **User Authentication**: Register and securely log in with email and password.
- 💸 **Money Transfers**: Pay other users by account ID with description and optional PIN verification.
- 🙏 **Request Money**: Send money requests to other users.
- 💳 **Card Issuance**: Issue virtual cards with Luhn-validated numbers, expiry date, and CVV.
- 🔗 **Card Sharing**: Share your card with trusted users with usage limits.
- ❤️ **Favorite Contacts**: Save frequent payees for quicker access.
- 🪙 **Rewards System**: Earn rewards on payments, withdraw to balance.
- 👁 **View Transactions**: Complete history of past transactions.
- 📄 **JSON Data Persistence**: User data is stored in `users.json`; no serialization is used.
- 📌 **PIN Verification**: Set and validate PINs for secure payments.
- 🔒 **Hashed Credentials**: Passwords and PINs are stored as salted PBKDF2-SHA256 hashes, never as plain text. Older plain-text accounts are upgraded automatically on their next login.
- 🧵 **Thread-Safe Transfers**: Every payment goes through one transfer method that locks both accounts in account-ID order, so concurrent payments never lose or create money and never deadlock. Negative, zero, and NaN amounts are rejected on every payment path.

---

## 🗂 File Structure

| File                  | Description                                          |
|-----------------------|------------------------------------------------------|
| `Main.java`           | Entry point with menu navigation logic              |
| `Backend.java`        | Business logic and operations                        |
| `User.java`           | Represents user data and account info                |
| `Transaction.java`    | Models a payment or request transaction              |
| `FavouriteID.java`    | Represents saved favorite user IDs                   |
| `MoneyRequests.java`  | Represents incoming money requests                   |
| `SharedCard.java`     | Represents a shared card between users               |
| `users.json`          | JSON storage for all user accounts and transactions |
| `latestAccountID.txt` | Tracks the latest account ID for uniqueness         |

---

## 📦 Prerequisites

- Java 11 or above
- JSON.simple library (`json-simple-1.1.1.jar`)

---

## 🧪 How to Run

1. Compile:
   ```bash
   javac -cp .;json-simple-1.1.1.jar Main.java

# 📌 Note: Ensure users.json and latestAccountID.txt are in the project root.

---

## ✅ Checks

```bash
./scripts/check.sh
```

Compiles the app and runs 13 checks, including a stress test of 200,000 transfers across 8 threads that verifies the total money in the system never changes and no account goes negative. GitHub Actions runs the same script on every push.

