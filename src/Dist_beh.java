import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

public class Dist_beh extends CyclicBehaviour {

    public Dist_beh(int[][] tabl,List elem)
    {
        tab=tabl;
        elementy_do_zrobienia =elem;

    }
    int i = 0;
    int j = 0;
    int numerbledu=0;
    Integer sizeX = 8;
    int tab[][];
    List robione = new LinkedList();
    int wynik[][] = new int[sizeX][sizeX];

    String str = "";
    StringBuffer SB = new StringBuffer(str);
    int ile_wyslanych = 0;
    int ile_bledow =0;
    int ile_poprawnych =0;
    List lista_bledow = new LinkedList();
    List elementy_do_zrobienia = new LinkedList();
    String elem;
    String ij[];
    int czy_test = 0;
    int wynik_do_testu=0;
    int wynik_po_tescie=0;
    Boolean blad =false;
    List<AID> lista_klientow = new LinkedList();
    List<AID> lista_zbanowanych = new LinkedList();
    Boolean flag=false;
    AID klient_testowany = new AID();
    AID klient_testujacy = new AID();
    AID klient_sedziujacy = new AID();

    public void action() {
        ACLMessage wiadomosc = new ACLMessage();

        wiadomosc = myAgent.receive();

        if(wiadomosc != null)
        {
            if(!lista_klientow.contains(wiadomosc.getSender()))
            {
                lista_klientow.add(wiadomosc.getSender());
            }
            if(wiadomosc.getPerformative() == ACLMessage.INFORM)
            {
                ACLMessage Answ = wiadomosc.createReply();
                SB.delete(0, SB.length());
                if(ile_wyslanych >(sizeX*sizeX)-1 && ile_bledow == 0 && robione.size()==sizeX*sizeX /*&& elementy_do_zrobienia.size()==0*/)
                {
                    Answ.setPerformative(ACLMessage.CANCEL);

                }
                else if (robione.size()<sizeX*sizeX && !lista_zbanowanych.contains(wiadomosc.getSender()))
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
                    System.out.println("WYSYLAM DO "+ wiadomosc.getSender());
                    System.out.println(elementy_do_zrobienia);
                    ij = elem.split(" ");
                    elem="";
                    Answ.setPerformative(ACLMessage.REQUEST);
                    Answ.setContent(Crt_str(Integer.parseInt(ij[0]),Integer.parseInt(ij[1]),tab,0));
                    ile_wyslanych++;
                }else if(ile_bledow >0&& (ile_poprawnych + ile_bledow)>(sizeX*sizeX)-1 /*!lista_bledow.isEmpty()&& robione.size()==sizeX*sizeX*/)
                {
                    System.out.println("OBSLUGA BLEDOW");
                    blad =true;
                    String[] parts = lista_bledow.get(numerbledu).toString().split(" ");
                    Answ.setPerformative(ACLMessage.REQUEST);
                    Answ.setContent(Crt_str(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),tab,0));
                    numerbledu++;
                    ile_bledow--;

                }
                myAgent.send(Answ);
            }
            else if(wiadomosc.getPerformative() == ACLMessage.AGREE)
            {
                String odb = wiadomosc.getContent();
                String[] parts = odb.split(" ");
                if(Integer.parseInt(parts[0])==0)
                {
                    if(czy_test == 3 && blad == false && flag == false)
                    {
                        System.out.println("Wynik bedzie testowany");
                        ACLMessage wiadomosc_testowana = new ACLMessage();
                        flag=true;
                        wynik_do_testu=Integer.parseInt(parts[3]);
                        wiadomosc_testowana.setPerformative(ACLMessage.REQUEST);
                        wiadomosc_testowana.setContent(Crt_str(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),tab,1));
                        klient_testowany = wiadomosc.getSender();
                        for (int i = 0; i< lista_klientow.size(); i++)
                        {
                            if (!klient_testowany.equals(lista_klientow.get(i)));
                            klient_testujacy = lista_klientow.get(i);
                            wiadomosc_testowana.addReceiver(lista_klientow.get(i));
                            break;
                        }
                        myAgent.send(wiadomosc_testowana);
                    }
                    else {

                        ile_poprawnych++;
                        wynik[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])] = Integer.parseInt(parts[3]);
                        wypisz(wynik,sizeX);

                        elementy_do_zrobienia.remove(Integer.parseInt(parts[1]) + " " + Integer.parseInt(parts[2]));
                        System.out.println(elementy_do_zrobienia);
                        System.out.println("Agent: " + wiadomosc.getSender().getName() + " Podał wynik: " + parts[3]);
                    }

                    if (czy_test == 3)
                        czy_test = 0;
                    czy_test++;
                }
                else if(Integer.parseInt(parts[0])==1)
                {
                    wynik_po_tescie = Integer.parseInt(parts[3]);

                    if(Integer.parseInt(parts[3])==wynik_do_testu)
                    {
                        System.out.println("PO PRZEPROWADZONYCH TESTACH WYNIK ["+ Integer.parseInt(parts[1])+
                                "]["+Integer.parseInt(parts[2])+"] SIE ZGADZA");
                        wynik[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])] = Integer.parseInt(parts[3]);
                        wypisz(wynik,sizeX);
                        ile_poprawnych++;
                        elementy_do_zrobienia.remove(Integer.parseInt(parts[1]) + " " + Integer.parseInt(parts[2]));
                        System.out.println(elementy_do_zrobienia);
                        flag=false;
                    }
                    else {
                        ACLMessage wiadomosc_sedziowana = new ACLMessage();
                        for (int i = 0; i< lista_klientow.size(); i++)
                        {
                            if (!klient_testowany.equals(lista_klientow.get(i)) && !klient_testujacy.equals(lista_klientow.get(i)));
                            klient_sedziujacy = lista_klientow.get(i);
                            wiadomosc_sedziowana.addReceiver(lista_klientow.get(i));
                            break;
                        }
                        wiadomosc_sedziowana.setPerformative(ACLMessage.REQUEST);
                        wiadomosc_sedziowana.setContent(Crt_str(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),tab,2));
                        System.out.println("Wysylam do sedziego");
                        myAgent.send(wiadomosc_sedziowana);
                    }

                } else if(Integer.parseInt(parts[0])==2)
                {
                    if (Integer.parseInt(parts[3]) == wynik_po_tescie) {

                        System.out.println("Po sedziowaniu Wynik2 == wynik3");
                        wynik[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])] = wynik_po_tescie;
                        wypisz(wynik,sizeX);

                        ile_poprawnych++;
                        elementy_do_zrobienia.remove(Integer.parseInt(parts[1]) + " " + Integer.parseInt(parts[2]));
                        System.out.println(elementy_do_zrobienia);
                        if (!lista_zbanowanych.contains(klient_testowany))
                            lista_zbanowanych.add(klient_testowany);
                        System.out.println("ZBANOWANI" + lista_zbanowanych);
                        flag = false;

                    } else if (wynik_do_testu == Integer.parseInt(parts[3])) {

                        System.out.println("Po sedziowaniu Wynik1 == wynik3");
                        wynik[Integer.parseInt(parts[1])][Integer.parseInt(parts[2])] = wynik_do_testu;
                        wypisz(wynik,sizeX);
                        ile_poprawnych++;
                        elementy_do_zrobienia.remove(Integer.parseInt(parts[1]) + " " + Integer.parseInt(parts[2]));
                        System.out.println(elementy_do_zrobienia);
                        if (!lista_zbanowanych.contains(klient_testujacy))
                            lista_zbanowanych.add(klient_testujacy);
                        System.out.println("ZBANOWANI" + lista_zbanowanych);
                        flag = false;
                    }
                }
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
    public String Crt_str(Integer i ,Integer j, int tab[][],int typ)
    {
        SB.delete(0,SB.length());
        SB.append(typ);//rozmiar
        SB.append(' ');
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
    public static Integer Rand(Integer a, Integer b){
        Random r = new Random();
        return r.nextInt(b-a+1)+a;
    }
    public void wypisz(int[][] wynik,int sizeX)
    {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeX; j++) {
                System.out.print(wynik[i][j] + " ");
            }
            System.out.println();
        }
    }

}