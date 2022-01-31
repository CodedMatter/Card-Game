package com.company;

import java.util.ArrayList;
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
            program.addDelay(2);
            addCardToStash(cardSelected);
            addCardToStash(cardSelectedFromTable);
            table.removeCardFromTable(cardSelectedFromTable);
            removeCardFromHand(cardSelected);

            //printHand();
        }else
        {
            // try adding cards together
            List<Card> cardsThatSumUp = new ArrayList<>();
            for (Card card : getHand()){
                if(card != null){
                    if(checkTableForSumMatches(table,card).size() != 0){
                        cardsThatSumUp.addAll(checkTableForSumMatches(table,card));
                        cardSelected = card;
                        break;
                    }
                }

            }

            if (cardsThatSumUp.size() != 0){
                System.out.println(getName() + " Found cards that add up to: " + cardSelected.printCard());
                program.addDelay(2);
                addCardToStash(cardSelected);
                for(Card card : cardsThatSumUp){
                    table.removeCardFromTable(card);
                }
                addCardToStash(cardSelected);
                removeCardFromHand(cardSelected);
                //printHand();
            }
            else{
                //else put card down
                System.out.println("NPC did not find a match in hand and table");
                System.out.println("Thinking of what card to place..");
                program.addDelay(2);

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
                //printHand();
            }
        }
        printHand();
    }

    public List<Card> checkTableForSumMatches(Table table,Card cardFromHand){

        // copy the table list to a new list to cycle through
        // while the new table list is greater than 0
        // create a list to store cards to count
        // add the first card to the stored card list
        // sum up each individual card on table to the sum of the card in stored list
        // when all cards are cycled through add the next card on the table to the stored card list
        // add each individual card on table to the sum of cards in stored card list
        // repeat till all cards are added to stored card list
        // then place all cards back into the new table list and remove the first card.
        // repeat until there are no more cards on the table list

        List<Card> tableCardsToLoopThrough = new ArrayList<>();
        List<Card> storedCards = new ArrayList<>();
        List<Card> cardsThatWhereMatch = new ArrayList<>();
        int sum = 0;
        // add card number that im trying to match default
        System.out.println("Card that you are trying to match is: " + cardFromHand.printCard());
        boolean hasFoundMatch = false;

        for (int i = 0; i < table.getCardsOnTable().size(); i++) {

            System.out.println();

            for (int j = i; j < table.getCardsOnTable().size(); j++) {
                tableCardsToLoopThrough.add(table.getCardsOnTable().get(j));
            }

            System.out.println("Table to loop through" + tableCardsToLoopThrough);
            storedCards = new ArrayList<>();

            while (tableCardsToLoopThrough.size() > 0){
                storedCards.add(tableCardsToLoopThrough.get(0));
                tableCardsToLoopThrough.remove(0);

                System.out.println("Cards in Stored List: " + storedCards);

                sum = 0;
                for (Card card: storedCards){
                    sum += card.getCardNumber();
                }

                System.out.println("Sum of those cards: " + sum);

                for (Card card : tableCardsToLoopThrough){
                    System.out.println("Card being counted " + card.printCard());
                    int newSum = sum + card.getCardNumber();
                    System.out.println("New sum: " + newSum);
                    if (newSum == cardFromHand.getCardNumber()){
                        System.out.println("Found Match");
                        hasFoundMatch = true;
                        cardsThatWhereMatch.addAll(storedCards);
                        cardsThatWhereMatch.add(card);
                        System.out.println("Exiting loop since match was found");
                        break;
                    }
                    System.out.println("Card dont match");
                }
                if(hasFoundMatch){
                    break;
                }
            }
            if(hasFoundMatch) {
                break;
            }
        }

        System.out.println("Cards that match where: ");
        for (Card card : cardsThatWhereMatch){
            card.printCard();
        }
        System.out.println("Sum is: " + sum);

        return cardsThatWhereMatch;
    }
}
