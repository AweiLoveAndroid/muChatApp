package com.example.factory.data.helper;

import com.example.common.utils.CollectionUtil;
import com.example.factory.model.db.AppDatabase;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.GroupMember;
import com.example.factory.model.db.Group_Table;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Session;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据库的存储框架
 * 辅助完成增删改操作
 * Created by John on 2017/6/28.
 */

public class DbHelper {
    private static final DbHelper instance;

    static {
        instance = new DbHelper();
    }

    private DbHelper() {

    }

    /**
     * 观察者集合
     * Class:观察的表
     * changedListeners:每一个表对应的观察者
     */
    private final Map<Class<?>, Set<ChangedListener>> changedListeners = new HashMap<>();

    /**
     * 从所有的监听器中获取一个表的所有监听者
     *
     * @param modelClass 表对应的class信息
     * @param <Model>
     * @return
     */
    private <Model extends BaseModel> Set<ChangedListener> getListeners(Class<Model> modelClass) {
        if (changedListeners.containsKey(modelClass)) {
            return changedListeners.get(modelClass);
        }
        return null;
    }

    /**
     * 添加一个监听
     *
     * @param tClass   关注的表
     * @param listener 监听者
     * @param <Model>  表的泛型
     */
    public static <Model extends BaseModel> void addChangedListener
    (Class<Model> tClass, ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            //初始化某一类型的容器
            changedListeners = new HashSet<>();
            //添加到总的map
            instance.changedListeners.put(tClass, changedListeners);
        }
        changedListeners.add(listener);
    }

    /**
     * 删除一个表的监听器
     *
     * @param tClass   表名
     * @param listener 监听器
     * @param <Model>  表的泛型
     */
    public static <Model extends BaseModel> void removeChangedListener
    (Class<Model> tClass, ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            return;
        }
        changedListeners.remove(listener);
    }

    /**
     * 新增或者修改
     *
     * @param tClass  传递一个class信息
     * @param models  class对应的实例数组
     * @param <Model> 这个实例的泛型
     */
    public static <Model extends BaseModel> void save(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0)
            return;
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        //提交一个事物
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                adapter.saveAll(CollectionUtil.toArrayList(models));
                instance.notifySave(tClass, models);
            }
        }).build().execute();
    }

    /**
     * 删除操作
     *
     * @param tClass
     * @param models
     * @param <Model>
     */
    public static <Model extends BaseModel> void delete(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0)
            return;
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        //提交一个事物
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                adapter.deleteAll(CollectionUtil.toArrayList(models));
                instance.notifyDelete(tClass, models);
            }
        }).build().execute();
    }

    /**
     * 进行通知调用
     *
     * @param tClass  通知的类型
     * @param models  通知的model数组
     * @param <Model> 实例的泛型
     */
    public final <Model extends BaseModel> void notifySave(final Class<Model> tClass, final Model... models) {
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataSave(models);
            }
        }
        if (GroupMember.class.equals(tClass)) {
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            updateSession((Message[]) models);
        }
    }

    /**
     * 从成员中找出成员对应的群，并对群进行更新
     * @param members
     */
    private void  updateGroup(GroupMember...members){
        final Set<String>groupIds=new HashSet<>();
        for (GroupMember member : members) {
            //添加群ID
            groupIds.add(member.getGroup().getId());
            DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
            //提交一个事物
            definition.beginTransactionAsync(new ITransaction() {
                @Override
                public void execute(DatabaseWrapper databaseWrapper) {
                    //找到需要通知的群
                    List<Group>groups= SQLite.select()
                            .from(Group.class)
                            .where(Group_Table.id.in(groupIds))
                            .queryList();
                    //通知
                    instance.notifySave(Group.class,groups.toArray(new Group[0]));
                }
            }).build().execute();
        }

    }

    /**
     * 从消息列表中筛选出对应的会话并对会话进行更新
     * @param messages
     */
    private void updateSession(Message...messages){
        //标识一个session的唯一性
        final Set<Session.Identify>identifies=new HashSet<>();
        for (Message message : messages) {
            Session.Identify identify=Session.createSessionIdentify(message);
            identifies.add(identify);
        }
        if(identifies.size()==0)
            return;
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        //提交一个事物
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Session>adapter=FlowManager.getModelAdapter(Session.class);
                Session[]sessions=new Session[identifies.size()];
                int index=0;
                for (Session.Identify identify : identifies) {
                    Session session=SessionHelper.findFromLocal(identify.id);
                    if(session==null){
                        //第一次聊天创建一个会话
                        session=new Session(identify);
                    }
                    //会话刷新到当前message的最新状态
                    session.refreshToNow();
                    adapter.save(session);
                    sessions[index++]=session;//添加到集合
                }
                //通知
                instance.notifySave(Session.class,sessions);
            }
        }).build().execute();
    }

    public final <Model extends BaseModel> void notifyDelete(final Class<Model> tClass, final Model... models) {
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null || listeners.size() > 0) {
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataDelete(models);
            }
        }
        if (GroupMember.class.equals(tClass)) {
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            updateSession((Message[]) models);
        }
    }

    /**
     * 通知监听器
     */
    @SuppressWarnings({"unused", "unchecked"})
    public interface ChangedListener<Data extends BaseModel> {
        void onDataSave(Data... list);
        void onDataDelete(Data... list);
    }
}
