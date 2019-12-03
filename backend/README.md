# API

Project : **Cash manager**
> Backend API using JAVA

## Summary

- [Usage](#Usage)
- [Protocol Description](#Protocol-Description)
  - [Authentication](#Authentication)
  - [Available Commands](#Available-Commands)
    - [DescribeAccount](#DescribeAccount-Command)
    - [StartTransaction Command](#StartTransaction-Command)
    - [ValidateAndProcessTransaction Command](#ValidateAndProcessTransaction-Command)
  - [Error Codes](#Error-Codes)
  - [Real Life Example](#Real-Life-Example)
- [Configuring the server](#Configuring-the-server)
- [Contributing to the server](#Contributing-to-the-server)

# Usage

### build:
```
mvn compile
```
### test:
```
mvn test` # code coverage in ./target/site/jacoco/
```
### package:
```
mvn package # output ./target/cashmanager-0.0.1-jar-with-dependencies.jar
```
### exec (devlocal):
```
mvn exec:java -Dcashmanager.config.localfile=$(pwd)/CashManagerConfig.json
```
### exec (prod):
```
java -Dcashmanager.config.localfile=/CashManagerConfig.json -jar ./target/cashmanager-0.0.1-jar-with-dependencies.jar
```
### debug tests:
```
mvn -Dmaven.surefire.debug test
```
### debug main:
```
mvnDebug exec:java -Dcashmanager.config.localfile=$(pwd)/CashManagerConfig.json
```

# Protocol Description

The client and the server interact on a socket stream with a default port of 8080. The protocol used between the client and the server is based on [Json RPC](https://www.jsonrpc.org/specification).

In the next examples each line starting with '>' is a line send by the client and each line with '<' is a line received by the client.

## Authentication

You need to be authenticated to perform others requests to the server. Here is an example of an authentication request:
```
> {"jsonrpc": "2.0", "method": "Login", "params": { "id": "user1", "password": "p1" }, "id": 1}
< {"jsonrpc":"2.0","id":1,"result":{"success":true}}
```

## Available Commands

### DescribeAccount Command
Describe an account.

- method: `DescribeAccount`
- params:
  - `accountId` (`string`)
- example:
```
> {"jsonrpc": "2.0", "method": "DescribeAccount", "params": { "accountId": "acc1" }, "id": 1}
< {"jsonrpc":"2.0","id":1,"result":{"id":"acc1","balance":12.04}}
```

### StartTransaction Command
Start a transaction. Take care the transaction is not done until the result of [ValidateAndProcessTransaction](#ValidateAndProcessTransaction-Command).

- method: `StartTransaction`
- params:
  - `CreditorAccountId` (`string`)
  - `Amount` (`number`)
- example:
```
> {"jsonrpc": "2.0", "method": "StartTransaction", "params": { "CreditorAccountId": "acc2", "Amount": 30.00 }, "id": 1}
< {"jsonrpc":"2.0","id":1,"result":{"creditor":"acc2","debtor":null,"amount":30.0}}
```

### ValidateAndProcessTransaction Command
Validate and process a transaction. The transaction need to be started before with [StartTransaction](#StartTransaction-Command)

- method: `ValidateAndProcessTransaction`
- params:
  - `DebtorAccountId` (`string`)
- example:
```
> {"jsonrpc": "2.0", "method": "ValidateAndProcessTransaction", "params": { "DebtorAccountId": "acc1" }, "id": 1}
< {"jsonrpc":"2.0","id":1,"result":{"success":true}}
```

## Error Codes
The error is acompagned with a message explaining the error. But here are the differents error code and the associated description.

| Code   | Description       |
| ------ | ----------------- |
| -32700 | Parse error       |
| -32600 | Invalid Request   |
| -32601 | Method not found  |
| -32602 | Invalid params    |
| -32603 | Internal error    |
| -32021 | Service error     |
| -32022 | Not authenticated |

## Real Life Example
Here is a real life example, from the start of the connection to the end of the transaction.
```
> {"jsonrpc": "2.0", "method": "Login", "params": { "id": "user1", "password": "p1" }, "id": 1}
< {"jsonrpc":"2.0","id":1,"result":{"success":true}}
> {"jsonrpc": "2.0", "method": "DescribeAccount", "params": { "accountId": "acc2" }, "id": 2}
< {"jsonrpc":"2.0","id":2,"result":{"id":"acc2","balance":30.0}}
> {"jsonrpc": "2.0", "method": "StartTransaction", "params": { "CreditorAccountId": "acc2", "Amount": 3.00 }, "id": 3}
< {"jsonrpc":"2.0","id":3,"result":{"creditor":"acc2","debtor":null,"amount":3.0}}
> {"jsonrpc": "2.0", "method": "ValidateAndProcessTransaction", "params": { "DebtorAccountId": "acc1" }, "id": 4}
< {"jsonrpc":"2.0","id":4,"result":{"success":true}}
> {"jsonrpc": "2.0", "method": "DescribeAccount", "params": { "accountId": "acc2" }, "id": 5}
< {"jsonrpc":"2.0","id":5,"result":{"id":"acc2","balance":33.0}}
```

# Configuring the server
The server can be configured by the json file provided by the `cashmanager.config.localfile` java property.
Here is an example:
```
{
    "accounts": [
        { "id": "acc1", "balance": 12.04 },
        { "id": "acc2", "balance": 30.00 }
    ],
    "users": [
        { "id": "user1", "password": "p1" }
    ],
    "preferences": {
        "server.port": "8080",
        "transactions.maxdelay": "5",
        "transactions.maxretry": "2"
    }
}
```

# Contributing to the server

The server use the dependency injection design pattern with a custom IOC container. The IOC container is the class `fr.cashmanager.impl.ioc.ServicesContainer`. The production service container is initialized in the `CashManager` Class and in the method `initContainer()`.
The services can be singleton or factory (by extending `ServiceFactory` class).

The Json Rpc service is based on middlewares. The middlewares handle the connection in chain. The middleware chain used by the server is ErrorMiddleware > AuthenticationMiddleware > CommandMiddleware.
 - the ErrorMiddleware handle the JsonRPCException throwed by the other middleware and format it in a Json RPC compatible message.
 - the AuthenticationMiddleware handle the login command and if the user is logged in the user can access to the next middleware in the chain.
 - the CommandMiddleware handle the JsonRpc request and launch the associated command

The Commands and the middlewares are registered in `CashManager` Class (method `initCommandsAndMiddlewares()`).
