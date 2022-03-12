DROP TABLE IF EXISTS PAIR;
CREATE TABLE PAIR (
    ID UUID PRIMARY KEY
);

DROP TABLE IF EXISTS USER;
CREATE TABLE USER (
    ID UUID PRIMARY KEY,
    USER_NAME VARCHAR,
    USER_EMAIL VARCHAR,
    PAIR_ID UUID,
    FOREIGN KEY (PAIR_ID) REFERENCES PAIR(ID)
);
