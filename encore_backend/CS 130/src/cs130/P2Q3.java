package cs130;

import java.util.Random;

class P2Q3 {

    public static void main(String[] args) {
        loop: for (int i = -10; i <= 10; i++) {
            for (int j = -10; j <= 10; j++) {
                try {
                    getRandomInteger(i, j);
                } catch (Exception e) {
                    continue loop;
                }
            }
        }
    }

    private static int getRandomInteger(int a, int b) {
        int size = a;
        int index = b;

        int value = 2 * a - 1;
        if (a * a < value) {
            System.out.println("A");
            size++;
        } else {
            System.out.println("B");
            size--;
        }

        Random rand = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(i + 1) + 1;
        }

        return value > array[index] ? value : array[index];
    }

}
