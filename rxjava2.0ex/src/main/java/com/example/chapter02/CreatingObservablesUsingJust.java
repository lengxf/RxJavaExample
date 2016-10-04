package com.example.chapter02;


import com.example.common.Program;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Demonstrates using Observable.just for creating Observables.
 *
 * @author meddle
 */
public class CreatingObservablesUsingJust implements Program {

    public static void main(String[] args) {
        new CreatingObservablesUsingJust().run();
    }

    @Override
    public String name() {
        return "Using the Observable.just method to create Observables";
    }

    @Override
    public int chapter() {
        return 2;
    }

    @Override
    public void run() {
        Observable.just('S').subscribe(new Consumer<Character>() {
            @Override
            public void accept(Character character) throws Exception {
                System.out.print(character);
            }
        });

        Observable.just('R', 'x', 'J', 'a', 'v', 'a').subscribe(
                new Consumer<Character>() {
                    @Override
                    public void accept(Character character) throws Exception {
                        System.out.print(character);
                    }
                }
                , new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.err.println(throwable);
                    }
                }
                , new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println();
                    }
                }
        );

        Observable.just('R', 'x', 'J', 'a', 'v', 'a').subscribe(
                System.out::print, System.err::println, System.out::println);

        Observable.just(new User("Dali", "Bali"))
                .map(new Function<User, String>() {
                    @Override
                    public String apply(User user) throws Exception {
                        return user.getForename() + " " + user.getLastname();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                });

        Flowable.just('R', 'x', 'J', 'a', 'v', 'a').subscribe(
                System.out::print, System.err::println, System.out::println);

        Observable.just(new User("Dali", "Bali"))
                .map(u -> u.getForename() + " " + u.getLastname())
                .subscribe(System.out::println);
    }

    public static class User {

        private final String forename;
        private final String lastname;

        public User(String forename, String lastname) {
            this.forename = forename;
            this.lastname = lastname;
        }

        public String getForename() {
            return this.forename;
        }

        public String getLastname() {
            return this.lastname;
        }

    }

}
