/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop 
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*****************************************************************/

package examples.inprocess;

import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;

import jade.wrapper.*;

/**
   This class is an example of how you can embed JADE runtime
   environment within your applications.

   @author Giovanni Rimassa - Universita` di Parma

 */
public class InProcessTest {


  public static void main(String args[]) {

    try {

      // Get a hold on JADE runtime
      Runtime rt = Runtime.instance();

      // Exit the JVM when there are no more containers around
      rt.setCloseVM(true);

      // Check whether a '-container' flag was given
      if(args.length > 0) {
	if(args[0].equalsIgnoreCase("-container")) {
	  // Create a default profile
	  Profile p = new ProfileImpl();
	  //p.setParameter(Profile.MAIN, "false");

	  // Create a new non-main container, connecting to the default
	  // main container (i.e. on this host, port 1099)
	  AgentContainer ac = rt.createAgentContainer(p);

	  // Create a new agent, a DummyAgent
	  Agent dummy = ac.createAgent("inProcess", "jade.tools.DummyAgent.DummyAgent", new Object[0]);

	  // Fire up the agent
	  System.out.println("Starting up a DummyAgent...");
	  dummy.start();

	  // Wait for 10 seconds
	  Thread.sleep(10000);

	  // Kill the DummyAgent
	  System.out.println("Killing DummyAgent...");
	  dummy.delete();

	  // Create another peripheral container within the same JVM
	  // NB. Two containers CAN'T share the same Profile object!!! -->
	  // Create a new one.
	  p = new ProfileImpl();
	  //p.putProperty(Profile.MAIN, "false");
	  AgentContainer another = rt.createAgentContainer(p);

	  // Launch the Mobile Agent example
	  // and pass it 2 arguments: a String and an object reference
	  Object[] arguments = new Object[2];
	  arguments[0] = "Hello World!";
	  arguments[1]=dummy;
	  Agent mobile = another.createAgent("Johnny", "examples.mobile.MobileAgent", arguments);
	  mobile.start();

	  return;
	}
      }

      // Launch a complete platform on the 8888 port
      // create a default Profile 
      Profile pMain = new ProfileImpl(null, 8888, null);

      System.out.println("Launching a whole in-process platform..."+pMain);
      MainContainer mc = rt.createMainContainer(pMain);

      // set now the default Profile to start a container
      ProfileImpl pContainer = new ProfileImpl(null, 8888, null);
      System.out.println("Launching the agent container ..."+pContainer);
      AgentContainer cont = rt.createAgentContainer(pContainer);

      System.out.println("Launching the rma agent on the main container ...");
      Agent rma = mc.createAgent("rma", "jade.tools.rma.rma", new Object[0]);
      rma.start();

    }
    catch(Exception e) {
      e.printStackTrace();
    }

  }

}
