
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;

/**
 * Simple login bean.
 *
 * @author itcuties
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 7765876811740798583L;

    // Simple user database :)
    private String kuladigiris;
    private String sifregiris;
    private String userId;
    private boolean loggedIn;
    private boolean userLoggedIn;
    private boolean adminLoggedIn;
    private String kul_adi_2;

    public String getKul_adi_2() {
        return kul_adi_2;
    }

    public void setKul_adi_2(String kul_adi_2) {
        this.kul_adi_2 = kul_adi_2;
    }

    public boolean isUserLoggedIn() {
        return userLoggedIn;
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    public boolean isAdminLoggedIn() {
        return adminLoggedIn;
    }

    public void setAdminLoggedIn(boolean adminLoggedIn) {
        this.adminLoggedIn = adminLoggedIn;
    }

    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    String hata;
    CachedRowSet rowSet = null;
    DataSource dataSource;
    String checkUser;
    String checkAdmin;

    ResultSet resultUser;
    ResultSet resultAdmin;

    private String username;
    private String password;

    private boolean isLoggedIn;

    private String userAdi;
    private String userEmail;
    private String userTel;

    // @ManagedProperty(value = "#{navigationBean}")
    // private NavigationBean navigationBean;
    public String getUserAdi() {
        return userAdi;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserTel() {
        return userTel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKuladigiris() {
        return kuladigiris;
    }

    public void setKuladigiris(String kuladigiris) {
        this.kuladigiris = kuladigiris;
    }

    public String getSifregiris() {
        return sifregiris;
    }

    public void setSifregiris(String sifregiris) {
        this.sifregiris = sifregiris;
    }

    public LoginBean() {
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("jdbc/addressbook");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public String hashFunc2(String sifre) {

        String passwordToHash = sifre;
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;

    }

    public String uyeGiris() throws SQLException {

        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to conntect to DataSource");
        }

        try {
            checkUser = "SELECT * FROM UYELER WHERE kul_adi = '" + getKuladigiris()
                    + "' AND sifre = '" + hashFunc2(getSifregiris()) + "'";
            checkAdmin = "SELECT * FROM ADMIN WHERE KULADI = '" + getKuladigiris()
                    + "' AND SIFRE = '" + hashFunc2(getSifregiris()) + "'";
            Statement statement1 = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            Statement statement2 = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            resultUser = null;
            resultAdmin = null;
            resultUser = statement1.executeQuery(checkUser);
            resultAdmin = statement2.executeQuery(checkAdmin);

            if (resultUser.isBeforeFirst()) {
                while (resultUser.next()) {
                    kul_adi_2 = resultUser.getString("KUL_ADI");
                    userId = resultUser.getString("ID");
                    userAdi = resultUser.getString("KUL_ADI");
                    userEmail = resultUser.getString("EMAIL");
                    userTel = resultUser.getString("TELNO");

                }
                userLoggedIn = true;
                return "/index.xhtml?faces-redirect=true";
            } else if (resultAdmin.isBeforeFirst()) {
                adminLoggedIn = true;
                return "/secured/admin.xhtml?faces-redirect=true";

            } else {
                return "/girisyap";

            }

        } catch (Exception e) {
            hata = e.getMessage();
            return "/girisyap";
        } finally {
            connection.close();
        }

    }

    /**
     * Logout operation.
     *
     * @return
     */
    public void doLogout() throws IOException {
        // Set the paremeter indicating that user is logged in to false
        userLoggedIn = false;

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        FacesMessage msg = new FacesMessage("Logout success!", "INFO MSG");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);

        ec.redirect(ec.getRequestContextPath() + "/faces/index.xhtml");

        // Set logout message
        // return navigationBean.toIndex();
    }

    // ------------------------------
    // Getters & Setters 
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /*  public void setNavigationBean(NavigationBean navigationBean) {
     this.navigationBean = navigationBean;
     } */
}
