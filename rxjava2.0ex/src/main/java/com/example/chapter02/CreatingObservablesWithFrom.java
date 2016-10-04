package com.example.chapter02;

import com.example.common.Program;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


/**
 * A set of examples of using Observable.from.
 * 
 * @author meddle
 */
public class CreatingObservablesWithFrom implements Program {

	public static Consumer<?> N = (v) -> {
	};
	public static Consumer<Throwable> NE = (e) -> {
	};

	@Override
	public String name() {
		return "Creating Observables with Observable.from";
	}

	@Override
	public int chapter() {
		return 3;
	}

	@Override
	public void run() {
		// from(list)
		List<String> list = Arrays.asList("blue", "red", "green", "yellow",
				"orange", "cyan", "purple");

		Observable<String> listObservable = Observable.fromIterable(list);
		listObservable.subscribe(System.out::println);
		listObservable.subscribe(color -> System.out.print(color + "|"), NE,
				System.out::println);
		listObservable.subscribe(color -> System.out.print(color + "/"), NE,
				System.out::println);

		// from(Iterable)
		Path resources = Paths.get("src", "main", "resources");
		try {
			DirectoryStream<Path> dStream = Files.newDirectoryStream(resources);
			Observable<Path> dirObservable = Observable.fromIterable(dStream);
			dirObservable.subscribe(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// from(array)
		Observable<Integer> arrayObservable = Observable.fromArray(new Integer[] {
				3, 5, 8 });
		arrayObservable.subscribe(System.out::println);
	}
	
	public static void main(String[] args) {
		new CreatingObservablesWithFrom().run();
	}

}
