package paj.project5_vc.enums;

public enum TaskState {

    TODO(100),
    DOING(200),
    DONE(300);

    private final int value;

    TaskState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TaskState fromValue(int value) {
        for (TaskState state : TaskState.values()) {
            if (state.getValue() == value) {
                return state;
            }
        }
        throw new IllegalArgumentException("No such state with value: " + value);
    }
}