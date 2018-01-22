package com.synerise.sdk.sample.view.events.widgets.recycler.adapter;



public class Customer {

    private final String name, lastName, nickname;
    private final int age;

    // ****************************************************************************************************************************************

    public Customer(String name, String lastName, String nickname, int age) {
        this.name = name;
        this.lastName = lastName;
        this.nickname = nickname;
        this.age = age;
    }

    // ********************************************************************************************************************************

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAge() {
        return age;
    }

    // ****************************************************************************************************************************************

    @Override
    public String toString() {
        return "Customer name: " + name + ", last name: " + lastName + ", nickname: " + nickname + ", age: " + age;
    }
}