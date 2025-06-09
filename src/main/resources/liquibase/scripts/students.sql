-- liquibase formatted sql

-- changeset aydar:1
CREATE INDEX students_name_index ON student USING GIST (name)