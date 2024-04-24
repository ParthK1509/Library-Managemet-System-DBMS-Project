CREATE DATABASE library_db1;
USE library_db1;

-- Create tables
CREATE TABLE member (
    lib_id INT PRIMARY KEY auto_increment,
    fname VARCHAR(50),
    lname VARCHAR(50),
    gender CHAR(1),
    email VARCHAR(100),
    mtype VARCHAR(20),
    status VARCHAR(10)
);

CREATE TABLE student (
    lib_id INT PRIMARY KEY ,
    bits_id varchar(13) UNIQUE,
    branch VARCHAR(50),
    semester INT,
    FOREIGN KEY (lib_id) REFERENCES member(lib_id)
);

CREATE TABLE teacher (
    lib_id INT PRIMARY KEY ,
    teacher_id varchar(13) UNIQUE,
    designation VARCHAR(50),
    department VARCHAR(50),
    FOREIGN KEY (lib_id) REFERENCES member(lib_id)
);

CREATE TABLE librarian (
    lib_id INT PRIMARY KEY,
    staff_id varchar(13) unique,
    active BOOLEAN,
    FOREIGN KEY (lib_id) REFERENCES member(lib_id)
);

CREATE TABLE member_phone (
    lib_id INT,
    phone_no numeric(10,0),
    PRIMARY KEY (lib_id, phone_no),
    FOREIGN KEY (lib_id) REFERENCES member(lib_id)
);

CREATE TABLE account (
    lib_id INT PRIMARY KEY,
    password VARCHAR(100),
    FOREIGN KEY (lib_id) REFERENCES member(lib_id)
);

CREATE TABLE shelf (
    shelf_id INT PRIMARY KEY auto_increment,
    hall_no INT,
    shelf_no INT,
    UNIQUE(hall_no,shelf_no)
);

CREATE TABLE book (
    book_id INT PRIMARY KEY auto_increment,
    title VARCHAR(100),
    year INT
    
);

CREATE TABLE author (
    author_id INT PRIMARY KEY auto_increment,
    fname VARCHAR(50),
    lname VARCHAR(50)
);

CREATE TABLE publisher (
    pub_name VARCHAR(50) PRIMARY KEY,
    location VARCHAR(100)
);

CREATE TABLE writes (
    book_id INT,
    author_id INT,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (author_id) REFERENCES author(author_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id)
);

CREATE TABLE publishes (
    pub_name VARCHAR(50),
    book_id INT,
    PRIMARY KEY (pub_name, book_id),
    FOREIGN KEY (pub_name) REFERENCES publisher(pub_name),
    FOREIGN KEY (book_id) REFERENCES book(book_id)
);

CREATE TABLE inventory (
    inventory_id INT PRIMARY KEY auto_increment,
    book_id INT,
    issued BOOLEAN,
    FOREIGN KEY (book_id) REFERENCES book(book_id)
);

CREATE TABLE stored_in (
    inventory_id INT,
    shelf_id INT,
    PRIMARY KEY (inventory_id, shelf_id),
    FOREIGN KEY (inventory_id) REFERENCES inventory(inventory_id),
    FOREIGN KEY (shelf_id) REFERENCES shelf(shelf_id)
);

CREATE TABLE bookgenre (
    book_id INT,
    genre_name VARCHAR(50),
    PRIMARY KEY (book_id, genre_name),
    FOREIGN KEY (book_id) REFERENCES book(book_id)
);

CREATE TABLE borrows (
    borrow_id INT PRIMARY KEY auto_increment,
    lib_id INT,
    inventory_id INT,
    borrow_date DATE,
    return_date DATE,
    approvereturn VARCHAR(20),
    FOREIGN KEY (lib_id) REFERENCES member(lib_id),
    FOREIGN KEY (inventory_id) REFERENCES inventory(inventory_id)
);

CREATE TABLE rating (
    book_id INT,
    lib_id INT,
    rating INT,
    PRIMARY KEY (book_id, lib_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id),
    FOREIGN KEY (lib_id) REFERENCES member(lib_id)
);

CREATE TABLE holds (
    hold_id INT PRIMARY KEY auto_increment,
    book_id INT,
    lib_id INT,
    status VARCHAR(20),
    hold_date DATE,
    hold_time TIME,
    FOREIGN KEY (book_id) REFERENCES book(book_id),
    FOREIGN KEY (lib_id) REFERENCES member(lib_id)
);

CREATE TABLE penalty (
    borrow_id INT PRIMARY KEY,
    amount DECIMAL(10,2),
    paid_status VARCHAR(10),
    FOREIGN KEY (borrow_id) REFERENCES borrows(borrow_id)
);

-- Inserting fake member data
INSERT INTO member (lib_id, fname, lname, gender, email, mtype, status) VALUES
(1, 'John', 'Doe', 'M', 'john.doe@example.com', 'Student', 'Unlocked'),
(2, 'Jane', 'Doe', 'F', 'jane.doe@example.com', 'Teacher', 'Unlocked'),
(3, 'Alice', 'Smith', 'F', 'alice.smith@example.com', 'Librarian', 'Unlocked'),
(4, 'Bob', 'Johnson', 'M', 'bob.johnson@example.com', 'Student', 'Unlocked'),
(5, 'Emily', 'Williams', 'F', 'emily.williams@example.com', 'Teacher', 'Unlocked'),
(6, 'Michael', 'Brown', 'M', 'michael.brown@example.com', 'Librarian', 'Unlocked'),
(7, 'Sarah', 'Jones', 'F', 'sarah.jones@example.com', 'Student', 'Unlocked'),
(8, 'David', 'Martinez', 'M', 'david.martinez@example.com', 'Teacher', 'Unlocked'),
(9, 'Jessica', 'Garcia', 'F', 'jessica.garcia@example.com', 'Librarian', 'Unlocked'),
(10, 'James', 'Hernandez', 'M', 'james.hernandez@example.com', 'Student', 'Unlocked');

-- Inserting fake student data
INSERT INTO student (lib_id, bits_id, branch, semester) VALUES
(1, '2021A7PS0001P', 'Computer Science', 3),
(4, '2021AAPS0002P', 'Electronics and Communication', 2),
(7, '2021A4PS0003P', 'Mechanical Engineering', 4),
(10, '2021A2PS0004P', 'Civil Engineering', 6);

-- Inserting fake teacher data
INSERT INTO teacher (lib_id, teacher_id, designation, department) VALUES
(2, 'T20210001', 'Assistant Professor', 'Computer Science'),
(5, 'T20210002', 'Associate Professor', 'Electronics and Communication'),
(8, 'T20210003', 'Professor', 'Mechanical Engineering');
-- Inserting fake librarian data
INSERT INTO librarian (lib_id, staff_id, active) VALUES
(3, 'L20210001', true),
(6, 'L20210002', true),
(9, 'L20210003', false);

-- Inserting fake member phone data
INSERT INTO member_phone (lib_id, phone_no) VALUES
(1, 1234567890),
(2, 2345678901),
(3, 3456789012),
(4, 4567890123),
(5, 5678901234),
(6, 6789012345),
(7, 7890123456),
(8, 8901234567),
(9, 9012345678),
(10, 9876543210);

-- Inserting fake account data
INSERT INTO account (lib_id, password) VALUES
(1, 'password1'),
(2, 'password2'),
(3, 'password3'),
(4, 'password4'),
(5, 'password5'),
(6, 'password6'),
(7, 'password7'),
(8, 'password8'),
(9, 'password9'),
(10, 'password10');

-- Inserting fake publisher data
INSERT INTO publisher (pub_name, location) VALUES
('MIT Press', 'Cambridge, Massachusetts'),
('Pearson', 'New York, USA'),
('Oxford Press', 'London, UK'),
('Puffin', 'Sydney, Australia'),
('Wiley', 'Hoboken, New Jersey'),
('McGraw-Hill', 'New York, USA'),
('Prentice Hall', 'Upper Saddle River, New Jersey'),
('Elsevier', 'Amsterdam, Netherlands'),
('W. W. Norton & Company', 'New York, USA'),
('Morgan Kaufmann', 'San Francisco, USA');

-- Inserting fake author data
INSERT INTO author (fname, lname) VALUES
('Thomas', 'Cormen'),
('Abraham', 'Silberschatz'),
('Stuart', 'Russell'),
('Andrew', 'Tanenbaum'),
('Kenneth', 'Rosen'),
('Floyd', 'Thomas'),
('Alfred', 'Aho'),
('Michael', 'T. Goodrich'),
('David', 'Patterson');

-- Inserting fake book data
INSERT INTO book (title, year) VALUES
('Introduction to Algorithms', 2009),
('Database Management Systems', 2018),
('Artificial Intelligence: A Modern Approach', 2020),
('Operating System Concepts', 2016),
('Computer Networks', 2019),
('Discrete Mathematics and Its Applications', 2017),
('Digital Electronics', 2015),
('Principles of Compiler Design', 2021),
('Data Structures and Algorithms in Python', 2020),
('Computer Organization and Design', 2018);

-- Inserting fake writes data
INSERT INTO writes (book_id, author_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 2),
(5, 4),
(6, 5),
(7, 6),
(8, 7),
(9, 8),
(10, 9);

-- Inserting fake publishes data
INSERT INTO publishes (pub_name, book_id) VALUES
('MIT Press', 1),
('Pearson', 2),
('Pearson', 3),
('Wiley', 4),
('Pearson', 5),
('McGraw-Hill', 6),
('Pearson', 7),
('Prentice Hall', 8),
('W. W. Norton & Company', 9),
('Morgan Kaufmann', 10);

-- Inserting fake inventory data
INSERT INTO inventory (book_id, issued) VALUES
(1, false),
(1, false),
(2, false),
(3, false),
(3, false),
(4, false),
(5, false),
(5, false),
(6, false),
(6, false),
(6, false),
(7, false),
(8, false),
(8, false),
(9, false),
(10, false);

-- Inserting fake bookgenre data
INSERT INTO bookgenre (book_id, genre_name) VALUES
(1, 'Computer Science'),
(2, 'Computer Science'),
(3, 'Computer Science'),
(4, 'Computer Science'),
(5, 'Computer Science'),
(6, 'Mathematics'),
(7, 'Electronics'),
(8, 'Computer Science'),
(9, 'Computer Science'),
(10, 'Computer Science');

-- Inserting fake shelf data
INSERT INTO shelf (hall_no, shelf_no) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 2),
(3, 1),
(3, 2),
(4, 1),
(4, 2);

-- Inserting fake stored_in data
INSERT INTO stored_in (inventory_id, shelf_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 1),
(10, 2),
(11, 3),
(12, 4),
(13, 5),
(14, 6),
(15, 7),
(16, 8);

-- Inserting fake borrows data
-- INSERT INTO borrows (lib_id, inventory_id, borrow_date, return_date, approvereturn) VALUES
-- (1, 1, '2024-04-01', '2024-04-10', "Approved"),
-- (2, 2, '2024-03-15', '2024-03-30', "Approved"),
-- (3, 3, '2023-03-20', NULL, NULL),
-- (4, 4, '2023-03-20', NULL, NULL),
-- (5, 5, '2023-03-20', NULL, NULL),
-- (6, 6, '2023-03-20', NULL, NULL);

-- Inserting fake rating data
-- INSERT INTO rating (book_id, lib_id, rating) VALUES
-- (1, 1, 4),
-- (2, 2, 5),
-- (3, 3, 3);

-- Inserting fake holds data
-- INSERT INTO holds (book_id, lib_id, status, hold_date, hold_time) VALUES
-- (1, 1, 'Pending', '2024-03-25', '12:00:00'),
-- (2, 2, 'Approved', '2024-03-28', '10:00:00'),
-- (2, 10, 'Pending', '2024-03-25', '12:00:00'),
-- (2, 11, 'Pending', '2024-03-26', '12:00:00'),
-- (2, 12, 'Pending', '2024-03-27', '12:00:00'),
-- (1, 13, 'Pending', '2024-03-28', '12:00:00'),
-- (1, 14, 'Pending', '2024-03-29', '12:00:00'),
-- (1, 15, 'Pending', '2024-03-30', '12:00:00');

-- Inserting fake penalty data
-- INSERT INTO penalty (borrow_id, amount, paid_status) VALUES
-- (1, 5.00, "Unpaid"),
-- (2, 10.00, "Paid");

