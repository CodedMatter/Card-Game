import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<Card> deck = new ArrayList<>();
    Random random = new Random();

    Deck(){
        createDeck();
    }

    public void createDeck(){
        for (int i = 0; i < 13; i++) {
            deck.add(new Card(i+1,CardType.CLUBS));
            deck.add(new Card(i+1,CardType.HEARTS));
            deck.add( new Card(i+1,CardType.DIAMONDS));
            deck.add(new Card(i+1,CardType.SPADES));
        }
    }

    public void shuffleDeck() {
        List<Card> shuffledDeck = new ArrayList<>();

        while(deck.size() > 0){
            Card card = deck.get(random.nextInt(deck.size()));
            deck.remove(card);
            shuffledDeck.add(card);
        }
        deck.addAll(shuffledDeck);
    }

    private Card drawACard(){
        Card card = deck.get(deck.size()-1);
        deck.remove(card);
        return card;
    }

    public int amountOfCardsInDeck(){
        return deck.size();
    }

    public Card[] drawThisManyCards(int numOfCards){
        Card[] cardsDrawn = new Card[numOfCards];
        for (int i = 0; i < numOfCards; i++) {
            cardsDrawn[i] = drawACard();
        }
        return cardsDrawn;
    }

}
