package br.com.nogueira.servicedesk.ui;

import br.com.nogueira.servicedesk.config.AppConfig;
import br.com.nogueira.servicedesk.exception.BusinessException;
import br.com.nogueira.servicedesk.model.Priority;
import br.com.nogueira.servicedesk.model.Ticket;
import br.com.nogueira.servicedesk.model.TicketStatus;
import br.com.nogueira.servicedesk.service.TicketService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

public class MainFrame extends JFrame {
    private final TicketService ticketService;
    private final TicketTableModel tableModel = new TicketTableModel();

    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField departmentField = new JTextField();
    private final JTextField titleField = new JTextField();
    private final JTextArea descriptionArea = new JTextArea(4, 20);
    private final JComboBox<Priority> priorityCombo = new JComboBox<>(Priority.values());
    private final JComboBox<TicketStatus> statusCombo = new JComboBox<>(TicketStatus.values());
    private final JTable ticketTable = new JTable(tableModel);
    private final JLabel openCounter = new JLabel("0");
    private final JLabel progressCounter = new JLabel("0");
    private final JLabel resolvedCounter = new JLabel("0");
    private final JLabel canceledCounter = new JLabel("0");

    public MainFrame(TicketService ticketService) {
        this.ticketService = ticketService;
        configureLookAndFeel();
        configureWindow();
        buildLayout();
        refreshData();
    }

    private void configureLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    private void configureWindow() {
        setTitle(AppConfig.APP_NAME + " v" + AppConfig.APP_VERSION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 720));
        setSize(new Dimension(1180, 720));
        setLocationRelativeTo(null);
    }

    private void buildLayout() {
        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        root.setBackground(new Color(245, 247, 250));

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildFormPanel(), BorderLayout.WEST);
        root.add(buildTicketsPanel(), BorderLayout.CENTER);
        root.add(buildDashboardPanel(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel title = new JLabel("ServiceDesk Pro");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        JLabel subtitle = new JLabel("Gestao de chamados internos com Java, Swing, SQLite e arquitetura em camadas");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(title, BorderLayout.NORTH);
        panel.add(subtitle, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(360, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 232)),
                new EmptyBorder(16, 16, 16, 16)));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        addField(panel, gbc, "Solicitante", nameField);
        addField(panel, gbc, "E-mail", emailField);
        addField(panel, gbc, "Departamento", departmentField);
        addField(panel, gbc, "Titulo do chamado", titleField);

        gbc.gridy++;
        panel.add(new JLabel("Descricao"), gbc);
        gbc.gridy++;
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        panel.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridy++;
        panel.add(new JLabel("Prioridade"), gbc);
        gbc.gridy++;
        panel.add(priorityCombo, gbc);

        JButton saveButton = new JButton("Abrir chamado");
        saveButton.addActionListener(event -> createTicket());
        gbc.gridy++;
        gbc.insets = new Insets(18, 0, 6, 0);
        panel.add(saveButton, gbc);

        JButton clearButton = new JButton("Limpar formulario");
        clearButton.addActionListener(event -> clearForm());
        gbc.gridy++;
        gbc.insets = new Insets(6, 0, 6, 0);
        panel.add(clearButton, gbc);

        return panel;
    }

    private JPanel buildTicketsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        ticketTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ticketTable.setRowHeight(28);
        ticketTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(new JScrollPane(ticketTable), BorderLayout.CENTER);

        JPanel actions = new JPanel(new BorderLayout(10, 10));
        actions.setOpaque(false);
        actions.add(statusCombo, BorderLayout.CENTER);

        JButton updateButton = new JButton("Atualizar status selecionado");
        updateButton.addActionListener(event -> updateSelectedStatus());
        actions.add(updateButton, BorderLayout.EAST);

        JButton detailsButton = new JButton("Ver detalhes");
        detailsButton.addActionListener(event -> showSelectedDetails());
        actions.add(detailsButton, BorderLayout.WEST);

        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 12, 0));
        panel.setOpaque(false);
        panel.add(counterCard("Abertos", openCounter));
        panel.add(counterCard("Em andamento", progressCounter));
        panel.add(counterCard("Resolvidos", resolvedCounter));
        panel.add(counterCard("Cancelados", canceledCounter));
        return panel;
    }

    private JPanel counterCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 232)),
                new EmptyBorder(12, 14, 12, 14)));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field) {
        gbc.gridy++;
        panel.add(new JLabel(label), gbc);
        gbc.gridy++;
        panel.add(field, gbc);
    }

    private void createTicket() {
        try {
            ticketService.openTicket(
                    nameField.getText(),
                    emailField.getText(),
                    departmentField.getText(),
                    titleField.getText(),
                    descriptionArea.getText(),
                    (Priority) priorityCombo.getSelectedItem()
            );
            clearForm();
            refreshData();
            showMessage("Chamado aberto com sucesso.");
        } catch (BusinessException exception) {
            showError(exception.getMessage());
        } catch (Exception exception) {
            showError("Erro inesperado: " + exception.getMessage());
        }
    }

    private void updateSelectedStatus() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow < 0) {
            showError("Selecione um chamado na tabela.");
            return;
        }
        Ticket ticket = tableModel.getTicketAt(ticketTable.convertRowIndexToModel(selectedRow));
        ticketService.changeStatus(ticket.getId(), (TicketStatus) statusCombo.getSelectedItem());
        refreshData();
        showMessage("Status atualizado com sucesso.");
    }

    private void showSelectedDetails() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow < 0) {
            showError("Selecione um chamado na tabela.");
            return;
        }
        Ticket ticket = tableModel.getTicketAt(ticketTable.convertRowIndexToModel(selectedRow));
        String details = "ID: " + ticket.getId()
                + "\nTitulo: " + ticket.getTitle()
                + "\nDescricao: " + ticket.getDescription()
                + "\nPrioridade: " + ticket.getPriority().getLabel()
                + "\nStatus: " + ticket.getStatus().getLabel()
                + "\nSolicitante: " + ticket.getCustomer().getName()
                + "\nE-mail: " + ticket.getCustomer().getEmail()
                + "\nDepartamento: " + ticket.getCustomer().getDepartment()
                + "\nCriado em: " + ticket.getCreatedAt();
        JOptionPane.showMessageDialog(this, details, "Detalhes do chamado", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshData() {
        List<Ticket> tickets = ticketService.listTickets();
        tableModel.setTickets(tickets);
        openCounter.setText(String.valueOf(ticketService.countByStatus(TicketStatus.OPEN)));
        progressCounter.setText(String.valueOf(ticketService.countByStatus(TicketStatus.IN_PROGRESS)));
        resolvedCounter.setText(String.valueOf(ticketService.countByStatus(TicketStatus.RESOLVED)));
        canceledCounter.setText(String.valueOf(ticketService.countByStatus(TicketStatus.CANCELED)));
    }

    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        departmentField.setText("");
        titleField.setText("");
        descriptionArea.setText("");
        priorityCombo.setSelectedItem(Priority.MEDIUM);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Atencao", JOptionPane.WARNING_MESSAGE);
    }

    public void showWindow() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}


