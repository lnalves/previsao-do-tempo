package view;

import javax.swing.*;
import java.awt.*;

public class FrmSobre extends JFrame {

    public FrmSobre() {
        super("Sobre o App Previsão do Tempo");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel("App Previsão do Tempo");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblVersao = new JLabel("Versão única");
        lblVersao.setFont(new Font("Arial", Font.PLAIN, 12));
        lblVersao.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAutoresTitulo = new JLabel("Desenvolvido por:");
        lblAutoresTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblAutoresTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAutoresTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        JLabel lblAutor1 = new JLabel("Leonardo N. Alves");
        lblAutor1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblAutor2 = new JLabel("Lucas O. Dias");
        lblAutor2.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblAutor3 = new JLabel("João Antonio S. Pinto");
        lblAutor3.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblAutor4 = new JLabel("Roberto A. Giuliani");
        lblAutor4.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblAutor5 = new JLabel("Ryan M. Zemniaçak");
        lblAutor5.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblVersao);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(lblAutoresTitulo);
        panel.add(lblAutor1);
        panel.add(lblAutor2);
        panel.add(lblAutor3);
        panel.add(lblAutor4);
        panel.add(lblAutor5);
        panel.add(Box.createVerticalGlue());

        add(panel);
    }
}