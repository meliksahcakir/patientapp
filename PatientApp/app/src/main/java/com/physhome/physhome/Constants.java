package com.physhome.physhome;

/**
 * Created by meliksah on 6/5/2017.
 */

public class Constants {
    public static final String YES_ACTION = "com.physhome.physhome.YES";
    public static final String NO_ACTION = "com.physhome.physhome.NO";
    public static final String NOTIFICATION_ACTION = "com.physhome.physhome.NOTIFICATION";
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "com.physhome.physhome";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static final String URL_TASKS = "http://146.185.176.206/tasks";
    public static final String URL_USERS = "http://146.185.176.206/users";
    public static final String URL_REGISTER = "http://146.185.176.206/register";
    public static final String URL_SIGNIN = "http://146.185.176.206/signin";
    public static final String URL_IMAGES = "http://146.185.176.206/images";
    public static final String URL_EXERCISES = "http://146.185.176.206/exercises";
    public static final String URL_DEVICES = "http://146.185.176.206/devices";

    public static final String PREF_EXERCISE_LIST = "exercise_list";
    public static final String PREF_EXERCISE_INFO_LIST = "exercise_info_list";
    public static final String PREF_TREATMENT_LIST = "treatment_list";
    public static final String PREF_SUMMARY = "unrecorded_summary";
    public static final String PREF_SUMMARY_ID = "unrecorded_summary_id";
    public static final String PREF_UNSAVED_EXERCISE = "unsaved_exercise";
    public static final String PREF_UNSAVED_EXERCISE_ID = "unsaved_exercise_id";

    public static final int BT_MAIN_ACTIVITY = 0;
    public static final int BT_SETTINGS_ACTIVITY = 1;
    public static final int BT_SELECT_EXERCISE_ACTIVITY = 2;

    public static final String BT_LIMB_NAME = "bt_limb_name";
    public static final String BT_CHEST_NAME = "bt_chest_name";
    public static final String BT_LIMB_MAC = "bt_limb_mac";
    public static final String BT_CHEST_MAC = "bt_chest_mac";
    public static final String BT_RETURN_ACTIVITY = "return_activity";

    public static final String LIMB = "limb";
    public static final String CHEST = "chest";
    public static final String USER_DEVICES = "user_devices";
}
