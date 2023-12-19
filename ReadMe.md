# ACCELEPAY: The Next Generation Payment Gateway

## Introduction
The goal with the payment platform is to provide unified endpoints for merchants and personal users to make payments
and view the payments made on the platform. This application is built with the following technologies:
1. Spring Boot
2. Spring Data JPA
3. Lombok
4. H2 Database
5. Java 21

## The Thought Process
The design of the application is based on the following assumptions:
1. The application is a payment processor for merchants and personal users and as such it does not integrate with other payment platforms like PayStack(Integrations used are for demonstration purposes)
2. The application only supports card payments and assumes the currency being used is the Nigerian Naira
3. The application is a REST API and as such does not have a UI
4. The application resolves card details to their affiliated account numbers and then proceeds to make bank to bank transfers
5. Certain aspects of the application are randomized to give our error handling systems a workout
6. Request and Response are abstracted to allow one endpoint service multiple type of related requests

## The Endpoints
The application has the following endpoints:
### POST /api/v1/payment/makePayment:
For merchants the request body is as follows:
```json
{
  "apiKey":"01234ADEDC8890A",
  "amountToPay":50.00,
  "paymentType":{
    "cardNumber":"1234567890987654",
    "cardExpiry":"08/26",
    "cardCcv":"234",
    "cardPin":"1234",
    "cardHoldersName":"Juliet Ambodaka"
  },
  "paymentDescription":"Dodo Pizza Order No.6 for Customer 116",
  "alternateAccountId": null,
  "requestPlatform":"ANDROID"
}
```
When a merchant makes a payment we assume the following:
1. The merchant has a primary account with the payment platform and if they want to make user of secondary account, they attach the ID of said account in the request
this is indicated by the `alternateAccountId` field
2. The merchant has a valid API key which is used to authenticate the request
3. The merchant is providing the card details of a customer who has given them permission to do so and wants to make a payment on their behalf. Example: a customer entering their card details on the DSTV app to pay a bill

With these assumptions in mind, we resolve the card details to the account number of the customer and then proceed to make a bank to bank transfer from the customer's account to the merchant's account.
If there are no errors the response body is as follows:
```json
{
  "error": null,
  "paymentReference": "7344-1759-20231120044",
  "paymentStatus": "SUCCESSFUL",
  "paymentAmount": 50.0
}
```

For personal users the request body is as follows:
```json
{
  "apiKey":"01234ADEDC88909",
  "amountToPay":50.00,
  "paymentType":{
    "cardNumber":"1234567890987654",
    "cardExpiry":"08/26",
    "cardCcv":"234",
    "cardPin":"1234",
    "cardHoldersName":"Juliet Ambodaka"
  },
  "paymentDescription":"Dodo Pizza Order No.6 for Customer 116",
  "destinationAccountNumber": "0981235673",
  "destinationBank":"GTB",
  "destinationAccountName":"George Agboghoroma",
  "requestPlatform":"ANDROID"
}
```
When a personal user makes a payment we assume the following:
1. The user has a valid api key which is used to authenticate the request
2. the user is providing the details of the card they want to use to make a payment
3. The user is providing the account number of the person they want to make a payment to, we could also have this mapped to merchants on our platform where in the user would provide just the merchant ID, and we would do the rest

With these assumptions in mind we can proceed to make a bank to bank transfer after resolving the user's card details to their account number.

###
