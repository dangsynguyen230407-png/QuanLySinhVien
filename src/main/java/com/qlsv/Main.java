
package com.qlsv;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class Main extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private TableRowSorter<DefaultTableModel> sorter;

    public Main() {
        setTitle("PHẦN MỀM QUẢN LÝ SINH VIÊN");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Tiêu đề
        JLabel title = new JLabel(
            "HỆ THỐNG QUẢN LÝ SINH VIÊN",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel();
        header.setBackground(new Color(30, 90, 160));
        header.setPreferredSize(new Dimension(1000, 65));
        header.add(title);

        // Các tab chức năng
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Sinh viên", createStudentPanel());
        tabs.addTab("Lớp học", new JPanel());
        tabs.addTab("Thống kê", new JPanel());

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    // Giao diện quản lý sinh viên
    private JPanel createStudentPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tiêu đề
        JLabel lblTitle = new JLabel("DANH SÁCH SINH VIÊN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(30, 90, 160));

        // Tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtSearch = new JTextField(25);
        JButton btnSearch = new JButton("Tìm kiếm");

        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(lblTitle, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        // Bảng sinh viên
        String[] columns = {
            "Mã SV", "Họ và tên", "Ngày sinh",
            "Giới tính", "Lớp", "Email", "Số điện thoại"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);

        // Các nút chức năng
        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 15, 10)
        );

        JButton btnAdd = new JButton("Thêm sinh viên");
        JButton btnEdit = new JButton("Sửa thông tin");
        JButton btnDelete = new JButton("Xóa sinh viên");
        JButton btnRefresh = new JButton("Làm mới");

        btnAdd.setBackground(new Color(40, 167, 69));
        btnEdit.setBackground(new Color(255, 193, 7));
        btnDelete.setBackground(new Color(220, 53, 69));
        btnRefresh.setBackground(new Color(30, 90, 160));

        btnAdd.setForeground(Color.WHITE);
        btnDelete.setForeground(Color.WHITE);
        btnRefresh.setForeground(Color.WHITE);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Sự kiện nút
        btnAdd.addActionListener(e -> addStudent());
        btnEdit.addActionListener(e -> editStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnRefresh.addActionListener(e -> txtSearch.setText(""));

        btnSearch.addActionListener(e -> searchStudent());

        txtSearch.addActionListener(e -> searchStudent());

        return panel;
    }

    // Thêm sinh viên
    private void addStudent() {

        JTextField maSV = new JTextField();
        JTextField hoTen = new JTextField();
        JTextField ngaySinh = new JTextField();
        JComboBox<String> gioiTinh = new JComboBox<>(
            new String[]{"Nam", "Nữ", "Khác"}
        );
        JTextField lop = new JTextField();
        JTextField email = new JTextField();
        JTextField sdt = new JTextField();

        Object[] fields = {
            "Mã sinh viên:", maSV,
            "Họ và tên:", hoTen,
            "Ngày sinh (dd/MM/yyyy):", ngaySinh,
            "Giới tính:", gioiTinh,
            "Lớp:", lop,
            "Email:", email,
            "Số điện thoại:", sdt
        };

        int result = JOptionPane.showConfirmDialog(
            this,
            fields,
            "THÊM SINH VIÊN",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            String ma = maSV.getText().trim();
            String ten = hoTen.getText().trim();

            if (ma.isEmpty() || ten.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "Mã sinh viên và họ tên không được để trống!"
                );
                return;
            }

            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).toString().equalsIgnoreCase(ma)) {
                    JOptionPane.showMessageDialog(
                        this, "Mã sinh viên đã tồn tại!"
                    );
                    return;
                }
            }

            model.addRow(new Object[]{
                ma,
                ten,
                ngaySinh.getText(),
                gioiTinh.getSelectedItem(),
                lop.getText(),
                email.getText(),
                sdt.getText()
            });

            JOptionPane.showMessageDialog(
                this, "Thêm sinh viên thành công!"
            );
        }
    }

    // Sửa sinh viên
    private void editStudent() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, "Vui lòng chọn sinh viên cần sửa!"
            );
            return;
        }

        int row = table.convertRowIndexToModel(selectedRow);

        JTextField maSV = new JTextField(
            model.getValueAt(row, 0).toString()
        );
        JTextField hoTen = new JTextField(
            model.getValueAt(row, 1).toString()
        );
        JTextField ngaySinh = new JTextField(
            model.getValueAt(row, 2).toString()
        );

        JComboBox<String> gioiTinh = new JComboBox<>(
            new String[]{"Nam", "Nữ", "Khác"}
        );
        gioiTinh.setSelectedItem(model.getValueAt(row, 3).toString());

        JTextField lop = new JTextField(
            model.getValueAt(row, 4).toString()
        );
        JTextField email = new JTextField(
            model.getValueAt(row, 5).toString()
        );
        JTextField sdt = new JTextField(
            model.getValueAt(row, 6).toString()
        );

        Object[] fields = {
            "Mã sinh viên:", maSV,
            "Họ và tên:", hoTen,
            "Ngày sinh:", ngaySinh,
            "Giới tính:", gioiTinh,
            "Lớp:", lop,
            "Email:", email,
            "Số điện thoại:", sdt
        };

        int result = JOptionPane.showConfirmDialog(
            this,
            fields,
            "SỬA THÔNG TIN SINH VIÊN",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            if (maSV.getText().trim().isEmpty()
                    || hoTen.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "Mã sinh viên và họ tên không được để trống!"
                );
                return;
            }

            String ma = maSV.getText().trim();

            for (int i = 0; i < model.getRowCount(); i++) {
                if (i != row &&
                    model.getValueAt(i, 0).toString().equalsIgnoreCase(ma)) {
                    JOptionPane.showMessageDialog(
                        this, "Mã sinh viên đã tồn tại!"
                    );
                    return;
                }
            }

            model.setValueAt(ma, row, 0);
            model.setValueAt(hoTen.getText(), row, 1);
            model.setValueAt(ngaySinh.getText(), row, 2);
            model.setValueAt(gioiTinh.getSelectedItem(), row, 3);
            model.setValueAt(lop.getText(), row, 4);
            model.setValueAt(email.getText(), row, 5);
            model.setValueAt(sdt.getText(), row, 6);

            JOptionPane.showMessageDialog(
                this, "Cập nhật sinh viên thành công!"
            );
        }
    }

    // Xóa sinh viên
    private void deleteStudent() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, "Vui lòng chọn sinh viên cần xóa!"
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa sinh viên này?",
            "XÁC NHẬN XÓA",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            int row = table.convertRowIndexToModel(selectedRow);
            model.removeRow(row);

            JOptionPane.showMessageDialog(
                this, "Xóa sinh viên thành công!"
            );
        }
    }

    // Tìm kiếm sinh viên
    private void searchStudent() {

        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(
                RowFilter.regexFilter(
                    "(?i)" + java.util.regex.Pattern.quote(keyword)
                )
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
