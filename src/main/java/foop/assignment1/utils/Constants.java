package foop.assignment1.utils;

public class Constants {
    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 2;
        public static final int GROUND = 2;
        public static final int HIT = 2;
        public static final int ATTACK_1 = 2;
        public static final int ATTACK_JUMP_1 = 2;
        public static final int ATTACK_JUMP_2 = 2;

        public static int GetSpriteAmount(int player_action) {
            switch (player_action) {
                case RUNNING:
                    return 6;
                case IDLE:
                    return 5;
                case HIT:
                    return 3;
                default:
                    return 1;
            }
        }
    }

}