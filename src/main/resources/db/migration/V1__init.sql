CREATE TABLE course(
  course_id bigint NOT NULL AUTO_INCREMENT,
  course_name varchar(255) DEFAULT NULL,
  num_of_registered_students int NOT NULL,
  PRIMARY KEY (course_id)
);
CREATE TABLE student (
  student_id varchar(255) NOT NULL,
  email varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  num_of_registered_courses int NOT NULL,
  password varchar(255) DEFAULT NULL,
  surname varchar(255) DEFAULT NULL,
  PRIMARY KEY (student_id)
  );
CREATE TABLE student_course_set (
  student_id varchar(255) NOT NULL,
  course_id bigint NOT NULL,
  PRIMARY KEY (student_id,course_id),
  KEY FKsyuml99l3hkd4o8ayrllk87af (course_id),
  CONSTRAINT FKld9y7b2gywux329dkfw11x6rs FOREIGN KEY (student_id) REFERENCES student (student_id),
  CONSTRAINT FKsyuml99l3hkd4o8ayrllk87af FOREIGN KEY (course_id) REFERENCES course (course_id)
  )