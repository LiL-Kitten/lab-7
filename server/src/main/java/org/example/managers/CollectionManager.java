package org.example.managers;

import org.example.data.Person;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CollectionManager {

    private static long idCounter;
    private LocalDate lastInitDate;
    private LocalDate lastSaveDate;
    private List<Person> collection;

    public CollectionManager() {
        collection = Collections.synchronizedList(new LinkedList<>()); // Синхронизированная коллекция
        lastInitDate = LocalDate.now();
    }

    public static long getTotalID() {
        return idCounter;
    }

    public static long getNextID() {
        return ++idCounter;
    }

    public void initializeDB(Person person) {
        if (person.getCreationDate() == null) {
            person.setCreationDate(LocalDate.now());
        } else {
            person.setCreationDate(person.getCreationDate());
        }
        this.collection.add(person);
        sortCollectionByID();
        setIdCounter(person.getID());
    }

    public void addElement(Person person) {
        person.setID(getNextID());
        if (person.getCreationDate() == null) {
            person.setCreationDate(LocalDate.now());
        } else {
            person.setCreationDate(person.getCreationDate());
        }
        this.collection.add(person);
        sortCollectionByID();

    }

    public void setIdCounter(long idCounter) {
        if (idCounter < CollectionManager.idCounter) {
            System.out.println("gjitk d gjge");
        } else {
            CollectionManager.idCounter = idCounter;
        }
    }

    public boolean checkID(long id) {
        for (Person person : collection) {
            if (person.getID() == id) {
                return true;
            }
        }
        return false;
    }

    private void sortCollectionByID() {
        collection.sort(Comparator.comparingLong(Person::getID));
    }

    public void removeByID(long id) {
        collection = collection.stream()
                .filter(person -> person.getID() != id)
                .collect(Collectors.toList());
    }

    public void removeElements(Collection<Person> elements) {

        collection.removeAll(elements);

    }

    public Person getFirst() {

        if (collection.isEmpty()) {
            throw new NoSuchElementException("Collection is empty");
        }
        return collection.get(0);

    }

    public Person removeFirst() {

        if (collection.isEmpty()) {
            throw new NoSuchElementException("Collection is empty");
        }
        return collection.remove(0);

    }

    public void updateId(long id, long newId) {

        collection.stream()
                .filter(person -> person.getID() == id)
                .findFirst()
                .ifPresent(person -> person.setID(newId));
        sortCollectionByID();

    }

    public Collection<Person> getCollection() {
        return Collections.unmodifiableList(collection); // Возвращаем немодифицируемую копию коллекции
    }

    public Integer collectionSize() {
        System.out.println(collection.size());
        return collection.size();
    }

    public String collectionType() {
        return collection.getClass().getSimpleName();
    }

    public LocalDate getLastInitDate() {
        return lastInitDate;
    }

    public LocalDate getLastSaveDate() {
        return lastSaveDate;
    }

    public void setLastSaveDate() {
        this.lastSaveDate = LocalDate.now();
    }

    public void clear() {

        collection.clear();

    }

}