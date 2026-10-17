public class Person {
    private String id;
    private String nama;
    private int tahunlahir;
    private String kategori;
    // Atribut baru
    private int jumlahkarya;
    private String status;

    // Konstruktor
    public Person(){
    }
    public Person(String id, String nama, int tahunlahir, String kategori, int jumlahkarya, String status){
        this.id = id;
        this.nama = nama;
        this.tahunlahir = tahunlahir;
        this.kategori = kategori;
        this.jumlahkarya = jumlahkarya;
        this.status = status;
    }

    // Setter
    public void setId(String id){
        this.id = id;
    }

    public void setNama(String nama){
        this.nama = nama;
    }

    public void setTahunLahir(int tahunlahir){
        this.tahunlahir = tahunlahir;
    }

    public void setKategori(String kategori){
        this.kategori = kategori;
    }

    public void setJumlahKarya(int jumlahkarya){
        this.jumlahkarya = jumlahkarya;
    }

    public void setStatusPenulis(String status){
        this.status = status;
    }

    // Getter
    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public int getTahunLahir() {
        return tahunlahir;
    }

    public String getKategori() {
        return kategori;
    }

    public int getJumlahKarya() {
        return jumlahkarya;
    }

    public String getStatusPenulis() {
        return status;
    }
}
