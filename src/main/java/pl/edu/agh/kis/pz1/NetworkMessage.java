package pl.edu.agh.kis.pz1;

public class NetworkMessage {
    public Gracz sender;
    public String type;
    /* Typy Ruchów:
    * SetUsername
    * SetID
    * */
    public String content;


    public NetworkMessage() {}
    public NetworkMessage(String type, String content) {
        this.type=type;
        this.content=content;
    }
}

