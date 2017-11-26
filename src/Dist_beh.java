
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
                System.out.println("MAM KOMUNIKAT");
            }
        }
        else
        {
            block();
        }

    }


}