package com.example.tkk.intentbus;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created  on 2018/4/13
 *
 * @author 唐开阔
 * @describe
 */
public class UserParcelable implements Parcelable{
    String name = "name";
    int age;
    List<String> books;

    public UserParcelable() {
    }

    protected UserParcelable(Parcel in) {
        name = in.readString();
        age = in.readInt();
        books = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeStringList(books);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserParcelable> CREATOR = new Creator<UserParcelable>() {
        @Override
        public UserParcelable createFromParcel(Parcel in) {
            return new UserParcelable(in);
        }

        @Override
        public UserParcelable[] newArray(int size) {
            return new UserParcelable[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getBooks() {
        return books;
    }

    public void setBooks(List<String> books) {
        this.books = books;
    }
}
