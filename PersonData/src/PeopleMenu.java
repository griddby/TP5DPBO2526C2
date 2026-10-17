import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.util.Locale;

public class PeopleMenu {
    // Komponen GUI
    private JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunField;
    private JSpinner jumlahSpinner;
    private JComboBox kategoriBox;

    // Radio button untuk menentukan status penulis
    private JRadioButton tidakAktifRadioButton;
    private JRadioButton pensiunRadioButton;
    private JRadioButton aktifRadioButton;

    // Komponen tabel dan tombol
    private JTable penulisTable;
    private JButton updateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JLabel kategoriLabel;
    private JLabel judulLabel;
    private String selectedId = null;
    private Database database;

    // Konstruktor
    public PeopleMenu(){
        database = new Database();

        // Mengatur teks, ukuran, dan posisi judul
        judulLabel.setText("Data Penulis");
        judulLabel.setFont(judulLabel.getFont().deriveFont(Font.BOLD, 30f));
        judulLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Menentukan pilihan kategori yang tersedia dan memasukkannya ke dalam ComboBox
        String[] kategoriData = {"Novel", "Esai", "Non-Fiksi", "Biografi", "Puisi", "Cerpen"};
        kategoriBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // Mengelompokkan RadioButton agar hanya satu yang dipilih
        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(aktifRadioButton);
        statusGroup.add(pensiunRadioButton);
        statusGroup.add(tidakAktifRadioButton);

        // Menentukan status awal penulis
        aktifRadioButton.setSelected(true);

        // Mengatur nilai minimum, maksimum, dan kenaikan spinner
        jumlahSpinner.setModel(new SpinnerNumberModel(0, 0,10000,1));

        // Mengatur font pada komponen input
        Font inputFont = new Font("SansSerif", Font.PLAIN, 14);
        idField.setFont(inputFont);
        namaField.setFont(inputFont);
        tahunField.setFont(inputFont);
        kategoriBox.setFont(inputFont);
        jumlahSpinner.setFont(inputFont);

        // Mengatur model dan tampilan tabel berdasarkan data dari database
        penulisTable.setModel(setTable());
        penulisTable.setFillsViewportHeight(true);
        penulisTable.setRowHeight(26);
        penulisTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        penulisTable.setAutoCreateRowSorter(true);

        // Mengatur teks dan kondisi awal tombol
        updateButton.setText("Add");
        deleteButton.setEnabled(false);

        // Listener untuk tombol Add dan Update
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Jika belum ada data yang dipilih, maka tambahkan data baru
                if(selectedId == null){
                    insertData();
                } else {
                    // Jika ada data yang dipilih, maka perbarui data tersebut
                    updateData();
                }
            }
        });

        // Listener untuk tombol Cancel
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mengosongkan form dan membatalkan pemilihan data
                clearForm();
            }
        });

        // Listener untuk tombol Delete
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Menjalankan proses penghapusan data dari database
                deleteData();
            }
        });

        // Listener untuk mendeteksi pemilihan baris pada tabel
        penulisTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = penulisTable.getSelectedRow();

                if (selectedRow != -1) {
                    // Mengubah indeks baris tampilan menjadi indeks baris model tabel
                    int modelRow = penulisTable.convertRowIndexToModel(selectedRow);

                    // Menampilkan data yang dipilih ke dalam form
                    showSelectedData(modelRow);
                }
            }
        });
    }

    // Mengambil status berdasarkan RadioButton yang dipilih
    private String getStatus(){
        if(aktifRadioButton.isSelected()){
            return "Aktif";
        }
        if(tidakAktifRadioButton.isSelected()){
            return "Tidak Aktif";
        }
        if(pensiunRadioButton.isSelected()){
            return "Pensiun";
        }
        // Menggunakan Aktif sebagai status default
        return "Aktif";
    }

    // Memilih RadioButton sesuai status dari data penulis
    private void setStatus(String status){
        if("Aktif".equals(status)){
            aktifRadioButton.setSelected(true);
        } else if("Tidak Aktif".equals(status)){
            tidakAktifRadioButton.setSelected(true);
        } else if("Pensiun".equals(status)){
            pensiunRadioButton.setSelected(true);
        } else {
            aktifRadioButton.setSelected(true);
        }
    }

    // Membuat model tabel berdasarkan data dari database
    public DefaultTableModel setTable(){
        // Menentukan nama kolom tabel
        Object[] columns = {"ID", "Nama", "Tahun Lahir", "Kategori", "Jumlah Karya", "Status"};

        // Membuat model tabel yang tidak dapat diedit langsung
        DefaultTableModel model = new DefaultTableModel(columns, 0){
            @Override
            public  boolean isCellEditable(int row, int column){
                return false;
            }
        };

        try{
            // Mengambil seluruh data penulis dari database
            ResultSet resultSet = database.selectQuery("SELECT * FROM penulis");

            // Membaca setiap baris hasil query
            while(resultSet.next()) {
                Object[] row = {
                        resultSet.getString("id"),
                        resultSet.getString("nama"),
                        resultSet.getString("tahunlahir"),
                        resultSet.getString("kategori"),
                        resultSet.getString("jumlahkarya"),
                        resultSet.getString("status")
                };
                // Menambahkan data ke dalam model tabel
                model.addRow(row);
            }
        } catch (Exception e) {
            // Menampilkan error jika pengambilan data dari database gagal
            throw new RuntimeException(e);
        }
        return model;
    }

    // Memeriksa kelengkapan dan validitas input form
    private boolean validateForm(){
        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        String tahun = tahunField.getText().trim();

        // Memeriksa apakah ID kosong
        if(id.isEmpty()){
            JOptionPane.showMessageDialog(mainPanel, "ID penulis wajib diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            idField.requestFocus();
            return false;
        }

        // Memeriksa apakah nama kosong
        if(nama.isEmpty()){
            JOptionPane.showMessageDialog(mainPanel, "Nama penulis wajib diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            namaField.requestFocus();
            return false;
        }

        // Memeriksa apakah tahun lahir kosong
        if(tahun.isEmpty()){
            JOptionPane.showMessageDialog(mainPanel, "Tahun lahir wajib diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            tahunField.requestFocus();
            return false;
        }

        try{
            // Mengubah input tahun lahir menjadi angka
            int tahunlahir = Integer.parseInt(tahun);

            // Memeriksa apakah tahun lahir berada dalam rentang yang valid
            if(tahunlahir < 1 || tahunlahir > 2026){
                JOptionPane.showMessageDialog(mainPanel, "Tahun lahir tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
                tahunField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e){
            // Menampilkan pesan jika tahun lahir bukan angka
            JOptionPane.showMessageDialog(mainPanel, "Tahun lahir harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
            tahunField.requestFocus();
            return false;
        }

        // Mengambil dan memeriksa jumlah karya penulis
        int jumlahkarya = ((Number)jumlahSpinner.getValue()).intValue();
        if(jumlahkarya <=0){
            JOptionPane.showMessageDialog(mainPanel, "Jumlah karya harus lebih dari 0 karena penulis " + "harus memiliki minimal 1 karya.", "Input tidak valid.", JOptionPane.ERROR_MESSAGE);
            jumlahSpinner.requestFocus();
            return false;
        }
        return true;
    }

    // Menambahkan data penulis baru ke dalam database
    public void insertData(){
        // Memastikan seluruh input valid sebelum data ditambahkan
        if(!validateForm()){
            return;
        }

        // Mengambil data dari form
        String id = idField.getText();
        String nama = namaField.getText();
        int tahunlahir = Integer.parseInt(tahunField.getText());
        String kategori = kategoriBox.getSelectedItem().toString();
        int jumlahkarya = ((Number) jumlahSpinner.getValue()).intValue();
        String status = getStatus();

        try{
            // Memeriksa apakah ID sudah digunakan di database
            ResultSet resultSet = database.selectQuery("Select id FROM penulis WHERE id = '" + id + "'");
            if(resultSet.next()){
                JOptionPane.showMessageDialog(mainPanel, "ID penulis sudah digunakan.", "Error", JOptionPane.ERROR_MESSAGE);
                idField.requestFocus();
                return;
            }

            // Membuat query untuk menambahkan data penulis
            String sqlQuery = "INSERT INTO penulis VALUES('"
                    + id + "', '"
                    + nama + "', "
                    + tahunlahir + ", '"
                    + kategori + "', "
                    + jumlahkarya + ", '"
                    + status + "')";
            // Menjalankan query insert ke database
            database.insertUpdateDeleteQuery(sqlQuery);
            // Memperbarui tabel berdasarkan data terbaru dari database
            penulisTable.setModel(setTable());
            // Mengosongkan form setelah data berhasil ditambahkan
            clearForm();
            // Menampilkan pesan keberhasilan
            JOptionPane.showMessageDialog(mainPanel, "Data penulis berhasil ditambahkan");
        } catch(Exception e){
            // Mengambil pesan error dari proses insert
            String pesanError = e.getMessage();

            // Memeriksa apakah error disebabkan oleh ID duplikat
            if(pesanError != null && pesanError.toLowerCase().contains("duplicate entry")){
                JOptionPane.showMessageDialog(mainPanel, "ID penulis sudah digunakan. Silahkan gunakan ID lain.", "Gagal Menambahkan Data", JOptionPane.ERROR_MESSAGE);
            } else{
                // Menampilkan pesan jika data gagal ditambahkan
                JOptionPane.showMessageDialog(mainPanel, "Data gagal ditambahkan. Periksa kembali data " + "yang dimasukkan atau koneksi database.\n" + "Detail: " + pesanError,
                        "Gagal Menambahkan Data", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Memperbarui data penulis yang dipilih di database
    public void updateData(){
        // Memastikan seluruh input valid sebelum data diperbarui
        if(!validateForm()){
            return;
        }

        // Memastikan data penulis yang dipilih valid
        if(selectedId == null){
            return;
        }

        // Mengambil data terbaru dari form
        String id = idField.getText();
        String nama = namaField.getText();
        int tahunlahir = Integer.parseInt(tahunField.getText());
        String kategori = kategoriBox.getSelectedItem().toString();
        int jumlahkarya = ((Number) jumlahSpinner.getValue()).intValue();
        String status = getStatus();

        try{
            // Membuat query untuk memperbarui data penulis berdasarkan ID lama
            String sqlQuery = "UPDATE penulis SET "
                    + "id = '" + id + "', "
                    + "nama = '" + nama + "', "
                    + "tahunlahir = " + tahunlahir + ", "
                    + "kategori = '" + kategori + "', "
                    + "jumlahkarya = " + jumlahkarya + ", "
                    + "status = '" + status + "' "
                    + "WHERE id = '" + selectedId + "'";
            // Menjalankan query update ke database
            database.insertUpdateDeleteQuery(sqlQuery);
            // Memperbarui tabel berdasarkan data terbaru dari database
            penulisTable.setModel(setTable());
            // Mengosongkan form setelah data berhasil diperbarui
            clearForm();
            // Menampilkan pesan keberhasilan
            JOptionPane.showMessageDialog(mainPanel, "Data berhasil diperbarui");
        } catch (Exception e){
            // Mengambil pesan error dari proses update
            String pesanError = e.getMessage();

            // Memeriksa apakah error disebabkan oleh ID duplikat
            if(pesanError != null && pesanError.toLowerCase().contains("duplicate entry")){
                JOptionPane.showMessageDialog(mainPanel, "ID penulis sudah digunakan oleh penulis lain. Silahkan gunakan ID lain.", "Gagal Memperbarui Data", JOptionPane.ERROR_MESSAGE);
            } else{
                // Menampilkan pesan jika data gagal diperbarui
                JOptionPane.showMessageDialog(mainPanel, "Data gagal diperbarui. Periksa kembali data " + "yang dimasukkan atau koneksi database.\n" + "Detail: " + pesanError,
                        "Gagal Memperbarui Data", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Menghapus data penulis yang dipilih dari database
    public void deleteData(){
        // Memastikan ada data penulis yang dipilih
        if(selectedId == null){
            return;
        }

        // Meminta konfirmasi sebelum menghapus data
        int action = JOptionPane.showConfirmDialog(mainPanel, "Yakin ingin menghapus data?", "Konfirmasi hapus", JOptionPane.YES_NO_OPTION);

        // Menghapus data jika pengguna memilih YES
        if(action == JOptionPane.YES_OPTION){
            try{
                // Membuat query untuk menghapus data berdasarkan ID yang dipilih
                String sqlQuery = "DELETE FROM penulis WHERE id = '" + selectedId + "'";
                // Menjalankan query delete ke database
                database.insertUpdateDeleteQuery(sqlQuery);
                // Memperbarui tabel berdasarkan data terbaru dari database
                penulisTable.setModel(setTable());
                // Mengosongkan form setelah data berhasil dihapus
                clearForm();
                // Menampilkan pesan keberhasilan
                JOptionPane.showMessageDialog(mainPanel, "Data berhasil dihapus.");
            } catch (Exception e) {
                // Mengambil pesan error dari proses delete
                String pesanError = e.getMessage();

                // Memeriksa apakah data masih digunakan oleh data lain
                if (pesanError != null && pesanError.contains("foreign key")) {
                    JOptionPane.showMessageDialog(mainPanel, "Data tidak dapat dihapus karena masih digunakan " + "oleh data lain.", "Gagal Menghapus data", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Menampilkan pesan jika data gagal dihapus
                    JOptionPane.showMessageDialog(mainPanel, "Data gagal dihapus. Silahkan coba kembali\n" + "Detail: " + pesanError, "Gagal Menghapus Data", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Menampilkan data penulis yang dipilih ke dalam form
    private void showSelectedData(int modelRow){
        // Memastikan indeks baris yang dipilih valid
        if(modelRow < 0 || modelRow >= penulisTable.getModel().getRowCount()){
            return;
        }

        // Menyimpan ID data yang dipilih sebagai acuan update dan delete
        selectedId = penulisTable.getModel().getValueAt(modelRow, 0).toString();

        // Mengisi field berdasarkan data penulis yang dipilih
        idField.setText(selectedId);
        namaField.setText(penulisTable.getModel().getValueAt(modelRow, 1).toString());
        tahunField.setText(penulisTable.getModel().getValueAt(modelRow, 2).toString());
        kategoriBox.setSelectedItem(penulisTable.getModel().getValueAt(modelRow, 3));
        jumlahSpinner.setValue(Integer.parseInt(penulisTable.getModel().getValueAt(modelRow,4).toString()));
        setStatus(penulisTable.getModel().getValueAt(modelRow, 5).toString());

        // Mengubah tombol Add menjadi Update
        updateButton.setText("Update");
        // Mengaktifkan tombol Delete
        deleteButton.setEnabled(true);
    }

    // Mengosongkan semua input dan membatalkan pemilihan data
    public void clearForm(){
        idField.setText("");
        namaField.setText("");
        tahunField.setText("");
        jumlahSpinner.setValue(0);
        kategoriBox.setSelectedIndex(0);
        aktifRadioButton.setSelected(true);

        // Mengatur ulang ID data yang dipilih dan pilihan tabel
        selectedId = null;
        penulisTable.clearSelection();

        // Mengembalikan tombol ke kondisi awal
        updateButton.setText("Add");
        deleteButton.setEnabled(false);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Membuat objek form PeopleMenu
                PeopleMenu form = new PeopleMenu();

                // Membuat jendela utama aplikasi
                JFrame menu = new JFrame("Writer Database");
                menu.setContentPane(form.mainPanel);
                menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                menu.setSize(650, 530);
                menu.setLocationRelativeTo(null);
                menu.setVisible(true);
            }
        });
    }
}