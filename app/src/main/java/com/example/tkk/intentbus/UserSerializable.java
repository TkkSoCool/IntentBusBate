package com.example.tkk.intentbus;

import java.io.Serializable;
import java.util.List;

/**
 * Created  on 2018/5/2
 *
 * @author 唐开阔
 * @describe
 */
public class UserSerializable implements Serializable {
    String name = "name";
    int age;
    List<String> books;

    public UserSerializable() {
    }


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
