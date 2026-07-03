package view;

import dal.DAO;
import model.Localizacao;
import javax.swing.*;
import java.awt.*;

public class FrmAdicionarCidade extends JFrame {
    private JTextField txtCidade, txtLatitude, txtLongitude;
    private JButton btnSalvar;
    private DAO dao;

    public FrmAdicionarCidade() {
        super("Adicionar Nova Cidade");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));


        try {
            dao = new DAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro de conexão: " + e.getMessage());
            return;
        }

        add(new JLabel("Cidade:"));
        txtCidade = new JTextField();
        add(txtCidade);

        add(new JLabel("Latitude:"));
        txtLatitude = new JTextField();
        add(txtLatitude);

        add(new JLabel("Longitude:"));
        txtLongitude = new JTextField();
        add(txtLongitude);

        btnSalvar = new JButton("Salvar");
        add(new JLabel());
        add(btnSalvar);

        btnSalvar.addActionListener(e -> salvarCidade());
    }

    private void salvarCidade() {
        try {
            String cidade = txtCidade.getText();
            double latitude = Double.parseDouble(txtLatitude.getText());
            double longitude = Double.parseDouble(txtLongitude.getText());

            Localizacao novaCidade = new Localizacao(0, cidade, latitude, longitude, null);
            dao.inserirLocalizacao(novaCidade);

            JOptionPane.showMessageDialog(this, "Cidade adicionada com sucesso!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar a cidade: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}