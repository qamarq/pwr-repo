import java.lang.reflect.Field;

public class Debug {
    public static void fields(Object o) {
        Class<?> c = o.getClass();
        Field[] fields = c.getDeclaredFields();

        for (Field f : fields) {
            f.setAccessible(true);
            try {
                System.out.println(
                    f.getName() + ", " + f.getType().getSimpleName() + ", " + f.get(o)
                );
            } catch (IllegalAccessException e) {
                System.out.println("brak dostepu");
            }
        }
    }
}
