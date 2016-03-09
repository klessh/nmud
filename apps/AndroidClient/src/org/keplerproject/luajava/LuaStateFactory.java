package org.keplerproject.luajava;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for instantiating new LuaStates.
 * When a new LuaState is instantiated it is put into a List
 * and an index is returned. This index is registred in Lua
 * and it is used to find the right LuaState when lua calls
 * a Java Function.
 * 
 * @author Thiago Ponte
 */
public final class LuaStateFactory
{
	/**
	 * Array with all luaState's instances
	 */
	private static final List states = new ArrayList();
	
	/**
	 * Non-public constructor. 
	 */
	private LuaStateFactory()
	{}
	
	/**
	 * Method that creates a new instance of LuaState
	 * @return LuaState
	 */
	public synchronized static LuaState newLuaState()
	{
		int i = getNextStateIndex();
		LuaState L = new LuaState(i);
		
		states.add(i, L);
		
		return L;
	}
	
	/**
	 * Returns a existing instance of LuaState
	 * @param index
	 * @return LuaState
	 */
	public synchronized static LuaState getExistingState(int index)
	{
		return (LuaState) states.get(index);
	}
	
	/**
	 * Receives a existing LuaState and checks if it exists in the states list.
	 * If it doesn't exist adds it to the list.
	 * @param L
	 * @return int
	 */
	public synchronized static int insertLuaState(LuaState L)
	{
		int i;
		for (i = 0 ; i < states.size() ; i++)
		{
			LuaState state = (LuaState) states.get(i);
			
			if (state != null)
			{
				if (state.getCPtrPeer() == L.getCPtrPeer())
					return i;
			}
		}

		i = getNextStateIndex();
		
		states.set(i, L);
		
		return i;
	}
	
	/**
	 * removes the luaState from the states list
	 * @param idx
	 */
	public synchronized static void removeLuaState(int idx)
	{
		states.add(idx, null);
	}
	
	/**
	 * Get next available index
	 * @return int
	 */
	private synchronized static int getNextStateIndex()
	{
		int i;
		for ( i=0 ; i < states.size() && states.get(i) != null ; i++ );
		
		return i;
	}
}
