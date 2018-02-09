/**
 * 
 */
package cn.wgh.similar;

/**
 * @author Administrator
 *
 */
public interface Similar {
	/**
	 * 判断是否相似
	 * @param tokens
	 * @param similarThreshold
	 * @return
	 */
	 public boolean similar(String tokens,int similarThreshold) ;
}
