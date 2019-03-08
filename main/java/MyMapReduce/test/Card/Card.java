package MyMapReduce.test.Card;

import org.apache.hadoop.io.WritableComparable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

//用于比较 且当做key来使用 则定义ta
public class Card implements WritableComparable<Card> {

private String city;
private String card;

   public Card(){}

    public Card(String city, String card) {
        this.city = city;
        this.card = card;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return city+card;
    }

//这个阶段是在洗牌时候用的
    public int compareTo(Card o) {

        if(city.equals(o.city))
        {
            if(card.equals(o.card))
            {
                return 0;
            }
            else
            {
                return card.compareTo(o.card);
            }
        }
        else
        {
            return city.compareTo(o.city);
        }

    }

    public void write(DataOutput out) throws IOException {
            out.writeUTF(city);
            out.writeUTF(card);
    }

    public void readFields(DataInput in) throws IOException {

        this.city=in.readUTF();
        this.card=in.readUTF();
    }
}
