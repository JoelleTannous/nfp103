
package projetaccov;

public class deplacement {
    private int cap;
    private int vitesse;

    public deplacement(int cap, int vitesse) 
    {
        this.cap = cap ;
        this.vitesse = vitesse;
    }
    
    public int getCap () 
    {
        return this.cap;
    }
    
    public int getVitesse ()
    {
        return this.vitesse;
    }
    
    public void setCap (int c)
    {
        this.cap = c ;
    }
    
    public void setVitesse (int v)
    {
        this.vitesse = v ;
    }
}
