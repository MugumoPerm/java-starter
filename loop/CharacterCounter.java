public class CharacterCounter {
    public static void main(String[] args) {
	String s = "Hello World";
	int target = 9;

	int count = count(s, target);
	System.out.println(count);
    }

    private static int count(String s, int target) {
	int count = 0;

	for (int i = 0; i < s.length(); i++) {
	    if (s.charAt(i) == target) {
	    	count++;
	    }
	
    }

    return count;
    }
}
