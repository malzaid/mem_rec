
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

CREATE INDEX movies_idx ON movies (movieid);
CREATE INDEX ratings_idx ON ratings (userid,movieid);

LOAD CLASSES storedprocs.jar;


CREATE PROCEDURE FROM CLASS PrepareUsers;
CREATE PROCEDURE FROM CLASS PrepareItems;
CREATE PROCEDURE FROM CLASS PrepareEvents;
CREATE PROCEDURE FROM CLASS PrepareUserEvents;
CREATE PROCEDURE FROM CLASS PrepareItemEvents;
CREATE PROCEDURE FROM CLASS PrepareItemUsers;


