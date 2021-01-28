# Weird Transaction System

These repositories are intented to be a place where I can practice my development skills as well as my investigation skills when I need to make a decision between two approaches and I don't know how to select them. Futhermore, I'll try always to apply the TDD approach. Here I intend to add the back-end and in the future a front-end. In latter time I might add CI/CD capbility in order to enchance my DevOps skills

The name Weird Transaction System comes from the fact that some "business decisions" are far from what an Entity would like to have in a transaction system, but simplifies a lot the need to develop other services. One such example is the fact that a Reciever Entity needs to perform a REST call in order to check payments they are receiving instead of being informed by the WTS directly

## Use cases

For all the use cases, it is important to note that WTS interfaces with Sender and Receiver Entities, but it is not in the scope of this project to interact with Users nor define how the Entities themselfes interact with their Users

Actors:

- **User**: a person or organization that is a registred in an *Entity* that in turn performs transactions with *WTS*
- **Entity**: an organization that is allowed to perform transactions with other *Entities* via *WTS* on behalf of their *Users* or on behalf of itself
- **WTS**: Weird Transaction System, what this code is all about. The system that allows *Entities* to perform transactions on behalf of their Users
- **Sender Entity**: an *Entity* that for a particular transaction is the provider of amounts on behalf of an *User* or itself
- **Receiver Entity**: an *Entity* that for a particular transaction is the receiver of amounts on behalf of an *User* or itself

Other terms:

- **Amount**: it is a number representing a quantity that is transfered between actors. It is a generic term. Concrete terms could be points (for a Loyalty Points levaring *WTS*) or dollars (for a financial institution levaring *WTS*)
- **Transaction**: a movement of *Amounts* between entities via *WTS*
- **Confirmation**: a confirmation signals that a transaction has been accepted by an *Entity*

### User A wants to transfer an amount to User B - Positive scenario wherein Receiver Entity confirms transaction

**Notice**: currently this is still in development. Some domain terms will be different between what is described in here due to the initial target being a banking application but latter I realized that a generic transactions system would be nicer. Code will be update in a future commit to reflect the correct usage of terms

[The sequence diagram can be found here](https://raw.githubusercontent.com/mgkramar/Weird-Transaction-System/master/readme/UseCase1.svg)

User A wants to transfer an amount to User B. User A does so through Entity A:

- Entity A performs a POST call to WTS informing the transaction details such as Receiver account / Sender account / Amount and transaction reference
- WTS validates the content of the transaction making sure the transaction sintax is correct. Examples being: check that the Amount is numeric; check that the transaction reference is not empty
- WTS validates the content of the transaction making sure that the values are correct. Examples being: check that the Amount is not bigger than the maximum value configured for two entites to exchange; check that the transaction reference is not duplicated. Notice that User A and User B are not in the scope of WTS, thus WTS cannot verify whether User B in Entity B exists or whether User A have sufficient amounts to perform the transaction
- WTS registers the transaction information updating balances for Entities A and B
- WTS informs Sender Entity that the transaction is all correct

At some point latter:

- Receiver Entity performs a GET call filtering for INITIATED transactions with INWARD direction (new transactions destinated to Receiver Entity)
- WTS returns all the transactions matching the criteria
- Receiver Entity validates the content of the transaction making sure the transaction sintax is correct. Examples being: check that the Amount is numeric; check that the Receiver Name is not empty
- Receiver Entity validates the content of the transaction making sure that the values are correct. Examples being: check that the Receiver Account Number is indeed of a User B that exists in its DB; check that the Amount won´t make User B´s account to overflow
- The Receiver Entity informs to User B that they received a transaction from User A
- Optionally the Receiver Entity might or not require User B to accept the transaction, this is out of WTS´s scope
- The Receiver Entity confirms which transactions it will accept including the transaction from User B
- WTS registers the transaction information

At some point latter:

- Sender Entity performs a GET call filtering for CONFIRMED transactions with OUTWARD direction (transactions made by Sender Entity that were confirmed by their counterpart)
- WTS returns all transactions matching the criteria. It is important to notice that after a transaction is informed to an Entity it will be marked as INFORMED in the system and won´t show in subsequent calls for CONFIRMED / OUTWARD transactions unless explicitly required for


## TODO

- Dockerize database
- Dockerize application
- Add this to github pipeline
- Create an AWS demo environment
- Document build procces (how to build with gradle, how to deploy it locally, how to deploy it to AWS)
