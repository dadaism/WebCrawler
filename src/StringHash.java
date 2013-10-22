
public class StringHash {
	private int range;
	private int seed;
	/**
	 * @param args
	 */
	public StringHash(int range, int seed) {
		this.range = range;
		this.seed = seed;
	}
	public int hash(String value) {
		int result = 0;
		int len = value.length();
		for (int i=0; i<len; ++i) {
			result += seed * result + value.charAt(i);
		}
		return (range-1) & result;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringHash myhash = new StringHash(1<<8, 7);
		System.out.println(myhash.hash("Hello World"));
		System.out.println(myhash.hash("Da Li"));
	}

}
