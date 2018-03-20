package com.aoshuotec.iron.cornerstone.encryptKit;

import java.nio.ByteOrder;

/**
 * Created by molaith on 2017/11/30.
 */

public class ByteUtils {

    public static short[] bytes2Shorts(byte[] bytes, int dataLength) {
        short[] shorts = null;
        byte bLength = 2;
        if (dataLength > bytes.length) {
            throw new IllegalArgumentException("dataLength>shorts.length in shorts2Bytes");
        }
        shorts = new short[dataLength / bLength];
        for (int iLoop = 0; iLoop < shorts.length; iLoop++) {
            byte[] temp = new byte[bLength];
            for (int jLoop = 0; jLoop < bLength; jLoop++) {
                temp[jLoop] = bytes[iLoop * bLength + jLoop];
            }
            shorts[iLoop] = getShort(temp, ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
        }
        return shorts;
    }

    public static short getShort(byte[] buf, boolean bBigEnding) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (buf.length > 2) {
            throw new IllegalArgumentException("byte array size > 2 !");
        }

        short r = 0;

        if (bBigEnding) {
            for (int i = 0; i < buf.length; i++) {
                r <<= 8;
                r |= (buf[i] & 0x00ff);
            }
        } else {
            for (int i = buf.length - 1; i >= 0; i--) {
                r <<= 8;
                r |= (buf[i] & 0x00ff);
            }
        }
        return r;
    }

    public static byte[] shorts2Bytes(short[] shorts,int dataLength){
        byte[] bytes=null;
        if (shorts!=null&&dataLength>shorts.length){
            throw new IllegalArgumentException("dataLength>shorts.length in shorts2Bytes");
        }
        if (shorts==null){
            return null;
        }
        bytes=new byte[dataLength*2];
        for (int i=0;i<dataLength;i++){
            short s=shorts[i];
            byte[] temp = getBytes(s,ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
            for (int j=0;j<temp.length;j++){
                bytes[i*2+j]=temp[j];
            }
        }
        return bytes;
    }

    public static byte[] getBytes(short s, boolean bBigEnding) {
        byte[] buf = new byte[2];
        if (bBigEnding)
            for (int i = buf.length - 1; i >= 0; i--) {
                buf[i] = (byte) (s & 0x00ff);
                s >>= 8;
            }
        else
            for (int i = 0; i < buf.length; i++) {
                buf[i] = (byte) (s & 0x00ff);
                s >>= 8;
            }
        return buf;
    }
}
