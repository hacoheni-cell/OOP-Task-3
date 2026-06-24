package BusinessLayer;
import java.util.Scanner;

    public class CLI {
        private Scanner scanner;

        public CLI() {
            this.scanner = new Scanner(System.in);
        }
        public void displayMessage(String message) {
            System.out.println(message);
        }

        public void displayBoard(String boardDisplay) {
            System.out.println(boardDisplay);
        }

        public void displayPlayerStats(String stats) {
            System.out.println(stats);
        }

        public String getInput() {
            return scanner.nextLine();
        }

        public void close() {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

