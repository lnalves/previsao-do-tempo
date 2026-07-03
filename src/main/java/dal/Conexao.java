package dal;

import java.sql.*;


public class Conexao {

    private final String driver = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost/" + "climadb";
    private final String usuario = "root";
    private final String senha = "1234";
    private Connection conexao;

    public Conexao() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        conexao = DriverManager.getConnection(url, usuario, senha);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return conexao.prepareStatement(sql);
    }

    public void fechar() throws SQLException {
        if (conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
    }
}