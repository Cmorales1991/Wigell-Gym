INSERT INTO gymcustomer (first_name, last_name, user_name) VALUES('Joey', 'Tribbiani', 'joey');
INSERT INTO gymcustomer (first_name, last_name, user_name) VALUES('Ross', 'Geller','ross');
INSERT INTO gymcustomer (first_name, last_name, user_name) VALUES('Rachel', 'Green','rachel');
INSERT INTO gymcustomer (first_name, last_name, user_name) VALUES('Chandler', 'Bing','chandler');

INSERT INTO instructor (name, speciality) VALUES ('Christoffer', 'Yoga');
INSERT INTO instructor (name, speciality) VALUES ('Kevin', 'Lifting');

INSERT INTO workout (workout_name, type, duration, max_participants, price_sek, instructor_id) VALUES ('Hot Yoga', 'Yoga', 60, 10, 350,1);
INSERT INTO workout (workout_name, type, duration, max_participants, price_sek, instructor_id) VALUES ('Hot Yoga', 'Yoga', 120, 10, 700,1);
INSERT INTO workout (workout_name, type, duration, max_participants, price_sek, instructor_id) VALUES ('Crossfit', 'Lifting', 75, 5, 450,2);

INSERT INTO bookings (booking_date, cancelled, customer_id, workout_id, total_price_sek) VALUES ('2025-10-15', false,2,1,350);
INSERT INTO bookings (booking_date, cancelled, customer_id, workout_id, total_price_sek) VALUES ('2025-10-16', false,3,2,700);
INSERT INTO bookings (booking_date, cancelled, customer_id, workout_id, total_price_sek) VALUES ('2025-10-01', false,4,3,450);

