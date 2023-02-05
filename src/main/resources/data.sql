INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED) VALUES(1, 'Fizyka', 'Kurs z fizyki - poziom podstawowy', 1);
INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED) VALUES(2, 'Matematyka', 'Kurs z matematyki - poziom podstawowy', 1);
INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED) VALUES(3, 'Biologia', 'Kurs z bilogii - poziom rozszerzony', 0);
INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED) VALUES(4, 'Chemia', 'Kurs z chemii - poziom rozszerzony', 0);
INSERT INTO COURSE(ID, NAME, DESCRIPTION, FINISHED) VALUES(5, 'Informatyka', 'Kurs z informatyki - poziom podstawowy', 1);

INSERT INTO LESSON VALUES(1, 'Lekcja numer 1', 1);
INSERT INTO LESSON VALUES(2, 'Lekcja numer 2', 1);
INSERT INTO LESSON VALUES(3, 'Lekcja numer 3', 1);
INSERT INTO LESSON VALUES(4, 'Lekcja numer 4', 1);
INSERT INTO LESSON VALUES(5, 'Lekcja numer 5', 1);
INSERT INTO LESSON VALUES(6, 'Lekcja numer 6', 1);

Insert into Exam (id, name, course_id) values (1, 'Test zaliczeniowy - semestr 1', 1);

Insert into Question (id, content, points, exam_id) values (1, 'Ile to 2+3?',1,1);

Insert into Answer (id, content, correct, question_id) values (1,'2',0,1);
Insert into Answer (id, content, correct, question_id) values (2,'4',0,1);
Insert into Answer (id, content, correct, question_id) values (3,'-2',0,1);
Insert into Answer (id, content, correct, question_id) values (4,'5',1,1);