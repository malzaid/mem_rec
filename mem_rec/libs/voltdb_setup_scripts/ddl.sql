
-- Tell sqlcmd to batch the following commands together,
-- so that the schema loads quickly.
file -inlinebatch END_OF_BATCH


-- ----------------------------
--  Table structure for "ratings"
-- ----------------------------

CREATE TABLE ratings (
	userid		integer		NOT NULL,
	movieid		integer		NOT NULL,
	rating		FLOAT		NOT NULL,
	timestamp	integer		NOT NULL
);

-- ----------------------------
--  Table structure for "movies"
-- ----------------------------

CREATE TABLE movies (
	movieid	integer			NOT NULL,
	title	varchar(255)	NOT NULL,
	genres	varchar(255)	NOT NULL
)



END_OF_BATCH
-- 
-- -- Update classes from jar so that the server will know about classes
-- -- but not procedures yet.
-- -- This command cannot be part of a DDL batch.
-- LOAD CLASSES voter-procs.jar;
-- 
-- -- The following CREATE PROCEDURE statements can all be batched.
-- file -inlinebatch END_OF_2ND_BATCH
-- 
-- -- stored procedures
-- CREATE PROCEDURE FROM CLASS voter.Initialize;
-- CREATE PROCEDURE FROM CLASS voter.Results;
-- CREATE PROCEDURE PARTITION ON TABLE votes COLUMN phone_number FROM CLASS voter.Vote;
-- CREATE PROCEDURE FROM CLASS voter.ContestantWinningStates;
-- CREATE PROCEDURE FROM CLASS voter.GetStateHeatmap;
-- 
-- END_OF_2ND_BATCH
