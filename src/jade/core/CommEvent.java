/*
 * $Id$
 */

package jade.core;

import java.awt.AWTEvent;
import jade.lang.acl.*;

/***************************************************************

  Name: CommEvent

  Responsibilities and Collaborations:

  + Objectifies the reception event, embedding the received ACL
    message.
    (ACLMessage)

  + Holds a list of recipients agent to allow trasparent message
    multicasting
    (AgentGroup)

******************************************************************/
public class CommEvent extends AWTEvent {

  private ACLMessage  message;
  private AgentGroup recipients;

  public CommEvent(CommBroadcaster source, ACLMessage message) {
    super(source, -1);
    this.message = message;
    recipients = null;
  }

  public CommEvent(CommBroadcaster source, ACLMessage message, AgentGroup group) {
    this(source, message);
    recipients = group;
  }

  public ACLMessage getMessage() {
    return message;
  }

  public boolean isMulticast() {
    return recipients != null;
  }

  public AgentGroup getRecipients() {
    return recipients;
  }

}
