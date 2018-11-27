package fall18_207project.GameCenter;

/***
 * This is the game history of Account. It stores all the games that the user saved, but have not finished.
 * For finished games, visit ScoreBoard.
 */
public class NonDuplicateGameManager extends GameManager {

    public NonDuplicateGameManager() {
        super();
    }


    /***
     * Do not allow two saved Games to have same saveId.
     */
    @Override
    void addGame(Game newGame) {
        if (hasGame(newGame.getSaveId())) {
            deleteGame(newGame.getSaveId());
        }
        this.gameList.add(newGame);
    }

}