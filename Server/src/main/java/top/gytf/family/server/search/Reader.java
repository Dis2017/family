package top.gytf.family.server.search;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 阅读器<br>
 * CreateDate:  2021/12/12 11:59 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class Reader {
    private final static String TAG = Reader.class.getName();

    /**
     * 内容
     */
    private final char[] content;

    /**
     * 左边界
     */
    private final int left;

    /**
     * 右边界
     */
    private final int right;

    /**
     * 指针位置
     */
    private int pos;

    /**
     * 标记点<br>
     * 内部使用
     */
    private int mark;

    public Reader(String str) {
        content = str.toCharArray();
        left = 0; right = content.length;
        pos = 0;
        mark = -1;
    }

    /**
     * 标记当前指针位置
     */
    private void mark() {
        mark = pos;
    }

    /**
     * 返回标记位置，且清除标记
     */
    private void reset() {
        if (mark < left || mark >= right) {
            return;
        }
        pos = mark;
        mark = -1;
    }

    /**
     * 重置指针
     */
    public void resetPos() {
        pos = left;
    }

    /**
     * 读取一个字符
     * @return 字符
     */
    public char read() {
        if (pos < left || pos >= right) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return content[pos++];
    }

    /**
     * 跳过SIZE个字符
     * @param size 跳过个数
     */
    public void skips(int size) {
        pos += size;
        if (pos > right) {
            pos = right;
        }
    }

    /**
     * 还可以读取的字符数量
     * @return 还可以读取的字符数量
     */
    public long readable() {
        return right - pos;
    }

    /**
     * 剩下的内容是否以str开头
     * @param str str
     * @return 结果
     */
    public boolean startsWith(String str) {
        Reader reader = new Reader(str);
        if (reader.readable() > readable()) {
            return false;
        }

        mark();
        while (reader.readable() > 0) {
            if (reader.read() != read()) {
                reset();
                return false;
            }
        }
        reset();

        return true;
    }
}
