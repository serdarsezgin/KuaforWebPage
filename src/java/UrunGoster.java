/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Sammy
 */
@ManagedBean
@RequestScoped
public class UrunGoster {

    /**
     * Creates a new instance of UrunGoster
     */
    DataSource dataSource;
    private String id;
    private String ad;
    private String aciklama;
    private String miktar;
    private String fiyat;
    private String foto;
    String hata;
    
    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getMiktar() {
        return miktar;
    }

    public void setMiktar(String miktar) {
        this.miktar = miktar;
    }

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }

    public String getHata() {
        return hata;
    }

    public void setHata(String hata) {
        this.hata = hata;
    }
    
    
    

    public UrunGoster() {
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("jdbc/addressbook");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    
    public String urunBilgileri(String id) throws SQLException {
        

        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        // obtain a connection from the connection pool
        Connection connection = dataSource.getConnection();

        // check whether connection was successful
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {
            String sql = "Select * from URUNLER where ID= ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            int num = Integer.valueOf(id);
            ps.setInt(1, num);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                setId(rs.getString("ID"));
                setAd(rs.getString("URUN_ADI"));
                setAciklama(rs.getString("ACIKLAMA"));
                setMiktar(rs.getString("MIKTAR"));
                setFiyat(rs.getString("FIYAT"));
                setFoto(rs.getString("FOTO"));
            }

        } // end try
        catch (Exception e) {
            hata = e.getMessage();
            return "/hakkimizda";
            
        } finally {
            connection.close();
        }

        return "/urunbilgi";

    }

}

