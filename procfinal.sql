DELIMITER //
CREATE PROCEDURE addStudent(
    
    IN p_fname VARCHAR(50),
    IN p_lname VARCHAR(50),
    IN p_gender CHAR(1),
    IN p_email VARCHAR(100),
    IN p_phone_no numeric(10,0),
    IN p_password VARCHAR(100),
    IN p_bits_id varchar(13),
    IN p_branch VARCHAR(50),
    IN p_semester INT,
    OUT out_status int
)
BEGIN
 DECLARE user_id INT;
 DECLARE bits_id_exists INT;


    -- Check if the email already exists
    SELECT lib_id INTO user_id
    FROM member
    WHERE email = p_email;

    -- Check if bits_id already exists
    SELECT COUNT(*) INTO bits_id_exists
    FROM student
    WHERE bits_id = p_bits_id;
    
    IF bits_id_exists > 0 THEN
        SET out_status =  1;-- 'BITS ID already exists';
    ELSEIF user_id IS NULL THEN
        -- Register a new student
        CALL addMember(@p_lib_id, p_fname, p_lname, p_gender, p_email, 'Student', p_phone_no, p_password);
        INSERT INTO student( lib_id,bits_id, branch, semester)
        VALUES (@p_lib_id, p_bits_id, p_branch, p_semester);
        SET out_status = 0; -- 'Student registered successfully';
    ELSE
        SET out_status = 2; -- 'Email already exists';
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE addTeacher(
    
    IN p_fname VARCHAR(50),
    IN p_lname VARCHAR(50),
    IN p_gender CHAR(1),
    IN p_email VARCHAR(100),
    IN p_phone_no numeric(10,0),
    IN p_password VARCHAR(100),
    IN p_teacher_id varchar(13),
    IN p_designation VARCHAR(50),
    IN p_department VARCHAR(50),
    OUT out_status int
)
BEGIN
    DECLARE user_id INT;
    DECLARE teacher_id_exists INT;
    declare p_lib_id int;
    -- Check if the email already exists
    SELECT lib_id INTO user_id
    FROM member
    WHERE email = p_email;
    
    SELECT COUNT(*) INTO teacher_id_exists
    FROM teacher
    WHERE teacher_id = p_teacher_id;
    IF teacher_id_exists > 0 THEN
        SET out_status = 1; -- 'Teacher ID already exists';
    ELSEIF user_id IS NULL THEN
        -- Register a new teacher
        CALL addMember( @p_lib_id,p_fname, p_lname, p_gender, p_email, 'Teacher', p_phone_no, p_password);
        INSERT INTO teacher(lib_id, teacher_id, designation, department)
        VALUES (@p_lib_id, p_teacher_id, p_designation, p_department);
        SET out_status = 0 ;-- 'Teacher registered successfully';
    ELSE
        SET out_status = 2 ;-- 'Email already exists';
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE addLibrarian(
    
    IN p_fname VARCHAR(50),
    IN p_lname VARCHAR(50),
    IN p_gender CHAR(1),
    IN p_email VARCHAR(100),
    IN p_phone_no numeric(10,0),
    IN p_password VARCHAR(100),
    IN p_staff_id VARCHAR(13),
    IN p_active BOOLEAN,
    OUT out_status int 
)
BEGIN
     Declare user_id int;
     DECLARE staff_id_exists int;
     declare p_lib_id int;
 -- Check if the email already exists
    SELECT lib_id INTO user_id
    FROM member
    WHERE email = p_email;
    
     SELECT COUNT(*) INTO staff_id_exists
    FROM librarian
    WHERE staff_id = p_staff_id;
    IF staff_id_exists > 0 THEN
        SET out_status = 1; -- 'Staff ID already exists';
    ELSEIF user_id IS NULL THEN
        -- Register a new librarian
       CALL addMember(@p_lib_id, p_fname, p_lname, p_gender, p_email, 'Librarian', p_phone_no, p_password);
       INSERT INTO librarian(lib_id, staff_id, active)
       VALUES (@p_lib_id, p_staff_id, p_active);
	   SET out_status = 0; -- 'Librarian registered successfully';
    ELSE
        SET out_status = 2; -- 'Email already exists';
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE addMember(
    out p_lib_id INT,
    IN p_fname VARCHAR(50),
    IN p_lname VARCHAR(50),
    IN p_gender CHAR(1),
    IN p_email VARCHAR(100),
    IN p_mtype VARCHAR(20),
    IN p_phone_no numeric(10,0),
    IN p_password VARCHAR(100)
)
BEGIN
    -- Add member to 'member' table
    INSERT INTO member (fname, lname, gender, email, mtype, status)
    VALUES (p_fname, p_lname, p_gender, p_email, p_mtype, "Unlocked");
    select lib_id into p_lib_id from member where email=p_email;
    -- Add phone number to 'member_phone' table
    INSERT INTO member_phone(lib_id, phone_no)
    VALUES (p_lib_id, p_phone_no);

    -- Add account information to 'account' table
    INSERT INTO account(lib_id, password)
    VALUES (p_lib_id, p_password);
END//
DELIMITER ;

DELIMITER //
-- 
CREATE PROCEDURE signIn(
    IN p_email VARCHAR(100),
    IN p_password VARCHAR(100),
    OUT user_id INT,
    OUT user_type VARCHAR(20),
	OUT out_status int
)
BEGIN
    
    -- Check if the email and password match
    SELECT lib_id, mtype INTO user_id, user_type
    FROM member
    WHERE email = p_email AND lib_id IN (SELECT lib_id FROM account WHERE password = p_password);

    IF user_id IS NOT NULL THEN
       set out_status=0; --  Sign-in succesful
       IF user_type IN ('Teacher', 'Student') THEN
            SET user_type = "Member"; -- Teacher or Student
        ELSE
            SET user_type = "Librarian"; -- Librarian
        END IF;       
    ELSE
       set out_status=1; -- 'Incorrect email/password'
    END IF;
END//
DELIMITER ;

DELIMITER //

CREATE PROCEDURE returnBook(
    IN in_lib_id INT,
    IN in_book_id INT,
    OUT out_status INT
)
BEGIN
    DECLARE borrowed INT;
    DECLARE fine DECIMAL(10,2);     
    DECLARE borrow_duration INT;

    SELECT COUNT(*) INTO borrowed
    FROM borrows
    WHERE lib_id = in_lib_id AND inventory_id in (SELECT inventory_id from inventory where book_id = in_book_id) AND return_date IS NULL;

    IF borrowed > 0 THEN
        SELECT DATEDIFF(CURDATE(), borrow_date) INTO borrow_duration
        FROM borrows
        WHERE lib_id = in_lib_id AND inventory_id in (SELECT inventory_id from inventory where book_id = in_book_id) AND return_date IS NULL;
        
        IF borrow_duration > 20 THEN
            SET fine = (borrow_duration - 20) * 5;
            INSERT INTO penalty(borrow_id, amount, paid_status)
            VALUES((SELECT borrow_id FROM borrows WHERE lib_id = in_lib_id AND inventory_id in (SELECT inventory_id from inventory where book_id = in_book_id) AND return_date IS NULL), fine, "Unpaid");
        END IF;
        
        UPDATE borrows
        SET return_date = CURDATE(), approvereturn = "Pending"
        WHERE lib_id = in_lib_id AND inventory_id in (SELECT inventory_id from inventory where book_id = in_book_id) AND return_date IS NULL;
                
		SET out_status = 0;
    ELSE
        SET out_status = 1;
    END IF;
END //

DELIMITER ;

DELIMITER //

-- LOCK/UNLOCK
CREATE PROCEDURE withdrawBook2(
    IN in_book_id INT,
    IN in_lib_id INT,
    OUT out_status INT
)
BEGIN
    DECLARE book_exists INT;
    DECLARE available_inventory INT;
    DECLARE book_already_borrowed INT;
    DECLARE account_status VARCHAR(10);
	
    SELECT status into account_status from member where lib_id = in_lib_id;
    
    IF account_status = "Unlocked" then
		SELECT COUNT(*) INTO book_exists
		FROM book
		WHERE book_id = in_book_id;

		IF book_exists > 0 THEN
			CALL bookAvailable(in_book_id,@status, @available_inventory);

			IF @status = 0 THEN
				SELECT COUNT(*) INTO book_already_borrowed
				FROM borrows
				WHERE lib_id = in_lib_id AND inventory_id in (SELECT inventory_id from inventory where book_id = in_book_id) AND return_date IS NULL;

				IF book_already_borrowed = 0 THEN
					INSERT INTO borrows(lib_id, inventory_id, borrow_date)
					VALUES(in_lib_id, @available_inventory, CURDATE());

					UPDATE inventory
					SET issued = TRUE
					WHERE inventory_id = @available_inventory;

					SET out_status = 0;
				ELSE
					SET out_status = 2;
				END IF;
			ELSE
				SET out_status = 1;
			END IF;
		ELSE
			SET out_status = 3;
		END IF;
	ELSE
		SET out_status = 4; -- The user's account is locked, so the functionality is disabled
	END IF;
END //

DELIMITER ;

DELIMITER //

-- LOCK/UNLOCK
CREATE PROCEDURE withdrawBook(
    IN in_book_id INT,
    IN in_lib_id INT,
    OUT out_status INT
)
BEGIN
    DECLARE book_exists INT;
    DECLARE available_inventory INT;
    DECLARE book_already_borrowed INT;
    DECLARE account_status VARCHAR(10);
	
    SELECT status into account_status from member where lib_id = in_lib_id;
    
    IF account_status = "Unlocked" then
		SELECT COUNT(*) INTO book_exists
		FROM book
		WHERE book_id = in_book_id;

		IF book_exists > 0 THEN
            SELECT COUNT(*) INTO book_already_borrowed
			FROM borrows
			WHERE lib_id = in_lib_id AND inventory_id in (SELECT inventory_id from inventory where book_id = in_book_id) AND return_date IS NULL;

			IF book_already_borrowed = 0 THEN
				CALL bookAvailable(in_book_id,@status, @available_inventory);

				IF @status = 0 THEN
					INSERT INTO borrows(lib_id, inventory_id, borrow_date)
					VALUES(in_lib_id, @available_inventory, CURDATE());

					UPDATE inventory
					SET issued = TRUE
					WHERE inventory_id = @available_inventory;

					SET out_status = 0;
				ELSE
					SET out_status = 2;
				END IF;
			ELSE
				SET out_status = 1;
			END IF;
		ELSE
			SET out_status = 3;
		END IF;
	ELSE
		SET out_status = 4; -- The user's account is locked, so the functionality is disabled
	END IF;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE bookAvailable(
    IN in_book_id INT,
    OUT out_status_code INT,
    OUT out_inventory_id INT
)
BEGIN
    DECLARE book_exists INT;

    -- Check if the book exists
    SELECT COUNT(*) INTO book_exists
    FROM book
    WHERE book_id = in_book_id;

    IF book_exists > 0 THEN
        -- Check if there is available inventory
        SELECT inventory_id INTO out_inventory_id
        FROM inventory
        WHERE book_id = in_book_id AND issued = FALSE
        LIMIT 1;

        IF out_inventory_id IS NOT NULL THEN
            -- Book exists and available inventory found
            SET out_status_code = 0;
        ELSE
            -- Book exists but no available inventory
            SET out_status_code = 1;
            SET out_inventory_id = -1;
        END IF;
    ELSE
        -- Book doesn't exist
        SET out_status_code = 2;
        SET out_inventory_id = -1;
    END IF;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE borrowHistory (
    IN in_lib_id INT
)
BEGIN
    SELECT 
		b.book_id,
        b.title AS 'Book Title',
        CONCAT(a.fname, ' ', a.lname) AS 'Author',
        br.borrow_date AS 'Borrow Date',
        br.return_date AS 'Return Date',
        COALESCE(p.amount, 0) AS 'Fine',
        CASE 
            WHEN p.paid_status = 'Paid' THEN 'Paid'
            WHEN br.return_date IS NULL THEN 'Unreturned'
            WHEN p.amount IS NULL THEN "No Fine"
            ELSE 'Unpaid'
        END AS 'Fine Status',
        br.approvereturn as 'Status'
    FROM 
        borrows br
	JOIN 
        inventory i ON br.inventory_id = i.inventory_id
	JOIN 
        book b ON i.book_id = b.book_id
    LEFT JOIN 
        penalty p ON br.borrow_id = p.borrow_id
	JOIN 
        writes w ON b.book_id = w.book_id
    JOIN 
        author a ON w.author_id = a.author_id
    WHERE 
        br.lib_id = in_lib_id
    ORDER BY 
        br.borrow_date;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE searchByBookName(
    IN in_title VARCHAR(100),
    OUT out_status INT
)
BEGIN
    DECLARE book_exists INT;

    -- Search for the book by title
    SELECT COUNT(*) INTO book_exists
    FROM book
    WHERE LOWER(title) LIKE CONCAT('%', LOWER(in_title), '%');

    IF book_exists = 0 THEN
        -- Book not found
        SET out_status = 1; -- Not found
    ELSE
        -- Display details of the book and its inventory copies
        SELECT b.book_id, b.title, CONCAT(a.fname, ' ', a.lname) AS author_name, bg.genre_name, s.hall_no, s.shelf_no, inv.inventory_id, inv.issued, FindAvgRating(b.book_id)
        FROM book b
        INNER JOIN writes w ON b.book_id = w.book_id
        INNER JOIN author a ON w.author_id = a.author_id
        INNER JOIN bookgenre bg ON b.book_id = bg.book_id
        INNER JOIN inventory inv ON b.book_id = inv.book_id
        INNER JOIN stored_in si ON inv.inventory_id = si.inventory_id
        INNER JOIN shelf s ON si.shelf_id = s.shelf_id
        WHERE LOWER(title) LIKE CONCAT('%', LOWER(in_title), '%');
        
        SET out_status = 0; -- Found
    END IF;
END //

DELIMITER ;

DELIMITER //
CREATE PROCEDURE searchByGenre(
    IN in_genre VARCHAR(50),
    OUT out_status INT
)
BEGIN
    DECLARE genre_exists INT;

    -- Search for the genre
    SELECT COUNT(*) INTO genre_exists
    FROM bookgenre
    WHERE LOWER(genre_name) LIKE CONCAT('%',LOWER(in_genre),'%');

    IF genre_exists = 0 THEN
        -- Genre not found
        SET out_status = 1; -- Not found
    ELSE
        -- Display details of books with the given genre and their inventory copies
        SELECT b.book_id, b.title, CONCAT(a.fname, ' ', a.lname) AS author_name, bg.genre_name, s.hall_no, s.shelf_no, inv.inventory_id, inv.issued, FindAvgRating(b.book_id)
        FROM book b
        INNER JOIN writes w ON b.book_id = w.book_id
        INNER JOIN author a ON w.author_id = a.author_id
        INNER JOIN bookgenre bg ON b.book_id = bg.book_id
        INNER JOIN inventory inv ON b.book_id = inv.book_id
        INNER JOIN stored_in si ON inv.inventory_id = si.inventory_id
        INNER JOIN shelf s ON si.shelf_id = s.shelf_id
        WHERE LOWER(genre_name) LIKE CONCAT('%',LOWER(in_genre),'%');
        
        SET out_status = 0; -- Found
    END IF;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE searchByAuthorName(
    IN in_fname VARCHAR(50),
    IN in_lname VARCHAR(50),
    OUT out_status INT
)
BEGIN
    DECLARE author_exists INT;

    -- Search for the author
    SELECT COUNT(*) INTO author_exists
    FROM author
    WHERE LOWER(fname) LIKE CONCAT('%',LOWER(in_fname),'%') AND LOWER(lname) LIKE CONCAT('%',LOWER(in_lname),'%');

    IF author_exists = 0 THEN
        -- Author not found
        SET out_status = 1; -- Not found
    ELSE
        -- Display details of books by the author and their inventory copies
        SELECT b.book_id, b.title, CONCAT(a.fname, ' ', a.lname) AS author_name, bg.genre_name, s.hall_no, s.shelf_no, inv.inventory_id, inv.issued, FindAvgRating(b.book_id)
        FROM book b
		JOIN writes w ON (b.book_id = w.book_id)
        JOIN author a ON (w.author_id = a.author_id)
        JOIN bookgenre bg ON (b.book_id = bg.book_id)
        JOIN inventory inv ON (b.book_id = inv.book_id)
        JOIN stored_in si ON (inv.inventory_id = si.inventory_id)
        JOIN shelf s ON (si.shelf_id = s.shelf_id)
        WHERE LOWER(fname) LIKE CONCAT('%',LOWER(in_fname),'%') AND LOWER(lname) LIKE CONCAT('%',LOWER(in_lname),'%');
        
        SET out_status = 0; -- Found
    END IF;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE placeHold(
    IN p_lib_id INT,
    IN p_book_id INT,
    OUT out_status INT
)
BEGIN
    DECLARE book_status BOOLEAN;
    DECLARE max_hold_id INT;
    DECLARE account_status VARCHAR(10);
    DECLARE book_already_borrowed INT;
	
    SELECT status into account_status from member where lib_id = p_lib_id;
    
    IF account_status = "Unlocked" then
        SELECT COUNT(*) INTO book_already_borrowed
		FROM borrows
		WHERE lib_id = p_lib_id AND inventory_id in (SELECT inventory_id from inventory where book_id = p_book_id) AND return_date IS NULL;
		IF book_already_borrowed = 0 THEN
			CALL bookAvailable(p_book_id,@status, @available_inventory);
			IF @status = 1 THEN
				INSERT INTO holds(book_id, lib_id, status, hold_date, hold_time)
				VALUES (p_book_id, p_lib_id, 'Pending', CURRENT_DATE(), CURRENT_TIME());
				
				SET out_status = 0;
			ELSE
				SET out_status = 3;
			END IF;
		ELSE
			SET out_status = 1; -- Book is currently available
		END IF;
	ELSE
		SET out_status = 2;
	END IF;
END//

DELIMITER ;

DELIMITER //

-- LOCK/UNLOCK
CREATE PROCEDURE renewBook(
    IN in_book_id INT,
    IN in_lib_id INT,
    OUT out_status INT
)
BEGIN
    DECLARE borrow_exists INT;
    DECLARE hold_exists INT;
    DECLARE p_borrow_date DATE;
    DECLARE difference_days INT;
    DECLARE inv_id INT;
    DECLARE account_status VARCHAR(10);
	
    SELECT status into account_status from member where lib_id = in_lib_id;
    
    IF account_status = "Unlocked" then
    -- Check if there is an open borrow with the given lib_id and book_id
		SELECT COUNT(*) INTO borrow_exists
		FROM borrows br
		JOIN inventory inv ON br.inventory_id = inv.inventory_id
		WHERE br.lib_id = in_lib_id AND inv.book_id = in_book_id AND br.return_date IS NULL;

		IF borrow_exists = 0 THEN
			-- No open borrow found
			SET out_status = 1; -- No open borrow
		ELSE
			-- Get the inventory_id for the book
			SELECT inv.inventory_id INTO inv_id
			FROM borrows br
			JOIN inventory inv ON br.inventory_id = inv.inventory_id
			WHERE br.lib_id = in_lib_id AND inv.book_id = in_book_id AND br.return_date IS NULL;

			-- Check if there is a pending hold on the book by another user
			SELECT COUNT(*) INTO hold_exists
			FROM holds
			WHERE book_id = in_book_id AND status = 'Pending';

			IF hold_exists > 0 THEN
				-- Pending hold found
				SET out_status = 2; -- Pending hold
			ELSE
				-- Retrieve borrow_date
				SELECT borrow_date INTO p_borrow_date
				FROM borrows
				WHERE lib_id = in_lib_id AND inventory_id = inv_id AND return_date IS NULL;

				-- Calculate the difference in days between current_date and borrow_date
				SET difference_days = DATEDIFF(CURDATE(), p_borrow_date);

				IF difference_days > 20 THEN
					-- Borrow duration exceeded 20 days
					SET out_status = 3; -- Exceeded borrow duration
				ELSE
					-- Update borrow_date to current date
					UPDATE borrows
					SET borrow_date = CURDATE()
					WHERE lib_id = in_lib_id AND inventory_id = inv_id AND return_date IS NULL;

					SET out_status = 0; -- Success
				END IF;
			END IF;
		END IF;
	ELSE
		SET out_status = 4; -- account is locked
	END IF;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE updateName(
    IN in_fname VARCHAR(50),
    IN in_lname VARCHAR(50),
    IN in_lib_id INT,
    OUT out_status INT
)
BEGIN
    UPDATE member
    SET fname = in_fname, lname = in_lname
    WHERE lib_id = in_lib_id;

    SET out_status = 0; -- Success
END //

CREATE PROCEDURE updateGender(
    IN in_gender CHAR(1),
    IN in_lib_id INT,
    OUT out_status INT
)
BEGIN
    UPDATE member
    SET gender = in_gender
    WHERE lib_id = in_lib_id;

    SET out_status = 0; -- Success
END //

CREATE PROCEDURE updatePassword(
    IN in_old_password VARCHAR(100),
    IN in_new_password VARCHAR(100),
    IN in_lib_id INT,
    OUT out_status INT
)
BEGIN
    DECLARE current_password VARCHAR(100);
    
    -- Retrieve current password
    SELECT password INTO current_password
    FROM account
    WHERE lib_id = in_lib_id;

    -- Check if the old password matches
    IF current_password = in_old_password THEN
        -- Update the password
        UPDATE account
        SET password = in_new_password
        WHERE lib_id = in_lib_id;

        SET out_status = 0; -- Success
    ELSE
        SET out_status = 1; -- Incorrect old password
    END IF;
END //
DELIMITER ;

DELIMITER //

CREATE FUNCTION FindAvgRating(bookId INT) RETURNS DECIMAL(5,2) READS SQL DATA
BEGIN
    DECLARE avgRating DECIMAL(5,2);

    SELECT AVG(rating) INTO avgRating
    FROM rating
    WHERE book_id = bookId;

    RETURN avgRating;
END//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE insertRating(
    IN in_lib_id INT,
    IN in_book_id INT,
    IN in_rating INT,
    OUT out_status INT
)
BEGIN
    DECLARE borrow_exists INT;
    DECLARE rating_exists INT;

    -- Check if the user has borrowed or currently has the book
    SELECT COUNT(*) INTO borrow_exists
    FROM borrows
    WHERE lib_id = in_lib_id AND inventory_id in (
        SELECT inventory_id FROM inventory WHERE book_id = in_book_id
    );

    IF borrow_exists = 0 THEN
        -- User has not borrowed the book
        SET out_status = 1; -- Not borrowed
    ELSE
        -- Check if the user has already rated the book
        SELECT COUNT(*) INTO rating_exists
        FROM rating
        WHERE lib_id = in_lib_id AND book_id = in_book_id;

        IF rating_exists = 0 THEN
            -- Insert a new rating
            INSERT INTO rating (lib_id, book_id, rating) VALUES (in_lib_id, in_book_id, in_rating);
        ELSE
            -- Update the existing rating
            UPDATE rating SET rating = in_rating WHERE lib_id = in_lib_id AND book_id = in_book_id;
        END IF;

        SET out_status = 0; -- Success
    END IF;
END //

DELIMITER ;

-- LOCK/UNLOCK
DELIMITER //
CREATE PROCEDURE bookRoom(
    IN in_room_name VARCHAR(50),
    IN in_start_time TIME,
    IN in_date DATE,
    IN in_lib_id INT,
    OUT out_status INT
)
BEGIN
    DECLARE room_exists INT;
    DECLARE conflict_exists INT;
    DECLARE account_status VARCHAR(10);
	
    SELECT status into account_status from member where lib_id = in_lib_id;
    
    IF account_status = "Unlocked" then
    -- Check if the room name exists in the library
		SELECT COUNT(*) INTO room_exists
		FROM room
		WHERE room_name = in_room_name;

		IF room_exists = 0 THEN
			-- Room does not exist
			SET out_status = 1; -- Room does not exist
		ELSE
			-- Check for conflicting bookings
			SELECT COUNT(*) INTO conflict_exists
			FROM rents
			WHERE room_name = in_room_name
				AND in_date = DATE(date)
				AND TIME(in_start_time) BETWEEN start_time AND end_time;

			IF conflict_exists > 0 THEN
				-- Conflicting booking found
				SET out_status = 2; -- Conflicting booking
			ELSE
				-- Insert new rental record
				INSERT INTO rents (start_time, end_time, date, lib_id, room_name)
				VALUES (in_start_time, ADDTIME(in_start_time, '01:00:00'), in_date, in_lib_id, in_room_name);

				SET out_status = 0; -- Success
			END IF;
		END IF;
	ELSE
		SET out_status = 3;
	END IF;
END //

DELIMITER ;

DELIMITER //

CREATE PROCEDURE approveBookReturn(
)
BEGIN
    DECLARE hold_lib_id INT;
    DECLARE hold_book_id INT;
    DECLARE hold_pending INT;   
    DECLARE inv_id INT;
	DECLARE pendingborrows INT;
    DECLARE id_book INT;
    
    SELECT COUNT(*) INTO pendingborrows FROM borrows where approvereturn = 'Pending';
    WHILE pendingborrows > 0 DO
			SELECT inventory_id into inv_id from borrows where approvereturn = "Pending" LIMIT 1;
			SELECT book_id into id_book from inventory where inventory_id = inv_id LIMIT 1;
            
			UPDATE inventory
			SET issued = FALSE
			WHERE inventory_id = inv_id;
			
			UPDATE borrows
			SET approvereturn = "Approved"
			WHERE approvereturn = "Pending" and inventory_id = inv_id;
			
			SELECT lib_id, book_id INTO hold_lib_id, hold_book_id
			FROM holds
			WHERE book_id = id_book AND status = 'Pending'
			ORDER BY hold_date, hold_time
			LIMIT 1;
			
			IF hold_lib_id IS NOT NULL AND hold_book_id IS NOT NULL THEN
				CALL withdrawBook(id_book, hold_lib_id, @withdraw_status);
                IF @withdraw_status = 0 THEN
					UPDATE holds set status = "Approved" where lib_id = hold_lib_id AND book_id = hold_book_id;
				END IF;
			END IF;
            SET pendingborrows = pendingborrows - 1;
        END WHILE;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE lockOverdue()
BEGIN
    UPDATE member SET status = "Locked" where lib_id in (SELECT lib_id from borrows b join penalty p on (b.borrow_id = p.borrow_id) where paid_status = "Unpaid");
    SET SQL_SAFE_UPDATES = 0;
    delete from holds where lib_id in (SELECT lib_id from borrows b join penalty p on (b.borrow_id = p.borrow_id) where paid_status = "Unpaid") AND status = "Pending";
	SET SQL_SAFE_UPDATES = 1;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE acceptFine(
	IN in_lib_id INT,
    INOUT payment INT,
    OUT out_status INT
)
BEGIN
    DECLARE penalties INT;
    DECLARE fine INT;
    DECLARE penalty_id INT;
    
    SELECT COUNT(*) INTO penalties FROM penalty where borrow_id IN (select borrow_id from borrows where lib_id = in_lib_id) AND paid_status = "Unpaid";
    
    if penalties > 0 then
	loop1: WHILE penalties > 0 AND payment>0 DO
			SELECT amount, borrow_id into fine, penalty_id from penalty where borrow_id in (select borrow_id from borrows where lib_id = in_lib_id) AND paid_status = "Unpaid" LIMIT 1;
            if fine <= payment then
				set payment = payment-fine;
                set penalties = penalties -1;
                UPDATE penalty SET paid_status="Paid" where borrow_id = penalty_id;
			else
				leave loop1;
			end if;
		END WHILE;
            IF penalties = 0 then
				update member set status = "Unlocked" where lib_id = in_lib_id;
                set out_status = 0; -- All penalties paid and account is unlocked
			else
				set out_status = 1; -- Amount not sufficient to cover the penalties
			end if;
	else
		set out_status = 2; -- No penalties for the user
	end if;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE addBook(
   
    IN p_title VARCHAR(100),
    IN p_year INT,
    in p_shelf_no int,
    IN p_hall_no int,
    IN p_pub_name VARCHAR(50),
    IN p_author_fname VARCHAR(50),
    IN p_author_lname VARCHAR(50),
    IN p_location VARCHAR(50),
    IN p_genre_name VARCHAR(50),
    OUT out_status INT
    
)
BEGIN
    declare publisher_count int;
    declare shelf_count int;
    declare p_book_id int;
    DECLARE author_count INT;
    DECLARE book_exists INT;
	declare p_author_id int;
	declare p_inventory_id int;
    declare p_shelf_id int;
     
	SELECT COUNT(*) INTO book_exists
    FROM book
    WHERE title=p_title and year=p_year;

   
    IF book_exists > 0 THEN
        SET out_status = 1;
        
    ELSE
        -- Insert the book
        INSERT INTO book( title, year)
        VALUES (p_title, p_year);
        
        select last_insert_id() into p_book_id;
        
        INSERT INTO bookgenre (book_id, genre_name) VALUES (p_book_id, p_genre_name);
        
         -- Check if the publisher exists
		SELECT COUNT(*) INTO publisher_count
		FROM publisher
		WHERE pub_name = p_pub_name;

    -- If publisher does not exist, add it
		IF publisher_count =0 THEN
			INSERT INTO publisher(pub_name,location)
			VALUES (p_pub_name,p_location);
			
		END IF;
		insert into publishes(pub_name,book_id)
		 VALUES (p_pub_name,p_book_id);
			-- Add the book to inventory
		-- Check if the author exists
		SELECT COUNT(*) INTO author_count
		FROM author
		WHERE  fname=p_author_fname and lname=p_author_lname;

		-- If author does not exist, add it
		IF author_count = 0 THEN
			
			INSERT INTO author( fname, lname)
			VALUES ( p_author_fname, p_author_lname);
		   
		END IF;
		
		select author_id into p_author_id from author
		where fname=p_author_fname and lname=p_author_lname;
	 
		insert into writes(book_id,author_id)
			  VALUES (p_book_id,p_author_id);
		-- Check if the book exists
	
		SELECT COUNT(*) INTO shelf_count
		FROM shelf
		WHERE shelf_no = p_shelf_no and hall_no = p_hall_no;

		-- If author does not exist, add it
		IF shelf_count = 0 THEN
			INSERT INTO shelf (shelf_no, hall_no)
			VALUES (p_shelf_no, p_hall_no);
		END IF;
		
		select shelf_id into p_shelf_id FROM shelf
		WHERE shelf_no = p_shelf_no and hall_no = p_hall_no;
	 			
		SET out_status = 0;
        
    END IF;
    
    select book_id into p_book_id from book
        WHERE title=p_title and year=p_year;
  
    INSERT INTO inventory( book_id, issued)
        VALUES ( p_book_id, FALSE); -- Generate random inventory ID
        select last_insert_id() into p_inventory_id;
	insert into stored_in(inventory_id,shelf_id)
		values(p_inventory_id,p_shelf_id);
END//
DELIMITER ;

DELIMITER //

CREATE PROCEDURE viewHolds (
    IN in_lib_id INT
)
BEGIN
    SELECT 
        b.title AS 'Book Title',
        CONCAT(a.fname, ' ', a.lname) AS 'Author',
        h.hold_date AS 'Hold Date',
        h.hold_time AS 'Hold Time',
        h.status AS 'Status'
    FROM 
        holds h
	JOIN 
        book b ON h.book_id = b.book_id
	JOIN 
        writes w ON b.book_id = w.book_id
    JOIN 
        author a ON w.author_id = a.author_id
    WHERE 
        h.lib_id = in_lib_id
    ORDER BY 
        h.hold_date,
		h.hold_time;
END //

DELIMITER ;