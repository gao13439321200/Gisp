package com.giiisp.giiisp.dto;

import java.util.List;

public class GroupInfoBean extends BaseBean {

    private GroupOwnerInfo group;

    private List<GroupMemberInfo> groupusers;

    public GroupOwnerInfo getGroup() {
        return group;
    }

    public void setGroup(GroupOwnerInfo group) {
        this.group = group;
    }

    public List<GroupMemberInfo> getGroupusers() {
        return groupusers;
    }

    public void setGroupusers(List<GroupMemberInfo> groupusers) {
        this.groupusers = groupusers;
    }
}
