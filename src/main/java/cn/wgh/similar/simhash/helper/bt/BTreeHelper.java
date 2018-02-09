package cn.wgh.similar.simhash.helper.bt;

import java.math.BigInteger;
import java.util.List;

import cn.wgh.similar.simhash.support.NodeBuilderAdapter;
import cn.wgh.similar.simhash.support.bt.BTreeBulder;

public final class BTreeHelper {
	/**
	 * 建造二叉排序树
	 * @param simHashList
	 * @return
	 */
	public static BTreeBulder build(List<BigInteger> simHashList){
		if(simHashList==null || simHashList.isEmpty()){
			return null;
		}
		BTreeBulder bTreeBulder = new BTreeBulder(simHashList) ;
		NodeBuilderAdapter nodeBuilderAdapter = new NodeBuilderAdapter();
		nodeBuilderAdapter.build(bTreeBulder);
		return bTreeBulder;
	}
}
