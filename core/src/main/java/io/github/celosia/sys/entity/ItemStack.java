package io.github.celosia.sys.entity;

public class ItemStack extends IconEntity {

    private int count = 0;

    public ItemStack(String name, String desc, String icon) {
        super(name, desc, icon);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
