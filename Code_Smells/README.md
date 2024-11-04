# Code Smells
Code Smells:

1)  Lazy Class: Delete EmailSender class because it is never used and its functionality is covered by a separate method.
2) Bloater: class order.java; is excessively long
3) Object Oriented Abusers: Switch statement in calculateTotalPrice()
4) Data Class: Item.java only contains getters, setters, and fields

The code smells we found in the code are a lazy class, a bloater class, object oriented abusers, and a data class. The lazy class is the EmailSender class because it is never used and all of its functionality is covered by another method. The bloater class is Order.java because it is excessively long and can be split up into different methods. The object oriented abuser is the switch statement in calculateTotalPrice(). The data class is Item.java because it only contains getters, setters, and fields, and has no functionality. 

How we fixed the code smells:

We will fix the lazy class, EmailSender.java, by creating an inline class because it is a class that does almost nothing and isn’t responsible for anything. We will fix the bloater method by extracting parts of the method. We will fix the switch statement by extracting parts and implementing polymorphism using method overrides. We will fix the data class, Item.java, by extracting the method or encapsulating the class.

