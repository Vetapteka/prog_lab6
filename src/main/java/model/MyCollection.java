package model;

import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyCollection {
    private final Hashtable<Integer, Flat> flats;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public MyCollection(Hashtable<Integer, Flat> flats) {
        this.flats = flats;
    }

    public void put(Integer id, Flat flat) {
        Lock writeLock = rwLock.writeLock();
        writeLock.lock();

        try {
            flats.put(id, flat);
        } finally {
            writeLock.unlock();
        }
    }

    public boolean containsKey(Integer id) {
        Lock readLock = rwLock.readLock();
        readLock.lock();

        try {
            return flats.containsKey(id);
        } finally {
            readLock.unlock();
        }
    }

    public void clear(Set<Integer> flatsId) {
        Lock writeLock = rwLock.writeLock();
        writeLock.lock();

        try {
            flats.entrySet().removeIf(x -> flatsId.contains(x.getKey()));
        } finally {
            writeLock.unlock();
        }
    }


    public long countByHouseName(House house) {
        Lock readLock = rwLock.readLock();
        readLock.lock();
        try {
            return flats.values().stream().map(Flat::getHouse)
                    .filter(x -> x != null && x.compare(house)).count();
        } finally {
            readLock.unlock();
        }
    }

    public void filterStartName(String strStartName, StringBuilder sb) {
        Lock readLock = rwLock.readLock();
        readLock.lock();

        try {
            flats.keySet().stream().map(flats::get).filter(x -> x.getName().startsWith(strStartName)).forEach(sb::append);

        } finally {
            readLock.unlock();
        }

    }

    public String info() {
        Lock readLock = rwLock.readLock();
        readLock.lock();

        try {
            return "type: " + flats.getClass().toString() + "\n" +
                    "number of elements: " + flats.size() + "\n";
        } finally {
            readLock.unlock();
        }
    }

    public void printField(StringBuilder sb) {
        Lock readLock = rwLock.readLock();
        readLock.lock();

        try {
            flats.keySet().stream().map(x -> flats.get(x).getFurnish())
                    .sorted(new FurnishComparator()).forEach(x -> sb.append(x).append(" "));

        } finally {
            readLock.unlock();
        }
    }

    public void remove(Integer id) {
        Lock writeLock = rwLock.writeLock();
        writeLock.lock();

        try {
            flats.remove(id);
        } finally {
            writeLock.unlock();
        }
    }

    public Flat get(Integer id) {
        Lock readLock = rwLock.readLock();
        readLock.lock();

        try {
            return flats.get(id);

        } finally {
            readLock.unlock();
        }
    }

    public void replace(Integer id, Flat flat) {
        Lock writeLock = rwLock.writeLock();
        writeLock.lock();

        try {
            flats.replace(id, flat);
        } finally {
            writeLock.unlock();
        }
    }

    public boolean isEmpty() {
        Lock readLock = rwLock.readLock();
        readLock.lock();

        try {
            return flats.isEmpty();

        } finally {
            readLock.unlock();
        }
    }

    public void show(StringBuilder sb) {
        Lock readLock = rwLock.readLock();
        readLock.lock();

        try {
            flats.keySet().stream().map(flats::get).forEach(x -> sb.append(x.toString()).append("\n"));

        } finally {
            readLock.unlock();
        }
    }
}

