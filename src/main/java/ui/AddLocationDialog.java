package ui;

import api.GeocodingClient;
import api.dto.GeocodingResponse;
import service.LocationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/** Add a city by searching its name (geocoding) or by typing coordinates manually. */
public class AddLocationDialog extends JDialog {

    private final LocationService locationService;
    private final GeocodingClient geocodingClient = new GeocodingClient();
    private final Runnable onSaved;

    private JTextField txtSearch;
    private JTextField txtName;
    private JTextField txtLatitude;
    private JTextField txtLongitude;
    private DefaultListModel<GeocodingResponse.ResultDto> resultsModel;
    private JList<GeocodingResponse.ResultDto> lstResults;

    public AddLocationDialog(Frame owner, LocationService locationService, Runnable onSaved) {
        super(owner, "Adicionar Nova Cidade", true);
        this.locationService = locationService;
        this.onSaved = onSaved;
        setSize(480, 420);
        setLocationRelativeTo(owner);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Search by name
        JPanel pnlSearch = new JPanel(new BorderLayout(5, 5));
        pnlSearch.add(new JLabel("Buscar cidade pelo nome:"), BorderLayout.NORTH);
        txtSearch = new JTextField();
        JButton btnSearch = new JButton("Buscar");
        JPanel pnlSearchRow = new JPanel(new BorderLayout(5, 5));
        pnlSearchRow.add(txtSearch, BorderLayout.CENTER);
        pnlSearchRow.add(btnSearch, BorderLayout.EAST);
        pnlSearch.add(pnlSearchRow, BorderLayout.CENTER);

        resultsModel = new DefaultListModel<>();
        lstResults = new JList<>(resultsModel);
        lstResults.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                GeocodingResponse.ResultDto r = (GeocodingResponse.ResultDto) value;
                String admin = r.admin1() != null ? r.admin1() + ", " : "";
                setText(String.format("%s (%s%s) — %.4f, %.4f", r.name(), admin, r.country(), r.latitude(), r.longitude()));
                return this;
            }
        });
        lstResults.addListSelectionListener(e -> {
            GeocodingResponse.ResultDto selected = lstResults.getSelectedValue();
            if (selected != null) {
                txtName.setText(selected.name());
                txtLatitude.setText(String.valueOf(selected.latitude()));
                txtLongitude.setText(String.valueOf(selected.longitude()));
            }
        });

        // Manual fields (filled by selection, editable as fallback)
        JPanel pnlFields = new JPanel(new GridLayout(3, 2, 8, 8));
        pnlFields.setBorder(BorderFactory.createTitledBorder("Dados da cidade"));
        pnlFields.add(new JLabel("Cidade:"));
        txtName = new JTextField();
        pnlFields.add(txtName);
        pnlFields.add(new JLabel("Latitude:"));
        txtLatitude = new JTextField();
        pnlFields.add(txtLatitude);
        pnlFields.add(new JLabel("Longitude:"));
        txtLongitude = new JTextField();
        pnlFields.add(txtLongitude);

        JButton btnSave = new JButton("Salvar");
        JPanel pnlBottom = new JPanel(new BorderLayout(10, 10));
        pnlBottom.add(pnlFields, BorderLayout.CENTER);
        JPanel pnlSave = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSave.add(btnSave);
        pnlBottom.add(pnlSave, BorderLayout.SOUTH);

        panel.add(pnlSearch, BorderLayout.NORTH);
        panel.add(new JScrollPane(lstResults), BorderLayout.CENTER);
        panel.add(pnlBottom, BorderLayout.SOUTH);
        add(panel);

        btnSearch.addActionListener(e -> search());
        txtSearch.addActionListener(e -> search());
        btnSave.addActionListener(e -> save());
    }

    private void search() {
        String query = txtSearch.getText().trim();
        if (query.isEmpty()) {
            return;
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<List<GeocodingResponse.ResultDto>, Void>() {
            @Override
            protected List<GeocodingResponse.ResultDto> doInBackground() throws Exception {
                return geocodingClient.search(query);
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    resultsModel.clear();
                    List<GeocodingResponse.ResultDto> results = get();
                    if (results.isEmpty()) {
                        JOptionPane.showMessageDialog(AddLocationDialog.this,
                                "Nenhuma cidade encontrada para \"" + query + "\".", "Busca", JOptionPane.INFORMATION_MESSAGE);
                    }
                    results.forEach(resultsModel::addElement);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(AddLocationDialog.this,
                            "Erro na busca: " + e.getCause().getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void save() {
        try {
            double latitude = Double.parseDouble(txtLatitude.getText().trim().replace(',', '.'));
            double longitude = Double.parseDouble(txtLongitude.getText().trim().replace(',', '.'));
            locationService.add(txtName.getText(), latitude, longitude);
            JOptionPane.showMessageDialog(this, "Cidade adicionada com sucesso!");
            onSaved.run();
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Latitude e longitude devem ser números.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar a cidade: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
