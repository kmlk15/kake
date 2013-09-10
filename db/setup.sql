create table invitees (
id int primary key auto_increment,
englishName varchar(255),
chineseName varchar(255),
partyName varchar(255),
relationship varchar(20),
gender varchar(1),
invitedBy varchar(5),
invitedNum int default 1,
invitedTo varchar(20),
status varchar(20),
goingNum int default 0,
tableNum int default 0,
mealPref varchar(100),
giftItem varchar(255),
giftValue double default 0
) CHARACTER SET utf8 COLLATE utf8_unicode_ci;


create table guestbook (
  id bigint primary key,
  name varchar(255) not null,
  message text
) CHARACTER SET utf8 COLLATE utf8_unicode_ci;


create table invitee (
  id bigint primary key,
  gid bigint,
  leader int default 0,
  name varchar(255) not null,
  ceremony int default 0,
  reception int default 0,
  tnum int,
  meal int default 0
) CHARACTER SET utf8 COLLATE utf8_unicode_ci;
