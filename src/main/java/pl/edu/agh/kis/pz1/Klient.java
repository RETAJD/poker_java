package pl.edu.agh.kis.pz1;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Klient implements NetworkMessageReceiver {

    Gracz gracz;

    /**
     * Funckja publiczna inicjalizowanie nazwy uzytkownika przyjmujaca jako argument String username
     * */
    public Klient(String username) throws InterruptedException {
        Initialize(username);
    }
    /**
     * Funkcja publiczna Initialize - inicjalizujaca
     * */
    public void Initialize(String username) {
        try {
            Socket socket = new Socket("localhost", 1234);
            gracz = new Gracz();
            gracz.socket = socket;
            gracz.username = username;
            gracz.networkMessageReceiver = this;
            gracz.Initialize();
            gracz.Send(new NetworkMessage("Set username", username));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Funckja OdbiornikRuchu przyjmujaca jako argument - wiadomosc
     * dla poszczegolnych r.type rozpisane case-y  z mozliwosciami
     * w przypadku zlej komendy : " Niezrozumiała komenda ruch typu "
     * */
    public void OdbiornikRuchu(NetworkMessage r) {
        //if(!r.type.contains("Add card") && !r.type.contains("Remove card") && !r.type.contains("Message") )
        //    System.out.printf("Otrzymano ruch typu<%s> z zawartością <%s>\n", r.type, r.content);
        switch (r.type) {
            case "Set ID":
                System.out.printf("ID zostało ustawione na %s\n", r.content);
                r.sender.ID = Integer.parseInt(r.content);
                break;
            case "Add card":
                gracz.taliaGracza.karty.add(Card.fromString(r.content));
                //if(gracz.taliaGracza.karty.size()==5)
               // System.out.println(gracz.taliaGracza.toString());
                break;
            case "Remove card":
                gracz.taliaGracza.removeCard(Card.fromString(r.content));
                break;
            case "Restart":
                System.out.println("Rozpoczyna się nowa gra");
                gracz.restart();
                break;
            case "Already swapped":
                gracz.wymienial=true;
                break;
            case "Set tablePlace":
                System.out.printf("Gracz zajął %s miejsce przy stole\n", r.content);
                gracz.miejscePrzyStole=Integer.parseInt(r.content);
                break;
            case "Set bid":
                System.out.printf("Twój wkład wynosi %s\n", r.content);
                gracz.bid=Integer.parseInt(r.content);
                break;
            case "Set zetony":
                System.out.printf("Twoje saldo wynosi %s\n", r.content);
                gracz.zetony=Integer.parseInt(r.content);
                break;
            case "Declare move":
                GetMove();
                break;
            case "Message":
                System.out.println(">"+r.content);
                break;
            default:
                System.out.printf("Niezrozumiała komenda ruch typu<%s> z zawartością <%s>\n", r.type, r.content);
                break;

        }
    }

    @Override
    public void OnNetworkMessageReceived(NetworkMessage r) {
        OdbiornikRuchu(r);
    }



    /**
     * Funckja printująca instrukcje pomocnicze dla uzytkownika
     * */
    public void GetMove(){
        System.out.println("Teraz wykonujesz ruch");
        System.out.println(" Wpisz Przebijam 20 aby przebić do 20");
        System.out.println(" Wpisz Sprawdzam aby sprawdzić");
        System.out.println(" Wpisz Pasuje aby spasować");
        System.out.println(" Wpisz Czekam aby zaczekać");

        String cardSwapTutorial="Żeby wymienić niektóre karty wpisz np.  Swap: "+gracz.taliaGracza.karty.get(2).toString()+","+gracz.taliaGracza.karty.get(4).toString();
        cardSwapTutorial+="\nUwaga, masz tylko jedną zamianę - wpisz wszystkie karty, które chcesz wymienić";
        if(!gracz.wymienial)
            System.out.println(cardSwapTutorial);
        //System.out.println();
        System.out.println("Twoja talia to: "+gracz.taliaGracza.toString());

        Scanner scanner = new Scanner(System.in);
        udaloSieWprowadzicKomende=false;
        while(!udaloSieWprowadzicKomende)
            SendMove(scanner.nextLine());
    }
    public boolean udaloSieWprowadzicKomende=false;

    /**
     * Metoda wychwytująca ruchy uzytkownika przyjmujaca jako argument String input
     * */
    public void SendMove(String input) {
        System.out.println("Send move: <"+input+">");
        udaloSieWprowadzicKomende = true;
        if (input.contains("Swap")) {
            //System.out.println(input);
            //System.out.println(input.substring(input.indexOf(':')+1).replace(" ",""));
            gracz.Send(new NetworkMessage("GAME#Swap", input.substring(input.indexOf(':') + 1).replace(" ", "")));
        } else if (input.contains("Sprawdzam"))
            gracz.Send(new NetworkMessage("GAME#Sprawdzenie", " "));
        else if (input.contains("Pass"))
            gracz.Send(new NetworkMessage("GAME#Pass", " "));
        else if (input.contains("Przebijam"))
            gracz.Send(new NetworkMessage("GAME#Przebicie", input.substring(input.lastIndexOf(' ') + 1).replace(" ", "")));
        else {
            System.out.println("Wpisz ponownie");
            udaloSieWprowadzicKomende=false;
        }
    }


    public void TestSetReady(){
        gracz.Send(new NetworkMessage("Ready to play", "tru"));
        gracz.ready = true;
    }

    /**
     * Rozpoczęcie rozgrywki
     * */
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj nazwe: ");
        String username = scanner.nextLine();

        Klient k = new Klient(username);
        System.out.println("Wpisz    Ready    żeby rozpocząć grę: ");
        while (!k.gracz.ready) {
            String input = scanner.nextLine();
            if (input.contains("Ready")) {
                k.gracz.Send(new NetworkMessage("Ready to play", "tru"));
                k.gracz.ready = true;
                System.out.println("Zadeklarowałeś gotowość do gry!");
            }
        }
    }
}
