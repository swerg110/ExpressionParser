package expression.parser;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface CharSource {
    boolean hasNext();
    char next();

    char prevChar();
    String getData();

    char getNextChar();

    void skipWhitespace();

    IllegalArgumentException error(String message);

    int getPosIndex();
}
