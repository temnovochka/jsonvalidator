public class Main {

    public static void main(String[] args) {
        try {
            JSONValidator validator = new JSONValidator();
            validator.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
