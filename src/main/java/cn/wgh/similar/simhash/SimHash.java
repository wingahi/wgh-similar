/**
 * 
 */
package cn.wgh.similar.simhash;

import java.math.BigInteger;
import java.util.List;

import cn.wgh.similar.Similar;

/**
 * @author Administrator
 *
 */
public interface SimHash extends Similar{

	/**
	 *  判断是否相似
	 * @param simHash
	 * @return 相似-true，否则-false
	 */
	public boolean similar(AbstractSimHash simHash);
	/**
	 *  判断是否相似
	 * @param simHash
	 * @param similarDistance 相似汉明距离，默认3
	 * @return 相似-true，否则-false
	 */
	public boolean similar(AbstractSimHash simHash,int similarDistance);
	
	/**
	 *  判断是否相似
	 * @param s
	 * @return 相似-true，否则-false
	 */
	public boolean similar(String s);
	/**
	 * 获取分段hash签名
	 * @param distance
	 * @return
	 */
	public List<Long> subByDistance(int distance) ;
}
