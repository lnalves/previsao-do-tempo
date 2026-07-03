package ui;

import api.ForecastResult;
import domain.DailyForecast;
import domain.Location;
import domain.WeatherReading;
import service.LocationService;
import service.WeatherService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class MainFrame extends JFrame {

    private final WeatherService weatherService = new WeatherService();
    private final LocationService locationService = new LocationService();

    private JComboBox<Location> cmbLocation;
    private JLabel lblLocation;
    private JLabel lblReadAt;
    private JLabel lblTemperature;
    private JLabel lblPrecipitation;
    private JPanel pnlForecastGrid;

    public MainFrame() {
        super("Previsão do Tempo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        initMenu();
        initUI();
        reloadLocations();
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("Arquivo");
        JMenuItem itemAddLocation = new JMenuItem("Adicionar Cidade");
        JMenuItem itemExit = new JMenuItem("Sair");
        menuFile.add(itemAddLocation);
        menuFile.add(itemExit);

        JMenu menuData = new JMenu("Dados");
        JMenuItem itemRefresh = new JMenuItem("Atualizar dados");
        JMenuItem itemHistory = new JMenuItem("Ver Histórico");
        menuData.add(itemRefresh);
        menuData.add(itemHistory);

        JMenu menuHelp = new JMenu("Ajuda");
        JMenuItem itemSettings = new JMenuItem("Configurações");
        JMenuItem itemAbout = new JMenuItem("Sobre");
        menuHelp.add(itemSettings);
        menuHelp.add(itemAbout);

        menuBar.add(menuFile);
        menuBar.add(menuData);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);

        itemExit.addActionListener(e -> dispose());
        itemRefresh.addActionListener(e -> refreshWeather());
        itemHistory.addActionListener(e -> new HistoryFrame(weatherService, locationService).setVisible(true));
        itemSettings.addActionListener(e -> new SettingsDialog(this, weatherService).setVisible(true));
        itemAbout.addActionListener(e -> new AboutDialog(this).setVisible(true));
        itemAddLocation.addActionListener(e ->
                new AddLocationDialog(this, locationService, this::reloadLocations).setVisible(true));
    }

    private void initUI() {
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlTop.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblSelect = new JLabel("Selecione a cidade:");
        lblSelect.setFont(UiStyle.PLAIN);
        pnlTop.add(lblSelect);

        cmbLocation = new JComboBox<>();
        cmbLocation.setFont(UiStyle.PLAIN);
        cmbLocation.setPreferredSize(new Dimension(260, 25));
        cmbLocation.addActionListener(e -> {
            if (cmbLocation.getSelectedItem() != null) {
                refreshWeather();
            }
        });
        pnlTop.add(cmbLocation);

        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JPanel pnlCurrent = createWidget("Tempo Agora");
        lblLocation = createInfoLabel("Local: [Selecione uma cidade]");
        lblReadAt = createInfoLabel("Data/Hora: [N/A]");
        lblTemperature = createInfoLabel("Temperatura: [N/A]");
        lblPrecipitation = createInfoLabel("Precipitação: [N/A]");
        pnlCurrent.add(lblLocation);
        pnlCurrent.add(lblReadAt);
        pnlCurrent.add(lblTemperature);
        pnlCurrent.add(lblPrecipitation);

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlCenter.add(pnlCurrent, gbc);

        JPanel pnlForecastWidget = createWidget("Previsão Diária");
        pnlForecastGrid = new JPanel();
        pnlForecastWidget.add(pnlForecastGrid);
        gbc.gridy = 1;
        pnlCenter.add(pnlForecastWidget, gbc);

        add(pnlTop, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UiStyle.PLAIN);
        return label;
    }

    private JPanel createWidget(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiStyle.BORDER),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UiStyle.TITLE);
        lblTitle.setForeground(UiStyle.ACCENT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(10));
        return panel;
    }

    private void reloadLocations() {
        try {
            List<Location> locations = locationService.listAll();
            Location selected = (Location) cmbLocation.getSelectedItem();
            cmbLocation.removeAllItems();
            for (Location location : locations) {
                cmbLocation.addItem(location);
            }
            if (selected != null) {
                cmbLocation.setSelectedItem(selected);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cidades do banco de dados:\n" + e.getMessage(),
                    "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Network and DB work runs off the EDT; only the display update touches Swing. */
    private void refreshWeather() {
        Location location = (Location) cmbLocation.getSelectedItem();
        if (location == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma cidade primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<ForecastResult, Void>() {
            @Override
            protected ForecastResult doInBackground() throws Exception {
                return weatherService.fetchAndStore(location);
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    displayWeather(location, get());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Erro ao buscar dados do tempo:\n" + e.getCause().getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void displayWeather(Location location, ForecastResult result) {
        WeatherReading reading = result.reading();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        lblLocation.setText(String.format("Local: %s (%.2f, %.2f)", location.name(), location.latitude(), location.longitude()));
        lblReadAt.setText("Data/Hora: " + reading.readAt().format(formatter));
        lblTemperature.setText(String.format("Temperatura: %.1f°C (Sensação: %.1f°C)", reading.temperature(), reading.feelsLike()));
        lblPrecipitation.setText(String.format("Precipitação: %.1f mm", reading.precipitation()));

        pnlForecastGrid.removeAll();
        List<DailyForecast> forecasts = result.dailyForecasts();
        pnlForecastGrid.setLayout(new GridLayout(1, forecasts.size(), 10, 10));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");

        for (DailyForecast forecast : forecasts) {
            JPanel pnlDay = new JPanel();
            pnlDay.setLayout(new BoxLayout(pnlDay, BoxLayout.Y_AXIS));
            pnlDay.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    new EmptyBorder(10, 10, 10, 10)));
            pnlDay.setBackground(Color.WHITE);

            JLabel lblDate = new JLabel(forecast.date().format(dateFormatter));
            lblDate.setFont(UiStyle.BOLD);
            lblDate.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblMax = new JLabel(String.format("Max: %.1f°C", forecast.temperatureMax()));
            lblMax.setForeground(UiStyle.WARM);
            lblMax.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblMin = new JLabel(String.format("Min: %.1f°C", forecast.temperatureMin()));
            lblMin.setForeground(UiStyle.ACCENT);
            lblMin.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblRain = new JLabel(String.format("Chuva: %.0f%%", forecast.precipitationProbability()));
            lblRain.setAlignmentX(Component.CENTER_ALIGNMENT);

            pnlDay.add(lblDate);
            pnlDay.add(Box.createVerticalStrut(8));
            pnlDay.add(lblMax);
            pnlDay.add(lblMin);
            pnlDay.add(Box.createVerticalStrut(5));
            pnlDay.add(lblRain);

            pnlForecastGrid.add(pnlDay);
        }

        pnlForecastGrid.revalidate();
        pnlForecastGrid.repaint();
    }
}
