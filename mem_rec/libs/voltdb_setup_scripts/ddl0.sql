
-- Tell sqlcmd to batch the following commands together,
-- so that the schema loads quickly.
file -inlinebatch END_OF_BATCH


-- ----------------------------
--  Table structure for "ratings"
-- ----------------------------

CREATE TABLE ratings_100k (
	userid		integer		NOT NULL,
	movieid		integer		NOT NULL,
	rating		FLOAT		NOT NULL,
	timestamp	integer		NOT NULL
);

-- ----------------------------
--  Table structure for "movies"
-- ----------------------------

CREATE TABLE movies_100k (
	movieid	integer			NOT NULL,
	title	varchar(255)	NOT NULL,
	genres	varchar(255)	NOT NULL
)




END_OF_BATCH

CREATE INDEX movies_idx100k  ON movies_100k (movieid);
CREATE INDEX ratings_idx100k ON ratings_100k (userid,movieid);



