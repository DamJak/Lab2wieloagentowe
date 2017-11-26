
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.LinkedList;
import java.util.List;

public class Dist_beh extends CyclicBehaviour {

    public Dist_beh(int[][] tabl,List elem)
    {
        tab=tabl;
        elementy_do_zrobienia =elem;

    }

    int tab[][];
    List elementy_do_zrobienia = new LinkedList();
    String elem;
    List robione = new LinkedList();
    String str = "";
    StringBuffer SB = new StringBuffer(str);
    String ij[];
    int sizeX = 4;
    int ile_poprawnych =0;
    int wynik[][] = new int[sizeX][sizeX];

    public void action() {
        ACLMessage wiadomosc = new ACLMessage();

        wiadomosc = myAgent.receive();

        if(wiadomosc != null)
        {
            if(wiadomosc.getPerformative() == ACLMessage.INFORM)
            {

                for(int i = 0; i< elementy_do_zrobienia.size(); i++) //znalezienie elementu do wykonania
                {
                    elem = elementy_do_zrobienia.get(i).toString();
                    if(!robione.contains(elem))
                    {
                        robione.add(elem);
                        break;
                    }
                }
                ij = elem.split(" ");
                SB.append(' ');
                SB.append(tab[0].length);//rozmiar
                SB.append(' ');

                SB.append(ij[0]); //aktualna pozycja
                SB.append(' ');
                SB.append(ij[1]);
                SB.append(' ');

                //zapis do stringa
                for (int k = 0; k < tab[0].length; k++) {
                    SB.append(tab[Integer.parseInt(ij[0])][k]);
                    SB.append(' ');
                }
                for (int m = 0; m < tab.length; m++) {
                    SB.append(tab[m][Integer.parseInt(ij[1])]);
                    SB.append(' ');
                }


                ACLMessage Answ = wiadomosc.createReply();
                Answ.setContent(SB.toString());
                System.out.println("WYSYLAM DO "+ wiadomosc.getSender());
                Answ.setPerformative(ACLMessage.REQUEST);
                myAgent.send(Answ);
            }
            else if(wiadomosc.getPerformative() == ACLMessage.AGREE)
            {
                String odb = wiadomosc.getContent();
                String[] parts = odb.split(" ");
                ile_poprawnych++;
                wynik[Integer.parseInt(parts[0])][Integer.parseInt(parts[1])] = Integer.parseInt(parts[2]);
                for (int i = 0; i < sizeX; i++) {
                    for (int j = 0; j < sizeX; j++) {
                        System.out.print(wynik[i][j] + " ");
                    }
                    System.out.println();
                }

                elementy_do_zrobienia.remove(Integer.parseInt(parts[0]) + " " + Integer.parseInt(parts[1]));
                System.out.println(elementy_do_zrobienia);
                System.out.println("Agent: " + wiadomosc.getSender().getName() + " Podał wynik: " + parts[2]);

            }

        }
        else
        {
            block();
        }

    }


}