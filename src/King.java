public class King extends Piece {
    private boolean castlingDone = false;
    private boolean haveMoved = false;

    public King(boolean white) {
        super(white);
    }

    public boolean isCastlingDone() {
        return this.castlingDone;
    }

    public boolean haveMoved() {
        return this.haveMoved;
    }

    public void setMoved(boolean moved) {
        this.haveMoved = moved;
    }

    public void setCastlingDone(boolean castlingDone) {
        this.castlingDone = castlingDone;
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) throws Exception {
        // we can't move the piece to a Spot that
        // has a piece of the same color
        if (end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        if (x + y == 1) {
            // check if this move will not result in the king
            // being attacked if so return true
            if (underAttack(board, end)) {
                return false;
            }
            this.setMoved(true);
            return true;
        }

        if (this.isValidCastling(board, start, end)) {
            this.setMoved(true);
            this.setCastlingDone(true);
            return true;
        }
        return false;
    }

    private boolean isValidCastling(Board board,
            Spot start, Spot end) throws Exception {

        if (this.isCastlingDone() || this.haveMoved()) {
            return false;
        }
        if (start.getY() != 4 || end.getY() != 2 || end.getY() != 6 || start.getX() == 0 || start.getX() == 7
                || end.getX() == 0 || end.getX() == 7) {
            return false;
        }
        if (start.getX() == 0 && end.getY() == 2) {
            if (board.getBox(0, 0).getPiece() instanceof Rook) {
                Rook rook = (Rook) board.getBox(0, 0).getPiece();
                if (rook.haveMoved()) {
                    return false;
                }
                for (int i = 1; i < 4; i++) {
                    if (board.getBox(0, i).getPiece() != null) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        if (start.getX() == 0 && end.getY() == 6) {
            if (board.getBox(0, 7).getPiece() instanceof Rook) {
                Rook rook = (Rook) board.getBox(0, 7).getPiece();
                if (rook.haveMoved()) {
                    return false;
                }
                for (int i = 5; i < 7; i++) {
                    if (board.getBox(0, i).getPiece() != null) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        if (start.getX() == 7 && end.getY() == 2) {
            if (board.getBox(7, 0).getPiece() instanceof Rook) {
                Rook rook = (Rook) board.getBox(7, 0).getPiece();
                if (rook.haveMoved()) {
                    return false;
                }
                for (int i = 1; i < 4; i++) {
                    if (board.getBox(7, i).getPiece() != null) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        if (start.getX() == 7 && end.getY() == 6) {
            if (board.getBox(7, 7).getPiece() instanceof Rook) {
                Rook rook = (Rook) board.getBox(7, 7).getPiece();
                if (rook.haveMoved()) {
                    return false;
                }
                for (int i = 5; i < 7; i++) {
                    if (board.getBox(7, i).getPiece() != null) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean isCastlingMove(Spot start, Spot end) {
        // check if the starting and
        // ending position are correct
        if (start.getX() == 0 && start.getY() == 4 && end.getX() == 0 && end.getY() == 6) {
            return true;
        }
        if (start.getX() == 7 && start.getY() == 4 && end.getX() == 7 && end.getY() == 6) {
            return true;
        }
        if (start.getX() == 0 && start.getY() == 4 && end.getX() == 0 && end.getY() == 2) {
            return true;
        }
        if (start.getX() == 7 && start.getY() == 4 && end.getX() == 7 && end.getY() == 2) {
            return true;
        }
        return false;
    }

    public boolean underAttack(Board board, Spot current) throws Exception {
        int x = current.getX();
        int y = current.getY();
        int i, j;
        // Check for king
        for (i = x - 1; i <= x + 1; i++) {
            for (j = y - 1; j <= j + 1; j++) {
                if (i < 0 || i > 7 || j < 0 || j > 7) {
                    continue;
                }
                if (board.getBox(i, j).getPiece() instanceof King) {
                    if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                        return true;
                    }
                }
            }
        }
        // Check for pawns
        if (this.isWhite()) {
            i = x + 1;
            j = y - 1;
            if (board.getBox(i, j).getPiece() instanceof Pawn) {
                if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
            i = x + 1;
            j = y + 1;
            if (board.getBox(i, j).getPiece() instanceof Pawn) {
                if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
        } else {
            i = x - 1;
            j = y - 1;
            if (board.getBox(i, j).getPiece() instanceof Pawn) {
                if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
            i = x - 1;
            j = y + 1;
            if (board.getBox(i, j).getPiece() instanceof Pawn) {
                if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
        }
        // Check lines
        for (i = x - 1; i >= 0; i--) {
            if (board.getBox(i, y).getPiece() instanceof Pawn || board.getBox(i, y).getPiece() instanceof Bishop
                    || board.getBox(i, y).getPiece() instanceof Knight) {
                return false;
            }
            if (board.getBox(i, y).getPiece() instanceof Queen || board.getBox(i, y).getPiece() instanceof Rook) {
                if (board.getBox(i, y).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
        }
        for (i = x + 1; i < 8; i++) {
            if (board.getBox(i, y).getPiece() instanceof Pawn || board.getBox(i, y).getPiece() instanceof Bishop
                    || board.getBox(i, y).getPiece() instanceof Knight) {
                return false;
            }
            if (board.getBox(i, y).getPiece() instanceof Queen || board.getBox(i, y).getPiece() instanceof Rook) {
                if (board.getBox(i, y).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
        }
        for (i = y - 1; i >= 0; i--) {
            if (board.getBox(x, i).getPiece() instanceof Pawn || board.getBox(x, i).getPiece() instanceof Bishop
                    || board.getBox(x, i).getPiece() instanceof Knight) {
                return false;
            }
            if (board.getBox(x, i).getPiece() instanceof Queen || board.getBox(x, i).getPiece() instanceof Rook) {
                if (board.getBox(x, i).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
        }
        for (i = y + 1; i < 8; i++) {
            if (board.getBox(x, i).getPiece() instanceof Pawn || board.getBox(x, i).getPiece() instanceof Bishop
                    || board.getBox(x, i).getPiece() instanceof Knight) {
                return false;
            }
            if (board.getBox(x, i).getPiece() instanceof Queen || board.getBox(x, i).getPiece() instanceof Rook) {
                if (board.getBox(x, i).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
        }
        // Check diagonals
        for (i = x + 1; i < 8; i++) {
            for (j = y + 1; j < 8; j++) {
                if (board.getBox(i, j).getPiece() instanceof Pawn || board.getBox(i, j).getPiece() instanceof Rook
                        || board.getBox(i, j).getPiece() instanceof Knight) {
                    return false;
                }
                if (board.getBox(i, j).getPiece() instanceof Queen || board.getBox(i, j).getPiece() instanceof Bishop) {
                    if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                        return true;
                    }
                }
            }
        }
        for (i = x + 1; i < 8; i++) {
            for (j = y - 1; j >= 0; j--) {
                if (board.getBox(i, j).getPiece() instanceof Pawn || board.getBox(i, j).getPiece() instanceof Rook
                        || board.getBox(i, j).getPiece() instanceof Knight) {
                    return false;
                }
                if (board.getBox(i, j).getPiece() instanceof Queen || board.getBox(i, j).getPiece() instanceof Bishop) {
                    if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                        return true;
                    }
                }
            }
        }
        for (i = x - 1; i >= 0; i--) {
            for (j = y - 1; j >= 0; j--) {
                if (board.getBox(i, j).getPiece() instanceof Pawn || board.getBox(i, j).getPiece() instanceof Rook
                        || board.getBox(i, j).getPiece() instanceof Knight) {
                    return false;
                }
                if (board.getBox(i, j).getPiece() instanceof Queen || board.getBox(i, j).getPiece() instanceof Bishop) {
                    if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                        return true;
                    }
                }
            }
        }
        for (i = x - 1; i >= 0; i--) {
            for (j = y + 1; j < 8; j++) {
                if (board.getBox(i, j).getPiece() instanceof Pawn || board.getBox(i, j).getPiece() instanceof Rook
                        || board.getBox(i, j).getPiece() instanceof Knight) {
                    return false;
                }
                if (board.getBox(i, j).getPiece() instanceof Queen || board.getBox(i, j).getPiece() instanceof Bishop) {
                    if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                        return true;
                    }
                }
            }
        }
        // Check for knight
        i = x + 1;
        j = y - 2;
        if (!(x > 7 || y < 0)) {
            if (board.getBox(i, j).getPiece() instanceof Knight) {
                if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
        }
        i = x - 1;
        j = y - 2;
        if (!(x < 0 || y < 0)) {
            if (board.getBox(i, j).getPiece() instanceof Knight) {
                if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
        }
        i = x + 1;
        j = y + 2;
        if (!(x > 7 || y > 7)) {
            if (board.getBox(i, j).getPiece() instanceof Knight) {
                if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
        }
        i = x - 1;
        j = y + 2;
        if (!(x < 0 || y > 7)) {
            if (board.getBox(i, j).getPiece() instanceof Knight) {
                if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
        }
        i = x + 2;
        j = y + 1;
        if (!(x > 7 || y > 7)) {
            if (board.getBox(i, j).getPiece() instanceof Knight) {
                if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
        }
        i = x + 2;
        j = y - 1;
        if (!(x > 7 || y < 0)) {
            if (board.getBox(i, j).getPiece() instanceof Knight) {
                if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
        }
        i = x - 2;
        j = y + 1;
        if (!(x < 0 || y > 7)) {
            if (board.getBox(i, j).getPiece() instanceof Knight) {
                if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
        }
        i = x - 2;
        j = y - 1;
        if (!(x < 0 || y < 0)) {
            if (board.getBox(i, j).getPiece() instanceof Knight) {
                if (board.getBox(i, j).getPiece().isWhite() != this.isWhite()) {
                    return true;
                }
            }
        }
        return false;
    }
}
