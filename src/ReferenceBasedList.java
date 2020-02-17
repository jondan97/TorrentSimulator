import java.text.Collator;
import java.util.Locale;

public class ReferenceBasedList implements ListInterface{
    private ListNode head;
    private ListNode tail;
    int numItems;

    public ReferenceBasedList() {
        head = null;
        tail = null;
        numItems = 0;
    }

    public boolean isEmpty() {
        return (head == null);
    }

    public int size() {
        return numItems;
    }

    public int sizeAll() { //returns the sum of the size attribute of all the movies that are in the list
        int sizeAll = 0;
        for (ListNode curr = head; curr != null; curr = curr.getNext()) {
            sizeAll += ((Movie) curr.getItem()).getSize();
        }
        return sizeAll;
    }

    public void showAll() {
        int count = 1;
        for (ListNode curr = head; curr != null; curr = curr.getNext()) {
            System.out.println(count + " - " + curr.getItem());
            count++;
        }
    }

    public ListNode find(int index) {

        ListNode curr = head;
        for (int i = 1; i < index; i++)
            curr = curr.getNext();
        return curr;
    }

    public void add(int index, Object newDataItem) throws ListIndexOutOfBoundsException {
        if (index >= 1 && index <= numItems + 1) {
            if (index == 1) {
                ListNode newNode = new ListNode(newDataItem, head);
                head = newNode;
                if (tail == null)
                    tail = head;
            } else if (index == numItems + 1) {
                ListNode newNode = new ListNode(newDataItem);
                tail.setNext(newNode);
                tail = newNode;
            } else {
                ListNode prev = find(index - 1);
                ListNode newNode = new ListNode(newDataItem, prev.getNext());
                prev.setNext(newNode);
            }
            numItems++;
        } else {
            throw new ListIndexOutOfBoundsException("Index outOfBounds exception on add");
        }
    }

    public void addSortedBySize(Movie m) throws ListIndexOutOfBoundsException { //used to compare movies and sort them from smallest to biggest
        if (head == null) {
            this.add(1, m);
        } else {
            int index = 1; // beginning
            for (ListNode curr = head; curr != null; curr = curr.getNext()) {
                if (((Movie) curr.getItem()).compareTo(m) <= 0) {
                    this.add(index, m);
                    return;
                }
                index++;
            }
            this.add(numItems + 1, m);//case where movie is the biggest one in the list
        }
    }

    public void addSortedAlphabetically(Movie m) throws ListIndexOutOfBoundsException {
        Collator usCollator = Collator.getInstance(Locale.US);
        usCollator.setStrength(Collator.PRIMARY);
        if (head == null) {
            this.add(1, m);
        } else {
            int index = 1; // beginning
            for (ListNode curr = head; curr != null; curr = curr.getNext()) {
                if (usCollator.compare(m.getName(), ((Movie) curr.getItem()).getName()) <= 0) {
                    this.add(index, m);
                    return;
                }
                index++;
            }
            this.add(numItems + 1, m);//case where movie is the last (lexicographically) one in the list
        }
    }


    public void remove(int index) throws ListIndexOutOfBoundsException {
        if (index >= 1 && index <= numItems) {
            if (index == 1) {
                head = head.getNext();
                if (head == null)
                    tail = null;
            } else {
                ListNode prev = find(index - 1);
                ListNode curr = prev.getNext();
                prev.setNext(curr.getNext());
                if (index == numItems)
                    tail = prev;
            }
            numItems--;
        } else {
            throw new ListIndexOutOfBoundsException(
                    "List index out of bounds exception on remove");
        }
    }

    public void removeAll() {
      head = tail = null;
    }

    public Object get(int index) throws ListIndexOutOfBoundsException {
        if (index >= 1 && index <= numItems) {
            ListNode curr = find(index);
            return curr.getItem();
        } else {
            throw new ListIndexOutOfBoundsException("Index outOfBounds exception on get");
        }
    }

    public boolean exists(Movie m) { //if a movie exists in the list
        boolean exists = false;
        for (ListNode curr = head; curr != null; curr = curr.getNext()) {
            if ((curr.getItem()).equals(m)) {
                exists = true;
                break;
            }
        }
        return exists;
    }
}

