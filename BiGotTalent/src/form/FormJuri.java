/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form;
import koneksi.koneksi;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author muham
 */
public class FormJuri extends javax.swing.JFrame {
    private JTable tblPeserta;
    private DefaultTableModel model;
    private JSpinner spnNilai;
    private JLabel lblHasil;
    private JTextArea txtCatatan;
    private JButton btnSimpan, btnLogout;
    private int selectedUserId = -1;
    private static final int NILAI_LULUS = 70;

    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormJuri.class.getName());

    /**
     * Creates new form FormJuri
     */
    public FormJuri() {
        setTitle("Dashboard Juri - BI GOT TALENT");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(750, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Tabel peserta
        model = new DefaultTableModel(
                new String[]{"ID User", "Username", "Lomba", "Nilai", "Status", "Catatan"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tblPeserta = new JTable(model);
        tblPeserta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPeserta.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) pilihBaris();
        });
        add(new JScrollPane(tblPeserta), BorderLayout.CENTER);

        // Panel penilaian
        JPanel panelNilai = new JPanel(new BorderLayout(5, 5));
        panelNilai.setBorder(BorderFactory.createTitledBorder("Penilaian"));

        JPanel atas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        atas.add(new JLabel("Nilai (0-100):"));
        spnNilai = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        atas.add(spnNilai);
        lblHasil = new JLabel();
        atas.add(lblHasil);
        panelNilai.add(atas, BorderLayout.NORTH);
        spnNilai.addChangeListener(e -> updateHasil());
        updateHasil();

        txtCatatan = new JTextArea(4, 40);
        txtCatatan.setLineWrap(true);
        txtCatatan.setWrapStyleWord(true);
        panelNilai.add(new JScrollPane(txtCatatan), BorderLayout.CENTER);

        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSimpan = new JButton("Simpan Nilai");
        btnLogout = new JButton("Logout");
        bawah.add(btnSimpan);
        bawah.add(btnLogout);
        panelNilai.add(bawah, BorderLayout.SOUTH);

        add(panelNilai, BorderLayout.SOUTH);

        btnSimpan.addActionListener(e -> simpanKeputusan());
        btnLogout.addActionListener(e -> {
            new FormLogin().setVisible(true);
            dispose();
        });

        loadData();
    }
    
    private String hasil(int nilai) {
        return nilai >= NILAI_LULUS ? "Lolos" : "Tidak Lolos";
    }

    private void updateHasil() {
        int nilai = (Integer) spnNilai.getValue();
        lblHasil.setText("→ " + hasil(nilai));
    }

    private void loadData() {
        model.setRowCount(0);
        selectedUserId = -1;
        String sql = "SELECT p.user_id, u.username, l.nama_lomba, p.nilai, p.status, p.catatan "
                + "FROM pendaftaran p "
                + "JOIN users u ON p.user_id = u.id "
                + "JOIN lomba l ON p.lomba_id = l.id";
        try {
            Connection c = koneksi.configDB();
            Statement s = c.createStatement();
            ResultSet r = s.executeQuery(sql);
            while (r.next()) {
                model.addRow(new Object[]{
                    r.getInt("user_id"),
                    r.getString("username"),
                    r.getString("nama_lomba"),
                    r.getObject("nilai"),   // bisa null kalau belum dinilai
                    r.getString("status"),
                    r.getString("catatan")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }

    private void pilihBaris() {
        int row = tblPeserta.getSelectedRow();
        if (row < 0) return;
        selectedUserId = (int) model.getValueAt(row, 0);
        Object nilai = model.getValueAt(row, 3);
        Object catatan = model.getValueAt(row, 5);

        spnNilai.setValue(nilai == null ? 0 : ((Number) nilai).intValue());
        txtCatatan.setText(catatan == null ? "" : catatan.toString());
    }

    private void simpanKeputusan() {
        if (selectedUserId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih peserta di tabel dulu!");
            return;
        }
        int nilai = (Integer) spnNilai.getValue();
        String status = hasil(nilai);
        String catatan = txtCatatan.getText().trim();

        if (status.equals("Tidak Lolos") && catatan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nilai di bawah " + NILAI_LULUS
                    + ", isi catatan alasan tidak lolos!");
            return;
        }

        try {
            Connection c = koneksi.configDB();
            PreparedStatement ps = c.prepareStatement(
                    "UPDATE pendaftaran SET nilai = ?, status = ?, catatan = ? WHERE user_id = ?");
            ps.setInt(1, nilai);
            ps.setString(2, status);
            ps.setString(3, catatan.isEmpty() ? null : catatan);
            ps.setInt(4, selectedUserId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Tersimpan! Nilai " + nilai + " → " + status);
            loadData();
            txtCatatan.setText("");
            spnNilai.setValue(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + e.getMessage());
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FormJuri().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}