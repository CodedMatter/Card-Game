package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // set up game
        Main program = new Main();
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to Card Game!");
        System.out.print("Enter players name: ");
        Player player = new Player(input.nextLine());
        Deck deck = new Deck();
        Table table = new Table();
        boolean isGameEnded = false;

        // shuffle the deck
        deck.shuffleDeck();

        // add card to player and table
        for (Card card : deck.drawThisManyCards(4)){
            table.addCardToTable(card);
        }
        player.setPlayerHand(deck.drawThisManyCards(4));

        // Begin the game
        int turn = 1;


        while (!isGameEnded){
            program.printOutGame(table,player);

            System.out.println("This is Turn " + turn);

            // ask player to choose what they want to do
            program.askPlayerToMakeChoice(table,player,input);

            System.out.println("Turn " + turn + " has ended");

            turn++;
            if(turn > 4){
                if(deck.amountOfCardsInDeck() == 0){
                    isGameEnded = true;
                    System.out.println("Game has ended!!");
                    System.out.println("Player had a stack of " + player.checkHowManyCardsInStash());
                }
                else {
                    player.setPlayerHand(deck.drawThisManyCards(4));
                    turn = 1;
                    isGameEnded=false;
                    System.out.println("Next turn is " + turn);
                }
                System.out.println("Cards left in deck: " + deck.amountOfCardsInDeck());
            }
        }
    }

    public void printOutGame(Table table, Player player){
        table.printTable();
        player.printHand();
    }

    public void askPlayerToMakeChoice(Table table, Player player, Scanner input ){

        // ask player what action they want to take
        System.out.println("Do you want to " +
                "\n(1) select a single card " +
                "\n(2) add cards together" +
                "\n(3) put a card on table");

        int response = Integer.parseInt(input.nextLine());

        if(response != 1 && response != 2 && response != 3){
            System.out.println("You need to input a valid choice.");
            askPlayerToMakeChoice(table,player,input);
            return;
        }

        boolean isIndexValid = false;
        while (!isIndexValid){
            // ask player what card they want to select
            System.out.print("Select a Card from Hand you are trying to match (1-" + player.numOfCardsInHand() + ") :");
            int selectedCardIndex = Integer.parseInt(input.nextLine());

            if(selectedCardIndex > player.numOfCardsInHand()){
                System.out.println("Need to enter a valid card position to continue.");
                isIndexValid = false;
            }
            else {
                isIndexValid = true;
                player.selectCardFromHand(selectedCardIndex);
            }
        }

        switch (response){
            case 1:
                choiceOne(response,table,player,input);
                break;
            case 2:
                choiceTwo(table,player,input);
                break;
            case 3:
                choiceThree(table,player);
                break;
            default:
                System.out.println("Wrong Choice Made It Through!!!! Check Code :O :(");
                break;
        }
    }

    public void choiceOne(int response, Table table, Player player, Scanner input) {
        boolean isIndexValid = false;
        while (!isIndexValid){
            System.out.println("Select the card from the table (1-" + table.getCardsOnTable().size() + ")");
            response = Integer.parseInt(input.nextLine());

            if(response > table.getCardsOnTable().size()){
                System.out.println("Need to enter a valid card position to continue.");
                isIndexValid = false;
            }
            else {
                isIndexValid = true;

            }
        }

        Card cardOnTable = table.getCardsOnTable().get(response - 1);
        System.out.println("Card on table selected: " + cardOnTable.printCard());
        System.out.println("Number of card in hand is: " + player.cardSelected.getCardNumber());
        System.out.println("Number of card selected on table is: " + cardOnTable.getCardNumber());

        if(player.cardSelected.getCardNumber() == cardOnTable.getCardNumber()){
            System.out.println("There numbers match!!");
            player.addCardToStash(cardOnTable);
            player.addCardToStash(player.cardSelected);
            System.out.println("Number of cards in stash: " + player.stash.size());
            System.out.println("Card on top of stash: " + player.checkTopCardInStash(player.stash).printCard());
            player.removeCardFromHand(player.cardSelected);
            table.removeCardFromTable(cardOnTable);
        }
        else{
            System.out.println("Those cards don't match. Select different options.");
            System.out.println();
            askPlayerToMakeChoice(table,player,input);
        }
    }

    public void choiceTwo(Table table,Player player, Scanner input){
        int sumOfCards = 0;
        boolean isValidIndex = false;
        String[] responseInArray = new String[0];

        while (!isValidIndex){
            System.out.println("Select the position of the cards you want to add together (1-" + table.getCardsOnTable().size() + "):");
            System.out.print("Leave a space between cards. EX: 1 3 5 : ");
            String responseString = input.nextLine();
            responseInArray = responseString.split(" ");

            boolean areNumbersInArrayValid = false;
            for(String num : responseInArray){
                if(Integer.parseInt(num) > table.getCardsOnTable().size()){
                    areNumbersInArrayValid = false;
                    break;
                }else{
                    areNumbersInArrayValid = true;
                }
            }

            if(areNumbersInArrayValid){
                isValidIndex = true;
                sumOfCards = addCardNumbers(responseInArray,table);
            }
            else{
                System.out.println("The card positions you choose are invalid. Try again.");
                System.out.println();
                isValidIndex = false;
            }
        }

        if(sumOfCards == player.cardSelected.getCardNumber() || player.cardSelected.getCardNumber() == 1 && sumOfCards == 14){
            System.out.println("Cards selected from table sum up to your card!!!! ");
            Card[] cardsSelectedOnTable = getCardsOnTableByIndexes(responseInArray,table);
            for (Card card : cardsSelectedOnTable){
                player.addCardToStash(card);
                table.removeCardFromTable(card);
            }
            player.addCardToStash(player.cardSelected);
            player.removeCardFromHand(player.cardSelected);
            System.out.println("Number of cards in stash: " + player.stash.size());
            System.out.println("Card on top of stash: " + player.checkTopCardInStash(player.stash).printCard());
        }
        else{
            System.out.println("Cards selected from table dint match up to your card!!! :(");
            askPlayerToMakeChoice(table,player,input);
        }
    }

    public void choiceThree(Table table, Player player){
        System.out.println("Player placed " + player.cardSelected.printCard() + " on table");
        table.addCardToTable(player.cardSelected);
        player.removeCardFromHand(player.cardSelected);
    }

    public int addCardNumbers(String[] numOfCardsToAdd, Table table){
        Card[] cardsSelected = getCardsOnTableByIndexes(numOfCardsToAdd, table);

        int sumOfCards = 0;
        System.out.print("Card from table selected : ");
        for ( Card card : cardsSelected){
            System.out.print(card.printCard());
            sumOfCards += card.getCardNumber();
        }
        System.out.println(" = " + sumOfCards);
        return sumOfCards;

    }

    public Card[] getCardsOnTableByIndexes(String[] numOfCardsToAdd, Table table){
        Card[] cardsSelected = new Card[numOfCardsToAdd.length];
        for (int i = 0; i < numOfCardsToAdd.length; i++) {
            // subtract one from index since player pick starting from 1 not 0
            cardsSelected[i] = table.getCardsOnTable().get(Integer.parseInt(numOfCardsToAdd[i])-1);
        }
        return cardsSelected;
    }
}
