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
                    if(Rand(0,100)<15)
                    {
                        SB.delete(0, SB.length());
                        Answ.setPerformative(ACLMessage.FAILURE);
                        SB.append(0);
                        SB.append(' ');
                        SB.append(parts[1]);
                        SB.append(' ');
                        SB.append(parts[2]);
                        Answ.setContent(SB.toString());
                        myAgent.send(Answ);
                        block(2000);
                    }
                    else
                    {
                        SB.delete(0, SB.length());

                        wynik = 0;
                        for (int i = 3; i < (Integer.parseInt(parts[0]) + 3); i++) {
                            wynik += Integer.parseInt(parts[i]) * Integer.parseInt(parts[i + Integer.parseInt(parts[0])]);
                        }
                        SB.append(Integer.parseInt(parts[0])); //aktualna pozycja
                        SB.append(' ');
                        SB.append(Integer.parseInt(parts[1]));
                        SB.append(' ');
                        SB.append(wynik);

                        wynik = 0;
                        block(Rand(500, 1500));
                        Answ.setPerformative(ACLMessage.AGREE);
                        myAgent.send(Answ);
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

}