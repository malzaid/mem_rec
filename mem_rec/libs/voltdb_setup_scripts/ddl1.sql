
-- Tell sqlcmd to batch the following commands together,
-- so that the schema loads quickly.
file -inlinebatch END_OF_BATCH


-- ----------------------------
--  Table structure for "ratings"
-- ----------------------------

CREATE TABLE ratings_1m (
	userid		integer		NOT NULL,
	movieid		integer		NOT NULL,
	rating		FLOAT		NOT NULL,
	timestamp	integer		NOT NULL
);

-- ----------------------------
--  Table structure for "movies"
-- ----------------------------

CREATE TABLE movies_1m (
	movieid	integer			NOT NULL,
	title	varchar(255)	NOT NULL,
	genres	varchar(255)	NOT NULL
)




END_OF_BATCH

CREATE INDEX movies_idx1m ON movies_1m (movieid);
CREATE INDEX ratings_idx1m ON ratings_1m (userid,movieid);



