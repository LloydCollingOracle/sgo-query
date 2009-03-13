package org.sgodden.query.models;

import org.sgodden.query.Restriction;
import org.sgodden.query.SortData;

public interface RefreshableQueryTableModel extends QueryTableModel {
    public void refresh(Restriction criterion);
    public void refresh(Restriction criterion,
            SortData[] sortData);
}
