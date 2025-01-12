package common.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pair<T extends Comparable<T>, K extends Comparable<K>> implements Comparable<Pair<T, K>>{
    private T left;
    private K right;


    public static <t extends Comparable<t>, k extends Comparable<k>> Pair<t, k> of(t left, k right) {
        return new Pair<>(left, right);
    }

    @Override
    public int compareTo(Pair<T, K> o) {
        if(this.left.equals(o.getLeft())) {
            return this.right.compareTo(o.getRight());
        } else return this.left.compareTo(o.getLeft());
    }
}
