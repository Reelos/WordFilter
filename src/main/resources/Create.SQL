DROP TABLE IF EXISTS BadWords; 

CREATE TABLE BadWords(
	PkID SERIAL PRIMARY KEY,
	Added_By VARCHAR(32) DEFAULT "Console",
	Word VARCHAR(32),
	For_All INTEGER DEFAULT 1
);

INSERT INTO BadWords
	(Word,For_All)
VALUES
	("Fuck",1),
	("Fick",1),
	("Hurensohn",1),
	("Arschloch",1),
	("Nigger",1),
	("Beppo",1),
	("Idiot",0),
	("Arschgeige",1),
	("Arsch",1),
	("Vollpfosten",0),
	("Shit",1),
	("Motherfucker",1),
	("Birdo",0),
	("Trottel",0),
	("Wixxer",1),
	("Lutscher",0),
	("NeuxzKopf",0);
