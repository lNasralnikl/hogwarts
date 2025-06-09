-- liquibase formatted sql

-- changeset aydar:1
CREATE INDEX faculty_color_index ON faculty USING GIST (color)