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
        }

        // Begin the game
        int turn = 1;

        // game loop
        while (!isGameEnded){

            int indexOfCurrentPlayerTurn = 0;

            while (indexOfCurrentPlayerTurn < playerOrder.size()){

                if(playerOrder.get(indexOfCurrentPlayerTurn) instanceof NPC){
                    System.out.println("Its NPCs Turn");
                    System.out.println(playerOrder.get(indexOfCurrentPlayerTurn).getName() + " is thinking ...");
                    program.addDelay(2);
                    ((NPC) playerOrder.get(indexOfCurrentPlayerTurn)).npcTurn(program,table);
                }
                else{
                    program.printOutGame(table,player,playerOrder);

                    // ask player to choose what they want to do
                    program.askPlayerToMakeChoice(table,player,input, playerOrder);
                }
                indexOfCurrentPlayerTurn++;
            }

            turn++;

            if(turn > 4){
                if(deck.amountOfCardsInDeck() == 0) {
                    isGameEnded = true;
                    System.out.println("Game has ended!!");
                    System.out.println("The Player that won was " + program.checkWhichPlayerWon(playerOrder).getName());
                    System.out.println("They had a stack of " + program.checkWhichPlayerWon(playerOrder).stash.size() + " cards");
                }
                else {
                    for (Player p : playerOrder){
                        p.setPlayerHand(deck.drawThisManyCards(4));
                    }
                    turn = 1;
                    isGameEnded=false;
                }
                System.out.println("Cards left in deck: " + deck.amountOfCardsInDeck());
            }
        }
    }

    public void printOutGame(Table table, Player player, List<Player> playerOrder){

        table.printTable();
        player.printHand();

        System.out.println();
        System.out.println("Stash");
        System.out.println("_______________________________________________________________________");
        for(Player p : playerOrder){
            if(p.stash.size() != 0){
                System.out.print(p.getName());
                System.out.print(p.checkTopCardInStash(p.stash).printCard() +
                        "Amount " + p.checkHowManyCardsInStash() + "          ");
            }
        }
        System.out.println();
        System.out.println("_______________________________________________________________________");
    }

    public void askPlayerToMakeChoice(Table table, Player player, Scanner input, List<Player> playerOrder){

        // ask player what action they want to take
        System.out.println("Do you want to " +
                "\n(1) select a single card " +
                "\n(2) add cards together" +
                "\n(3) put a card on table" +
                "\n(4) steal a player's stash");

        int response;
        try {
            response = Integer.parseInt(input.nextLine().trim());
        }catch (Exception e){
            response = -1;
        }

        if(response != 1 && response != 2 && response != 3 && response !=4){
            System.out.println("You need to input a valid choice. ");
            askPlayerToMakeChoice(table,player,input, playerOrder);
            return;
        }else{
            if(response == 4){
                List<Player> playersWithStash = new ArrayList<>();
                for (Player p : playerOrder){
                    if(p != playerOrder.get(0)){
                        if(p.stash.size() != 0){
                            playersWithStash.add(p);
                        }
                    }
                }
                if(playersWithStash.size() == 0){
                    System.out.println("No one has a stash to steal yet");
                    System.out.println("Pick a different option");
                    askPlayerToMakeChoice(table,playerOrder.get(0),input,playerOrder);
                    return;
                }
            }
        }

        boolean isIndexValid = false;
        while (!isIndexValid){
            // ask player what card they want to select
            System.out.print("Select a Card from Hand you are trying to match (1-" + player.numOfCardsInHand() + "): ");

            int selectedCardIndex;
            try {
                selectedCardIndex = Integer.parseInt(input.nextLine().trim());
            } catch (Exception e){
                selectedCardIndex = 1000;
            }

            if(selectedCardIndex > player.numOfCardsInHand()){
                System.out.println("Need to enter a valid card position to continue. ");
                isIndexValid = false;
            }
            else {
                isIndexValid = true;
                player.selectCardFromHand(selectedCardIndex);
            }
        }

        switch (response){
            case 1:
                choiceOne(response,table,player,input,playerOrder);
                break;
            case 2:
                choiceTwo(table,player,input,playerOrder);
                break;
            case 3:
                choiceThree(table,player);
                break;
            case 4:
                choiceFour(table,playerOrder,input);
            default:
                System.out.println("Wrong Choice Made It Through!!!! Check Code :O :( ");
                break;
        }
    }

    public void choiceOne(int response, Table table, Player player, Scanner input , List<Player> playerOrder) {
        boolean isIndexValid = false;
        while (!isIndexValid){
            System.out.print("Select the card from the table (1-" + table.getCardsOnTable().size() + ") ");
            try {
                response = Integer.parseInt(input.nextLine().trim());
            }catch (Exception e){
                response = 1000;
            }

            if(response > table.getCardsOnTable().size()){
                System.out.println("Need to enter a valid card position to continue. ");
                isIndexValid = false;
            }
            else {
                isIndexValid = true;

            }
        }

        Card cardOnTable = table.getCardsOnTable().get(response - 1);

        if(player.cardSelected.getCardNumber() == cardOnTable.getCardNumber()){
            player.addCardToStash(cardOnTable);
            player.addCardToStash(player.cardSelected);
            player.removeCardFromHand(player.cardSelected);
            table.removeCardFromTable(cardOnTable);
        }
        else{
            System.out.println("Those cards don't match. Select different options.");
            System.out.println();
            askPlayerToMakeChoice(table,player,input, playerOrder);
        }
    }

    public void choiceTwo(Table table, Player player, Scanner input, List<Player> playerOrder){
        int sumOfCards = 0;
        boolean isValidIndex = false;
        String[] responseInArray = new String[0];

        while (!isValidIndex){
            System.out.println("Select the position of the cards you want to add together (1-" + table.getCardsOnTable().size() + "): ");
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
            Card[] cardsSelectedOnTable = getCardsOnTableByIndexes(responseInArray,table);
            for (Card card : cardsSelectedOnTable){
                player.addCardToStash(card);
                table.removeCardFromTable(card);
            }
            player.addCardToStash(player.cardSelected);
            player.removeCardFromHand(player.cardSelected);
        }
        else{
            System.out.println("Cards selected from table didn't match up to your card!!! :(");
            askPlayerToMakeChoice(table,player,input, playerOrder);
        }
    }

    public void choiceThree(Table table, Player player){
        System.out.println("Player placed " + player.cardSelected.printCard() + " on table");
        table.addCardToTable(player.cardSelected);
        player.removeCardFromHand(player.cardSelected);
    }

    public void choiceFour(Table table, List<Player> playerOrder, Scanner input){
        List<Player> playerWithStashes = new ArrayList<>();
        // add npcs to the list that have stashes
        for (Player player : playerOrder){
            if(player != null){
                if(player != playerOrder.get(0)){
                    if(player.checkHowManyCardsInStash() > 0){
                        playerWithStashes.add(player);
                    }
                }
            }
        }

        boolean hasCardThatMatchesAStack = false;
        for (Card card : playerOrder.get(0).getHand()){
            if(card != null){
                for (Player player : playerWithStashes){
                    if(player.checkTopCardInStash(player.stash).getCardNumber() == card.getCardNumber()){
                        hasCardThatMatchesAStack = true;
                        break;
                    }
                }
            }

            if(hasCardThatMatchesAStack){
                break;
            }
        }

        if(hasCardThatMatchesAStack){
            // print out the stashes the player can select
            int indexOfPlayerWithStash = 0;
            for (Player player : playerOrder){
                if(player != playerOrder.get(0)){
                    System.out.println("(" + ++indexOfPlayerWithStash + ")" +player.getName() +
                            "'s Stash has " + player.checkHowManyCardsInStash() +
                            " Cards"+ player.checkTopCardInStash(player.stash).printCard());
                }
            }
        }


        boolean isIndexValid = false;
        int response;

        while (!isIndexValid){
            System.out.print("Select which Stash to steal (1-" + playerWithStashes.size() + ") ");
            try {
                response = Integer.parseInt(input.nextLine().trim());
            }catch (Exception e){
                response = 1000;
            }

            if(response > playerWithStashes.size()){
                System.out.println("Need to enter a valid card position to continue. ");
                isIndexValid = false;
            }
            else {
                isIndexValid = true;

            }
            // check if the card player selected match the the card at the top of npcs stash
            if(playerOrder.get(response).checkTopCardInStash(playerOrder.get(response).stash).getCardNumber() == playerOrder.get(0).cardSelected.getCardNumber()){
                playerOrder.get(0).stash.addAll(playerOrder.get(response).stash);
                playerOrder.get(response).removeStash();
                playerOrder.get(0).addCardToStash(playerOrder.get(0).cardSelected);
                playerOrder.get(0).removeCardFromHand(playerOrder.get(0).cardSelected);
            }else{
                System.out.println("Card selected doesn't match the card at the top of " + playerOrder.get(response).getName()+ "'s Stash");
                System.out.println();
                askPlayerToMakeChoice(table,playerOrder.get(0),input,playerOrder);
            }
        }
    }

    public int addCardNumbers(String[] numOfCardsToAdd, Table table){
        Card[] cardsSelected = getCardsOnTableByIndexes(numOfCardsToAdd, table);

        int sumOfCards = 0;
        for ( Card card : cardsSelected){
            sumOfCards += card.getCardNumber();
        }
        return sumOfCards;

    }

    public Card[] getCardsOnTableByIndexes(String[] numOfCardsToAdd, Table table){
        Card[] cardsSelected = new Card[numOfCardsToAdd.length];
        for (int i = 0; i < numOfCardsToAdd.length; i++) {
            // subtract one from index since player pick starting from 1 not 0
            cardsSelected[i] = table.getCardsOnTable().get(Integer.parseInt(numOfCardsToAdd[i]) - 1);
        }
        return cardsSelected;
    }

    public void addDelay(int seconds){
        try {
            Thread.sleep((seconds * 1000L));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Player checkWhichPlayerWon(List<Player> playerInGame){
        Player playerThatOne = new Player();
        for(Player player : playerInGame){
            if(playerThatOne == null){
                playerThatOne = player;
            }
            else if(playerThatOne.stash.size() < player.stash.size()){
                playerThatOne = player;
            }
        }
        return playerThatOne;
    }
}
