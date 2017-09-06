
package projetaccov;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Saca extends Thread
{
   // private static ArrayList <Avion> listAvion;
    private static HashMap <String,Avion> mapAvion; 
    private static HashMap<String,Avion> avionCtrl;
    
    private static BufferedReader buffer;
    private static ArrayList <BufferedReader> listBuffer;
    
    static ServerSocket  serveur;
    static Socket socket;
    static controlleur controlleur;
    
    
    public Saca()  
    {
       mapAvion = new HashMap<String, Avion>(); 
       listBuffer = new ArrayList<BufferedReader>();
       avionCtrl = new HashMap<String, Avion>();
    }
    
    public HashMap<String, Avion> get_mapAvion() 
    {
        return mapAvion;
    }

    public void set_mapAvion(HashMap<String, Avion> a) 
    {
	this.mapAvion = a;
    }

    public void ajouter_avion (Avion a)
    {
        if (a != null)
        {
           mapAvion.put(a.numeroAvion, a);
        }  
    }
        
    public class AjouterControlAvion extends Thread 
    {
        @Override
        public void run ()
        {
            try 
            {
                serveur = new ServerSocket(4001);
                while (true)
                {
                    socket = serveur.accept();
                    System.out.println ("Controlleur #"+controlleur.num_ctrl+" connecté");
                    controlleur = new controlleur (socket);
                    controlleur.start();
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public static class controlleur extends Thread
    {
        PrintWriter out;
        BufferedReader in;
	Socket socket;
        
        Avion avion;
	String num_avion = "";
        
	String num_ctrl = "";
        String [] c;
        String user;
        String result = null;
        int valeur = 0;
        int num_mod = 0;
    
        public controlleur (Socket socket)
        {
            this.socket=socket;
            try 
            {
                in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter (socket.getOutputStream(),true);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        
    public boolean ctrl_request (String s)
    {
        c = s.split("-");        
        num_ctrl = c[0];
        user = c[1];
        
        if (c[1].equals("4"))
        {
            avionCtrl.remove(num_ctrl);
        }
        else if (user.contains("avion"))
        {
            num_avion = user.substring(5); // avion --> 5 characters
            if (mapAvion.get(num_avion)== null)
            {
                result = "Cette avion n'existe pas.";
                return false;
            }
            else
            {
                for (String key : avionCtrl.keySet())
                {
                    if (num_avion.equals(avionCtrl.get(key).numeroAvion))
                    {
                        result = "Cette avion est controlée par un autre controlleur";
                        return false;
                    }
                }
                avionCtrl.put(num_ctrl, mapAvion.get(num_avion));
            }
        }
        else 
        {
            String [] temp = user.split(".");
            try 
            {
                num_mod = Integer.parseInt(temp[0]);               
            }
            catch (NumberFormatException e)
            {
                  result = "Le numero de modification doit être un nombre";
            }
            try 
            {
                valeur = Integer.parseInt(temp[0]);
                return false;
            }
            catch (NumberFormatException e)
            {
                  result = "La valeur doit être un nombre";
                  return false;
            }
        }
        
        avion = avionCtrl.get(num_ctrl);
        switch (num_mod)
        {
            case 1: avion.changer_vitesse(valeur); break;
            case 2: avion.changer_capacite(valeur); break;
            case 3: avion.changer_altitude(valeur); break;
            default: result = "Numero de modification est faux."; return false;
        }
    return true;    
}
   
        @Override
    public void run ()
    {
        try
        {
            while (true)
            {
                if (ctrl_request(in.readLine()))
                {
                    out.println("Succès !");
                }
                else
                {
                    out.println(result);
                }
                    
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    }
    
    public static class log extends Thread 
    {
        @Override
        public void run ()
        {
            try 
            {
                while (true)
                {
 //                   sleep (1500);
//                    if (collision())
//                    {
//                        System.out.println("Possibilite de collision !");
//                        this.stop();
//                    }
//                    else
//                    {
                        for (String key : mapAvion.keySet())
                        {
                            System.out.println(mapAvion.get(key).afficher_donnees());
                        }
              //      }
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }    
            }
        }
    
    public class ajouterAvion extends Thread
    {
        Socket socket;
        ServerSocket server;
        log log = new log ();
        
        @Override
        public void run()
        {
            try 
            {
                server = new ServerSocket (4000);
                while (true)
                {
                    socket = server.accept();
                    System.out.println("Connection avec Saca est établie." );
                    buffer = new BufferedReader (new InputStreamReader(socket.getInputStream()));
                    System.out.println(buffer.readLine());
                    listBuffer.add(buffer);
                    if (listBuffer.size()==1) log.start();
                }
            } 
            catch (Exception e) 
            {
               System.out.println(e.getMessage());
            }
        }
    }
    
        @Override
   public void run ()
   {
       System.out.println("SACA a demarré");
       ajouterAvion ajouteravion = new ajouterAvion();
       AjouterControlAvion  ajoutercontrolavion = new AjouterControlAvion();
       try 
       {
           ajouteravion.start();
           ajoutercontrolavion.start();
       }
       catch (Exception e)
       {
           System.out.println(e.getMessage());
       }
   }
   
   
}
