# Cryptocurrency Wallet Manager

## Technologies used
* Spring Boot
* Hibernate
* Java 17
* Git
* Angular
* SQL db

## Description of the idea
The idea is to create an application providing an opportunity to create and manage a personal cryptocurrency wallet.

Information about cryptocurrencies can be obtained by sending requests to the public [REST API](https://www.coinapi.io/). The information itself can be stored in a database, and since cryptocurrencies are too dynamic, we could put a certain period of time in which the retrieved information is considered up-to-date (for example ```5``` minutes), after which a new request is made to the REST API. This way we will also optimize the requests to the API.

#### Users will have the following functionalities:
* User account
  * Register – Create a crypto wallet
  * Login – Login to the profile
  * Update - Update user account credentials
  * Logout - Exit from the account (end session)
* Wallet interactions
  * Deposit - Deposit in the wallet
  * Withdraw - Withdraw an amount from the wallet
  * List offerings - Overview of available cryptocurrencies that are provided by the application
  * Buy - Purchase of a certain amount of a given cryptocurrency
  * Sell - Selling a certain amount of pre-purchased cryptocurrency
  * Wallet summary - Overview of all active investments - Information is provided on the current profit/loss of all active investments
  * Wallet overall summary - Overview of account history - Provides comprehensive information on all purchases and sales of different currencies. Profit and loss is calculated for each investment, as well as   total profit and loss from all investments made
  * Filtered transactions according to date - Gets all/bought/sold transactions after/before/beteween date(s)
  * Crypto transaction history - Gets all(bought + sold) his transactions with the specified cryptocurrency to concrete user

The idea is to store all information about users' crypto wallets in a database.

## REST endpoints demonstration
![REST endpoints](./img/REST%20endpoints.png)

## DB structure: diagram
![DB structure](./img/DB%20diagram.png)

### DB structure: explanation
> [!IMPORTANT]
> In order to follow the rules of Blockchain protocol(where all crypto are "located"), we track every single transaction that will be stored in DB for later statistics.

> [!NOTE]
> In the table ```transaction``` column ```type``` can have only 2 values: ```bought``` and ```sold```. Also column ```selling_profit``` can be null as soon as it only makes sense when we are trying to sold crypto.

#### What will be stored in ```crypto``` table ?
* This table will hold cryptocurrencies that were bought by at least 1 user. Info about cryptocurrencies will be provided by ```CoinAPI```

### Calculating profit for cryptocurrencies
#### Explanation/influence on DB structure
* We can maintain field ```average_asset_buying_price```(with initial value ```0```) in ```wallet``` table. With its help we will calculate profit from selling any crypto at any time with minimum DB operations.   
#### Algorithm for calculating profit
* ```selling_profit``` = "selling_amount" * ("selling_price" - ```average_asset_buying_price```)
#### Algorithm for calculating ```average_asset_buying_price```
* "average_asset_buying_price" = ("bought_price_of_new_crypto" * "bought_amount" + (```average_asset_buying_price``` * "existed_amount")) / ("existed_amount" + "bought_amount")
#### Example of calculating ```profit``` and ```average_asset_buying_price```
1) We buy 2 BTC with price 30000 each.
> "average_asset_buying_price" = (30000 * 2 + (0 * 0)) / 2 = 30000
2) We buy 1 BTC later with price 60000.
> "average_asset_buying_price" = (60000 * 1 + (30000 * 2)) / 3 = 40000
3) We sell 1 BTC for 45000.
> So there after selling 1 BTC for 45000 our profit will be: 45000 - "average_asset_buying_price"((30000*2 + 60000) / 3) = 5000; so after selling 1 BTC we still have 2 BTC and "average_asset_buying_price" won't change.
4) Then we buy 2 BTC for 50000 each.
> "average_asset_buying_price" become: (50000 * 2 + ("average_asset_buying_price" * 2)) / 4 = 45000

> [!IMPORTANT] 
> ```average_asset_buying_price``` can be the column in ```wallet``` table and to be updated at every buy transaction for this asset. By this way we will know at any moment what was the actual buying price(average) for the concrete crypto by the concrete user and selling price will be fetched from the CoinAPI.
