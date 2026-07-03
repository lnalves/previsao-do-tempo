package ui;

import domain.Location;
import domain.ReadingHistoryEntry;
import service.LocationService;
import service.WeatherService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoryFrame extends JFrame {

    private static final DateTimeFormatter READ_AT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final WeatherService weatherService;
    private final LocationService locationService;

    private JComboBox<Location> cmbLocation;
    private JTextField txtFrom;
    private JTextField txtTo;
    private JTable tblResults;

    public HistoryFrame(WeatherService weatherService, LocationService locationService) {
        super("Histórico de Leituras");
        this.weatherService = weatherService;
        this.locationService = locationService;
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadLocations();
    }

    private void initUI() {
        JPanel pnlFilters = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlFilters.setBorder(BorderFactory.createTitledBorder("Filtros"));

        cmbLocation = new JComboBox<>();
        cmbLocation.setPreferredSize(new Dimension(240, 25));
        txtFrom = new JTextField(10);
        txtTo = new JTextField(10);
        JButton btnSearch = new JButton("Buscar Histórico");
        btnSearch.setFont(UiStyle.BOLD);

        pnlFilters.add(new JLabel("Cidade:"));
        pnlFilters.add(cmbLocation);
        pnlFilters.add(new JLabel("Data Início (AAAA-MM-DD):"));
        pnlFilters.add(txtFrom);
        pnlFilters.add(new JLabel("Data Fim (AAAA-MM-DD):"));
        pnlFilters.add(txtTo);
        pnlFilters.add(btnSearch);

        tblResults = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblResults);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));

        setLayout(new BorderLayout(10, 10));
        add(pnlFilters, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> search());
    }

    private void loadLocations() {
        try {
            for (Location location : locationService.listAll()) {
                cmbLocation.addItem(location);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cidades: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void search() {
        Location location = (Location) cmbLocation.getSelectedItem();
        if (location == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma cidade.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate from;
        LocalDate to;
        try {
            from = parseDate(txtFrom.getText());
            to = parseDate(txtTo.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datas devem estar no formato AAAA-MM-DD.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<ReadingHistoryEntry> entries = weatherService.getHistory(location, from, to);
            tblResults.setModel(buildTableModel(entries));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar histórico: " + e.getMessage(),
                    "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static LocalDate parseDate(String text) {
        return text == null || text.isBlank() ? null : LocalDate.parse(text.trim());
    }

    private static DefaultTableModel buildTableModel(List<ReadingHistoryEntry> entries) {
        String[] columns = {"Data e Hora", "Temperatura (°C)", "Sensação (°C)", "Máxima Dia (°C)", "Mínima Dia (°C)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (ReadingHistoryEntry entry : entries) {
            model.addRow(new Object[]{
                    entry.readAt().format(READ_AT_FORMAT),
                    entry.temperature(),
                    entry.feelsLike(),
                    entry.dayMax(),
                    entry.dayMin()});
        }
        return model;
    }
}
