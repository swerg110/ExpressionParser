package expression.parser;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class StringSource implements CharSource {
    private final String data;
    private int pos;

    public String getData(){
        return data;
    }

    public StringSource(final String data) {
        this.data = data + " ";

    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    public int getPosIndex(){
        return pos;
    }

    public char getNextChar(){
//        System.out.println(getData());
        while (hasNext()){
            char nextChar = next();
//            System.out.println(nextChar);
            if (!Character.isWhitespace(nextChar)){
                return nextChar;
            }
        }
        return '@';

    }

    @Override
    public char prevChar(){

        return pos < 1 ? '#' : data.charAt(pos - 2);
    }

    @Override
    public void skipWhitespace() {
        while (hasNext() && Character.isWhitespace(data.charAt(pos))) {
            pos++;
        }
    }


    @Override
    public IllegalArgumentException error(final String message) {
        return new IllegalArgumentException(pos + ": " + message);
    }
}
