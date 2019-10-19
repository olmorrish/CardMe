import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CardData implements Serializable {
	
	public ArrayList<Card> list;
	
	public CardData() {
		list = new ArrayList<Card>();
	}
	

	
	/*
	 * Gets all set names in the database as an ArrayList<String>
	 */
	public ArrayList<String> listSets() {
		ArrayList<String> ret = new ArrayList<String>();
		for(Card c : list) {
			if(!ret.contains(c.getSet())) {
				ret.add(c.getSet());
			}	
		}
		return ret;
	}
	
	/*
	 * Prints all set names in the database 
	 */
	public void printSets() {
		System.out.println("~ All sets:");
		ArrayList<String> allSets = this.listSets();
		for(String set : allSets) {
			System.out.println(">> " + set);
		}
	}
	
	/*
	 * Gets all cards in the database for a single set as an ArrayList<Card>
	 */
	public ArrayList<Card> getCardsInSet(String set) {	
		ArrayList<Card> query = new ArrayList<Card>();
		for(Card c : list) {
			if(c.getSet().equals(set)) {
				query.add(c);
			}	
		}
		return query;
	}

	/*
	 * Prints all cards in the database for a single set
	 */
	public void printCardsInSet(String set) {	
		ArrayList<Card> query = new ArrayList<Card>();
		for(Card c : list) {
			if(c.getSet().equals(set)) {
				query.add(c);
			}	
		}
		
		int IDNo = 1;
		for(Card c : query) {
			System.out.println(">> " + IDNo + ": \"" + c.getQuestion() + "\"");
			IDNo++;
		}
	}
}
