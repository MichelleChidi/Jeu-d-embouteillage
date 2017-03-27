package cmd;

import model.Board;
import model.Coord;
import model.Vehicle;
import util.Contract;

public class GoForward extends AbstractCommand {

	// ATTRIBUTS

	private Vehicle vehicle;
	private Coord newCoord;
	private Coord oldCoord;
	private Board board;

	// CONSTRUCTEUR

	/**
	 * Création d'une commande GoForward.
	 */
	public GoForward(Board board, Vehicle vehicle, Coord newCoord, Coord oldCoord) {
		super(board);
		Contract.checkCondition(vehicle != null && newCoord != null && oldCoord != null);
		this.board = board;
		this.vehicle = vehicle;
		this.newCoord = newCoord;
		this.oldCoord = oldCoord;
	}

	// REQUETES

	public Vehicle getVehicle() {
		return vehicle;
	}

	public Coord getNewCoord() {
		return newCoord;
	}

	public Coord getOldCoord() {
		return oldCoord;
	}

	public Board getBoard() {
		return board;
	}

	public boolean canDo() {
		return super.canDo() && board.canMoveTo(vehicle, newCoord)
				&& (board.getCoord(vehicle).getCol() - newCoord.getCol() <= 0
						|| board.getCoord(vehicle).getRow() - newCoord.getRow() <= 0);
	}

	public boolean canUndo() {
		return super.canUndo() && board.canMoveTo(vehicle, oldCoord)
				&& (board.getCoord(vehicle).getCol() - oldCoord.getCol() >= 0
						|| board.getCoord(vehicle).getRow() - oldCoord.getRow() >= 0);
	}

	// COMMANDES

	public void doIt() {
		Contract.checkCondition(canDo());
		board.goForwardAux(vehicle, newCoord);
	}

	public void undoIt() {
		Contract.checkCondition(canUndo());
		board.goBackwardsAux(vehicle, oldCoord);
	}

}
