package adbund.com.rxjava2_test;

/**
 * 类描述：
 * Created by 殴打小熊猫 on 2016/12/22.
 */

public class BookList {
    private String bookName;
    private double bookPrice;
    private int bookNumbers;

    public BookList(String bookName, int bookNumbers, double bookPrice) {
        this.bookName = bookName;
        this.bookNumbers = bookNumbers;
        this.bookPrice = bookPrice;
    }

    @Override
    public String toString() {
        return "BookList{" +
                "bookName='" + bookName + '\'' +
                ", bookPrice=" + bookPrice +
                ", bookNumbers=" + bookNumbers +
                '}';
    }
}
