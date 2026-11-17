
package com.qlsv;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        setTitle("PHẦN MỀM QUẢN LÝ SINH VIÊN");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel(
            "HỆ THỐNG QUẢN LÝ SINH VIÊN",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel();
        header.setBackground(new Color(30, 90, 160));
        header.add(title);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Sinh viên", new JPanel());
        tabs.addTab("Lớp học", new JPanel());
        tabs.addTab("Thống kê", new JPanel());

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
