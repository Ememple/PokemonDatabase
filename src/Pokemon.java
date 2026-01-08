public class Pokemon {
    private int id;
    private int trainerId;
    private String nickname;
    private String rarity;

    public Pokemon(int trainerId, String nickname, String rarity) {
        this.trainerId = trainerId;
        this.nickname = nickname;
        this.rarity = rarity;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRarity() {
        return rarity;
    }
}
