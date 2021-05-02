package banking;


import java.util.Arrays;

public class Code {

    long numberCard = cardNumberRandom();
    static long d;
    public Long cardNumberRandom() {
        long number = 4000000000000000L + (long) (Math.random() * ((4000009000000000L - 4000000000000000L) + 1));
        long checksum;
        String temp = Long.toString(number);
        int[] newGuess = new int[temp.length()];
        int sum = 0;
        for (int i = 0; i < temp.length() - 1; i++) {
            newGuess[i] = temp.charAt(i) - '0';
            if (i % 2 == 0)  {
                newGuess[0] = 8;
                newGuess[i] = newGuess[i] * 2;
            } if (newGuess[i] > 9) {
            newGuess[i] = newGuess[i] - 9;
        }
            sum = sum + newGuess[i];
        }
        sum = sum + 1;
        if (sum % 10 == 0) {
            checksum = 0;
        } else {
            checksum = 10 - (sum % 10);
        }
        long[] last = new long[temp.length()];
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < temp.length(); i++) {
            last[i] = temp.charAt(i) - '0';
            last[15] = checksum;
            sb.append(last[i]);
        }
        return Long.parseLong(sb.toString());
    }



    public void PrintCard() {
        System.out.println("Your card number: " + "\n" + numberCard);
    }
}
