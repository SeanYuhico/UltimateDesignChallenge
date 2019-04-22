package Model;

import java.util.List;

public class ListRepository implements Container {

    public List<Song> songList;

    public ListRepository(List<Song> songList) {
        this.songList = songList;
    }

    @Override
    public Iterator getIterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator {

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
