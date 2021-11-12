package com.example.parkeringsautomat2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class Bil{
    public String bilNummer;
    public Date startTid;
    public int plass;
    public boolean kortTid; // true = korttids parkering

    public Bil(String bilNummer, Date startTid, boolean kortTid) {
        // konstruktør
        this.bilNummer = bilNummer;
        this.startTid = startTid;
        this.kortTid = kortTid;
    }

    public String formaterKvittering(){
        // formater kvitteringen etter oppgitt format
        Date nå = new Date();
        String startDatoTid = new SimpleDateFormat("dd.MM.YYYY HH:mm").format(startTid);
        String sluttTid = new SimpleDateFormat("dd.MM.YYYY HH:mm").format(nå);
        return "Kvittering for bilnr " + bilNummer + "\n" +
                "Starttid: " + startDatoTid + " til " + sluttTid + "\n" +
                "Betalt: " + String.format("%.2f", avgift());
    }

    public double getPris(){
        // returner 10 eller 20 kroner avhengig av kortTid eller ikke
        if(kortTid) return 20;
        return 10;
    }
    public double avgift(){
        Date nå = new Date();
        double varighet = (nå.getTime() - startTid.getTime()) / 3_600_000d;
        return getPris() * varighet;

    }
}

class Parkeringshus{
    // opprett arrayet av biler
    ArrayList<Bil> biler = new ArrayList<>();

    public void reserverPlass(Bil enBil){
        biler.add(enBil);
    }

    public String frigjørPlass(String bilNummeret){
        /*
         ** må finne bilen i arrayet
         ** når den er funnet slett den fra arrayet
         ** og formater kvitteringen som returneres
         ** dersom bilen ikke finnes skal man returnere en passenede tekst
         */
        for(Bil bil : biler) {
            if(bil.bilNummer.equals(bilNummeret)) {
                String kvittering = bil.formaterKvittering();
                biler.remove(bil);
                return kvittering;
            }
        }
        return "Fant ikke bilen, prøv igjen!";
    }
}


public class FXMLDocumentController {

    // opprett parkeringshuset
    Parkeringshus parkeringshus = new Parkeringshus();

    @FXML
    private Label lblAvgift;

    @FXML
    private TextField txtBilnummer;

    @FXML
    void kjørUt(ActionEvent event) {
        // kall frigjør plass og legg ut kvitteringen i lblAvgift
        String kvittering = parkeringshus.frigjørPlass(txtBilnummer.getText());
        lblAvgift.setText(kvittering);
    }

    @FXML
    void regKorttid(ActionEvent event) {
        // opprett en bil
        // og kall på reserver plass
        lagreBil(true);
    }

    @FXML
    void regLangtid(ActionEvent event) {
        // opprett en bil
        // og kall på reserver plass
        lagreBil(false);
    }

    private void lagreBil(boolean kortTid) {
        Bil bil = new Bil(txtBilnummer.getText(), new Date(), kortTid);
        parkeringshus.reserverPlass(bil);
    }
}