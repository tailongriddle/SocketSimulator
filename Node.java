
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public final class Node {
  private static int port = 7123;
  
  
  /***
   * 
   * Prime number brute force
   * 
   * 
   * @param num
   * @return bool
   */
  public static boolean isPrime(int num) {
    int i = 2;

    if (num == 2) { // exception for 2
      return true;
    }
    
    if (num == 1) {
      return false; 
    }
    
    while (i * i <= num) { // test numbers from 2 up
      // System.out.println("num is: " + num + ", i is: " + i);
      if (num % i == 0) { // if divisible
        return false; // it is not prime!
      }

      i++;

    }

    return true;
  }

  /**
   * 
   * Prime number counter
   * 
   * 
   * @param num1
   * @param num2
   * @return count
   */
  public static int countPrime(int num1, int num2) {

    int count = 0;

    for (int i = num1; i < num2; i++) { // for each num in range...
      if (isPrime(i)) {
        // System.out.println(i);
        count++;
      }

    }

    return count;
  }



  public static void main(String[] args) {
    
    int primeCount = 0;

    try {
      
      System.out.println("About to call");
      Socket s = new Socket("localhost", port); // (IP address of server, port)
      System.out.println("Connected");
      
      ObjectInputStream ois = new ObjectInputStream(s.getInputStream()); //create new input stream
      ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream()); //create new output stream
       
      int start = (int) ois.readObject(); //read input from Head and set start int
      int end = (int) ois.readObject(); //read input from Head and set start int
      
      System.out.println("Range is: " + start + " to " + end);
      
      // find primes
      primeCount = countPrime(start, end); // count number of primes in range
      
      // send prime count back
      oos.writeObject(primeCount); // write to output stream
      oos.flush(); // make sure all writes are completed
      s.close(); // close socket
      
      
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

  }

}
