package org.hummer.core.codec;

public interface ICodec {
    String encrypt(String var1) throws Exception;

    String decrypt(String var1) throws Exception;
}