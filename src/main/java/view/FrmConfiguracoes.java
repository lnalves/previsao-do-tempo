package view;

import dal.DAO;

import javax.swing.*;
import java.awt.*;

class FrmConfiguracoes extends JFrame {
    private JTextField txtApiUrl;
    private JButton btnSalvar;
    private DAO dao;

    public FrmConfiguracoes() {
        super("Configurações");
        setSize(520, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        try {
            dao = new DAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro de conexão: " + e.getMessage());
            return;
        }

        initUI();
        carregarConfiguracoes();
    }

    private void initUI() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel lblApi = new JLabel("URL Base da API:");
        lblApi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        pnl.add(lblApi, gbc);

        txtApiUrl = new JTextField(30);
        txtApiUrl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        pnl.add(txtApiUrl, gbc);

        btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(52, 152, 219));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        pnl.add(btnSalvar, gbc);

        add(pnl);

        btnSalvar.addActionListener(e -> salvarConfiguracoes());
    }

    private void carregarConfiguracoes() {
        try {
            String apiUrl = dao.getConfiguracao("API_BASE_URL");
            txtApiUrl.setText(apiUrl);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar configurações: " + e.getMessage());
        }
    }

    private void salvarConfiguracoes() {
        try {
            dao.salvarConfiguracao("API_BASE_URL", txtApiUrl.getText());
            JOptionPane.showMessageDialog(this, "Configurações salvas com sucesso!");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar configurações: " + e.getMessage());
        }
    }
}