--Question 1: creating sequential logs
CREATE SEQUENCE log_seq START WITH 1000 INCREMENT BY 1;

-- Question 5, update class size trigger
CREATE OR REPLACE TRIGGER UpdateClassSize
AFTER INSERT ON g_enrollments
FOR EACH ROW
BEGIN
    -- Increment the class_size in the classes table for the class into which the student was enrolled
    UPDATE classes
    SET class_size = class_size + 1
    WHERE classid = :NEW.classid;
END;
/

-- Question 6, decrease class size trigger
CREATE OR REPLACE TRIGGER DecreaseClassSize
AFTER DELETE ON g_enrollments
FOR EACH ROW
BEGIN
    -- Decrement the class_size in the classes table for the class from which the student was dropped
    UPDATE classes
    SET class_size = class_size - 1
    WHERE classid = :OLD.classid;
END;
/

-- Question 7 Trigger, delete student
CREATE OR REPLACE TRIGGER CascadeDeleteEnrollments
AFTER DELETE ON students
FOR EACH ROW
BEGIN
    -- Delete related entries in G_Enrollments table
    DELETE FROM g_enrollments WHERE g_B# = :OLD.B#;
END;
/

-- Question 8 trigger to delete student from student table
CREATE OR REPLACE TRIGGER Log_Student_Delete
AFTER DELETE ON students
FOR EACH ROW
BEGIN
  INSERT INTO logs (log#, user_name, op_time, table_name, operation, tuple_keyvalue)
  VALUES (log_seq.nextval, USER, SYSDATE, 'students', 'DELETE', :OLD.B#);
END;
/

--- Bonus trigger to insert student and update the logs
CREATE OR REPLACE TRIGGER Log_Student_Insert
AFTER INSERT ON students
FOR EACH ROW
BEGIN
  INSERT INTO logs (log#, user_name, op_time, table_name, operation, tuple_keyvalue)
  VALUES (log_seq.nextval, USER, SYSDATE, 'students', 'INSERT', :NEW.B#);
END;
/

---- Question 8 trigger to add new enrolled student and update the logs
CREATE OR REPLACE TRIGGER Log_Enrollment_Insert
AFTER INSERT ON g_enrollments
FOR EACH ROW
BEGIN
  INSERT INTO logs (log#, user_name, op_time, table_name, operation, tuple_keyvalue)
  VALUES (log_seq.nextval, USER, SYSDATE, 'g_enrollments', 'INSERT', :NEW.g_B# || ',' || :NEW.classid);
END;
/

----
CREATE OR REPLACE TRIGGER Log_Enrollment_Delete
AFTER DELETE ON g_enrollments
FOR EACH ROW
BEGIN
  INSERT INTO logs (log#, user_name, op_time, table_name, operation, tuple_keyvalue)
  VALUES (log_seq.nextval, USER, SYSDATE, 'g_enrollments', 'DELETE', :OLD.g_B# || ',' || :OLD.classid);
END;
/

---- Bonus trigger to insert courses and update the logs
CREATE OR REPLACE TRIGGER Log_Course_Insert
AFTER INSERT ON courses
FOR EACH ROW
DECLARE
    v_course_number courses.course#%TYPE;
BEGIN
    v_course_number := :NEW.course#;
    
    INSERT INTO logs (log#, user_name, op_time, table_name, operation, tuple_keyvalue)
    VALUES (log_seq.nextval, USER, SYSDATE, 'courses', 'INSERT', : NEW.v_course_number);
END;
/
---Bonus trigger to delete courses and update the logs

CREATE OR REPLACE TRIGGER Log_Course_Delete
AFTER DELETE ON courses
FOR EACH ROW
DECLARE
    v_course_number courses.course#%TYPE;
BEGIN
    v_course_number := :OLD.course#;
    
    INSERT INTO logs (log#, user_name, op_time, table_name, operation, tuple_keyvalue)
    VALUES (log_seq.nextval, USER, SYSDATE, 'courses', 'DELETE', v_course_number);
END;
/

----Bonus trigger to Insert class and update the logs
CREATE OR REPLACE TRIGGER Update_Logs_After_Class_Add
AFTER INSERT ON classes
FOR EACH ROW
DECLARE
    v_log_message VARCHAR2(100);
BEGIN
    v_log_message := 'Class added - Class ID: ' || :NEW.classid || ', Dept Code: ' || :NEW.dept_code || ', Course #: ' || :NEW.course# || ', Sect #: ' || :NEW.sect# || ', Year: ' || :NEW.year || ', Semester: ' || :NEW.semester || ', Limit: ' || :NEW.limit || ', Class Size: ' || :NEW.class_size || ', Room: ' || :NEW.room;

    INSERT INTO logs (log#, user_name, op_time, table_name, operation, tuple_keyvalue)
    VALUES (log_seq.nextval, USER, SYSDATE, 'classes', 'INSERT', v_log_message);
END;
/

