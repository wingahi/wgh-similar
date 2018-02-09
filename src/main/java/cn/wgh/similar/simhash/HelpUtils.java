package cn.wgh.similar.simhash;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import cn.wgh.similar.simhash.support.bt.KdBTree;

public final class HelpUtils {
	
	public static List getSubByDistance(Object key,int distance){
		List subKeyList = null;
		if(BigInteger.class.isAssignableFrom(key.getClass())){
			 subKeyList = HelpUtils.getSubByDistance((BigInteger)key, KdBTree.DEFAULT_DISTANCE);
		}else{
			 subKeyList = HelpUtils.getSubByDistance((long)key, KdBTree.DEFAULT_DISTANCE);
		}
		return subKeyList;
	}
	
	public static List getSubByDistance(BigInteger key,int distance) {
		int numEach = KdBTree.HASH_BITS / (distance + 1);
		List<Long> characters = new ArrayList();
		StringBuffer buffer = new StringBuffer();

		int k = 0;
		for (int i = 0; i < key.bitLength(); i++) {
			boolean sr = key.testBit(i);
			if (sr) {
				buffer.append("1");
			} else {
				buffer.append("0");
			}
			if ((i + 1) % numEach == 0) {
				BigInteger eachValue = new BigInteger(buffer.toString(), 2);
				buffer.delete(0, buffer.length());
				characters.add(eachValue.longValue());
			}
		}
		if(buffer.length()>0){
			BigInteger eachValue = new BigInteger(buffer.toString(), 2);
			buffer.delete(0, buffer.length());
			characters.add(eachValue.longValue());
		}
		return characters;
	}
	
	
	public static List getSubByDistance(long key,int distance) {
		int numEach = KdBTree.HASH_BITS / (distance + 1);
		List<Long> characters = new ArrayList();
		int loopCnt = 0;
		while (loopCnt<=distance) {
			 characters.add(key&(KdBTree.DEFAULT_SPLITKEY_NUMBER>>(numEach*loopCnt)));
			 loopCnt++;
		}
		return characters;
	}
	
	//
	private final static long TEST_tOW_SIGN_SAME = 0x7fff000000000000L;
	/**
	 * 每段16位，默认共64位，即是将签名分为4段，判断两标签是否至少有一段相同
	 * 作用：用于过滤两个签名是否需要计算汉明距离，
	 * 如果返回true（至少有一段相同），则需要计算汉明距离来判断两字符串是否相似；
	 * 否则，不需计算汉明距离，节省性能
	 * @param l1
	 * @param l2
	 * @return
	 */
	public static boolean hasOneSame(long sign1, long sign2) {
		long m = sign1 ^ sign2;
		long temp = TEST_tOW_SIGN_SAME;
		do {
			if ((m & temp) == 0) {
				return true;
			}
			temp >>= 16;
		} while (temp > 0);
		return false;
	}
	
	/**
	 * 每段16位，默认共64位，即是将签名分为4段，统计两签名相同端数
	 * 作用：用于过滤两个签名是否需要计算汉明距离，
	 * 如果返回范围x存在以下情况
	 * 1、x=0,则不需计算汉明距离
	 * 1、0<x<=3，则需要计算汉明距离
	 * 2、x=4，则不需要计算汉明距离，即可认为两字符串相等
	 * @param l1
	 * @param l2
	 * @return
	 */
	public static int hasSameCount(long sign1, long sign2) {
		long m = sign1 ^ sign2;
		long temp = TEST_tOW_SIGN_SAME;
		int cnt = 0;
		do {
			if ((m & temp) == 0) {
				cnt++;
			}
			temp >>= 16;
		} while (temp > 0);
		return cnt;
	}
	
	public static BigInteger hash(String source,int hashbits) {
		if (source == null || source.length() == 0) {
			return new BigInteger("0");
		} else {
			char[] sourceArray = source.toCharArray();
			BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
			BigInteger m = new BigInteger("1000003");
			BigInteger mask = new BigInteger("2").pow(hashbits).subtract(new BigInteger("1"));
			for (char item : sourceArray) {
				BigInteger temp = BigInteger.valueOf((long) item);
				x = x.multiply(m).xor(temp).and(mask);
			}
			x = x.xor(new BigInteger(String.valueOf(source.length())));
			if (x.equals(new BigInteger("-1"))) {
				x = new BigInteger("-2");
			}
			return x;
		}
	}
	
	/**
	 * 取两个二进制的异或，统计为1的个数，就是海明距离
	 * 
	 * @param other
	 * @return 返回汉明距离
	 */
	public static int hammingDistance(AbstractSimHash source, AbstractSimHash other) {

		BigInteger x = source.intSimHash.xor(other.intSimHash);
		int tot = 0;

		// 统计x中二进制位数为1的个数
		// 我们想想，一个二进制数减去1，那么，从最后那个1（包括那个1）后面的数字全都反了，对吧，然后，n&(n-1)就相当于把后面的数字清0，
		// 我们看n能做多少次这样的操作就OK了。
		
		while (x.signum() != 0) {
			tot += 1;
			x = x.and(x.subtract(new BigInteger("1")));
		}
		return tot;
	}
	
	/**
	 * 判断是否相似
	 * 
	 * @param other
	 * @return 返回汉明距离
	 */

	public static boolean doCheckSimilar(AbstractSimHash source, AbstractSimHash other,int simHanmingDistance) {

		BigInteger x = source.intSimHash.xor(other.intSimHash);
		int tot = 0;

		// 统计x中二进制位数为1的个数
		// 我们想想，一个二进制数减去1，那么，从最后那个1（包括那个1）后面的数字全都反了，对吧，然后，n&(n-1)就相当于把后面的数字清0，
		// 我们看n能做多少次这样的操作就OK了。
		
		while (x.signum() != 0 && tot<=simHanmingDistance) {
			tot += 1;
			x = x.and(x.subtract(new BigInteger("1")));
		}
		return tot<=simHanmingDistance;
	}
	
	public static boolean similar(AbstractSimHash source, AbstractSimHash other,int similarDistance){
		return doCheckSimilar(source, other, similarDistance);
	}
	
}
