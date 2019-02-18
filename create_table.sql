DROP TABLE IF EXISTS moviedb;
create database moviedb;
use moviedb;
create table movies(
	id varchar(10) NOT NULL,
    title varchar(100) NOT NULL,
    year integer NOT NULL,
    director varchar(100) NOT NULL,
    primary key(id)
    );
create table stars(
	id varchar(10) NOT NULL,
	name varchar(100) NOT NULL,
	birthYear integer,
    primary key(id)
    );
create table stars_in_movies(
	starId varchar(10) NOT NULL,
    movieId varchar(10) NOT NULL,
    primary key(starId, movieId),
    foreign key(starId) references stars(id) on delete cascade,
    foreign key(movieId) references movies(id) on delete cascade
    );
create table genres(
		id integer NOT NULL AUTO_INCREMENT,
        name varchar(32),
        primary key(id)
        );
create table genres_in_movies(
	genreId integer NOT NULL,
    movieId varchar(10) NOT NULL,
    primary key(genreId, movieId),
    foreign key(genreId) references genres(id) on delete cascade,
    foreign key(movieId) references movies(id) on delete cascade
    );
 create table creditcards(
	id varchar(20) NOT NULL,
    firstName varchar(50) NOT NULL,
    lastName varchar(50) NOT NULL,
    expiration date,
    primary key(id)
    );
    
    
create table customers(
	id integer NOT NULL AUTO_INCREMENT,
    firstName varchar(50) NOT NULL,
    lastName varchar(50) NOT NULL,
    ccId varchar(20) NOT NULL,
    address varchar(200) NOT NULL,
    email varchar(50) NOT NULL,
    password varchar(20) NOT NULL,
    primary key(id),
    foreign key (ccId) references creditcards(id) on delete cascade
    );

create table sales(
	id integer NOT NULL auto_increment,
	customerId integer NOT NULL,
	movieId varchar(10) NOT NULL,
	saleDate date NOT NULL,
	primary key(id)
);
create table ratings(
movieId varchar(10) NOT NULL,
rating float NOT NULL,
numVoters integer NOT NULL
);

create table employees(
email varchar(50),
password varchar(20) not null,
fullname varchar(100),
primary key(email)
);

DROP PROCEDURE IF EXISTS insert_star;
DELIMITER $$
CREATE PROCEDURE insert_star(sname varchar(100),syear int) 
BEGIN
	DECLARE sid char(10) default NULL;
    DECLARE num int default NULL;
    DECLARE cnum char(10) default NULL;
    set num = (SELECT CONVERT(SUBSTRING_INDEX(max(id),'m',-1),UNSIGNED INTEGER)+1  FROM moviedb.stars);
    set cnum = cast(num as char(10));
	SET sid = (SELECT CONCAT(SUBSTRING(MAX(id),1,2),cnum)  FROM moviedb.stars);
	INSERT INTO stars VALUES(sid, sname, syear);
END
$$

DELIMITER ;

-- call add_star("Eric vvv", 2000);
-- ************************************************************** --  

DROP PROCEDURE IF EXISTS add_movie;
DELIMITER $$
CREATE PROCEDURE add_movie(titleIn VARCHAR(100),directorIn VARCHAR(100),genreIn VARCHAR(32), yearIn INT,  
starname VARCHAR(100),
birth int(11))
BEGIN
		DECLARE idmovie char(10) DEFAULT NULL;
		DECLARE idstar char(10) DEFAULT NULL;
		DECLARE idgenre INT(11) DEFAULT NULL;
		DECLARE namedirector char(100) default NULL;
        
		DECLARE sid char(10) default NULL;
		DECLARE num int default NULL;
		DECLARE cnum char(10) default NULL;
    if (select id from movies where title = titleIn and director = directorIn and yearIn = year) is not null then
		select concat(titleIn," is existed");
	else
			SET idmovie = (select id from movies where title = titleIn);
			SET idstar = (select id from stars where name = starname);
			SET idgenre = (select id from genres where name = genreIn);
			
			
			SET idmovie = (SELECT  CONCAT('tt0',CAST(CONVERT(SUBSTRING_INDEX(max(id),'t',-1),UNSIGNED INTEGER)+1 as char(11))) AS num FROM movies);
			INSERT INTO movies(id,title,year,director) VALUES (idmovie,titleIn,yearIn,directorIn);
            INSERT INTO ratings(movieId, rating, numVoters) VALUES (idmovie,0,0);
			
			IF idstar IS NOT NULL THEN
				INSERT INTO stars_in_movies VALUES(idstar,idmovie);
			ELSE
				set num = (SELECT CONVERT(SUBSTRING_INDEX(max(id),'m',-1),UNSIGNED INTEGER)+1  FROM moviedb.stars);
				set cnum = cast(num as char(10));
				SET sid = (SELECT CONCAT(SUBSTRING(MAX(id),1,2),cnum)  FROM moviedb.stars);
				INSERT INTO stars VALUES(sid, starname, birth);
				INSERT INTO stars_in_movies VALUES(sid,idmovie);
			END IF;
			IF idgenre IS NOT NULL THEN
				INSERT INTO  genres_in_movies VALUES(idgenre,idmovie);
			ELSE
				INSERT INTO genres(name) VALUES (genreIn);
				SET idgenre = (select id from genres where name = genreIn);
				INSERT INTO genres_in_movies VALUES (idgenre,idmovie);
			END IF;
		end if;
END
$$
DELIMITER ;

-- call add_movie("cs12332", "zy", "drama", 2001, "yindi ma", 1998 );

-- ************************************************************** --  



-- SELECT * from movies where title="zkding";

-- SELECT * from genres_in_movies where movieId="tt0499471";

-- SELECT * from stars where id="nm9423081";

-- SELECT * from genres where id=24;

