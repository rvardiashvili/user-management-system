INSERT INTO permissions (permission_name, description) VALUES ("view-users", "allows to view all user data");
INSERT INTO permissions (permission_name, description) VALUES ("add-users", "allows to add users to company");
INSERT INTO permissions (permission_name, description) VALUES ("remove-users", "allows to remove users from company");
INSERT INTO permissions (permission_name, description) VALUES ("role", "allows to assign roles to user");
INSERT INTO permissions (permission_name, description) VALUES ("permission", "allows to assign permissions to user");
INSERT INTO permissions (permission_name, description) VALUES ("group", "allows to create permission groups");
INSERT INTO permissions (permission_name, description) VALUES ("position", "allows to assign position to users");
INSERT INTO permissions (permission_name, description) VALUES ("view-company", "allows user to view company");
INSERT INTO permissions (permission_name, description) VALUES ("edit-users", "allows user to edit details of all other users");
INSERT INTO permissions (permission_name, description) VALUES ("create-sub", "allows user to create subcategories");



INSERT INTO roles (role_name, description) VALUES ("admin", "grants all permissions");
INSERT INTO roles (role_name, description) VALUES ("moderator", "grants some permissions");
INSERT INTO roles (role_name, description) VALUES ("employee", "grants minimum permissions for an employee");
INSERT INTO roles (role_name, description) VALUES ("user", "default role");

INSERT INTO role_permissions VALUES (1, 1);
INSERT INTO role_permissions VALUES (1, 2);
INSERT INTO role_permissions VALUES (1, 3);
INSERT INTO role_permissions VALUES (1, 4);
INSERT INTO role_permissions VALUES (1, 5);
INSERT INTO role_permissions VALUES (1, 6);
INSERT INTO role_permissions VALUES (1, 7);
INSERT INTO role_permissions VALUES (1, 8);
INSERT INTO role_permissions VALUES (1, 9);
INSERT INTO role_permissions VALUES (1, 10);
INSERT INTO role_permissions VALUES (2, 1);
INSERT INTO role_permissions VALUES (2, 4);
INSERT INTO role_permissions VALUES (2, 6);
INSERT INTO role_permissions VALUES (2, 7);
INSERT INTO role_permissions VALUES (2, 8);
INSERT INTO role_permissions VALUES (2, 2);
INSERT INTO role_permissions VALUES (2, 3);
INSERT INTO role_permissions VALUES (3, 8);

