CREATE OR REPLACE PACKAGE display_package AS
    PROCEDURE show_students(p_cursor OUT SYS_REFCURSOR);
    PROCEDURE show_courses(p_cursor OUT SYS_REFCURSOR);
    PROCEDURE show_prerequisites(p_cursor OUT SYS_REFCURSOR);
    PROCEDURE show_course_credit(p_cursor OUT SYS_REFCURSOR);
    PROCEDURE show_classes(p_cursor OUT SYS_REFCURSOR);
    PROCEDURE show_score_grade(p_cursor OUT SYS_REFCURSOR);
    PROCEDURE show_g_enrollments(p_cursor OUT SYS_REFCURSOR);
    PROCEDURE show_logs(p_cursor OUT SYS_REFCURSOR);
    PROCEDURE enroll_student_into_class(p_Bnum IN CHAR,p_Classid IN CHAR); 
    PROCEDURE drop_student(p_Bnum IN CHAR, p_Classid IN CHAR);
    PROCEDURE delete_student(p_Bnum IN CHAR);
    PROCEDURE list_students_in_class (class_id_param IN classes.classid%TYPE,students_out OUT SYS_REFCURSOR);
    PROCEDURE add_students(a_B# IN students."B#"%TYPE,a_firstname IN students.first_name%TYPE,a_lastname IN students.last_name%TYPE,a_status IN students.st_level%TYPE,a_gpa IN students.gpa%TYPE,a_email IN students.email%TYPE,a_bdate IN students.bdate%TYPE,show_message OUT VARCHAR2);
    PROCEDURE find_prerequisites (p_dept_code IN courses.dept_code%TYPE,p_course_num IN courses.course#%TYPE,prereq_cursor OUT SYS_REFCURSOR);
    PROCEDURE add_course(p_dept_code IN courses.dept_code%TYPE,p_course_number IN courses.course#%TYPE,p_title IN courses.title%TYPE);
    PROCEDURE delete_course(p_dept_code IN courses.dept_code%TYPE,p_course_number IN courses.course#%TYPE);
    PROCEDURE add_class(p_classid IN CHAR,p_dept_code IN VARCHAR2,p_course_num IN NUMBER,p_sect_num IN NUMBER,p_year IN NUMBER,p_semester IN VARCHAR2,p_limit IN NUMBER,p_class_size IN NUMBER,p_room IN VARCHAR2);
    PROCEDURE delete_class(p_classId IN CHAR);
END display_package;
/


CREATE OR REPLACE PACKAGE BODY display_package AS

    PROCEDURE show_students(p_cursor OUT SYS_REFCURSOR) IS
    BEGIN
        OPEN p_cursor FOR SELECT * FROM Students;
    END show_students;

    PROCEDURE show_courses(p_cursor OUT SYS_REFCURSOR) IS
    BEGIN
        OPEN p_cursor FOR SELECT * FROM Courses;
    END show_courses;

    PROCEDURE show_prerequisites(p_cursor OUT SYS_REFCURSOR) IS
    BEGIN
        OPEN p_cursor FOR SELECT * FROM Prerequisites;
    END show_prerequisites;

    PROCEDURE show_course_credit(p_cursor OUT SYS_REFCURSOR) IS
    BEGIN
        OPEN p_cursor FOR SELECT * FROM Course_Credit;
    END show_course_credit;

    PROCEDURE show_classes(p_cursor OUT SYS_REFCURSOR) IS
    BEGIN
        OPEN p_cursor FOR SELECT * FROM Classes;
    END show_classes;

    PROCEDURE show_score_grade(p_cursor OUT SYS_REFCURSOR) IS
    BEGIN
        OPEN p_cursor FOR SELECT * FROM Score_Grade;
    END show_score_grade;

    PROCEDURE show_g_enrollments(p_cursor OUT SYS_REFCURSOR) IS
    BEGIN
        OPEN p_cursor FOR SELECT * FROM G_Enrollments;
    END show_g_enrollments;

    PROCEDURE show_logs(p_cursor OUT SYS_REFCURSOR) IS
    BEGIN
        OPEN p_cursor FOR SELECT * FROM Logs;
    END show_logs;
	
    PROCEDURE enroll_student_into_class(
    p_Bnum IN CHAR,
    p_Classid IN CHAR
) 
IS
    v_st_level VARCHAR2(10);
    v_semester VARCHAR2(8);
    v_year NUMBER;
    v_class_size NUMBER;
    v_limit NUMBER;
    v_count_classes NUMBER;
    v_prereq_count NUMBER;
    v_completed_count NUMBER;
 BEGIN
    -- Check if the B# exists and is a graduate student
 SELECT st_level INTO v_st_level FROM students WHERE B# = p_Bnum;
    IF v_st_level IS NULL THEN
        RAISE_APPLICATION_ERROR(-20001, 'The B# is invalid.');
    ELSIF v_st_level NOT IN ('master', 'PhD') THEN
            RAISE_APPLICATION_ERROR(-20002, 'This is not a graduate student.');
    END IF;

    -- Check if the classid is valid and retrieve semester, year, class size, and limit
    SELECT SEMESTER, "YEAR", CLASS_SIZE, "LIMIT" INTO v_semester, v_year, v_class_size, v_limit
       FROM classes WHERE classid = p_Classid;
    IF SQL%NOTFOUND THEN
        RAISE_APPLICATION_ERROR(-20003, 'The classid is invalid.');
    ELSIF v_semester != 'Spring' OR v_year != 2021 THEN
           RAISE_APPLICATION_ERROR(-20004, 'Cannot enroll into a class from a previous semester.');
    ELSIF v_class_size >= v_limit THEN
        RAISE_APPLICATION_ERROR(-20005, 'The class is already full.');
               END IF;

-- Check if the student is already enrolled in this class
    SELECT COUNT(*) INTO v_count_classes FROM g_enrollments WHERE g_B# = p_Bnum AND classid = p_Classid;
    IF v_count_classes > 0 THEN
        RAISE_APPLICATION_ERROR(-20006, 'The student is already in the class.');
    END IF;

    -- Check if the student is enrolled in more than five classes in the same semester and year
    SELECT COUNT(*) INTO v_count_classes
    FROM g_enrollments JOIN classes ON g_enrollments.classid = classes.classid
       WHERE g_B# = p_Bnum AND year = v_year AND semester = v_semester;
    IF v_count_classes >= 5 THEN
        RAISE_APPLICATION_ERROR(-20007, 'Students cannot be enrolled in more than five classes in the same semester.');
           END IF;

    -- Insert the enrollment if all checks are passed
    INSERT INTO g_enrollments (g_B#, classid, score)
    VALUES (p_Bnum, p_Classid, NULL);
 
     COMMIT;
     EXCEPTION
    WHEN NO_DATA_FOUND THEN
         RAISE_APPLICATION_ERROR(-20008, 'An unexpected error occurred.');
    WHEN OTHERS THEN
        ROLLBACK;
          RAISE;
 END enroll_student_into_class;

PROCEDURE drop_student(p_Bnum IN CHAR, p_Classid IN CHAR) IS
        v_st_level VARCHAR2(10);
        v_semester VARCHAR2(8);
        v_year NUMBER;
        v_count_classes NUMBER;
    BEGIN
        -- Check if the B# exists and is a graduate student
        SELECT st_level INTO v_st_level FROM students WHERE B# = p_Bnum;
        IF v_st_level IS NULL THEN
            RAISE_APPLICATION_ERROR(-20001, 'The B# is invalid.');
        ELSIF v_st_level NOT IN ('master', 'PhD') THEN
            RAISE_APPLICATION_ERROR(-20002, 'This is not a graduate student.');
        END IF;

        -- Check if the classid is valid
        SELECT semester, year INTO v_semester, v_year FROM classes WHERE classid = p_Classid;
        IF SQL%NOTFOUND THEN
            RAISE_APPLICATION_ERROR(-20003, 'The classid is invalid.');
        ELSIF v_semester != 'Spring' OR v_year != 2021 THEN
            RAISE_APPLICATION_ERROR(-20004, 'Only enrollment in the current semester can be dropped.');
        END IF;

        -- Check if the student is enrolled in the class
        SELECT COUNT(*) INTO v_count_classes FROM g_enrollments WHERE g_B# = p_Bnum AND classid = p_Classid;
        IF v_count_classes = 0 THEN
            RAISE_APPLICATION_ERROR(-20005, 'The student is not enrolled in the class.');
        END IF;

        -- Check if this is the only class for this student in Spring 2021
        SELECT COUNT(*) INTO v_count_classes FROM g_enrollments WHERE g_B# = p_Bnum AND classid IN 
            (SELECT classid FROM classes WHERE semester = 'Spring' AND year = 2021);
        IF v_count_classes = 1 THEN
            RAISE_APPLICATION_ERROR(-20006, 'This is the only class for this student in Spring 2021 and cannot be dropped.');
        END IF;

        -- Delete the student from the class
        DELETE FROM g_enrollments WHERE g_B# = p_Bnum AND classid = p_Classid;

        COMMIT;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20007, 'An unexpected error occurred.');
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END drop_student;
	
PROCEDURE delete_student(p_Bnum IN CHAR) IS
        v_count NUMBER;
    BEGIN
        -- Check if the B# exists in the Students table
        SELECT COUNT(*) INTO v_count FROM students WHERE B# = p_Bnum;
        IF v_count = 0 THEN
            RAISE_APPLICATION_ERROR(-20001, 'The B# is invalid.');
        END IF;

        -- Delete the student from the Students table
        DELETE FROM students WHERE B# = p_Bnum;

        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            RAISE;
    END delete_student;
	
PROCEDURE list_students_in_class (
    class_id_param IN classes.classid%TYPE,
    students_out OUT SYS_REFCURSOR
)
IS
    v_class_exists NUMBER;
BEGIN
    -- Check if the provided classid exists in the Classes table
    SELECT COUNT(*)
    INTO v_class_exists
    FROM classes
    WHERE classid = class_id_param;

    -- If the classid is invalid, raise an error
    IF v_class_exists = 0 THEN
        raise_application_error(-20001, 'The classid is invalid.');
    ELSE
        -- Open the cursor for the query to select students in the class
        OPEN students_out FOR
            SELECT s."B#", s.first_name, s.last_name
            FROM students s
            JOIN g_enrollments ge ON s."B#" = ge.g_B#
            WHERE ge.classid = class_id_param;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        raise_application_error(-20002, 'An error occurred: ' || SQLERRM);
END list_students_in_class;

PROCEDURE add_students (
    a_B# IN students."B#"%TYPE,
    a_firstname IN students.first_name%TYPE,
    a_lastname IN students.last_name%TYPE,
    a_status IN students.st_level%TYPE,
    a_gpa IN students.gpa%TYPE,
    a_email IN students.email%TYPE,
	a_bdate IN students.bdate%TYPE,
    show_message OUT VARCHAR2
)
AS
BEGIN
    INSERT INTO students ("B#", first_name, last_name, st_level, gpa, email, bdate)
    VALUES (a_B#, a_firstname, a_lastname, a_status, a_gpa, a_email, a_bdate);
    
    show_message := 'Student has been added successfully';
END add_students;

PROCEDURE find_prerequisites (
    p_dept_code IN courses.dept_code%TYPE,
    p_course_num IN courses.course#%TYPE,
    prereq_cursor OUT SYS_REFCURSOR
)
IS
BEGIN
    -- Check if the provided course exists
    DECLARE
        v_exists NUMBER;
    BEGIN
        SELECT COUNT(*)
        INTO v_exists
        FROM courses
        WHERE dept_code = p_dept_code AND course# = p_course_num;

        IF v_exists = 0 THEN
            -- If the course does not exist, raise an error
            raise_application_error(-20001, p_dept_code || p_course_num || ' does not exist.');
        END IF;
    EXCEPTION
        WHEN OTHERS THEN
            raise_application_error(-20002, 'An error occurred while checking if the course exists: ' || SQLERRM);
    END;

    -- Declare the cursor for prerequisites
    OPEN prereq_cursor FOR
        SELECT pre_dept_code || pre_course# AS prerequisite
        FROM prerequisites
        WHERE dept_code = p_dept_code AND course# = p_course_num;

EXCEPTION
    WHEN OTHERS THEN
        raise_application_error(-20003, 'An error occurred: ' || SQLERRM);
END find_prerequisites;

PROCEDURE add_course(
    p_dept_code IN courses.dept_code%TYPE,
    p_course_number IN courses.course#%TYPE,
    p_title IN courses.title%TYPE
) AS
BEGIN
    INSERT INTO courses (dept_code, course#, title)
    VALUES (p_dept_code, p_course_number, p_title);
END add_course;

PROCEDURE delete_course(
    p_dept_code IN courses.dept_code%TYPE,
    p_course_number IN courses.course#%TYPE
) AS
BEGIN
    DELETE FROM courses
    WHERE dept_code = p_dept_code
    AND course# = p_course_number;
    
    DBMS_OUTPUT.PUT_LINE('Course deleted successfully.');
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Course not found.');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END delete_course;

PROCEDURE add_class(
    p_classid IN CHAR,
    p_dept_code IN VARCHAR2,
    p_course_num IN NUMBER,
    p_sect_num IN NUMBER,
    p_year IN NUMBER,
    p_semester IN VARCHAR2,
    p_limit IN NUMBER,
    p_class_size IN NUMBER,
    p_room IN VARCHAR2
)
IS
BEGIN
    -- Check if classid starts with 'c'
    IF p_classid NOT LIKE 'c%' THEN
        RAISE_APPLICATION_ERROR(-20001, 'Classid must start with "c".');
    END IF;
    
    -- Check if semester is valid
    IF p_semester NOT IN ('Spring', 'Fall', 'Summer 1', 'Summer 2', 'Winter') THEN
        RAISE_APPLICATION_ERROR(-20002, 'Invalid semester.');
    END IF;
    
    -- Check if class_size <= limit
    IF p_class_size > p_limit THEN
        RAISE_APPLICATION_ERROR(-20003, 'Class size exceeds limit.');
    END IF;
    
    -- Check if class_size constraint is satisfied
    IF NOT ((p_class_size >= 6 AND p_course_num >= 500) OR p_class_size >= 10) THEN
        RAISE_APPLICATION_ERROR(-20004, 'Class size constraint not satisfied.');
    END IF;
    
    -- Insert the class into the table
    INSERT INTO classes (classid, dept_code, course#, sect#, year, semester, limit, class_size, room) 
    VALUES (p_classid, p_dept_code, p_course_num, p_sect_num, p_year, p_semester, p_limit, p_class_size, p_room);
    
    COMMIT;
    
    DBMS_OUTPUT.PUT_LINE('Class added successfully.');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END add_class;



PROCEDURE delete_class(
    p_classId IN CHAR
) 
IS
BEGIN
    -- Delete the class with the given classId
    DELETE FROM classes WHERE classid = p_classId;

    -- Commit the transaction
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        -- Handle exceptions
        ROLLBACK;
        RAISE;
END delete_class;
	
END display_package;
/
show errors
