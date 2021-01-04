create table user_table(
	username varchar_ignorecase(100) not null primary key,
	password varchar_ignorecase(100) not null,
	enabled boolean not null
);

create table authorities (
	username varchar_ignorecase(50) not null,
	authority varchar_ignorecase(50) not null,
	constraint fk_authorities_users foreign key(username) references user_table(username)
);
create unique index ix_auth_username on authorities (username,authority);

insert into user_table (username,password,enabled)
values('bugs','$2a$10$rxUQYxbFsxy3l.oeMLmVEO6PoCHiNEamgrZSoutqBEloP0eztei96',1);
insert into authorities (username,authority)
values('bugs','ROLE_USER');

insert into user_table (username,password,enabled)
values('daffy','$2a$10$9g.m.Yha80vKFhyeO3IH2ePWdbJVDXeYaNL11GKZB5t3VQ4xof9Nm',1);
insert into authorities (username,authority)
values('daffy','ROLE_USER');
insert into authorities (username,authority)
values('daffy','ROLE_MANAGER');

create table books (
  id       LONG NOT NULL Primary Key AUTO_INCREMENT,
  title    VARCHAR(128) NOT NULL,
  author   VARCHAR(128) NOT NULL,
  aveStars FLOAT DEFAULT (0.0),
  img	   VARCHAR(200) DEFAULT('https://islandpress.org/sites/default/files/default_book_cover_2015.jpg'),
  description VARCHAR(1024) NOT NULL
);

create table reviews (
  id       LONG NOT NULL Primary Key AUTO_INCREMENT,
  bookId   LONG NOT NULL,	
  text     VARCHAR(1024) NOT NULL UNIQUE,
  stars	   FLOAT NOT NULL,
  username VARCHAR(100)
  
);

alter table reviews
  add constraint book_review_fk foreign key (bookId)
  references books (id);
 alter table reviews
  add constraint username_review_fk foreign key (username)
  references user_table (username);

insert into books (title, author, description, img)
values ('The 7 Habits of Highly Effective People', 
'Stephen R. Covey',
'The 7 Habits of Highly Effective People by Stephen R. Covey is a self-improvement book. It is written on Covey''s belief that the way we see the world is entirely based on our own perceptions. In order to change a given situation, we must change ourselves, and in order to change ourselves, we must be able to change our perceptions.',
'https://images-na.ssl-images-amazon.com/images/I/71bv3XdE90L.jpg');
 
insert into books (title, author, description,img)
values ('The Prince', 
'Niccolo Machiavelli',
'The Prince is an extended analysis of how to acquire and maintain political power. It includes 26 chapters and an opening dedication to Lorenzo de Medici. The dedication declares Machiavelli''s intention to discuss in plain language the conduct of great men and the principles of princely government.',
'https://images-na.ssl-images-amazon.com/images/I/4117w9ptZnL._SX331_BO1,204,203,200_.jpg'); 
 
insert into reviews (stars, text, bookId, username)
values (4, 'An older book, but still a very good read for priniciple-centered leadership.', 1, 'daffy');
insert into reviews (stars, text, bookId, username)
values (5, 'changed my life, interesting perspective', 1, 'bugs');
 
 
insert into reviews (stars, text, bookId, username)
values (5, 'A very old book that expounds on gaining and keeping power; at any and all costs. It was banned by the Pope in 1559.', 2, 'bugs');

