insert into users(name, email)
values ('User1', 'email1@email.com'),
       ('User2', 'email2@email.com');

insert into items(name, description, available, owner_id, request_id)
values ('Item1', 'Description1', true, 1, null),
       ('Item2', 'Description2', false, 1, null);

insert into bookings(start_date, end_date, item_id, booker_id, status)
values ('1990-01-01 00:00:00', '1990-01-01 00:01:00', 1, 2, 'APPROVED'),
       ('2990-01-01 00:00:00', '2990-01-01 00:01:00', 1, 2, 'WAITING');

insert into comments(text, item_id, author_id, created)
values ('Comment1', 1, 2, '1990-01-01 00:02:00');

insert into requests(description, requestor_id, created)
values ('Description1', 2, '1990-01-01 00:00:00');


