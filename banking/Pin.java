package banking;

public class Pin {
    int number = Pin();
    int randomPin = number;
    public int Pin() {
        return 1000 + (int) (Math.random() * ((9999 - 1000) + 1));
    }

    public void PrintPin() {
        System.out.println("Your card PIN: " + "\n" + randomPin);
    }
}
