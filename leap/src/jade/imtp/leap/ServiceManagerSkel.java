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

package jade.imtp.leap;

//#MIDP_EXCLUDE_FILE

import jade.core.Node;
import jade.core.NodeDescriptor;
import jade.core.Service;
import jade.core.ServiceManagerImpl;
import jade.core.ServiceException;
import jade.core.IMTPException;

import jade.security.AuthException;

import jade.util.leap.List;
import jade.util.leap.LinkedList;
import jade.util.leap.Iterator;


/**

   The <code>ServiceManagerSkel</code> class is the remote
   adapter for JADE platform <i>Service Manager</i> and
   <i>Service Finder</i> components, running over LEAP transport layer.

   @author Giovanni Rimassa - FRAMeTech s.r.l.
*/
class ServiceManagerSkel extends Skeleton {

    private ServiceManagerImpl impl;
    private LEAPIMTPManager manager;

    public ServiceManagerSkel(ServiceManagerImpl sm, LEAPIMTPManager mgr) {
	impl = sm;
	manager = mgr;
    }

    public Command executeCommand(Command command) throws Throwable {

	switch (command.getCode()) {

	case Command.GET_PLATFORM_NAME: {

	    // Execute command...
	    String name = impl.getPlatformName();

	    command.reset(Command.OK);
	    command.addParam(name);
	    break;
	} 

	case Command.ADD_NODE: {
	    NodeDescriptor desc = (NodeDescriptor)command.getParamAt(0);
	    String[] svcNames = (String[])command.getParamAt(1);
	    String[] svcInterfaceNames = (String[])command.getParamAt(2);
	    boolean propagate = ((Boolean)command.getParamAt(3)).booleanValue();

	    // Execute command...
	    String name = addNode(desc, svcNames, svcInterfaceNames, propagate);

	    command.reset(Command.OK);
	    command.addParam(name);
	    break;
	} 

	case Command.REMOVE_NODE: {
	    NodeDescriptor desc = (NodeDescriptor)command.getParamAt(0);
	    boolean propagate = ((Boolean)command.getParamAt(1)).booleanValue();

	    // Execute command...
	    removeNode(desc, propagate);

	    command.reset(Command.OK);
	    break;
	} 

	case Command.ACTIVATE_SERVICE: {
	    String svcName = (String)command.getParamAt(0);
	    String itfName = (String)command.getParamAt(1);
	    NodeDescriptor where = (NodeDescriptor)command.getParamAt(2);
	    boolean propagate = ((Boolean)command.getParamAt(3)).booleanValue();

	    // Execute command...
	    Class itf = Class.forName(itfName);
	    activateService(svcName, itf, where, propagate);

	    command.reset(Command.OK);
	    break;
	} 

	case Command.FIND_SLICE_NODE: {
	    String serviceKey = (String)command.getParamAt(0);
	    String sliceKey = (String)command.getParamAt(1);

	    // Execute command...
	    Node n = findSliceNode(serviceKey, sliceKey);
      
	    command.reset(Command.OK);
	    command.addParam(n);
	    break;
	} 

	case Command.FIND_ALL_NODES: {
	    String serviceKey = (String)command.getParamAt(0);

	    // Do something...
	    Node[] nodes = findAllNodes(serviceKey);

	    command.reset(Command.OK);
	    command.addParam(nodes);
	    break;
	}

	case Command.SERVICE_MANAGER_ADOPT: {

	    // Do something...
	    Node n = (Node)command.getParamAt(0);
	    adopt(n);

	    command.reset(Command.OK);
	    break;
	}

	case Command.SERVICE_MANAGER_ADD_REPLICA: {
	    String addr = (String)command.getParamAt(0);

	    // Do something...
	    String[] addresses = addReplica(addr);

	    command.reset(Command.OK);
	    command.addParam(addresses);
	    break;
	}

	case Command.SERVICE_MANAGER_UPDATE_COUNTERS: {
	    int nodeCnt = ((Integer)command.getParamAt(0)).intValue();
	    int mainCnt = ((Integer)command.getParamAt(1)).intValue();

	    // Do something...
	    updateCounters(nodeCnt, mainCnt);

	    command.reset(Command.OK);
	    break;
	}

	}

	return command;
    }

    private void activateService(String name, Class itf, NodeDescriptor desc, boolean propagate) throws ServiceException, IMTPException {

	String sliceName = desc.getName();
	Node remoteNode = desc.getNode();

	//	System.out.println("Activation requested of service <" + name + "> on node <" + sliceName + ">");

	// Create a slice proxy for the new node
	Service.Slice slice = manager.createSliceProxy(name, itf, remoteNode);
	impl.addRemoteSlice(name, sliceName, slice, remoteNode);

	if(propagate) {
	    manager.serviceActivated(name, itf, remoteNode);
	}
    }

    private void deactivateService(String name, NodeDescriptor desc) throws ServiceException, IMTPException {
	// FIXME: To be implemented

	// Remove the slice of the service corresponding to the calling node...

	// If no slices remain, remove also the service...

    }

    private String addNode(NodeDescriptor desc, String[] svcNames, String[] svcInterfacesNames, boolean propagate) throws ServiceException, AuthException, IMTPException {

	// Add the node to the node table
	String containerName = impl.addRemoteNode(desc, propagate);

	String name = desc.getName();

	System.out.println("Adding node <" + name + "> to the platform.");

	// Fill a Class array from the names array
	Class[] svcInterfaces = new Class[svcInterfacesNames.length];
	for(int i = 0; i < svcInterfacesNames.length; i++) {
	    try {
		svcInterfaces[i] = Class.forName(svcInterfacesNames[i]);
	    }
	    catch(ClassNotFoundException cnfe) {
		svcInterfaces[i] = jade.core.Service.Slice.class;
	    }
	}

	// Activate all the node services
	List failedServices = new LinkedList();
	for(int i = 0; i < svcNames.length; i++) {
	    try {
		activateService(svcNames[i], svcInterfaces[i], desc, propagate);
	    }
	    catch(IMTPException imtpe) {
		// This should never happen, because it's a local call...
		imtpe.printStackTrace();
	    }
	    catch(ServiceException se) {
		failedServices.add(svcNames[i]);
	    }
	}

	// Throw a failure exception, if needed
	if(!failedServices.isEmpty()) {

	    // All service activations failed: throw a single exception 
	    if(failedServices.size() == svcNames.length) {
		throw new ServiceException("Total failure in installing the services for local node");
	    }
	    else {

		// Only some service activations failed: throw a single exception with the list of the failed services
		Iterator it = failedServices.iterator();
		String names = "[ ";
		while(it.hasNext()) {
		    names = names.concat((String)it.next() + " ");		
		}
		names = names.concat("]");
		throw new ServiceException("Partial failure in installing the services " + names);
	    }
	}

	if(propagate) {
	    manager.nodeAdded(desc, svcNames, svcInterfaces, impl.getNodeCounter(), impl.getMainNodeCounter());
	}

	return containerName;
    }

    private void removeNode(NodeDescriptor desc, boolean propagate) throws ServiceException, IMTPException {
	impl.removeRemoteNode(desc, propagate);
    }

    private Node[] findAllNodes(String serviceKey) throws ServiceException, IMTPException {
	try {
	    return impl.findAllNodes(serviceKey);
	}
	catch(IMTPException imtpe) {
	    throw new ServiceException("IMTP Error during slice list retrieval", imtpe);
	}
    }

    private Node findSliceNode(String serviceKey, String sliceKey) throws ServiceException, IMTPException {
	try {
	    return (Node) impl.findSliceNode(serviceKey, sliceKey);
	}
	catch(IMTPException imtpe) {
	    throw new ServiceException("IMTP Error during slice lookup", imtpe);
	}
    }

    private void adopt(Node n) throws IMTPException {
	impl.monitor(n);
    }

    public String[] addReplica(String addr) throws IMTPException {
	try {

	    // Retrieve the RMI object for the replica...
	    ServiceManagerStub replica = manager.lookupRemoteServiceManager(addr);

	    // Send all nodes with their installed services...
	    List infos = impl.getAllNodesInfo();

	    Iterator it = infos.iterator();
	    while(it.hasNext()) {
		ServiceManagerImpl.NodeInfo info = (ServiceManagerImpl.NodeInfo)it.next();
		replica.addNode(info.getNodeDescriptor(), info.getServiceNames(), info.getServiceInterfacesNames(), false);
	    }

	    replica.updateCounters(impl.getNodeCounter(), impl.getMainNodeCounter());
	    return manager.getServiceManagerAddresses();
	}
	catch(Exception e) {
	    e.printStackTrace();
	    return new String[0];
	}
    }

    public void updateCounters(int nodeCnt, int mainCnt) throws IMTPException {
	impl.setNodeCounters(nodeCnt, mainCnt);
    }

}
