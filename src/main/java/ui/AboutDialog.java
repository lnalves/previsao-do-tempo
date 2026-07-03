package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AboutDialog extends JDialog {

    public AboutDialog(Frame owner) {
        super(owner, "Sobre o App Previsão do Tempo", true);
        setSize(400, 300);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("App Previsão do Tempo");
        lblTitle.setFont(UiStyle.TITLE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAuthorsTitle = new JLabel("Desenvolvido por:");
        lblAuthorsTitle.setFont(UiStyle.BOLD);
        lblAuthorsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAuthorsTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(lblAuthorsTitle);
        for (String author : List.of("Leonardo N. Alves", "Lucas O. Dias", "João Antonio S. Pinto",
                "Roberto A. Giuliani", "Ryan M. Zemniaçak")) {
            JLabel lblAuthor = new JLabel(author);
            lblAuthor.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(lblAuthor);
        }
        panel.add(Box.createVerticalGlue());

        add(panel);
    }
}
