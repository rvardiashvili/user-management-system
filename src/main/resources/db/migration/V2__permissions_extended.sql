
CREATE TABLE company_person_permissions (
    person_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    company_id BIGINT NOT NULL,
    PRIMARY KEY (person_id, permission_id, company_id),
    FOREIGN KEY (person_id) REFERENCES persons(person_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE,
    FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE
);

CREATE TABLE organisation_person_permissions (
    person_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    organisation_id BIGINT NOT NULL,
    PRIMARY KEY (person_id, permission_id, organisation_id),
    FOREIGN KEY (person_id) REFERENCES persons(person_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE,
    FOREIGN KEY (organisation_id) REFERENCES organisations(organisation_id) ON DELETE CASCADE
);

CREATE TABLE unit_person_permissions (
    person_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    unit_id BIGINT NOT NULL,
    PRIMARY KEY (person_id, permission_id, unit_id),
    FOREIGN KEY (person_id) REFERENCES persons(person_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE,
    FOREIGN KEY (unit_id) REFERENCES organisational_units(unit_id) ON DELETE CASCADE
);

CREATE TABLE permission_groups (
    group_id BIGINT NOT NULL AUTO_INCREMENT,
    group_name VARCHAR(128) NOT NULL UNIQUE,
    group_description VARCHAR(1024),
    PRIMARY KEY (group_id)
);

CREATE TABLE company_group(
    group_id BIGINT NOT NULL,
    company_id BIGINT NOT NULL,
    PRIMARY KEY (group_id, company_id),
    FOREIGN KEY (group_id) REFERENCES permission_groups(group_id),
    FOREIGN KEY (company_id) REFERENCES companies(company_id)
);
CREATE TABLE organisation_group(
    group_id BIGINT NOT NULL,
    organisation_id BIGINT NOT NULL,
    PRIMARY KEY (group_id, organisation_id),
    FOREIGN KEY (group_id) REFERENCES permission_groups(group_id),
    FOREIGN KEY (organisation_id) REFERENCES organisations(organisation_id)
);
CREATE TABLE unit_group(
    group_id BIGINT NOT NULL,
    unit_id BIGINT NOT NULL,
    PRIMARY KEY (group_id, unit_id),
    FOREIGN KEY (group_id) REFERENCES permission_groups(group_id),
    FOREIGN KEY (unit_id) REFERENCES organisational_units(unit_id)
);

CREATE TABLE group_permissions (
    group_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (group_id, permission_id),
    FOREIGN KEY (group_id) REFERENCES permission_groups(group_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES  permissions(permission_id) ON DELETE CASCADE
);

CREATE TABLE group_persons(
    group_id BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    PRIMARY KEY (group_id, person_id),
    FOREIGN KEY (group_id) REFERENCES permission_groups(group_id) ON DELETE CASCADE,
    FOREIGN KEY (person_id) REFERENCES  persons(person_id) ON DELETE CASCADE
);