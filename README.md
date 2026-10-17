# Tugas Praktikum 5 DPBO

## Deskripsi Program

Program ini merupakan aplikasi pengelolaan data penulis berbasis Java Swing yang terhubung dengan database MySQL. Program digunakan untuk menampilkan, menambahkan, memperbarui, dan menghapus data penulis.

Data penulis yang dikelola meliputi ID, nama, tahun lahir, kategori tulisan, jumlah karya, dan status penulis.

## Desain Program

### 1. Struktur Data

Program menggunakan database MySQL dengan nama `db_person` dan tabel `penulis`.

Tabel `penulis` memiliki struktur sebagai berikut:

| No. | Atribut | Tipe Data | Keterangan |
|---|---|---|---|
| 1 | `id` | VARCHAR(255) | ID unik penulis dan primary key |
| 2 | `nama` | VARCHAR(255) | Nama penulis |
| 3 | `tahunlahir` | INT | Tahun lahir penulis |
| 4 | `kategori` | VARCHAR(255) | Kategori tulisan penulis |
| 5 | `jumlahkarya` | INT | Jumlah karya yang dimiliki penulis |
| 6 | `status` | VARCHAR(30) | Status penulis, yaitu Aktif, Tidak Aktif, atau Pensiun |

### 2. Struktur Kelas

Program terdiri dari kelas `PeopleMenu` dan kelas `Database`.

- **PeopleMenu**: Mengatur tampilan GUI, menerima input pengguna, menjalankan operasi CRUD, memvalidasi input, serta menampilkan dialog informasi dan error.
- **Database**: Menangani koneksi dan operasi database MySQL yang digunakan oleh program.

### 3. Rancangan Antarmuka

Antarmuka program terdiri dari beberapa komponen berikut:

- **ID**: Field untuk memasukkan ID penulis.
- **Nama**: Field untuk memasukkan nama penulis.
- **Tahun Lahir**: Field untuk memasukkan tahun lahir penulis.
- **Kategori**: ComboBox untuk memilih kategori tulisan.
- **Jumlah Karya**: Spinner untuk menentukan jumlah karya.
- **Status**: RadioButton untuk memilih status penulis.
- **Tabel Data Penulis**: Menampilkan data penulis yang tersimpan di database.
- **Add/Update**: Tombol untuk menambahkan atau memperbarui data.
- **Delete**: Tombol untuk menghapus data yang dipilih.
- **Cancel**: Tombol untuk mengosongkan form dan membatalkan pemilihan data.

## Alur Program

### 1. Inisialisasi Program

Ketika program dijalankan, objek `Database` dibuat untuk menangani komunikasi dengan database MySQL. Program kemudian mengatur komponen antarmuka, pilihan kategori, kelompok RadioButton status, spinner jumlah karya, dan model tabel.

Data penulis yang tersimpan di database ditampilkan pada tabel.

### 2. Read Data

Program mengambil seluruh data penulis dari tabel `penulis` menggunakan perintah SQL `SELECT`. Data yang diperoleh ditampilkan pada tabel di antarmuka.

### 3. Add Data

Pengguna mengisi form data penulis dan menekan tombol **Add**.

Program memeriksa kelengkapan serta validitas input. Jika input valid, program memeriksa apakah ID sudah digunakan. Jika ID belum digunakan, data disimpan ke database menggunakan perintah SQL `INSERT`.

Setelah berhasil, tabel diperbarui dan form dikosongkan. Jika terjadi kesalahan, program menampilkan dialog error.

### 4. Update Data

Pengguna memilih salah satu baris pada tabel. Data dari baris tersebut ditampilkan pada form, kemudian tombol **Add** berubah menjadi **Update**.

Pengguna dapat mengubah data yang diperlukan dan menekan tombol **Update**. Program melakukan validasi input, kemudian memperbarui data di database menggunakan perintah SQL `UPDATE`.

Setelah berhasil, tabel diperbarui dan form dikosongkan.

### 5. Delete Data

Pengguna memilih data pada tabel, kemudian menekan tombol **Delete**.

Program menampilkan dialog konfirmasi penghapusan. Jika pengguna memilih **Yes**, data dihapus dari database menggunakan perintah SQL `DELETE`. Jika pengguna memilih **No**, penghapusan dibatalkan.

Setelah penghapusan berhasil, tabel diperbarui dan form dikosongkan.

### 6. Cancel

Tombol **Cancel** digunakan untuk mengosongkan seluruh input, mengembalikan status ke pilihan awal, membatalkan pemilihan baris tabel, dan mengembalikan tombol ke kondisi awal.

## Validasi dan Penanganan Error

Program memiliki beberapa validasi untuk memastikan data yang dimasukkan sesuai dengan ketentuan.

### 1. Input Wajib Diisi

ID, nama, dan tahun lahir wajib diisi. Jika salah satu input tersebut kosong, program menampilkan dialog error dan mengarahkan fokus ke field yang perlu diperbaiki.

### 2. Tahun Lahir Tidak Valid

Tahun lahir harus berupa angka dan berada pada rentang 1 sampai 2026. Jika tahun lahir kosong, bukan angka, atau berada di luar rentang tersebut, program menampilkan dialog error.

### 3. Jumlah Karya Tidak Valid

Jumlah karya harus lebih dari 0. Jika jumlah karya tidak memenuhi ketentuan, program menampilkan dialog error.

### 4. ID Duplikat

ID penulis harus unik. Jika pengguna memasukkan ID yang sudah digunakan, program menampilkan dialog error dan membatalkan penambahan data.

Jika terjadi duplikasi ID ketika memperbarui data, program juga menampilkan dialog error.

### 5. Kesalahan Database

Jika operasi database gagal, program menampilkan dialog error yang berisi informasi kegagalan. Pada proses penghapusan, program juga menangani kondisi ketika data tidak dapat dihapus karena masih digunakan oleh data lain.

## Teknologi yang Digunakan

- **Java**: Bahasa pemrograman untuk mengembangkan aplikasi.
- **Java Swing**: Pustaka untuk membangun antarmuka grafis.
- **MySQL**: Sistem manajemen basis data untuk menyimpan data penulis.
- **JDBC**: Penghubung antara aplikasi Java dan database MySQL.


## Kesimpulan

Aplikasi pengelolaan data penulis ini menerapkan operasi CRUD menggunakan Java Swing dan MySQL. Program menyediakan fitur untuk menampilkan, menambahkan, memperbarui, dan menghapus data penulis, disertai validasi input serta dialog informasi dan error untuk membantu pengguna ketika berinteraksi dengan aplikasi.

## Dokumentasi Program

Dokumentasi berikut memperlihatkan pelaksanaan operasi CRUD dan validasi pada aplikasi.

### 1. Read Data

Menampilkan data penulis yang tersimpan di database MySQL.

<p align="left">
  <img src="Dokumentasi/read_data.gif" width="500" alt="Dokumentasi Read Data">
</p>

### 2. Add Data

Menambahkan data penulis baru melalui form. Setelah data berhasil ditambahkan, tabel diperbarui untuk menampilkan data terbaru.

<p align="left">
  <img src="Dokumentasi/add_data.gif" width="500" alt="Dokumentasi Add Data">
</p>

### 3. Update Data

Memilih data penulis pada tabel, mengubah informasi pada form, kemudian menyimpan perubahan melalui tombol Update.

<p align="left">
  <img src="Dokumentasi/update_data.gif" width="500" alt="Dokumentasi Update Data">
</p>

### 4. Delete Data

Menghapus data penulis yang dipilih melalui tombol Delete. Program menampilkan dialog konfirmasi sebelum menjalankan penghapusan.

<p align="left">
  <img src="Dokumentasi/delete_data.gif" width="500" alt="Dokumentasi Delete Data">
</p>

### 5. Validasi Input Wajib

Menampilkan dialog error ketika pengguna mencoba menambahkan data dengan input wajib yang belum diisi.

<p align="left">
  <img src="Dokumentasi/data_wajib_isi.gif" width="500" alt="Dokumentasi Validasi Input Wajib">
</p>

### 6. Validasi ID Duplikat dan Tahun Lahir

Menampilkan validasi ketika ID penulis sudah digunakan atau tahun lahir tidak memenuhi ketentuan.

<p align="left">
  <img src="Dokumentasi/id_duplikat_tahun_valid.gif" width="500" alt="Dokumentasi Validasi ID Duplikat dan Tahun Lahir">
</p>
