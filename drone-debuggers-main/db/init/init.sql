CREATE DATABASE IF NOT EXISTS air_assist;
USE air_assist;
drop table if exists UserRole;
drop table if exists Hero;
drop table if exists `User`;
drop table if exists Role;

CREATE OR REPLACE TABLE document_type (

                                          document_type_id INT AUTO_INCREMENT PRIMARY KEY,
                                          document_type_name VARCHAR(255) NOT NULL
);




CREATE OR REPLACE TABLE role (
                                 role_id INT AUTO_INCREMENT PRIMARY KEY,
                                 role_name VARCHAR(255) NOT NULL
);


CREATE OR REPLACE TABLE `user` (
                                 user_id INT AUTO_INCREMENT PRIMARY KEY,
                                 first_name VARCHAR(255) NOT NULL,
                                 last_name VARCHAR(255) NOT NULL,
                                 email VARCHAR(255) UNIQUE NOT NULL,
                                 password VARCHAR(255) NOT NULL,
                                 role INT,
                                 first_login BOOLEAN,
                                 account_blocked BOOLEAN,
                                 number_of_failed_attempts INT,
                                 FOREIGN KEY (role) REFERENCES role(role_id)
);

CREATE OR REPLACE TABLE status (
                                   status_id INT AUTO_INCREMENT PRIMARY KEY,
                                   status_name VARCHAR(255) NOT NULL
);

CREATE OR REPLACE TABLE airport (
                                    airport_id INT AUTO_INCREMENT PRIMARY KEY,
                                    airport_name VARCHAR(255) NOT NULL,
                                    airport_code VARCHAR(3) NOT NULL,
                                    deleted BOOLEAN DEFAULT FALSE
);

CREATE OR REPLACE TABLE reservation (
                                        reservation_id INT AUTO_INCREMENT PRIMARY KEY,
                                        reservation_number VARCHAR(255) NOT NULL,
                                        departing_airport INT NOT NULL,
                                        destination_airport INT NOT NULL,
                                        FOREIGN KEY (departing_airport) REFERENCES airport(airport_id),
                                        FOREIGN KEY (destination_airport) REFERENCES airport(airport_id)
);

CREATE OR REPLACE TABLE flight_info (
                                        flight_info_id INT AUTO_INCREMENT PRIMARY KEY,
                                        reservation_id INT NOT NULL,
                                        airline VARCHAR(255) NOT NULL,
                                        planned_departure_date DATE NOT NULL,
                                        planned_arrival_date DATE NOT NULL,
                                        flight_nr VARCHAR(255) NOT NULL,
                                        departing_airport INT NOT NULL,
                                        destination_airport INT NOT NULL,
                                        problem_flight BOOLEAN NOT NULL,
                                        FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id),
                                        FOREIGN KEY (departing_airport) REFERENCES airport(airport_id),
                                        FOREIGN KEY (destination_airport) REFERENCES airport(airport_id)
);

CREATE OR REPLACE TABLE passenger_details(
                                             passenger_details_id INT AUTO_INCREMENT PRIMARY KEY,
                                             date_of_birth DATE NOT NULL,
                                             phone VARCHAR(255) NOT NULL,
                                             address VARCHAR(255) NOT NULL,
                                             postal_code INT NOT NULL,
                                             email VARCHAR(255) NOT NULL,
                                             first_name VARCHAR(255) NOT NULL,
                                             last_name VARCHAR(255) NOT NULL
);

CREATE OR REPLACE TABLE cancellation_type(
                                             cancellation_type_id INT AUTO_INCREMENT PRIMARY KEY,
                                             cancellation_type_description VARCHAR(255) NOT NULL
);

CREATE OR REPLACE TABLE disruption_option(
                                             disruption_option_id INT AUTO_INCREMENT PRIMARY KEY,
                                             disruption_option_description VARCHAR(255) NOT NULL
);

CREATE OR REPLACE TABLE airline_motive(
                                          airline_motive_id INT AUTO_INCREMENT PRIMARY KEY,
                                          airline_motive_type_description VARCHAR(255) NOT NULL
);


CREATE OR REPLACE TABLE disruption (
                                       disruption_id INT AUTO_INCREMENT PRIMARY KEY,
                                       cancellation_type INT NOT NULL,
                                       disruption_option INT,
                                       airline_motive INT,
                                       incident_description VARCHAR(1024),
                                       FOREIGN KEY (cancellation_type) REFERENCES cancellation_type(cancellation_type_id),
                                       FOREIGN KEY (disruption_option) REFERENCES disruption_option(disruption_option_id),
                                       FOREIGN KEY (airline_motive) REFERENCES airline_motive(airline_motive_id)
);

CREATE OR REPLACE TABLE compensation_case(
                                  case_id INT AUTO_INCREMENT PRIMARY KEY,
                                  system_case_id VARCHAR(255) NOT NULL,
                                  date_created TIMESTAMP NOT NULL,
                                  status INT NOT NULL,
                                  colleague INT,
                                  reservation_id INT NOT NULL,
                                  passenger_details_id INT,
                                  disruption_id INT NOT NULL,
                                  passenger INT NOT NULL,
                                  compensation INT NOT NULL,
                                  FOREIGN KEY (status) REFERENCES status(status_id),
                                  FOREIGN KEY (colleague) REFERENCES user(user_id),
                                  FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id),
                                  FOREIGN KEY (passenger_details_id) REFERENCES passenger_details(passenger_details_id),
                                  FOREIGN KEY (disruption_id) REFERENCES disruption(disruption_id),
                                  FOREIGN KEY (passenger) REFERENCES user(user_id)
);

CREATE OR REPLACE TABLE comment(
                                   comment_id INT AUTO_INCREMENT PRIMARY KEY,
                                   case_id INT NOT NULL,
                                   user_id INT NOT NULL,
                                   comment_text VARCHAR(1024) NOT NULL,
                                   timestamp TIMESTAMP NOT NULL,
                                   FOREIGN KEY (case_id) REFERENCES compensation_case(case_id),
                                   FOREIGN KEY (user_id) REFERENCES user(user_id)
);
CREATE OR REPLACE TABLE generated_pdf (
                                          document_id INT AUTO_INCREMENT PRIMARY KEY,
                                          document_name VARCHAR(255) NOT NULL,
                                          case_id INT,
                                          data MEDIUMBLOB,
                                          upload_timestamp TIMESTAMP,
                                          FOREIGN KEY (case_id) REFERENCES compensation_case(case_id)
);

CREATE OR REPLACE TABLE document (
                                     document_id INT AUTO_INCREMENT PRIMARY KEY,
                                     case_id INT,
                                     document_name VARCHAR(255) NOT NULL,
                                     document_type INT,
                                     document_data_type VARCHAR(255) NOT NULL,
                                     data MEDIUMBLOB,
                                     upload_timestamp TIMESTAMP,
                                     FOREIGN KEY (document_type) REFERENCES document_type(document_type_id),
                                     FOREIGN KEY (case_id) REFERENCES compensation_case(case_id)
);


