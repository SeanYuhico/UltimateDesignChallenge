package Model;

import java.util.ArrayList;

public class SongRepository implements Container {

    public ArrayList<Song> songList;

    public SongRepository(ArrayList<Song> songList) {
        this.songList = songList;
    }

    @Override
    public Iterator getIterator() {
        return new SongIterator();
    }

    private class SongIterator implements Iterator {

        int index;

        @Override
        public boolean hasNext() {
            if (index < songList.size())
                return true;
            return false;
        }

        @Override
        public Object next() {
            if (this.hasNext())
                return songList.get(index++);
            return null;
        }
    }
}
