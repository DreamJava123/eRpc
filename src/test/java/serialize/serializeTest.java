package serialize;

import enums.SerializerAlogrithm;
import java.util.Map;
import util.ProtoSerializationUtil;

/**
 * Created by TOM
 * On 2019/9/29 22:56
 */
public class serializeTest {

  public static void main(String[] args) {
    Person person = new Person(0, "dd", 45);

    byte[] serialize = ProtoSerializationUtil.getInstance().serialize(person);
    Person person1 = ProtoSerializationUtil.getInstance().deserialize(serialize, Person.class);
    System.out.println(serialize);
    System.out.println(person1);

    son son = new son("1", 1);
    son1 son1 = new son1("2", 2);

    Map test = person.test();
    System.out.println(test);
    System.out.println(SerializerAlogrithm.JSON.getCode());

  }
}
