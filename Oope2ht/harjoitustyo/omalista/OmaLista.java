package harjoitustyo.omalista;
import java.util.*;

/**
 * 
 * Implementoi Ooperoiva -rajapinan ja toteuttaa lisää-metodin
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet II, kevät 2020.
 * <p>
 * @author Jerkko Viisteensaari (Jerkko.Viisteensaari@tuni.fi, 430586)
 * 
 */
public class OmaLista<E> extends LinkedList<E> implements harjoitustyo.apulaiset.Ooperoiva<E> {
   
    /**
     * Lisää listalle uuden alkion sen suuruusjärjestyksen mukaiseen paikkaan.
     <p>
     * Käyttää hyödykseen Comparable -rajapinnan compareTo-metodia, jolla vertaillaan paluuarvona saatua tulosta
     * CompareTo-metodi palauttaa arvon 1, 0 tai -1. 
     * Alustetaan int tyyppinen taulukko, joka saa kookseen OmaListan koon
     * Käydään silmukassa läpi OmaListaa. Vertaillaan CompareTo-metodilla alkioiden tunnisteita.
     * Palautetaan tulokset taulukkoon, joka vastaa kooltaan ns. rakenteeltaan OmaaListaa.
     * Kasvatetetaan samoilla paluuarvoilla summa -ja paikka nimisiä muuttujia.  
     * Näiden ero on, että paikka -muuttujaan lisätään vain positiiviset arvot
     </p>
     <p>
     * Toistolausekkeen loputtua summa -ja paikka muuttujan avulla päätellään lisättävän alkion paikka pinossa.
     * Jos lisättävä on suurempi kuin mikään verrrattavista viitteistä, niin kaikki paluuarvot ovat 1. 
     * Sama pätee, jos paluuarvot ovat -1, voidaan päätellä arvon olevan pienin.
     * Esim. Lisättävä: 15: {2,5,7,12}  | Lisättävä: 1: {2,5,7,12}
     *               Paluuarvo:1,1,1,1,1  |      Paluuarvo:-1,-1,-1,-1,-1 
     * Kun paluuarvoina saadaan positiivisia ja negatiivisia arvoja tarvitaan paikka-muuttujaa paikan määrittämiseen.
     * Esim. Lisättävä: 6: {2,5,7,12}
     *            Paluuarvo:1,1,-1,-1,-1
     * Täten paikka-muuttuja saa arvokseen 2. Kun muuttujan arvoon lisätään yhdellä,
     * sysätään paikalla oleva pinossa eteenpäin ja 6 asettuu tilalle 
     * @param uusi pinolle lisättävä alkio, joka pitää saada suuruusjärjestyksensä mukaiselle paikalleen
     </p>
     * @throws IllegalArgumentException Jos lisättävä on null tai lisättävä ei peri Comparable -rajapintaa
     */
   @SuppressWarnings({"unchecked"})
   @Override
   public void lisää(E uusi) throws IllegalArgumentException {
       //Jos lista on tyhjä, niin voidaan lisätä sinne suoraan uusi alkio

       if (uusi == null || !(uusi instanceof Comparable)){
           throw new IllegalArgumentException();
       }
       if (size() > 0) {  
            //Tehdään apuviite
            Comparable lisattava = (Comparable)uusi;
            int summa = 0;
            int paikka = 0;
            //Luodaan uusi taulukko, joka saa kookseen listan koon
            int[] tulokset = new int [size()];
            
            //Käydään lista läpi alkio kerrallaan
            int ind = 0;
            while(ind < size()) {
                //Haetaan nykyisen Comparable -viite
                Comparable nykyinen = (Comparable)get(ind);
                //Verrataan compararable metodilla listan alkion nykyistä viitetttä ja lisättävää viitettä
                //Palautetaan vertailun tulokset taulukkoon
                 
                if(lisattava.compareTo(nykyinen) >= 0) {
                     tulokset[ind] = 1;
                 }

                summa =+ tulokset[ind];
                //Lisätään lukumuuttujaan vertailun tulos on 1
                if (tulokset[ind] >= 0) {
                    paikka += tulokset[ind];
                }
                ind++;
            }
            
            //Vertaillaan onko lisättävä arvo suurempi kuin kaikki listan arvot
            if (summa == size()) {
                //Lisätään suurin arvo listan päähän
                addLast(uusi);
            }
            //Vertaillaan onko lisättävä arvo pienempi kuin kaikki listan alkiot
            else if (summa == size() * -1) {
                //Lisätään pienin arvo listan alkuun
                addFirst(uusi);
            }
            
            else {
                //Lisätään uusi alkio paikkaan, joka ei ole pinon alussa tai lopussa
                add(paikka, uusi); 
                    
            }
       }
       //Lisätään uusi alkio tyhjään listaan
       else {
           add(uusi);
       }
   }
}
