package pl.edu.agh.kis.pz1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static java.lang.Thread.sleep;

public class Serwer implements NetworkMessageReceiver {
    public static void main(String[] args) {
        // write your code here
        Serwer s = new Serwer();
    }

    Random r=new Random();
    ServerSocket serverSocket;
    List<Gracz> gracze=new ArrayList<>();
    List<Talia> talie = new ArrayList<>();

    List<Integer> playersThatWantToStart=new ArrayList<>();

    HashMap<Integer,Gracz> przypisanieGraczyDoMiejsc=new HashMap<Integer,Gracz>();

    int phase=0;// 0 - pre, 1 - pierwsza tura
    Talia allCards=Talia.allCards();
    int pula = 0;

    Serwer(){
        System.out.println("Nowy serwer powstał");
        Initialize();
    }

    public void Initialize(){
        System.out.println("Inicjalizacja");
        //inicjalizacja sieci
        try {
            serverSocket = new ServerSocket(1234);//nasluchiwanie portu 1234
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Port jest zajęty");
        }
        new Thread(this::PlayerAccepter).start();
        System.out.println("Koniec Inicjalizacji");
    }


    /**
     * Funckcja dodająca graczy do rozgrywki,
     * ustawiajaca ID przypisująca do stołu
     * */
    public void PlayerAccepter(){

        System.out.println("Oczekiwanie na graczy");
        try{
            while(!serverSocket.isClosed()){
                Socket graczSocket = serverSocket.accept();
                System.out.println("Nowy gracz się podłączył");
                Gracz g= new Gracz();
                g.socket=graczSocket;
                g.ID=g.hashCode();
                g.networkMessageReceiver =this;
                g.Initialize();
                g.Send(new NetworkMessage("Set ID",String.valueOf(g.ID)));
                int mps=randomRange(0,9);
                while(przypisanieGraczyDoMiejsc.containsKey(mps))
                    mps=randomRange(0,9);
                g.miejscePrzyStole=mps;
                przypisanieGraczyDoMiejsc.put(mps,g);
                g.Send(new NetworkMessage("Set tablePlace",String.valueOf(g.miejscePrzyStole)));
                gracze.add(g);
            }
        }catch(IOException ignored){

        }
    }
    public int randomRange(int min, int max) {
        return r.nextInt(max - min) + min;
    }
    @Override
    public void OnNetworkMessageReceived(NetworkMessage r) {
        ProcessRuch(r);
    }
    public void ProcessRuch(NetworkMessage r) {
        System.out.printf("Otrzymano ruch typu<%s> z zawartością <%s>\n",r.type,r.content);
        switch (r.type){
            case "Set username":
                r.sender.username=r.content;
                break;
            case "Set ID":
                r.sender.ID=Integer.parseInt(r.content);
                break;
            case "Set bid":
                r.sender.bid=Integer.parseInt(r.content);
                break;
            case "Ready to play":
                if(!playersThatWantToStart.contains(r.sender.ID))
                    playersThatWantToStart.add(r.sender.ID);
                r.sender.ready=true;
                PhaseZero();
                break;
            default:
                if(r.type.contains("GAME#")){
                    ProcessGameMove(r);
                }
                else
                    System.out.printf("Niezrozumiała komenda ruch typu<%s> z zawartością <%s>\n",r.type,r.content);
                break;
        }
    }
    /**
     * Funckcja odpowiedzialna za komunikacje servera
     * dająca informacje o dzialaniu gracza
     * */
    public void ProcessGameMove(NetworkMessage r){
        if(r.sender==tenCoAktualnieWykonujeRuch){
            switch (r.type){
                case "GAME#Swap":
                    if(phase==1) {
                        if (!r.sender.wymienial) {
                            SwapCards(r);
                            r.sender.Send(new NetworkMessage("Already swapped"," "));
                        } else {
                            System.out.printf("Wymieniac można tylko raz");
                        }
                        RequestMoveAgain();
                    }
                    else{
                        System.out.printf("Wymieniac można tylko w turze pierwszej");
                        RequestMoveAgain();
                    }
                    break;
                case "GAME#Przebicie":
                    r.sender.SetBid(Integer.parseInt(r.content));
                    SendMessangetoAll(r.sender, r.sender.username + " podbił stawkę do " + r.sender.bid);
                    ProcessGame();
                    break;
                case "GAME#Pass":
                    r.sender.passed = true;
                    SendMessangetoAll(r.sender, r.sender.username+ " spasował");
                    pula +=r.sender.bid;
                    r.sender.SetZetony(r.sender.zetony - r.sender.bid);
                    r.sender.SetBid(0);
                    ProcessGame();
                    break;
                case "GAME#Sprawdzenie":
                    //r.sender.bid = poprzedniGracz.bid;
                    r.sender.SetBid(poprzedniGracz.bid);
                    SendMessangetoAll(r.sender, r.sender.username+ " sprawdza");
                    ProcessGame();
                    break;
                case "GAME#Czekaj":
                    if(phase==1){
                        SendMessangetoAll(r.sender, r.sender.username+ " czeka");
                        ProcessGame();
                    }
                    else
                        System.out.println("Nielegalny ruch!");
                    break;
                default:
                    System.out.printf("Niezrozumiała komenda GAME#  typu<%s> z zawartością <%s>\n",r.type,r.content);
                    break;
            }
        }
        else{
            System.out.print("Gracz się chciał ruszyć kiedy niepowinien");
            r.sender.Send(new NetworkMessage("Message", "Nie twoja kolej"));
        }
    }
    /**
     * Funkcja wysylajaca wiadomosci do wszystkich
     * */
    public void SendMessangetoAll(Gracz excuded, String message){
        for(var g: gracze){
            if(g!= excuded)
                g.Send(new NetworkMessage("Message",message));
        }
    }
    public Gracz tenCoAktualnieWykonujeRuch;
    public Gracz poprzedniGracz;
    /**
     * Przejście w kolejce do następnego gracza
     * */
    public void moveToNextGracz() {
        int miejsce=0;
        if(tenCoAktualnieWykonujeRuch!=null)
            miejsce=tenCoAktualnieWykonujeRuch.miejscePrzyStole+1;
        miejsce%=9;
        while(!przypisanieGraczyDoMiejsc.containsKey(miejsce)) {
            miejsce++;
            miejsce%=9;
        }
        poprzedniGracz=tenCoAktualnieWykonujeRuch;
        tenCoAktualnieWykonujeRuch = przypisanieGraczyDoMiejsc.get(miejsce);
        if(poprzedniGracz==null)
            poprzedniGracz=tenCoAktualnieWykonujeRuch;
        if(tenCoAktualnieWykonujeRuch.passed)
            moveToNextGracz();
    }

    public void RequestNextMove(){
        moveToNextGracz();
        tenCoAktualnieWykonujeRuch.Send(new NetworkMessage("Declare move",String.valueOf(poprzedniGracz.bid)));
    }
    public void RequestMoveAgain(){
        tenCoAktualnieWykonujeRuch.Send(new NetworkMessage("Declare move",String.valueOf(poprzedniGracz.bid)));
    }
    /**
     * Funkcja wymieniająca karty w pokerze, przyjmujaca NetworkMessage jako argument
     * */
    public void SwapCards(NetworkMessage r){
        r.sender.wymienial=true;
        String ss=r.content.replace(" ","");
        ss=ss.replace("\n","");
        String[] sCards=ss.split(",");

        for (var s : sCards){
            Card c = Card.fromString(s);
            r.sender.taliaGracza.removeCard(c);
            allCards.karty.add(c);
            r.sender.Send(new NetworkMessage("Remove card",c.toString()));
        }
        FillPlayerDeck(r.sender);
    }
    private void FillPlayerDeck(Gracz g) {
        while (g.taliaGracza.karty.size() < 5) {
            int idx = randomRange(0, allCards.karty.size());
            Card picked = allCards.karty.get(idx);
            allCards.removeCard(picked);
            g.taliaGracza.karty.add(picked);
            g.Send(new NetworkMessage("Add card", picked.toString()));
        }
    }
    public void ProcessGame(){
        switch (phase) {
            case 0 -> PhaseZero();
            case 1 -> PhaseOne();
            case 2 -> PhaseTwo();
            case 3 -> PhaseThree();
        }
    }
    /**
     * Funckcja dodająca graczy do rozgrywki,
     * ustawiajaca ID przypisująca do stołu
     * */
    public void PhaseZero(){
        System.out.println("Phase Zero");
        if(playersThatWantToStart.size()>gracze.size()/2 && phase==0 && gracze.size()>=2){
            phase=1;
            PreOnePhase();
            PhaseOne();
            return;
        }
    }
    public List<Gracz> graczePoPierwszejTurze=new ArrayList<>();
    /**
     * Faza 1 rozgrywki pokerowej
     * */
    public void PhaseOne(){
        System.out.println("PhaseOne");
        if(graczePoPierwszejTurze.size()==gracze.size()&&phase==1) {
            phase = 2;
            for(var g : gracze)
                g.Send(new NetworkMessage("Already swapped"," "));
            PhaseTwo();
            return;
        }
        else
            graczePoPierwszejTurze.add(tenCoAktualnieWykonujeRuch);
        RequestNextMove();
    }
    /**
     * Faza 2 rozgrywki pokerowej
     * */
    public void PhaseTwo(){
        System.out.println("PhaseTwo");
        int v1=tenCoAktualnieWykonujeRuch.bid;
        for(var g : gracze)
            if(g.bid!=v1)
                v1=-1;
        if(v1!=-1){
            //wszyscy mają tyle samo
            phase=3;
            PhaseThree();
            return;
        }
        RequestNextMove();
    }

    /**
     * Faza 3 rozgrywki pokerowej
     * */
    public void PhaseThree(){
        System.out.println("PhaseThree");
        //Podsumuj ręce i rozdziel nagrody
        Gracz bestPlayer=gracze.get(0);
        for (var g:gracze) {
            if(!g.passed) {
                if (g.taliaGracza.isGreater(bestPlayer.taliaGracza)){
                    bestPlayer = g;
                }
            }
        }
        List<Gracz> bestPlayers=new ArrayList<>();
        for (var g:gracze) {
            if (!g.passed) {
                if (g.taliaGracza.isEqual(bestPlayer.taliaGracza)) {
                    bestPlayers.add(g);
                }
            }
        }
        for (var g:gracze) {
            pula+=g.bid;
            g.SetZetony(g.zetony-g.bid);
        }
        SendMessangetoAll(null, "Zwycięzcy to  "+listofwinners(bestPlayers));

        int prize=pula/bestPlayers.size();
        SendMessangetoAll(null, "Nagroda wynosi  "+String.valueOf(prize));
        for (var g:bestPlayers)
            g.SetZetony(g.zetony+prize);
        for (var g:gracze)
            g.restartWithMsg();
        phase=0;
        playersThatWantToStart=new ArrayList<>();
        pula=0;
        ProcessGame();
    }
    public void PreOnePhase(){
        for(var g : gracze) {
            FillPlayerDeck(g);
            g.SetBid(10);
        }
    }
    /**
     * Funckcja dodajaca (zwyciezcow, zwyciezcy) rozgrywki
     * */
    public String listofwinners(List <Gracz> zwyciezcy){
        String x = "";
        for (int i=0;i<zwyciezcy.size();i++){
            x+= zwyciezcy.get(i).username;
            if(i != zwyciezcy.size()-1){
                x+=", ";
            }
        }
        return x;
    }
}

