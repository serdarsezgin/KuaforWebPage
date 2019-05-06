/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author Sammy
 */
@ManagedBean
@RequestScoped
public class UyeBean {

    private String kuladi;
    private String email;
    private String telno;
    private String sifre;

    private String userId;
    
    String hata;
    CachedRowSet rowSet = null;
    DataSource dataSource;
    String check;
    ResultSet result;

    public String getUserId() {
        return userId;
    }


    public String getKuladi() {
        return kuladi;
    }

    public void setKuladi(String kuladi) {
        this.kuladi = kuladi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelno() {
        return telno;
    }

    public String getHata() {
        return hata;
    }

    public void setHata(String hata) {
        this.hata = hata;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public UyeBean() {
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("jdbc/addressbook");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public String hashFunc(String sifre) {

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

    public String uyeEkle() throws SQLException {

        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to conntect to DataSource");
        }

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO uyeler "
                    + "(kul_adi,email,telno,sifre)"
                    + "VALUES ( ?, ?, ?, ? )");
            statement.setString(1, getKuladi());
            statement.setString(2, getEmail());
            statement.setString(3, getTelno());
            statement.setString(4, hashFunc(getSifre()));

            statement.executeUpdate();
            return "index";

        } catch (Exception e) {
            hata = e.getMessage();
            return "/secured/randevu";
        } finally {
            connection.close();
        }

    }

   

}
