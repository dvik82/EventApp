package com.vermis.eventapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;


public class Controller {

    @FXML
    Button addButton;

    @FXML
    Button saveButton;

    @FXML
    Button loadButton;

    @FXML
    TextField descriptionTextField;

    @FXML
    DatePicker datePicker;

    @FXML
    ListView<LocalEvent> eventList;

    ObservableList<LocalEvent> list = FXCollections.observableArrayList();

    public void initialize() {
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    private void addEvent(Event e) {
        list.add(new LocalEvent(datePicker.getValue(), descriptionTextField.getText()));
        eventList.setItems(list);
        refresh();
    }

    private void refresh() {
        datePicker.setValue(LocalDate.now());
        descriptionTextField.setText(null);
    }

    @FXML
    private int saveList(Event e) {
        try {
            FileWriter out = new FileWriter(new File("listView.dat"));

            StringBuilder sb = new StringBuilder("");
            eventList.getItems().stream().forEach(l -> {
                sb.append(l.toString() + "\n");
            });

           out.write(sb.toString());
           out.close();
           return 1;
        } catch(IOException ex) {
            ex.printStackTrace();
            return 0;
        }

    }

    @FXML
    private int loadList(Event e) {
        try {

            String content = new String(Files.readAllBytes(Paths.get("listView.dat")));
            System.out.println(content);
            String[] lines = content.split("\\r?\\n");
            for(String s : lines) {
                System.out.println("new line: " + s);
                list.add(new LocalEvent(LocalDate.parse(s.substring(4, 14)), s.substring(15)));
            }
            eventList.setItems(list);
            return 1;

        } catch(IOException ex) {
            ex.printStackTrace();
            return 0;
        }

    }

}
