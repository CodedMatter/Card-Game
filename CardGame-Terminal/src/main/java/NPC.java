import java.util.ArrayList;
import java.util.List;

public class NPC extends Player{

    NPC(String name) {
        super(name);
    }

    public void npcTurn(Main program,Table table, List<Player> playerList){

        printHand();
        // check if npc can steal anyone's stash
        Player playerStashToSteal = doesAnyStackMatchCardInHand(playerList);

        if(playerStashToSteal != null){
            System.out.println(getName() + " decided to steal " + playerStashToSteal.getName() + "'s stash");
            for (Card card : getHand()){
                if(card.getCardNumber() == playerStashToSteal.checkTopCardInStash(playerStashToSteal.stash).getCardNumber()){
                    cardSelected = card;
                    break;
                }
            }
            stash.addAll(playerStashToSteal.stash);
            playerStashToSteal.removeStash();
            stash.add(getCardSelected());
            removeCardFromHand(getCardSelected());
            return;
        }

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
        }else {
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

                for(Card card : cardsThatSumUp){
                    addCardToStash(card);
                    table.removeCardFromTable(card);
                }
                addCardToStash(cardSelected);
                removeCardFromHand(cardSelected);

            }
            else{
                //else put card down
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
            }
        }
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

    public Player doesAnyStackMatchCardInHand(List<Player> playerList){

        Player playerStashToSteal= null;
        boolean hasFoundStashToSteal = false;
        for (Player player : playerList){
            if(player != this){
                for (Card card : getHand()){
                    if(card != null){
                        if(player.stash.size() != 0){
                            if(card.getCardNumber() == player.checkTopCardInStash(player.stash).getCardNumber()){
                                playerStashToSteal = player;
                                hasFoundStashToSteal = true;
                                break;
                            }
                        }
                    }
                }
                if(hasFoundStashToSteal){
                    break;
                }
            }
        }
        if(hasFoundStashToSteal){
            return playerStashToSteal;
        }
        else{
            return null;
        }
    }

    public List<Card> checkTableForSumMatches(Table table,Card cardFromHand){

        List<Card> tableCardsToLoopThrough = new ArrayList<>();
        List<Card> storedCards = new ArrayList<>();
        List<Card> cardsThatWhereMatch = new ArrayList<>();

        int sum = 0;

        boolean hasFoundMatch = false;

        for (int i = 0; i < table.getCardsOnTable().size(); i++) {
            for (int j = i; j < table.getCardsOnTable().size(); j++) {
                tableCardsToLoopThrough.add(table.getCardsOnTable().get(j));
            }

            storedCards = new ArrayList<>();

            while (tableCardsToLoopThrough.size() > 0){
                storedCards.add(tableCardsToLoopThrough.get(0));
                tableCardsToLoopThrough.remove(0);

                sum = 0;
                for (Card card: storedCards){
                    sum += card.getCardNumber();
                }

                for (Card card : tableCardsToLoopThrough){
                    int newSum = sum + card.getCardNumber();
                    if (newSum == cardFromHand.getCardNumber()){
                        hasFoundMatch = true;
                        cardsThatWhereMatch.addAll(storedCards);
                        cardsThatWhereMatch.add(card);
                        break;
                    }
                }
                if(hasFoundMatch){
                    break;
                }
            }
            if(hasFoundMatch) {
                break;
            }
        }

        for (Card card : cardsThatWhereMatch){
            card.printCard();
        }

        return cardsThatWhereMatch;
    }
}
