import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;



public class Calcul_beh extends CyclicBehaviour {

    public void action()
    {

        ACLMessage Mess_Rcv = new ACLMessage();
        if(Fnd_Serv(myAgent)!=null)
        {
            Mess_Rcv = myAgent.receive();

            if(Mess_Rcv !=null)
            {
                if(Mess_Rcv.getPerformative() == ACLMessage.REQUEST)
                {
                    System.out.println("ODEBRALEM ODPOWIEDDZ");
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

}