package dev.djigit.chessonevsone.game.chessboard.cell;

import dev.djigit.chessonevsone.game.chessboard.cell.CellModel.State;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

import java.util.Objects;

public class CellViewModel {

    private final CellModel model;
    private final ObjectProperty<State> stateProperty;

    public CellViewModel(CellModel model) {
        this.model = model;
        stateProperty = new SimpleObjectProperty<>(model.getState());
    }

    public void addStatePropertyListener(ChangeListener<State> changeListener) {
        stateProperty.addListener(changeListener);
    }

    public void setState(State state) {
        model.setState(state);
        stateProperty.set(state);
    }

    public CellModel getModel() {
        return model;
    }
}
