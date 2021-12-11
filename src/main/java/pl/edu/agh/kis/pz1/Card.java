package pl.edu.agh.kis.pz1;

public class Card {

    public Rank rank;
    public Suit suit;

    public Card(Rank r, Suit s){
        this.rank = r;
        this.suit = s;
    }
    /**
     * Funkcja porownująca czy dwie karty są takie same
     * */
    public boolean SameRank(Card other){
        return rank==other.rank;
    }
    public boolean SameSuit(Card other){
        return suit==other.suit;
    }
    public Card clone(){
        return new Card(rank,suit);
    }

    /**
     * Publiczna metoda boolowska która jako argument przyjmuje Card
     * działanie: sprawdza czy karta jest o 1 wartoscia mniejsza od drugiej wtedy zwraca true
     * w przeciwnym wypadku zwraca falsz
     * zrobiony dodadkowo punkt z tym ze AS moze być jako jedynka i wraz z dwojka tworzą kolejne karty
     * */
    public boolean IsOneBelow(Card other){
        int wart= rank.ordinal();
        int wart2 = other.rank.ordinal();
        if(wart2 - wart == 1)
            return true;
        else if(rank==Rank.ACE && other.rank==Rank.TWO)
            return true;
        return false;
    }
    /* public boolean samePhysicalColor(Card other){
        if ((kolor == 1 || kolor == 2) && (other.kolor == 1 || other.kolor == 2))
            return true;
        else if ((kolor == 3 || kolor == 4) && (other.kolor == 3 || other.kolor == 4))
            return true;
        return false;
    } */

    @Override
    /**
     *  funkcja porównująca dwie karty ( rank i suit )
     *  przyjmująca jako argument Object o
     * */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    /**
     * Metoda publiczna która konwertuje Suit i Rank  na jeden string  i go zwraca
     * string ma postac [wartosc_karty][kolor_karty]
     * */
    public String toString(){
        String s="";
        //wartosci
        String two="2";
        String three="3";
        String four="4";
        String five="5";
        String six="6";
        String seven="7";
        String eight="8";
        String nine="9";
        String ten="1";
        String walet="J";
        String dama="Q";
        String krol="K";
        String as="A";

        String serce ="♥";
        String dzwonek ="♦";
        String zoladz ="♣";
        String wino ="♠";

        if(suit==Suit.HEART)  s = s +serce;
        if(suit==Suit.DIAMOND)  s = s +dzwonek;
        if(suit==Suit.CLUB)  s = s +zoladz;
        if(suit==Suit.SPADE)  s = s +wino;

        if(rank == Rank.TWO) s = two + s;
        if(rank == Rank.THREE) s = three + s;
        if(rank == Rank.FOUR) s = four + s;
        if(rank == Rank.FIVE) s = five + s;
        if(rank == Rank.SIX) s = six + s;
        if(rank == Rank.SEVEN) s = seven + s;
        if(rank == Rank.EIGHT) s = eight + s;
        if(rank == Rank.NINE) s = nine + s;
        if(rank == Rank.TEN) s = ten + s;
        if(rank == Rank.JACK) s = walet + s;
        if(rank == Rank.QUEEN) s = dama + s;
        if(rank == Rank.KING) s = krol + s;
        if(rank == Rank.ACE) s = as + s;

        return s;
    }

    /**
     * Publiczna statyczna metoda fromString która ma za zadanie zwrócić null w poszczególnych warunkach
     * oraz konwertuje argument przez nią przyjmowany(string) na rank,suit i zwraca jako Card
     * */
    public static Card fromString(String s){
        if(s.length()<2)return null;
        if(!("234567891JQKA").contains(String.valueOf(s.charAt(0))))
            return null;
        if(!("♥♦♣♠").contains(String.valueOf(s.charAt(1))))
            return null;

        Rank rank=Rank.FOUR;
        Suit suit=Suit.CLUB;
        if( s.charAt(0) == '2' ) rank=Rank.TWO;
        if( s.charAt(0) == '3' ) rank=Rank.THREE;
        if( s.charAt(0) == '4' ) rank=Rank.FOUR;
        if( s.charAt(0) == '5' ) rank=Rank.FIVE;
        if( s.charAt(0) == '6' ) rank=Rank.SIX;
        if( s.charAt(0) == '7' ) rank=Rank.SEVEN;
        if( s.charAt(0) == '8' ) rank=Rank.EIGHT;
        if( s.charAt(0) == '9' ) rank=Rank.NINE;
        if( s.charAt(0) == '1' ) rank=Rank.TEN;
        if( s.charAt(0) == 'J' ) rank=Rank.JACK;
        if( s.charAt(0) == 'Q' ) rank=Rank.QUEEN;
        if( s.charAt(0) == 'K' ) rank=Rank.KING;
        if( s.charAt(0) == 'A' ) rank=Rank.ACE;

        if( s.charAt(1) == '♥') suit= Suit.HEART;
        if( s.charAt(1) == '♦') suit= Suit.DIAMOND;
        if( s.charAt(1) == '♣') suit= Suit.CLUB;
        if( s.charAt(1) == '♠') suit= Suit.SPADE;


        return new Card(rank,suit);
    }
}