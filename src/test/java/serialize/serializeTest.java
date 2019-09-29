package serialize;

import util.JsonSerializationUtil;
import util.ProtoSerializationUtil;

/**
 * Created by TOM
 * On 2019/9/29 22:56
 */
public class serializeTest {

  public static void main(String[] args) {
    Person person = new Person(0, "dd", 45);

    byte[] serialize = ProtoSerializationUtil.INSTANCE.serialize(person);
    Person person1 = ProtoSerializationUtil.INSTANCE.deserialize(serialize, Person.class);
    System.out.println(serialize);
    System.out.println(person1);

    byte[] serialize1 = JsonSerializationUtil.INSTANCE.serialize(person);
    Person deserialize = JsonSerializationUtil.INSTANCE.deserialize(serialize, Person.class);
    System.out.println(serialize1);
    System.out.println(deserialize);
  }
}
