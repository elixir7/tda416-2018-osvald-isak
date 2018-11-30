/**
 * Minimal implementation of a Splay Tree, a special version of Binary Search Tree
 * When searching in the tree the searched element get tranversed to the root by tree rotations
 * This is done in order to make cost amortized and have normal operations (add, get, search) O(log(n))
 * @param <E> The type which should be stored in the Tree, has to be comparable.
 * @author Isak Åslund
 * @version 2018
 */

public class SplayWithGet<E extends Comparable<? super E>>
                        extends BinarySearchTree<E>
                        implements CollectionWithGet<E> {

    //Used internally for returning the found element when searching, if it isn't found it will return null.
    private E found;

    /**
     * Constructor of the Splay Tree.
     * Only calls the parent (Binary Search Tree) class constructor.
     */
    public SplayWithGet(){ super(); }

    /**
     * Used for testing the zig/zag methods.
     */
    public void test(){
        //Doing the zagzag and then the inverse (zigzig) should result in the original tree.
        zagzag(root);
        zigzig(root);
    }

    /* Rotera 1 steg i hogervarv, dvs
               x'                 y'
              / \                / \
             y'  C   -->        A   x'
            / \                    / \
           A   B                  B   C
     */
    private void zag( Entry x ) {
        Entry   y = x.left;
        E    temp = x.element;
        x.element = y.element;
        y.element = temp;
        x.left    = y.left;
        if ( x.left != null )
            x.left.parent   = x;
        y.left    = y.right;
        y.right   = x.right;
        if ( y.right != null )
            y.right.parent  = y;
        x.right   = y;
    } //   rotateRight

    /* Rotera 1 steg i vanstervarv, dvs
              x'                 y'
             / \                / \
            A   y'  -->        x'  C
               / \            / \
              B   C          A   B
    */
    private void zig( Entry x ) {
        Entry  y  = x.right;
        E temp    = x.element;
        x.element = y.element;
        y.element = temp;
        x.right   = y.right;
        if ( x.right != null )
            x.right.parent  = x;
        y.right   = y.left;
        y.left    = x.left;
        if ( y.left != null )
            y.left.parent   = y;
        x.left    = y;
    } //   rotateLeft

    /* Rotera 2 steg i hogervarv, dvs
              x'                  z'
             / \                /   \
            y'  D   -->        y'    x'
           / \                / \   / \
          A   z'             A   B C   D
             / \
            B   C
    */
    private void zagzig( Entry x ) {
        Entry   y = x.left,
                z = x.left.right;
        E       e = x.element;
        x.element = z.element;
        z.element = e;
        y.right   = z.left;
        if ( y.right != null )
            y.right.parent = y;
        z.left    = z.right;
        z.right   = x.right;
        if ( z.right != null )
            z.right.parent = z;
        x.right   = z;
        z.parent  = x;
    }  //  doubleRotateRight

    /* Rotera 2 steg i vanstervarv, dvs
               x'                  z'
              / \                /   \
             A   y'   -->       x'    y'
                / \            / \   / \
               z   D          A   B C   D
              / \
             B   C
     *///Här Checkas inte B om den är null, B:s föräkdrar sätts aldrig? wtf
    private void zigzag( Entry x ) {
        Entry  y  = x.right,
                z  = x.right.left;
        E      e  = x.element;
        x.element = z.element;
        z.element = e;
        y.left    = z.right;
        if ( y.left != null )
            y.left.parent = y;
        z.right   = z.left;
        z.left    = x.left;
        if ( z.left != null )
            z.left.parent = z;
        x.left    = z;
        z.parent  = x;
    } //  doubleRotateLeft

    /* Rotera ---------
               x                   z
              / \                 / \
             y   D      -->      A   y
            / \                     / \
           z  C                    B   x
          / \                         / \
         A   B                       C   D
     */
    private void zagzag( Entry x){ //
        Entry y = x.left;
        Entry z = y.left;
        E e = x.element;

        Entry A = z.left;
        Entry B = z.right;
        Entry C = y.right;
        Entry D = x.right;

        x.element = z.element;
        z.element = e;

        z = x;
        x = y.left;

        //Puzzle it together
        z.left = A;     if(A != null)   A.parent = z;
        z.right = y;                    y.parent = z;

        y.left = B;     if(B != null)   B.parent = y;
        y.right = x;    x.parent = y;

        x.left = C;     if(C != null)   C.parent = x;
        x.right = D;    if(D != null)   D.parent = x;
    }

    /* Rotera ---------
               x                   z
              / \                 / \
             A   y      -->      y   D
                / \             / \
               B  z            x   C
                 / \          / \
                C  D         A  B
     */
    private void zigzig( Entry x){ //
        Entry y = x.right;
        Entry z = y.right;
        E e = x.element;

        Entry A = x.left;
        Entry B = y.left;
        Entry C = z.left;
        Entry D = z.right;

        x.element = z.element;
        z.element = e;

        z = x;
        x = y.right;

        //Puzzle it together
        z.left = y;                     y.parent = z;
        z.right = D;    if(D != null)   D.parent = z;

        y.left = x;     x.parent = y;
        y.right = C;    if(C != null)   C.parent = y;

        x.left = A;     if(A != null)   A.parent = x;
        x.right = B;    if(B != null)   B.parent = x;
    }

    /**
     * Recursive method for brining the KEY element to the top of the tree.
     * @param e The current node
     * @param key The element we are seraching for
     * @param depth The curennt nodes depth.
     * @return Positive if element is on right side, negative if it is on the left
     */
    private int splayFind(Entry e, E key, int depth) {
        int token;
        int comparator = key.compareTo(e.element);
        if(comparator > 0){
           //key is larger then the node, GO right
             token = e.right != null ? splayFind(e.right, key, depth+1)*3 + 1 : 0;
        }else if(comparator < 0){
            //key is smaller then the node, GO LEFT
            token = e.left != null ? splayFind(e.left, key, depth+1)*3 - 1 : 0;
        }else{
            //Key found at node e.
            found = e.element;
            return 0;
        }
        //System.out.println("Depth: " + depth + " Data: " + e.element + " Token : " + token);
        if(depth % 2 == 0 && token != 0){
            splay(e, token);
            token = 0;
        }
        return token;
    }

    /**
     * Method for splaying, top-down implementation.
     * @param node The top node for splaying.
     * @param val  A token for knowing which type rotations should be done.
     *            -4  = zagzag
     *             4  = zigzig
     *             2  = zagzig
     *            -2  = zigzag
     *             1  = zig
     *            -1  = zag
     */
    private void splay(Entry node, int val){
        if(val == -4)       zagzag(node);
        else if(val == 4)   zigzig(node);
        else if(val == 2)   zagzig(node);
        else if(val == -2)  zigzag(node);
        else if(val == 1)   zig(node);
        else if(val == -1)  zag(node);
    }

    /**
     * Searches and returns the supplied element in the tree.
     * @param e The dummy element to compare to.
     * @return The element searched
     * @thros NullPointerException if the supplied element is null
     */
    @Override
    public E get(E e) {
        if(e == null){
            throw new NullPointerException();
        }
        if(root == null){
           return null;
        }
        found = null;
        splayFind(root,e,0);
        return found;

    }
}