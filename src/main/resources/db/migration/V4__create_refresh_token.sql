CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,

    token_hash VARCHAR(255) NOT NULL UNIQUE,

    user_id INTEGER NOT NULL,

    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);