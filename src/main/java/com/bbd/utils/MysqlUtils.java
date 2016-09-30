package com.bbd.utils;

import com.bbd.domain.Page;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by bbd on 2016/9/29.
 *
 create table spider(
    create_dt timestamp default CURRENT_TIMESTAMP,
    id int(11) PRIMARY KEY AUTO_INCREMENT,
    title varchar(252),
    status varchar(64),
    price varchar(20),
    address varchar(252),
    startdate varchar(20),
    launchdate varchar(20),
    decorate_status varchar(64),
    data_url varchar(252)
 )ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
 */
public class MysqlUtils {

    String driver = "com.mysql.jdbc.Driver";
    String url = Config.mysql_url;
    String username = Config.mysql_username;
    String password = Config.mysql_password;

    Connection conn = null;

    public MysqlUtils() {
        try {
            Class.forName(driver);
            conn = (Connection) DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insert(Page page){
        String sql = "insert into spider(title,status,price,address,startdate,launchdate,decorate_status,data_url) values (?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt;
        int i = 0;
        try {
            String goodsId = page.getGoodsId();
            Map<String, String> values = page.getValues();
            pstmt = (PreparedStatement) conn.prepareStatement(sql);

            pstmt.setString(1,values.get("title"));
            pstmt.setString(2,values.get("status"));
            pstmt.setString(3,values.get("price"));
            pstmt.setString(4,values.get("address"));
            pstmt.setString(5,values.get("startdate"));
            pstmt.setString(6,values.get("launchdate"));
            pstmt.setString(7,values.get("decorate_status"));
            pstmt.setString(8,page.getUrl());
            i = pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }




}
