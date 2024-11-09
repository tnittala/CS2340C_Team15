class Item {
    protected String name;
    protected double price;
    protected int quantity;
    protected DiscountType discountType;
    protected double discountAmount;

    public Item(String name, double price, int quantity, DiscountType discountType, double discountAmount) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
    }

    public double calculateDiscountedPrice() {
        double discountedPrice = price;
        if (discountType == DiscountType.PERCENTAGE) {
            discountedPrice -= discountAmount * price;
        } else if (discountType == DiscountType.AMOUNT) {
            discountedPrice -= discountAmount;
        }
        return discountedPrice * quantity;
    }
    
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }
}
