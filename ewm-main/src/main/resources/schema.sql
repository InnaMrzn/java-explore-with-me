DROP
    TABLE IF EXISTS REQUESTS;
DROP
    TABLE IF EXISTS EVENTS_COMPILATIONS;
DROP
    TABLE IF EXISTS EVENTS;
DROP
    TABLE IF EXISTS COMPILATIONS;
DROP
    TABLE IF EXISTS CATEGORIES;
DROP
    TABLE IF EXISTS USERS;
CREATE TABLE IF NOT EXISTS USERS (
                                     ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     NAME VARCHAR NOT NULL, EMAIL VARCHAR NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS CATEGORIES (
                                          ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                          NAME VARCHAR NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS COMPILATIONS (
                                            ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                            TITLE VARCHAR NOT NULL, PINNED BOOLEAN NOT NULL
);
CREATE TABLE IF NOT EXISTS EVENTS (
                                      ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                      TITLE VARCHAR NOT NULL,
                                      ANNOTATION VARCHAR NOT NULL,
                                      DESCRIPTION VARCHAR NOT NULL,
                                      CATEGORY_ID BIGINT NOT NULL,
                                      CREATED_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                      EVENT_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                      PUBLISHED_DATE TIMESTAMP WITHOUT TIME ZONE,
                                      INITIATOR_ID BIGINT NOT NULL,
                                      EVENT_STATE VARCHAR NOT NULL,
                                      PAID BOOLEAN NOT NULL,
                                      REQUEST_MODERATION BOOLEAN NOT NULL,
                                      PARTS_LIMIT INTEGER NOT NULL,
                                      LAT NUMERIC (8, 5),
                                      LON NUMERIC (8, 5),
                                      RADIUS INTEGER,
    CONSTRAINT fk_events_to_users FOREIGN KEY (INITIATOR_ID) REFERENCES USERS (ID) ON DELETE RESTRICT,
    CONSTRAINT fk_events_to_category FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORIES (ID) ON DELETE RESTRICT
    );

CREATE TABLE IF NOT EXISTS EVENTS_COMPILATIONS (
                                                   COMPILATION_ID BIGINT NOT NULL,
                                                   EVENT_ID BIGINT NOT NULL,
                                                   CONSTRAINT fk_compilation FOREIGN KEY (COMPILATION_ID) REFERENCES COMPILATIONS (ID) ON DELETE CASCADE,
    CONSTRAINT fk_event FOREIGN KEY (EVENT_ID) REFERENCES EVENTS (ID) ON DELETE CASCADE,
    PRIMARY KEY (COMPILATION_ID, EVENT_ID)
    );
CREATE TABLE IF NOT EXISTS REQUESTS (
                                        ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                        CREATED_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        EVENT_ID BIGINT NOT NULL,
                                        REQUESTER_ID BIGINT NOT NULL,
                                        REQUEST_STATUS VARCHAR NOT NULL,
                                        CONSTRAINT fk_request_to_users FOREIGN KEY (REQUESTER_ID) REFERENCES USERS (ID) ON DELETE RESTRICT,
    CONSTRAINT fk_request_to_event FOREIGN KEY (EVENT_ID) REFERENCES EVENTS (ID) ON DELETE RESTRICT
    );



