package cn.wgh.similar;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import cn.wgh.similar.simhash.helper.bt.BTreeHelper;
import cn.wgh.similar.simhash.support.NodeBuilderAdapter;
import cn.wgh.similar.simhash.support.bt.BTreeBulder;
import cn.wgh.similar.simhash.support.bt.BTreeNode;
import cn.wgh.similar.simhash.support.bt.KdBTree;

public class KdBTreeTest {
	public static void main(String[] args) {
		List<BigInteger> list = new ArrayList<>();
		for (long i = 1; i < 9; i++) {
			list.add(new BigInteger(i+""));
			list.add(new BigInteger((i*2)+""));
		}
		list.add(new BigInteger("2302916563077"));
		BTreeHelper.build(list);
		
		//methodBigInteger();
	}

	private static void methodBigInteger() {
		List<BigInteger> list = new ArrayList<>();

		long st = System.currentTimeMillis();
		for (long i = 1; i < 9; i++) {
			list.add(new BigInteger(i+""));
			list.add(new BigInteger((i*2)+""));
		}
		list.add(new BigInteger("2302916563077"));
		
		long st1 = System.currentTimeMillis();
		System.out.println("准备数据耗时："+(st1-st));
		BTreeBulder bTreeBulder = new BTreeBulder(list) ;
		NodeBuilderAdapter nodeBuilderAdapter = new NodeBuilderAdapter();
		nodeBuilderAdapter.build(bTreeBulder);
		long st2 = System.currentTimeMillis();
		System.out.println("建立排序二叉树耗时："+(st2-st1));
		KdBTree kdBTree = bTreeBulder.getbTree();
		BTreeNode node = 	kdBTree.queryBykey(new BigInteger("1000000000000000"));//kdBTree.queryBykey(new BigInteger("2577794470021"));
		long st3 = System.currentTimeMillis();
		System.out.println("查询耗时："+(st3-st2));
		System.out.println(node==null?"null":(node.getId()+"--"+node.getSubKey()));
	}
}