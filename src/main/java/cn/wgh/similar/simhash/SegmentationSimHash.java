package cn.wgh.similar.simhash;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;


public class SegmentationSimHash extends AbstractSimHash {
	

	public SegmentationSimHash(String tokens) {
		super(tokens);
	}

	public SegmentationSimHash(String tokens, int hashbits) {
		super(tokens, hashbits);
	}

	@Override
	protected BigInteger simHash() {
		return getSimHash(tokens, hashbits);
	}

	public static BigInteger getSimHash(String tokens,int hashbits){
		// 定义特征向量/数组
        int[] v = new int[hashbits];
        StringReader reader = new StringReader(tokens);
        // 当为true时，分词器进行最大词长切分
        IKSegmenter ik = new IKSegmenter(reader, true);
        //IKSegmentation ik = new IKSegmentation(reader, true);
        Lexeme lexeme = null;
        String word = null;
        try {
			while ((lexeme = ik.next()) != null) {
			    word = lexeme.getLexemeText();
			    // 注意停用词会被干掉
			    // System.out.println(word);
			    // 2、将每一个分词hash为一组固定长度的数列.比如 64bit 的一个整数.
			    BigInteger t = HelpUtils.hash(word,hashbits);
			    for (int i = 0; i < hashbits; i++) {
			        BigInteger bitmask = new BigInteger("1").shiftLeft(i);
			        // 3、建立一个长度为64的整数数组(假设要生成64位的数字指纹,也可以是其它数字),
			        // 对每一个分词hash后的数列进行判断,如果是1000...1,那么数组的第一位和末尾一位加1,
			        // 中间的62位减一,也就是说,逢1加1,逢0减1.一直到把所有的分词hash数列全部判断完毕.
			        if (t.and(bitmask).signum() != 0) {
			            // 这里是计算整个文档的所有特征的向量和
			            // 这里实际使用中需要 +- 权重，比如词频，而不是简单的 +1/-1，
			            v[i] += 1;
			        } else {
			            v[i] -= 1;
			        }
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        BigInteger fingerprint = new BigInteger("0");
        for (int i = 0; i < hashbits; i++) {
            // 4、最后对数组进行判断,大于0的记为1,小于等于0的记为0,得到一个 64bit 的数字指纹/签名.
            if (v[i] >= 0) {
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
            }
        }
		return fingerprint;
	}

	@Override
	public boolean similar(String s, int similarDistance) {
		return similar(new SegmentationSimHash(s), similarDistance);
	}

	@Override
	public boolean similar(String s) {
		return similar(new SegmentationSimHash(s));
	}

}
