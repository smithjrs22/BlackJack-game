import java.util.Scanner;

public class Game {

    //fields needed for Game class
    private Deck deck, discarded;
    private Dealer dealer;
    private Player player;
    private int wins, losses, ties;

    //constructor
    public Game(){

        //Create a new deck with 52 cards
        deck = new Deck(true);
        //Create a new empty deck
        discarded = new Deck();

        //Create the People player or dealer
        dealer = new Dealer();
        player = new Player();

        //Shuffle the deck and start the first round
        deck.shuffle();
        startRound();
    }


    /**
     * startRound begins a new round, has score display, distributes card, checks to see who won aks play what they want 
     */
    private void startRound(){

        //If this isn't the first round, display the users score and put their cards back in the deck
        if(wins> 0 || losses> 0 || ties > 0){
            System.out.println();
            System.out.println("Starting Next Round... Wins: " + wins + " Losses: "+ losses+ " Ties: "+ties);
            dealer.getHand().discardHandToDeck(discarded);
            player.getHand().discardHandToDeck(discarded);
        }

        //Check to make sure the deck has at least 4 cards left to start
        if(deck.cardsLeft() < 4){
            //reload the deck from discard pile if we're out of cards
            deck.reloadDeckFromDiscard(discarded);
        }

        //Give the dealer two cards
        dealer.getHand().takeCardFromDeck(deck);
        dealer.getHand().takeCardFromDeck(deck);

        //Give the player two cards
        player.getHand().takeCardFromDeck(deck);
        player.getHand().takeCardFromDeck(deck);

        //Show the dealers hand with one card face down
        dealer.printFirstHand();

        //Show the player's hand
        player.printHand();

        //Check if dealer has BlackJack to start
        if(dealer.hasBlackjack()){
            //Show the dealer has BlackJack
            dealer.printHand();

            //Check if the player also has BlackJack
            if(player.hasBlackjack()){
                //End the round with a push
                System.out.println("You both have 21 - Tie.");
                ties++;
                //start a new round
                startRound();
            }
            else{
                System.out.println("Dealer has BlackJack. You lose.");
                dealer.printHand();
                losses++;
                //player lost, start a new round
                startRound();
            }
        }

        //Check if player has blackjack to start
        if(player.hasBlackjack()){
            System.out.println("You have Blackjack! Congrats. You win!");
            wins++;
            startRound();
        }

        //Let the player decide what to do next, hit or stand
        //pass the decks in case they decide to hit
        player.makeDecision(deck, discarded);

        //Check if they busted
        if(player.getHand().calculatedValue() > 21){
            System.out.println("You have gone over 21.");
            losses ++;
            startRound();
        }

        //Now it's the dealer's turn
        dealer.printHand();
        while(dealer.getHand().calculatedValue()< 17){
            dealer.hit(deck, discarded);
        }

        //Check who wins and count wins or losses
        if(dealer.getHand().calculatedValue()> 21){
            System.out.println("Dealer busts");
            wins++;
        }
        else if(dealer.getHand().calculatedValue() > player.getHand().calculatedValue()){
            System.out.println("You lose.");
            losses++;
        }
        else if(player.getHand().calculatedValue() > dealer.getHand().calculatedValue()){
            System.out.println("You win.");
            wins++;
        }
        else{
            System.out.println("Push.");
            ties++;
        }

        //Start a new round
        startRound();
    }

    /**
     * Brief pause for the game
     */
    public static void pause(){
    try {
        Thread.sleep(5000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
}

