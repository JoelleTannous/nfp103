
package projetaccov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Thread.sleep;
import java.net.Socket;

public class Avion 
{
    int altitudeMax;
    int altitudeMin;
    int vitesseMax;
    int vitesseMin;
    
    int pause;
    
    String numeroAvion;
    coordonnees coord;
    deplacement dep; 
    
    boolean isCrashed;
    
    Socket socket;
    PrintStream out; 
    BufferedReader in;
    
    String ctrl = "c";
    
public void avion () throws IOException
{
    altitudeMax = 20000;
    altitudeMin = 0;
    vitesseMin = 200;
    vitesseMax = 1000;
    pause = 2000;
    
    // intialisation des paramétres de l'avion
    coord.setX ((int) (1000 + Math.random()*10 % 1000));
    coord.setY ((int) (1000 + Math.random()*10 % 1000));
    coord.setAltitude ((int) (1000 + Math.random()*10 % 1000));

    dep.setCap ( (int)( Math.random() * 10 % 360));
    dep.setVitesse ( (int) (600  + Math.random() * 10 % 200));

    // initialisation du numero de l'avion : chaine de 5 caract?res 
    // formée de 2 lettres puis 3 chiffres
    numeroAvion = generateNum();
    
    ouvrir_communication();
    envoyer_caracteristiques();
}

public int distance(Avion a) 
{   // altitude n'est pas prise en consideration
    return (int) Math.sqrt(Math.pow(a.coord.getX() - this.coord.getX(), 2) + Math.pow(a.coord.getY() - this.coord.getY(), 2));
}

public boolean isIsCrashed() 
{
    return isCrashed;
}

public String generateNum()
{
    char [] n = new char [6];
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuwxyz";
    for(int i=0;i<2;i++)
    {
	int x = (int)Math.floor(Math.random() * 51); 
	n[i]= chars.charAt(x);
    }
    int rand1 = (int)(Math.random()*10);
    int rand2 = (int)(Math.random()*10);
    int rand3 = (int)(Math.random()*10);
            
    String num = new String (n);
    num = num + rand1 + rand2 + rand3;
    return(num);
	   	    
}
  
public boolean ouvrir_communication () throws IOException
{ // permet d'entrer en communication via TCP avec le gestionnaire de vols
    try 
    {
        socket = new Socket ("localhost",4000);
        out = new PrintStream (socket.getOutputStream());
        in = new BufferedReader (new InputStreamReader (socket.getInputStream()));
        return true;
    }
    catch (Exception e)
    {
        System.out.println(e.getMessage());
        return false;
    }
}

public void fermer_communication () 
{ // permet de fermer la communication avec le gestionnaire de vols
    try
    {
        socket.close();
    }
    catch (Exception e)
    {
        System.out.println(e.getMessage());
    }
}

public String afficher_donnees ()
{   
    int x = coord.getX();
    int y = coord.getY();
    int a = coord.getAltitude();
    int v = dep.getVitesse();
    int c = dep.getCap();
    return "Avion " + numeroAvion + " ---> localisation: ( "+ x +" , "+ y +" ) -- altitude: " + a + " -- vitesse: " + v + " -- capacite: " + c ;
}

void envoyer_caracteristiques ()
{ // envoie l'ensemble des caractéristiques courantes de l'avion au gestionnaire de vols
    out.println (afficher_donnees());
}

public synchronized void setControlleur (String c)
{
    this.ctrl = c ;
}

public String getControlleur ()
{
    return this.ctrl;
}

public void changer_vitesse (int vitesse)
{
    if (vitesse < vitesseMin) dep.setVitesse (vitesseMin);
    else if (vitesse > vitesseMax) dep.setVitesse(vitesseMax);
    else dep.setVitesse (vitesse);
}

public void changer_capacite (int capacite)
{
    if ((capacite > 0)&&( capacite <360))
    {
        dep.setCap (capacite);
    }
}

public void changer_altitude (int alt)
{
    if (alt<0) coord.setAltitude (altitudeMin);
    else if (alt > altitudeMax) coord.setAltitude (altitudeMax);
    else coord.setAltitude (alt);
}

void calcul_deplacement() 
{
// vitesse trop faible - crah de l'avion - fermer comm - system.exit()
    double cosinus, sinus;
    double dep_x, dep_y;
    int nb;

    if (dep.getVitesse() < vitesseMin) {
        System.out.println("Vitesse trop faible : crash de l'avion");
        fermer_communication();
        System.exit(2);
    }
    if (coord.getAltitude() == 0) {
        System.out.println("L'avion s'est ecrase au sol");
        fermer_communication();
        System.exit(3);
    }
    //cos et sin ont un paramétre en radian, dep.cap en degré nos habitudes francophone
    /* Angle en radian = pi * (angle en degré) / 180 
       Angle en radian = pi * (angle en grade) / 200 
       Angle en grade = 200 * (angle en degré) / 180 
       Angle en grade = 200 * (angle en radian) / pi 
       Angle en degré = 180 * (angle en radian) / pi 
       Angle en degré = 180 * (angle en grade) / 200 
     */
    
    cosinus = cos(dep.getCap() * 2 * Math.PI / 360);
    sinus = sin(dep.getCap() * 2 * Math.PI / 360);

    //newPOS = oldPOS + Vt
    dep_x = cosinus * dep.getVitesse() * 10 / vitesseMin;
    dep_y = sinus * dep.getVitesse() * 10 / vitesseMin;

    // on se d?place d'au moins une case quels que soient le cap et la vitesse
    // sauf si cap est un des angles droit
    if ((dep_x > 0) && (dep_x < 1)) dep_x = 1;
    if ((dep_x < 0) && (dep_x > -1)) dep_x = -1;

    if ((dep_y > 0) && (dep_y < 1)) dep_y = 1;
    if ((dep_y < 0) && (dep_y > -1)) dep_y = -1;

    //printf(" x : %f y : %f\n", dep_x, dep_y);

    coord.setX( coord.getX() + (int) dep_x );
    coord.setY( coord.getY() + (int) dep_y );

}

public void se_deplacer () throws InterruptedException
{
    sleep (pause);
    calcul_deplacement();
    envoyer_caracteristiques();
}


}
