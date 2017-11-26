
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Dist_beh extends CyclicBehaviour {

    public void action() {
        ACLMessage wiadomosc = new ACLMessage();

        wiadomosc = myAgent.receive();

        if(wiadomosc != null)
        {
            if(wiadomosc.getPerformative() == ACLMessage.INFORM)
            {
                ACLMessage Answ = wiadomosc.createReply();
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