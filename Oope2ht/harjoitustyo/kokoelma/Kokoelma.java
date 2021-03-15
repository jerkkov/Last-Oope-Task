package harjoitustyo.kokoelma;

import harjoitustyo.apulaiset.Kokoava;
import harjoitustyo.omalista.OmaLista;
import harjoitustyo.dokumentit.Dokumentti;
import harjoitustyo.dokumentit.Uutinen;
import harjoitustyo.dokumentit.Vitsi;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * Varsinainen kokoelma, joka implementoi Dokumentti -luokan.
 * <p> Kokoelmassa hallinnoidaan dokumentteja, joita voidaan esim. lisätä, poistaa jne.</p>
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet II, kevät 2020.
 * <p>
 * @author Jerkko Viisteensaari (Jerkko.Viisteensaari@tuni.fi, 430586)
 */
public class Kokoelma implements Kokoava<Dokumentti> {

    private OmaLista<Dokumentti> dokumentit;
    public OmaLista<String> sulkusanat;

    public Kokoelma() {
        dokumentit = new OmaLista<Dokumentti>();
        sulkusanat = new OmaLista<String>();
    }

    public OmaLista<Dokumentti> dokumentit() {
        return dokumentit;
    }

    /**
     * Kokoaa merkkijonoja sisältävän taulukon yhdeksi merkkijonoksi lisäämällä " " merkin alkioiden välille
     * <p>
     * Taulukon alusta voi jättää lisäämättä alkioita antamalla parametriksi paikan taulukosta, josta aloitetaan
       lisääminen. </p> 
     *
     * @param ind taulukon paikka, josta lähdetään kokoamaan merkkijonoa
     * @param koottava Merkkijonotaulukko jonka alkiot muutetaan yhdeksi merkkijonoksi
     * @return koottu palauttaa kootun merkkijonon
     */
    public String kokoa(int ind, String[] koottava) {
        String koottu = "";
        
        for (int i = ind; i < koottava.length; i++) {
            if (!koottava[i].equals("")) {
                koottu += koottava[i] + " ";
            }
        }
        
        //Poistetaan viimeinen tyhjä merkki
        koottu = koottu.substring(0, koottu.length() - 1);
        
        return koottu;
    }
    /**
     * Lisää kokoelmaan käyttöliittymän kautta annetun dokumentin.
     * @param uusi Dokumentti, joka lisätään omalle listalle
     * @throws IllegalArgumentException Jos lisättävä dokumentti on null tai samanlainen
     */
    @Override
    public void lisää(Dokumentti uusi) throws IllegalArgumentException {
        //Käydään dokumenttikokoelma läpi vertaillen lisättävää
        boolean samat = false;
        int i = 0;
        if (!dokumentit.isEmpty() && dokumentit != null) {
            while (i < dokumentit.size() && !samat) {
                if (dokumentit.get(i).teksti().equals(uusi.teksti())
                        || dokumentit.get(i).tunniste() == uusi.tunniste() || dokumentit.get(i).equals(uusi)) {
                    samat = true;
                }
                i++;
            }
        }
        
        if (uusi == null || samat) {
            throw new IllegalArgumentException();
        }
        
        //Jos lisättävä ei ollut samanlainen tai null, voidaan lisätä dokumenttikokoelmaan uusi dokumentti
        dokumentit.lisää(uusi);
    }

    /**
     * Hakee dokumenttikokoelmasta parametrina annettua tunnistetta vastaavan dokumentin
     *
     * @param tunniste saadaan syötteenä käyttöliittymältä
     * @return palauttaa tunnistetta vastaavan dokumentin, jos sellainen on olemassa, muutoin null
     */
    @Override
    public Dokumentti hae(int tunniste) {
        /*        
        Käydään dokumenttikokoelma läpi toistolausekkeessa vertailemalla tunnisteita. Osuman sattuessa kohdalle
        keskeytetään silmukka ja palautetaan dokumentti.
         */
        boolean loytyi = false;
        int i = 0;
        while (i < dokumentit.size() && !loytyi) {
            if (dokumentit.get(i).tunniste() == tunniste) {
                loytyi = true;
            }
            i++;
        }
        
        return loytyi ? dokumentit.get(i - 1) : null;
    }
    
    /**
     * Muuttaa päivämäärän, joka on Suomalaista muotoa LocalDate muotoon: 25.6.1992 = 1992-06-1992
     * @param pvm Suomalaisessa muodossa annettu päivämäärä
     * @return uusiPvm palauttaa muutetun LocalDate -tyyppiä olevan päivämäärän
     * @throws IllegalArgumentException Jos päivämäärän muutoksessa tapahtuu virhe
     */
    public LocalDate muutaPaivamaara(String pvm) throws IllegalArgumentException {
        DateTimeFormatter pvmMuuntaja = DateTimeFormatter.ofPattern("d.M.yyyy");
        LocalDate uusiPvm = LocalDate.parse(pvm, pvmMuuntaja);
        
        return uusiPvm;
    }
    
    /**
     * Tarkistetaan lisättävän dokumentin tyyppi, jonka perusteella luodaan oikeantyyppinen olio, joka saa arvoikseen
       syötetyt arvot.
     *
     * @param tarkistettava Käyttäliittymältä annetut tiedot
     * @param tyyppi Minkä tyyppinen lisättävän kuuluu olla: Vitsi = 'v' ja Uutinen = 'u'
     * @throws IllegalArgumentException Jos vitsi -tai uutiskokoelmaan yritetään lisätä väärää tyyppiä
     */
    public void lisaaTyyppi(String[] tarkistettava, char tyyppi) throws IllegalArgumentException {
        /*
        Uutinen -tyyppi tarkistetaan muuttamalla päivämäärää vastaava merkkijono LocalDate muotoon. 
        Jos muuttaminen ei onnistu, niin kyseessä on väärää tyyppiä.
        */
        switch (tyyppi) {
            case 'u':
                if (tarkistettava.length == 3) {
                    try {
                        //Muutetaan merkkijono LocalDate muotoon
                        String apuPvm = tarkistettava[1];
                        LocalDate pvm = muutaPaivamaara(apuPvm);
                        
                        //Lisätään uusi Uutinen
                        lisää(
                            new Uutinen(Integer.parseInt(tarkistettava[0]), pvm, kokoa(2, tarkistettava))
                        );
                    } catch (Exception e) {
                        throw new IllegalArgumentException();
                    }
                }
                break;
            
            /*
            Vitsin lisäys tarkistetaan muuttamalla lisättävän lajia vastaava merkkijono päivämäärä muotoon.
            Kun päivämäärän muuttaminen heittää poikkeuksen tiedetään, että kyseessä on Vitsi
            */
            case 'v':
                if (tarkistettava.length == 3) {
                    boolean virhe = false;
                    try {
                        String apuPvm = tarkistettava[1];
                        LocalDate pvm = muutaPaivamaara(apuPvm);
                        virhe = true;
                        
                    } catch (Exception e) {
                        lisää(
                            new Vitsi(Integer.parseInt(tarkistettava[0]), tarkistettava[1], kokoa(2, tarkistettava))
                        );
                    }
                    //Heitetään poikkeus, jos päivämäärän muuttaminen onnistui.
                    if (virhe) {
                        throw new IllegalArgumentException();
                    }
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Palauttaa dokumenttikokoelman alkuperäiseen kuntoon lataamalla dokumentit uudelleen
     * <p>
     * Lataa myös sulkusanat.</p>
     *
     * @param args Annetut komentoriviparametrit
     * @return tyyppi Palauttaa tiedon dokumenttikokoelman tyypistä
     * @throws Exception Jos tiedosto puuttuu
     */
    public char palauta(String[] args) throws Exception {
        char tyyppi = ' ';
        //Tyhjätään dokumenttikokoelma ennen palautusta
        if (!dokumentit.isEmpty()) {
            dokumentit.clear();
        }
        
        //Tarkastetaan dokumenttikokoelman tyyppi komentoriviparametreista 
        tyyppi = args[0].contains("jokes") ? 'v' : 'u';
        
        //Ladataan dokumenttitiedosto
        File korpus = new File(args[0]);
        BufferedReader br = new BufferedReader(new FileReader(korpus));

        //Käydään tiedostoa läpi rivi kerrallaan pilkkomalla se merkkijonotaulukoksi, joka välitetään lisäysmetodille
        String rivi;
        while ((rivi = br.readLine()) != null) {
            String[] pilkottuTyyppi = rivi.split("///");
            lisaaTyyppi(pilkottuTyyppi, tyyppi);
        }

        //Luetaan sulkusanat tiedostosta ja lisätään ne listaan rivi kerrallaan
        File sulkusanatTeksti = new File(args[1]);
        br = new BufferedReader(new FileReader(sulkusanatTeksti));
        while ((rivi = br.readLine()) != null) {
            sulkusanat.lisää(rivi);
        }
        
        return tyyppi;
    }

    /**
     * Etsii dokumenttikokoelmasta dokumenttia, joka vastaa parametrina annettuja hakusanoja. Varsinainen vertailu
       tehdään Dokumentti -luokassa.
     *
     * @param kayttajanHakusanat Parametrina välitetyt hakusanat
     * @return dokuTunniste Palauttaa löydettyjen dokumenttien tunnisteet
     */
    public String etsi(String kayttajanHakusanat) {
        
        //Lisätään hakusanat uudelle OmaLista:lle
        @SuppressWarnings("unchecked")
        OmaLista<String> hakusanat = new <String> OmaLista();
        String[] apu = kayttajanHakusanat.split(" ");
        for (int i = 0; i < apu.length; i++) {
            hakusanat.lisää(apu[i]);
        }

        //Käydään dokumenttivaraston alkiot läpi vertaillen dokumenttia hakusanoihin
        boolean loytyy = false;
        String dokuTunniste = "";
        int i = 0;
        while (i < dokumentit.size()) {
            loytyy = dokumentit.get(i).sanatTäsmäävät(hakusanat);
            if (loytyy) {
                dokuTunniste += String.valueOf(dokumentit.get(i).tunniste()) + System.lineSeparator();
                loytyy = false;
            }
            i++;
        }
        
        //Poistetaan ylimääräinen rivinvaihto
        if (!dokuTunniste.equals("")) {
            loytyy = true;
            dokuTunniste = dokuTunniste.substring(0, dokuTunniste.length() - 1);
        }
        
        return loytyy ? dokuTunniste : null;
    }

    /**
     * Poistaa annettua tunnistetta vastaavan dokumentin dokumenttikokoelmasta
     * @param tunniste poistettavan dokumentin tunniste
     * @throws IllegalArgumentException Heittää poikkeuksen, jos dokumenttia ei ole
     */
    public void poista(int tunniste) throws IllegalArgumentException {
        Dokumentti haettava = hae(tunniste);
        if (haettava != null) {
            dokumentit.remove(haettava);
        } else {
            throw new IllegalArgumentException();
        }
    }
/**
 * Kutsuu Dokumenttiolion siivoa -metodia, joka helpottaa tiedonhakua.
 * <p> Muuttaa dokumenttien tekstit pieniksi kirjaimiksi, poistaa annetut välimerkit ja poistaa sulkusanat </p>
 * @param valimerkit Syötteenä annetut välimerkit
 * @throws IllegalArgumentException Välittää poikkeuksen Dokumenttioliolta 
 */
    public void siivoa(String valimerkit) throws IllegalArgumentException {
        for(int i = 0; i < dokumentit.size(); i++) {
            dokumentit.get(i).siivoa(sulkusanat, valimerkit);
        }

    }
}
