DROP TABLE IF EXISTS configuration;

CREATE TABLE configuration
(
    name VARCHAR(250) PRIMARY KEY,
    value VARCHAR(250) NOT NULL
);

INSERT INTO configuration
    (name, value)
VALUES
    ('MAX_AMOUNT', '50000'),
    ('MIN_AMOUNT', '0');

DROP TABLE IF EXISTS BANK_LINK;

CREATE TABLE BANK_LINK
(
    BANK_IP VARCHAR(45) PRIMARY KEY,
    BANK_CODE VARCHAR(2) NOT NULL
);

INSERT INTO BANK_LINK
    (BANK_IP, BANK_CODE)
VALUES
    ('0:0:0:0:0:0:0:1', '01'),
    ('127.0.0.1', '01');

DROP TABLE IF EXISTS CREDIT_TRANSACTION;

CREATE TABLE CREDIT_TRANSACTION
(
    amount INT NOT NULL,
    transaction_reference VARCHAR(10) NOT NULL CHECK (transaction_reference <> ''),
    previous_transaction_reference VARCHAR(10) CHECK (previous_transaction_reference <> ''),
    receiver_First_Name VARCHAR(40) NOT NULL CHECK (receiver_First_Name <> ''),
    receiver_Last_Name VARCHAR(40) NOT NULL CHECK (receiver_Last_Name <> ''),
    receiver_Document_Number VARCHAR(10) NOT NULL CHECK (receiver_Document_Number <> ''),
    receiver_Account_Number VARCHAR(10) NOT NULL CHECK (receiver_Account_Number <> ''),
    receiver_Bank_Code VARCHAR(2) CHECK (receiver_Bank_Code <> ''),
    sender_first_name VARCHAR(40) NOT NULL CHECK (sender_first_name <> ''),
    sender_Last_Name VARCHAR(40) NOT NULL CHECK (sender_Last_Name <> ''),
    sender_Document_Number VARCHAR(10) NOT NULL CHECK (sender_Document_Number <> ''),
    sender_Account_Number VARCHAR(10) NOT NULL CHECK (sender_Account_Number <> ''),
    sender_Bank_Code VARCHAR(2) CHECK (sender_Bank_Code <> '')
);

INSERT INTO CREDIT_TRANSACTION
    (amount, transaction_reference, previous_transaction_reference,
    receiver_First_Name, receiver_Last_Name, receiver_Document_Number, receiver_Account_Number,
    receiver_Bank_Code, sender_first_name, sender_Last_Name,
    sender_Document_Number,sender_Account_Number,sender_Bank_Code)
VALUES
    ('50', '0123456789', '0123456789', 'John', 'Johnsons', 'WWW123456', '123654123', '35',
        'James', 'Jameson', '246813579', '000654123', '01'),

    ('50', '9999999999', '0123456789', 'John', 'Johnsons', 'WWW123456', '123654123', '35',
        'James', 'Jameson', '246813579', '000654123', '01'),

    ('50', '0000000000', null, 'Receiver', 'Receiverson', 'WWW123456', '123654123', '01',
        'Sender', 'Senderson', '246813579', '000654123', '35');
    