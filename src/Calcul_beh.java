import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Random;


public class Calcul_beh extends CyclicBehaviour {

    Integer wynik = 0;
    Integer wynik1 = 0;
    String str="";
    StringBuffer SB = new StringBuffer(str);

    public void action()
    {

        ACLMessage Mess_Rcv = new ACLMessage();
        if(Fnd_Serv(myAgent)!=null)
        {
            Mess_Rcv = myAgent.receive();

            if(Mess_Rcv !=null)
            {

                String odb = Mess_Rcv.getContent();
                ACLMessage Answ = Mess_Rcv.createReply();

                if(Mess_Rcv.getPerformative() == ACLMessage.REQUEST)
                {
                    String[] parts = odb.split(" ");
                    if(Integer.parseInt(parts[0]) == 0)
                    {
                        if(Rand(0,100)<15)
                        {
                            SB.delete(0, SB.length());
                            Answ.setPerformative(ACLMessage.FAILURE);
                            SB.append(0);
                            SB.append(' ');
                            SB.append(parts[2]);
                            SB.append(' ');
                            SB.append(parts[3]);
                            Answ.setContent(SB.toString());
                            myAgent.send(Answ);
                            block(2000);
                        }
                        else
                        {
                            wynik = oblicz(parts);
                            if (myAgent.getClass().toString().equals("class Malicious"))
                                wynik = 123456;
                            Answ.setContent(Crt_str(parts[2],parts[3],wynik,0));
                            wynik=0;
                            block(Rand(500,1500));
                            Answ.setPerformative(ACLMessage.AGREE);
                            myAgent.send(Answ);
                        }
                    }
                    else if(Integer.parseInt(parts[0]) == 1)
                    {
                        SB.delete(0, SB.length());
                        wynik1 = oblicz(parts);
                        Answ.setContent(Crt_str(parts[2],parts[3],wynik1,1));
                        wynik1=0;
                        Answ.setPerformative(ACLMessage.AGREE);
                        myAgent.send(Answ);
                    }
                    else if(Integer.parseInt(parts[0]) == 2)
                    {
                        System.out.println("ODEBRALEM DO SEDZIOWANA" +  Mess_Rcv.getContent());
                    }

                }
                else if(Mess_Rcv.getPerformative() == ACLMessage.CANCEL)
                {
                    System.out.println("Nie mam co robic " + myAgent.getName().toString());
                    myAgent.doDelete();

                }

            }
            else {
                if(Fnd_Serv(myAgent)!=null)
                {
                    ACLMessage Mess_Snd = new ACLMessage();
                    AID server = Fnd_Serv(myAgent)[0].getName();
                    Mess_Snd.setPerformative(ACLMessage.INFORM);
                    Mess_Snd.addReceiver(server);
                    myAgent.send(Mess_Snd);
                }
                block();
            }
        }
    }

    public DFAgentDescription[] Fnd_Serv(Agent myAgent)
    {
        DFAgentDescription[] result = null;
        DFAgentDescription DFAD = new DFAgentDescription();
        ServiceDescription SD = new ServiceDescription();
        SD.setType("Sender");
        SD.setName("Token");
        DFAD.addServices(SD);
        try {
            result = DFService.search(myAgent, DFAD);
        } catch (Exception ex) {
        }
        return result;
    }
    public static Integer Rand(Integer a, Integer b){
        Random r = new Random();
        return r.nextInt(b-a+1)+a;

    }
    public int oblicz(String[] parts)
    {
        wynik=0;
        for (int i = 4; i < (Integer.parseInt(parts[1]) + 4); i++) {
            wynik += Integer.parseInt(parts[i]) * Integer.parseInt(parts[i + Integer.parseInt(parts[1])]);
        }
        return wynik;
    }
    public String Crt_str(String i ,String j, int wynik,int typ) {
        SB.delete(0, SB.length());
        SB.append(typ); //aktualna pozycja
        SB.append(' ');
        SB.append(i); //aktualna pozycja
        SB.append(' ');
        SB.append(j);
        SB.append(' ');
        SB.append(wynik);
        return SB.toString();
    }

}