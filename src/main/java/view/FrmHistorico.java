package view;

import dal.DAO;
import model.Localizacao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

class FrmHistorico extends JFrame {
    private JComboBox<Localizacao> cmbCidade;
    private JTextField txtDataInicio;
    private JTextField txtDataFim;
    private JButton btnBuscar;
    private JTable tblResultados;
    private JScrollPane scrollPane;
    private DAO dao;

    public FrmHistorico() {
        super("Histórico de Previsões");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            dao = new DAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + e.getMessage(), "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            return;
        }

        initUI();
        carregarCidades();
    }

    private void initUI() {
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));
        pnlFiltros.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        cmbCidade = new JComboBox<>();
        cmbCidade.setPreferredSize(new Dimension(200, 25));

        txtDataInicio = new JTextField(10);
        txtDataFim = new JTextField(10);

        btnBuscar = new JButton("Buscar Histórico");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));

        pnlFiltros.add(new JLabel("Cidade:"));
        pnlFiltros.add(cmbCidade);
        pnlFiltros.add(new JLabel("Data Início (AAAA-MM-DD):"));
        pnlFiltros.add(txtDataInicio);
        pnlFiltros.add(new JLabel("Data Fim (AAAA-MM-DD):"));
        pnlFiltros.add(txtDataFim);
        pnlFiltros.add(btnBuscar);

        tblResultados = new JTable();
        scrollPane = new JScrollPane(tblResultados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));

        setLayout(new BorderLayout(10, 10));
        add(pnlFiltros, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnBuscar.addActionListener(this::buscarHistorico);
    }

    private void carregarCidades() {
        try {
            List<Localizacao> cidades = dao.listarLocalizacoes();
            for (Localizacao cidade : cidades) {
                cmbCidade.addItem(cidade);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cidades: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarHistorico(ActionEvent e) {
        Localizacao cidadeSelecionada = (Localizacao) cmbCidade.getSelectedItem();
        if (cidadeSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma cidade.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dataInicio = txtDataInicio.getText();
        String dataFim = txtDataFim.getText();

        try {
            ResultSet rs = dao.listarHistoricoPorData(cidadeSelecionada.getId(), dataInicio, dataFim);
            tblResultados.setModel(buildTableModel(rs));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar histórico: " + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();

        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<>();

        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }
}