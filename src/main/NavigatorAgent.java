package main;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NavigatorAgent extends Agent {

    @Override
    protected void setup() {
        System.out.println("Hello! The navigator agent " + getAID().getName() + " is ready.");

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(WumpusWorldAgent.Constants.NAVIGATOR_AGENT_TYPE);
        sd.setName(WumpusWorldAgent.Constants.NAVIGATOR_SERVICE_DESCRIPTION);
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new LocationRequestsServer());
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("The navigator agent " + getAID().getName() + " terminating.");
    }

    private class LocationRequestsServer extends CyclicBehaviour {

        int time = 0;

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                if (parseSpeleologistMessageRequest(msg.getContent())){
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.REQUEST);
                    reply.setContent(WumpusWorldAgent.Constants.INFORMATION_PROPOSAL_NAVIGATOR);
                    System.out.println("NavigatorAgent: " + WumpusWorldAgent.Constants.INFORMATION_PROPOSAL_NAVIGATOR);
                    myAgent.send(reply);
                } else if (parseSpeleologistMessageProposal(msg.getContent()))
                {
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.PROPOSE);
                    String advice = getAdvice(msg.getContent());
                    reply.setContent(advice);
                    System.out.println("NavigatorAgent: " + advice);
                    myAgent.send(reply);

                } else
                    System.out.println("NavigatorAgent: Wrong message!");
            } else {
                block();
            }
        }

        private boolean parseSpeleologistMessageRequest(String instruction) {
            String regex = "\\bHelp\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(instruction);
            if (matcher.find()) {
                String res = matcher.group();
                return res.length() > 0;
            }
            return false;
        }

        private boolean parseSpeleologistMessageProposal(String instruction) {
            String regex = "\\bPosition\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(instruction);
            if (matcher.find()) {
                String res = matcher.group();
                return res.length() > 0;
            }
            return false;
        }

        private String getAdvice(String content){
            boolean stench = false;
            boolean breeze = false;
            boolean glitter = false;
            boolean scream = false;
            String advicedAction = "";

            for(String word : PerceptKeyWords.stench) {
				if(content.contains(word)) {
					stench = true;
					break;
				}
			}
			for(String word : PerceptKeyWords.breeze) {
				if(content.contains(word)) {
					breeze = true;
					break;
				}
			}
			for(String word : PerceptKeyWords.glitter) {
				if(content.contains(word)) {
					glitter = true;
					break;
				}
			}
			for(String word : PerceptKeyWords.scream) {
				if(content.contains(word)) {
					scream = true;
					break;
				}
			}

            switch (time){
                case 0: advicedAction = WumpusWorldAgent.Constants.MESSAGE_RIGHT; time++; break;
                case 1: advicedAction = WumpusWorldAgent.Constants.MESSAGE_FORWARD; time++; break;
                case 2: advicedAction = WumpusWorldAgent.Constants.MESSAGE_LEFT; time++; break;
                case 3: advicedAction = WumpusWorldAgent.Constants.MESSAGE_FORWARD; time++; break;
                case 4: advicedAction = WumpusWorldAgent.Constants.MESSAGE_FORWARD; time++; break;
                case 5: advicedAction = WumpusWorldAgent.Constants.MESSAGE_GRAB; time++; break;
                case 6: advicedAction = WumpusWorldAgent.Constants.MESSAGE_LEFT; time++; break;
                case 7: advicedAction = WumpusWorldAgent.Constants.MESSAGE_SHOOT; time++; break;
                case 8: advicedAction = WumpusWorldAgent.Constants.MESSAGE_FORWARD; time++; break;
                case 9: advicedAction = WumpusWorldAgent.Constants.MESSAGE_LEFT; time++; break;
                case 10: advicedAction = WumpusWorldAgent.Constants.MESSAGE_FORWARD; time++; break;
                case 11: advicedAction = WumpusWorldAgent.Constants.MESSAGE_FORWARD; time++; break;
                case 12: advicedAction = WumpusWorldAgent.Constants.MESSAGE_CLIMB; time++; break;
            }

            Random random_generator = new Random();
            return PerceptKeyWords.action_proposals.get(random_generator.nextInt(PerceptKeyWords.action_proposals.size())) + advicedAction;
        }

        public static final class PerceptKeyWords {
    		public static List<String> breeze = List.of("breez");
    		public static List<String> stench = List.of("stench", "stinky", "smell");
    		public static List<String> glitter = List.of("glitter", "shiny");
    		public static List<String> scream = List.of("scream", "hear");
    		public static List<String> action_proposals = List.of("It seems to me you should ", "It would be a good idea to ", "I think you can ");
    	}	    	
    }
}