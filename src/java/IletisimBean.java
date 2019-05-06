
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author ss
 */
@ManagedBean
@SessionScoped
public class IletisimBean {

    private String iletmail;
    private String iletnumara;
    private String iletaciklama;
    private String iletadi;
    private String hata;

    DataSource dataSource;

    public IletisimBean() {
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("jdbc/addressbook");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public String getIletadi() {
        return iletadi;
    }

    public void setIletadi(String iletadi) {
        this.iletadi = iletadi;
    }

    public String getIletmail() {
        return iletmail;
    }

    public void setIletmail(String iletmail) {
        this.iletmail = iletmail;
    }

    public String getIletnumara() {
        return iletnumara;
    }

    public void setIletnumara(String iletnumara) {
        this.iletnumara = iletnumara;
    }

    public String getIletaciklama() {
        return iletaciklama;
    }

    public void setIletaciklama(String iletaciklama) {
        this.iletaciklama = iletaciklama;
    }

    public String getHata() {
        return hata;
    }

    public void setHata(String hata) {
        this.hata = hata;
    }

    public ResultSet iletisimGetir() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM ILETISIM");
            CachedRowSet resultSet1 = new com.sun.rowset.CachedRowSetImpl();
            resultSet1.populate(stmt.executeQuery());
            return resultSet1;
        } finally {
            connection.close();
        }
    }

    public String iletisimEkle() throws SQLException {

        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to conntect to DataSource");
        }

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO ILETISIM "
                    + "(ILET_ADI,ILET_NUMARA,ILET_MAIL,RAN_ACIKLAMA) "
                    + "VALUES ( ?, ?, ?, ?)");
            statement.setString(1, getIletadi());
            statement.setString(2, getIletnumara());
            statement.setString(3, getIletmail());
            statement.setString(4, getIletaciklama());
            statement.executeUpdate();
            return "/hakkimizda";

        } catch (Exception e) {
            hata = e.getMessage();
            System.out.println("ee:" + hata);
            return "/secured/randevu.xhtml?faces-redirect=true";
        } finally {
            connection.close();
        }

    }

}
