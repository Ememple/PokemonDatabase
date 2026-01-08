public class Trainer {
    private int id;
    private String name;
    private float experiencePoints;
    private boolean isGymLeader;

    public Trainer(int id, String name, float xp, boolean isLeader) {
        this.id = id;
        this.name = name;
        this.experiencePoints = xp;
        this.isGymLeader = isLeader;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getXp() {
        return experiencePoints;
    }

    public boolean isLeader() {
        return isGymLeader;
    }
}