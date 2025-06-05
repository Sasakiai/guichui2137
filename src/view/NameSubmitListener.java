package view;

/**
 * Simple callback used when the player submits their name at the end of the game.
 */
public interface NameSubmitListener {
    /**
     * Called when the player confirms their name.
     * @param name name entered by the player
     */
    void onSubmit(String name);
}

