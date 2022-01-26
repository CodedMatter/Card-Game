package com.company;

import java.util.List;

public class NPC extends Player{

    NPC(String name) {
        super(name);
    }

    public Card doesCardInHandMatchTable(List<Card> cardsOnTable){
        for (int i = 0; i < getHand().length; i++) {
            for (Card card : cardsOnTable){
                if(getHand()[i] != null){
                    if(card.getCardNumber() == getHand()[i].getCardNumber()){
                        return getHand()[i];
                    }
                }
            }
        }
        return null;
    }

    public void npcTurn(Main program,Table table){
        cardSelected = null;
        cardSelected = doesCardInHandMatchTable(table.getCardsOnTable());
        if(cardSelected != null){
            Card cardSelectedFromTable = null;
            for(Card card : table.getCardsOnTable()){
                if(card.getCardNumber() == cardSelected.getCardNumber()){
                    cardSelectedFromTable = card;
                    break;
                }
            }
            System.out.println(getName() + " selected " + cardSelected.printCard() +
                    "from hand and " + cardSelectedFromTable.printCard() +
                    "from Table");
            addCardToStash(cardSelected);
            addCardToStash(cardSelectedFromTable);
            table.removeCardFromTable(cardSelectedFromTable);
            removeCardFromHand(cardSelected);

            printHand();
        }else
        {
            // try adding cards together
            //else put card down
            System.out.println("NPC did not find a match in hand and table");
            System.out.println("Thinking of what card to place..");
            program.addDelay(1);

            Card smallestNumCard = null;
            for (Card card : getHand()){
                if(card != null){
                    if(smallestNumCard == null){
                        smallestNumCard = card;
                    }else{
                        if(smallestNumCard.getCardNumber() > card.getCardNumber()){
                            smallestNumCard = card;
                        }
                    }
                }
            }
            System.out.println("Selected to place" + smallestNumCard.printCard() + "on table");
            table.addCardToTable(smallestNumCard);
            removeCardFromHand(smallestNumCard);
            printHand();
        }
    }
}
