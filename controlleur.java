
package projetaccov;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class controlleur extends Thread
{
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    
    String response;
    String user;
    String  avion;
    String num;
    
    public controlleur (String num) 
    {
        this.num = num;
    }
       
    
    @Override
    public void run ()
    {
        BufferedReader read = new BufferedReader (new InputStreamReader(System.in));
        try
        {
            socket = new Socket ("localhost",4001);
            out = new PrintWriter (socket.getOutputStream(),true);
            in = new BufferedReader (new InputStreamReader (socket.getInputStream())); 
            
            
            while (avion == null)
            {   
                System.out.println("Entrez le numero de l'avion.");
                user = read.readLine();
                
                    out.println(num + "-avion" + user); // envoyer a saca: numctrl-numavion
                    response = in.readLine();
                    System.out.println("SACA répond :"+response);
                    if(response!=null) avion = user;
            }
            while (true)
            {
                System.out.println ("Entrez un nombre de la liste suivante "
                        + "puis un point suivi de la nouvelle valeur:"
                        + " \n 1. changer vitesse \n 2. changer capacite \n 3. changer altitude \n 4. sortir");
                user = read.readLine();
                out.println(num + "-" + user); // envoyer a saca: numctrl-#mod.valeur
                response = in.readLine();
                if (response != null) System.out.println(response);
                if (user.split(".")[1].equals("4")) {Thread.interrupted();System.exit(0);}
            }
        }
            
        
        catch (Exception e)
        {
            System.out.println(e.getMessage());       
        }
    } 
}
