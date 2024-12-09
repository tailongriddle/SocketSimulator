
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


// To run your code, you will start the Head node. Then you will start as many Nodes as you
// specified in your Head. Note that print statements showing connection and range status will be
// quite useful in debugging your code. Each time you start a Node it should be by pressing the run
// button in Eclipse. Thus, each Node will be running in its own virtual machine (which could be on
// different computers).
public class Head {
  private static int port = 7123; // set port
  private static List<Socket> nodeSockets = new ArrayList<>();
  private static int nodesIncoming = 3;
  private static int rangeStart = 1000;
  private static int rangeEnd = 1000000;
  private static int range = rangeEnd - rangeStart;
  


  public static void main(String[] args) {
    int finalCount = 0;
    
    try {
      ServerSocket ss = new ServerSocket(port); // new socket to communicate with client

      while (nodeSockets.size() < nodesIncoming) { // while less than amount of expected nodes...
        System.out.println("Waiting for a call");
        // blocking, accept() just sits there until a client connects to the server ^
        Socket nodeSocket = ss.accept(); // accept node

        System.out.println("Node accepted");
        nodeSockets.add(nodeSocket); // add node socket to list

      }

      System.out.println("All nodes connected");
      System.out.println("Distributing work");

      // distribute work between Nodes
      for (int i = 0; i < nodesIncoming; i++) {
        int nodeStart = rangeStart + (i * (range/nodesIncoming)); // start is beginning of range + increment
        int nodeEnd = (i == nodesIncoming - 1) ? rangeEnd : nodeStart + (range/nodesIncoming); // avoid div
                                                                                   // error

        // ask node to do work over range
        Socket nodeSocket = nodeSockets.get(i); // get nodeSocket from list

        try {
          int primeCount = 0;
          ObjectOutputStream oos = new ObjectOutputStream(nodeSocket.getOutputStream()); // create
                                                                                         // new
                                                                                         // output
                                                                                         // stream
          ObjectInputStream ois = new ObjectInputStream(nodeSocket.getInputStream()); // create new
                                                                                      // input
                                                                                      // stream

          oos.writeObject(nodeStart); // write start to node
          oos.writeObject(nodeEnd); // write end to node
          oos.flush(); // make sure all writes are completed

          System.out.println("Sent");

          // wait for node result
           int toAdd = (int) ois.readObject(); // read to primes list
           primeCount += toAdd;
          
          System.out.println("Received");

          // close streams
          oos.close(); // close output stream
          ois.close(); // close input stream
          nodeSocket.close(); // close node socket
          
          System.out.println("Prime Count: " + primeCount);
          finalCount += primeCount;

        } catch (IOException | ClassNotFoundException e) {// why class not found exception?
          e.printStackTrace();
        }


      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println("Final Count: " + finalCount);
  }

}

