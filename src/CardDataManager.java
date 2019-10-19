

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/*
 * SaveDataReader is a file reader that contains implementations to help construct the CoreFilmList
 */
public class CardDataManager {

	//private constructor to prevent instantiation; utility class
	private CardDataManager() {}
	
	/*
	 * @params the CoreFilmList and directory to save to
	 */
	public static void saveCardList(CardData dataSet, String saveDirectory) {
		FileOutputStream writer = null;
		ObjectOutputStream objWriter = null;
		try {
			writer = new FileOutputStream(saveDirectory);
			objWriter = new ObjectOutputStream(writer);

			objWriter.writeObject(dataSet.list);

			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("List save-state could not be located.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Object writing for list failed.");
		}
		finally {
			try {
				writer.close();
				objWriter.close();
			} catch(IOException e){
				e.printStackTrace();
				System.err.println("Error closing list or saving streams.");
			}
		}
	}
	
	/*
	 * @param directory location of file of serialized core list
	 * @returns CoreFilmList instance to be used
	 */
	@SuppressWarnings("unchecked")
	public static CardData loadCardList(String saveDirectory) {
		
		CardData recovery = new CardData();
		
		FileInputStream reader = null;
		ObjectInputStream objReader = null;
		try {
			reader = new FileInputStream(saveDirectory);
			objReader = new ObjectInputStream(reader);
			
			recovery.list = (ArrayList<Card>) objReader.readObject();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("List save-state could not be located.");		
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Object reader setup for list recovery failed.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("List object definition could not be located.");
		} 
		finally {
			try {
				reader.close();
				objReader.close();
			} catch(IOException e){
				e.printStackTrace();
				System.err.println("Error closing list or loading streams.");
			}
		}
		
		return recovery;
	}
}
