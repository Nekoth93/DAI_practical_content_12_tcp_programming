package ch.heigvd.dai.commands;

import java.util.concurrent.Callable;
import picocli.CommandLine;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.Random;

@CommandLine.Command(name = "server", description = "Start the server part of the network game.")
public class Server implements Callable<Integer> {

  @CommandLine.Option(
      names = {"-p", "--port"},
      description = "Port to use (default: ${DEFAULT-VALUE}).",
      defaultValue = "6433")
  protected int port;

  @CommandLine.Parameters(
          paramLabel = "<guessedValue>",
          description = "Value guessed by user"
  )
  private int guessValue;

  @Override
  public Integer call() {
    public static String END_OF_LINE = "\n";

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("[Server] listening on port " + port);


      Random random = new Random();
      int randomNumber = random.nextInt(100 - 0 + 1) + min;


      while (!serverSocket.isClosed()) {
        try(Socket socket = serverSocket.accept();
            Reader reader = new InputStreamReader(socket.getInputStream(), StandardCharset.UTF_8);
            BufferedReader in = new BufferedReader(reader);
            Writer writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharset.UTF_8);
            BufferedWriter out = new BufferedWriter(writer)) {

          System.out.println("[Server] New client connected from "
                  + socket.getInetAddress().getHostAddress()
                  + ":"
                  + socket.getPort());
          while (!socket.isClosed()) {
            // Lecture de la réponse du client
            String clientRequest = in.readLine();

            // Si clientRequest est null, le client à été déconnecté
            // Le serveur peut alors fermer la connection et attendre un nouveau client
            if (clientRequest == null) {
              socket.close();
              continue;
            }

            // Divise l'entrée de l'utilisateur pour traiter la commande
            if(clientRequest.length() > 1) {
              String[] clientRequestParts = clientRequest.split(" ", 2);
              port = clientRequestParts[0];

            } else {
              guessValue = clientRequestParts[1];
            }

            ClientCommand command = null;
            try {
              command = ClientCommand.valueOf(clientRequestParts[0]);
            } catch (Exception e) {
              // Ne fait rien
            }

            // Préparation de la réponse
            String response = null;

            // Gère la requête du client

            if(Object.equals(value, guessValue)) {
              response = "Good guess!";
              socket.close();
            } else {
              response = "Try again!";
            }

//            switch (command) {
//              case HELLO -> {
//                if (clientRequestParts.length < 2) {
//                  System.out.println(
//                          "[Server] " + command + " command received without <name> parameter. Replying with "
//                                  + ServerCommand.INVALID
//                                  + ".");
//                  response = ServerCommand.INVALID + "Missing <name> parameter. Please try again.";
//
//                  break;
//                }
//                String name = clientRequestParts[1];
//
//                Systeme.out.println("[Server] Received HELLO command with name: " + name);
//                System.out.println("[Server] Replying with Hi command.");
//
//                response = ServerCommand.HI + "Hi, " + name + "!";
//              }
//              case null, default -> {
//                System.out.println(
//                        "[Server] Unknown command: sent by client, reply with "
//                                + ServerCommand.INVALID
//                                + ".");
//                response = ServerCommand.INVALID + "Unknown command. Please try again.";
//              }
            }

            // Envois la réponse au client
            out.write(response + END_OF_LINE);
            out.flush();
          }

          System.out.println("[Server] Closing connection");
        } catch (IOException e) {
          System.out.println("[Server] IO execption" + e);
        }
      }
    } catch (IOException e) {
      System.out.println("[Server] IO execption" + e);
    }
  }
}
