package hr.fer.azzi.menze.classes;

import android.content.Context;

import com.turbomanage.storm.DatabaseHelper;
import com.turbomanage.storm.api.Database;
import com.turbomanage.storm.api.DatabaseFactory;

/**
 * Created by Azzaro on 24.12.2014..
 */
@Database(name = DataBase.DB_NAME, version = DataBase.DB_VERSION)
public class DataBase extends DatabaseHelper {

    public final static String DB_NAME = "samples";
    public final static int DB_VERSION = 1;

    /**
     * The constructor that should be overridden. Simply invoke
     * the super constructor.
     *
     * @param ctx
     * @param dbFactory
     */
    public DataBase(Context ctx, DatabaseFactory dbFactory) {
        super(ctx, dbFactory);
    }

    @Override
    public UpgradeStrategy getUpgradeStrategy() {
        return UpgradeStrategy.DROP_CREATE;
    }
}
