package com.company;

enum CardType {
    CLUBS,
    HEARTS,
    DIAMONDS,
    SPADES
}

public class Card {
    private int num;
    private CardType type;

    Card(int cardNum,CardType cardType){
        num = cardNum;
        type = cardType;
    }

    public int getCardNumber(){
        return num;
    }

    public String printCard(){
        String cardNumToPrint;
        if(num == 1){
            cardNumToPrint = "A";
        }
        else if(num == 11){
            cardNumToPrint = "J";
        }
        else if(num == 12){
            cardNumToPrint = "Q";
        }
        else if (num == 13){
            cardNumToPrint = "K";
        }
        else {
            cardNumToPrint = "" + num;
        }

        if(type == CardType.SPADES) {
            return " |" + cardNumToPrint + (char)'\u2660' + "| ";
        }
        else if(type == CardType.DIAMONDS) {
            return " |" + cardNumToPrint + (char)'\u2666' + "| ";
        }
        else if(type == CardType.CLUBS) {
            return " |" + cardNumToPrint + (char)'\u2663' + "| ";
        }
        else if(type == CardType.HEARTS) {
            return " |" + cardNumToPrint + (char)'\u2764' + "| ";
        }
        return "ERROR!! CARD DOESN'T HAVE A TYPE HHHOOOWWWW!!!!";
    }
}
