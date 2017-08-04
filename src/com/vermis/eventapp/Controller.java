package com.vermis.eventapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javax.swing.event.HyperlinkEvent;
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

    private ObservableList<LocalEvent> list = FXCollections.observableArrayList();

    private ContextMenu rightClickMenu = new ContextMenu();

    public void initialize() {

        datePicker.setValue(LocalDate.now());

        MenuItem clearEventsItem = new MenuItem("Clear Events");
        clearEventsItem.setOnAction(event -> clearEventList());

        rightClickMenu.getItems().add(clearEventsItem);
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
            eventList.getItems().forEach(l -> sb.append(l.toString() + "\n"));

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

    @FXML
    private void handleMouseEvent(MouseEvent e) {
        if(e.getButton() == MouseButton.SECONDARY) {
            eventList.setOnContextMenuRequested(ev -> rightClickMenu.show(eventList, ev.getScreenX(), ev.getScreenY()));
        }
    }

    private void clearEventList() {
        eventList.setItems(null);
        list.clear();
    }
}
