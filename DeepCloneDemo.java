import java.lang.reflect.Field;

class Address {
    String city;
    String state;

    public Address(String city, String state) {
        this.city = city;
        this.state = state;
    }
}

class Person implements Cloneable {
    String name;
    int age;
    Address address;

    public Person(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    // Custom deep clone using Reflection
    @Override
    public Person clone() {
        try {
            Person cloned = (Person) super.clone();
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(this);
                if (value != null && !field.getType().isPrimitive() && !(value instanceof String)) {
                    // Create new instance of the object (deep copy)
                    Object copy = field.getType().getConstructor(
                            value.getClass().getDeclaredConstructors()[0].getParameterTypes()
                    ).newInstance(((Address) value).city, ((Address) value).state);
                    field.set(cloned, copy);
                }
            }
            return cloned;
        } catch (Exception e) {
            throw new RuntimeException("Cloning failed", e);
        }
    }
}

public class DeepCloneDemo {
    public static void main(String[] args) {
        Address addr = new Address("Chennai", "Tamil Nadu");
        Person p1 = new Person("Arjun", 22, addr);

        Person p2 = p1.clone();

        // Modify cloned object’s address
        p2.address.city = "Bangalore";

        System.out.println("Original Person City: " + p1.address.city);
        System.out.println("Cloned Person City: " + p2.address.city);
    }
              }
