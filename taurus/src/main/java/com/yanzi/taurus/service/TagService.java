package com.yanzi.taurus.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanzi.common.constants.ReturnCode;
import com.yanzi.common.entity.user.TagInfo;
import com.yanzi.common.exception.CommonException;
import com.yanzi.taurus.data.TagData;
import com.yanzi.taurus.mysql.TagMapper;

@Service
public class TagService {

    @Autowired
    private TagData tagdata;
    @Autowired
    private TagMapper tagMapper;

    // public List<Long> getFollowedTagIdsByUserId(long userId) {
    // List<Long> followedTagIdList = tagMapper.selectTagIdsByUserId(userId);
    // List<Long> resultList = new ArrayList<>();
    // for (long tagId : followedTagIdList) {
    // if (tagInfoMap.containsKey(tagId)) {
    // resultList.add(tagId);
    // }
    // }
    // return resultList;
    // }

    public List<TagInfo> loadUserFollowTags(long userId) {
        List<Long> followedTagIdList = tagMapper.selectTagIdsByUserId(userId);
        List<TagInfo> resultList = new ArrayList<>();
        for (long tagId : followedTagIdList) {
            TagInfo tagInfo = tagdata.get(tagId);
            if (null != tagInfo) {
                resultList.add(tagInfo);
            }
        }
        return resultList;
    }

    public void userFollowTags(long userId, long[] tagIds) {
        if (tagIds == null || tagIds.length == 0) {
            return;
        }
        for (long tagId : tagIds) {
            if (!tagdata.contains(tagId)) {
                throw new CommonException(ReturnCode.USER_TAG_IS_NOT_EXIST);
            }
        }
        tagMapper.deleteAllFollowedTags(userId);
        tagMapper.insertOrUpdateTagIdsByUserId(userId, tagIds);
    }
}
