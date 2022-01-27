package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // set up game
        Main program = new Main();
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to Card Game!");

        //Ask how many players
        // for now default to 2
        int numOfPlayers = 2;

        System.out.print("Enter players name: ");
        Player player = new Player(input.nextLine());

        // add player and create the necessary npcs to playerOrder
        NPCNameLibrary npcNameLibrary = new NPCNameLibrary();
        List<Player> playerOrder = new ArrayList<>();
        playerOrder.add(player);

        for (int i = 0; i < numOfPlayers-1; i++) {
            NPC npc = new NPC(npcNameLibrary.getRandomName());
            playerOrder.add(npc);
        }

        Deck deck = new Deck();
        Table table = new Table();
        boolean isGameEnded = false;

        // shuffle the deck
        deck.shuffleDeck();

        // add card to each player and table
        for (Card card : deck.drawThisManyCards(4)){
            table.addCardToTable(card);
        }

        for (Player p : playerOrder){
            p.setPlayerHand(deck.drawThisManyCards(4));
            //System.out.println(p.getName() + " received " + p.numOfCardsInHand() + " cards");
            //System.out.println("Cards in Deck left: " + deck.amountOfCardsInDeck());
        }


        // Begin the game
        int turn = 1;

        System.out.println("========================================");
//        // try adding cards together
//        boolean canCardsCanBeAdded = false;
//        Card[] cardsToBeAdded = new Card[0];
//
//        for (Card cardInHand : player.getHand()) {
//            System.out.println("Card to match: " + cardInHand.printCard());
//            int sum = 0;
//            ///////////////////////////////////////////////////
//
//            int count = 2;
//            while(count < table.getCardsOnTable().size()){
//                for (int i = 0; i < table.getCardsOnTable().size(); i++) {
//                    System.out.println();
//                    for (int j = 0; j < count; j++) {
//                        cardsToBeAdded = new Card[count];
//                        if(i+j<=table.getCardsOnTable().size()){
//                            sum += table.getCardsOnTable().get(i+j).getCardNumber();
//                            cardsToBeAdded[j] = table.getCardsOnTable().get(i+j);
//                            System.out.print(table.getCardsOnTable().get(i+j).printCard());
//                        }
//                    }
//                    System.out.println("sum is: " + sum);
//                    if(sum == cardInHand.getCardNumber()){
//                        canCardsCanBeAdded = true;
//                        System.out.println("Found cards that add up to selected card");
//                        break;
//                    }
//                    else {
//                        sum=0;
//                    }
//
//                }
//                if(canCardsCanBeAdded){
//                    System.out.println(cardsToBeAdded.length);
//                    for (Card card : cardsToBeAdded){
//                        System.out.print(card.printCard());
//                    }
//                    break;
//                }
//                count++;
//            }
//            System.out.println("canCardsCanBeAdded: " + canCardsCanBeAdded);
//
//            ///////////////////////////////////////////////////
//
//            // add all the cards together
//            // add all variation of three cards
//            // add all variation of two cards
//        }

        //program.checkWhatCardsInTableSumUpToo(table,player);
        System.out.println("========================================");

        // game loop
        while (!isGameEnded){

            System.out.println("Turn " + turn + " has stared");
            int indexOfCurrentPlayerTurn = 0;

            while (indexOfCurrentPlayerTurn < playerOrder.size()){

                if(playerOrder.get(indexOfCurrentPlayerTurn) instanceof NPC){
                    System.out.println("Its NPCs Turn");
                    System.out.println(playerOrder.get(indexOfCurrentPlayerTurn).getName() + " is thinking ...");
                    program.addDelay(2);
                    ((NPC) playerOrder.get(indexOfCurrentPlayerTurn)).npcTurn(program,table);
                    // if npc has a card that matches select choice one
                    // else see if they can add up to a card in there hand select choice 2
                    // else put the lowest card down on table
                }
                else{
                    program.printOutGame(table,player);

                    // ask player to choose what they want to do
                    program.askPlayerToMakeChoice(table,player,input);
                }
                indexOfCurrentPlayerTurn++;
            }

            System.out.println("Turn " + turn + " has ended");
            turn++;

            if(turn > 4){
                if(deck.amountOfCardsInDeck() == 0){
                    isGameEnded = true;
                    System.out.println("Game has ended!!");
                    System.out.println("Player had a stack of " + player.checkHowManyCardsInStash());
                }
                else {
                    for (Player p : playerOrder){
                        p.setPlayerHand(deck.drawThisManyCards(4));
                        //System.out.println(p.getName() + " received " + p.numOfCardsInHand() + " cards");
                        //System.out.println("Cards in Deck left: " + deck.amountOfCardsInDeck());
                    }
                    turn = 1;
                    isGameEnded=false;
                    System.out.println("Next turn is " + turn);
                }
                System.out.println("Cards left in deck: " + deck.amountOfCardsInDeck());
            }
        }
    }

    public void checkWhatCardsInTableSumUpToo(Table table, Player player){
        for (Card cardSelectedFromHand : player.getHand()) {

            System.out.println("Card trying to match is: " + cardSelectedFromHand.printCard());

            for (int i = 0; i < table.getCardsOnTable().size(); i++) {

                int sum = 0;

                for (int j = i + 1; j < table.getCardsOnTable().size(); j++) {

                    System.out.print(table.getCardsOnTable().get(i).printCard() + "+");
                    System.out.print(table.getCardsOnTable().get(j).printCard() + ":");

                    sum += table.getCardsOnTable().get(i).getCardNumber()
                            + table.getCardsOnTable().get(j).getCardNumber();

                    System.out.println(sum);

                    if (sum == cardSelectedFromHand.getCardNumber()) {
                        System.out.println("cards add up to selected card");
                    }
                }
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



        int response;
        try {
            response = Integer.parseInt(input.nextLine().trim());
        }catch (Exception e){
            response = -1;
        }

        if(response != 1 && response != 2 && response != 3){
            System.out.println("You need to input a valid choice.");
            askPlayerToMakeChoice(table,player,input);
            return;
        }

        boolean isIndexValid = false;
        while (!isIndexValid){
            // ask player what card they want to select
            System.out.print("Select a Card from Hand you are trying to match (1-" + player.numOfCardsInHand() + ") :");

            int selectedCardIndex;
            try {
                selectedCardIndex = Integer.parseInt(input.nextLine().trim());
            } catch (Exception e){
                selectedCardIndex = 1000;
            }

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
            System.out.print("Select the card from the table (1-" + table.getCardsOnTable().size() + ")");
            try {
                response = Integer.parseInt(input.nextLine().trim());
            }catch (Exception e){
                response = 1000;
            }

            if(response > table.getCardsOnTable().size()){
                System.out.println("Need to enter a valid card position to continue.");
                isIndexValid = false;
            }
            else {
                isIndexValid = true;

            }
        }

        Card cardOnTable = table.getCardsOnTable().get(response - 1);
        //System.out.println("Card on table selected: " + cardOnTable.printCard());
        //System.out.println("Number of card in hand is: " + player.cardSelected.getCardNumber());
        //System.out.println("Number of card selected on table is: " + cardOnTable.getCardNumber());

        if(player.cardSelected.getCardNumber() == cardOnTable.getCardNumber()){
            //System.out.println("There numbers match!!");
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
            //System.out.println("Cards selected from table sum up to your card!!!! ");
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

    public void addDelay(int seconds){
        try {
            Thread.sleep(((int)seconds*1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
