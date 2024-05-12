# Cryptocurrency Wallet Manager

## Technologies used
* Spring Boot
* Hibernate
* Java 11
* Git
* Angular
* SQL db

## Description of the idea
The idea is to create an application providing an opportunity to create and manage a personal cryptocurrency wallet.

Information about cryptocurrencies can be obtained by sending requests to the public [REST API](https://www.coinapi.io/). The information itself can be stored in a database, and since cryptocurrencies are too dynamic, we could put a certain period of time in which the retrieved information is considered up-to-date (for example, ```20-30``` minutes), after which a new request is made until The REST API. This way we will also optimize the requests to the API.

#### Users will have the following functionalities:
- Register – Create a crypto wallet
- Login – Login to the profile
- Deposit - Deposit in the wallet
- Withdraw - Withdraw an amount from the wallet
- List offerings - Overview of available cryptocurrencies that are provided by the application
- Buy - Purchase of a certain amount of a given cryptocurrency
- Sell - Selling a certain amount of pre-purchased cryptocurrency
- Wallet summary - Overview of all active investments - Information is provided on the current profit/loss of all active investments
- Wallet history - Overview of account history - Provides comprehensive information on all purchases and sales of different currencies. Profit and loss is calculated for each investment, as well as total profit and loss from all investments made
- Logout - Exit

The idea is to store all information about users' crypto wallets in a database.

## REST endpoints demonstration
![REST endpoints](./img/REST%20structure.jpg)

## DB structure: diagram
![DB structure](./img/DB%20structure.jpg)

### DB structure: explanation
> [!IMPORTANT]
> In order to follow the rules of Blockchain protocol(where all crypto are "located"), we track every single transaction that will be stored in DB for later statistics.

> [!NOTE]
> In the table ```transaction``` column ```type``` can have only 2 values: ```bought``` and ```sold```. Also column ```profit``` can be null as soon as it only makes sense when we are trying to sold crypto.
