
package projetaccov;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Projetaccov {

    public static void main(String[] args) throws IOException, InterruptedException {
        
        Saca saca = new Saca ();
        saca.start();
        
        for(int i = 0; i<3; i++)
        {
            saca.ajouter_avion(new Avion());
        }
    	
        try 
        {			
            while(true)
            {
              
            for (String key : saca.get_mapAvion().keySet())
            {
                saca.get_mapAvion().get(key).se_deplacer();
            }
            }
        } 
        catch (Exception e)
        {
            System.out.println(e.getMessage());       
        }
    }
    
}




