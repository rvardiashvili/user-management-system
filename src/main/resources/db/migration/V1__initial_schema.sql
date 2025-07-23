CREATE TABLE addresses (
    address_id BIGINT NOT NULL AUTO_INCREMENT,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(128) NOT NULL,
    state VARCHAR(128),
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(128) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY (address_id)
);

CREATE TABLE users (
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(128) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'active',
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_id),
    UNIQUE (email)
);

CREATE TABLE persons (
    person_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    first_name VARCHAR(128) NOT NULL,
    last_name VARCHAR(128) NOT NULL,
    phone_number VARCHAR(64),
    gender VARCHAR(8),
    date_of_birth DATE,
    created_at DATETIME NOT NULL DEFAULT NOW(),
    updated_at DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY (person_id),
    UNIQUE (user_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE positions (
    position_id BIGINT NOT NULL AUTO_INCREMENT,
    position_name VARCHAR(128) NOT NULL,
    description VARCHAR(1024),
    created_at DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY (position_id)
);

CREATE TABLE roles (
    role_id BIGINT NOT NULL AUTO_INCREMENT,
    role_name VARCHAR(128) NOT NULL,
    description VARCHAR(1024),
    PRIMARY KEY (role_id),
    UNIQUE (role_name)
);

CREATE TABLE permissions (
    permission_id BIGINT NOT NULL AUTO_INCREMENT,
    permission_name VARCHAR(128) NOT NULL,
    description VARCHAR(1024),
    PRIMARY KEY (permission_id),
    UNIQUE (permission_name)
);


CREATE TABLE person_addresses (
    person_id BIGINT NOT NULL,
    address_id BIGINT NOT NULL,
    PRIMARY KEY (person_id, address_id),
    FOREIGN KEY (person_id) REFERENCES persons(person_id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES addresses(address_id) ON DELETE CASCADE
);



CREATE TABLE person_positions (
    person_id BIGINT NOT NULL,
    position_id BIGINT NOT NULL,
    start_date DATE,
    end_date DATE,
    PRIMARY KEY (person_id, position_id),
    FOREIGN KEY (person_id) REFERENCES persons(person_id) ON DELETE CASCADE,
    FOREIGN KEY (position_id) REFERENCES positions(position_id) ON DELETE CASCADE
);

CREATE TABLE person_roles (
    person_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (person_id, role_id),
    FOREIGN KEY (person_id) REFERENCES persons(person_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE
);
