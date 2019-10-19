import java.io.Serializable;

@SuppressWarnings("serial")
public class Card implements Serializable {
	
	private String set;
	private String question;
	private String answer;
	
	public Card(String set, String question, String answer) {
		this.set = set;
		this.question = question;
		this.answer = answer;
	}

	public String getSet() {
		return set;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	
}


