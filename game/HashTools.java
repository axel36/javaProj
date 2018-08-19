package ru.nbki;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.security.*;

public class HashTools {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    static final protected ThreadLocal<MessageDigest> digests = new ThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() {
            return createDigest();
        }
    };

    protected static MessageDigest getDigest() {
        return digests.get();
    }
    public static MessageDigest createDigest() {
        try {
            return MessageDigest.getInstance("GOST3411");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    protected static byte[] longTohash(long num, MessageDigest digest) {
        return digest.digest(String.format("%010d", num).getBytes(UTF_8));
    }

    public static String hash(String value) {
       return bytesToHex(getDigest().digest(value.getBytes(UTF_8)));
    }

    public static String hash(String value, final MessageDigest digest) {
        return bytesToHex(digest.digest(value.getBytes(UTF_8)));
    }


    protected static int compare(byte[] a, int aOffset, byte[] b, int bOffset, int len) {
        for (int i = 0; i < len; i++) {
            int a1 = (0xff & a[aOffset + i]);
            int b1 = (0xff & b[bOffset + i]);
            if (a1 != b1)
                return a1 - b1;
        }
        return 0;
    }

    protected static int compareUnsafe32(byte[] a, byte[] b) {
        return UnsafeComparer.compareTo32(a, 0, b, 0);
    }

    protected static int compareJava(byte[] a, byte[] b, int len) {
        for (int i = 0; i < len; i++) {
            int a1 = (0xff & a[i]);
            int b1 = (0xff & b[i]);
            if (a1 != b1)
                return a1 - b1;
        }
        return 0;
    }

    public static int[] hexToInts(String value) {
        int[] ints = new int[8];
        for (int i = 0; i < 8; i++) {
            ints[i] =
                    ((value.charAt(i * 8) - 0x61) << 28) +
                            ((value.charAt(i * 8 + 1) - 0x61) << 24) +
                            ((value.charAt(i * 8 + 2) - 0x61) << 20) +
                            ((value.charAt(i * 8 + 3) - 0x61) << 16) +
                            ((value.charAt(i * 8 + 4) - 0x61) << 12) +
                            ((value.charAt(i * 8 + 5) - 0x61) << 8) +
                            ((value.charAt(i * 8 + 6) - 0x61) << 4) +
                            (value.charAt(i * 8 + 7) - 0x61);
        }
        return ints;
    }

    public static String bytesToHex(byte[] digest) {
        char[] fb = new char[64];
        for (int i = 0; i < 32; i++) {
            fb[i * 2] = (char) (0x61 + (((int) digest[i] & 0xFF) >> 4));
            fb[i * 2 + 1] = (char) (0x61 + ((int) digest[i] & 0x0F));
        }
        return new String(fb);
    }

    public static byte[] hexToBytes(String digest) {
        byte[] bytes = new byte[32];
        for (int i = 0; i < 32; i++) {
            bytes[i] = (byte) (
                    ((digest.charAt(i * 2) - 0x61) << 4) +
                            digest.charAt(i * 2 + 1) - 0x61);
        }
        return bytes;
    }

    public static String intsToHex(int[] ints) {
        if (ints == null)
            return "";

        char[] fb = new char[64];
        for (int i = 0; i < 8; i++) {
            int anInt = ints[i];
            int n = i * 8;
            fb[n] = (char) (0x61 + (((anInt >> 28) & 0x0F)));
            fb[n + 1] = (char) (0x61 + ((anInt >> 24) & 0x0F));
            fb[n + 2] = (char) (0x61 + (((anInt >> 20) & 0x0F)));
            fb[n + 3] = (char) (0x61 + ((anInt >> 16) & 0x0F));
            fb[n + 4] = (char) (0x61 + (((anInt >> 12) & 0x0F)));
            fb[n + 5] = (char) (0x61 + ((anInt >> 8) & 0x0F));
            fb[n + 6] = (char) (0x61 + (((anInt >> 4) & 0x0F)));
            fb[n + 7] = (char) (0x61 + (anInt & 0x0F));
        }
        return new String(fb);
    }

    public static boolean isValidHex(String s) {
        if (s == null || s.length() != 64)
            return false;
        for (int i = 0; i < 64; i++) {
            char c = s.charAt(i);
            if (c < 'a' || c > ('p'))
                return false;
        }
        return true;
    }

    public static String hexOf(String value) {
        return bytesToHex(getDigest().digest(value.getBytes(UTF_8)));
    }

    public enum UnsafeComparer {
        INSTANCE;

        private static class Longs {
            static final int BYTES = 8;
        }

        static final Unsafe theUnsafe;

        /**
         * The offset to the first element in a byte array.
         */
        static final int BYTE_ARRAY_BASE_OFFSET;

        static {
            theUnsafe = (Unsafe) AccessController.doPrivileged(
                    new PrivilegedAction<Object>() {
                        @Override
                        public Object run() {
                            try {
                                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                                f.setAccessible(true);
                                return f.get(null);
                            } catch (NoSuchFieldException e) {
                                // It doesn't matter what we throw;
                                // it's swallowed in getBestComparer().
                                throw new Error();
                            } catch (IllegalAccessException e) {
                                throw new Error();
                            }
                        }
                    });

            BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);

            // sanity check - this should never fail
            if (theUnsafe.arrayIndexScale(byte[].class) != 1) {
                throw new AssertionError();
            }
        }

        static {
            if (!ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN))
                throw new UnsupportedOperationException("Only LITTLE_ENDIAN processors supported");
        }

        /**
         * Returns true if x1 is less than x2, when both values are treated as
         * unsigned.
         */
        static boolean lessThanUnsigned(long x1, long x2) {
            return (x1 + Long.MIN_VALUE) < (x2 + Long.MIN_VALUE);
        }

        /**
         * original source on https://svn.apache.org/repos/asf/cassandra/trunk/src/java/org/apache/cassandra/utils/FastByteComparisons.java
         * Lexicographically compare two arrays.
         *
         * @param buffer1 left operand
         * @param buffer2 right operand
         * @param offset1 Where to start comparing in the left buffer
         * @param offset2 Where to start comparing in the right buffer
         * @param length  How much to compare from the buffers
         * @return 0 if equal, < 0 if left is less than right, etc.
         */

        public static int compareTo(byte[] buffer1, int offset1,
                                    byte[] buffer2, int offset2, int length) {
          /*  // Short circuit equal case
            if (buffer1 == buffer2 &&
                    offset1 == offset2 &&
                    length1 == length2) {
                return 0;
            }*/
            int minWords = length / Longs.BYTES;
            int offset1Adj = offset1 + BYTE_ARRAY_BASE_OFFSET;
            int offset2Adj = offset2 + BYTE_ARRAY_BASE_OFFSET;

        /*
         * Compare 8 bytes at a time. Benchmarking shows comparing 8 bytes at a
         * time is no slower than comparing 4 bytes at a time even on 32-bit.
         * On the other hand, it is substantially faster on 64-bit.
         */
            for (int i = 0; i < minWords * Longs.BYTES; i += Longs.BYTES) {
                long lw = theUnsafe.getLong(buffer1, offset1Adj + (long) i);
                long rw = theUnsafe.getLong(buffer2, offset2Adj + (long) i);
                long diff = lw ^ rw;

                if (diff != 0) {
                    // Use binary search
                    int n = 0;
                    int y;
                    int x = (int) diff;
                    if (x == 0) {
                        x = (int) (diff >>> 32);
                        n = 32;
                    }

                    y = x << 16;
                    if (y == 0) {
                        n += 16;
                    } else {
                        x = y;
                    }

                    y = x << 8;
                    if (y == 0) {
                        n += 8;
                    }
                    return (int) (((lw >>> n) & 0xFFL) - ((rw >>> n) & 0xFFL));
                }
            }
            return 0;
        }

        public static int compareTo32(byte[] buffer1, int offset1,
                                      byte[] buffer2, int offset2) {

            int offset1Adj = offset1 + BYTE_ARRAY_BASE_OFFSET;
            int offset2Adj = offset2 + BYTE_ARRAY_BASE_OFFSET;

        /*
         * Compare 8 bytes at a time. Benchmarking shows comparing 8 bytes at a
         * time is no slower than comparing 4 bytes at a time even on 32-bit.
         * On the other hand, it is substantially faster on 64-bit.
         */
            for (int i = 0; i < 32; i += 8) {
                long lw = theUnsafe.getLong(buffer1, offset1Adj + (long) i);
                long rw = theUnsafe.getLong(buffer2, offset2Adj + (long) i);
                long diff = lw ^ rw;

                if (diff != 0) {

                    // Use binary search
                    int n = 0;
                    int y;
                    int x = (int) diff;
                    if (x == 0) {
                        x = (int) (diff >>> 32);
                        n = 32;
                    }

                    y = x << 16;
                    if (y == 0) {
                        n += 16;
                    } else {
                        x = y;
                    }

                    y = x << 8;
                    if (y == 0) {
                        n += 8;
                    }
                    return (int) (((lw >>> n) & 0xFFL) - ((rw >>> n) & 0xFFL));
                }
            }
            return 0;
        }
    }


}
