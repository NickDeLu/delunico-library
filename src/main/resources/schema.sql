create table user_table(
	username varchar_ignorecase(50) not null primary key,
	password varchar_ignorecase(50) not null,
	enabled boolean not null
);

create table authorities (
	username varchar_ignorecase(50) not null,
	authority varchar_ignorecase(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);
create table books (
  id       LONG NOT NULL Primary Key AUTO_INCREMENT,
  title    VARCHAR(128) NOT NULL,
  author   VARCHAR(128) NOT NULL,
  aveStars FLOAT DEFAULT (0.0)
);

create table reviews (
  id       LONG NOT NULL Primary Key AUTO_INCREMENT,
  bookId   LONG NOT NULL,	
  text     VARCHAR(1024) NOT NULL UNIQUE,
  stars	   FLOAT NOT NULL 
  
);

alter table reviews
  add constraint book_review_fk foreign key (bookId)
  references books (id);

insert into books (title, author)
values ('The 7 Habits of Highly Effective People', 'Stephen R. Covey');
 
insert into books (title, author)
values ('The Prince', 'Niccolo Machiavelli'); 
 
insert into reviews (stars, text, bookId)
values (4, 'An older book, but still a very good read for priniciple-centered leadership.', 1);
insert into reviews (stars, text, bookId)
values (4, 'changed my life, interesting perspective', 1);
insert into reviews (stars, text, bookId)
values (3, 'wow so good.', 1);
 
 
insert into reviews (stars, text, bookId)
values (5, 'A very old book that expounds on gaining and keeping power; at any and all costs. It was banned by the Pope in 1559.', 2);

