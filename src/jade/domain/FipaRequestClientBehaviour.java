/*
  $Log$
  Revision 1.4  1999/02/15 11:45:12  rimassa
  Changed removeBehaviour() calls to removeSubBehaviour() calls.

  Revision 1.3  1999/02/14 23:20:56  rimassa
  Changed addBehaviour() calls to addSubBehaviour() calls where
  appropriate.

  Revision 1.2  1999/02/03 10:38:42  rimassa
  Made class constructor public, since this class is to be used from
  application code.

  Revision 1.1  1998/12/01 23:44:26  rimassa
  A generic Behaviour subclass to perform 'fipa-request' interaction as client.

*/

package jade.domain;

import jade.core.*;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

// This Behaviour plays the initiator role in 'fipa-request' protocol:
// this is an abstract class, defining an abstract method for each
// message type expected from a 'fipa-request' interaction.

public abstract class FipaRequestClientBehaviour extends SequentialBehaviour {

  private ComplexBehaviour firstReceive;
  private ComplexBehaviour secondReceive;

  public FipaRequestClientBehaviour(Agent client, ACLMessage request, MessageTemplate template) {
    super(client);

    // Set type and protocol for request
    request.setType("request");
    request.setProtocol("fipa-request");

    // Create all necessary MessageTemplate objects
    MessageTemplate FipaRequestTemplate = MessageTemplate.and(
      MessageTemplate.MatchProtocol("fipa-request"),
      MessageTemplate.MatchSource(request.getDest()));
    String convID = request.getConversationId();
    String replyWith = request.getReplyWith();
    if(convID != null)
      FipaRequestTemplate = MessageTemplate.and(
        MessageTemplate.MatchConversationId(convID),
	FipaRequestTemplate);
    if(replyWith != null)
      FipaRequestTemplate = MessageTemplate.and(
        MessageTemplate.MatchReplyTo(replyWith),
	FipaRequestTemplate);
    FipaRequestTemplate = MessageTemplate.and(template, FipaRequestTemplate);

    final MessageTemplate NotUnderstoodTemplate = MessageTemplate.and(
      FipaRequestTemplate, MessageTemplate.MatchType("not-understood"));

    final MessageTemplate RefuseTemplate = MessageTemplate.and(
      FipaRequestTemplate, MessageTemplate.MatchType("refuse"));

    final MessageTemplate AgreeTemplate = MessageTemplate.and(
      FipaRequestTemplate, MessageTemplate.MatchType("agree"));

    final MessageTemplate FailureTemplate = MessageTemplate.and(
      FipaRequestTemplate, MessageTemplate.MatchType("failure"));

    final MessageTemplate InformTemplate = MessageTemplate.and(
      FipaRequestTemplate, MessageTemplate.MatchType("inform"));


    firstReceive = NonDeterministicBehaviour.createWhenAny(client);
    firstReceive.addSubBehaviour(new SimpleBehaviour(client) {

      private boolean finished = false;
      public void action() {
	// Receive 'not-understood'
	ACLMessage msg = myAgent.receive(NotUnderstoodTemplate);
	if(msg != null) {
	  handleNotUnderstood(msg);
	  finished = true;
	}
	else
	  block();
      }

      public boolean done() {
	return finished;
      }

      public void reset() {
	finished = false;
      }

    });
    firstReceive.addSubBehaviour(new SimpleBehaviour(client) {

      private boolean finished = false;
      public void action() {
	// Receive 'refuse'
	ACLMessage msg = myAgent.receive(RefuseTemplate);
	if(msg != null) {
	  handleRefuse(msg);
	  finished = true;
	}
	else 
	  block();
      }

      public boolean done() {
	return finished;
      }

      public void reset() {
	finished = false;
      }

    });
    firstReceive.addSubBehaviour(new SimpleBehaviour(client) {

      private boolean finished = false;
      public void action() {
	// Receive 'agree'
	ACLMessage msg = myAgent.receive(AgreeTemplate);
	if(msg != null) {
	  handleAgree(msg);
	  // Add a second NonDeterministicBehaviour to the main behaviour
	  FipaRequestClientBehaviour.this.addSubBehaviour(secondReceive);
	  finished = true;
	}
	else
	  block();
      }

      public boolean done() {
	return finished;
      }

      public void reset() {
	if(finished)
	  FipaRequestClientBehaviour.this.removeSubBehaviour(secondReceive);
	finished = false;
      }

    });

    secondReceive = NonDeterministicBehaviour.createWhenAny(client);
    secondReceive.addSubBehaviour(new SimpleBehaviour(client) {
      private boolean finished = false;
      public void action() {
	// Receive 'failure'
	ACLMessage msg = myAgent.receive(FailureTemplate);
	if(msg != null) {
	  handleFailure(msg);
	  finished = true;
	}
	else 
	  block();
      }

      public boolean done() {
	return finished;
      }

      public void reset() {
	finished = false;
      }

    });
    secondReceive.addSubBehaviour(new SimpleBehaviour(client) {
      private boolean finished = false;
      public void action() {
	// Receive 'inform'
	ACLMessage msg = myAgent.receive(InformTemplate);
	if(msg != null) {
	  handleInform(msg);
	  finished = true;
	}
	else
	  block();
      }

      public boolean done() {
	return finished;
      }

      public void reset() {
	finished = false;
      }

    });


    addSubBehaviour(new SenderBehaviour(client, request));
    addSubBehaviour(firstReceive);      

  }

  protected abstract void handleNotUnderstood(ACLMessage reply);
  protected abstract void handleRefuse(ACLMessage reply);
  protected abstract void handleAgree(ACLMessage reply);
  protected abstract void handleFailure(ACLMessage reply);
  protected abstract void handleInform(ACLMessage reply);


}
