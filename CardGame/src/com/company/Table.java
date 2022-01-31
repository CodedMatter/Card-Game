package com.company;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<Card> cardsOnTable = new ArrayList<>();

    public void addCardToTable(Card cardToAdd){
        cardsOnTable.add(cardToAdd);
    }

    public void removeCardFromTable(Card cardToRemove){
        int indexOfCardToRemove = -1;
        for (int i = 0; i < cardsOnTable.size(); i++) {
            if(cardsOnTable.get(i) == cardToRemove){
                indexOfCardToRemove = i;
                break;
            }
        }
        if(indexOfCardToRemove == -1){
            System.out.println("Card to remove from table was not found!!");
            System.out.println("Card to remove : " + cardToRemove.printCard());
        }
        else{
            cardsOnTable.remove(indexOfCardToRemove);
        }
    }

    public List<Card> getCardsOnTable(){
        return cardsOnTable;
    }

    public void printTable(){
        System.out.println("Table");
        System.out.println("_______________________________________________________________________");
        for (Card card : cardsOnTable){
            if(card != null){
                System.out.print(card.printCard());
            }
        }
        System.out.println();
        System.out.println("_______________________________________________________________________");
        System.out.println();
    }
}
