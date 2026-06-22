CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    role VARCHAR(20)
);

CREATE TABLE approval_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    signoff_persons TEXT,
    main_category VARCHAR(50),
    sub_category VARCHAR(50),
    content TEXT,
    assignee_id BIGINT,
    creator_id BIGINT,
    current_step VARCHAR(20) NOT NULL,
    comment VARCHAR(255),
    FOREIGN KEY (assignee_id) REFERENCES users(id),
    FOREIGN KEY (creator_id) REFERENCES users(id)
);

CREATE TABLE approval_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id BIGINT,
    operator_id BIGINT,
    change_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    field_name VARCHAR(50),
    old_value TEXT,
    new_value TEXT,
    FOREIGN KEY (request_id) REFERENCES approval_requests(id),
    FOREIGN KEY (operator_id) REFERENCES users(id)
);

-- Initial Data
-- password is encrypted by BCrypt
-- https://www.esunbank.com/zh-tw/about/faq/content?q=credit_card/054
INSERT INTO users (username, password, full_name, role) VALUES ('admin', '$2a$10$8.V6j5V5f.R1B1yG9v1vP.u/6S1kU7m7S7W7R7S7W7R7S7W7R7S7W', 'Administrator', 'ADMIN');
INSERT INTO users (username, password, full_name, role) VALUES ('user1', '$2a$10$8.V6j5V5f.R1B1yG9v1vP.u/6S1kU7m7S7W7R7S7W7R7S7W7R7S7W', 'User One', 'USER');
INSERT INTO users (username, password, full_name, role) VALUES ('manager1', '$2a$10$8.V6j5V5f.R1B1yG9v1vP.u/6S1kU7m7S7W7R7S7W7R7S7W7R7S7W', 'Manager One', 'MANAGER');
INSERT INTO users (username, password, full_name, role) VALUES ('manager2', '$2a$10$8.V6j5V5f.R1B1yG9v1vP.u/6S1kU7m7S7W7R7S7W7R7S7W7R7S7W', 'Manager Two', 'MANAGER');
INSERT INTO users (username, password, full_name, role) VALUES ('bala', '$2a$10$OhVsV7NxxQbGQcea7iHO4ee4b5eYlS7yu6EEi5r/gaWYMSQ013Kbe', 'Administrator', 'ADMIN');
INSERT INTO users (username, password, full_name, role) VALUES ('gama', '$2a$10$OhVsV7NxxQbGQcea7iHO4ee4b5eYlS7yu6EEi5r/gaWYMSQ013Kbe', 'Administrator', 'ADMIN');
INSERT INTO users (username, password, full_name, role) VALUES ('laiya', '$2a$10$OhVsV7NxxQbGQcea7iHO4ee4b5eYlS7yu6EEi5r/gaWYMSQ013Kbe', 'Administrator', 'ADMIN');
