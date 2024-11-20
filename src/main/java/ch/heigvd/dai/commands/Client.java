package ch.heigvd.dai.commands;

import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "client", description = "Start the client part of the network game.")
public class Client implements Callable<Integer> {

  @CommandLine.Option(
      names = {"-H", "--host"},
      description = "Host to connect to.",
      required = true)
  protected String host;

  @CommandLine.Option(
      names = {"-p", "--port"},
      description = "Port to use (default: ${DEFAULT-VALUE}).",
      defaultValue = "6433")
  protected int port;

  @Override
  public Integer call() {
    throw new UnsupportedOperationException(
        "Please remove this exception and implement this method.");
  }

  public static String END_OF_LINE = "\n";

  public void run() {
    // Constants for messages
    public enum ClientCommand {
      HELLO,
      HELLO_WITHOUT_NAME,
      INVALID,
      HELP,
      QUIT
    }

    public enum ServerCommand {
      HI,
      INVALID
    }

    try (Socket socket = new Socket(HOST, PORT);
         Reader reader = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
         BufferedReader in = new BufferedReader(reader);
         Writer writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
         BufferedWriter out = new BufferedWriter(writer)) {
      System.out.println("[Client] Connected to " + HOST + ":" + PORT);
      System.out.println();

      // Display help message
      help();

      // Run REPL until user quits
      while (!socket.isClosed()) {
        // Display prompt
        System.out.print("> ");

        // Read user input
        Reader inputReader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
        BufferedReader bir = new BufferedReader(inputReader);
        String userInput = bir.readLine();

        try {
          // Split user input to parse command (also known as message)
          String[] userInputParts = userInput.split(" ", 2);
          ClientCommand command = ClientCommand.valueOf(userInputParts[0].toUpperCase());

          // Prepare request
          String request = null;

          if (request != null) {
            // Send request to server
            out.call()
            out.flush();
          }
        } catch (Exception e) {
          System.out.println("Invalid command. Please try again.");
          continue;
        }

        // Read response from server and parse it
        String serverResponse = in.readLine();

        // If serverResponse is null, the server has disconnected
        if (serverResponse == null) {
          socket.close();
          continue;
        }

        // Split response to parse message (also known as command)
        String[] serverResponseParts = serverResponse.split(" ", 2);

        ServerCommand message = null;
        try {
          message = ServerCommand.valueOf(serverResponseParts[0]);
        } catch (IllegalArgumentException e) {
          // Do nothing
        }

        // Handle response from server
        switch (message) {
          case HI -> {
            // As we know from the server implementation, the message is always the second part
            String helloMessage = serverResponseParts[1];
            System.out.println(helloMessage);
          }

          case INVALID -> {
            if (serverResponseParts.length < 2) {
              System.out.println("Invalid message. Please try again.");
              break;
            }

            String invalidMessage = serverResponseParts[1];
            System.out.println(invalidMessage);
          }
          case null, default ->
                  System.out.println("Invalid/unknown command sent by server, ignore.");
        }
      }

      System.out.println("[Client] Closing connection and quitting...");
    } catch (Exception e) {
      System.out.println("[Client] Exception: " + e);
    }

  }

  private static void help() {
    System.out.println("Usage:");
    System.out.println("  " + ClientCommand.HELLO + " <your name> - Say hello with a name.");
    System.out.println("  " + ClientCommand.HELLO_WITHOUT_NAME + " - Say hello without a name.");
    System.out.println("  " + ClientCommand.INVALID + " - Send an invalid command to the server.");
    System.out.println("  " + ClientCommand.QUIT + " - Close the connection to the server.");
    System.out.println("  " + ClientCommand.HELP + " - Display this help message.");
  }

}
