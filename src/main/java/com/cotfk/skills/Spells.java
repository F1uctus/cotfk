package com.cotfk.skills;

import com.cotfk.creatures.CreatureBase;
import com.crown.common.ObjectsMap;
import com.crown.i18n.I18n;
import com.crown.i18n.ITemplate;
import com.crown.time.Action;

public class Spells extends ObjectsMap<Spell> {
    private static Spells instance;

    public static Spells getSpells() {
        if (instance == null) {
            synchronized (Spells.class) {
                if (instance == null) {
                    instance = new Spells();
                }
            }
        }
        return instance;
    }

    Spells() {
        add(new Spell<>(
            CreatureBase.class,
            "Fatigue",
            new Action<>(null) {
                @Override
                public ITemplate perform() {
                    for (int i = 0; i < 5; i++) {
                        getTarget().changeEnergy(-10);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {
                        }
                    }
                    return I18n.okMessage;
                }

                @Override
                public ITemplate rollback() {
                    for (int i = 0; i < 5; i++) {
                        getTarget().changeEnergy(10);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {
                        }
                    }
                    return I18n.okMessage;
                }
            },
            15,
            10,
            5,
            false
        ));

        add(new Spell<>(
            CreatureBase.class,
            "Snare",
            new Action<>(null) {
                @Override
                public ITemplate perform() {
                    getTarget().changeSpeed(-1);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ignored) {
                    }
                    getTarget().changeSpeedBy(1);
                    return I18n.okMessage;
                }

                @Override
                public ITemplate rollback() {
                    getTarget().changeSpeed(1);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ignored) {
                    }
                    getTarget().changeSpeedBy(-1);
                    return I18n.okMessage;
                }
            },
            15,
            10,
            5,
            false
        ));

        add(new Spell<>(
            CreatureBase.class,
            "Fireball",
            new Action<>(null) {
                @Override
                public ITemplate perform() {
                    for (int i = 0; i < 5; i++) {
                        getTarget().changeHp(-10);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {
                        }
                    }
                    return I18n.okMessage;
                }

                @Override
                public ITemplate rollback() {
                    for (int i = 0; i < 5; i++) {
                        getTarget().changeHp(10);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {
                        }
                    }
                    return I18n.okMessage;
                }
            },
            15,
            10,
            5,
            false
        ));
    }
}
