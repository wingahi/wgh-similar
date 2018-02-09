package cn.wgh.similar.simhash.support.bt;

import java.io.Serializable;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cn.wgh.similar.simhash.HelpUtils;

/**
 * 双向二叉排序树
 * 
 * @author Administrator
 *
 */
public class KdBTree extends AbstractBTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6862111998888135047L;

	public final static long DEFAULT_SPLITKEY_NUMBER = 0x7fff000000000000L;

	public final static int HASH_BITS = 64;

	public final static int DEFAULT_DISTANCE = 3;

	private final static Logger LOGGER = LoggerFactory.getLogger(KdBTree.class);
	
	public KdBTree() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public long add(long key) {
		if(this.node==null){
			this.node= new BTreeNode(this.getNextNodeId(), key,null, null, null);
			this.curNode = this.node;
			super.add(this.curNode);
		} else {
			add(this.node,this.node, key);
		}
		return this.curNode.getId();
	}

	public void add(BTreeNode preNode, BTreeNode nextNode, long key) {
		if (nextNode == null) {
			if (key > preNode.getSubKey()) {
				this.curNode = new BTreeNode(this.getNextNodeId(), key,preNode, null, null);
				preNode.setRightNode(this.curNode);
				super.add(this.curNode);
			} else if (key < preNode.getSubKey()) {
				this.curNode = new BTreeNode(this.getNextNodeId(), key,preNode, null, null);
				preNode.setLeftNode(this.curNode);
				super.add(this.curNode);
			}else{
				this.curNode = preNode;
			}
		} else {
			if (key > nextNode.getSubKey()) {
				addRight(nextNode,nextNode.getRightNode(), key);
			} else if (key < nextNode.getSubKey()){
				addLeft(nextNode,nextNode.getLeftNode(), key);
			}else{
				this.curNode = preNode;
			}
		}
	}
	
	public void addRight(BTreeNode preNode, BTreeNode nextNode, long key) {
		 if (nextNode==null){
			 add(preNode,nextNode, key);
		 }else if (key < nextNode.getSubKey()) {
			addLeft(nextNode, nextNode.getLeftNode(), key);
		} else if (key > nextNode.getSubKey()){
			add(nextNode,nextNode.getRightNode(), key);
		}else if(key == nextNode.getSubKey()){
			this.curNode = nextNode;
		}
	}

	public void addLeft(BTreeNode preNode,BTreeNode nextNode, long key) {
		 if (nextNode==null){
			 add(preNode,nextNode, key);
		 }else if (key > nextNode.getSubKey()) {
			addRight(nextNode, nextNode.getRightNode(), key);
		} else if (key < nextNode.getSubKey()){
			add(nextNode,nextNode.getLeftNode(), key);
		}else if(key == nextNode.getSubKey()){
			this.curNode = nextNode;
		}
	}
	
	@Deprecated
	@Override
	public boolean delete(long nodeId) {
		BTreeNode curNode = queryByNodeId(nodeId);
		if (curNode == null) {
			return false;
		}
		BTreeNode parent = curNode.getParentNode();
		BTreeNode right = curNode.getRightNode();
		BTreeNode left = curNode.getLeftNode();
		if (parent == null) {
			// 如果是根元素,则取右节点作为根节点
			if (right != null) {
				this.node = right;
				right.setParentNode(parent);
				left.setParentNode(right);
			} else {
				if (left != null) {
					this.node = left;
					left.setParentNode(parent);
				} else {
					// 如果只有一个节点
					this.node = null;
				}
			}
		} else {
			// 非根元素
			right.setParentNode(parent);
			left.setParentNode(parent);
			parent.setLeftNode(left);
			parent.setRightNode(right);
		}
		// 设置关联节点为null
		curNode.setParentNode(null);
		curNode.setLeftNode(null);
		curNode.setRightNode(null);
		super.remove(curNode.getId());
		// 不标为null
		// curNode = null;
		return true;
	}

	@Deprecated
	@Override
	public boolean update(long nodeId, long subKey) {
		BTreeNode curNode = queryByNodeId(nodeId);
		if (curNode == null) {
			return false;
		}
		curNode.setSubKey(subKey);
		// 删除当前节点
		delete(curNode.getId());
		// 重新查找节点位置，插入新节点
		add(this.node, this.node, curNode);
		return true;
	}

	/**
	 * 判断是否为平衡树
	 * @return
	 */
	private boolean checkBalance(){
		if(this.node!=null){
			int leftCount = getTreeBranchCount(this.node.getLeftNode());
			int rightCount = getTreeBranchCount(this.node.getRightNode());
//			if(LOGGER.isInfoEnabled()){
//				LOGGER.info("根节点key和id：id={},key：{}\n节点分布统计：\n left：{}，right：{}", this.node.getId(),this.node.getSubKey(),leftCount,rightCount);
//			}
			System.out.println("根节点key和id：id="+this.node.getId()+",key："+this.node.getSubKey()+"\n节点分布统计：\n left："+leftCount+"，right："+rightCount);
			//平衡失衡高度为1000个
//			if(leftCount-rightCount>1000){
//				//左边重
//			}else if(rightCount-leftCount>1000){
//				//右边重
//			}
		}
		return true;
	}
	
	private int getTreeBranchCount(BTreeNode treeNode){
		int count = 0;
		if (treeNode!=null) {
			count++;
			return count+getTreeBranchCount(treeNode.getLeftNode())+getTreeBranchCount(treeNode.getRightNode());
		}
		return count;
	}
	
	/**
	 * 调整平衡树
	 */
	public void rebuild(){
		if(!checkBalance()){
			
		}
	}
	
	@Deprecated
	public void add(BTreeNode preNode, BTreeNode nextNode, BTreeNode newNode) {
		if (nextNode == null) {
			if (newNode.getSubKey() > preNode.getSubKey()) {
				newNode.setParentNode(preNode);
				this.curNode = newNode;
				preNode.setRightNode(newNode);
				super.add(newNode);
			} else if (newNode.getSubKey() < preNode.getSubKey()) {
				this.curNode = newNode;
				preNode.setLeftNode(newNode);
				super.add(newNode);
			} else {
				this.curNode = preNode;
			}
		} else {
			if (newNode.getSubKey() > nextNode.getSubKey()) {
				addRight(nextNode, nextNode.getRightNode(), newNode);
			} else {
				addLeft(nextNode, nextNode.getLeftNode(), newNode);
			}
		}
	}

	@Deprecated
	public void addRight(BTreeNode preNode, BTreeNode nextNode, BTreeNode newNode) {
		if (nextNode != null && newNode.getSubKey() < nextNode.getSubKey()) {
			this.curNode = newNode;
			nextNode.setLeftNode(this.curNode);
			super.add(newNode);
		} else if (nextNode == null || newNode.getSubKey() > nextNode.getSubKey()) {
			add(preNode, nextNode, newNode);
		} else if (nextNode != null && newNode.getSubKey() == nextNode.getSubKey()) {
			this.curNode = nextNode;
		}
	}

	@Deprecated
	public void addLeft(BTreeNode preNode, BTreeNode nextNode, BTreeNode newNode) {
		if (nextNode != null && nextNode.getSubKey() > nextNode.getSubKey()) {
			this.curNode = newNode;
			nextNode.setRightNode(this.curNode);
		} else if (nextNode == null || nextNode.getSubKey() < nextNode.getSubKey()) {
			add(preNode, nextNode, newNode);
		} else if (nextNode != null && nextNode.getSubKey() == nextNode.getSubKey()) {
			this.curNode = nextNode;
		}
	}
	
	@Override
	public BTreeNode queryBykey(BigInteger key) {
		return query(this.node, Long.valueOf(String.valueOf(HelpUtils.getSubByDistance(key, DEFAULT_DISTANCE).get(0)).concat(String.valueOf(1))).longValue());
	}
	

	private BTreeNode query(BTreeNode preNode, long subKey) {
		if (preNode != null) {
			if (subKey > preNode.getSubKey()) {
				return query(preNode.getRightNode(), subKey);
			} else if (subKey < preNode.getSubKey()) {
				return query(preNode.getLeftNode(), subKey);
			} else {
				try {
					return (BTreeNode) preNode.clone();
				} catch (CloneNotSupportedException e) {
					throw new RuntimeException(e);
				}
			}
		} else {
			return null;
		}
	}

	@Deprecated
	@Override
	public BTreeNode queryByNodeId(long nodeId) {
		return nodeIdAndNodeMap.get(nodeId);
	}

}
