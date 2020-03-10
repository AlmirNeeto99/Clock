package Util;

import java.util.ArrayList;

public class EntryTable {

    private ArrayList<Entry> entries = new ArrayList();

    public EntryTable() {

    }

    public ArrayList<Entry> get_entries() {
        return entries;
    }

    public boolean add_entry(String hostname, String type) {
        if (verify_exists(hostname)) {
            return false;
        }
        entries.add(new Entry(hostname, type));
        return true;
    }

    public boolean exists(String hostname) {
        return verify_exists(hostname);
    }

    public boolean remove_entry(String hostname) {
        if (verify_exists(hostname)) {
            remove(hostname);
            return true;
        }
        return false;
    }

    private void remove(String hostname) {
        int pos = 0;
        for (Entry entry : entries) {
            if (entry.get_ip().equals(hostname)) {
                entries.remove(pos);
            }
            pos++;
        }
    }

    private boolean verify_exists(String hostname) {
        for (Entry entry : entries) {
            if (entry.get_ip().equals(hostname)) {
                return true;
            }
        }
        return false;
    }

    private class Entry {

        private String hostname;
        private String type;

        public Entry(String hostname, String type) {
            this.hostname = hostname;
            this.type = type;
        }

        public String get_ip() {
            return this.hostname;
        }

        public String get_type() {
            return this.type;
        }
    }
}