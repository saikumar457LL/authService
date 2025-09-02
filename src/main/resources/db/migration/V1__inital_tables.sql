
CREATE SCHEMA IF NOT EXISTS authentication;

CREATE TABLE roles
(
    id            BIGSERIAL PRIMARY KEY,
    role_name     VARCHAR(255) NOT NULL,
    created_date  TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT uc_roles_rolename UNIQUE (role_name)
);

CREATE TABLE users
(
    id                      SERIAL PRIMARY KEY,
    username                VARCHAR(255) NOT NULL,
    password                VARCHAR(255) NOT NULL,
    email                   VARCHAR(255) NOT NULL,
    enabled                 BOOLEAN NOT NULL,
    account_non_expired     BOOLEAN NOT NULL,
    account_non_locked      BOOLEAN NOT NULL,
    credentials_non_expired BOOLEAN NOT NULL,
    CONSTRAINT uc_user_username UNIQUE (username),
    CONSTRAINT uc_user_email UNIQUE (email)
);

CREATE TABLE user_roles
(
    id            SERIAL PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    role_id       BIGINT NOT NULL,
    created_date  TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    CONSTRAINT uc_user_roles UNIQUE (user_id, role_id) -- prevent duplicate mappings
);
