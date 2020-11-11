CREATE TABLE CONFIGURATION (
  name VARCHAR(250) PRIMARY KEY,
  value VARCHAR(250) NOT NULL
);

CREATE TABLE BANK_LINK (
  BANK_IP VARCHAR(45) PRIMARY KEY,
  BANK_CODE VARCHAR(2) NOT NULL
);

CREATE TABLE CREDIT_TRANSACTION (
  amount INT NOT NULL,
  current_state VARCHAR(20) NOT NULL CHECK (current_state <> ''),
  informed BOOL NOT NULL DEFAULT TRUE,
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