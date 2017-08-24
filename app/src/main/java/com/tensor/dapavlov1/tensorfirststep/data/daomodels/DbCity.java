package com.tensor.dapavlov1.tensorfirststep.data.daomodels;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.ToMany;

/**
 * Created by da.pavlov1 on 07.08.2017.
 */

@Entity(active = true, nameInDb = "City")
public class DbCity implements Parcelable {
    @Id
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String lastTimeUpdate;

    @ToMany(referencedJoinProperty = "cityId")
    private List<DbWeather> weathers;


    protected DbCity(Parcel in) {
        name = in.readString();
        lastTimeUpdate = in.readString();
        weathers = in.createTypedArrayList(DbWeather.CREATOR);
    }

    public static final Creator<DbCity> CREATOR = new Creator<DbCity>() {
        @Override
        public DbCity createFromParcel(Parcel in) {
            return new DbCity(in);
        }

        @Override
        public DbCity[] newArray(int size) {
            return new DbCity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(lastTimeUpdate);
        parcel.writeTypedList(weathers);
    }

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1507836013)
    private transient DbCityDao myDao;

    @Generated(hash = 457832615)
    public DbCity(Long id, @NotNull String name, @NotNull String lastTimeUpdate) {
        this.id = id;
        this.name = name;
        this.lastTimeUpdate = lastTimeUpdate;
    }

    @Generated(hash = 1720623914)
    public DbCity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastTimeUpdate() {
        return this.lastTimeUpdate;
    }

    public void setLastTimeUpdate(String lastTimeUpdate) {
        this.lastTimeUpdate = lastTimeUpdate;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1065597217)
    public List<DbWeather> getWeathers() {
        if (weathers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DbWeatherDao targetDao = daoSession.getDbWeatherDao();
            List<DbWeather> weathersNew = targetDao._queryDbCity_Weathers(id);
            synchronized (this) {
                if (weathers == null) {
                    weathers = weathersNew;
                }
            }
        }
        return weathers;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 883555496)
    public synchronized void resetWeathers() {
        weathers = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 47395212)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDbCityDao() : null;
    }
}
