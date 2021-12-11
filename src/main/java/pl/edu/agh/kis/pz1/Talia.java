package pl.edu.agh.kis.pz1;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Talia {
    public List<Card> karty = new ArrayList<>();

    public boolean hasCard(Card c) {
        for (var ca : karty)
            if (ca == c)
                return true;
        return false;
    }

    public static Talia allCards() {
        Talia t = new Talia();
        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                t.karty.add(new Card(r, s));
            }
        }
        return t;
    }

    //czy wszystkie karty w talii maja ten sam kolor
    public boolean sameColor() {
        Suit kolor = karty.get(0).suit;

        for (int i = 1; i < karty.size(); i++) {
            if (karty.get(i).suit != kolor)
                return false;
        }
        return true;
    }
    public void removeCard(Card c){
        for(int i =0 ; i<karty.size(); i++ ){
            if(karty.get(i).equals(c)){
                karty.remove(i);
                i--;
            }
        }
    }


    //FUNKCJA KTORA SPRAWDZA CZY MAMY 5 KART POD RZAD
    public boolean chainowanie() {
        //TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT,
        //        NINE, TEN, JACK, QUEEN, KING, ACE


        int counter = 1;
        boolean hasKing = false;
        boolean hasTwo = false;

        for (int i = 0; i < karty.size(); i++) {
            counter=1;
            Card wart = karty.get(i);
            for (int j = 0; j < karty.size(); j++) {
                Card wart2 = karty.get(j);
                if(wart.rank==Rank.KING) hasKing=true;
                if(wart2.rank==Rank.TWO) hasTwo=true;
                if(hasKing && hasTwo) return false;

                if (wart.IsOneBelow(wart2)) {
                    wart=wart2;
                    counter++;
                    j=-1;
                }
            }
            if (counter == 5)
                return true;
        }
        return false;
    }

    /**
     * getType - zwraca jaki układ mamy w danej talii
     * */
    TaliaType getType(){
         if(Poker_krolewski())
             return TaliaType.PokerKrolewski;
         if(Poker())
             return TaliaType.Poker;
         if(Kareta())
            return TaliaType.Kareta;
         if(Full())
             return TaliaType.Full;
         if(sameColor())
             return TaliaType.Color;
         if(Strit())
             return TaliaType.Strit;
         if(Trojka())
             return TaliaType.Trojka;
         if(DwiePary())
             return TaliaType.DwiePary;
         if(Para())
             return TaliaType.Para;
         return TaliaType.Najwyzsza;
     }


    /**
     * funkcja porownujaca wartosci ( czy zawiera  (AS, KRÓL, DAMA, WALET,dziesiatka))
     * */
    private boolean Poker_krolewski() {
        int AS = 0;
        int KROL = 0;
        int DAMA = 0;
        int WALET = 0;
        int DZIESIEC = 0;

        for (int i = 1; i < karty.size(); i++) {
            if (karty.get(i).rank == Rank.ACE) AS++;
            if (karty.get(i).rank == Rank.KING) KROL++;
            if (karty.get(i).rank == Rank.QUEEN) DAMA++;
            if (karty.get(i).rank == Rank.JACK) WALET++;
            if (karty.get(i).rank == Rank.TEN) DZIESIEC++;
        }
        if ((AS == KROL && KROL == DAMA && DAMA == WALET && WALET == DZIESIEC && DZIESIEC == 1) && sameColor()) return true;
        return false;
    }
    private Rank getPoker_krolewski(){
        return Rank.ACE;
    }

    /**
     * funkcja zwracajaca najwyzsza karte
     * */
    private Rank najwyzsza(){
        int max = karty.get(0).rank.ordinal();

        for (int i = 0; i < karty.size(); i++) {
            if(karty.get(i).rank.ordinal()> max)
                max= karty.get(i).rank.ordinal();
        }
        Rank e = Rank.values()[max];
        return e;
    }

    /**
     * funckja zwracajaca rank Pary
     * */
    private Rank getPara(){
        int t[]= new int[13];
        for (int i = 0; i < karty.size(); i++) {
            int wart= karty.get(i).rank.ordinal();
            t[wart]++;
        }
        int karta_para=0;

        for(int i=0;i<13;i++){
            if(t[i]==2) karta_para=i;
        }

        Rank e = Rank.values()[karta_para];
        return e;
    }
    /**
     * funckja zwracajaca czy jest para w talii
     * */
    private boolean Para(){
        int t[]= new int[13];

        for (int i = 0; i < karty.size(); i++) {
            int wart= karty.get(i).rank.ordinal();
            t[wart]++;
        }
        for(int i=0;i<13;i++){
            if(t[i]==2) return true;
        }

        return false;
    }
    /**
     * funckja zwracajaca czy sa dwie pary
     * */
    private boolean DwiePary(){
        int t[]= new int[13];

        for (int i = 0; i < karty.size(); i++) {
            int wart= karty.get(i).rank.ordinal();
            t[wart]++;
        }
        int counter=0;

        for(int i=0;i<13;i++){
            if(t[i]==2) counter++;
        }

        if(counter==2) return true;
        return false;
    }
    /**
     * funckja zwracajaca rank pierwszej pary
     * */
    private Rank getFirstPara(){
        int t[]= new int[13];
        for (int i = 0; i < karty.size(); i++) {
            int wart= karty.get(i).rank.ordinal();
            t[wart]++;
        }
        int karta_para=0;

        for(int i=0;i<13;i++){
            if(t[i]==2) {
                karta_para = i;
                break;
            }
        }
        Rank e = Rank.values()[karta_para];
        return e;
    }
    /**
     * funckja zwracajaca rank drugiej pary
     * */
    private Rank getSecondPara(){
        int t[]= new int[14];
        for (int i = 0; i < karty.size(); i++) {
            int wart= karty.get(i).rank.ordinal();
            t[wart]++;
        }
        int karta_para=0;

        for(int i=12; i>=0 ;i--){
            if(t[i]==2) {
                karta_para = i;
                break;
            }
        }
        Rank e = Rank.values()[karta_para];
        return e;
    }

    /**
     * funckja zwracajaca rank trójki
     * */
    private Rank getTrojka(){
        int t[]= new int[14];
        for (int i = 0; i < karty.size(); i++) {
            int wart= karty.get(i).rank.ordinal();
            t[wart]++;
        }
        int karta_para=0;

        for(int i=0;i<14;i++){
            if(t[i]==3) karta_para=i;
        }

        Rank e = Rank.values()[karta_para];
        return e;
    }
    /**
     * funckja zwracajaca czy zawiera trojka
     * */
    private boolean Trojka(){
        int t[]= new int[14];

        for (int i = 0; i < karty.size(); i++) {
            int wart= karty.get(i).rank.ordinal();
            t[wart]++;
        }
        for(int i=0;i<14;i++){
            if(t[i]==3) return true;
        }

        return false;
    }
    /**
     * funckja zwracajaca czy zawiera Fulla
     * */
    private boolean Full(){
        if(Trojka() && Para()) return true;
        return false;
    }
    /**
     * funckja zwracajaca rank trojki z Fulla
     * */
    private Rank TrojkaFull(){
        int t[]= new int[14];
        for (int i = 0; i < karty.size(); i++) {
            int wart= karty.get(i).rank.ordinal();
            t[wart]++;
        }
        int karta_para=0;

        for(int i=0;i<14;i++){
            if(t[i]==3) karta_para=i;
        }
        Rank e = Rank.values()[karta_para];
        return e;
    }
    /**
     * funckja zwracajaca rank Pary z fulla
     * */
    private Rank ParaFull(){
        int t[]= new int[14];
        for (int i = 0; i < karty.size(); i++) {
            int wart= karty.get(i).rank.ordinal();
            t[wart]++;
        }
        int karta_para=0;

        for(int i=0;i<14;i++){
            if(t[i]==2) karta_para=i;
        }
        Rank e = Rank.values()[karta_para];
        return e;
    }
    /**
     * funckja zwracajaca czy zawiera karete
     * */
    private boolean Kareta(){
        int t[]= new int[14];
        for (Card card : karty) {
            int wart = card.rank.ordinal();
            t[wart]++;
        }
        for(int i=0;i<14;i++){
            if(t[i]==4) return true;
        }

        return false;
    }
    /**
     * funckja zwracajaca rank karety
     * */
    private Rank wartKareta(){
        int t[]= new int[14];
        for (Card card : karty) {
            int wart = card.rank.ordinal();
            t[wart]++;
        }
        int karta_para=0;

        for(int i=0;i<14;i++){
            if(t[i]==4) karta_para=i;
        }
        Rank e = Rank.values()[karta_para];
        return e;
    }
    /**
     * funckja zwracajaca rank dodatkowej karty w karecie
     * */
    private Rank dodatkowakartaKareta(){
        int t[]= new int[14];
        for (Card card : karty) {
            int wart = card.rank.ordinal();
            t[wart]++;
        }
        int karta_para=0;
        for(int i=0;i<14;i++){
            if(t[i]==1) karta_para=i;
        }
        Rank e = Rank.values()[karta_para];
        return e;
    }
    /**
     * funckja zwracajaca czy jest strit
     * */
    private boolean Strit(){
        return (!sameColor()) && chainowanie();
    }
    /**
     * funckja zwracajaca najwyzsza karte w stricie
     * */
    private Rank getStrit(){

        boolean hasAs = false;
        boolean hasFive = false;

        int max= karty.get(0).rank.ordinal();

        for (Card card : karty) {
            if (card.rank == Rank.ACE) hasAs = true;
            if (card.rank == Rank.FIVE) hasFive = true;
            if (card.rank.ordinal() > max) max = card.rank.ordinal();
        }
        if(hasAs && hasFive) return Rank.FIVE;
        return Rank.values()[max];
    }

    /**
     * funckja zwracajaca czy jest uklad poker
     * */
    private boolean Poker(){
        return ((sameColor()   && chainowanie())   &&  !Poker_krolewski());
    }
    /**
     * funckja zwracajaca rank ukladu poker
     * */
    private Rank getPoker(){

        boolean hasAs = false;
        boolean hasFive = false;

        int max= karty.get(0).rank.ordinal();

        for (Card card : karty) {
            if (card.rank == Rank.ACE) hasAs = true;
            if (card.rank == Rank.FIVE) hasFive = true;
            if (card.rank.ordinal() > max) max = card.rank.ordinal();
        }
        if(hasAs && hasFive) return Rank.FIVE;
        return Rank.values()[max];
    }
    /**
     * funckja zwracajaca porownujaca talie
     * */
    public boolean isGreater(Talia other) {
        TaliaType typPierwszej=this.getType();
        TaliaType typDrugiej = other.getType();

        return typPierwszej.ordinal()>typDrugiej.ordinal();

    }
    /**
     * funckja zwracajaca czy taka sama talia
     * */
    public boolean isEqual(Talia other) {
        TaliaType typPierwszej=this.getType();
        TaliaType typDrugiej = other.getType();
        return typPierwszej.ordinal()==typDrugiej.ordinal();
    }
    /**
     * enum(wyliczeniowy) z elementami wszystkich ukladow w kolejnosci od najmniej znaczacego do najbardziej
     * */
    enum TaliaType{
        Najwyzsza, Para, DwiePary, Trojka, Strit, Color, Full, Kareta, Poker, PokerKrolewski
    }

    public String toString() {
        String s = "{";
        for (int x = 0; x < karty.size(); x++) {
            Card k = karty.get(x);
            s += k.toString();
            if (x != karty.size() - 1)
                s += ',';
        }
        s += "}";
        return s;
    }
}
