DROP SCHEMA IF EXISTS `hfri` ;
CREATE SCHEMA IF NOT EXISTS `hfri` DEFAULT CHARACTER SET utf8 ;
USE `hfri` ;


DROP TABLE IF EXISTS `hfri`.`addresses` ;

CREATE TABLE IF NOT EXISTS `hfri`.`addresses` (
  `address_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `number` INT UNSIGNED NOT NULL,
  `street` VARCHAR(45) NOT NULL,
  `city` VARCHAR(20) NOT NULL,
  `postal_code` VARCHAR(5) NOT NULL)
ENGINE = InnoDB;


DROP TABLE IF EXISTS `hfri`.`organisations` ;

CREATE TABLE IF NOT EXISTS `hfri`.`organisations` (
  `organisation_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `acronym` VARCHAR(20) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `type` ENUM('COLLEGE', 'RESEARCH_CENTER', 'COMPANY') NOT NULL,
  `address_id` INT UNSIGNED NOT NULL,
  `budget` INT UNSIGNED NOT NULL,
  UNIQUE INDEX `name_unique_index` (`name` ASC),
  INDEX `organisation_address_id_index` (`address_id` ASC),
  CONSTRAINT `organisation_address_id`
    FOREIGN KEY (`address_id`)
    REFERENCES `hfri`.`addresses` (`address_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


DROP TABLE IF EXISTS `hfri`.`executives` ;

CREATE TABLE IF NOT EXISTS `hfri`.`executives` (
  `executive_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(45) NOT NULL,
  UNIQUE INDEX `name_unique_index` (`name` ASC))
ENGINE = InnoDB;


DROP TABLE IF EXISTS `hfri`.`research_workers` ;

CREATE TABLE IF NOT EXISTS `hfri`.`research_workers` (
  `research_worker_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `organisation_id` INT UNSIGNED NOT NULL,
  `first_name` VARCHAR(20) NOT NULL,
  `last_name` VARCHAR(20) NOT NULL,
  `sex` ENUM('MALE', 'FEMALE') NOT NULL,
  `age` INT UNSIGNED NOT NULL,
  `birth_date` DATE NOT NULL,
  `join_date` DATE NOT NULL,
  INDEX `research_worker_organisation_id_index` (`organisation_id` ASC),
  CONSTRAINT `research_worker_organisation_id`
    FOREIGN KEY (`organisation_id`)
    REFERENCES `hfri`.`organisations` (`organisation_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


DROP TABLE IF EXISTS `hfri`.`projects` ;

CREATE TABLE IF NOT EXISTS `hfri`.`projects` (
  `project_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `title` VARCHAR(45) NOT NULL,
  `description` VARCHAR(120) NOT NULL,
  `start_date` DATE NOT NULL,
  `finish_date` DATE NOT NULL,
  `duration_years` INT GENERATED ALWAYS AS (TIMESTAMPDIFF(YEAR, start_date, finish_date)) VIRTUAL,
  `organisation_id` INT UNSIGNED NOT NULL,
  `executive_id` INT UNSIGNED NOT NULL,
  `supervisor_id` INT UNSIGNED NOT NULL,
  UNIQUE INDEX `title_unique_index` (`title` ASC),
  INDEX `project_organisation_id_index` (`organisation_id` ASC),
  INDEX `project_executive_id_index` (`executive_id` ASC),
  INDEX `project_supervisor_id_index` (`supervisor_id` ASC),
  CONSTRAINT `project_organisation_id`
    FOREIGN KEY (`organisation_id`)
    REFERENCES `hfri`.`organisations` (`organisation_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `project_executive_id`
    FOREIGN KEY (`executive_id`)
    REFERENCES `hfri`.`executives` (`executive_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `project_supervisor_id`
    FOREIGN KEY (`supervisor_id`)
    REFERENCES `hfri`.`research_workers` (`research_worker_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


DROP TABLE IF EXISTS `hfri`.`phone_numbers` ;

CREATE TABLE IF NOT EXISTS `hfri`.`phone_numbers` (
  `phone_number_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `organisation_id` INT UNSIGNED NOT NULL,
  `number` VARCHAR(15) NOT NULL,
  UNIQUE INDEX `phone_number_unique_index` (`number` ASC),
  INDEX `phone_number_organisation_id_index` (`organisation_id` ASC),
  CONSTRAINT `phone_number_organisation_id`
    FOREIGN KEY (`organisation_id`)
    REFERENCES `hfri`.`organisations` (`organisation_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


DROP TABLE IF EXISTS `hfri`.`programs` ;

CREATE TABLE IF NOT EXISTS `hfri`.`programs` (
  `program_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(45) NOT NULL,
  `address_id` INT UNSIGNED NOT NULL,
  INDEX `program_address_id_index` (`address_id` ASC),
  CONSTRAINT `program_address_id`
    FOREIGN KEY (`address_id`)
    REFERENCES `hfri`.`addresses` (`address_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


DROP TABLE IF EXISTS `hfri`.`research_fields` ;

CREATE TABLE IF NOT EXISTS `hfri`.`research_fields` (
  `research_field_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `description` VARCHAR(120) NOT NULL,
  UNIQUE INDEX `description_unique_index` (`description` ASC))
ENGINE = InnoDB;


DROP TABLE IF EXISTS `hfri`.`fundings` ;

CREATE TABLE IF NOT EXISTS `hfri`.`fundings` (
  `program_id` INT UNSIGNED NOT NULL,
  `project_id` INT UNSIGNED NOT NULL,
  `sum` INT UNSIGNED NOT NULL,
  INDEX `funding_program_id_index` (`program_id` ASC),
  INDEX `funding_project_id_index` (`project_id` ASC),
  UNIQUE INDEX `project_id_unique_index` (`project_id` ASC),
  PRIMARY KEY (`program_id`, `project_id`),
  CONSTRAINT `funding_program_id`
    FOREIGN KEY (`program_id`)
    REFERENCES `hfri`.`programs` (`program_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `funding_project_id`
    FOREIGN KEY (`project_id`)
    REFERENCES `hfri`.`projects` (`project_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


DROP TABLE IF EXISTS `hfri`.`reviews` ;

CREATE TABLE IF NOT EXISTS `hfri`.`reviews` (
  `project_id` INT UNSIGNED NOT NULL,
  `research_worker_id` INT UNSIGNED NOT NULL,
  `rating` ENUM('1', '2', '3', '4', '5', '6', '7', '8', '9', '10') NOT NULL,
  `date` DATE NOT NULL,
  INDEX `review_project_id_index` (`project_id` ASC),
  INDEX `review_research_worker_id_index` (`research_worker_id` ASC),
  PRIMARY KEY (`project_id`, `research_worker_id`),
  CONSTRAINT `review_project_id`
    FOREIGN KEY (`project_id`)
    REFERENCES `hfri`.`projects` (`project_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `review_research_worker_id`
    FOREIGN KEY (`research_worker_id`)
    REFERENCES `hfri`.`research_workers` (`research_worker_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


DROP TABLE IF EXISTS `hfri`.`commissions` ;

CREATE TABLE IF NOT EXISTS `hfri`.`commissions` (
  `commission_id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `project_id` INT UNSIGNED NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `description` VARCHAR(120) NOT NULL,
  `deadline` DATE NOT NULL,
  INDEX `commission_project_id_index` (`project_id` ASC),
  CONSTRAINT `commission_project_id`
    FOREIGN KEY (`project_id`)
    REFERENCES `hfri`.`projects` (`project_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


DROP TABLE IF EXISTS `hfri`.`project_research_fields` ;

CREATE TABLE IF NOT EXISTS `hfri`.`project_research_fields` (
  `project_id` INT UNSIGNED NOT NULL,
  `research_field_id` INT UNSIGNED NOT NULL,
  INDEX `project_research_field_research_field_id_index` (`research_field_id` ASC),
  INDEX `project_research_field_project_id_index` (`project_id` ASC),
  PRIMARY KEY (`project_id`, `research_field_id`),
  CONSTRAINT `project_research_field_project_id`
    FOREIGN KEY (`project_id`)
    REFERENCES `hfri`.`projects` (`project_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `project_research_field_research_field_id`
    FOREIGN KEY (`research_field_id`)
    REFERENCES `hfri`.`research_fields` (`research_field_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


DROP TABLE IF EXISTS `hfri`.`project_research_workers` ;

CREATE TABLE IF NOT EXISTS `hfri`.`project_research_workers` (
  `research_worker_id` INT UNSIGNED NOT NULL,
  `project_id` INT UNSIGNED NOT NULL,
  INDEX `project_research_worker_project_id_index` (`project_id` ASC),
  INDEX `project_research_worker_research_worker_id_index` (`research_worker_id` ASC),
  PRIMARY KEY (`research_worker_id`, `project_id`),
  CONSTRAINT `project_research_worker_research_worker_id`
    FOREIGN KEY (`research_worker_id`)
    REFERENCES `hfri`.`research_workers` (`research_worker_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  CONSTRAINT `project_research_worker_project_id`
    FOREIGN KEY (`project_id`)
    REFERENCES `hfri`.`projects` (`project_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;