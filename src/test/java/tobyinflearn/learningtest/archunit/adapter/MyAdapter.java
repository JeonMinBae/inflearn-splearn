package tobyinflearn.learningtest.archunit.adapter;

import tobyinflearn.learningtest.archunit.application.MyService;


public class MyAdapter {
    MyService myService;

    void run(){
        myService = new MyService();
        System.out.println("myService = " + myService);
    }
}
