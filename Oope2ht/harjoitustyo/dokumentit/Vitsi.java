package harjoitustyo.dokumentit;

/**
 * Julkinen Vitsi-luokka, joka sisältää piirteet yksinkertaiselle vitsille ja perii Dokumentti-luokan.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet II, kevät 2020.
 * <p>
 * @author Jerkko Viisteensaari (Jerkko.Viisteensaari@tuni.fi, 430586)
 * 
 */
public class Vitsi extends Dokumentti {
    private String laji;
    
    public Vitsi(int id, String tyyppi, String merkkeja) throws IllegalArgumentException {
        super(id, merkkeja);
        laji(tyyppi);
    }
    
    public String laji() {
        return laji;
    }
    
    public void laji(String tyyppi) throws IllegalArgumentException {
        if (tyyppi == null || tyyppi.equals("")) {
            throw new IllegalArgumentException();
        }
        laji = tyyppi;
    }
    
    /*
    * Object-luokan korvatut metodit.
    *
    */
    
    /**
     * Dokumentti -luokalta peritty object-luokan korvaus. 
     * @return palauttaa merkkijonoesityksenä tunnisteen, lajin ja tekstin
     * <p>esim: 11///miscellaneous///tämä on esimerkkivitsi</p>
     */
     @Override
    public String toString() {
        return super.tunniste() + EROTIN + laji + EROTIN + super.teksti();
    }
    
}
