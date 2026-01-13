package com.arqivame.drive.domain.acl;

public enum AccessPermission {
    MANAGE(0),
    WRITE(1),
    READ(2);

    private final Integer level;

    AccessPermission(int level) {
        this.level = level;
    }

    public Integer level() {
        return level;
    }

    public Boolean isAtMost(final AccessPermission maximum) {
        return this.level >= maximum.level;
    }

}
