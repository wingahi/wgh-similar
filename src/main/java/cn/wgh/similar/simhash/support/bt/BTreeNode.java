/**
 * 
 */
package cn.wgh.similar.simhash.support.bt;

import java.io.Serializable;

import cn.wgh.similar.simhash.support.AbstractNode;

/**
 * 二叉排序树实体
 * 
 * @author Administrator
 *
 */
public class BTreeNode extends AbstractNode implements Serializable {

	public BTreeNode() {
		super();
	}

	

	public BTreeNode(long id, long subKey,BTreeNode parentNode, BTreeNode leftNode, BTreeNode rightNode) {
		super(id, subKey);
		this.parentNode = parentNode;
		this.leftNode = leftNode;
		this.rightNode = rightNode;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = -806350237751479798L;

	private BTreeNode parentNode;//父节点
	private BTreeNode leftNode;//左节点
	private BTreeNode rightNode;//右节点

	
	
	public BTreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(BTreeNode parentNode) {
		this.parentNode = parentNode;
	}

	public BTreeNode getLeftNode() {
		return leftNode;
	}

	public void setLeftNode(BTreeNode leftNode) {
		this.leftNode = leftNode;
	}

	public BTreeNode getRightNode() {
		return rightNode;
	}

	public void setRightNode(BTreeNode rightNode) {
		this.rightNode = rightNode;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new BTreeNode(this.getId(), this.getSubKey(), null, null, null);
	}
}
