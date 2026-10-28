package koneksi;
import java.sql.Connection;

public class CekKoneksi {
    public static void main(String[] args) {
        Connection c = koneksi.configDB();
        if (c != null) {
            System.out.println("HORE! NetBeans Berhasil Terhubung ke Laragon!");
        } else {
            System.out.println("GAGAL TERHUBUNG!");
        }
    }
}
