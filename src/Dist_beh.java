
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
    int ile_bledow =0;
    List lista_bledow = new LinkedList();
    int ile_wyslanych = 0;
    int numerbledu=0;
    Boolean blad =false;

    public void action() {
        ACLMessage wiadomosc = new ACLMessage();

        wiadomosc = myAgent.receive();

        if(wiadomosc != null)
        {
            if(wiadomosc.getPerformative() == ACLMessage.INFORM)
            {
                ACLMessage Answ = wiadomosc.createReply();
                if(ile_wyslanych >(sizeX*sizeX)-1 && ile_bledow == 0 && robione.size()==sizeX*sizeX )
                {
                    Answ.setPerformative(ACLMessage.CANCEL);

                }
                else if (robione.size()<sizeX*sizeX ) {
                    for (int i = 0; i < elementy_do_zrobienia.size(); i++) //znalezienie elementu do wykonania
                    {
                        elem = elementy_do_zrobienia.get(i).toString();
                        if (!robione.contains(elem)) {
                            robione.add(elem);
                            break;
                        }
                    }
                    ij = elem.split(" ");

                    Answ.setContent(Crt_str(Integer.parseInt(ij[0]),Integer.parseInt(ij[1]),tab));
                    System.out.println("WYSYLAM DO " + wiadomosc.getSender());
                    Answ.setPerformative(ACLMessage.REQUEST);
                    ile_wyslanych++;
                }else if(ile_bledow >0&& (ile_poprawnych + ile_bledow)>(sizeX*sizeX)-1)
                {
                    System.out.println("OBSLUGA BLEDOW");
                    blad =true;
                    String[] parts = lista_bledow.get(numerbledu).toString().split(" ");
                    Answ.setPerformative(ACLMessage.REQUEST);
                    Answ.setContent(Crt_str(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),tab));
                    numerbledu++;
                    ile_bledow--;
                }

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
            else if (wiadomosc.getPerformative() == ACLMessage.FAILURE)
            {
                System.out.println("AGENT ZGLOSIL BLAD");
                ile_bledow++;
                lista_bledow.add(wiadomosc.getContent());
                System.out.println(wiadomosc.getContent());
            }

        }
        else
        {
            block();
        }

    }
    public String Crt_str(Integer i ,Integer j, int tab[][])
    {
        SB.delete(0,SB.length());
        SB.append(tab[0].length);//rozmiar
        SB.append(' ');

        SB.append(i); //aktualna pozycja
        SB.append(' ');
        SB.append(j);
        SB.append(' ');

        //zapis do stringa
        for (int k = 0; k < tab[0].length; k++) {
            SB.append(tab[i][k]);
            SB.append(' ');
        }
        for (int m = 0; m < tab.length; m++) {
            SB.append(tab[m][j]);
            SB.append(' ');
        }
        return SB.toString();
    }


}