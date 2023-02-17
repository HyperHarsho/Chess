public class Rook extends Piece {
    private boolean haveMoved = false;

    public Rook(boolean white) {
        super(white);
    }

    public boolean haveMoved() {
        return this.haveMoved;
    }

    public void setMoved(boolean moved) {
        this.haveMoved = moved;
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) throws Exception {
        // TODO Auto-generated method stub
        if (end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }
        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        if (x == 0 && y == 0) {
            return false;
        }
        if (x == 0 || y == 0) {
            if (x == 0) {
                int s = Math.min(start.getY(), end.getY());
                int e = Math.max(start.getY(), end.getY());
                for (int i = s + 1; i < e; i++) {
                    if (board.getBox(start.getX(), i).getPiece() != null) {
                        return false;
                    }
                }
            } else {
                int s = Math.min(start.getX(), end.getX());
                int e = Math.max(start.getX(), end.getX());
                for (int i = s + 1; i < e; i++) {
                    if (board.getBox(i, start.getY()).getPiece() != null) {
                        return false;
                    }
                }
            }
            Piece temp = board.boxes[start.getX()][start.getY()].getPiece();
            board.boxes[start.getX()][start.getY()].setPiece(null);
            King king = null;
            int i,j;
            for (i = 0; i < 7; i++) {
                for (j = 0; j < 7; j++) {
                    if (board.getBox(i, j).getPiece() instanceof King) {
                        if (board.getBox(i, j).getPiece().isWhite() == this.isWhite()) {
                            king = (King) board.getBox(i, j).getPiece();
                            break;
                        }
                    }
                }
            }
            if(king.underAttack(board, new Spot(x,y,null))) {
                board.boxes[start.getX()][start.getY()].setPiece(temp);
                return false;
            }
        }
        return true;
    }
}
