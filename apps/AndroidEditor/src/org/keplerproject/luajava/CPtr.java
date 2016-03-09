package org.keplerproject.luajava;

/**
 * An abstraction for a C pointer data type.  A CPtr instance represents, on
 * the Java side, a C pointer.  The C pointer could be any <em>type</em> of C
 * pointer. 
 */
public class CPtr{
    
    /**
     * Compares this <code>CPtr</code> to the specified object.
     *
     * @param other a <code>CPtr</code>
     * @return      true if the class of this <code>CPtr</code> object and the
     *		    class of <code>other</code> are exactly equal, and the C
     *		    pointers being pointed to by these objects are also
     *		    equal. Returns false otherwise.
     */
	public boolean equals(Object other){
		if (other == null)
			return false;
		if (other == this)
	    return true;
		if (CPtr.class != other.getClass())
	    return false;
		return peer == ((CPtr)other).peer;
   }


    /* Pointer value of the real C pointer. Use long to be 64-bit safe. */
    private long peer;
    
    /**
     * Gets the value of the C pointer abstraction
     * @return long
     */
    protected long getPeer(){
    	return peer;
    }

    /* No-args constructor. */
    CPtr() {}
 
}
