INSERT INTO USER_ACCOUNT(id, first_name, last_name, email, password, disabled_account) VALUES (1, 'Nauczyciel', 'Kowalski', 'nauczycielkowalski@mymoodleapp.pl', '$2a$12$l966.69rZuRGr3qOYFRnr.aBiTMy619MyLDMgNGBWsWpcfmMTJ0Ha', 0);
INSERT INTO USER_ACCOUNT(id, first_name, last_name, email, password, disabled_account) VALUES (2, 'Admin', 'Kowalski', 'adminkowalski@mymoodleapp.pl', '$2a$12$Y7alLBzB.gQ.g..DYdWviOTHevaTx7k2n3eZX8lzQKB5EhDhofScu', 0);
INSERT INTO USER_ROLE(USER_ACCOUNT_ID, ROLE) VALUES (1,'TEACHER');
INSERT INTO USER_ROLE(USER_ACCOUNT_ID, ROLE) VALUES (2,'ADMIN');

INSERT INTO USER_ACCOUNT (id, first_name, last_name, email, password, disabled_account) VALUES (3, 'Krzysztof', 'Wójcik', 'krzysztof@wojcik.pl', '$2a$12$1z/qXuIP9CkvfviOcQOqIeTSDKeA0SnN36lOdY29QShKig0FnMqYS', 0);
INSERT INTO USER_ROLE(USER_ACCOUNT_ID, ROLE) VALUES (3,'STUDENT');
INSERT INTO USER_ACCOUNT (id, first_name, last_name, email, password, disabled_account) VALUES (4, 'Barbara', 'Kowalczyk', 'barbara@kowalczyk.pl', '$2a$12$bhgVfFoR.5YWttXWAkDiYeHu6S.oGgoxRr0fXizT2rbDSay5mRwMC', 0);
INSERT INTO USER_ROLE(USER_ACCOUNT_ID, ROLE) VALUES (4,'STUDENT');
INSERT INTO USER_ACCOUNT (id, first_name, last_name, email, password, disabled_account) VALUES (5, 'Piotr', 'Lewandowski', 'piotr@lewandowski.pl', '$2a$12$FshmGXPf6gZAJq8xveaewOYOmdvzle3g2Xxq2XfE6KLPqWorhPvAO', 0);
INSERT INTO USER_ROLE(USER_ACCOUNT_ID, ROLE) VALUES (5,'STUDENT');
INSERT INTO USER_ACCOUNT (id, first_name, last_name, email, password, disabled_account) VALUES (6 ,'Jan','Kowalski','janko@walksi.pl','$2a$12$sRtxw0J.0xgvzZLuYUmGKujkzj/y25YG9Iw94z585xCQf6liTgixi', 0);
INSERT INTO USER_ROLE(USER_ACCOUNT_ID, ROLE) VALUES (6,'STUDENT');
INSERT INTO USER_ACCOUNT (id, first_name, last_name, email, password, disabled_account) VALUES (7, 'Anna', 'Nowak', 'anna@nowak.pl', '$2a$12$7JcVsDkBms70UZ/6icSeNOAuNuOyHjJKv1bkmj7rvVRCVdFl5hREW', 1);
INSERT INTO USER_ROLE(USER_ACCOUNT_ID, ROLE) VALUES (7,'STUDENT');

INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED, owner_id, access_code) VALUES(1, 'Fizyka', 'Kurs z fizyki - poziom podstawowy', 0, 1, 'Sa342S@ykU');
INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED, owner_id, access_code) VALUES(2, 'Matematyka', 'Kurs z matematyki - poziom podstawowy', 1, 1, 'zoI78ddS_25');
INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED, owner_id, access_code) VALUES(3, 'Biologia', 'Kurs z bilogii - poziom rozszerzony', 0, 1, '12kypoWsxj');
INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED, owner_id, access_code) VALUES(4, 'Chemia', 'Kurs z chemii - poziom rozszerzony', 0, 1, 'PsSKt221LX');
INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED, owner_id, access_code) VALUES(5, 'Informatyka', 'Kurs z informatyki - poziom podstawowy', 1, 1, 'X8uIddQsf');

INSERT INTO STUDENT_COURSES(student_id, courses_as_student_id) VALUES (3, 1);
INSERT INTO STUDENT_COURSES(student_id, courses_as_student_id) VALUES (4, 1);
INSERT INTO STUDENT_COURSES(student_id, courses_as_student_id) VALUES (5, 1);

INSERT INTO LESSON VALUES(1, 'Lekcja numer 1', 1);
INSERT INTO LESSON VALUES(2, 'Lekcja numer 2', 1);
INSERT INTO LESSON VALUES(3, 'Lekcja numer 3', 1);
INSERT INTO LESSON VALUES(4, 'Lekcja numer 4', 1);
INSERT INTO LESSON VALUES(5, 'Lekcja numer 5', 1);
INSERT INTO LESSON VALUES(6, 'Lekcja numer 6', 1);

INSERT INTO CALENDAR_EVENT(ID, DAY, MONTH, YEAR, HOUR, MINUTES, DESCRIPTION, TYPE, LESSON_ID)
            VALUES(1, 26, 7, 2023, 10, 15, 'Test podsumowujący pierwszy rok nauki', 'START_EXAM', 6);

INSERT INTO CALENDAR_EVENT(ID, DAY, MONTH, YEAR, HOUR, MINUTES, DESCRIPTION, TYPE, LESSON_ID)
            VALUES(2, 23, 5, 2023, 9, 30, 'Wycieczka do ZOO - zbiórka', 'OTHER', 5);
INSERT INTO CALENDAR_EVENT(ID, DAY, MONTH, YEAR, HOUR, MINUTES, DESCRIPTION, TYPE, LESSON_ID)
            VALUES(4, 23, 5, 2023, 18, 30, 'Powrót z ZOO', 'OTHER', 5);

INSERT INTO CALENDAR_EVENT(ID, DAY, MONTH, YEAR, HOUR, MINUTES, DESCRIPTION, TYPE, LESSON_ID)
            VALUES(3, 24, 5, 2023, 16, 30, 'Wywiadówka', 'OTHER', 4);


Insert into Exam (id, name, lesson_id, start_date, end_date, max_minutes) values (1, 'Test zaliczeniowy - semestr 1', 1, '2023-10-01 10:30:00',
                                               '2024-10-15 11:30:00' , 45);

Insert into Question (id, content, points, exam_id, question_type) values (1, 'Ile to 2+3?',1,1, 'ONE_CHOICE');

Insert into Answer (id, content, correct, question_id) values (1,'2',0,1);
Insert into Answer (id, content, correct, question_id) values (2,'4',0,1);
Insert into Answer (id, content, correct, question_id) values (3,'-2',0,1);
Insert into Answer (id, content, correct, question_id) values (4,'5',1,1);

Insert into Question (id, content, points, exam_id, question_type) values (2, 'Ile to 2+9?',1,1, 'MULTI_CHOICE');

Insert into Answer (id, content, correct, question_id) values (5,'11',1,2);
Insert into Answer (id, content, correct, question_id) values (6,'6',0,2);
Insert into Answer (id, content, correct, question_id) values (7,'3+8',1,2);
Insert into Answer (id, content, correct, question_id) values (8,'5',0,2);

Insert into Question (id, content, points, exam_id, question_type) values (3, 'Ile to 6+12?',5,1, 'OPEN');

Insert into Material (id, name, description, type, filename, lesson_id) values (1, 'Materiał 1', '', 'TXT', 'zadania.txt', 1);
Insert into Material (id, name, description, type, filename, lesson_id) values (2, 'Materiał 2', '', 'PDF', 'zadania.pdf', 1);
Insert into Material (id, name, description, type, filename, lesson_id) values (3, 'Materiał 3', '', 'TXT', 'zadania.txt', 1);
Insert into Material (id, name, description, type, filename, lesson_id) values (4, 'Materiał 4', 'Materiały z lekcji', 'TXT', 'zadania.txt', 1);

INSERT INTO Task (id, description, end_date, lesson_id) VALUES (1, 'Wykonaj zadania zamieszczone w materiałach 4, 5, 6', '2023-10-04',  1);
INSERT INTO Task (id, description, end_date, lesson_id) VALUES (2, 'Przygotuj prezentację na temat historii programowania', '2023-03-05',  1);
INSERT INTO Task (id, description, end_date, lesson_id) VALUES (3, 'Zaimplementuj algorytm sortowania bąbelkowego', '2023-03-06',  1);
INSERT INTO Task (id, description, end_date, lesson_id) VALUES (4, 'Przeczytaj rozdział 7 z podręcznika', '2023-04-20',  1);
INSERT INTO Task (id, description, end_date, lesson_id) VALUES (5, 'Rozwiąż ćwiczenia z programowania dynamicznego', '2023-08-23',  1);
INSERT INTO Task (id, description, end_date, lesson_id) VALUES (6, 'Zaprojektuj interfejs użytkownika dla aplikacji mobilnej', '2024-01-25',  1);

INSERT INTO GRADE (id, category, value, comment, task_owner_id, student_id, lesson_id) VALUES (1, 'Zadanie', 5, 'Wszystko poprawnie rozwiązane!', null, 3, 1);
INSERT INTO GRADE (id, category, value, comment, task_owner_id, student_id, lesson_id) VALUES (2, 'Odpowiedź ustna', 5, 'Poprawna i składna odpowiedź!', null, 3, 1);

INSERT INTO courses_grades (grade_id, course_id) VALUES(1, 1);
INSERT INTO courses_grades (grade_id, course_id) VALUES(2, 1);

