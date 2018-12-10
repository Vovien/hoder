package com.holderzone.intelligencepos.printer;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

// 迁移类示例
public class PushRealmMigration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        // DynamicRealm 暴露了一个可编辑的schema
        RealmSchema schema = realm.getSchema();

        // 迁移到版本 1 : 添加一个新的类
        // 示例:
        // public Person extends RealmObject {
        //     private String name;
        //     private int age;
        //     // getters and setters left out for brevity
        // }
        if (oldVersion == 0) {
            schema.get("PushPrintBean")
                    .removeField("obtainPrintContentDataTime")
                    .addField("hasObtain", boolean.class)
                    .transform(obj -> {
                    });
            schema.get("PushPrintDataRecord")
                    .removeField("finishDateTime")
                    .addField("printUseTime", long.class)
                    .transform(obj -> {
                    });
            oldVersion++;
        }

        // 迁移到版本 2 :添加一个primary key + 对象引用
        // 示例:
        // public Person extends RealmObject {
        //     private String name;
        //     @PrimaryKey
        //     private int age;
        //     private Dog favoriteDog;
        //     private RealmList<Dog> dogs;
        //     // getters and setters left out for brevity
        // }
        if (oldVersion == 1) {
//             schema.get("Person")
//                 .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
//                 .addRealmObjectField("favoriteDog", schema.get("Dog"))
//                 .addRealmListField("dogs", schema.get("Dog"));
//             oldVersion++;
        }
    }

}