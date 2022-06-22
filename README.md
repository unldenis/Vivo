# Vivo

Vivo is the embryo of a dynamically typed programming language. The following compiler converts the source code into Java code.
'Vivo' in Italian meaning alive, the idea is reflected in the use of the word 'morto' as a type without value, often called 'null' in other languages.
In <a href="https://github.com/unldenis/Vivo/blob/master/src/main/resources/test.vivo">this</a> simple example, the following code is compiled and translated as follows:
```java
Object i = Math.sqrt((double)25.0);
public void onEnable() {
    int i100 = 0;
    while(i100 < (int)5.0)
    {
        System.out.println(i);
        i100++;
    }
}
public Object add(Object a,Object b) {
    if(true) {

        return VivoUtils.sum(a, b );
    }
    return null;
}

```

The language was created following <a href="https://craftinginterpreters.com">this</a> book.
