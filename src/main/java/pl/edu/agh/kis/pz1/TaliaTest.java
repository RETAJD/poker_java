package pl.edu.agh.kis.pz1;

import java.awt.*;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TaliaTest {

    @org.junit.jupiter.api.Test
    void isGreater() {
        Talia dwiePary = createDwiePary();
        Talia jednaPara = createJednaPara();
        String messageInCase = "Jedna jest wykrywana jako lepsza niż dwie pary. Talia jednaPara: " + jednaPara.toString() + ", dwiePary: " + dwiePary.toString();
        assertFalse(jednaPara.isGreater(dwiePary), messageInCase);
        assertTrue(dwiePary.isGreater(jednaPara), messageInCase);
    }
    /**
     * Test sprawdzajacy czy dziala metoda chainowanie - sprawdza czy sa to kolejne karty, czy strit
     * */
    @org.junit.jupiter.api.Test
    void chainowanie() {
        Talia chain = new Talia();
        chain.karty.add(Card.fromString("7♥"));
        chain.karty.add(Card.fromString("9♦"));
        chain.karty.add(Card.fromString("5♥"));
        chain.karty.add(Card.fromString("6♥"));
        chain.karty.add(Card.fromString("8♦"));
        assertTrue(chain.chainowanie(),"Chainowanie nie wykryte w zestawie"+chain.toString());

        Talia chain2 = new Talia();
        chain2.karty.add(Card.fromString("1♥"));
        chain2.karty.add(Card.fromString("J♦"));
        chain2.karty.add(Card.fromString("A♥"));
        chain2.karty.add(Card.fromString("Q♦"));
        chain2.karty.add(Card.fromString("K♥"));
        assertTrue(chain2.chainowanie(),"Chainowanie nie wykryte w zestawie"+chain.toString());

        Talia noChain = new Talia();
        noChain.karty.add(Card.fromString("1♦"));
        noChain.karty.add(Card.fromString("J♥"));
        noChain.karty.add(Card.fromString("A♦"));
        noChain.karty.add(Card.fromString("2♥"));
        noChain.karty.add(Card.fromString("K♦"));
        assertFalse(noChain.chainowanie(),"Wykryto chainowanie mimo braku w zestawie"+chain.toString());

        Talia edge = new Talia();
        edge.karty.add(Card.fromString("4♥"));
        edge.karty.add(Card.fromString("3♦"));
        edge.karty.add(Card.fromString("A♥"));
        edge.karty.add(Card.fromString("2♦"));
        edge.karty.add(Card.fromString("5♥"));
        assertTrue(edge.chainowanie(),"Chainowanie nie wykryte w zestawie"+chain.toString());

        Talia thorughEdge = new Talia();
        thorughEdge.karty.add(Card.fromString("Q♥"));
        thorughEdge.karty.add(Card.fromString("A♦"));
        thorughEdge.karty.add(Card.fromString("K♥"));
        thorughEdge.karty.add(Card.fromString("2♦"));
        thorughEdge.karty.add(Card.fromString("3♥"));
        assertFalse(thorughEdge.chainowanie(),"Chainowanie przez edge jest niedopuszczalne w zestawie"+chain.toString());
    }

    Talia losowaTalia() {
        Talia jednaPara = new Talia();
        Card c;
        Rank r;

        while (jednaPara.karty.size() < 5) {
            c = getRandomCard();
            while (jednaPara.hasCard(c))
                c = getRandomCard();
            jednaPara.karty.add(c);
        }
        return jednaPara;
    }

    Talia createJednaPara() {
        Talia jednaPara = new Talia();
        Card c;
        Rank r = Rank.values()[randomRange(0, 13)];

        c = getRandomCard(r);
        jednaPara.karty.add(c);

        c = c.clone();
        c.suit = otherSuit(c.suit);
        jednaPara.karty.add(c);

        while (jednaPara.karty.size() < 5) {
            c = getRandomCard(otherRank(r));
            while (jednaPara.hasCard(c))
                c = getRandomCard(otherRank(r));
            jednaPara.karty.add(c);
        }
        return jednaPara;
    }

    Talia createDwiePary() {
        Talia dwiePary = new Talia();
        Card c=getRandomCard();
        Rank r = c.rank;

        dwiePary.karty.add(c);

        c = c.clone();
        c.suit = otherSuit(c.suit);
        dwiePary.karty.add(c);

        r = otherRank(r);

        c = getRandomCard(r);
        dwiePary.karty.add(c);

        c = c.clone();
        c.suit = otherSuit(c.suit);
        dwiePary.karty.add(c);

        while (dwiePary.karty.size() < 5) {
            c = getRandomCard(otherRank(r));
            while (dwiePary.hasCard(c))
                c = getRandomCard(otherRank(r));
            dwiePary.karty.add(c);
        }
        return dwiePary;
    }

    Card getRandomCard() {
        return new Card(Rank.values()[randomRange(0, 13)], Suit.values()[randomRange(0, 4)]);
    }

    Card getRandomCard(Rank forcedRank) {
        return new Card(forcedRank, Suit.values()[randomRange(0, 4)]);
    }

    Rank otherRank(Rank r) {
        Rank nr = Rank.values()[randomRange(0, 13)];
        while (nr == r)
            nr = Rank.values()[randomRange(0, 13)];
        return nr;
    }

    Suit otherSuit(Suit s) {
        Suit ns = Suit.values()[randomRange(0, 4)];
        while (ns == s)
            ns = Suit.values()[randomRange(0, 4)];
        return ns;
    }

    Random random = new Random();

    public int randomRange(int min, int max) {
        return random.nextInt(max - min) + min;
    }

}