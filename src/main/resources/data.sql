INSERT INTO
  configuration (name, value)
VALUES
  ('MAX_AMOUNT', '50000'),
  ('MIN_AMOUNT', '0');

INSERT INTO
  BANK_LINK (BANK_IP, BANK_CODE)
VALUES
  ('0:0:0:0:0:0:0:1', '01'),
  ('127.0.0.1', '01');


INSERT INTO
  CREDIT_TRANSACTION (
    amount,
    transaction_reference,
    previous_transaction_reference,
    receiver_First_Name,
    receiver_Last_Name,
    receiver_Document_Number,
    receiver_Account_Number,
    receiver_Bank_Code,
    sender_first_name,
    sender_Last_Name,
    sender_Document_Number,
    sender_Account_Number,
    sender_Bank_Code,
    current_state,
    informed
  )
VALUES
  (
    '50',
    '0123456789',
    '0123456789',
    'John',
    'Johnsons',
    'WWW123456',
    '123654123',
    '35',
    'James',
    'Jameson',
    '246813579',
    '000654123',
    '01',
    'INITIATED',
    0
  ),
  (
    '50',
    '9999999999',
    '0123456789',
    'John',
    'Johnsons',
    'WWW123456',
    '123654123',
    '35',
    'James',
    'Jameson',
    '246813579',
    '000654123',
    '01',
    'INITIATED',
    0
  ),
  (
    '50',
    '0000000000',
    null,
    'Receiver',
    'Receiverson',
    'WWW123456',
    '123654123',
    '01',
    'Sender',
    'Senderson',
    '246813579',
    '000654123',
    '35',
    'INITIATED',
    0
  ),
  (
    '50',
    '0000000001',
    null,
    'Receiver',
    'Receiverson',
    'WWW123456',
    '123654123',
    '01',
    'Sender',
    'Senderson',
    '246813579',
    '000654123',
    '35',
    'CONFIRMED',
    0
  );