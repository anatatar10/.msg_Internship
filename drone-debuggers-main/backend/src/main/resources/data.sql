use air_assist;
delete from document where document_id>0;
delete from comment where comment_id>0;
delete from generated_pdf where document_id>0;
delete from compensation_case where case_id>0;
delete from disruption where disruption_id>0;
delete from airline_motive where airline_motive_id>0;
delete from disruption_option where disruption_option_id>0;
delete from cancellation_type where cancellation_type_id>0;
delete from passenger_details where passenger_details_id>0;
delete from flight_info where flight_info_id>0;
delete from reservation where reservation_id>0;
delete from airport where airport_id>0;

delete from user where user_id>0;
delete from status where status_id>0;
delete from role where role_id>0;
delete from document_type where document_type_id>0;

INSERT INTO document_type (document_type_name)
VALUES
    ('Boarding Pass'),
    ('ID'),
    ('Passport'),
    ('IdPassport'),
    ('Other');

INSERT INTO role (role_name)
VALUES
    ('Passenger'),
    ('Colleague'),
    ('System Admin'),
    ('Unknown');

INSERT INTO status (status_name)
VALUES
    ('NEW'),
    ('VALID'),
    ('ASSIGNED'),
    ('INVALID'),
    ('INCOMPLETE');

INSERT INTO airport (airport_code, airport_name, deleted)
VALUES
    ('LHR', 'London Heathrow', false),
    ('CDG', 'Paris Charles de Gaulle', false),
    ('FRA', 'Frankfurt', false),
    ('AMS', 'Amsterdam Schiphol', false),
    ('BCN', 'Barcelona El Prat', false),
    ('MAD', 'Madrid Barajas', false),
    ('CLJ', 'Cluj-Napoca International Airport', false);

-- User table with modified roles and user names
INSERT INTO user (first_name, last_name, email, password, role, first_login, account_blocked, number_of_failed_attempts)
VALUES
    ('Unknown', 'Unknown', 'unknown', 'unknown', (SELECT role_id FROM role WHERE role_name='Unknown'), FALSE, FALSE, 0),
    ('Jon', 'Snow', 'jon.snow@thenorth.got', '$2a$10$tuclt2Zq9eI2Aw.dgcBaOenG4VwwVoJKdfW6h2HhfpAxNsH64PDee', (SELECT role_id FROM role WHERE role_name='Passenger'), FALSE, FALSE, 0),
    ('Arya', 'Stark', 'arya.stark@facelessmen.got', '$2a$10$56hUHDh3Lq41Esq3KOt5YuDhx9zk7mUbPLG.lXbmDxWiwCtpT6Yyu', (SELECT role_id FROM role WHERE role_name='Passenger'), FALSE, FALSE, 0),
    ('Tyrion', 'Lannister', 'tyrion.lannister@casterlyrock.got', '$2a$10$UXyeAu5uEPj1I4pVniGkVu3trvpQG4v4j/m9Y53I3OoznQn9EO0Ga', (SELECT role_id FROM role WHERE role_name='System Admin'), TRUE, FALSE, 1),
    ('Daenerys', 'Targaryen', 'daenerys.targaryen@dragonsden.got', '$2a$10$faHCU07toW5aUuyx15dvHOGLd95dTz8GEA5iTt7EIZm5m7BLcKhY.', (SELECT role_id FROM role WHERE role_name='Colleague'), FALSE, FALSE, 2),
    ('Varys', 'The Spider', 'varys@kingslanding.got', '$2a$10$faHCU07toW5aUuyx15dvHOGLd95dTz8GEA5iTt7EIZm5m7BLcKhY', (SELECT role_id FROM role WHERE role_name='Colleague'), FALSE, FALSE, 0);



-- Reservation table remains unchanged
INSERT INTO reservation (reservation_number, departing_airport, destination_airport)
VALUES
    ('ABC123', (SELECT airport_id FROM airport WHERE airport_code='LHR'), (SELECT airport_id FROM airport WHERE airport_code='CDG')),
    ('DEF456', (SELECT airport_id FROM airport WHERE airport_code='FRA'), (SELECT airport_id FROM airport WHERE airport_code='AMS')),
    ('GHI789', (SELECT airport_id FROM airport WHERE airport_code='LHR'), (SELECT airport_id FROM airport WHERE airport_code='CLJ'));

-- Flight information table remains unchanged except problem flight references
INSERT INTO flight_info (reservation_id, airline, planned_departure_date, planned_arrival_date, flight_nr, departing_airport, destination_airport, problem_flight)
VALUES
    ((SELECT reservation_id FROM reservation WHERE reservation_number='ABC123'), 'American Airlines', '2024-08-10', '2024-08-10', 'AA123', (SELECT airport_id FROM airport WHERE airport_code='LHR'), (SELECT airport_id FROM airport WHERE airport_code='MAD'), true),
    ((SELECT reservation_id FROM reservation WHERE reservation_number='ABC123'), 'American Airlines', '2024-08-10', '2024-08-10', 'AA124', (SELECT airport_id FROM airport WHERE airport_code='MAD'), (SELECT airport_id FROM airport WHERE airport_code='CDG'), false),
    ((SELECT reservation_id FROM reservation WHERE reservation_number='DEF456'), 'United Airlines', '2024-08-11', '2024-08-11', 'UA456', (SELECT airport_id FROM airport WHERE airport_code='FRA'), (SELECT airport_id FROM airport WHERE airport_code='AMS'),true),
    ((SELECT reservation_id FROM reservation WHERE reservation_number='GHI789'), 'Southwest Airlines', '2024-08-12', '2024-08-12', 'SW012', (SELECT airport_id FROM airport WHERE airport_code='BCN'), (SELECT airport_id FROM airport WHERE airport_code='CLJ'),true),
    ((SELECT reservation_id FROM reservation WHERE reservation_number='GHI789'), 'Southwest Airlines', '2024-08-13', '2024-08-13', 'SW013', (SELECT airport_id FROM airport WHERE airport_code='LHR'), (SELECT airport_id FROM airport WHERE airport_code='BCN'),false);

-- Passenger details remain unchanged except for the email addresses
INSERT INTO passenger_details (date_of_birth, phone, address, postal_code, email, first_name, last_name)
VALUES
    ( '1980-01-01', '555-1234', '123 Winterfell St', 12345, 'jon.snow@thenorth.got', 'Jon', 'Snow'),
    ( '1990-02-02', '555-5678', '456 Needle St', 67890, 'arya.stark@facelessmen.got', 'Arya', 'Stark');

INSERT INTO cancellation_type (cancellation_type_description)
VALUES
    ('Cancellation'),
    ('Delay'),
    ('Denied Boarding');

INSERT INTO disruption_option (disruption_option_description)
VALUES
    ('>14 days'),
    ('<14 days'),
    ('on flight day'),
    ('>3 hours'),
    ('<3 hours'),
    ('Never Arrived'),
    ('Flight Overbooked'),
    ('Aggressive Behaviour'),
    ('Intoxication'),
    ('Unspecified Reason');


INSERT INTO airline_motive (airline_motive_type_description)
VALUES
    ('Technical problem'),
    ('Meteorological Conditions'),
    ('Strike'),
    ('Problems with Airport'),
    ('Crew Problems'),
    ('Other Motives'),
    ('I don\'t know');

-- Disruption table with Game of Thrones themed disruptions
INSERT INTO disruption (cancellation_type, disruption_option, airline_motive, incident_description)
VALUES
    ((SELECT cancellation_type_id FROM cancellation_type WHERE cancellation_type_description='Cancellation'), (SELECT disruption_option_id FROM disruption_option WHERE disruption_option_description='>14 days'), (SELECT airline_motive_id FROM airline_motive WHERE airline_motive_type_description='Meteorological Conditions'), 'Flight cancelled due to dragon attack.'),
    ((SELECT cancellation_type_id FROM cancellation_type WHERE cancellation_type_description='Cancellation'), (SELECT disruption_option_id FROM disruption_option WHERE disruption_option_description='<14 days'), (SELECT airline_motive_id FROM airline_motive WHERE airline_motive_type_description='Technical problem'), 'Flight cancelled due to sabotage by House Lannister.'),
    ((SELECT cancellation_type_id FROM cancellation_type WHERE cancellation_type_description='Delay'), (SELECT disruption_option_id FROM disruption_option WHERE disruption_option_description='>3 hours'), (SELECT airline_motive_id FROM airline_motive WHERE airline_motive_type_description='Crew Problems'), 'Flight delayed due to execution of crew members by Joffrey Baratheon.'),
    ((SELECT cancellation_type_id FROM cancellation_type WHERE cancellation_type_description='Delay'), (SELECT disruption_option_id FROM disruption_option WHERE disruption_option_description='<3 hours'), (SELECT airline_motive_id FROM airline_motive WHERE airline_motive_type_description='Strike'), 'Flight delayed due to rebellion by Unsullied soldiers.'),
    ((SELECT cancellation_type_id FROM cancellation_type WHERE cancellation_type_description='Denied Boarding'), (SELECT disruption_option_id FROM disruption_option WHERE disruption_option_description='Flight Overbooked'), null, null);




-- Compensation cases updated with thematic user emails
INSERT INTO compensation_case (system_case_id, date_created, status, colleague, reservation_id, passenger_details_id, disruption_id, passenger, compensation)
VALUES
    ('AAA111', '2024-07-08 08:00:00', (SELECT status_id FROM status WHERE status_name='NEW'), null, (SELECT reservation_id FROM reservation WHERE reservation_number='ABC123'), (SELECT passenger_details_id FROM passenger_details WHERE email='jon.snow@thenorth.got'), (SELECT disruption_id FROM disruption WHERE incident_description='Flight cancelled due to dragon attack.'), (SELECT user_id FROM user WHERE email='jon.snow@thenorth.got'), 250),

    ('BBB222', '2024-06-07 08:00:00', (SELECT status_id FROM status WHERE status_name='VALID'), (SELECT user_id FROM user WHERE email='varys@kingslanding.got'), (SELECT reservation_id FROM reservation WHERE reservation_number='DEF456'), (SELECT passenger_details_id FROM passenger_details WHERE email='jon.snow@thenorth.got'), (SELECT disruption_id FROM disruption WHERE incident_description='Flight delayed due to execution of crew members by Joffrey Baratheon.'), (SELECT user_id FROM user WHERE email='jon.snow@thenorth.got'), 250),

    ('CCC333', '2024-05-05 08:00:00', (SELECT status_id FROM status WHERE status_name='INVALID'), (SELECT user_id FROM user WHERE email='daenerys.targaryen@dragonsden.got'), (SELECT reservation_id FROM reservation WHERE reservation_number='GHI789'), (SELECT passenger_details_id FROM passenger_details WHERE email='arya.stark@facelessmen.got'), (SELECT disruption_id FROM disruption WHERE incident_description='Flight delayed due to rebellion by Unsullied soldiers.'), (SELECT user_id FROM user WHERE email='arya.stark@facelessmen.got'), 400);


-- Comments table with enriched thematic comments and emails
INSERT INTO comment (case_id, user_id, comment_text, timestamp)
VALUES
    ((SELECT case_id FROM compensation_case WHERE date_created='2024-07-08 08:00:00'), (SELECT user_id FROM user WHERE email='jon.snow@thenorth.got'), 'The Night’s Watch has reviewed the case. Initial findings suggest interference by White Walkers.', '2024-07-08 08:00:00'),
    ((SELECT case_id FROM compensation_case WHERE date_created='2024-05-05 08:00:00'), (SELECT user_id FROM user WHERE email='arya.stark@facelessmen.got'), 'The Faceless Men have performed their subtle investigations. Rumors of White Walkers’ interference are being carefully considered. The night may be dark and full of terrors, but justice will prevail.','2024-05-06 09:00:00'),
    ((SELECT case_id FROM compensation_case WHERE date_created='2024-05-05 08:00:00'), (SELECT user_id FROM user WHERE email='daenerys.targaryen@dragonsden.got'), 'By the decree of Daenerys Targaryen, the Dragon Queen, a raven has been dispatched to ensure the customer’s plight is addressed. The Iron Throne’s justice is swift and unyielding, and no claim shall be left unattended.', '2024-05-06 15:00:00'),
    ((SELECT case_id FROM compensation_case WHERE date_created='2024-06-07 08:00:00'), (SELECT user_id FROM user WHERE email='jon.snow@thenorth.got'), 'The Maester’s scroll is awaited, though the winds of Winter delay its arrival.', '2024-06-07 08:00:00'),
    ((SELECT case_id FROM compensation_case WHERE date_created='2024-06-07 08:00:00'), (SELECT user_id FROM user WHERE email='varys@kingslanding.got'), 'Case closed. Compensation delivered via raven to the far reaches of Westeros.', '2024-06-07 08:05:00');

INSERT INTO document (case_id, document_type, document_name, document_data_type, data, upload_timestamp)
VALUES
    ((SELECT case_id FROM compensation_case WHERE date_created='2024-07-08 08:00:00'), (SELECT document_type_id FROM document_type WHERE document_type_name='ID'), 'ID.pdf','application/pdf' ,'binarydata1', '2024-07-08 09:00:00'),
    ((SELECT case_id FROM compensation_case WHERE date_created='2024-06-07 08:00:00'), (SELECT document_type_id FROM document_type WHERE document_type_name='Passport'), 'Passport.pdf','application/pdf' ,'binarydata2', '2024-06-07 09:00:00');

INSERT INTO generated_pdf (document_name, case_id, data, upload_timestamp)
VALUES
    ('Document1', (SELECT case_id FROM compensation_case WHERE date_created='2024-07-08 08:00:00'), 'binarydata1', '2024-08-06 10:00:00'),
    ('Document2', (SELECT case_id FROM compensation_case WHERE date_created='2024-07-08 08:00:00'), 'binarydata2', '2024-08-06 11:00:00'),
    ('Document3', (SELECT case_id FROM compensation_case WHERE date_created='2024-06-07 08:00:00'), 'binarydata3', '2024-08-06 12:00:00');

