package dal;

import model.DadosDiarios;
import model.DadosHorarios;
import model.Localizacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;


public class DAO<T> {
    private Conexao conn;

    public DAO() throws SQLException, ClassNotFoundException {
        conn = new Conexao();
    }

    public void inserir(T obj, int idLocalizacao) throws SQLException {
        String sql;
        PreparedStatement st;

        if (obj instanceof DadosDiarios) {
            sql = "INSERT INTO dados_diarios (id_localizacao, data, temperatura_max, temperatura_min, precipitacao) VALUES (?, ?, ?, ?, ?)";
            DadosDiarios d = (DadosDiarios) obj;
            st = conn.prepareStatement(sql);
            st.setInt(1, idLocalizacao);
            st.setDate(2, Date.valueOf(d.getData()));
            st.setDouble(3, d.getTemperaturaMaxima());
            st.setDouble(4, d.getTemperaturaMinima());
            st.setDouble(5, d.getPrecipitacao());

        } else if (obj instanceof DadosHorarios) {
            sql = "INSERT INTO dados_horarios (id_localizacao, horario, temperatura, sensacao_termica, precipitacao) VALUES (?, ?, ?, ?, ?)";
            DadosHorarios h = (DadosHorarios) obj;
            st = conn.prepareStatement(sql);
            st.setInt(1, idLocalizacao);
            st.setTimestamp(2, Timestamp.valueOf(h.getHora()));
            st.setDouble(3, h.getTemperatura());
            st.setDouble(4, h.getSensacaoTermica());
            st.setDouble(5, h.getPrecipitacao());

        } else {
            throw new IllegalArgumentException("Tipo de objeto não suportado para inserção: " + obj.getClass().getName());
        }

        st.executeUpdate();
        st.close();
    }

    public List<Localizacao> listarLocalizacoes() throws SQLException {
        List<Localizacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM localizacao ORDER BY cidade";
        PreparedStatement st = conn.prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String cidade = rs.getString("cidade");
            double latitude = rs.getDouble("latitude");
            double longitude = rs.getDouble("longitude");
            Localizacao loc = new Localizacao(id, cidade, latitude, longitude, null);
            lista.add(loc);
        }

        rs.close();
        st.close();
        return lista;
    }

    public ResultSet listarHistoricoPorData(int idLocalizacao, String dataInicio, String dataFim) throws SQLException {
        String sql = "SELECT " +
                "h.horario AS 'Data e Hora', " +
                "h.temperatura AS 'Temperatura (C)', " +
                "h.sensacao_termica AS 'Sensação (C)', " +
                "d.temperatura_max AS 'Máxima Dia (C)', " +
                "d.temperatura_min AS 'Mínima Dia (C)' " +
                "FROM dados_horarios h " +
                "JOIN dados_diarios d ON h.id_localizacao = d.id_localizacao AND DATE(h.horario) = d.data " +
                "WHERE h.id_localizacao = ? ";

        if (dataInicio != null && !dataInicio.trim().isEmpty()) {
            sql += " AND DATE(h.horario) >= ? ";
        }
        if (dataFim != null && !dataFim.trim().isEmpty()) {
            sql += " AND DATE(h.horario) <= ? ";
        }
        sql += " ORDER BY h.horario DESC";

        PreparedStatement st = conn.prepareStatement(sql);
        int paramIndex = 1;
        st.setInt(paramIndex++, idLocalizacao);

        if (dataInicio != null && !dataInicio.trim().isEmpty()) {
            st.setString(paramIndex++, dataInicio);
        }
        if (dataFim != null && !dataFim.trim().isEmpty()) {
            st.setString(paramIndex++, dataFim);
        }

        return st.executeQuery();
    }

    public String getConfiguracao(String chave) throws SQLException {
        String sql = "SELECT valor FROM configuracoes WHERE chave = ?";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, chave);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            return rs.getString("valor");
        }
        rs.close();
        st.close();
        return null;
    }

    public void salvarConfiguracao(String chave, String valor) throws SQLException {
        String sql = "INSERT INTO configuracoes (chave, valor) VALUES (?, ?) ON DUPLICATE KEY UPDATE valor = ?";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, chave);
        st.setString(2, valor);
        st.setString(3, valor);
        st.executeUpdate();
        st.close();
    }

    public void inserirLocalizacao(Localizacao loc) throws SQLException {
        String sql = "INSERT INTO localizacao (cidade, latitude, longitude) VALUES (?, ?, ?)";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, loc.getCidade());
        st.setDouble(2, loc.getLatitude());
        st.setDouble(3, loc.getLongitude());
        st.executeUpdate();
        st.close();
    }
}