DROP TABLE IF EXISTS ENDPOINT_HITS;


CREATE TABLE IF NOT EXISTS ENDPOINT_HITS (ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                          APP VARCHAR NOT
                                          NULL,
                                          URI VARCHAR NOT
                                          NULL,
                                          IP VARCHAR NOT
                                          NULL,
                                          HIT_TIMESTAMP TIMESTAMP WITHOUT TIME ZONE);