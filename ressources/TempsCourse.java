package ressources;
/**
 * Temps course sert à calculer le temps d'un compétiteur en fonction de ses pénalités et de son temps réel. Cette application devra permettre de 
 * saisir les données de la course,
 * saisir les résultats de chaque manche (manche par manche)
 * afficher les résultats (temps compensés) de la course dans l’ordre des brassards,
 * afficher le podium de la course (les 3 meilleurs temps). 
 * @version (finale)
 */
import java.util.Scanner;
public class TempsCourse
{
    /**
     * Le programme main permet d'appeler les fonctions de saisie, de calcul et d'affichage.
     * C'est grâce à ce programme main que l'utilisateur pourra saisir les données de la course, les résultats de chaque manche et afficher les résultats de la course.
     * @author Alex
     * @coauthor Yahya/Titouan
     */
    public static void main(String[] args){
        //Initialisation des variables
        int nbComp, lenPiste, nbObstacle, nbBarres;
        int nbBarresTombees, nbRefus, tempsReel,tempsTour;
        boolean chute;
        Scanner clavier= new Scanner(System.in);
        int[] scores=new int[50];
        int numComp;
        //Traitement des variables
        System.out.println("Combien de compétiteur il y a dans cette compétition ?");
        nbComp= Saisie.saisieIntMinMax(1,50);
        System.out.println("Quelle est la longueur de la piste ? (en mètres)");
        lenPiste= Saisie.saisieIntMin(1);
        System.out.println("Combien il y a d'obstacle ?");
        nbObstacle= Saisie.saisieIntMinMax(0,lenPiste); // On considère qu'il ne peut pas y avoir plus d'obstacle que de mètres de piste. 
        System.out.println("Quel est le nombre total de barres ?");
        nbBarres= Saisie.saisieIntMinMax(nbObstacle*2,nbObstacle*4);

        for (int i=0;i<nbComp;i++){
            scores[i]=0;
            numComp=i+1;
            for (int j=0; j<2 && scores[i]!=-1;j++){
                System.out.println("Combien de barre le compétiteur n°"+numComp+" a-t-il fait tombé ?");
                nbBarresTombees=Saisie.saisieIntMinMax(0,nbBarres);
                System.out.println("Combien de refus le compétiteur n°"+numComp+" a-t-il reçu ?");
                nbRefus= Saisie.saisieIntMin(0);
                System.out.println("Le compétiteur n°" +numComp+ "est-il tombé ?");
                chute=clavier.nextBoolean();
                System.out.println("Quel a était le temps du compétiteur n°"+numComp+" ? (en ms)");
                tempsReel= Saisie.saisieIntMin(0);
                tempsTour=calculTemps(tempsReel, nbBarresTombees, nbRefus, chute, lenPiste);
                if (tempsTour!=-1){
                    scores[i]+=calculTemps(tempsReel, nbBarresTombees, nbRefus, chute, lenPiste);//retourne le temps réel
                }else{
                    scores[i]=-1;
                }
            }
        }
        //Affichage du podium et des temps de chaques compétiteurs.
        afficherPodium(scores, nbComp); //fonction max à 3 valeurs
    }
    
    /**
     * @Author: Titouan
     * @param pfTemps IN: 1 entier représentant le temps chronométré
     * @param pfNbBarres IN : 1 entier représentant le nombre de barres renversé
     * @param pfNbRefus IN : 1 entier représentant le nombre de refus enregistré
     * @param pfLenPiste IN : 1 entier représentant la longueur de la piste
     * @param pfChute IN: 1 bouléen représentant si oui ou non le candidat est tombé
     * @param tpsTotal OUT: 1 entier représentant le temps avec les pénalités
     */
    public static int calculTemps(int pfTemps, int pfNbBarres, int pfNbRefus, boolean pfChute, int pfLenPiste){
        int tpsTotal=pfTemps;
        
        // Je renvoie -1 si le candidat est éliminé 
        if( pfNbRefus>2 || pfChute || pfTemps>120000 && pfLenPiste<=600 || pfTemps>180000 && pfLenPiste>600){
             tpsTotal=-1;
        }else{
        // Sinon je calcule le temps avec pénalités selon la formule
            tpsTotal+=pfNbBarres*8000;
        }
        // Je renvoie enfin le résultat
        return tpsTotal;
    }
    
    
     /** Permet de convertir la valeur saisie en minutes, secondes, ms
     * @author Alex
     * @coauthor Titouan
     * @param pfTpsms IN : le temps que l'utilisateur veut convertir 
     */
    public static void conversion(int pfTpsms){
        int tpsms; //temps en ms
        int tpss; //temps en s
        int tpsmin; //temps en min
        
        tpss=pfTpsms/1000;
        tpsms=pfTpsms%1000;
        tpsmin=tpss/60;
        tpss=tpss%60;
        System.out.println(tpsmin+ "min "+tpss+"s "+tpsms + "ms");
    }
    
    
    /** Permet d'afficher les premiers du classement
     * @author Yahya
     * @coauthor Titouan
     * @param pfscore IN: Tableau contenant la somme des temps des 2 manches + pénalités
     * @param pfnbComp IN: nombre de compétiteur dans le tableau
     */
     public static void afficherPodium(int pfscore[],int pfnbComp){
         //initialisations des compteurs enregistrant le nombre de candidats à égalité
        int cptp=0,cpts=0,cptt=0;
        //créations des tableaux pour stocker le  numéro des meuilleurs compétiteurs
        int tabp[]= new int[pfnbComp];
        int tabs[]= new int[pfnbComp];
        int tabt[]= new int[pfnbComp];
        //initialisations des variables qui vont contenir le temps des meuilleurs compétiteurs selon leurs positions
        int first=2147483645;
        int second=2147483646;
        int third=2147483647;
        //boucle for qui parcours tout le tableau du temps des compétiteurs afin de pouvoir créer le podium
        for(int i=0;i<pfnbComp;i++){
            // On affiche le score du candidat
            i++;
            Sytem.out.println("Le candidat numéro : " + i + "\nA mis un temps de : ");
            i--;
            conversion(scores[i]);
            //On teste une égalité pour pouvoir sauvegarder celle-ci si nécessaire
            if(pfscore[i]==first && pfscore[i]>0){
                tabp[cptp]=i;
                cptp+=1;
                // En cas d'égalité on sauvegarde la position du premier ex aequo dans un tableau
            }
            //Sinon on se demande si il est un nouveau premier
            else if (pfscore[i]<first && pfscore[i]>0){
                // On rend les premiers deuxième et les deuxièmes troisièmes
                for (int j=0;j<cptp || j<cpts;j++){
                    if (j<cpts){// On verifie de ne pas être arrivé au bout d'un des tableaux pour n'utiliser qu'une seule boucle sans dépasser
                        tabt[j]=tabs[j];
                    }
                    if (j<cptp){
                        tabs[j]=tabp[j];
                    }
                }    
                //On change les compteur de premiers à deuxièmes, deuxièmes à troisièmes
                cptt=cpts;
                cpts=cptp;
                //On sauvegarde le dossard du nouveau premier
                tabp[0]=i;
                cptp=1;
                //On change les variables de scores
                third = second;
                second= first;
                first=pfscore[i];
            }
            //On teste si le compétiteur est deuxième à égalité
            else if(pfscore[i]==second){
                tabs[cpts]=i;
                cpts++;
                // En cas d'égalité on sauvegarde la position du deuxième ex aequo dans un tableau
            }
            //Sinon on se demande si il fait un nouveau deuxième
            else if (pfscore[i]<second && pfscore[i]>0){
                //On change les deuxièmes en troisième et l'on sauvegarde le nouveau deuxième
                for (int j=0;j<cpts;j++){
                    tabt[j]=tabs[j];
                }
                cptt=cpts;
                tabs[0]=i;
                cpts=1;
                third = second;
                second=pfscore[i];
            }
            //On teste si le compétiteur est troisième à égalité
            else if (pfscore[i]==third){
                tabt[cptt]=i;
                cptt++;
            }
            //Sinon on se demande si il fait un nouveau troisième
            else if(pfscore[i]<third && pfscore[i]>0){
                // Le cas échéant on sauvegarde son maillot et son temps
                tabt[0]=i;
                cptt=1;
                third = pfscore[i];
            }
        }
        //différents cas particuliers
        //premier cas particulier
        //3 premiers dont pas de deuxieme ou de troisieme
        if(cptp>=3){
            //initialisation de la variable pos qui va contenir le numéro des compétiteurs
            int pos=0;
            //incrémentation de la variable pos avec le numéro des joueurs grace a une boucle for qui parcours tout le tableau qui contient le numéro des jouers premier au classement
            System.out.println("La premiere place revient ex aequo aux joueurs n°:"); 
            for(int i=0; i<cptp;i++){
                pos=tabp[i]+1;
                System.out.print(pos+", ");
            }
            System.out.println("Avec comme temps ");
            //appelle d'un sous-programme qui permet de convertir la valeur saisie en minutes, secondes, ms
            conversion(first);
        }
        //deuxième cas particulier
        //1 premiers et plus de 2 deuxieme
        else if(cpts>2 && cptp==1){
            //affichage de la premier place pas besoins de boucle car un seul premier
            System.out.println("La premiere place revient au joueur n°: "+(tabp[0]+1)+ "avec un temps de : ");
            //appelle d'un sous-programme qui permet de convertir la valeur saisie en minutes, secondes, ms
            conversion(first);
            //initialisation de la variable pos qui va contenir le numéro des compétiteurs
            int pos=0;
            //incrémentation de la variable pos avec le numéro des joueurs grace a une boucle for qui parcours tout le tableau qui contient le numéro des jouers deuxieme au classement
            System.out.println("La deuxieme place revient ex aequo aux joueurs n°: ");
            for(int i=0; i<cpts;i++){
                pos=tabs[i]+1;
                System.out.print(pos+", ");
            }
            System.out.println("Avec comme temps ");
            //appelle d'un sous-programme qui permet de convertir la valeur saisie en minutes, secondes, ms
            conversion(second);
        }
        //troisième cas particulier
        //2 premiers et tous les autres joueurs éliminés
        else if (cptp==2 && cpts==0 && cptt==0){
            //initialisation de la variable pos qui va contenir le numéro des compétiteurs
            int pos=0;
            System.out.println("La premiere place revient ex aequo aux joueurs n°:");
            //incrémentation de la variable pos avec le numéro des joueurs grace a une boucle for qui parcours tout le tableau qui contient le numéro des deux premier au classement
            for(int i=0; i<cptp;i++){
                pos=tabp[i]+1;
                System.out.print(pos);
                //if pour ne pas mettre de virgule après l'affichage du dernier numéro
                if (i!=cptp-1){
                    System.out.print(", ");
                }
            }
            System.out.println(" Avec comme temps ");
            //appelle d'un sous-programme qui permet de convertir la valeur saisie en minutes, secondes, ms
            conversion(first);
        }
        //quatrième cas particulier
        //2 premiers et pleins de troisieme
        else if (cptp==2 && cptt!=0){
            //initialisation de la variable pos qui va contenir le numéro des compétiteurs
            int pos=0;
            System.out.println("La premiere place revient ex aequo aux joueurs n°:"); 
            //incrémentation de la variable pos avec le numéro des joueurs grace a une boucle for qui parcours tout le tableau qui contient le numéro des deux premier au classement
            for(int i=0; i<cptp;i++){
                pos=tabp[i]+1;
                System.out.print(pos);
                //if pour ne pas mettre de virgule après l'affichage du dernier numéro
                if (i!=cptp-1){
                    System.out.print(", ");
                }
            }
            System.out.println(" Avec comme temps ");
            //appelle d'un sous-programme qui permet de convertir la valeur saisie en minutes, secondes, ms
            conversion(first);

            //initialisation de la variable pos qui va contenir le numéro des compétiteurs à la troisieme place
            int pos2=0;
            System.out.println("La troisieme place revient aux joueurs n°: ");
            //incrémentation de la variable pos avec le numéro des joueurs grace a une boucle for qui parcours tout le tableau qui contient le numéro des troisieme au classement
            for(int i=0; i<cpts;i++){
                pos2=tabs[i]+1;
                System.out.print(pos2+", ");
            }
            System.out.println("Avec comme temps ");
            //appelle d'un sous-programme qui permet de convertir la valeur saisie en minutes, secondes, ms
            conversion(second);
        }
        //cinquième cas particulier
        //1 premiers 1 deuxieme et pleins de troisieme
        else if (cptp==1 && cpts==1 && cptt>1){
            //affichage de la premier place pas besoins de boucle car un seul premier
            System.out.println("La premiere place revient au joueur n°: "+(tabp[0]+1)+ "avec un temps de : " + pfscore[tabp[0]]);
            //affichage de la deuxieme place pas besoins de boucle car un seul deuxieme
            System.out.println("La deuxieme place revient au joueur n°: "+(tabs[0]+1)+ "avec un temps de : " + pfscore[tabs[0]]);
            //initialisation de la variable pos qui va contenir le numéro des compétiteurs à la troisieme place
            int pos3=0;
            System.out.println("La troisieme place revient aux joueurs n°: ");
            //incrémentation de la variable pos avec le numéro des joueurs grace a une boucle for qui parcours tout le tableau qui contient le numéro des troisieme au classement
            for(int i=0; i<cptt;i++){
                pos3=tabt[i]+1;
                System.out.print(pos3+", ");

            }
            System.out.println("Avec comme temps ");
            //appelle d'un sous-programme qui permet de convertir la valeur saisie en minutes, secondes, ms
            conversion(third);
        }
        //sixième cas particulier
        //1 premier et tous les autres éliminés
        else if (cptp==1 && cpts==0 && cptt==0){
            //affichage de la premier place pas besoins de boucle car un seul premier
            System.out.println("La premiere place revient au joueur n°: "+(tabp[0]+1)+ "avec un temps de : ");
            //appelle d'un sous-programme qui permet de convertir la valeur saisie en minutes, secondes, ms
            conversion(pfscore[tabp[0]]);
            System.out.println("Tout les autres compétiteurs onr été éliminés");

        }
        //septième cas simple
        //1 premier 1 deuxieme 1 troisieme
        else if (cptp==1 && cpts==1 && cptt==1){
            //affichage de la premier place pas besoins de boucle car un seul premier
            System.out.println("La premiere place revient au joueur n°: "+(tabp[0]+1)+ " avec un temps de : ");
            conversion(pfscore[tabp[0]]);
            //affichage de la deuxieme place pas besoins de boucle car un seul deuxieme
            System.out.println("La deuxieme place revient au joueur n°: "+(tabs[0]+1)+ " avec un temps de : ");
            conversion(pfscore[tabs[0]]);
            //affichage de la troisieme place pas besoins de boucle car un seul troisieme
            System.out.println("La troisieme place revient au joueur n°: "+(tabt[0]+1)+ " avec un temps de : ");
            //appelle d'un sous-programme qui change les ms en s
            conversion(pfscore[tabt[0]]);
        }
        //huitième cas particulier
        //Tous les compétiteurs ont été éliminés
        else if (cptp==0 && cpts==0 && cptt==0){
            System.out.println("Tous les joueurs ont été eliminés");
            
        }
    }
}