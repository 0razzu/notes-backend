DROP DATABASE IF EXISTS notes;
CREATE DATABASE notes;
USE notes;


##### USERS #####
CREATE TABLE user (
    id INT NOT NULL AUTO_INCREMENT,
    login VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    patronymic VARCHAR(255) DEFAULT NULL,
    last_name VARCHAR(255) NOT NULL,
    type ENUM("USER", "SUPER") NOT NULL,
    deleted bool NOT NULL DEFAULT 0,
    time_registered DATETIME NOT NULL DEFAULT now(),
    PRIMARY KEY (id),
    UNIQUE KEY (login)
) ENGINE=INNODB, DEFAULT CHARSET=UTF8MB4;

CREATE TABLE session (
	user_id INT NOT NULL,
    token VARCHAR(36) NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    UNIQUE KEY (token)
) ENGINE=INNODB, DEFAULT CHARSET=UTF8MB4;

CREATE TABLE user_followed (
	user_id INT NOT NULL,
    followed_id INT NOT NULL,
    PRIMARY KEY (user_id, followed_id),
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    FOREIGN KEY (followed_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB, DEFAULT CHARSET=UTF8MB4;

CREATE TABLE user_ignored (
	user_id INT NOT NULL,
    ignored_id INT NOT NULL,
    PRIMARY KEY (user_id, ignored_id),
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
    FOREIGN KEY (ignored_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB, DEFAULT CHARSET=UTF8MB4;


##### NOTES #####
CREATE TABLE section (
	id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    creator_id INT,
    PRIMARY KEY (id),
    FOREIGN KEY (creator_id) REFERENCES user (id) ON DELETE SET NULL
) ENGINE=INNODB, DEFAULT CHARSET=UTF8MB4;

CREATE TABLE note (
	id INT NOT NULL AUTO_INCREMENT,
    subject VARCHAR(255) NOT NULL,
    created DATETIME NOT NULL DEFAULT now(),
    author_id INT,
    section_id INT NOT NULL,
    PRIMARY KEY (id),
    KEY (created),
    FOREIGN KEY (author_id) REFERENCES user (id) ON DELETE SET NULL,
    FOREIGN KEY (section_id) REFERENCES section (id) ON DELETE CASCADE
) ENGINE=INNODB, DEFAULT CHARSET=UTF8MB4;

CREATE TABLE note_revision (
	id INT NOT NULL AUTO_INCREMENT,
    created DATETIME NOT NULL,
    body LONGTEXT NOT NULL,
    note_id INT NOT NULL,
    PRIMARY KEY (id),
    KEY (created),
    FOREIGN KEY (note_id) REFERENCES note (id) ON DELETE CASCADE
) ENGINE=INNODB, DEFAULT CHARSET=UTF8MB4;

CREATE TABLE note_comment (
	id INT NOT NULL AUTO_INCREMENT,
    created DATETIME NOT NULL,
    body MEDIUMTEXT NOT NULL,
    author_id INT,
    note_revision_id INT NOT NULL,
    PRIMARY KEY (id),
    KEY (created),
    FOREIGN KEY (author_id) REFERENCES user (id) ON DELETE SET NULL,
    FOREIGN KEY (note_revision_id) REFERENCES note_revision (id) ON DELETE CASCADE
) ENGINE=INNODB, DEFAULT CHARSET=UTF8MB4;

CREATE TABLE rating (
	id INT NOT NULL AUTO_INCREMENT,
    value TINYINT NOT NULL,
    note_id INT NOT NULL,
    author_id INT,
    PRIMARY KEY (id),
    KEY (note_id),
    FOREIGN KEY (note_id) REFERENCES note (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE=INNODB, DEFAULT CHARSET=UTF8MB4;

INSERT INTO user (login, password, first_name, patronymic, last_name, type)
VALUES ("admin", "tuGmah-vakmyf-1kezqy", "admin", NULL, "admin", "super");
