import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class Expense {
    String category;
    double amount;
    String date;

    Expense(String category, double amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
}

public class ExpenseTracker extends JFrame {

    private final JTextField tfAmount;
    private final JTextField tfDate;
    private final JComboBox<String> cbCategory;
    private final JTable expenseTable;
    private final DefaultTableModel tableModel;
    private final List<Expense> expenseList = new ArrayList<>();
    private final JLabel lblTotal;

    public ExpenseTracker() {
        setTitle("Nebula Expense Tracker");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(14, 14));
        getContentPane().setBackground(new Color(14, 17, 36));

        JLabel heading = new JLabel("Nebula Expense Tracker", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(124, 191, 255));
        heading.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        add(heading, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(8, 1, 8, 8));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(61, 70, 133), 1, true),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        inputPanel.setBackground(new Color(23, 28, 57));

        JLabel categoryLabel = createFormLabel("Category");
        JLabel amountLabel = createFormLabel("Amount (₹)");
        JLabel dateLabel = createFormLabel("Date (YYYY-MM-DD)");

        cbCategory = new JComboBox<>(new String[]{"Food", "Transport", "Bills", "Shopping", "Health", "Entertainment", "Others"});
        cbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbCategory.setBackground(new Color(36, 41, 80));
        cbCategory.setForeground(Color.WHITE);

        tfAmount = createInputField();
        tfDate = createInputField();
        tfDate.setText(LocalDate.now().toString());

        JButton btnAdd = createStyledButton("✚ Add Expense", new Color(46, 163, 115));
        JButton btnDelete = createStyledButton("🗑 Delete Selected", new Color(182, 70, 88));

        inputPanel.add(categoryLabel);
        inputPanel.add(cbCategory);
        inputPanel.add(amountLabel);
        inputPanel.add(tfAmount);
        inputPanel.add(dateLabel);
        inputPanel.add(tfDate);
        inputPanel.add(btnAdd);
        inputPanel.add(btnDelete);

        add(inputPanel, BorderLayout.WEST);

        tableModel = new DefaultTableModel(new String[]{"Category", "Amount", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        expenseTable = new JTable(tableModel);
        styleTable(expenseTable);

        JScrollPane scrollPane = new JScrollPane(expenseTable);
        scrollPane.getViewport().setBackground(new Color(21, 25, 49));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(61, 70, 133), 1, true));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(14, 17, 36));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        lblTotal = new JLabel("Total: ₹0.00", JLabel.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotal.setForeground(new Color(255, 219, 117));
        bottomPanel.add(lblTotal, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addExpense());
        btnDelete.addActionListener(e -> deleteSelected());
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(197, 211, 255));
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }

    private JTextField createInputField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(36, 41, 80));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(91, 103, 181)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        return field;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return button;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(new Color(235, 240, 255));
        table.setBackground(new Color(28, 33, 66));
        table.setGridColor(new Color(54, 63, 120));
        table.setSelectionBackground(new Color(94, 109, 204));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(42, 50, 97));
        table.getTableHeader().setForeground(new Color(198, 216, 255));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
    }

    private void addExpense() {
        String category = cbCategory.getSelectedItem().toString();
        String date = tfDate.getText().trim();
        double amount;

        try {
            amount = Double.parseDouble(tfAmount.getText().trim());
            if (amount <= 0) {
                throw new NumberFormatException();
            }
            LocalDate.parse(date);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter valid amount and date (YYYY-MM-DD).", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Expense exp = new Expense(category, amount, date);
        expenseList.add(exp);
        tableModel.addRow(new Object[]{category, String.format("₹%.2f", amount), date});
        tfAmount.setText("");
        updateTotal();
    }

    private void deleteSelected() {
        int selected = expenseTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        expenseList.remove(selected);
        tableModel.removeRow(selected);
        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (Expense expense : expenseList) {
            total += expense.amount;
        }
        lblTotal.setText(String.format("Total: ₹%.2f", total));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenseTracker().setVisible(true));
    }
}
