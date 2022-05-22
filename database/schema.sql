-- create a database
drop schema if exists stocks;
create database stocks;

-- select database

use stocks;

-- create table
create table user (
	user_id int not null auto_increment,
    email varchar(128) UNIQUE not null,
    password varchar(128) not null,
    name varchar(128) not null,
    primary key (user_id)
);

create table assets (
	asset_id int not null auto_increment,
    symbol varchar(8) not null,
    shares int not null,
    share_price double not null,
    date_traded date,
    user_id int not null,
    primary key(asset_id),
    constraint fk_user_id
		foreign key (user_id)
        references user(user_id)
);