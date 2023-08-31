INSERT INTO TEACHER (ID, NAME,SURNAME,EMAIL, PESEL ) VALUES (1 ,'Jan','Kowalski','janko@walksi.pl','98282820110');
INSERT INTO TEACHER (ID, NAME,SURNAME,EMAIL, PESEL ) VALUES (2 ,'Jan1','Kowalski1','janko1@walksi.pl','98282820111');
INSERT INTO TEACHER (ID, NAME,SURNAME,EMAIL, PESEL ) VALUES (3 ,'Jan2','Kowalski2','janko2@walksi.pl','98282820112');
INSERT INTO TEACHER (ID, NAME,SURNAME,EMAIL, PESEL ) VALUES (4 ,'Jan3','Kowalski3','janko3@walksi.pl','98282820113');

INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED, TEACHER_ID) VALUES(1, 'Fizyka', 'Kurs z fizyki - poziom podstawowy', 1, 1);
INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED, TEACHER_ID) VALUES(2, 'Matematyka', 'Kurs z matematyki - poziom podstawowy', 1, 1);
INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED, TEACHER_ID) VALUES(3, 'Biologia', 'Kurs z bilogii - poziom rozszerzony', 0, 1);
INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED, TEACHER_ID) VALUES(4, 'Chemia', 'Kurs z chemii - poziom rozszerzony', 0, 1);
INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED, TEACHER_ID) VALUES(5, 'Informatyka', 'Kurs z informatyki - poziom podstawowy', 1, 1);

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


Insert into Exam (id, name, course_id, start_date, end_date, max_minutes) values (1, 'Test zaliczeniowy - semestr 1', 1, '2023-10-15 10:30:00',
                                               '2023-10-15 11:30:00' , 45);

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

Insert into Material (id, name, description, type, filename, lesson_id) values (1, 'Materiał 1', 'm1', 'TXT', 'zadania.txt', 1);
Insert into Material (id, name, description, type, filename, lesson_id) values (2, 'Materiał 2', 'm1', 'PDF', 'zadania.pdf', 1);
Insert into Material (id, name, description, type, filename, lesson_id) values (3, 'Materiał 3', 'm1', 'TXT', 'zadania.txt', 1);
Insert into Material (id, name, description, type, filename, lesson_id) values (4, 'Materiał 4', 'm1', 'TXT', 'zadania.txt', 1);


INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (1 ,'Jan','Kowalski','janko@walksi.pl','98282820110');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (2, 'Anna', 'Nowak', 'anna@nowak.pl', '92020210123');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (3, 'Krzysztof', 'Wójcik', 'krzysztof@wojcik.pl', '88051551234');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (4, 'Barbara', 'Kowalczyk', 'barbara@kowalczyk.pl', '91010170345');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (5, 'Piotr', 'Lewandowski', 'piotr@lewandowski.pl', '95021580456');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (6, 'Magdalena', 'Dąbrowska', 'magda@dabrowska.pl', '89080810567');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (7, 'Kamil', 'Zieliński', 'kamil@zielinski.pl', '93030330678');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (8, 'Agnieszka', 'Szymańska', 'agnieszka@szymanska.pl', '97070740789');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (9, 'Mateusz', 'Woźniak', 'mateusz@wozniak.pl', '96060650890');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (10, 'Katarzyna', 'Kamińska', 'katarzyna@kaminska.pl', '94041460901');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (11, 'Michał', 'Lis', 'michal@lis.pl', '90090971012');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (12, 'Joanna', 'Piotrowska', 'joanna@piotrowska.pl', '86030381123');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (13, 'Andrzej', 'Grabowski', 'andrzej@grabowski.pl', '93020291234');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (14, 'Monika', 'Jaworska', 'monika@jaworska.pl', '92010101345');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (15, 'Łukasz', 'Nowicki', 'lukasz@nowicki.pl', '91050511456');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (16, 'Karolina', 'Malinowska', 'karolina@malinowska.pl', '96060621567');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (17, 'Tomasz', 'Oleksy', 'tomasz@oleksy.pl', '94030331678');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (18, 'Ewa', 'Stępień', 'ewa@stepien.pl', '90080841789');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (19, 'Marcin', 'Zawadzki', 'marcin@zawadzki.pl', '87070751890');
INSERT INTO STUDENT (ID, NAME, SURNAME, EMAIL, PESEL) VALUES (20, 'Natalia', 'Krawczyk', 'natalia@krawczyk.pl', '95060661901');

INSERT INTO Task (id, description, end_date, lesson_id) VALUES (1, 'Wykonaj zadania zamieszczone w materiałach 4, 5, 6', '2023-10-04',  1);
INSERT INTO Task (id, description, end_date, lesson_id) VALUES (2, 'Przygotuj prezentację na temat historii programowania', '2023-03-05',  1);
INSERT INTO Task (id, description, end_date, lesson_id) VALUES (3, 'Zaimplementuj algorytm sortowania bąbelkowego', '2023-03-06',  1);
INSERT INTO Task (id, description, end_date, lesson_id) VALUES (4, 'Przeczytaj rozdział 7 z podręcznika', '2023-04-20',  1);
INSERT INTO Task (id, description, end_date, lesson_id) VALUES (5, 'Rozwiąż ćwiczenia z programowania dynamicznego', '2023-08-23',  1);
INSERT INTO Task (id, description, end_date, lesson_id) VALUES (6, 'Zaprojektuj interfejs użytkownika dla aplikacji mobilnej', '2024-01-25',  1);

INSERT INTO Task_Student (id, status, task_id, student_id, filename) VALUES (1, 'OCENIONE', 1, 1, 'rozwiazanie.txt');
INSERT INTO GRADE (id, category, value, comment, task_student_id) VALUES (1, 'Zadanie', 5, 'Wszystko poprawnie rozwiązane!', 1);

INSERT INTO Task_Student (id, status, task_id, student_id, filename) VALUES (2, 'WYKONANE', 2, 1, 'kowalski.zip');
INSERT INTO Task_Student (id, status, task_id, student_id, filename) VALUES (3, 'OCENIONE', 3, 1, 'rozwiazanie.pdf');
INSERT INTO Task_Student (id, status, task_id, student_id, filename) VALUES (4, 'OCENIONE', 4, 1, 'zadania.pdf');
INSERT INTO Task_Student (id, status, task_id, student_id, filename) VALUES (5, 'WYKONANE', 5, 1, 'kowalski2.zip');



