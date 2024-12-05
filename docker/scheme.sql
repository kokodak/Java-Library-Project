DROP DATABASE IF EXISTS library_db;

CREATE DATABASE library_db;
USE library_db;

CREATE TABLE users
(
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    role     VARCHAR(20)  NOT NULL
);

CREATE TABLE books
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(200) NOT NULL,
    author       VARCHAR(100) NOT NULL,
    category     VARCHAR(50)  NOT NULL,
    is_available BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE rentals
(
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    username            VARCHAR(50) NOT NULL,
    book_id             INT         NOT NULL,
    rental_date         DATE        NOT NULL,
    due_date            DATE        NOT NULL,
    return_date         DATE        NULL,
    is_approved         BOOLEAN     NOT NULL DEFAULT FALSE,
    is_returned         BOOLEAN     NOT NULL DEFAULT FALSE,
    is_return_requested BOOLEAN     NOT NULL DEFAULT FALSE,
    FOREIGN KEY (username) REFERENCES users (username),
    FOREIGN KEY (book_id) REFERENCES books (id)
);

INSERT INTO users (username, password, role)
VALUES ('admin', 'admin', 'admin');

INSERT INTO users (username, password, role)
VALUES ('user1', 'user1', 'user'),
       ('user2', 'user2', 'user'),
       ('user3', 'user3', 'user');

INSERT INTO books (title, author, category, is_available)
VALUES ('자바의 정석', '남궁성', '프로그래밍', TRUE),
       ('이펙티브 자바', '조슈아 블로크', '프로그래밍', TRUE),
       ('토비의 스프링', '이일민', '프로그래밍', TRUE),
       ('클린 코드', '로버트 C. 마틴', '프로그래밍', TRUE),
       ('객체지향의 사실과 오해', '조영호', '프로그래밍', TRUE),
       ('알고리즘 문제 해결 전략', '구종만', '프로그래밍', TRUE),
       ('Do it! 점프 투 파이썬', '박응용', '프로그래밍', TRUE),
       ('Clean Architecture', '로버트 C. 마틴', '프로그래밍', TRUE),
       ('Java Concurrency in Practice', '브라이언 고츠', '프로그래밍', TRUE),
       ('Head First Design Patterns', '에릭 프리먼 외', '프로그래밍', TRUE);

