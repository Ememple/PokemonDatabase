import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.Vector;

/**
 * GUI class for the Pokémon Database Manager
 */
public class PokemonGUI extends JFrame {
    private JTable pokemonTable;
    private JTable trainerTable;
    private JTable typesTable;
    private JTable statsTable;
    private JTable linkTable;
    private final PokemonRepository pokemonRepo = new MySQLPokemonRepository();
    private final TrainerRepository trainerRepo = new MySQLTrainerRepository();
    private final TypeRepository typeRepo = new MySQLTypeRepository();

    /**
     * Initializes the main window and loads initial data
     */
    public PokemonGUI() {
        setTitle("Pokémon Database Manager");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Pokémon", createPokemonPanel());
        tabbedPane.addTab("Trainers", createTrainerPanel());
        tabbedPane.addTab("Types", createTypePanel());
        tabbedPane.addTab("Stats", createStatsPanel());
        tabbedPane.addTab("Links", createLinkPanel());

        add(tabbedPane);
        refreshAllData();
    }

    /**
     * Method for initializing the Pokémon panel
     * @return Pokémon Panel
     */
    private JPanel createPokemonPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        pokemonTable = new JTable();
        panel.add(new JScrollPane(pokemonTable), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnCatch = new JButton("Catch New Pokémon");
        JButton btnEdit = new JButton("Edit Pokémon");
        JButton btnImport = new JButton("Import CSV");
        JButton btnDelete = new JButton("Release Pokémon");

        btnRefresh.addActionListener(e -> refreshAllData());
        btnCatch.addActionListener(e -> showCatchPokemonDialog());
        btnEdit.addActionListener(e -> showEditPokemonDialog());
        btnImport.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    pokemonRepo.importPokemonsFromCSV(chooser.getSelectedFile().getAbsolutePath());
                    refreshAllData();
                    JOptionPane.showMessageDialog(this, "Pokémons were successfully imported");
                } catch (SQLException ex) { showError(ex.getMessage()); }
            }
        } );
        btnDelete.addActionListener(e -> {
            int row = pokemonTable.getSelectedRow();
            if (row != -1) {
                try {
                    Object value = pokemonTable.getValueAt(row, 0);
                    int id = Integer.parseInt(value.toString());

                    pokemonRepo.deletePokemon(id);
                    refreshAllData();
                    JOptionPane.showMessageDialog(this, "Pokémon was released");
                } catch (Exception ex) { showError("Please select a Pokémon from the table first"); }
            }
        });


        controls.add(btnRefresh);
        controls.add(btnCatch);
        controls.add(btnEdit);
        controls.add(btnImport);
        controls.add(btnDelete);
        panel.add(controls, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Initializes the form for editing new Pokémon
     */
    private void showEditPokemonDialog() {
        int row = pokemonTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a pokémon to edit!");
            return;
        }

        try {
            int id = Integer.parseInt(pokemonTable.getValueAt(row, 0).toString());
            String currentNick = pokemonTable.getValueAt(row, 1).toString();
            String currentRarity = pokemonTable.getValueAt(row, 2).toString();
            String currentHP = pokemonTable.getValueAt(row, 4).toString();
            String currentAtk = pokemonTable.getValueAt(row, 5).toString();
            String currentDef = pokemonTable.getValueAt(row, 6).toString();

            JTextField nickField = new JTextField(currentNick);
            JComboBox<String> rarityCombo = new JComboBox<>(new String[]{"Common", "Rare", "Legendary"});
            rarityCombo.setSelectedItem(currentRarity);
            JTextField hpField = new JTextField(currentHP);
            JTextField atkField = new JTextField(currentAtk);
            JTextField defField = new JTextField(currentDef);

            List<Types> allTypes = typeRepo.getAllTypes();
            JComboBox<TypeWrapper> type1Combo = new JComboBox<>();
            JComboBox<TypeWrapper> type2Combo = new JComboBox<>();
            type1Combo.addItem(new TypeWrapper(null));
            type2Combo.addItem(new TypeWrapper(null));

            for (Types t : allTypes) {
                type1Combo.addItem(new TypeWrapper(t));
                type2Combo.addItem(new TypeWrapper(t));
            }

            Object[] message = {
                    "Name:", nickField,
                    "Rarity:", rarityCombo,
                    "HP:", hpField,
                    "Attack:", atkField,
                    "Defense:", defField,
                    "Type 1:", type1Combo,
                    "Type 2:", type2Combo
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Pokémon ID: " + id, JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                pokemonRepo.updatePokemon(
                        id,
                        nickField.getText(),
                        (String) rarityCombo.getSelectedItem(),
                        Integer.parseInt(hpField.getText()),
                        Integer.parseInt(atkField.getText()),
                        Integer.parseInt(defField.getText())
                );

                TypeWrapper tw1 = (TypeWrapper) type1Combo.getSelectedItem();
                TypeWrapper tw2 = (TypeWrapper) type2Combo.getSelectedItem();

                Integer t1Id = (tw1 != null && tw1.type != null) ? tw1.type.getId() : null;
                Integer t2Id = (tw2 != null && tw2.type != null) ? tw2.type.getId() : null;

                pokemonRepo.updatePokemonTypes(id, t1Id, t2Id);

                refreshAllData();
                JOptionPane.showMessageDialog(this, "Pokémon and types updated successfully!");
            }
        } catch (Exception e) {
            showError("Edit error: " + e.getMessage());
        }
    }

    /**
     * Method for initializing the Trainer panel
     * @return Trainer Panel
     */
    private JPanel createTrainerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        trainerTable = new JTable();
        panel.add(new JScrollPane(trainerTable), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnAdd = new JButton("New Trainer");
        JButton btnEdit = new JButton("Edit Trainer");
        JButton btnImport = new JButton("Import from CSV");
        JButton btnDelete = new JButton("Delete Trainer");

        btnRefresh.addActionListener(e -> refreshAllData());
        btnAdd.addActionListener(e -> showAddTrainerDialog());
        btnEdit.addActionListener(e -> showEditTrainerDialog());

        btnImport.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    trainerRepo.importTrainersFromCSV(chooser.getSelectedFile().getAbsolutePath());
                    refreshAllData();
                    JOptionPane.showMessageDialog(this, "Trainers were successfully imported");
                } catch (SQLException ex) { showError(ex.getMessage()); }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = trainerTable.getSelectedRow();
            if (row != -1) {
                try {
                    Object value = trainerTable.getValueAt(row, 0);
                    int id = Integer.parseInt(value.toString());

                    trainerRepo.deleteTrainer(id);
                    refreshAllData();
                    JOptionPane.showMessageDialog(this, "Trainer was removed");
                } catch (Exception ex) { showError("Please select a Trainer from the table first"); }
            }
        });

        controls.add(btnRefresh);
        controls.add(btnAdd);
        controls.add(btnEdit);
        controls.add(btnImport);
        controls.add(btnDelete);
        panel.add(controls, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Initializes the form for editing new Trainer
     */
    private void showEditTrainerDialog() {
        int row = trainerTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a trainer to edit!");
            return;
        }

        try {
            int id = Integer.parseInt(trainerTable.getValueAt(row, 0).toString());
            String currentName = trainerTable.getValueAt(row, 1).toString();
            float currentXP = Float.parseFloat(trainerTable.getValueAt(row, 2).toString());
            boolean currentLeader = (boolean) trainerTable.getValueAt(row, 3);

            JTextField nameField = new JTextField(currentName);
            JTextField xpField = new JTextField(String.valueOf(currentXP));
            JCheckBox leaderBox = new JCheckBox("Gym Leader", currentLeader);

            Object[] message = {
                    "Trainer Name:", nameField,
                    "Experience (XP):", xpField,
                    "", leaderBox
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Trainer ID: " + id, JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                trainerRepo.updateTrainer(
                        id,
                        nameField.getText(),
                        Float.parseFloat(xpField.getText()),
                        leaderBox.isSelected()
                );
                refreshAllData();
                JOptionPane.showMessageDialog(this, "Trainer updated successfully!");
            }
        } catch (Exception e) {
            showError("Edit error: " + e.getMessage());
        }
    }

    /**
     * Method for initializing the Type panel
     * @return Trainer Panel
     */
    private JPanel createTypePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        typesTable = new JTable();
        panel.add(new JScrollPane(typesTable), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnAdd = new JButton("Add type");
        JButton btnDelete = new JButton("Remove type");

        btnAdd.addActionListener(e -> {
            String newTypeName = JOptionPane.showInputDialog(this, "Enter new type name:");
            if (newTypeName != null && !newTypeName.trim().isEmpty()) {
                try {
                    typeRepo.addType(newTypeName.trim());
                    refreshAllData();
                    JOptionPane.showMessageDialog(this, "Type '" + newTypeName + "' added successfully.");
                } catch (SQLException ex) {
                    showError("Could not add type: " + ex.getMessage());
                }
            }
        });

        btnRefresh.addActionListener(e -> {
            try {
                typesTable.setModel(buildTableModel("SELECT * FROM types"));
            } catch (SQLException ex) {
                showError("Error with loading types: " + ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            int row = typesTable.getSelectedRow();
            if (row != -1) {
                try {
                    Object value = typesTable.getValueAt(row, 0);
                    int id = Integer.parseInt(value.toString());

                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to delete this type?",
                            "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        typeRepo.deleteType(id);

                        refreshAllData();
                        JOptionPane.showMessageDialog(this, "Type was successfully removed from database.");
                    }
                } catch (SQLException ex) {
                    showError("Cannot remove type. It is likely assigned to one or more Pokémons.\nDetails: " + ex.getMessage());
                } catch (Exception ex) {
                    showError("An error occurred: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Choose a type from the table first.");
            }
        });

        controls.add(btnRefresh);
        controls.add(btnAdd);
        controls.add(btnDelete);
        panel.add(controls, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Method for initializing the Stats panel
     * @return Stats Panel
     */
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        statsTable = new JTable();
        panel.add(new JScrollPane(statsTable), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh");

        btnRefresh.addActionListener(e -> refreshAllData());

        controls.add(btnRefresh);
        panel.add(controls, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Method for initializing the Link panel
     * @return Link Panel
     */
    private JPanel createLinkPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        linkTable = new JTable();
        panel.add(new JScrollPane(linkTable), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh");

        btnRefresh.addActionListener(e -> refreshAllData());

        controls.add(btnRefresh);
        panel.add(controls, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Initializes the form for adding new Pokémon
     */
    private void showCatchPokemonDialog() {
        try {
            List<Trainer> trainers = trainerRepo.getAllTrainers();
            List<Types> typesList = typeRepo.getAllTypes();

            JComboBox<TrainerWrapper> trainerCombo = new JComboBox<>();
            for (Trainer t : trainers) trainerCombo.addItem(new TrainerWrapper(t));

            JTextField nickField = new JTextField();
            JComboBox<String> rarityCombo = new JComboBox<>(new String[]{"Common", "Rare", "Legendary"});

            JTextField hpField = new JTextField("50");
            JTextField atkField = new JTextField("50");
            JTextField defField = new JTextField("50");

            JComboBox<TypeWrapper> type1Combo = new JComboBox<>();
            JComboBox<TypeWrapper> type2Combo = new JComboBox<>();
            type1Combo.addItem(new TypeWrapper(null));
            type2Combo.addItem(new TypeWrapper(null));

            for (Types t : typesList) {
                type1Combo.addItem(new TypeWrapper(t));
                type2Combo.addItem(new TypeWrapper(t));
            }

            Object[] message = {
                    "Trainer:", trainerCombo,
                    "Nickname:", nickField,
                    "Rarity:", rarityCombo,
                    "HP:", hpField,
                    "Attack:", atkField,
                    "Defense:", defField,
                    "Type 1:", type1Combo,
                    "Type 2:", type2Combo
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Catch New Pokémon", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                Trainer selectedTrainer = ((TrainerWrapper) trainerCombo.getSelectedItem()).trainer;
                Pokemon p = new Pokemon(selectedTrainer.getId(), nickField.getText(), (String) rarityCombo.getSelectedItem());

                int hp = Integer.parseInt(hpField.getText());
                int atk = Integer.parseInt(atkField.getText());
                int def = Integer.parseInt(defField.getText());

                int newId = pokemonRepo.catchNewPokemon(p, hp, atk, def);

                TypeWrapper tw1 = (TypeWrapper) type1Combo.getSelectedItem();
                if (tw1 != null && tw1.type != null) pokemonRepo.addTypeToPokemon(newId, tw1.type.getId());

                TypeWrapper tw2 = (TypeWrapper) type2Combo.getSelectedItem();
                if (tw2 != null && tw2.type != null && tw2.type != tw1.type) pokemonRepo.addTypeToPokemon(newId, tw2.type.getId());

                refreshAllData();
            }
        } catch (Exception e) {
            showError("Error with adding new pokémon: " + e.getMessage());
        }
    }

    /**
     * Initializes the form for adding new Pokémon
     */
    private void showAddTrainerDialog() {
        JTextField nameField = new JTextField();
        JTextField xpField = new JTextField("0");
        JCheckBox leaderBox = new JCheckBox("Gym Leader");

        Object[] message = {"Name:", nameField, "XP:", xpField, "", leaderBox};
        int option = JOptionPane.showConfirmDialog(this, message, "New Trainer", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                trainerRepo.addTrainer(nameField.getText(), Float.parseFloat(xpField.getText()), leaderBox.isSelected());
                refreshAllData();
            } catch (Exception ex) { showError("Invalid trainer data"); }
        }
    }

    /**
     * Reloads the data from database
     */
    private void refreshAllData() {
        try {
            pokemonTable.setModel(buildTableModel("SELECT * FROM view_detailed_pokemons"));
            trainerTable.setModel(buildTableModel("SELECT * FROM view_trainer_performance"));
            typesTable.setModel(buildTableModel("SELECT * FROM types"));
            linkTable.setModel(buildTableModel("SELECT * FROM pokemon_types"));
            statsTable.setModel(buildTableModel("SELECT * FROM pokemon_stats"));
        } catch (SQLException e) { showError(e.getMessage()); }
    }

    /**
     * Creates popup for error
     * @param msg Error message
     */
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Builds a table from sql query
     * @param query SQL query
     * @return New table
     * @throws SQLException If the query execution fails
     */
    public static DefaultTableModel buildTableModel(String query) throws SQLException {
        Connection conn = DatabaseConfig.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int count = metaData.getColumnCount();
        for (int i = 1; i <= count; i++) columnNames.add(metaData.getColumnName(i));
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= count; i++) row.add(rs.getObject(i));
            data.add(row);
        }
        return new DefaultTableModel(data, columnNames);
    }

    /**
     *
     */
    private static class TrainerWrapper {
        Trainer trainer;
        TrainerWrapper(Trainer t) { this.trainer = t; }
        @Override public String toString() { return trainer.getName(); }
    }

    private static class TypeWrapper {
        Types type;
        TypeWrapper(Types t) { this.type = t; }
        @Override public String toString() { return type == null ? "--- None ---" : type.getTypeName(); }
    }
}