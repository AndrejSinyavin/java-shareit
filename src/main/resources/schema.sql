CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uc_users_email UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255)                            NOT NULL,
    description VARCHAR(255)                            NOT NULL,
    available   BOOLEAN                                 NOT NULL,
    owner       BIGINT                                  NOT NULL,
    request     BIGINT,
    CONSTRAINT pk_items PRIMARY KEY (id),
    CONSTRAINT fk_items_on_owner FOREIGN KEY (owner) REFERENCES users (id)
);