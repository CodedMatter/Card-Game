package com.company;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private Card[] hand = new Card[4];
    public List<Card> stash = new ArrayList<>();
    public Card cardSelected;

    Player (String name){
        playerName = name;
    }

    public Card[] getHand() {
        return hand;
    }

    public Card getCardSelected() {
        return cardSelected;
    }

    public String getName(){
        return playerName;
    }

    public void addCardToStash(Card cardToAdd){
        stash.add(cardToAdd);
    }

    public Card checkTopCardInStash(List<Card> stashToCheck){
        return stashToCheck.get(stashToCheck.size()-1);
    }

    public void removeStash(){
        stash.clear();
    }

    public int checkHowManyCardsInStash(){
        return stash.size();
    }

    public void setPlayerHand(Card[] cardToAddToHand){
        for (int i = 0; i < cardToAddToHand.length; i++) {
            hand[i] = cardToAddToHand[i];
        }
    }

    public void removeCardFromHand(Card cardToRemove){
        int indexOfCardToRemove = -1;
        for (int i = 0; i < hand.length; i++) {
            if(hand[i] == cardToRemove){
                indexOfCardToRemove = i;
                break;
            }
        }
        if(indexOfCardToRemove == -1){
            System.out.println("Card to remove from player hand was not found!!");
            System.out.println("Card to remove : " + cardToRemove.printCard());
        }
        else{
            hand[indexOfCardToRemove] = null;

            Card[] newCardArray = new Card[numOfCardsInHand()];
            for (Card card : hand){
                if(card != null){
                    for (int i = 0; i < newCardArray.length; i++) {
                        if(newCardArray[i] == null){
                            newCardArray[i] = card;
                            break;
                        }
                    }
                }
            }
            hand = new Card[4];
            for (int i = 0; i < newCardArray.length; i++) {
                hand[i] = newCardArray[i];
            }
        }
    }

    public int numOfCardsInHand(){
        int numOfCards = 0;
        for (Card card : hand){
            if(card != null){
                numOfCards++;
            }
        }
        return numOfCards;
    }

    public void printHand(){
        System.out.println(playerName + "'s" + " Hand");
        System.out.println("_______________________________________");
        for (Card card : hand){
            if (card != null){
                System.out.print(card.printCard());
            }

        }
        System.out.println();
        System.out.println("_______________________________________");
        System.out.println();
        if(stash.size() > 0){
            System.out.println("Stash: " + checkTopCardInStash(stash).printCard() + "Amount: " + checkHowManyCardsInStash());
        }
        else System.out.println("");
    }

    public void selectCardFromHand(int position){
        cardSelected = hand[position-1];
        System.out.println(playerName + " has selected " + cardSelected.printCard() + " from hand");
    }

    public void deselectCard(){
        cardSelected = null;
    }

}
