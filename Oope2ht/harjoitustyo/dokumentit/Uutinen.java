package harjoitustyo.dokumentit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Julkinen Uutinen-luokka, joka sisältää piirteet yksinkertaiselle uutiselle ja perii Dokumentti-luokan.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet II, kevät 2020.
 * <p>
 * @author Jerkko Viisteensaari (Jerkko.Viisteensaari@tuni.fi, 430586)
 * 
 */
public class Uutinen extends Dokumentti {
    private LocalDate päivämäärä;

    public Uutinen(int id, LocalDate päivä, String merkkeja) throws IllegalArgumentException {
        super(id, merkkeja);
        päivämäärä(päivä);
    }
    
    public LocalDate päivämäärä() {
        return päivämäärä;
    }
    
    public void päivämäärä(LocalDate päivä)  throws IllegalArgumentException {
        if (päivä == null) {
            throw new IllegalArgumentException();
        }
        
        päivämäärä = päivä;;
    }
    
    /*
    * Object-luokan korvatut metodit.
    *
    */
    
    /**
     * Dokumentti-luokalta peritty object-luokan korvaus. Luokassa muutetaan myös 
     * @return palauttaa merkkijonoesityksenä tunnisteen, päivämäärän ja tekstin
     *  <p>esim: 11///1978.05.16///tämä on esimerkkiuutinen</p>
     */
    @Override
    public String toString() {
        LocalDate muutettuPäivä = päivämäärä();   
        DateTimeFormatter muuntaja = DateTimeFormatter.ofPattern("d.M.yyyy");
        String apu = muutettuPäivä.format(muuntaja);
        
        return super.tunniste() + EROTIN + apu + EROTIN + super.teksti();
    }
}
