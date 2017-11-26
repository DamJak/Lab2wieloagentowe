
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
    int sizeX = 4;

    public void action() {
        ACLMessage wiadomosc = new ACLMessage();

        wiadomosc = myAgent.receive();

        if(wiadomosc != null)
        {
            if(wiadomosc.getPerformative() == ACLMessage.INFORM)
            {
                for (int i = 0; i < sizeX; i++) {
                    for (int j = 0; j < sizeX; j++) {
                        System.out.print(tab[i][j] + " ");
                    }
                    System.out.println();
                }

                ACLMessage Answ = wiadomosc.createReply();
                Answ.setContent(tab.toString());
                System.out.println("WYSYLAM DO "+ wiadomosc.getSender());
                Answ.setPerformative(ACLMessage.REQUEST);
                myAgent.send(Answ);
            }

        }
        else
        {
            block();
        }

    }


}