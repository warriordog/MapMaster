package net.acomputerdog.map.tool;

public class ColorCalc {

    public static int getColor(int alpha, int red, int green, int blue) {
        return (alpha << 24) + (red << 16) + (green << 8) + (blue);
    }

    public static void main(String[] args) {
        if (args.length >= 3) {
            try {
                int red = Integer.parseInt(args[0]);
                int green = Integer.parseInt(args[1]);
                int blue = Integer.parseInt(args[2]);
                int alpha = 255;
                if (args.length >= 4) {
                    alpha = Integer.parseInt(args[3]);
                }
                System.out.println(getColor(alpha, red, green, blue));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, all parameters must be integers.");
            }
        } else {
            System.out.println("Usage: cc <red> <green> <blue> [alpha]");
        }
    }
}
