import java.util.BitSet;


public class BloomFilter {
	private static final int SIZE = 1 << 24;
	private static final int [] seeds = new int [] {7, 11, 13, 31, 37, 61, 67};
	private BitSet bits = new BitSet(SIZE);
	private StringHash [] hashFunc = new StringHash [seeds.length];
	
	public BloomFilter(){
		for (int i=0; i<seeds.length; ++i) {
			hashFunc[i] = new StringHash( SIZE<<1, seeds[i]);
		}
	}
	public void addElement(String value) {
		for (StringHash f : hashFunc) {
			bits.set(f.hash(value), true);
		}
	}
	// return true if the value is in the bloom filter
	public boolean checkElement(String value) {
		if ( value==null )
			return false;
		boolean ret = true;
		for ( StringHash f : hashFunc ) {
			ret = ret && bits.get( f.hash(value) );
		}
		return ret;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BloomFilter bloomFilter = new BloomFilter();
		bloomFilter.addElement("http://www.danielbit.com");
		System.out.println( bloomFilter.checkElement("http://www.danielbit.com") );
		System.out.println( bloomFilter.checkElement("http://nps4.missouri.edu") );
	}

}
