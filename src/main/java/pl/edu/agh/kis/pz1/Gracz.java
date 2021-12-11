package pl.edu.agh.kis.pz1;

import java.io.*;
import java.net.Socket;

public class Gracz {

    /**
     * Deklaracja potrzebnych zmiennych
     * */
    public int ID=-1;
    public String username;
    public NetworkMessageReceiver networkMessageReceiver;
    Talia taliaGracza= new Talia();
    boolean wymienial=false;
    boolean ready=false;
    public int miejscePrzyStole=1;
    public int zetony=1000;
    public int bid = 0;
    public boolean passed = false;

    public Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Gracz(){}

    public Gracz(int ID, Socket socket) {
        this.ID = ID;
        this.socket = socket;
    }

    /**
     * Inicjalizacja
     * w przypadku bledu "Nie udało się zrobić podłączenia do socketa"
     * */
    public void Initialize() {
        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) { e.printStackTrace();
            System.out.println("Nie udało się zrobić podłączenia do socketa");
        }
        new Thread(() -> { Receiver(networkMessageReceiver); } ).start();
    }
    /**
     * Funkcja wysyłajaca NetworkMessage
     * w przypadku bledu "Pisanie do socketa nie dziala"
     * */
    public void Send(NetworkMessage networkMessageDoWyslania){
        try {
            bufferedWriter.write(networkMessageDoWyslania.type + ":" + networkMessageDoWyslania.content);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Pisanie do socketa nie dziala");
        }
    }
    /**
     * Funkcja odbierajaca wiadomosc, przyjmujaca jako argument : NetworkMessageReceiver rr
     * w przyp ze dlugosc contentu jest mniejsza niz 2 wypisuje "Wiadomość jest zepsuta"
     * */
    public void Receiver( NetworkMessageReceiver rr ){
        while(socket.isConnected()){
            try{
                String msgFromGroupChat = bufferedReader.readLine();
                NetworkMessage incomingMove = new NetworkMessage();
                String[] content=msgFromGroupChat.split(":");
                if(content.length<2)
                    System.out.println("Wiadomość jest zepsuta");
                else{
                    incomingMove.type=content[0];
                    incomingMove.content=content[1];
                    incomingMove.sender=this;
                    rr.OnNetworkMessageReceived(incomingMove);
                }
            }catch(IOException e){
                //System.out.println("Czytanie z socketa nie dziala");
                //closeEverything(socket,bufferedReader,bufferedWriter);
            }
        }
        System.out.println("Port nie jest podłączony");
    }
    public void restartWithMsg(){
        restart();
        Send(new NetworkMessage("Restart"," "));
    }
    /**
     * Funkcja resetująca poszczególne elementy:
     * Talia,
     * ustawia wymienianie na false
     * gotowosc na false - potem trzeba wpisac ready
     * passed na false
     * */
    public void restart(){
       taliaGracza= new Talia();
       wymienial=false;
       ready=false;
       passed=false;
    }

    /**
     * Funkcja przyjmujaca jako argument newBid
     * Ustawia nowy bid
     * */
    public void SetBid(int newBid){
        bid=newBid;
        Send(new NetworkMessage("Set bid",String.valueOf(bid)));
    }
    /**
     * Funkcja przyjmujaca jako argument newZetony
     * Ustawia nowe zetony
     * */
    public void SetZetony(int newZetony){
        zetony=newZetony;
        Send(new NetworkMessage("Set zetony",String.valueOf(zetony)));
    }


}
