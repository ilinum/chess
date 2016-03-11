package me.ilinskiy.chess.api.game;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/28/15.
 * <p>
 * Classes implementing this interface must create a deep copy of the class in copy()
 */
public interface Copyable {

    /**
     * @return a deep copy of this class
     */
    Copyable copy();
}
