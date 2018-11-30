/**
 * Minimal implementation of a Sorted, Single linked Collection with an modified add and get method
 * @param <E> The type which should be stored in the list.
 * @author Isak Åslund
 * @version 2018
 */
public class SLCWithGet<E extends Comparable<? super E>>
                        extends LinkedCollection<E>
                        implements CollectionWithGet<E> {

    /**
     * Constructor of the sorted collection.
     * Only calls the parent class constructor which sets the variable head to null.
     */
    public SLCWithGet(){
        super();
    }

    /**
     * Adds an element to the sorted list regarding it's value.
     * @param e Elementt to add to the list
     * @return If the operation was successful or not
     * @throws NullPointerException if the element is null
     */
    @Override
    public boolean add(E e) {
        if(e == null){
            throw new NullPointerException();
        }

        //When the list is empty we need a special case.
        if(head == null){
            head = new Entry(e, null);
            return true;
        }

        Entry p = head;
        if(e.compareTo(p.element) <= 0){
            head = new Entry(e, head);
            return true;
        }

        while(p.next != null){
            int comparator = e.compareTo(p.next.element);
            if(comparator <= 0){
                break;
            }
            p = p.next;
        }
        p.next =  new Entry(e, p.next);
        return true;
    }

    /**
     * Returns the first occurance of the elemt in the list.
     * @param e The element to find
     * @return The first occurance of e. If not found it returns null
     * @throws NullPointerException if the element supplied is null
     */
    @Override
    public E get(E e) {
        if (e == null){
            throw new NullPointerException();
        }

        Entry p = head;
        while(p != null){
            int comparator = e.compareTo(p.element);
            if(comparator < 0){
                return null;
            }else if(comparator == 0){
                return p.element;
            }
            p = p.next;
        }
        return null;
    }
}
