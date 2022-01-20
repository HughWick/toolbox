//package com.github.hugh.crypto.base;
//
////import cn.hutool.core.io.FileUtil;
////import cn.hutool.core.io.IoUtil;
////import cn.hutool.core.lang.Assert;
////import cn.hutool.core.util.ArrayUtil;
////import cn.hutool.core.util.CharUtil;
////import cn.hutool.core.util.RandomUtil;
////import cn.hutool.core.util.StrUtil;
////import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
////import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
//
//import com.github.hugh.util.EmptyUtils;
//import com.github.hugh.util.StringUtils;
//
//import java.security.*;
//import java.security.spec.AlgorithmParameterSpec;
//
///**
// * 密钥工具类
// *
// * <p>
// * 包括:
// * <pre>
// * 1、生成密钥（单密钥、密钥对）
// * 2、读取密钥文件
// * </pre>
// *
// * @author looly, Gsealy
// * @since 4.4.1
// */
//public class KeyUtil {
//    /**
//     * 默认密钥字节数
//     *
//     * <pre>
//     * RSA/DSA
//     * Default Keysize 1024
//     * Keysize must be a multiple of 64, ranging from 512 to 1024 (inclusive).
//     * </pre>
//     */
//    public static final int DEFAULT_KEY_SIZE = 1024;
//
//    /**
//     * 生成用于非对称加密的公钥和私钥，仅用于非对称加密<br>
//     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
//     *
//     * @param algorithm 非对称加密算法
//     * @return {@link KeyPair}
//     */
//    public static KeyPair generateKeyPair(String algorithm) {
//        int keySize = DEFAULT_KEY_SIZE;
//        if("ECIES".equalsIgnoreCase(algorithm)){
//            // ECIES算法对KEY的长度有要求，此处默认256
//            keySize = 256;
//        }
//
//        return generateKeyPair(algorithm, keySize);
//    }
//
//    /**
//     * 生成用于非对称加密的公钥和私钥<br>
//     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
//     *
//     * @param algorithm 非对称加密算法
//     * @param keySize   密钥模（modulus ）长度
//     * @return {@link KeyPair}
//     */
//    public static KeyPair generateKeyPair(String algorithm, int keySize) {
//        return generateKeyPair(algorithm, keySize, null);
//    }
//
//
//    public static KeyPair generateKeyPair(String algorithm, int keySize, SecureRandom random, AlgorithmParameterSpec... params) {
//        algorithm = getAlgorithmAfterWith(algorithm);
//        final KeyPairGenerator keyPairGen = getKeyPairGenerator(algorithm);
//
//        // 密钥模（modulus ）长度初始化定义
//        if (keySize > 0) {
//            // key长度适配修正
//            if ("EC".equalsIgnoreCase(algorithm) && keySize > 256) {
//                // 对于EC（EllipticCurve）算法，密钥长度有限制，在此使用默认256
//                keySize = 256;
//            }
//            if (null != random) {
//                keyPairGen.initialize(keySize, random);
//            } else {
//                keyPairGen.initialize(keySize);
//            }
//        }
//        // 自定义初始化参数
//        if (EmptyUtils.isNotEmpty(params)) {
//            for (AlgorithmParameterSpec param : params) {
//                if (null == param) {
//                    continue;
//                }
//                try {
//                    if (null != random) {
//                        keyPairGen.initialize(param, random);
//                    } else {
//                        keyPairGen.initialize(param);
//                    }
//                } catch (InvalidAlgorithmParameterException e) {
////                    throw new CryptoException(e);
//                    e.printStackTrace();
//                }
//            }
//        }
//        return keyPairGen.generateKeyPair();
//    }
//
//    /**
//     * 获取用于密钥生成的算法<br>
//     * 获取XXXwithXXX算法的后半部分算法，如果为ECDSA或SM2，返回算法为EC
//     *
//     * @param algorithm XXXwithXXX算法
//     * @return 算法
//     */
//    public static String getAlgorithmAfterWith(String algorithm) {
//        if(StringUtils.startWithIgnoreCase(algorithm, "ECIESWith")){
//            return "EC";
//        }
////        int indexOfWith = StrUtil.lastIndexOfIgnoreCase(algorithm, "with");
////        if (indexOfWith > 0) {
////            algorithm = StrUtil.subSuf(algorithm, indexOfWith + "with".length());
////        }
//        if ("ECDSA".equalsIgnoreCase(algorithm)
//                || "SM2".equalsIgnoreCase(algorithm)
//                || "ECIES".equalsIgnoreCase(algorithm)
//        ) {
//            algorithm = "EC";
//        }
//        return algorithm;
//    }
//
//    /**
//     * 获取{@link KeyPairGenerator}
//     *
//     * @param algorithm 非对称加密算法
//     * @return {@link KeyPairGenerator}
//     * @since 4.4.3
//     */
//    public static KeyPairGenerator getKeyPairGenerator(String algorithm) {
//        final Provider provider =  new org.bouncycastle.jce.provider.BouncyCastleProvider();
//        KeyPairGenerator keyPairGen;
//        try {
//            keyPairGen = (null == provider) //
//                    ? KeyPairGenerator.getInstance(getMainAlgorithm(algorithm)) //
//                    : KeyPairGenerator.getInstance(getMainAlgorithm(algorithm), provider);//
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//        return keyPairGen;
//    }
//
//    /**
//     * 获取主体算法名，例如RSA/ECB/PKCS1Padding的主体算法是RSA
//     *
//     * @param algorithm XXXwithXXX算法
//     * @return 主体算法名
//     * @since 4.5.2
//     */
//    public static String getMainAlgorithm(String algorithm) {
//        final int slashIndex = algorithm.indexOf('/');
//        if (slashIndex > 0) {
//            return algorithm.substring(0, slashIndex);
//        }
//        return algorithm;
//    }
//}
