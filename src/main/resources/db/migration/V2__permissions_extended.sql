CREATE TABLE person_permissions (
    person_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (person_id, permission_id),
    FOREIGN KEY (person_id) REFERENCES persons(person_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE
);

CREATE TABLE permission_groups (
    group_id BIGINT NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (group_id)
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