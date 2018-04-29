package Elements.Containers;

import Elements.Block;
import Elements.Connection;
import Elements.Port;
import javafx.scene.layout.AnchorPane;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class ItemContainer implements Serializable {

    private ArrayList <BlockSave> blocks;
    private ArrayList <ConnectionSave> connections;
    private ArrayList <PortSave> ports;

    public ItemContainer() {
        blocks = new ArrayList<>();
        connections = new ArrayList<>();
        ports = new ArrayList<>();

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
            System.out.println("\tPort: " + pt.getId() + " -> Block: " + pt.getBlockId());
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

}
