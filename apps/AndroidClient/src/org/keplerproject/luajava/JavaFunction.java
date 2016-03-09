package org.keplerproject.luajava;

/**
 * JavaFunction is a class that can be used to implement a Lua function in Java.
 * JavaFunction is an abstract class, so in order to use it you must extend this 
 * class and implement the <code>execute</code> method. This <code>execute</code> 
 * method is the method that will be called when you call the function from Lua.
 * To register the JavaFunction in Lua use the method <code>register(String name)</code>.
 */
public abstract class JavaFunction
{
	
	/**
	 * This is the state in which this function will exist.
	 */
	protected LuaState L;
	
	/**
	 * This method is called from Lua. Any parameters can be taken with
	 * <code>getParam</code>. A reference to the JavaFunctionWrapper itself is
	 * always the first parameter received. Values passed back as results
	 * of the function must be pushed onto the stack.
	 * @return The number of values pushed onto the stack.
	 */
	public abstract int execute() throws LuaException;
	
	/**
	 * Constructor that receives a LuaState.
	 * @param L LuaState object associated with this JavaFunction object
	 */
	public JavaFunction(LuaState L){
		this.L = L;
	}

	/**
	 * Returns a parameter received from Lua. Parameters are numbered from 1.
	 * A reference to the JavaFunction itself is always the first parameter
	 * received (the same as <code>this</code>).
	 * @param idx Index of the parameter.
	 * @return Reference to parameter.
	 * @see LuaObject
	 */
	public LuaObject getParam(int idx){
		return L.getLuaObject(idx);
	}

	/**
	 * Register a JavaFunction with a given name. This method registers in a
	 * global variable the JavaFunction specified.
	 * @param name name of the function.
	 */
	public void register(String name) throws LuaException{
	  synchronized (L){
			L.pushJavaFunction(this);
			L.setGlobal(name);
	  }
	}
}
