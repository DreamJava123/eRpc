package serialize;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by TOM
 * On 2019/9/29 22:54
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Person {

  private static Map concurrentHashMap = new ConcurrentHashMap<String, Integer>(16);
  private Integer id;
  private String name;
  private Integer age;

  public Person(String name, Integer age) {
    concurrentHashMap.put(name, age);
  }

  public Map test() {
    return concurrentHashMap;
  }
}
