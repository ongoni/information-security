package md5;

public class MD5Hash {

    private int[] abcd;
    private int[] x;

    private static final byte[] padding = {
            (byte)(0x80), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

    private byte[] digest = new byte[16];
    private byte[] buffer = new byte[64];
    private int[] count = new int[2];

    private static int F(int x, int y, int z) {
        return (x & y) | (~x & z);
    }

    private static int G(int x, int y, int z) {
        return (x & z) | (y & ~z);
    }

    private static int H(int x, int y, int z) {
        return x ^ y ^ z;
    }

    private static int I(int x, int y, int z) {
        return y ^ (x | ~z);
    }

    private static int FF(int a, int b, int c, int d, int t, int x, int s) {
        a += F(b, c, d) + x + t;

        return ((a << s) | (a >>> (32 - s))) + b;
    }

    private static int GG(int a, int b, int c, int d, int t, int x, int s) {
        a += G(b, c, d) + x + t;

        return ((a << s) | (a >>> (32 - s))) + b;
    }

    private static int HH(int a, int b, int c, int d, int t, int x, int s) {
        a += H(b, c, d) + x + t;

        return ((a << s) | (a >>> (32 - s))) + b;
    }

    private static int II(int a, int b, int c, int d, int t, int x, int s) {
        a += I(b, c, d) + x + t;

        return ((a << s) | (a >>> (32 - s))) + b;
    }

    private static int toInt(byte b) {
        return b < 0 ? b & 0x7F + 128 : b;
    }

    private static String toHex(byte ib) {
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };

        char[] ob = new char[2];
        ob[0] = hexDigits[(ib >>> 4) & 0x0F];
        ob[1] = hexDigits[ib & 0x0F];

        return new String(ob);
    }

    private void transform(byte block[]) {
        int a = abcd[0];
        int b = abcd[1];
        int c = abcd[2];
        int d = abcd[3];

        int[] x = new int[16];
        decode(x, block);

        //round 1
        a = FF ( a, b, c, d, 0xd76aa478, x[0], 7);
        d = FF ( d, a, b, c, 0xe8c7b756, x[1], 12);
        c = FF ( c, d, a, b, 0x242070db, x[2], 17);
        b = FF ( b, c, d, a, 0xc1bdceee, x[3], 22);
        a = FF ( a, b, c, d, 0xf57c0faf, x[4], 7);
        d = FF ( d, a, b, c, 0x4787c62a, x[5], 12);
        c = FF ( c, d, a, b, 0xa8304613, x[6], 17);
        b = FF ( b, c, d, a, 0xfd469501, x[7], 22);
        a = FF ( a, b, c, d, 0x698098d8, x[8], 7);
        d = FF ( d, a, b, c, 0x8b44f7af, x[9], 12);
        c = FF ( c, d, a, b, 0xffff5bb1, x[10], 17);
        b = FF ( b, c, d, a, 0x895cd7be, x[11], 22);
        a = FF ( a, b, c, d, 0x6b901122, x[12], 7);
        d = FF ( d, a, b, c, 0xfd987193, x[13], 12);
        c = FF ( c, d, a, b, 0xa679438e, x[14], 17);
        b = FF ( b, c, d, a, 0x49b40821, x[15], 22);

        //round 2
        a = GG ( a, b, c, d, 0xf61e2562, x[1], 5);
        d = GG ( d, a, b, c, 0xc040b340, x[6], 9);
        c = GG ( c, d, a, b, 0x265e5a51, x[11], 14);
        b = GG ( b, c, d, a, 0xe9b6c7aa, x[0], 20);
        a = GG ( a, b, c, d, 0xd62f105d, x[5], 5);
        d = GG ( d, a, b, c, 0x2441453, x[10], 9);
        c = GG ( c, d, a, b, 0xd8a1e681, x[15], 14);
        b = GG ( b, c, d, a, 0xe7d3fbc8, x[4], 20);
        a = GG ( a, b, c, d, 0x21e1cde6, x[9], 5);
        d = GG ( d, a, b, c, 0xc33707d6, x[14], 9);
        c = GG ( c, d, a, b, 0xf4d50d87, x[3], 14);
        b = GG ( b, c, d, a, 0x455a14ed, x[8], 20);
        a = GG ( a, b, c, d, 0xa9e3e905, x[13], 5);
        d = GG ( d, a, b, c, 0xfcefa3f8, x[2], 9);
        c = GG ( c, d, a, b, 0x676f02d9, x[7], 14);
        b = GG ( b, c, d, a, 0x8d2a4c8a, x[12], 20);

        //round 3
        a = HH ( a, b, c, d, 0xfffa3942, x[5], 4);
        d = HH ( d, a, b, c, 0x8771f681, x[8], 11);
        c = HH ( c, d, a, b, 0x6d9d6122, x[11], 16);
        b = HH ( b, c, d, a, 0xfde5380c, x[14], 23);
        a = HH ( a, b, c, d, 0xa4beea44, x[1], 4);
        d = HH ( d, a, b, c, 0x4bdecfa9, x[4], 11);
        c = HH ( c, d, a, b, 0xf6bb4b60, x[7], 16);
        b = HH ( b, c, d, a, 0xbebfbc70, x[10], 23);
        a = HH ( a, b, c, d, 0x289b7ec6, x[13], 4);
        d = HH ( d, a, b, c, 0xeaa127fa, x[0], 11);
        c = HH ( c, d, a, b, 0xd4ef3085, x[3], 16);
        b = HH ( b, c, d, a, 0x4881d05, x[6], 23);
        a = HH ( a, b, c, d, 0xd9d4d039, x[9], 4);
        d = HH ( d, a, b, c, 0xe6db99e5, x[12], 11);
        c = HH ( c, d, a, b, 0x1fa27cf8, x[15], 16);
        b = HH ( b, c, d, a, 0xc4ac5665, x[2], 23);

        //round 4
        a = II ( a, b, c, d, 0xf4292244, x[0], 6);
        d = II ( d, a, b, c, 0x432aff97, x[7], 10);
        c = II ( c, d, a, b, 0xab9423a7, x[14], 15);
        b = II ( b, c, d, a, 0xfc93a039, x[5], 21);
        a = II ( a, b, c, d, 0x655b59c3, x[12], 6);
        d = II ( d, a, b, c, 0x8f0ccc92, x[3], 10);
        c = II ( c, d, a, b, 0xffeff47d, x[10], 15);
        b = II ( b, c, d, a, 0x85845dd1, x[1], 21);
        a = II ( a, b, c, d, 0x6fa87e4f, x[8], 6);
        d = II ( d, a, b, c, 0xfe2ce6e0, x[15], 10);
        c = II ( c, d, a, b, 0xa3014314, x[6], 15);
        b = II ( b, c, d, a, 0x4e0811a1, x[13], 21);
        a = II ( a, b, c, d, 0xf7537e82, x[4], 6);
        d = II ( d, a, b, c, 0xbd3af235, x[11], 10);
        c = II ( c, d, a, b, 0x2ad7d2bb, x[2], 15);
        b = II ( b, c, d, a, 0xeb86d391, x[9], 21);

        abcd[0] += a;
        abcd[1] += b;
        abcd[2] += c;
        abcd[3] += d;
    }

    private void update(byte[] source, int inputLength) {
        int i, index = (count[0] >>> 3) & 0x3F;

        if ((count[0] += (inputLength << 3)) < (inputLength << 3)) {
            count[1]++;
        }

        count[1] += (inputLength >>> 29);
        int partLength = 64 - index;

        if (inputLength >= partLength) {
            copy(buffer, source, index, 0, partLength);
            transform(buffer);

            for (i = partLength; i + 63 < inputLength; i += 64) {
                copy(buffer, source, 0, i, 64);
                transform(buffer);
            }

            index = 0;
        } else {
            i = 0;
        }

        copy(buffer, source, index, i, inputLength - i);
    }

    private void MD5Final() {
        byte[] bytes = new byte[8];

        encode(bytes, count, 8);

        int index = (count[0] >>> 3) & 0x3f;

        update(padding, (index < 56) ? (56 - index) : (120 - index));
        update(bytes, 8);
        encode(digest, abcd, 16);
    }

    private void copy(byte[] output, byte[] input, int outpos, int inpos, int length) {
        System.arraycopy(input, inpos, output, outpos, length);
    }

    private void encode(byte[] output, int[] input, int length) {
        for (int i = 0, j = 0; j < length; i++, j += 4) {
            output[j] = (byte) (input[i] & 0xffL);
            output[j + 1] = (byte) ((input[i] >>> 8) & 0xffL);
            output[j + 2] = (byte) ((input[i] >>> 16) & 0xffL);
            output[j + 3] = (byte) ((input[i] >>> 24) & 0xffL);
        }
    }

    private void decode(int[] output, byte[] input) {
        for (int i = 0, j = 0; j < 64; i++, j += 4) {
            output[i] = toInt(input[j])
                    | (toInt(input[j + 1]) << 8)
                    | (toInt(input[j + 2]) << 16)
                    | (toInt(input[j + 3]) << 24);
        }
    }

    public MD5Hash() {
        init();
    }

    private void init() {
        abcd = new int[4];
        x = new int[16];

        count[0] = 0;
        count[1] = 0;

        abcd[0] = 0x67452301;
        abcd[1] = 0xEFCDAB89;
        abcd[2] = 0x98BADCFE;
        abcd[3] = 0x10325476;
    }

    public String getHash(String source) {
        init();
        update(source.getBytes(), source.length());
        MD5Final();

        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            encoded.append(toHex(digest[i]));
        }

        return encoded.toString();
    }

}