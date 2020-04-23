package APP18090087;

import java.awt.HeadlessException;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Anshalsa
 */
public class AppArsip extends javax.swing.JFrame {
    int idBaris=0;
    String role;
    DefaultTableModel model;
    String filename = null;
    
    public AppArsip() {
        initComponents();
        Koneksi.sambungDB();
        aturModelTabel();
        kategori();
        showForm(false);
        showData("");
    }
    
    private void aturModelTabel(){
        Object[] kolom = {"No","Kode Dokumen","Nama Dokumen","Kategori Dokumen","Lokasi Dokumen","Deskripsi Dokumen","Tanggal"};
        model = new DefaultTableModel(kolom,0) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        tabel.setModel(model);
        tabel.setRowHeight(20);
        tabel.getColumnModel().getColumn(0).setMinWidth(0);
        tabel.getColumnModel().getColumn(0).setMaxWidth(0);
    }
    private void showForm(boolean b){
        areaSplit.setDividerLocation(0.3);
        areaSplit.getLeftComponent().setVisible(b);
    }
    
    private void resetForm(){
        tabel.clearSelection();
        txtKode.setText("");
        txtNama.setText("");
        cmbKategori.setSelectedIndex(0);
        txtLok.setText("");
        txtDesk.setText("");
        txtDate.setText("");
        txtKode.requestFocus();
    }
    
    private void kategori(){
        cmbKategori.removeAllItems();
        cmbKategori.addItem("Umum");
        cmbKategori.addItem("Resmi");
        cmbKategori.addItem("Pribadi");
    }
    
    private void showData(String key){
        model.getDataVector().removeAllElements();         
        String where = "";         
        if(!key.isEmpty()){
            where += "WHERE kode_dokumen LIKE '%"+key+"%' "                     
                    + "OR nama_dokumen LIKE '%"+key+"%' "                    
                    + "OR kategori_dokumen LIKE '%"+key+"%' "                     
                    + "OR lokasi_dokumen LIKE '%"+key+"%' "                    
                    + "OR deskripsi_dokumen LIKE '%"+key+"%'"
                    + "OR tanggal LIKE '%"+key+"%'"; 
        }
        String sql = "SELECT * FROM tbdok "+where;                 
        Connection con;         
        Statement st;        
        ResultSet rs;         
        int baris = 0;         
        try {
            con = Koneksi.sambungDB();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                Object id = rs.getInt(1);
                Object kode_dokumen = rs.getString(2);
                Object nama_dokumen = rs.getString(3);
                Object kategori_dokumen = rs.getString(4);
                Object lokasi_dokumen = rs.getString(5);
                Object deskripsi_dokumen = rs.getString(6);
                Object tanggal = rs.getString(7);
                Object[] data = {id,kode_dokumen,nama_dokumen,kategori_dokumen,lokasi_dokumen,deskripsi_dokumen,tanggal};
                model.insertRow(baris, data);
                baris++;
            }
            st.close();
            con.close();
            tabel.revalidate();
            tabel.repaint();
        } catch (SQLException e) {
            System.err.println("showData(): "+e.getMessage());
        }
    }
    
    private void resetView(){
        resetForm();
        showForm(false);
        showData("");
        btnHapus.setEnabled(false);
        idBaris = 0;
    }
    
    private void pilihData(String n){
        btnHapus.setEnabled(true);
        String sql = "SELECT * FROM tbdok WHERE id='"+n+"'";
        Connection con;
        Statement st;
        ResultSet rs;
        try {
            con = Koneksi.sambungDB();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                String kode_dokumen = rs.getString(2);
                String nama_dokumen = rs.getString(3);
                Object kategori_dokumen = rs.getString(4);
                String lokasi_dokumen = rs.getString(5);
                String deskripsi_dokumen = rs.getString(6);
                String tanggal = rs.getString(7);
                idBaris = id;
                txtKode.setText(kode_dokumen);
                txtNama.setText(nama_dokumen);
                cmbKategori.setSelectedItem(kategori_dokumen);
                txtLok.setText(lokasi_dokumen);
                txtDesk.setText(deskripsi_dokumen);
                txtDate.setText(tanggal);
            }
            st.close();
            con.close();
            showForm(true);
        } catch (SQLException e) {
            System.err.println("pilihData(): "+e.getMessage());
        }
    }
    
    private void simpanData(){
        String kode_dokumen = txtKode.getText();
        String nama_dokumen = txtNama.getText();
        int kategori_dokumen = cmbKategori.getSelectedIndex();
        String lokasi_dokumen = txtLok.getText();
        String deskripsi_dokumen = txtDesk.getText();
        String tanggal = txtDate.getText();
        if(kode_dokumen.isEmpty() || nama_dokumen.isEmpty() || kategori_dokumen==0 || lokasi_dokumen.isEmpty() ||
                deskripsi_dokumen.isEmpty() || tanggal.isEmpty()){
            JOptionPane.showMessageDialog(this, "Mohon Lengkapi Data!");
        }else{
            String kategori_dokumen_isi = cmbKategori.getSelectedItem().toString();
            String sql =
                    "INSERT INTO tbdok (kode_dokumen,nama_dokumen,kategori_dokumen,lokasi_dokumen,deskripsi_dokumen,tanggal) "
                    + "VALUES (\""+kode_dokumen+"\",\""+nama_dokumen+"\","+ "\""+kategori_dokumen_isi
                    +"\",\""+lokasi_dokumen+"\",\""+deskripsi_dokumen+"\",\"" +tanggal+ "\")";
            Connection con;
            Statement st;
            try {
                con = Koneksi.sambungDB();
                st = con.createStatement();
                st.executeUpdate(sql);
                st.close();
                con.close();
                resetView();
                JOptionPane.showMessageDialog(this,"Data Telah Disimpan!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }
    
    private void ubahData(){
        String kode_dokumen = txtKode.getText();
        String nama_dokumen = txtNama.getText();
        int kategori_dokumen = cmbKategori.getSelectedIndex();
        String lokasi_dokumen = txtLok.getText();
        String deskripsi_dokumen = txtDesk.getText();
        String tanggal = txtDate.getText();
        if(kode_dokumen.isEmpty() || nama_dokumen.isEmpty() || kategori_dokumen==0 || lokasi_dokumen.isEmpty() ||
                deskripsi_dokumen.isEmpty() || tanggal.isEmpty()){
            JOptionPane.showMessageDialog(this, "Mohon Lengkapi Data!");
        }else{
            String kategori_dokumen_isi = cmbKategori.getSelectedItem().toString();
            String sql = "UPDATE tbdok "
                    + "SET kode_dokumen=\""+kode_dokumen+"\","
                    + "nama_dokumen=\""+nama_dokumen+"\","
                    + "kategori_dokumen_isi=\""+kategori_dokumen_isi+"\","
                    + "lokasi_dokumen=\""+lokasi_dokumen+"\","
                    + "deskripsi_dokumen=\""+deskripsi_dokumen+"\","
                    + "tanggal=\""+tanggal+"\" WHERE id=\""+idBaris+"\"";
            Connection con;
            Statement st;
            try {
                con = Koneksi.sambungDB();
                st = con.createStatement();
                st.executeUpdate(sql);
                st.close();
                con.close();
                resetView();
                JOptionPane.showMessageDialog(this,"Data Telah Diubah!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }
    
    private void hapusData(int baris){
        Connection con;
        Statement st;
        try {
            con = Koneksi.sambungDB();
            st = con.createStatement();
            st.executeUpdate("DELETE FROM tbdok WHERE id="+baris);
            st.close();
            con.close();
            resetView();
            JOptionPane.showMessageDialog(this, "Data Telah Dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        p1 = new javax.swing.JPanel();
        areaSplit = new javax.swing.JSplitPane();
        p3 = new javax.swing.JPanel();
        lbl3 = new javax.swing.JLabel();
        cmbKategori = new javax.swing.JComboBox<>();
        txtKode = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        lbl1 = new javax.swing.JLabel();
        lbl2 = new javax.swing.JLabel();
        lbl4 = new javax.swing.JLabel();
        lbl6 = new javax.swing.JLabel();
        spr1 = new javax.swing.JSeparator();
        btn_simpan = new javax.swing.JButton();
        btn_tutup = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDesk = new javax.swing.JTextArea();
        lbl5 = new javax.swing.JLabel();
        txtLok = new javax.swing.JTextField();
        txtDate = new javax.swing.JTextField();
        browse = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel = new javax.swing.JTable();
        btnTambah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        keluar = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        lbl7 = new javax.swing.JLabel();
        p2 = new javax.swing.JPanel();
        lbl8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ARSIP DOKUMEN");
        setBackground(new java.awt.Color(153, 204, 255));
        setMinimumSize(new java.awt.Dimension(45, 25));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        p1.setBackground(new java.awt.Color(0, 153, 255));
        p1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        areaSplit.setDividerLocation(400);
        areaSplit.setDividerSize(6);
        areaSplit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        areaSplit.setOneTouchExpandable(true);

        p3.setBackground(new java.awt.Color(0, 153, 255));

        lbl3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl3.setForeground(new java.awt.Color(255, 255, 255));
        lbl3.setText("KATEGORI DOKUMEN");

        cmbKategori.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtKode.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtNama.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lbl1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl1.setForeground(new java.awt.Color(255, 255, 255));
        lbl1.setText("KODE DOKUMEN");

        lbl2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl2.setForeground(new java.awt.Color(255, 255, 255));
        lbl2.setText("NAMA DOKUMEN");

        lbl4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl4.setForeground(new java.awt.Color(255, 255, 255));
        lbl4.setText("LOKASI DOKUMEN");

        lbl6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl6.setForeground(new java.awt.Color(255, 255, 255));
        lbl6.setText("TANGGAL");

        btn_simpan.setBackground(new java.awt.Color(153, 204, 255));
        btn_simpan.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btn_simpan.setText("SIMPAN");
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });

        btn_tutup.setBackground(new java.awt.Color(153, 204, 255));
        btn_tutup.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btn_tutup.setText("TUTUP");
        btn_tutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_tutupActionPerformed(evt);
            }
        });

        txtDesk.setColumns(20);
        txtDesk.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDesk.setRows(5);
        jScrollPane2.setViewportView(txtDesk);

        lbl5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbl5.setForeground(new java.awt.Color(255, 255, 255));
        lbl5.setText("DESKRIPSI DOKUMEN");

        txtLok.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        browse.setBackground(new java.awt.Color(153, 204, 255));
        browse.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        browse.setText("BROWSE");
        browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout p3Layout = new javax.swing.GroupLayout(p3);
        p3.setLayout(p3Layout);
        p3Layout.setHorizontalGroup(
            p3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p3Layout.createSequentialGroup()
                .addGroup(p3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(p3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl4)
                        .addGap(29, 29, 29)
                        .addComponent(txtLok, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browse, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(p3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl1)
                        .addGap(39, 39, 39)
                        .addComponent(txtKode))
                    .addGroup(p3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl2)
                        .addGap(37, 37, 37)
                        .addComponent(txtNama))
                    .addGroup(p3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl3)
                        .addGap(12, 12, 12)
                        .addComponent(cmbKategori, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(p3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl5)
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane2))
                    .addGroup(p3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl6)
                        .addGap(76, 76, 76)
                        .addComponent(txtDate))
                    .addGroup(p3Layout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(btn_tutup, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_simpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p3Layout.createSequentialGroup()
                .addComponent(spr1)
                .addContainerGap())
        );
        p3Layout.setVerticalGroup(
            p3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(p3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl1))
                    .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(p3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl2))
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(p3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lbl3))
                    .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(p3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(p3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(p3Layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(lbl4))
                        .addComponent(txtLok, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(browse, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(p3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p3Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(lbl5))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(p3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl6))
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(spr1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(p3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_tutup, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        areaSplit.setLeftComponent(p3);

        tabel.setBackground(new java.awt.Color(153, 204, 255));
        tabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel);

        areaSplit.setRightComponent(jScrollPane1);

        btnTambah.setBackground(new java.awt.Color(153, 204, 255));
        btnTambah.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnTambah.setText("TAMBAH DATA");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnHapus.setBackground(new java.awt.Color(153, 204, 255));
        btnHapus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnHapus.setText("HAPUS DATA");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnUbah.setBackground(new java.awt.Color(153, 204, 255));
        btnUbah.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnUbah.setText("UBAH DATA");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        keluar.setBackground(new java.awt.Color(153, 204, 255));
        keluar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        keluar.setText("KELUAR");
        keluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keluarActionPerformed(evt);
            }
        });

        txtCari.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });

        lbl7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbl7.setForeground(new java.awt.Color(255, 255, 255));
        lbl7.setText("CARI");

        javax.swing.GroupLayout p1Layout = new javax.swing.GroupLayout(p1);
        p1.setLayout(p1Layout);
        p1Layout.setHorizontalGroup(
            p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(btnTambah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapus)
                .addGap(6, 6, 6)
                .addComponent(btnUbah)
                .addGap(6, 6, 6)
                .addComponent(keluar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
            .addComponent(areaSplit, javax.swing.GroupLayout.DEFAULT_SIZE, 897, Short.MAX_VALUE)
        );
        p1Layout.setVerticalGroup(
            p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(p1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(keluar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl7)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(areaSplit, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
        );

        p2.setBackground(new java.awt.Color(153, 204, 255));
        p2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        p2.setLayout(new java.awt.BorderLayout());

        lbl8.setFont(new java.awt.Font("Tahoma", 1, 33)); // NOI18N
        lbl8.setForeground(new java.awt.Color(255, 255, 255));
        lbl8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl8.setText("A    R    S    I    P       D    O    K    U    M    E    N");
        lbl8.setPreferredSize(new java.awt.Dimension(747, 50));
        p2.add(lbl8, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(p2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(p2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(p1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        areaSplit.setDividerLocation(0.3);
    }//GEN-LAST:event_formComponentResized

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if(idBaris == 1) {
            hapusData(idBaris);
        }else{
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus");
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        role = "Tambah";
        btn_simpan.setText("SIMPAN");
        idBaris = 0;
        resetForm();
        showForm(true);
        btnHapus.setEnabled(false);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void tabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelMouseClicked
        role = "Ubah";
        int row = tabel.getRowCount();
        if(row > 0) {
            int sel = tabel.getSelectedRow();
            if(sel != -1){
                pilihData(tabel.getValueAt(sel, 0).toString());
                btn_simpan.setText("UBAH DATA");
            }
        }
    }//GEN-LAST:event_tabelMouseClicked

    private void btn_tutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_tutupActionPerformed
        resetForm();
        showForm(false);
        btnHapus.setEnabled(false);
        idBaris = 0;
    }//GEN-LAST:event_btn_tutupActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        if(role.equals("Tambah")){
            simpanData();
        } else if (role.equals("Ubah")){
            ubahData();
        }
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void keluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keluarActionPerformed
        Login ad = new Login();
        ad.setVisible(true);
        dispose();
    }//GEN-LAST:event_keluarActionPerformed

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
        String key = txtCari.getText();
        showData(key);
    }//GEN-LAST:event_txtCariKeyReleased

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if ("btnUbah".equals(btnUbah.getText())) {
            txtKode.requestFocus();
        } else {
            String kode_dokumen = txtKode.getText();
            String nama_dokumen = txtNama.getText();
            String kategori_dokumen = (String) cmbKategori.getSelectedItem();
            String deskripsi_dokumen = txtDesk.getText();
            String lokasi_dokumen = txtLok.getText();
            String tanggal = txtDate.getText();
            try {
                Statement st = Koneksi.sambungDB().createStatement();
                st.executeUpdate("update tbdok SET kode_dokumen='" + kode_dokumen + "'," + "nama_dokumen='" + nama_dokumen + 
                "'," + "kategori_dokumen='" + kategori_dokumen + "'," + "deskripsi_dokumen='" + deskripsi_dokumen +  
                "'," + "lokasi_dokumen='" + lokasi_dokumen + "'" + "tanggal='" + tanggal + "'," + 
                "WHERE kode_dokumen = '" + kode_dokumen + "'");
                st.close();
                JOptionPane.showMessageDialog(null, "Data Berhasil diubah");
            } catch (HeadlessException | SQLException t) {
                JOptionPane.showMessageDialog(null, "Data Gagal diubah");
            }
            aturModelTabel();
        }
    }//GEN-LAST:event_btnUbahActionPerformed

    private void browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        filename = f.getAbsolutePath();
        txtLok.setText(filename);        
    }//GEN-LAST:event_browseActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AppArsip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppArsip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppArsip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppArsip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AppArsip().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane areaSplit;
    private javax.swing.JButton browse;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_tutup;
    private javax.swing.JComboBox<String> cmbKategori;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton keluar;
    private javax.swing.JLabel lbl1;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lbl3;
    private javax.swing.JLabel lbl4;
    private javax.swing.JLabel lbl5;
    private javax.swing.JLabel lbl6;
    private javax.swing.JLabel lbl7;
    private javax.swing.JLabel lbl8;
    private javax.swing.JPanel p1;
    private javax.swing.JPanel p2;
    private javax.swing.JPanel p3;
    private javax.swing.JSeparator spr1;
    private javax.swing.JTable tabel;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextArea txtDesk;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtLok;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables
}