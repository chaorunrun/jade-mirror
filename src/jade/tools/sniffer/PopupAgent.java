/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop multi-agent systems in compliance with the FIPA specifications.
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

package jade.tools.sniffer;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
   /**
   Javadoc documentation for the file
   @author Francisco Regi, Andrea Soracchi - Universita` di Parma
   <Br>
   <a href="mailto:a_soracchi@libero.it"> Andrea Soracchi(e-mail) </a>
   @version $Date$ $Revision$
 */

 /**
  * This is the PopupMenu that will appear if the user click
  * on the canvas of agents.
  * @see jade.tools.sniffer.PopupMessage
  */

public class PopupAgent extends JPopupMenu {

 private JMenuItem tmp;
 private  PopSniffAgent popSniffAg;
 private  PopNoSniffAgent popNoSniffAg;
 private  PopShowAgent popShowAg;

 protected Agent agent;

 public PopupAgent(Sniffer mySniffer,MMCanvas canvAgent) {
  super();
   popSniffAg=new PopSniffAgent(this,mySniffer,canvAgent);
   popNoSniffAg=new PopNoSniffAgent(this,mySniffer,canvAgent);
   popShowAg=new PopShowAgent(this,mySniffer,canvAgent);

   tmp=add(popSniffAg);
   tmp.setIcon(null);

   tmp=add(popNoSniffAg);
   tmp.setIcon(null);

   tmp=add(popShowAg);
   tmp.setIcon(null);
 }

 protected void setAgent(Agent ag) {
  agent=ag;
 }

} // End of class PopupAgent