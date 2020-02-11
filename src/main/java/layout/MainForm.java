package layout;

import function.Functions;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static function.Functions.generateFinalString;
import static function.Functions.selectionStrings;

public class MainForm {
    private static JTextPane bigPane;

    private enum SelectionIndex {
        BAR_SERIAL_COMBO(0),
        BAR_SERIAL_TEXT(1),
        LOCATION_CODE(2),
        DEVICE_TYPE(3),
        TECH_NOTE(4),
        REQUESTING(5);

        private int value;

        SelectionIndex(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private static void addComponentsToPane(@NotNull Container pane) {
        pane.setLayout(null);
        Insets insets = pane.getInsets();

        createComboBox(pane, insets);
        createLabels(pane, insets);
        createTextBoxes(pane, insets);
        createButton(pane, insets);
        createScrollPane(pane, insets);
    }

    @SuppressWarnings("unchecked")
    private static void createComboBox(@NotNull Container pane, @NotNull Insets insets) {
        String[] combo = { "Barcode", "Serial" };
        selectionStrings[SelectionIndex.BAR_SERIAL_COMBO.getValue()] = "Barcode";
        JComboBox<String> barSerialBox = new JComboBox<>(combo);
        barSerialBox.addActionListener(e ->
                selectionStrings[SelectionIndex.BAR_SERIAL_COMBO.getValue()] = (String)((JComboBox<String>)e.getSource())
                .getSelectedItem());

        pane.add(barSerialBox);
        barSerialBox.setBounds(10 + insets.left, 13 + insets.top, 112, 22);
    }

    private static void createLabels(@NotNull Container pane, @NotNull Insets insets) {
        JLabel locCode = new JLabel("Location Code:");
        JLabel type = new JLabel("Device Type:");
        JLabel techNote = new JLabel("Tech Notes:");
        JLabel request = new JLabel("Requesting:");

        Font labelFont = new Font("Sans Sarif", Font.PLAIN, 12);

        pane.add(locCode);
        pane.add(type);
        pane.add(techNote);
        pane.add(request);

        locCode.setBounds(12 + insets.left, 39 + insets.top, 120, 20);
        locCode.setFont(labelFont);

        type.setBounds(12 + insets.left, 69 + insets.top, 120, 20);
        type.setFont(labelFont);

        techNote.setBounds(12 + insets.left, 99 + insets.top, 116, 20);
        techNote.setFont(labelFont);

        request.setBounds(12 + insets.left, 129 + insets.top, 94, 20);
        request.setFont(labelFont);
    }

    private static void createTextBoxes(@NotNull Container pane, @NotNull Insets insets) {
        JTextField barSerial = new JTextField() {
            public void addNotify() {
                super.addNotify();
                requestFocus();
            }
        };
        barSerial.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                selectionStrings[SelectionIndex.BAR_SERIAL_TEXT.getValue()] = barSerial.getText();
            }
        });

        JTextField locCode = new JTextField();
        locCode.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                selectionStrings[SelectionIndex.LOCATION_CODE.getValue()] = locCode.getText();
            }
        });

        JTextField type = new JTextField();
        type.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                selectionStrings[SelectionIndex.DEVICE_TYPE.getValue()] = type.getText();
            }
        });

        JTextField techNote = new JTextField();
        techNote.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                selectionStrings[SelectionIndex.TECH_NOTE.getValue()] = techNote.getText();
            }
        });

        JTextField requesting = new JTextField();
        requesting.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                selectionStrings[SelectionIndex.REQUESTING.getValue()] = requesting.getText();
            }
        });

        pane.add(barSerial);
        pane.add(locCode);
        pane.add(type);
        pane.add(techNote);
        pane.add(requesting);

        barSerial.setBounds(134 + insets.left, 16 + insets.top, 350, 20);
        locCode.setBounds(134 + insets.left, 42 + insets.top, 350, 20);
        type.setBounds(134 + insets.left, 72 + insets.top, 350, 20);
        techNote.setBounds(134 + insets.left, 101 + insets.top, 350, 20);
        requesting.setBounds(134 + insets.left, 131 + insets.top, 350, 20);
    }

    private static void createButton(@NotNull Container pane, @NotNull Insets insets) {
        JButton generate = new JButton("Generate");
        generate.addActionListener(e -> updateBigPane(Functions.gatherResults()));

        pane.add(generate);

        generate.setBounds(57 + insets.left, 169 + insets.top, 379, 66);
    }

    private static void createScrollPane(@NotNull Container pane, @NotNull Insets insets) {
        bigPane = new JTextPane();
        bigPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bigPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        pane.add(scrollPane);

        scrollPane.setBounds(16 + insets.left, 252 + insets.top, 468, 218);
    }

    private static void updateBigPane(@NotNull java.util.List<String> results) {
        String sb = generateFinalString(results);
        bigPane.setText(sb);
    }

    public static void createAndShowGUI() {
        System.out.println("Launching GUI...");

        JFrame frame = new JFrame("Replacement Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponentsToPane(frame.getContentPane());

        Insets insets = frame.getInsets();
        frame.setSize(520 + insets.left + insets.right,
                530 + insets.top + insets.bottom);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
