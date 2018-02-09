package cn.wgh.similar.simhash;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public abstract class AbstractSimHash implements SimHash{

	// 默认相似距离
	private final static int DEFAULT_SIMILAR_DISTANCE = 3;

	protected String tokens;

	protected BigInteger intSimHash;

	protected int hashbits = 64;

	public AbstractSimHash(String tokens) {
		this.tokens = tokens;
		this.intSimHash = this.simHash();
	}

	public AbstractSimHash(String tokens, int hashbits) {
		this.tokens = tokens;
		this.hashbits = hashbits;
		this.intSimHash = this.simHash();
	}

	public BigInteger getIntSimHash() {
		return intSimHash;
	}

	/**
	 * 有问题，会导致指纹丢失
	 * 如果海明距离取3，则分成四块，并得到每一块的bigInteger值 ，作为索引值使用
	 * 
	 * @param distance
	 * @return
	 */
	@Override
	public List<Long> subByDistance(int distance) {
		return HelpUtils.getSubByDistance(this.intSimHash, distance);
	}
	

	@Override
	public boolean similar(AbstractSimHash simHash){
		return HelpUtils.similar(this, simHash, DEFAULT_SIMILAR_DISTANCE);
	}

	@Override
	public boolean similar(AbstractSimHash simHash,int similarDistance){
		return HelpUtils.doCheckSimilar(this, simHash, similarDistance);
	}

	protected abstract BigInteger simHash() ;
}
