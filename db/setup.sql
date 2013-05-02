create table invitees (
id int primary key auto_increment,
englishName varchar(255),
chineseName varchar(255),
relationship varchar(1),
gender varchar(1),
invitedBy varchar(5),
invitedNum int default 1,
status varchar(10),
goingNum int default 0,
tableNum int default 0,
giftItem varchar(255),
giftValue double default 0
);