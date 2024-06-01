





import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AutoServiceApp extends JFrame {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/autoservice";
    private static final String USER = "root";
    private static final String PASSWORD = "123456789";

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneNumberField;
    private JTextField emailField;
    private JComboBox<String> clientComboBox;
    private JTextField makeField;
    private JTextField modelField;
    private JTextField yearField;
    private JTextField licensePlateField;
    private JTextField repairDateField;
    private JTextField descriptionField;
    private JTextField costField;
    private JTextArea clientsTextArea;
    private JTextArea carsTextArea;
    private JTextArea repairsTextArea;

    private Map<String, Integer> clientMap = new HashMap<>();

    public AutoServiceApp() {
        setTitle("Auto Service Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel clientPanel = new JPanel(new GridLayout(6, 2));
        clientPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        clientPanel.add(firstNameField);

        clientPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        clientPanel.add(lastNameField);

        clientPanel.add(new JLabel("Phone Number:"));
        phoneNumberField = new JTextField();
        clientPanel.add(phoneNumberField);

        clientPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        clientPanel.add(emailField);

        JButton addClientButton = new JButton("Add Client");
        clientPanel.add(addClientButton);

        clientsTextArea = new JTextArea();
        clientPanel.add(new JScrollPane(clientsTextArea));

        tabbedPane.addTab("Clients", clientPanel);

        JPanel carPanel = new JPanel(new GridLayout(7, 2));
        carPanel.add(new JLabel("Client:"));
        clientComboBox = new JComboBox<>();
        carPanel.add(clientComboBox);

        carPanel.add(new JLabel("Make:"));
        makeField = new JTextField();
        carPanel.add(makeField);

        carPanel.add(new JLabel("Model:"));
        modelField = new JTextField();
        carPanel.add(modelField);

        carPanel.add(new JLabel("Year:"));
        yearField = new JTextField();
        carPanel.add(yearField);

        carPanel.add(new JLabel("License Plate:"));
        licensePlateField = new JTextField();
        carPanel.add(licensePlateField);

        JButton addCarButton = new JButton("Add Car");
        carPanel.add(addCarButton);

        carsTextArea = new JTextArea();
        carPanel.add(new JScrollPane(carsTextArea));

        tabbedPane.addTab("Cars", carPanel);

        JPanel repairPanel = new JPanel(new GridLayout(7, 2));
        repairPanel.add(new JLabel("Car ID:"));
        JTextField carIdField = new JTextField();
        repairPanel.add(carIdField);

        repairPanel.add(new JLabel("Repair Date:"));
        repairDateField = new JTextField();
        repairPanel.add(repairDateField);

        repairPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        repairPanel.add(descriptionField);

        repairPanel.add(new JLabel("Cost:"));
        costField = new JTextField();
        repairPanel.add(costField);

        JButton addRepairButton = new JButton("Add Repair");
        repairPanel.add(addRepairButton);

        repairsTextArea = new JTextArea();
        repairPanel.add(new JScrollPane(repairsTextArea));

        tabbedPane.addTab("Repairs", repairPanel);

        add(tabbedPane, BorderLayout.CENTER);

        addClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addClient();
                displayClients();
                loadClients();
            }
        });

        addCarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCar();
                displayCars();
            }
        });

        addRepairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRepair(Integer.parseInt(carIdField.getText()));
                displayRepairs();
            }
        });

        displayClients();
        displayCars();
        displayRepairs();
        loadClients();
    }

    private void addClient() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO Clients (FirstName, LastName, PhoneNumber, Email) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, phoneNumber);
            statement.setString(4, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayClients() {
        clientsTextArea.setText("");
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM Clients";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int clientId = resultSet.getInt("ClientID");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String phoneNumber = resultSet.getString("PhoneNumber");
                String email = resultSet.getString("Email");
                clientsTextArea.append("ID: " + clientId + ", Name: " + firstName + " " + lastName + ", Phone: " + phoneNumber + ", Email: " + email + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadClients() {
        clientComboBox.removeAllItems();
        clientMap.clear();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM Clients";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int clientId = resultSet.getInt("ClientID");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String clientName = firstName + " " + lastName;
                clientComboBox.addItem(clientName);
                clientMap.put(clientName, clientId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCar() {
        String clientName = (String) clientComboBox.getSelectedItem();
        if (clientName == null) {
            return;
        }
        int clientId = clientMap.get(clientName);
        String make = makeField.getText();
        String model = modelField.getText();
        int year = Integer.parseInt(yearField.getText());
        String licensePlate = licensePlateField.getText();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO Cars (ClientID, Make, Model, Year, LicensePlate) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, clientId);
            statement.setString(2, make);
            statement.setString(3, model);
            statement.setInt(4, year);
            statement.setString(5, licensePlate);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayCars() {
        carsTextArea.setText("");
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM Cars";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int carId = resultSet.getInt("CarID");
                int clientId = resultSet.getInt("ClientID");
                String make = resultSet.getString("Make");
                String model = resultSet.getString("Model");
                int year = resultSet.getInt("Year");
                String licensePlate = resultSet.getString("LicensePlate");
                carsTextArea.append("Car ID: " + carId + ", Client ID: " + clientId + ", Make: " + make + ", Model: " + model + ", Year: " + year + ", License Plate: " + licensePlate + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addRepair(int carId) {
        String repairDate = repairDateField.getText();
        String description = descriptionField.getText();
        double cost = Double.parseDouble(costField.getText());

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO Repairs (CarID, RepairDate, Description, Cost) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, carId);
            statement.setString(2, repairDate);
            statement.setString(3, description);
            statement.setDouble(4, cost);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayRepairs() {
        repairsTextArea.setText("");
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM Repairs";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int repairId = resultSet.getInt("RepairID");
                int carId = resultSet.getInt("CarID");
                String repairDate = resultSet.getString("RepairDate");
                String description = resultSet.getString("Description");
                double cost = resultSet.getDouble("Cost");
                repairsTextArea.append("Repair ID: " + repairId + ", Car ID: " + carId + ", Repair Date: " + repairDate + ", Description: " + description + ", Cost: " + cost + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AutoServiceApp().setVisible(true);
            }
        });
    }
}
