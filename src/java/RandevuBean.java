/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
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
public class RandevuBean {

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean uye;

    DataSource dataSource;
    private String rankuladi;
    private String rantelno;
    private String ranemail;
    private String ranulasim;
    private String rantarih;
    private String ranhizmet;
    private String ransube;
    private String ranavciklama;
    private String hata;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public RandevuBean() {
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("jdbc/addressbook");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public String getHata() {
        return hata;
    }

    public void setHata(String hata) {
        this.hata = hata;
    }

    public LoginBean getUye() {
        return uye;
    }

    public void setUye(LoginBean uye) {
        this.uye = uye;
    }

    public String getRankuladi() {
        rankuladi = uye.getUserAdi();
        return rankuladi;
    }

    public void setRankuladi(String rankuladi) {
        this.rankuladi = rankuladi;
    }

    public String getRantelno() {
        rantelno = uye.getUserTel();

        return rantelno;
    }

    public void setRantelno(String rantelno) {
        this.rantelno = rantelno;
    }

    public String getRanemail() {
        ranemail = uye.getUserEmail();

        return ranemail;
    }

    public void setRanemail(String ranemail) {
        this.ranemail = ranemail;
    }

    public String getRanulasim() {
        return ranulasim;
    }

    public void setRanulasim(String ranulasim) {
        this.ranulasim = ranulasim;
    }

    public String getRantarih() {
        return rantarih;
    }

    public void setRantarih(String rantarih) {
        this.rantarih = rantarih;
    }

    public String getRanhizmet() {
        return ranhizmet;
    }

    public void setRanhizmet(String ranhizmet) {
        this.ranhizmet = ranhizmet;
    }

    public String getRansube() {
        return ransube;
    }

    public void setRansube(String ransube) {
        this.ransube = ransube;
    }

    public String getRanavciklama() {
        return ranavciklama;
    }

    public void setRanavciklama(String ranavciklama) {
        this.ranavciklama = ranavciklama;
    }

    public Date cevir(String tarih) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date date = sdf1.parse(tarih);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }

    public ResultSet randevuGetir() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM URUNLER");
            CachedRowSet resultSet1 = new com.sun.rowset.CachedRowSetImpl();
            resultSet1.populate(stmt.executeQuery());
            return resultSet1;
        } finally {
            connection.close();
        }
    }

    public String randevuEkle() throws SQLException {

        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to conntect to DataSource");
        }

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO RANDEVULAR "
                    + "(RAN_KUL_ADI,RAN_TEL_NO,RAN_EMAIL,RAN_ULASIM,RAN_TARIH, RAN_HIZMET,RAN_SUBE,RAN_ACIKLAMA) "
                    + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )");
            statement.setString(1, getRankuladi());
            statement.setString(2, getRantelno());
            statement.setString(3, getRanemail());
            statement.setString(4, getRanulasim());
            statement.setDate(5, cevir(getRantarih()));
            statement.setString(6, getRanhizmet());
            statement.setString(7, getRansube());
            statement.setString(8, getRanavciklama());

            statement.executeUpdate();
            return "/index";

        } catch (Exception e) {
            hata = e.getMessage();
            System.out.println("ee:" + hata);
            return "/secured/randevu.xhtml?faces-redirect=true";
        } finally {
            connection.close();
        }

    }

}
