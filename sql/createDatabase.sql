create database if not exists certificate;

use certificate;

create table tag(id INT AUTO_INCREMENT, name VARCHAR(50) NOT NULL UNIQUE, PRIMARY KEY (id));

create table gift_certificate(id INT AUTO_INCREMENT, name VARCHAR(50) NOT NULL UNIQUE,
description VARCHAR(200), price INT NOT NULL default 0, duration INT NOT NULL default 0,
create_date VARCHAR(50) NOT NULL, last_update_date VARCHAR(50),
is_deleted BOOLEAN NOT NULL default false, PRIMARY KEY (id));

create table gift_certificate_tag(certificate INT NOT NULL, tag INT NOT NULL);

create table user(id INT AUTO_INCREMENT, login VARCHAR(50) NOT NULL UNIQUE,
password VARCHAR(100) NOT NULL, name VARCHAR(50) NOT NULL, surname VARCHAR(50) NOT NULL,
PRIMARY KEY (id));

create table user_role(user_id INT NOT NULL, role ENUM('ADMIN','USER') NOT NULL);

create table order_certificate(id INT AUTO_INCREMENT, cost INT NOT NULL, purchase_time VARCHAR(50) NOT NULL,
certificate_id INT NOT NULL, user_id INT NOT NULL, PRIMARY KEY (id));