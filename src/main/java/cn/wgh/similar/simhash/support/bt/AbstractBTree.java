package cn.wgh.similar.simhash.support.bt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractBTree implements BTree {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7003496512256087505L;
	protected volatile long nodeCount;//节点总数
	protected volatile long nextNodeId;//下一个节点id
	protected BTreeNode node;//根节点
	protected BTreeNode curNode;//目前节点
	protected ConcurrentMap<Long, BTreeNode> nodeIdAndNodeMap;//扩展使用，节点id-节点关系
	public AbstractBTree() {
		nodeIdAndNodeMap = new ConcurrentHashMap<>();
	}
	public long getNodeCount() {
		return nodeCount;
	}
	public void increNodeCount() {
		this.nodeCount++;
	}
	
	public long getNextNodeId() {
		nextNodeId++;
		return nextNodeId;
	}

	public BTreeNode getNode() {
		return node;
	}
	public void setNode(BTreeNode node) {
		this.node = node;
	}
	public BTreeNode getCurNode() {
		return curNode;
	}
	public void setCurNode(BTreeNode curNode) {
		this.curNode = curNode;
	}
	
	public void add(BTreeNode node) {
		increNodeCount();
		//nodeIdAndNodeMap.put(node.getId(), node);
	}
	
	public void remove(long nodeId) {
		increNodeCount();
		//nodeIdAndNodeMap.remove(nodeId);
	}
}
