/**
 * ***************************************************************
 * JADE - Java Agent DEvelopment Framework is a framework to develop
 * multi-agent systems in compliance with the FIPA specifications.
 * Copyright (C) 2000 CSELT S.p.A.
 * 
 * GNU Lesser General Public License
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */


package jade.core;

import jade.util.leap.Properties;
import jade.util.leap.List;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.BasicProperties;
import java.io.IOException;
import java.net.*;
import java.util.Hashtable;

/**
 * This class allows the JADE core to retrieve configuration-dependent classes
 * and boot parameters.
 * <p>
 * Take care of using different instances of this class when launching
 * different containers/main-containers on the same JVM otherwise
 * they would conflict!
 * 
 * @author  Federico Bergenti
 * @author  Giovanni Caire - TILAB
 * @author  Giovanni Rimassa - Universita` di Parma
 * @version 1.0, 22/11/00
 * 
 */
public class ProfileImpl extends Profile {

  // HP Patch begin ----------------------------------------------------------------------------------
  private BasicProperties  props = null;
  // private Properties props = null;

  /**
   * Default communication port number.
   */
  public static final int DEFAULT_PORT = 1099;
  // HP Patch end ------------------------------------------------------------------------------------

    /**
       This constant is the key of the property whose value is the class name of
       the mobility manager.
     **/
  public static final String MOBILITYMGRCLASSNAME = "mobility";


  private Platform        myPlatform = null;
  private IMTPManager     myIMTPManager = null;
  private acc             myACC = null;
  private MobilityManager myMobilityManager = null;
  private NotificationManager myNotificationManager = null;
  private ResourceManager myResourceManager = null;

  public ProfileImpl(BasicProperties aProp) {
    props = aProp;
    try {
      // Set default values
      String host = InetAddress.getLocalHost().getHostName();
      props.setProperty(MAIN, "true");
      props.setProperty(MAIN_PROTO, "rmi");
      props.setProperty(MAIN_HOST, host);
      props.setProperty(MAIN_PORT, Integer.toString(DEFAULT_PORT));
      updatePlatformID();
      Specifier s = new Specifier();
      s.setClassName("jade.mtp.iiop.MessageTransportProtocol"); 
      List l = new ArrayList(1);
      l.add(s);
      props.put(MTPS, l);
    } 
    catch (UnknownHostException uhe) {
      uhe.printStackTrace();
    } 
    catch (IOException ioe) {
      ioe.printStackTrace();
    } 
  }

  /**
   * Creates a Profile implementation with the default configuration
   * for launching a main-container on the localhost, 
   * RMI internal Message Transport Protocol, port number 1099,
   * iiop MTP.
   */
  public ProfileImpl() {
    this(new BasicProperties());
  }

    /**
     * This constructor creates a default Profile for launching a platform.
     * @param host is the name of the host where the main-container should
     * be listen to. A null value means use the default (i.e. localhost)
     * @param port is the port number where the main-container should be
     * listen
     * for other containers. A negative value should be used for using
     * the default port number.
     * @param platformID is the synbolic name of the platform, if
     * different from default. A null value means use the default 
     * (i.e. localhost)
     **/
     public ProfileImpl(String host, int port, String platformID) {
     	this(); // Call default constructor
     	if(host != null)
       		props.setProperty(MAIN_HOST, host);
     	if(port > 0)
       		props.setIntProperty(MAIN_PORT, port);
     	if(platformID != null)
       		props.setProperty(PLATFORM_ID, platformID);
     	else 
	    updatePlatformID();
 	}

    public void updatePlatformID() {
	String h = props.getProperty(MAIN_HOST);
	String p = props.getProperty(MAIN_PORT);
	props.setProperty(PLATFORM_ID, h + ":" + p + "/JADE");
    }

    /**
     * Copy a collection of properties into this profile.
     * @param source The collection to be copied.
     */
    void copyProperties(BasicProperties source) {
        props.copyProperties(source);
    }
  
    /**
     * Return the underlying properties collection.
     * @return BasicProperties The properties collection.
     */
    public BasicProperties getProperties() {
        return props;
    }      

    /** HP.
    private MainContainerImpl theMainContainer = null;

    public void addPlatformListener(AgentManager.Listener aListener) throws NotFoundException {
        if (theMainContainer == null) {
            throw new NotFoundException("Unable to add listener, main container not set");
        }
        theMainContainer.addListener(aListener);
    }

    public void removePlatformListener(AgentManager.Listener aListener) throws NotFoundException {
        if (theMainContainer == null) {
            throw new NotFoundException("Unable to remove listener, main container not set");
        }
        theMainContainer.removeListener(aListener);
    }
    **/


  /**
   * Assign the given value to the given property name.
   *
   * @param key is the property name
   * @param value is the property value
   */
    public void setParameter(String key, String value) {
	props.put(key, value);
    }

  /**
   * Assign the given property value to the given property name
   *
   * @param key is the property name
   * @param value is the property value
   */
  public void setSpecifiers(String key, List value) {
    props.put(key, value);
  } 



  /**
   */
  protected Platform getPlatform() throws ProfileException {
    if (myPlatform == null) {
      createPlatform();
    } 

    return myPlatform;
  } 

  /**
   */
  protected IMTPManager getIMTPManager() throws ProfileException {
    if (myIMTPManager == null) {
      createIMTPManager();
    } 

    return myIMTPManager;
  } 

  /**
   */
  protected acc getAcc() throws ProfileException {
    if (myACC == null) {
      createACC();
    } 

    return myACC;
  } 

  /**
   */
  protected MobilityManager getMobilityManager() throws ProfileException {
    if (myMobilityManager == null) {
      createMobilityManager();
    } 

    return myMobilityManager;
  } 

  /**
   */
  protected ResourceManager getResourceManager() throws ProfileException {
    if (myResourceManager == null) {
      createResourceManager();
    } 

    return myResourceManager;
  }

  /**
   */
  protected NotificationManager getNotificationManager() throws ProfileException {
    if (myNotificationManager == null) {
      createNotificationManager();
    } 

    return myNotificationManager;
  }

  /**
   * Method declaration
   *
   * @throws ProfileException
   *
   * @see
   */
  private void createPlatform() throws ProfileException {
  	try {
	    String isMain = props.getProperty(MAIN);
  	  if (isMain == null || CaseInsensitiveString.equalsIgnoreCase(isMain, "true")) {
	      // The real Main
	      myPlatform = new MainContainerImpl(this);
	      // HP myPlatform = theMainContainer = new MainContainerImpl(this);
    	} 
    	else {
	    // A proxy to the Main
	    myPlatform = new MainContainerProxy(this);
    	}
  	}
  	catch (IMTPException imtpe) {
  		throw new ProfileException("Can't get a stub of the MainContainer: "+imtpe.getMessage());
  	}
  } 

  /**
   * Method declaration
   *
   * @throws ProfileException
   *
   * @see
   */
  private void createIMTPManager() throws ProfileException {
    // Get the parameter from the profile, use the RMI IMTP by default
    String className = getParameter(IMTP, "jade.imtp.rmi.RMIIMTPManager");

    try {
      myIMTPManager = (IMTPManager) Class.forName(className).newInstance();
    } 
    catch (Exception e) {
      e.printStackTrace();

      throw new ProfileException("Error loading IMTPManager class "+className);
    } 
  } 

  /**
   * Method declaration
   *
   * @throws ProfileException
   *
   * @see
   */
  private void createACC() throws ProfileException {
    // Use the Full ACC by default
    String className = new String("jade.core.FullAcc");
    try {
      myACC = (acc) Class.forName(className).newInstance();
    } 
    catch (Exception e) {
      throw new ProfileException("Error loading acc class "+className);
    } 
  } 

  /**
   * Method declaration
   *
   * @throws ProfileException
   *
   * @see
   */
  private void createMobilityManager() throws ProfileException {
     
      String className = getParameter(MOBILITYMGRCLASSNAME, "jade.core.RealMobilityManager");
      // default is real mobility manager
   
    try {
      myMobilityManager = (MobilityManager) Class.forName(className).newInstance();
    } 
    catch (Exception e) {
      throw new ProfileException("Error loading MobilityManager class "+className);
    } 
  } 

  private void createResourceManager() throws ProfileException {
  	myResourceManager = new FullResourceManager();
  } 

  private void createNotificationManager() throws ProfileException {
  	myNotificationManager = new RealNotificationManager();
  } 

  /**
   * Retrieve a String value from the configuration properties.
   * If no parameter corresponding to the specified key is found,
   * return the provided default.
   * @param key The key identifying the parameter to be retrieved
   * among the configuration properties.
   */
  public String getParameter(String key, String aDefault) {
    return props.getProperty(key, aDefault);
  } 

  /**
   * Retrieve a list of Specifiers from the configuration properties.
   * Agents, MTPs and other items are specified among the configuration
   * properties in this way.
   * If no list of Specifiers corresponding to the specified key is found,
   * an empty list is returned.
   * @param key The key identifying the list of Specifiers to be retrieved
   * among the configuration properties.
   */
  public List getSpecifiers(String key) throws ProfileException {
    List l = (List)props.get(key);
    if(l == null)
      l = new ArrayList(0);
    return l;
  } 

    public String toString() {
	StringBuffer str = new StringBuffer("(Profile");
	String[] properties = props.toStringArray();
	if (properties != null)
	    for (int i=0; i<properties.length; i++)
		str.append(" "+properties[i]);
	str.append(")");
	return str.toString();
    }
}

