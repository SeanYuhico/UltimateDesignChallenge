package Model;

public class Follower {
    public static final String TABLE_NAME = "followers";

    public static final String COL_FOLLOWER = "Follower";
    public static final String COL_FOLLOWING = "Following";
    public static final String COL_PK = "PK";

    private String follower;
    private String following;
    public static double PK = 0;

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }
}
