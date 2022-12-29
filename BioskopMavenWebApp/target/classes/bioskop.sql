DROP SCHEMA IF EXISTS bioskop;
CREATE SCHEMA bioskop DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE bioskop;

CREATE TABLE korisnici (
	korisnickoIme VARCHAR(20),
	lozinka VARCHAR(20) NOT NULL,
	eMail VARCHAR(20) NOT NULL,
	pol ENUM('muški', 'ženski') DEFAULT 'muški',
	administrator BOOL DEFAULT false,
	PRIMARY KEY(korisnickoIme)
);

CREATE TABLE filmovi (
	id BIGINT AUTO_INCREMENT,
	naziv VARCHAR(75) NOT NULL,
	trajanje INT NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE zanrovi (
	id BIGINT AUTO_INCREMENT,
	naziv VARCHAR(25) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE filmZanr (
    filmId BIGINT,
    zanrId BIGINT,
    PRIMARY KEY(filmId, zanrId),
    FOREIGN KEY(filmId) REFERENCES filmovi(id)
		ON DELETE CASCADE,
    FOREIGN KEY(zanrId) REFERENCES zanrovi(id)
		ON DELETE CASCADE
);

CREATE TABLE projekcije (
	id BIGINT AUTO_INCREMENT,
	datumIVreme DATETIME,
	filmId BIGINT NOT NULL,
	sala INT NOT NULL,
	tip ENUM('2D', '3D', '4D') DEFAULT '2D',
	cenaKarte DECIMAL(10, 2) NOT NULL,
	PRIMARY KEY(id),
    FOREIGN KEY(filmId) REFERENCES filmovi(id)
		ON DELETE CASCADE
);

INSERT INTO korisnici (korisnickoIme, lozinka, eMail, pol, administrator) VALUES ('a', 'a', 'a@a.com', 'muški', true);
INSERT INTO korisnici (korisnickoIme, lozinka, eMail, pol, administrator) VALUES ('b', 'b', 'b@b.com', 'ženski', false);
INSERT INTO korisnici (korisnickoIme, lozinka, eMail, pol, administrator) VALUES ('c', 'c', 'c@c.com', 'muški', false);

INSERT INTO zanrovi (id, naziv) VALUES (1, 'naučna fantastika');
INSERT INTO zanrovi (id, naziv) VALUES (2, 'akcija');
INSERT INTO zanrovi (id, naziv) VALUES (3, 'komedija');
INSERT INTO zanrovi (id, naziv) VALUES (4, 'horor');
INSERT INTO zanrovi (id, naziv) VALUES (5, 'avantura');

INSERT INTO filmovi (id, naziv, trajanje) VALUES (1, 'Avengers: Endgame', 182);
INSERT INTO filmovi (id, naziv, trajanje) VALUES (2, 'Life', 110);
INSERT INTO filmovi (id, naziv, trajanje) VALUES (3, 'It: Chapter 2', 170);
INSERT INTO filmovi (id, naziv, trajanje) VALUES (4, 'Pirates of the Caribbean: Dead Men Tell No Tales', 153);

INSERT INTO filmZanr (filmId, zanrId) VALUES (1, 1);
INSERT INTO filmZanr (filmId, zanrId) VALUES (1, 2);
INSERT INTO filmZanr (filmId, zanrId) VALUES (1, 5);

INSERT INTO filmZanr (filmId, zanrId) VALUES (2, 1);
INSERT INTO filmZanr (filmId, zanrId) VALUES (2, 4);

INSERT INTO filmZanr (filmId, zanrId) VALUES (3, 4);

INSERT INTO filmZanr (filmId, zanrId) VALUES (4, 2);
INSERT INTO filmZanr (filmId, zanrId) VALUES (4, 3);
INSERT INTO filmZanr (filmId, zanrId) VALUES (4, 5);

INSERT INTO projekcije (datumIVreme, filmId, sala, tip, cenaKarte) VALUES ('2020-06-22 20:00', 1, 1, '2D', 380.00);
INSERT INTO projekcije (datumIVreme, filmId, sala, tip, cenaKarte) VALUES ('2020-06-22 23:30', 3, 1, '2D', 380.00);
INSERT INTO projekcije (datumIVreme, filmId, sala, tip, cenaKarte) VALUES ('2020-06-22 20:00', 1, 2, '3D', 420.00);
INSERT INTO projekcije (datumIVreme, filmId, sala, tip, cenaKarte) VALUES ('2020-06-22 23:30', 2, 2, '3D', 420.00);
INSERT INTO projekcije (datumIVreme, filmId, sala, tip, cenaKarte) VALUES ('2020-06-22 20:00', 3, 3, '4D', 580.00);

INSERT INTO projekcije (datumIVreme, filmId, sala, tip, cenaKarte) VALUES ('2020-06-23 20:00', 2, 1, '2D', 380.00);
INSERT INTO projekcije (datumIVreme, filmId, sala, tip, cenaKarte) VALUES ('2020-06-23 22:00', 4, 1, '2D', 380.00);
INSERT INTO projekcije (datumIVreme, filmId, sala, tip, cenaKarte) VALUES ('2020-06-23 20:00', 2, 2, '3D', 420.00);
INSERT INTO projekcije (datumIVreme, filmId, sala, tip, cenaKarte) VALUES ('2020-06-23 22:00', 4, 2, '3D', 420.00);
INSERT INTO projekcije (datumIVreme, filmId, sala, tip, cenaKarte) VALUES ('2020-06-23 20:00', 1, 3, '4D', 580.00);
