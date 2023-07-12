CREATE SCHEMA IF NOT EXISTS `assignment_1` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `assignment_1` ;

CREATE TABLE IF NOT EXISTS `assignment_1`.`university` (
  `University_id` INT NOT NULL,
  `Name` VARCHAR(255) NOT NULL,
  `Address` VARCHAR(244) NOT NULL,
  `Website` VARCHAR(255) NOT NULL,
  `Email` VARCHAR(244) NOT NULL,
  PRIMARY KEY (`University_id`));

CREATE TABLE IF NOT EXISTS `assignment_1`.`campus` (
  `Name` VARCHAR(255) NOT NULL,
  `Location` VARCHAR(255) NOT NULL,
  `Facilities` VARCHAR(255) NOT NULL,
  `Services` VARCHAR(255) NOT NULL,
  `university_University_id` INT NOT NULL,
  PRIMARY KEY (`university_University_id`),
  CONSTRAINT `fk_campus_university1`
    FOREIGN KEY (`university_University_id`)
    REFERENCES `assignment_1`.`university` (`University_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `assignment_1`.`faculty` (
  `Name` VARCHAR(255) NOT NULL,
  `Faculty_id` INT NOT NULL,
  `Dean` VARCHAR(255) NOT NULL,
  `Email` VARCHAR(255) NOT NULL,
  `Departments` VARCHAR(255) NOT NULL,
  `Faculty_Members` INT NOT NULL,
  `university_University_id` INT NOT NULL,
  PRIMARY KEY (`Faculty_id`, `university_University_id`),
  INDEX `fk_faculty_university1_idx` (`university_University_id` ASC) VISIBLE,
  CONSTRAINT `fk_faculty_university1`
    FOREIGN KEY (`university_University_id`)
    REFERENCES `assignment_1`.`university` (`University_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `assignment_1`.`department` (
  `Name` VARCHAR(255) NOT NULL,
  `HOD` VARCHAR(255) NOT NULL,
  `Faculty_members` INT NOT NULL,
  `Programs` VARCHAR(255) NOT NULL,
  `Email` VARCHAR(255) NOT NULL,
  `Phone_number` INT NOT NULL,
  `departmentId` VARCHAR(45) NOT NULL,
  `course_CourseId` VARCHAR(45) NOT NULL,
  `faculty_Faculty_id` INT NOT NULL,
  `faculty_university_University_id` INT NOT NULL,
  PRIMARY KEY (`departmentId`, `course_CourseId`, `faculty_Faculty_id`, `faculty_university_University_id`),
  INDEX `fk_department_faculty1_idx` (`faculty_Faculty_id` ASC, `faculty_university_University_id` ASC) VISIBLE,
  CONSTRAINT `fk_department_faculty1`
    FOREIGN KEY (`faculty_Faculty_id` , `faculty_university_University_id`)
    REFERENCES `assignment_1`.`faculty` (`Faculty_id` , `university_University_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
CREATE TABLE IF NOT EXISTS `assignment_1`.`course` (
  `Title` VARCHAR(255) NOT NULL,
  `Description` VARCHAR(255) NULL DEFAULT NULL,
  `Total_Credit` FLOAT NOT NULL,
  `Prerequisites` VARCHAR(255) NOT NULL,
  `Instructor` VARCHAR(255) NOT NULL,
  `Schedule` VARCHAR(255) NOT NULL,
  `CourseId` VARCHAR(45) NOT NULL,
  `department_Id` VARCHAR(45) NULL,
  `department_departmentId` VARCHAR(45) NOT NULL,
  `department_course_CourseId` VARCHAR(45) NOT NULL,
  `faculty_Faculty_id` INT NOT NULL,
  PRIMARY KEY (`CourseId`, `department_departmentId`, `department_course_CourseId`, `faculty_Faculty_id`),
  INDEX `fk_course_department1_idx` (`department_departmentId` ASC, `department_course_CourseId` ASC) VISIBLE,
  INDEX `fk_course_faculty1_idx` (`faculty_Faculty_id` ASC) VISIBLE,
  CONSTRAINT `fk_course_department1`
    FOREIGN KEY (`department_departmentId` , `department_course_CourseId`)
    REFERENCES `assignment_1`.`department` (`departmentId` , `course_CourseId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_course_faculty1`
    FOREIGN KEY (`faculty_Faculty_id`)
    REFERENCES `assignment_1`.`faculty` (`Faculty_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `assignment_1`.`events` (
  `Name` VARCHAR(255) NOT NULL,
  `Date` DATE NOT NULL,
  `Location` VARCHAR(255) NOT NULL,
  `Description` VARCHAR(255) NOT NULL,
  `Email` VARCHAR(255) NULL DEFAULT NULL,
  `Phone_number` INT NOT NULL,
  `campus_university_University_id` INT NOT NULL,
  `department_departmentId` VARCHAR(45) NOT NULL,
  `department_course_CourseId` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`campus_university_University_id`, `department_departmentId`, `department_course_CourseId`),
  INDEX `fk_events_department1_idx` (`department_departmentId` ASC, `department_course_CourseId` ASC) VISIBLE,
  CONSTRAINT `fk_events_campus1`
    FOREIGN KEY (`campus_university_University_id`)
    REFERENCES `assignment_1`.`campus` (`university_University_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_events_department1`
    FOREIGN KEY (`department_departmentId` , `department_course_CourseId`)
    REFERENCES `assignment_1`.`department` (`departmentId` , `course_CourseId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `assignment_1`.`library` (
  `Name` VARCHAR(255) NOT NULL,
  `Address` VARCHAR(255) NOT NULL,
  `Website` VARCHAR(255) NULL DEFAULT NULL,
  `Email` VARCHAR(255) NOT NULL,
  `Contact` INT NOT NULL,
  `Number_of_Branches` INT NOT NULL,
  `campus_university_University_id` INT NOT NULL,
  PRIMARY KEY (`campus_university_University_id`),
  CONSTRAINT `fk_library_campus1`
    FOREIGN KEY (`campus_university_University_id`)
    REFERENCES `assignment_1`.`campus` (`university_University_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `assignment_1`.`program` (
  `Name` VARCHAR(255) NOT NULL,
  `Type` VARCHAR(255) NOT NULL,
  `level` VARCHAR(255) NOT NULL,
  `Duration` VARCHAR(255) NOT NULL,
  `Faculty` VARCHAR(255) NOT NULL,
  `Program_id` VARCHAR(255) NOT NULL,
  `department_departmentId` VARCHAR(45) NOT NULL,
  `department_course_CourseId` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Program_id`, `department_departmentId`, `department_course_CourseId`),
  INDEX `fk_program_department1_idx` (`department_departmentId` ASC, `department_course_CourseId` ASC) VISIBLE,
  CONSTRAINT `fk_program_department1`
    FOREIGN KEY (`department_departmentId` , `department_course_CourseId`)
    REFERENCES `assignment_1`.`department` (`departmentId` , `course_CourseId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE IF NOT EXISTS `assignment_1`.`research_group` (
  `Name` VARCHAR(255) NOT NULL,
  `Head_of_Research_group` VARCHAR(255) NOT NULL,
  `Research_Area` VARCHAR(255) NOT NULL,
  `Staff` VARCHAR(255) NOT NULL,
  `Email` VARCHAR(255) NOT NULL,
  `Phone_number` INT NOT NULL,
  `department_departmentId` VARCHAR(45) NOT NULL,
  `department_course_CourseId` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`department_departmentId`, `department_course_CourseId`),
  CONSTRAINT `fk_research_group_department1`
    FOREIGN KEY (`department_departmentId` , `department_course_CourseId`)
    REFERENCES `assignment_1`.`department` (`departmentId` , `course_CourseId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `assignment_1`.`publication` (
  `Title` VARCHAR(255) NOT NULL,
  `Author` VARCHAR(255) NOT NULL,
  `Publication_Date` DATE NOT NULL,
  `Publication_Venue` VARCHAR(255) NOT NULL,
  `department_departmentId` VARCHAR(45) NOT NULL,
  `department_course_CourseId` VARCHAR(45) NOT NULL,
  `research_group_department_departmentId` VARCHAR(45) NOT NULL,
  `research_group_department_course_CourseId` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`department_departmentId`, `department_course_CourseId`, `research_group_department_departmentId`, `research_group_department_course_CourseId`),
  INDEX `fk_publication_research_group1_idx` (`research_group_department_departmentId` ASC, `research_group_department_course_CourseId` ASC) VISIBLE,
  CONSTRAINT `fk_publication_department1`
    FOREIGN KEY (`department_departmentId` , `department_course_CourseId`)
    REFERENCES `assignment_1`.`department` (`departmentId` , `course_CourseId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_publication_research_group1`
    FOREIGN KEY (`research_group_department_departmentId` , `research_group_department_course_CourseId`)
    REFERENCES `assignment_1`.`research_group` (`department_departmentId` , `department_course_CourseId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `assignment_1`.`scholarship` (
  `Name` VARCHAR(255) NOT NULL,
  `Amount` FLOAT NOT NULL,
  `Eligibility_criteria` VARCHAR(255) NOT NULL,
  `Application_Deadline` DATE NOT NULL,
  `department_departmentId` VARCHAR(45) NOT NULL,
  `department_course_CourseId` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`department_departmentId`, `department_course_CourseId`),
  CONSTRAINT `fk_scholarship_department1`
    FOREIGN KEY (`department_departmentId` , `department_course_CourseId`)
    REFERENCES `assignment_1`.`department` (`departmentId` , `course_CourseId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `assignment_1`.`staff` (
  `Name` VARCHAR(255) NOT NULL,
  `Position` VARCHAR(255) NOT NULL,
  `Department` VARCHAR(255) NOT NULL,
  `Email` VARCHAR(255) NOT NULL,
  `department_departmentId` VARCHAR(45) NOT NULL,
  `department_course_CourseId` VARCHAR(45) NOT NULL,
  `campus_university_University_id` INT NOT NULL,
  PRIMARY KEY (`department_departmentId`, `department_course_CourseId`, `campus_university_University_id`),
  INDEX `fk_staff_campus1_idx` (`campus_university_University_id` ASC) VISIBLE,
  CONSTRAINT `fk_staff_department1`
    FOREIGN KEY (`department_departmentId` , `department_course_CourseId`)
    REFERENCES `assignment_1`.`department` (`departmentId` , `course_CourseId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_staff_campus1`
    FOREIGN KEY (`campus_university_University_id`)
    REFERENCES `assignment_1`.`campus` (`university_University_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

