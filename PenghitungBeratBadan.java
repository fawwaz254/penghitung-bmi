package com.example.demo2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JOptionPane;

public class PenghitungBeratBadan extends Application {

    private TextField entryTinggi;
    private TextField entryBerat;
    private Label labelHasil;
    private Label labelKategori;
    private Label labelSaran;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Penghitung Berat Badan");

        // Membuat GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        // Label dan TextField untuk Tinggi
        Label labelTinggi = new Label("Tinggi (cm) [50-300]:");
        GridPane.setConstraints(labelTinggi, 0, 0);
        entryTinggi = new TextField();
        entryTinggi.setPromptText("Masukkan tinggi (50-300) ");
        GridPane.setConstraints(entryTinggi, 1, 0);

        // Label dan TextField untuk Berat Badan
        Label labelBerat = new Label("Berat Badan (kg) [2-500] :");
        GridPane.setConstraints(labelBerat, 0, 1);
        entryBerat = new TextField();
        entryBerat.setPromptText("Masukkan BB (2-500)");
        GridPane.setConstraints(entryBerat, 1, 1);

        // Tombol Hitung BMI
        Button tombolHitung = new Button("Hitung BMI");
        GridPane.setConstraints(tombolHitung, 0, 2, 2, 1);
        tombolHitung.setOnAction(e -> hitungBMI());

        Button tombolSimpan = new Button("Simpan");
        GridPane.setConstraints(tombolSimpan, 0, 6, 2, 1);
        tombolSimpan.setOnAction(e -> simpan());
        gridPane.getChildren().add(tombolSimpan);

        Button tombolBaca = new Button("Menampilkan Data");
        GridPane.setConstraints(tombolBaca, 0, 7, 2, 1);
        tombolBaca.setOnAction(e -> Showdata());
        gridPane.getChildren().add(tombolBaca);

        // Label Hasil BMI
        labelHasil = new Label("BMI:");
        GridPane.setConstraints(labelHasil, 0, 3, 2, 1);

        // Label Kategori BMI
        labelKategori = new Label("Kategori:");
        GridPane.setConstraints(labelKategori, 0, 4, 2, 1);

        // Label Saran Kesehatan
        labelSaran = new Label("Saran:");
        GridPane.setConstraints(labelSaran, 0, 5, 2, 1);

        // Menambahkan elemen-elemen ke GridPane
        gridPane.getChildren().addAll(
                labelTinggi, entryTinggi,
                labelBerat, entryBerat,
                tombolHitung,
                labelHasil,
                labelKategori,
                labelSaran
        );

        // Membuat scene dan menetapkannya ke primaryStage
        Scene scene = new Scene(gridPane, 400, 350);
        primaryStage.setScene(scene);

        // Menampilkan primaryStage
        primaryStage.show();
    }

    private void resetLabelValues() {
        labelHasil.setText("BMI:");
        labelKategori.setText("Kategori:");
        labelSaran.setText("Saran:");
    }

    private void simpanInput(double tinggi, double berat) {
        // Melakukan operasi penulisan ke file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("input_tinggi_berat.txt"))) {
            writer.write("Tinggi: " + tinggi);
            writer.newLine();
            writer.write("Berat: " + berat);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void simpan() {
        // Mendapatkan nilai-nilai tinggi, berat, BMI, kategori, dan saran
        String tinggiStr = entryTinggi.getText();
        String beratStr = entryBerat.getText();
        String hasilBMI = labelHasil.getText();
        String kategori = labelKategori.getText();
        String saran = labelSaran.getText();

        // Melakukan operasi penulisan ke file
        // Misalnya, menggunakan BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("hasil_bmi.txt"))) {
            writer.write("Tinggi: " + tinggiStr); // Menyimpan nilai tinggi
            writer.newLine();
            writer.write("Berat: " + beratStr); // Menyimpan nilai berat
            writer.newLine();
            writer.write(hasilBMI);
            writer.newLine();
            writer.write(kategori);
            writer.newLine();
            writer.write(saran);
            writer.newLine();
            writer.flush();
            writer.close();

            // Setelah menyimpan, reset nilai-nilai label
            resetLabelValues();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void Showdata() {
        try (BufferedReader reader = new BufferedReader(new FileReader("hasil_bmi.txt"))) {
            String line;
            StringBuilder data = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                data.append(line).append("\n");
            }

            // Menampilkan data dari file pada layer output
            labelHasil.setText(data.toString());

            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    private void hitungBMI() {
        try {
            double tinggi = Double.parseDouble(entryTinggi.getText());
            double berat = Double.parseDouble(entryBerat.getText());

            if (tinggi < 50 && tinggi > 300 || berat < 2 && berat > 500) {
                if (tinggi < 50) {
                    throw new IllegalArgumentException("Tinggi minimal adalah 50 cm");
                } else if (berat < 2) {
                    throw new IllegalArgumentException("Berat minimal adalah 2 kg");
                } else {
                    throw new IllegalArgumentException("Masukkan tinggi antara 50-300 cm dan berat antara 2-500 kg");
                }
            }

            double bmi = berat / Math.pow(tinggi / 100, 2);
            String kategori = kategorikanBMI(bmi);
            String saran = saranKesehatan(kategori);

            labelHasil.setText("BMI: " + String.format("%.2f", bmi));
            labelKategori.setText("Kategori: " + kategori);
            labelSaran.setText("Saran: " + saran);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Masukkan harus berupa bilangan bulat (integer).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private String kategorikanBMI(double bmi) {
        if (bmi < 18.5) {
            return "Kurang berat badan";
        } else if (bmi < 24.9) {
            return "Normal";
        } else if (bmi < 29.9) {
            return "Kelebihan berat badan";
        } else {
            return "Obesitas";
        }
    }

    private String saranKesehatan(String kategori) {
        switch (kategori) {
            case "Kurang berat badan":
                return "Tingkatkan asupan nutrisi.";
            case "Normal":
                return "Pertahankan pola makan sehat.";
            case "Kelebihan berat badan":
                return "Kurangi asupan kalori dan tingkatkan aktivitas fisik.";
            case "Obesitas":
                return "Konsultasikan dengan profesional kesehatan.";
            default:
                return "";
        }
    }
}