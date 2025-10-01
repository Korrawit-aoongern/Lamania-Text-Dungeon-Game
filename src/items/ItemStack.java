package src.items;

public class ItemStack {
    private final Item item;
    private int count;

    public ItemStack(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public Item getItem() { return item; }
    public int getCount() { return count; }
    public void add(int n) { count = Math.min(99, count + n); }
    public void remove(int n) { count = Math.max(0, count - n); }
}
