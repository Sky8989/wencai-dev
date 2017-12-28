package com.sftc.web.service.impl;

import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.jpa.AddressBookDao;
import com.sftc.web.dao.jpa.AddressDao;
import com.sftc.web.dao.mybatis.AddressBookMapper;
import com.sftc.web.dao.mybatis.AddressMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.dto.AddressBookDTO;
import com.sftc.web.model.dto.AddressDTO;
import com.sftc.web.model.entity.Address;
import com.sftc.web.model.entity.AddressBook;
import com.sftc.web.model.entity.User;
import com.sftc.web.model.vo.swaggerRequest.AddressBookDeleteVO;
import com.sftc.web.model.vo.swaggerRequest.AddressBookRequestVO;
import com.sftc.web.model.vo.swaggerRequest.AddressBookUpdateVO;
import com.sftc.web.service.AddressBookService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.sftc.tools.api.ApiStatus.SUCCESS;
import static com.sftc.tools.constant.DkConstant.DK_USER_AVATAR_DEFAULT;

/**
 * @author Administrator
 */
@Transactional(rollbackFor = Exception.class)
@Service("addressBookService")
public class AddressBookServiceImpl implements AddressBookService {
    @Resource
    private AddressBookMapper addressBookMapper;
    @Resource
    private AddressMapper addressMapper;

    @Resource
    private AddressBookDao addressBookDao;
    @Resource
    private AddressDao addressDao;
    @Resource
    private UserMapper userMapper;
    private Gson gson = new Gson();

    @Override
    public ApiResponse addAddressBook(AddressBookRequestVO addressBookRequestVO) {
        ///验参
        String userUUID = TokenUtils.getInstance().getUserUUID();
        if (StringUtils.isBlank(userUUID)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "该用户不存在");
        }
        if (addressBookRequestVO.getIs_delete() == null && addressBookRequestVO.getIs_delete() != 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数is_delete为空");
        }
        if (addressBookRequestVO.getIs_mystery() == null && addressBookRequestVO.getIs_mystery() != 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数is_mystery为空");
        }
        if (StringUtils.isEmpty(addressBookRequestVO.getAddress_type())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数address_type为空");
        }
        if (StringUtils.isEmpty(addressBookRequestVO.getAddress_book_type())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数address_book_type为空");
        }
        if (addressBookRequestVO.getAddress() == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数address为空");
        }
        Address address = addressBookRequestVO.getAddress();
        if (StringUtils.isEmpty(address.getName())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数name为空");
        }
        if (StringUtils.isEmpty(address.getPhone())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数phone为空");
        }
        if (StringUtils.isEmpty(address.getProvince())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数province为空");
        }
        if (StringUtils.isEmpty(address.getCity())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数city为空");
        }
        if (StringUtils.isEmpty(address.getArea())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数area为空");
        }
        if (StringUtils.isEmpty(address.getAddress())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数address为空");
        }
        if (address.getLongitude() == null || address.getLongitude() == 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数longitude为空");
        }
        if (address.getLatitude() == null || address.getLatitude() == 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数latitude为空");
        }

        String supplementaryInfo = StringUtils.isNotEmpty(address.getSupplementary_info()) ? address.getSupplementary_info() : null;

        AddressBookDTO addressBookDTO = new AddressBookDTO();
        //复制类
        BeanUtils.copyProperties(addressBookRequestVO, addressBookDTO,
                "address");
        // 查找重复信息  去重
        List<AddressBookDTO> addressBookDTOList = addressBookMapper.selectAddressForRemoveDuplicate(
                userUUID,
                addressBookRequestVO.getAddress_type(),
                addressBookRequestVO.getAddress_book_type(),
                address.getName(),
                address.getPhone(),
                address.getProvince(),
                address.getCity(),
                address.getArea(),
                address.getAddress(),
                supplementaryInfo
        );

        if (addressBookDTOList.size() != 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿已有该地址");
        }

        String createTime = Long.toString(System.currentTimeMillis());
        // 插入地址记录
        address.setCreate_time(createTime);
        address.setUser_uuid(userUUID);
        addressMapper.addAddress(address);
        // 插入地址簿记录
        addressBookDTO.setCreate_time(createTime);
        addressBookDTO.setAddress_id(address.getId());
        addressBookDTO.setUser_uuid(userUUID);

        AddressBook addressBook = gson.fromJson(gson.toJson(addressBookDTO), AddressBook.class);
        addressBookDao.save(addressBook);

        addressBookDTO.setAddress(address);
        addressBookDTO.setId(addressBook.getId());
        return ApiUtil.getResponse(SUCCESS, addressBookDTO);
    }

    @Override
    public ApiResponse deleteAddressBook(AddressBookDeleteVO addressBookDeleteVO) {
        ///验参
        if (StringUtils.isEmpty(addressBookDeleteVO.getAddressBook_id())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "addressBook_id参数为空");
        }
        Long addressBookId = Long.parseLong(addressBookDeleteVO.getAddressBook_id());

        AddressBook addressBook = addressBookDao.findOne(addressBookId);
        if (addressBook == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "该地址簿不存在");
        }
        addressBook.setId(addressBookId);
        addressBook.setIs_delete(1);
        /// 执行删除操作
        addressBookDao.save(addressBook);
        return ApiUtil.getResponse(SUCCESS, addressBook);
    }

    @Override
    public ApiResponse updateAddressBook(AddressBookUpdateVO addressBookUpdateVO) {
        // 修改地址时 改变创建时间 以供查询地址簿列表时根据时间排序
        AddressBookDTO addressBookParam = new AddressBookDTO();
        BeanUtils.copyProperties(addressBookUpdateVO, addressBookParam);
        if (StringUtils.isEmpty(addressBookUpdateVO.getId().toString()) || addressBookUpdateVO.getId() == 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿id为空");
        }

        //地址的参数中只有补充地址可以为空
        Address address = addressBookUpdateVO.getAddress();
        if (StringUtils.isEmpty(address.getName())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数name为空");
        }
        if (StringUtils.isEmpty(address.getProvince())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数province为空");
        }
        if (StringUtils.isEmpty(address.getCity())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数city为空");
        }
        if (StringUtils.isEmpty(address.getArea())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数area为空");
        }
        if (StringUtils.isEmpty(address.getAddress())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数address为空");
        }
        if (address.getLongitude() == null || address.getLongitude() == 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数longitude为空");
        }
        if (address.getLatitude() == null || address.getLatitude() == 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "地址簿参数latitude为空");
        }

        ///更新 地址簿记录时间 包括映射关系和地址实体的时间
        String createTime = Long.toString(System.currentTimeMillis());

        //TODO 修改地址映射的时间
        addressBookParam.setCreate_time(createTime);

        // 查找重复信息去重
        String supplementaryInfo = StringUtils.isNotEmpty(address.getSupplementary_info()) ? address.getSupplementary_info() : null;
        List<AddressBookDTO> addressBookDTOList = addressBookMapper.selectDuplicateAddress(
                address.getName(),
                address.getPhone(),
                address.getPhone(),
                address.getCity(),
                address.getArea(),
                address.getAddress(),
                supplementaryInfo
        );

        AddressBookDTO addressBookDTO = addressBookMapper.selectByPrimaryKey(addressBookParam.getId());
        if (addressBookDTO == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "该地址簿不存在");
        }

        if (addressBookDTOList.size() != 0) {
            return ApiUtil.error(HttpStatus.CONFLICT.value(), "地址簿已有该地址");
        } else {
            AddressBook addressBook = gson.fromJson(gson.toJson(addressBookDTO), AddressBook.class);
            addressBookDao.save(addressBook);

            address.setId(addressBook.getAddress_id());
            address.setCreate_time(createTime);
            address.setUser_uuid(addressBookDTO.getUser_uuid());
            addressDao.save(address);
        }

        return ApiUtil.getResponse(SUCCESS, null);
    }

    @Override
    public ApiResponse selectAddressBookList(String addressBookType) {
        String userUUID = TokenUtils.getInstance().getUserUUID();

        //执行查询
        List<AddressBookDTO> addressBookDTOS = addressBookMapper.selectAddressBookList(userUUID, addressBookType);
        if (addressBookDTOS.size() > 0) {
            return ApiUtil.getResponse(SUCCESS, addressBookDTOS);
        } else {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "用户无地址簿信息");
        }
    }

    /**
     * 查询历史地址
     *
     * @return 地址簿中address
     */
    @Override
    public ApiResponse selectAddressHistory(Integer pageNum, Integer pageSzie) {
        // Param
        if (StringUtil.isEmpty(pageNum.toString())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "pageNum不能为空");
        }
        if (StringUtil.isEmpty(pageSzie.toString())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "pageSize不能为空");
        }
        String userUUID = TokenUtils.getInstance().getUserUUID();
        if (StringUtils.isBlank(userUUID)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "user_uuid不正确");
        }
        if (pageNum < 1 || pageSzie < 1) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "分页参数无效");
        }

        // Handle avatar
        List<AddressBookDTO> addressBookDTOList = addressBookMapper.selectAddressHistoryListByUserUUId(userUUID, (pageNum - 1) * pageSzie, pageSzie);
        if (addressBookDTOList == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "暂无历史地址");
        }
        for (AddressBookDTO ab : addressBookDTOList) {
            Address address = ab.getAddress();
            User user = null;
            if (StringUtils.isNotBlank(address.getUser_uuid())) {
                user = userMapper.selectUserByUserUUId(address.getUser_uuid());
            }
            AddressDTO addressDTO = gson.fromJson(gson.toJson(address), AddressDTO.class);
            String avatar = (user == null || StringUtils.isBlank(user.getAvatar())) ? DK_USER_AVATAR_DEFAULT : user.getAvatar();
            addressDTO.setAvatar(avatar);
            String weChatName = (user == null || StringUtils.isBlank(user.getName())) ? "default_name" : user.getName();
            ab.setShip_wechatname(weChatName);
            ab.setAddressDTO(addressDTO);
        }

        return ApiUtil.getResponse(SUCCESS, addressBookDTOList);
    }


}
