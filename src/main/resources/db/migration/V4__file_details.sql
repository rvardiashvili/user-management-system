CREATE TABLE uploaded_files (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_file_name VARCHAR(255) NOT NULL,
    object_name VARCHAR(255) NOT NULL UNIQUE,
    bucket_name VARCHAR(100) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    content_type VARCHAR(100),
    upload_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);