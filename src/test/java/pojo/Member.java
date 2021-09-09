package pojo;

import com.google.gson.GsonBuilder;

public final class Member {
    private final String firstName;
    private final String key;

    public Member(String firstName, String key) {
        this.firstName = firstName;
        this.key = key;
    }

    public String toStringJson() {
        return new GsonBuilder().create().toJson(this);
    }
}
