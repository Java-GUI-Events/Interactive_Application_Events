import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.TextListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.awt.*;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class ToDoList extends JFrame {
    // Atributos
    private JPanel mainPanel; // Painel Principal
    private JTextField taskInputField;
    private JButton addButton;
    private JList<String> taskList;
    private DefaultListModel<String> listModel;
    private JButton deleteButton;
    private JButton markDoneButton;
    private JComboBox<String> filterComboBox;
    private JButton clearCompletedButton;
    private List<Task> tasks;

    // Construtor
    public ToDoList() {
        // Configuração da janela principal
        super("To-Do List App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(650, 200, 500, 500);
        this.setVisible(true);

        // Inicializa o painel principal
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Inicializa a lista de tasks e a lista de tasks concluídas
        tasks = new ArrayList<>();
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);

        // Inicializa campos de entrada, botões e JComboBox
        taskInputField = new JTextField();
        addButton = new JButton("Adicionar");
        deleteButton = new JButton("Excluir");
        markDoneButton = new JButton("Concluir");
        filterComboBox = new JComboBox<>(new String[] { "Todas", "Ativas",
                "Concluídas" });
        clearCompletedButton = new JButton("Limpar Concluídas");

        // Configuração do painel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(taskInputField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        // Configuração do painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(deleteButton);
        buttonPanel.add(markDoneButton);
        buttonPanel.add(filterComboBox);
        buttonPanel.add(clearCompletedButton);

        // Adiciona os componentes ao painel principal
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        JScrollPane scrollDelete = new JScrollPane(taskList);
        // mainPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        mainPanel.add(scrollDelete, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Adiciona o painel principal à janela
        this.add(mainPanel);

        // Exibe a janela

        // Configuração de Listener para os Eventos
        // adicionar tarefa
        HandlerAddTask eventAddTask = new HandlerAddTask();
        addButton.addActionListener(eventAddTask);

        // excluir tarefa com delete
        HandlerDeleteTask eventDeleteTask = new HandlerDeleteTask();
        taskList.addKeyListener(eventDeleteTask);

        // excluir tarefa com o botão
        HandlerDeleteTask2 eventDeleteTask2 = new HandlerDeleteTask2();
        deleteButton.addActionListener(eventDeleteTask2);

        // concluir tarefa
        HandlerFinisihTask eventFinishTask = new HandlerFinisihTask();
        markDoneButton.addMouseListener(eventFinishTask);

        // filtrar tarefa
        HandlerFilterTask eventFilterTask = new HandlerFilterTask();
        filterComboBox.addItemListener(eventFilterTask);

        // limpar concluídas
        HandlerClearCompletedTasks eventClearCompletedTasks = new HandlerClearCompletedTasks();
        clearCompletedButton.addFocusListener(eventClearCompletedTasks);

    }

    private void addTask() {
        // Adiciona uma nova task à lista de tasks
        String taskDescription = taskInputField.getText().trim();// remove espaços vazios
        if (!taskDescription.isEmpty()) {
            Task newTask = new Task(taskDescription);
            tasks.add(newTask);
            updateTaskList();
            taskInputField.setText("");
        }
    }

    // public class HandlerAddTask implements ActionListener {

    // @Override
    // public void actionPerformed(ActionEvent e) {
    // addTask();
    // }

    // }

    public class HandlerAddTask implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                addTask();

                if(taskInputField.equals(null)) {
                throw new Exception("Digite alguma coisa");
            }
            }
            catch(Exception erro) {
                JOptionPane.showMessageDialog(null, erro.getMessage(), "valor nulo", 0);
            }
            
        }

    }

    private void deleteTask() {
        // Exclui a task selecionada da lista de tasks
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < tasks.size()) {
            tasks.remove(selectedIndex);
            updateTaskList();
        }
    }

    public class HandlerDeleteTask implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            // if(eventDeleteTask.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE){
            // System.out.println("Pressionou delete");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                deleteTask();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

    }

    public class HandlerDeleteTask2 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
           
            deleteTask();
        }
        
    }

    private void markTaskDone() {
        // Marca a task selecionada como concluída
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < tasks.size()) {
            Task task = tasks.get(selectedIndex);
            task.setDone(true);
            updateTaskList();
        }
    }

    public class HandlerFinisihTask implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            markTaskDone();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    private void filterTasks() {
        // Filtra as tasks com base na seleção do JComboBox
        String filter = (String) filterComboBox.getSelectedItem();
        listModel.clear();
        for (Task task : tasks) {
            if (filter.equals("Todas") || (filter.equals("Ativas") &&
                    !task.isDone()) || (filter.equals("Concluídas") && task.isDone())) {
                listModel.addElement(task.getDescription());
            }
        }
    }

    public class HandlerFilterTask implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            filterTasks();
        }

    }

    private void clearCompletedTasks() {
        // Limpa todas as tasks concluídas da lista
        List<Task> completedTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isDone()) {
                completedTasks.add(task);
            }
        }
        tasks.removeAll(completedTasks);
        updateTaskList();
    }

    public class HandlerClearCompletedTasks implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            clearCompletedTasks();
        }

        @Override
        public void focusLost(FocusEvent e) {
            clearCompletedTasks();
        }

    }

    private void updateTaskList() {
        // Atualiza a lista de tasks exibida na GUI
        listModel.clear();
        for (Task task : tasks) {
            listModel.addElement(task.getDescription() + (task.isDone() ?

                    " (Concluída)" : ""));

        }
    }

    public void run() {
        this.setVisible(true);
    }

}