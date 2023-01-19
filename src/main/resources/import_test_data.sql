insert into users(name, email)
values ('User1', 'email1@email.com'),
       ('User2', 'email2@email.com');

insert into items(name, description, available, owner_id, request_id)
values ('Item1', 'Description1', true, 1, null),
       ('Item2', 'Description2', false, 1, null);