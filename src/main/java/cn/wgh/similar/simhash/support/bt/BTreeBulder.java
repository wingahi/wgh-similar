/**
 * 
 */
package cn.wgh.similar.simhash.support.bt;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wgh.similar.simhash.HelpUtils;
import cn.wgh.similar.simhash.support.AbstractNodeBuilder;

/**
 * @author Administrator
 *
 */
public class BTreeBulder extends AbstractNodeBuilder  {

	private List<BigInteger> sourceDataList;
	
	private KdBTree bTree;

	private Map<BigInteger,Long[]> keyAndNodeIdMap;
	

	public BTreeBulder(List<BigInteger> sourceDataList) {
		super();
		if(sourceDataList==null || sourceDataList.isEmpty()){
			throw new RuntimeException("数据为空");
		}
		this.sourceDataList = sourceDataList;
		this.bTree = new KdBTree();
		this.keyAndNodeIdMap = new HashMap<>();
	}


	public KdBTree getbTree() {
		return bTree;
	}

	@Override
	public void build() {
		int nodeLen = this.sourceDataList.size();
		BigInteger root = null;
		int rootIndex = 0;
		if(nodeLen==1){
			root = this.sourceDataList .get(0);
		}else{
			//选取节点列表的1/2处的subKey当做根节点
			rootIndex = this.sourceDataList .size()/2-1;
			root = this.sourceDataList .get(rootIndex);
		}
		this.sourceDataList.remove(rootIndex);
		//添加根节点
		this.add(root);
		if(!this.sourceDataList.isEmpty()){
			for (BigInteger key : this.sourceDataList) {
				this.add(key);
			}
		}
		this.bTree.rebuild();
		sourceDataList = null;
	}
	
	public void add(BigInteger key) {
		List<Long>  subKeyList =  HelpUtils.getSubByDistance(key, KdBTree.DEFAULT_DISTANCE);
		
		int index = 1;
		Long[] nodeIds = new Long[KdBTree.DEFAULT_DISTANCE+1];
		for (Long subKey : subKeyList) {
			nodeIds[index-1]=this.bTree.add(Long.valueOf(String.valueOf(subKey).concat(String.valueOf(index))).longValue());
			index++;
		}
		this.keyAndNodeIdMap.put(key, nodeIds);
	}


	public Map<BigInteger, Long[]> getKeyAndNodeIdMap() {
		return keyAndNodeIdMap;
	}
}
