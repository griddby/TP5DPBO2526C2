
CREATE DATABASE db_person;
USE db_person;

CREATE TABLE penulis (
  id varchar(255) PRIMARY KEY,
  nama varchar(255) NOT NULL,
  tahunlahir int NOT NULL,
  kategori varchar(255) NOT NULL,
  jumlahkarya int NOT NULL,
  status varchar(30) CHECK (status IN ('Aktif', 'Tidak Aktif', 'Pensiun'))
);

INSERT INTO penulis (id, nama, tahunlahir, kategori, jumlahkarya, status) VALUES
("P001", "Andrea Hirata", 1967, "Novel", 12, "Aktif"),
("P002", "Dee Lestari", 1976, "Novel", 15, "Aktif"),
("P003", "Joko Pinurbo", 1962, "Puisi", 20, "Tidak Aktif"),
("P004", "Seno Gumira Ajidarma", 1958, "Cerpen", 18, "Aktif"),
("P005", "Raditya Dika", 1984, "Non-Fiksi", 10, "Aktif"),
("P006", "Tere Liye", 1979, "Novel", 30, "Aktif"),
("P007", "Chairil Anwar", 1922, "Puisi", 15, "Pensiun"),
("P008", "Pramoedya Ananta Toer", 1925, "Novel", 25, "Pensiun"),
("P009", "Ahmad Tohari", 1948, "Novel", 18, "Aktif"),
("P010", "Mira W.", 1951, "Novel", 40, "Aktif"),
("P011", "Sapardi Djoko Damono", 1940, "Puisi", 22, "Pensiun"),
("P012", "Goenawan Mohamad", 1941, "Esai", 15, "Aktif")