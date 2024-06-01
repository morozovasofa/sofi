import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class SimpleCalenadar extends JFrame {

    private Calendar calendar;
    private JLabel monthLabel;
    private int currentYear;
    private Map<String, String> events;

    public SimpleCalenadar() {
        calendar = Calendar.getInstance(Locale.getDefault());
        currentYear = calendar.get(Calendar.YEAR);
        events = new HashMap<>();
        initUI();
    }

    private void initUI() {
        // Set window title
        setTitle("Calendar");
        setSize(400, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        monthLabel = new JLabel();
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(monthLabel, BorderLayout.NORTH);

        JPanel monthsPanel = new JPanel(new GridLayout(0, 4));
        for (int month = 0; month < 12; month++) {
            calendar.set(Calendar.MONTH, month);
            String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            JButton monthButton = new JButton(monthName);
            int finalMonth = month;
            monthButton.addActionListener(e -> showMonthDetails(finalMonth));
            monthsPanel.add(monthButton);
        }
        add(monthsPanel, BorderLayout.CENTER);

        JPanel yearPanel = new JPanel();
        JButton prevYearButton = new JButton("Previous Year");
        prevYearButton.addActionListener(e -> {
            currentYear--;
            updateCalendar();
        });
        yearPanel.add(prevYearButton);

        JButton nextYearButton = new JButton("Next Year");
        nextYearButton.addActionListener(e -> {
            currentYear++;
            updateCalendar();
        });
        yearPanel.add(nextYearButton);

        add(yearPanel, BorderLayout.SOUTH);
        updateCalendar();
        setVisible(true);
    }

    private void updateCalendar() {
        calendar.set(Calendar.YEAR, currentYear);
        monthLabel.setText("Year: " + currentYear);
    }

    private void showMonthDetails(int month) {
        calendar.set(Calendar.MONTH, month);
        JFrame monthFrame = new JFrame(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        monthFrame.setLayout(new GridLayout(0, 7));
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String weekDay : weekDays) {
            JLabel dayOfWeekLabel = new JLabel(weekDay);
            monthFrame.add(dayOfWeekLabel);
        }

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        for (int i = Calendar.SUNDAY; i < firstDayOfWeek; i++) {
            monthFrame.add(new JLabel());
        }

        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            JButton dayButton = new JButton(day + " (" + calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()) + ")");
            int finalDay = day;
            dayButton.addActionListener(e -> {
                String event = JOptionPane.showInputDialog("Enter event:");
                if (event != null && !event.isEmpty()) {
                    events.put(finalDay + "-" + (calendar.get(Calendar.MONTH) + 1), event);
                    JOptionPane.showMessageDialog(null, "Event added.");
                    updateEventList();
                    updateCalendar();
                }
            });
            addStarIfEvent(dayButton, day);
            addEditButton(dayButton, day);
            monthFrame.add(dayButton);

            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                monthFrame.add(new JLabel());
            }
        }

        monthFrame.setSize(400, 300);
        monthFrame.setVisible(true);
    }

    private void addStarIfEvent(JButton dayButton, int day) {
        String eventKey = day + "-" + (calendar.get(Calendar.MONTH) + 1);
        if (events.containsKey(eventKey)) {
            String text = dayButton.getText();
            dayButton.setText(text + " *");
            dayButton.setForeground(Color.RED);
        }
    }

    private void addEditButton(JButton dayButton, int day) {
        dayButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (SwingUtilities.isRightMouseButton(evt)) {
                    String eventKey = day + "-" + (calendar.get(Calendar.MONTH) + 1);
                    String currentEvent = events.get(eventKey);
                    String newEvent = JOptionPane.showInputDialog("Edit event:", currentEvent);
                    if (newEvent != null && !newEvent.isEmpty()) {
                        events.put(eventKey, newEvent);
                        JOptionPane.showMessageDialog(null, "Event edited.");
                        updateEventList();
                    }
                }
            }
        });
    }

    private void updateEventList() {
        JFrame eventListFrame = new JFrame("Event List");
        JPanel eventsPanel = new JPanel(new GridLayout(0, 1));

        events.forEach((date, event) -> {
            JLabel eventLabel = new JLabel(date + ": " + event);
            eventsPanel.add(eventLabel);
        });

        JScrollPane scrollPane = new JScrollPane(eventsPanel);
        eventListFrame.add(scrollPane);
        eventListFrame.pack();
        eventListFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new SimpleCalenadar();
    }
}