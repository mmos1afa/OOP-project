public class InputOutput {
    public static int HandleInput( String input ) {
        while (true)

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("invalid choice ");
            }

    }
}