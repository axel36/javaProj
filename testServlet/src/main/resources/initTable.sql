CREATE TABLE logTable
(
  request_id        BIGINT PRIMARY KEY IDENTITY,
  request_time      TIMESTAMP,
  sur_name          VARCHAR(20),
  first_name        VARCHAR(20),
  middle_name       VARCHAR(20),
  date_of_birth     DATE,
  phone_number      VARCHAR(15),
  response_time     TIMESTAMP,
  response_msg      VARCHAR(100),
  error_in_response BIT
);