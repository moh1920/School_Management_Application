-- sequences
CREATE SEQUENCE IF NOT EXISTS BATCH_JOB_SEQ START 1;
CREATE SEQUENCE IF NOT EXISTS BATCH_JOB_INSTANCE_SEQ START 1;
CREATE SEQUENCE IF NOT EXISTS BATCH_JOB_EXECUTION_SEQ START 1;
CREATE SEQUENCE IF NOT EXISTS BATCH_STEP_EXECUTION_SEQ START 1;

-- Job instance
CREATE TABLE IF NOT EXISTS BATCH_JOB_INSTANCE (
                                                  JOB_INSTANCE_ID bigint NOT NULL,
                                                  VERSION bigint,
                                                  JOB_NAME varchar(100) NOT NULL,
    JOB_KEY varchar(32) NOT NULL,
    CONSTRAINT JOB_INST_PK PRIMARY KEY (JOB_INSTANCE_ID)
    );

CREATE UNIQUE INDEX IF NOT EXISTS JOB_INST_UNIQUE_IDX ON BATCH_JOB_INSTANCE (JOB_NAME, JOB_KEY);

-- Job execution
CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION (
                                                   JOB_EXECUTION_ID bigint NOT NULL,
                                                   VERSION bigint,
                                                   JOB_INSTANCE_ID bigint NOT NULL,
                                                   CREATE_TIME timestamp,
                                                   START_TIME timestamp,
                                                   END_TIME timestamp,
                                                   STATUS varchar(10),
    EXIT_CODE varchar(2500),
    EXIT_MESSAGE varchar(2500),
    LAST_UPDATED timestamp,
    JOB_CONFIGURATION_LOCATION varchar(2500),
    CONSTRAINT JOB_EXEC_PK PRIMARY KEY (JOB_EXECUTION_ID)
    );

CREATE INDEX IF NOT EXISTS JOB_EXEC_INST_IDX ON BATCH_JOB_EXECUTION (JOB_INSTANCE_ID);

-- Job execution params
CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION_PARAMS (
                                                          JOB_EXECUTION_ID bigint NOT NULL,
                                                          PARAMETER_NAME varchar(100) NOT NULL,
    PARAMETER_TYPE varchar(255) NOT NULL,
    PARAMETER_VALUE text,
    IDENTIFYING CHAR(1) NOT NULL
    );

CREATE INDEX IF NOT EXISTS JOB_EXEC_PARAMS_IDX ON BATCH_JOB_EXECUTION_PARAMS (JOB_EXECUTION_ID);

-- Step execution
CREATE TABLE IF NOT EXISTS BATCH_STEP_EXECUTION (
                                                    STEP_EXECUTION_ID bigint NOT NULL,
                                                    VERSION bigint,
                                                    STEP_NAME varchar(100) NOT NULL,
    JOB_EXECUTION_ID bigint NOT NULL,
    START_TIME timestamp,
    END_TIME timestamp,
    STATUS varchar(10),
    COMMIT_COUNT bigint,
    READ_COUNT bigint,
    FILTER_COUNT bigint,
    WRITE_COUNT bigint,
    EXIT_CODE varchar(2500),
    EXIT_MESSAGE varchar(2500),
    READ_SKIP_COUNT bigint,
    WRITE_SKIP_COUNT bigint,
    PROCESS_SKIP_COUNT bigint,
    ROLLBACK_COUNT bigint,
    LAST_UPDATED timestamp,
    CREATE_TIME timestamp,
    CONSTRAINT STEP_EXEC_PK PRIMARY KEY (STEP_EXECUTION_ID)
    );

CREATE INDEX IF NOT EXISTS STEP_EXEC_JOB_EXEC_IDX ON BATCH_STEP_EXECUTION (JOB_EXECUTION_ID);
CREATE INDEX IF NOT EXISTS STEP_EXEC_STEP_NAME_IDX ON BATCH_STEP_EXECUTION (STEP_NAME);

-- Execution contexts
CREATE TABLE IF NOT EXISTS BATCH_JOB_EXECUTION_CONTEXT (
                                                           JOB_EXECUTION_ID bigint PRIMARY KEY,
                                                           SHORT_CONTEXT varchar(2500),
    SERIALIZED_CONTEXT text
    );

CREATE TABLE IF NOT EXISTS BATCH_STEP_EXECUTION_CONTEXT (
                                                            STEP_EXECUTION_ID bigint PRIMARY KEY,
                                                            SHORT_CONTEXT varchar(2500),
    SERIALIZED_CONTEXT text
    );
