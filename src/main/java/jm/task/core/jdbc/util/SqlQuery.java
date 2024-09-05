package jm.task.core.jdbc.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SqlQuery {
    CREATE_TABLE("""
            CREATE TABLE IF NOT EXISTS users
            (
               id         SERIAL PRIMARY KEY,
               first_name VARCHAR(20) NOT NULL,
               last_name  VARCHAR(20) NOT NULL,
               age        SMALLINT    NOT NULL
            )
            """),
    DROP_TABLE("DROP TABLE IF EXISTS users"),
    INSERT("INSERT INTO users (first_name, last_name, age) VALUES (?, ?, ?)"),
    SELECT_ALL_USERS("SELECT * FROM users"),
    DELETE_USER("DELETE FROM users WHERE id = ?"),
    TRUNCATE("TRUNCATE users");

    private final String key;
}
