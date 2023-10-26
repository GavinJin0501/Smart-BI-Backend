package com.gavinjin.smartbibackend.util.constant;

/**
 * Constants related with users
 */
public interface ChartConstant {
    String DB_COL_CHART_ID = "id";
    String DB_COL_CHART_GOAL = "goal";
    String DB_COL_CHART_DATA = "chart_data";
    String DB_COL_CHART_TYPE = "chart_type";
    String DB_COL_GEN_CHART = "gen_chart";
    String DB_COL_GEN_RESULT = "gen_result";
    String DB_COL_USER_ID = "user_id";
    String DB_COL_NAME = "name";
    String DB_COL_CREATE_TIME = "create_time";
    String DB_COL_UPDATE_TIME = "update_time";
    String DB_COL_IS_DELETED = "is_deleted";

    String STATUS_WAIT = "wait";
    String STATUS_RUNNING = "running";
    String STATUS_SUCCEEDED = "succeeded";
    String STATUS_FAILED = "failed";
}
