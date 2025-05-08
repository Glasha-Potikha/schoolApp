set search_path to public;
SELECT *
FROM student;
select * from student where age between 10 and 20;
select name from student;
select * from student where name ILIKE'%О%';
select * from student where age < 15;
select * from student order by age;