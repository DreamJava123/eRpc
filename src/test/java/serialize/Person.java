package serialize;

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
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Person {

  private Integer id;
  private String name;
  private Integer age;

}
