create database file_storage;
use file_storage;

create table users(
id int auto_increment primary key,
enabled TINYINT NOT NULL DEFAULT 1,
login varchar(30) not null,
password varchar(100) not null
);

CREATE TABLE user_roles (
  id int NOT NULL AUTO_INCREMENT,
  user_id int NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (id), 
  FOREIGN KEY (user_id) REFERENCES users (id)
   ON DELETE CASCADE
       ON UPDATE CASCADE
  );

create table files(
id int auto_increment,
file_name varchar(100) not null,
file_path varchar(100) not null,
fake_file_path varchar(100) not null,
user_id int,
PRIMARY KEY (id),
FOREIGN KEY (user_id) REFERENCES users(id)
 ON DELETE CASCADE
       ON UPDATE CASCADE
);
