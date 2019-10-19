import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CardMe {
		
	public static void main(String[] args) {
		
		CardData data;
		String workingSet = null;
		
		//use the default directory if one is not supplied
		String saveDirectory = System.getProperty("user.dir");
		if(args.length > 0) {
			saveDirectory = saveDirectory + args[0]; 
		}
		else{
			saveDirectory = saveDirectory + "\\DefaultCardMeSave.txt"; 
		}
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("~ CardMe system has been booted! ~");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("");
		
		//make sure the save location exists; if not, create it with a default save
		File savetxt = new File(saveDirectory);
		if(!savetxt.exists()) {
			try {
				savetxt.createNewFile();
				CardData temp = new CardData();
				temp.list.add(new Card("default set", "Is this a default card?", "Yes, it is."));
				CardDataManager.saveCardList(temp, saveDirectory);
			} 
			catch (IOException e) {e.printStackTrace();}
		}
		
		//load info and perform setup
		data = CardDataManager.loadCardList(saveDirectory);
		Scanner keyboard = new Scanner(System.in);
		String input = "";
		
		
		//display all sets initial info
		data.printSets();
		
		//begin input recognition
		while(!input.equals("quit")) {
			
			System.out.println("~~~~~~~~");
			System.out.print("COMMAND: ");
			input = keyboard.nextLine();
			input = input.toLowerCase();	
			System.out.println("~~~~~~~~");
			
			if(input.equals("help")) {
				printHelpMessage();
			}

			
			
			//list either the sets if in main menu, or all cards in the working set
			else if(input.equals("list")) {
				if(workingSet == null) {
					data.printSets();
				}
				else {
					System.out.println("~ All cards in set \"" + workingSet + "\":");
					data.printCardsInSet(workingSet);
				}
			}
			
			
			
			//set the workingSet if it exists
			else if(input.length() > 5 && input.substring(0,3).equals("set")) {
				String enterSet = input.substring(4);
				boolean setFound = false;
				
				//check to see if the set exists
				ArrayList<String> allSets = data.listSets();
				for(String aSet : allSets) {
					if(enterSet.equals(aSet)) {
						setFound = true;
					}
				}
				
				if(setFound) {
					workingSet = enterSet;
					System.out.println("~ Working set has been changed to: \"" + workingSet +"\"");
					System.out.println("~ All cards in set \"" + workingSet + "\":");
					data.printCardsInSet(workingSet);
				}
				else {
					System.out.println("~ Could not find a set named " + "\""+ enterSet +"\".");
				}
			}
			
			
			
			else if(input.equals("back")) {
				if(workingSet == null) {
					System.out.println("~ You are already in the main menu!");
				}
				else {
					workingSet = null; 
					System.out.println("~ Back at main menu. Sets: ");
					data.printSets();
				}
			}
			
			
			
			else if(input.equals("add")) {
				
				//add a set
				if(workingSet == null) {
					System.out.print("Enter new set name: ");
					String newSetName = (keyboard.nextLine()).toLowerCase();
					System.out.println("~ A set must be contain a card. Let's create one for the new set: " + "\"" + newSetName + "\"");
					System.out.print("Enter a question: ");
					String q = keyboard.nextLine();
					System.out.print("Enter an answer: ");
					String a = keyboard.nextLine();
					
					//create the new set by adding the card to the data
					data.list.add(new Card(newSetName, q, a));
					System.out.println("Card has been added.");
				}
				
				//add a card
				else {
					System.out.print("Enter a question: ");
					String q = keyboard.nextLine();
					System.out.print("Enter an answer: ");
					String a = keyboard.nextLine();
					data.list.add(new Card(workingSet, q, a));
				}
			}
			
			
			
			//
			else if(input.length()>7 && input.substring(0,6).equals("remove")) {
				String strToRemove = input.substring(7);

				//REMOVAL OF A SET
				if(workingSet == null) {
					
					//check if set exists
					if(data.listSets().contains(strToRemove)) { 
						
						System.out.println("Are you sure? This operation will completely delete all cards in the set.");
						System.out.print("Type \"CONFIRM\" to delete the set \"" + strToRemove + "\": ");
						String conf = keyboard.nextLine();
						if(conf.equals("CONFIRM")) {
							ArrayList<Card> toRemove = new ArrayList<Card>();	//we collect all the items to remove first
							
							for(Card c : data.list) {
								if(c.getSet().equals(strToRemove)) {
									toRemove.add(c);
								}
							}
							
							data.list.removeAll(toRemove);
						}
					}
					
					//set does not exist
					else {
						System.out.println("~ Could not find a set named " + "\""+ strToRemove +"\".");
					}
				}
				
				//REMOVAL OF A CARD
				else {
					//format arg checking
					boolean formatted = true; 
					for(char let : strToRemove.toCharArray()) {
						if(!Character.isDigit(let)) {
							formatted = false;
						}
					}
					
					//print error or remove card
					if (!formatted) {
						System.out.println("~ Invalid input. Format as \"remove <cardNumber>\".");
					}
					else{
						int toRemoveID = Integer.parseInt(strToRemove);
						
						System.out.println("~ Attempting to remove card #" + toRemoveID);
						
						//find the card with the number for the query
						ArrayList<Card> thisSet = data.getCardsInSet(workingSet);
						Card removed = thisSet.get(toRemoveID - 1 /*index*/);
						data.list.remove(removed);
						
						//test if set is empty after removal; delete it if it is
						if(data.getCardsInSet(workingSet).isEmpty()) {
							System.out.println("~ All cards have been removed from set \"" + workingSet + "\", so it will be deleted.");
							System.out.println("~ Returning to main menu.");
							workingSet = null;
							data.printSets();
						}
						else {
							System.out.println("~ Current set list after operation:");
							data.printCardsInSet(workingSet);
						}
					}
				}
			}
			
			
			//card reveals as an alternative to drilling a full deck
			else if(input.length() > 5 && input.substring(0, 3).equals("show")) {
				int cardID = Integer.parseInt(input.substring(4));
				ArrayList<Card> currentSet = data.getCardsInSet(workingSet);
				Card theCard = currentSet.get(cardID);
				System.out.println("ANS: " + theCard.getAnswer());
			}
			
			//catch-all
			else if(!input.equals("quit")){
				System.out.println("~ Unrecognized command: \"" + input + "\".\n~ Type \"help\" for a list of commands.");
			}
			
			//at the end of each input, save the program state in case the user forgets to save
			CardDataManager.saveCardList(data, saveDirectory);			
			
		}//input loop over
		
		//cleanup
		keyboard.close();
		CardDataManager.saveCardList(data, saveDirectory);
	}
	

	/*
	 * Lists all commands
	 */
	private static void printHelpMessage(){
		System.out.println("~ HELP MENU:");
		System.out.println("~ \"set <name>\" to change set.");
		System.out.println("~ \"back\" to go back and change subject.");
		System.out.println("~ \"list\" to show all sets (or all sets if in home menu).");
		System.out.println("\n~ \"add\" to add a new card to the database.");
		System.out.println("~ \"remove <IDNo>\" to delete a card (or \"remove <setName>\" to remove a set)."); 
		System.out.println("~ \"quit\" to shut down the program. All data will be saved.");
		System.out.println("~ \"drill\" to test yourself on the current set.");
	}
	
	private void drill(ArrayList<Card> drillDeck) {
		
	}
	
}
