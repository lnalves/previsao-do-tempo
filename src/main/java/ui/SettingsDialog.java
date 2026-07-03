package ui;

import service.WeatherService;

import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {

    private final WeatherService weatherService;
    private JTextField txtApiUrl;

    public SettingsDialog(Frame owner, WeatherService weatherService) {
        super(owner, "Configurações", true);
        this.weatherService = weatherService;
        setSize(520, 180);
        setLocationRelativeTo(owner);
        setResizable(false);
        initUI();
        load();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel lblApi = new JLabel("URL Base da API:");
        lblApi.setFont(UiStyle.PLAIN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(lblApi, gbc);

        txtApiUrl = new JTextField(30);
        txtApiUrl.setFont(UiStyle.PLAIN);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(txtApiUrl, gbc);

        JButton btnSave = new JButton("Salvar");
        btnSave.setFont(UiStyle.BOLD);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(btnSave, gbc);

        add(panel);
        btnSave.addActionListener(e -> save());
    }

    private void load() {
        try {
            txtApiUrl.setText(weatherService.getApiBaseUrl());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar configurações: " + e.getMessage());
        }
    }

    private void save() {
        try {
            weatherService.saveApiBaseUrl(txtApiUrl.getText().trim());
            JOptionPane.showMessageDialog(this, "Configurações salvas com sucesso!");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar configurações: " + e.getMessage());
        }
    }
}
