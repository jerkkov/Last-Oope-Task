package harjoitustyo.dokumentit;

import harjoitustyo.apulaiset.Tietoinen;
import java.util.*;

/**
 * Abstrakti dokumentti -luokka, joka sisältää piirteet yksinkertaiselle dokumentille.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet II, kevät 2020.
 * <p>
 * @author Jerkko Viisteensaari (Jerkko.Viisteensaari@tuni.fi, 430586)
 *
 */
public abstract class Dokumentti implements Comparable<Dokumentti>, Tietoinen<Dokumentti> {

    protected final String EROTIN = "///";
    private int tunniste;
    private String teksti;

    /*
    *
    * Rakentajat
    *
    */
    
    public Dokumentti(int id, String merkkeja) throws IllegalArgumentException {
        tunniste(id);
        teksti(merkkeja);
    }

    /*
    *
    * Aksessorit
    *
     */
    public int tunniste() {
        return tunniste;
    }

    public void tunniste(int id) throws IllegalArgumentException {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        tunniste = id;
    }

    public String teksti() {
        return teksti;
    }

    public void teksti(String merkkeja) throws IllegalArgumentException {
        if (merkkeja == null || merkkeja.equals("")) {
            throw new IllegalArgumentException();
        }
        teksti = merkkeja;
    }

    /**
     * Tutkii sisältääkö dokumentin teksti kaikki syötteinä annetut hakusanat.
     * <p> Kaikkien hakusanojen on löydyttävä tekstistä, jotta haku katsotaan onnistuneeksi</p>
     * @param hakusanat syötteinä annetut hakusanat
     * @return loytyy palauttaa boolean arvon true, jos haku on onnistunut.
     * @throws IllegalArgumentException Jos hakusanat on null tai hakusanat -lista on tyhjä
     */
    public boolean sanatTäsmäävät(LinkedList<String> hakusanat)
            throws IllegalArgumentException {

        //Avaimet ovat tekstistä pilkottuja sanoja joita verrataan myöhemmin hakusanoihin
        String[] avaimet = teksti().split(" ");
        if (hakusanat == null || hakusanat.contains(null) || hakusanat.isEmpty()) {
            throw new IllegalArgumentException();
        }
        
        //Loytyy pysäyttää sisemmän silmukan ja pysayta ulomman
        boolean loytyy = false;
        boolean pysayta = false;
        int ind = 0;
        while (ind < hakusanat.size() && !pysayta) { //Toistolauseke hakusanojen läpikäymiseen    
            loytyy = false;
            
            if (avaimet != null || avaimet.length > 0) {
                //Varsinainen vertailu tehdään sisemmässä toistolausekkeessa
                int i = 0;
                while (i < avaimet.length) { //Toistolauseke avaimien läpikäymiseen
                    if (hakusanat.get(ind).equals(avaimet[i])) {
                        loytyy = true;
                    }
                    i++; //Kasvatetaan avaimien laskuria
                }
                if (!loytyy) {
                    pysayta = true;
                }
            }
            
            ind++; //Kasvatetaan hakusanojen laskuria
        }

        return loytyy;
    }

    /**
     * Muokkaa dokumenttia siten, että tiedonhaku on helpompaa ja tehokkaampaa.
     * <p> Teksti muutetaan pienemmäksi, siitä poistetaan syötteinä annetut välimerkit ja lopulta sulkusanat </p>
     * @param sulkusanat merkkijonolista, joka sisältää kaikki tekstistä poistettavat ns. turhat sanat
     * @param välimerkit syötteinä annetut poistettavat merkit
     * @throws IllegalArgumentException Jos sulkusanat on null, tyhjä ja välimerkit on null tai tyhjä.
     */
    public void siivoa(LinkedList<String> sulkusanat, String välimerkit)
            throws IllegalArgumentException {
        if (sulkusanat == null || sulkusanat.contains(null) || sulkusanat.isEmpty()
                || välimerkit == null || välimerkit.equals("")) {
            throw new IllegalArgumentException();
        }
        
        String siivottava = teksti(); //Haetaan muuttujaan puhdistettavan dokumentin teksti
        
        if (välimerkit.length() > 0 || välimerkit != null || !välimerkit.equals("")) {
            /*
            Käydään välimerkit läpi ja korvataan jokaisella kierroksella 
            kyseinen välimerkki tekstistä tyhjällä merkkijonolla
            */
            for (int i = 0; i < välimerkit.length(); i++) {
                String poistettava = välimerkit.substring(i, i + 1);
                siivottava = siivottava.replace(poistettava, "");
            }
        }

        siivottava = siivottava.toLowerCase(); //Muutetaan teksti pieniksi kirjaimiksi

        String[] siivottavatLista = siivottava.split(" ");
        siivottava = "";
        
        /* 
        Poistetaan sulkusanalistan sanat tekstistä käymällä ulommassa toistolausekkeessa sulkusanalistaa läpi.
        Sisemmässä silmukassa tarkistetaan vastaavatko sanat, jolloin laitetaan tilalle tyhjä merkkijono.
        */
        for (int ind = 0; ind < sulkusanat.size(); ind++) {
            
            for (int i = 0; i < siivottavatLista.length; i++) {
                if (sulkusanat.get(ind).equals(siivottavatLista[i])) {
                    siivottavatLista[i] = "";
                }
            }
            
        }
        
        //Kootaan taulukko takaisin merkkijonoksi
        for (int i = 0; i < siivottavatLista.length; i++) {
            if (!siivottavatLista[i].equals("")) {
                siivottava += siivottavatLista[i] + " ";
            }
        }

        siivottava = siivottava.substring(0, siivottava.length() - 1);
        //Asetetaan siivottu merkkijono uudeksi tekstiksi
        teksti(siivottava);
    }

    /*
    * Object-luokan korvatut metodit.
    *
     */
    
    /**
     * Object-luokan korvattu toString metodi
     *
     * @return palauttaa merkkijonoesityksenä tunnisteen ja tekstin: 11///tämä on esimerkki
     */
    @Override
    public String toString() {
        //String luokanNimi = getClass().getSimpleName(); Tarvitaanko?
        return tunniste + EROTIN + teksti;
    }

    /**
     * Object-luokan korvattu equals metodi, joka vertailee dokumentin tunnisteita
     *
     * @param obj vertailtava apuolio
     * @return palauttaa false, jos tunnisteet eivät täsmää
     */
    @Override
    public boolean equals(Object obj) {
        try {
            Dokumentti toinen = (Dokumentti) obj;

            return (tunniste == toinen.tunniste());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Object-luokan korvattu compareTo metodi, joka vertailee dokumentin tunnisteita
     *
     * @param toinen vertailtava apuolio
     * @return palauttaa -1, jos tunniste on pienempi, 0, jos ne ovat samoja ja 1, jos tunniste on isompi
     */
    @Override
    public int compareTo(Dokumentti toinen) {
        if (tunniste < toinen.tunniste()) {
            return -1;
        } else if (tunniste == toinen.tunniste()) {
            return 0;
        } else {
            return 1;
        }
    }
}