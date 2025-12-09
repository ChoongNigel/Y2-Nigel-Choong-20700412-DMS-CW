package com.comp2042.GameLogic;

import com.comp2042.GameInfo.ViewData;

public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    /**
     *Check on any row is cleared and make decision on whether refresh new brick
     * @param clearRow
     * @param viewData
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    public ClearRow getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }
}
