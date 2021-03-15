package harjoitustyo.kayttoliittyma;

import harjoitustyo.kokoelma.Kokoelma;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Käyttöliittymä - Välitetään syötteet kokoelmalle ja kokoelmalta käyttöliittymälle. Vuorovaikuttaa käyttäjän kanssa.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet II, kevät 2020.
 * </p>
 * @author Jerkko Viisteensaari (Jerkko.Viisteensaari@tuni.fi, 430586)
 */
public class Kayttoliittyma {

    boolean sulje = false;
    final String HYVASTIT = "Program terminated.";
    final String TERVEISET = "Welcome to L.O.T.";
    final String VIRHE = "Error!";
    final String VAARA_MAARA = "Wrong number of command-line arguments!";
    final String PUUTTUU = "Missing file!";
    final String PYYNTO = "Please, enter a command:";
    List<String> komennot = new ArrayList<String>();
    boolean echo = false;
    Kokoelma korpus = new Kokoelma();

    public Kayttoliittyma(String[] komentoriviparametrit) {
        komennot.add("print");
        komennot.add("find");
        komennot.add("remove");
        komennot.add("reset");
        komennot.add("add");
        komennot.add("echo");
        komennot.add("polish");
        komennot.add("quit");

    }
    /**
     * Tarkistaa, että annettu komento on oikeellinen
     * @param komento Syötteenä annettu komento
     * @return apu Palauttaa komennon merkkijonotaulukkona
     * @throws IllegalArgumentException Jos komento ei ole oikeellinen
     */
    public String[] tarkista(String komento) throws IllegalArgumentException {
        boolean loytyy = false;
        
        //Vertaillaan silkumassa onko syötteenä annettu komento oikeellinen
        String[] apu = komento.split(" ");
        int i = 0;
        while (i < komennot.size() && !loytyy) {
            if (apu[0].equals(komennot.get(i))) {                  
                loytyy = true;
            }
            i++;
        }

        if (!loytyy) {
            throw new IllegalArgumentException();
        }
        
        return apu;
    }
    
    /**
     * Tarkistetaan onko echo komento annettu
     * @param komento Syötteenä annettu komento
     */
    public void tarkistaEcho(String komento) {
        if (!echo && komento.equals("echo")) {
            echo = true;
        } else if (echo && komento.equals("echo")) {
            echo = false;
        }
    }
    
    /**
     * Tarkistaa ja muuttaa String tyypin int tyypiksi
     * @param komento syötteenä annettu tarkistettava
     * @return numero muutettu String tyypistä int tyypiksi
     * @throws IllegalArgumentException Jos syöte ei ole kokonaisluku
     */
    public int onkoNumero(String komento) throws IllegalArgumentException {
        if (komento == null) {
            throw new IllegalArgumentException();
        }
        try {
            int numero = Integer.parseInt(komento);
            
            return numero;
        } 
        catch (NumberFormatException nfe) {
            throw new IllegalArgumentException();
        }

    }
    
    /**
     * print - Välittää kokoelmalle haettavan dokumentin tunnisteen
     * <p> Jos komento on pelkästään print tulostetaan kaikki dokumettikokoelman dokumentit </p>
     * @param komento haettavan tunniste
     * @throws IllegalArgumentException Kun komento ei muotoa: print tai print 1
     */
    public void hae(String[] komento) {
        //Jos komento on pelkästään print
        if (komento.length == 1) {
            Iterator iteraattori = korpus.dokumentit().iterator();
            while(iteraattori.hasNext()) {
                System.out.println(iteraattori.next().toString());
            }
        }
        //Kun haetaan tunnisteella dokumenttia
        else if (komento.length == 2) {
            int tunniste = onkoNumero(komento[1]);
            System.out.println(korpus.hae(tunniste).toString());
        }
        else {
            throw new IllegalArgumentException();
        }
        
    }
    /**
     * find - Välittää kokoelmalle syötteenä annetut hakusanat
     * @param komento Syötteenä annetut hakusanat
     */
    public void etsi(String[] komento) {
        //Kootaan hakusanat jättämällä ensimmäinen alkio pois (komento) ja palautetaan tunnisteet
        String hakusanat = korpus.kokoa(1, komento);
        String dokuTunniste = korpus.etsi(hakusanat);
        
        //Tarkastetaan löytyikö dokumentteja
        if (dokuTunniste != null) {
            System.out.println(dokuTunniste);
        }
    }
    
    /**
     * remove - Välittää kokoelmalle syötteenä annetun poistettavan tunnisteen
     * @param komento syötteenä annettu tunniste
     * @throws IllegalArgumentException Jos komento on vääränlainen: esim. remove 1
     */
    public void poista(String[] komento) throws IllegalArgumentException {
        //Jos komento on vääränlainen
        if (komento.length != 2) {
            throw new IllegalArgumentException();
        }
        
            //Tarkistetaan, että tunniste on kokonaisluku
            int tunniste = onkoNumero(komento[1]);
            korpus.poista(tunniste);
    }
    
    /**
     * add - Välittää kokoelmalle syötteenä annetut olion tiedot
     * @param komento syötteenä annetut tiedot
     * @param tyyppi Mitä tyyppiä kokoelmaan tallennetaan
     * @throws IllegalArgumentException Jos komento ei ole 2 pituinen
     */
    public void lisaa(String[] komento, char tyyppi) throws IllegalArgumentException {
        //Kootaan tiedot jättämällä komento "add" pois merkkijonosta ja pilkotaan tiedot, jotka välitetään kokoelmalle
        String apu = korpus.kokoa(1, komento);
        komento = apu.split("///");
        if (komento.length != 3) {
            throw new IllegalArgumentException();
        }
        korpus.lisaaTyyppi(komento, tyyppi);
    }
    
    /**
     * polish - siivoaa kokoelman dokumenteista välimerkit, jotka on annettu syötteenä
     * @param komento syötteenä annetut välimerkit
     * @throws IllegalArgumentException Jos komento ei ole oikeellinen: esim. remove ,.?
     */
    public void siivoa(String[] komento) throws IllegalArgumentException {
        if (komento.length != 2) {
            throw new IllegalArgumentException();
        }
            korpus.siivoa(komento[1]);
        
    }

    public void Kayttoliittyma(String[] args) {
        Scanner lukija = new Scanner(System.in);
        System.out.println(TERVEISET);

        boolean lataa = true;
        char tyyppi = ' ';
        while (!sulje) { //Käynnistetään pääsilmukka
            
            try {
                /*
                Ladataan ensimmäisen kerran dokumentit dokumenttikokoelmaan ja tarkastetaan,
                että komentoriviparametrit ovat oikeelliset
                */
                if (lataa && args.length == 2) {
                    lataa = false;
                    tyyppi = korpus.palauta(args);
                }
                else if (args == null || args.length < 2 || args.length > 2) {
                    throw new IllegalArgumentException();
                }

                System.out.println(PYYNTO);
                String komento = lukija.nextLine().trim();
                
                //Tarkistetaan echo
                tarkistaEcho(komento);
                if (echo) {
                    System.out.println(komento);
                }
                
                //Tarkistetaan onko annettu komento oikeellinen
                String[] pilkottuKomento = tarkista(komento);
                String alku = pilkottuKomento[0];
                
                //Kutsutaan Switch case:n avulla oikeaa metodia
                switch (alku) {
                    case "print":
                        hae(pilkottuKomento);
                        break;

                    case "find":
                        etsi(pilkottuKomento);
                        break;

                    case "remove":
                        poista(pilkottuKomento);
                        break;

                    case "reset":
                        if (pilkottuKomento.length != 1) {
                            throw new IllegalArgumentException();
                        }
                        tyyppi = korpus.palauta(args);
                        break;

                    case "add":
                        lisaa(pilkottuKomento, tyyppi);
                        break;

                    case "quit":
                        if (pilkottuKomento.length != 1) {
                            throw new IllegalArgumentException();
                        }
                        sulje = true;
                        break;

                    case "polish":
                        siivoa(pilkottuKomento);
                        break;

                    case "echo":
                        if (pilkottuKomento.length != 1) {
                            throw new IllegalArgumentException();
                        }
                        break;
                }
                
            //Heitetään poikkeukset
            } catch (Exception e) {
                if (lataa) {
                    System.out.println(VAARA_MAARA); //Suljetaan ohjelma, koska komentoriviparametreja väärä määrä
                    sulje = true;
                } 
                else if (e instanceof FileNotFoundException) {
                    System.out.println(PUUTTUU); //Suljetaan ohjelma, koska ladattavaa tiedostoa ei löydy
                    sulje = true;
                } 
                else {
                    System.out.println(VIRHE); //Tavanomainen poikkeus
                }
            }
        }
        System.out.println(HYVASTIT); //Heitetään hyvästit
    }
}
