
package projetaccov;

public class coordonnees {
    private int x;
    private int y;
    private int altitude;
    
    public coordonnees(int x, int y, int altitude) 
    {
       this.x = x;
       this.y = y;
       this.altitude = altitude;
    }
    
    public int getX ()
    {
        return this.x;
    }
    
    public int getY ()
    {
        return this.y;
    }
    
    public int getAltitude ()
    {
        return this.altitude;
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public void setY (int y)
    {
        this.y = y ;
    }
    
    public void setAltitude (int alt)
    {
        this.altitude = alt ;
    }
    }

