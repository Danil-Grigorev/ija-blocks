package Elements.Containers;

import Elements.*;
import Logic.Logic;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class ItemContainer implements Serializable {

    private ArrayList <BlockSave> blocks;
    private ArrayList <ConnectionSave> connections;
    private ArrayList <PortSave> ports;
    private int id;

    public ItemContainer() {
        blocks = new ArrayList<>();
        connections = new ArrayList<>();
        ports = new ArrayList<>();
        id = 0;
    }

    public int generateId() {
        return id++;
    }

    public ArrayList<BlockSave> getBlocks() {
        return blocks;
    }

    public ArrayList<PortSave> getPorts() {
        return ports;
    }

    public ArrayList<ConnectionSave> getConnections() {
        return connections;
    }

    public void addBlock(BlockSave save) {
        if (this.blocks.contains(save)) {
            this.blocks.set(this.blocks.indexOf(save), save);
        }
        else {
            this.blocks.add(save);
        }
        show();
    }

    public void addConnection(ConnectionSave save) {
        if (this.connections.contains(save)) {
            this.connections.set(this.connections.indexOf(save), save);
        }
        else {
            this.connections.add(save);
        }
        show();
    }

    public void addPort(PortSave save) {
        if (this.ports.contains(save)) {
            this.ports.set(this.ports.indexOf(save), save);
        }
        else {
            this.ports.add(save);
        }
        show();
    }

    public void show() {
        System.out.println("Container content:");
        System.out.println("-------------------------------");
        System.out.println("blocks: " + this.blocks);
        for (BlockSave bl : this.blocks) {
            System.out.println("\tBlock: " + bl.getId());
        }
        System.out.println("ports: " + this.ports);
        for (PortSave pt : this.ports) {
            System.out.println("\t" + pt.getType() + "Port: " + pt.getId() + " -> Block: " + pt.getBlockId());
        }
        System.out.println("connections: " + this.connections);
        for (ConnectionSave cn : this.connections) {
            System.out.println("\tConnection: " + cn.getId()
                    + " -> Port from: " + cn.getFromPortId()
                    + " -> Port to: " + cn.getToPortId());
        }
        System.out.println("-------------------------------");

    }

    public void remove(Object o) {
        ArrayList<BlockSave> blocksToRemove = new ArrayList<>();
        ArrayList<PortSave> portsToRemove = new ArrayList<>();
        ArrayList<ConnectionSave> consToRemove = new ArrayList<>();
        if (o instanceof Block) {
            for (BlockSave block : this.blocks) {
                if (block.getId() == ((Block) o).getId()) {
                    blocksToRemove.add(block);
                }
            }

            for (PortSave port : this.ports) {
                if (port.getBlockId() == ((Block) o).getId()) {
                    portsToRemove.add(port);
                    for (ConnectionSave con : this.connections) {
                        if (con.getToPortId() == port.getId()
                                || con.getFromPortId() == port.getId()) {
                            consToRemove.add(con);
                        }
                    }
                }
            }
        }
        else if (o instanceof Port) {
            for (PortSave port : this.ports) {
                if (port.getId() == ((Port) o).getId()) {
                    portsToRemove.add(port);
                    for (ConnectionSave con: this.connections) {
                        if (port.getId() == con.getFromPortId()
                                || port.getId() == con.getToPortId()) {
                            consToRemove.add(con);
                        }
                    }
                }
            }
        }
        else if (o instanceof Connection) {
            for (ConnectionSave con : this.connections) {
                if (con.getId() == ((Port) o).getId()) {
                    consToRemove.add(con);
                }
            }
        }

        this.connections.removeAll(consToRemove);
        this.ports.removeAll(portsToRemove);
        this.blocks.removeAll(blocksToRemove);
        show();
    }

    public void restore(Logic logic, AnchorPane scheme) {
        Hashtable<Integer, Block> rest_blocks = new Hashtable<>();
        Hashtable<Integer, Port> rest_ports = new Hashtable<>();
        Hashtable<Integer, Connection> rest_cons = new Hashtable<>();
        Block tmp_bl;
        Port tmp_prt;
        Connection tmp_con;

        for (BlockSave bl : this.blocks) {
            rest_blocks.put(bl.getId(), bl.restore(logic, scheme));
        }
        for (PortSave prt : ports) {
            Block tmp = rest_blocks.get(prt.getBlockId());
            rest_ports.put(prt.getId(), prt.restore(logic, tmp));
        }
        for (ConnectionSave con : connections) {
            rest_cons.put(con.getId(), con.restore(logic, scheme));
        }

        try {
            for (ConnectionSave conS : connections) {
                tmp_con = rest_cons.get(conS.getId());
                tmp_prt = rest_ports.get(conS.getFromPortId());
                tmp_prt.setConnection(tmp_con);

                tmp_prt = rest_ports.get(conS.getToPortId());
                tmp_prt.setConnection(tmp_con);
            }
        } catch (IOException e) {
            System.err.println("Can't restore scheme from save");
            e.printStackTrace();
            Platform.exit();
            System.exit(99);
        }

        for (BlockSave blS : blocks) {
            tmp_bl = rest_blocks.get(blS.getId());
            for (Integer portId : blS.getInPorts()) {
                tmp_bl.addInPort((InputPort) rest_ports.get(portId));
            }
            for (Integer portId : blS.getOutPorts()) {
                tmp_bl.addOutPort((OutputPort) rest_ports.get(portId));
            }
        }
    }

}
