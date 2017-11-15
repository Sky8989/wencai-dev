package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.jpa.AddressBookDao;
import com.sftc.web.dao.jpa.AddressDao;
import com.sftc.web.dao.mybatis.AddressBookMapper;
import com.sftc.web.dao.mybatis.AddressMapper;
import com.sftc.web.model.dto.AddressBookDTO;
import com.sftc.web.model.entity.Address;
import com.sftc.web.model.entity.AddressBook;
import com.sftc.web.service.AddressBookService;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Transactional
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

    private Gson gson = new Gson();

    public APIResponse addAddressBook(APIRequest apiRequest) {
        ///验参
        JSONObject paramObject = JSONObject.fromObject(apiRequest.getRequestParam());
        Integer user_id = TokenUtils.getInstance().getUserId();
        if (user_id == 0) return APIUtil.paramErrorResponse("该用户不存在");
        if (!paramObject.containsKey("is_delete")) return APIUtil.paramErrorResponse("地址簿参数is_delete为空");
        if (!paramObject.containsKey("is_mystery")) return APIUtil.paramErrorResponse("地址簿参数is_mystery为空");
        if (!paramObject.containsKey("address_type")) return APIUtil.paramErrorResponse("地址簿参数address_type为空");
        if (!paramObject.containsKey("address_book_type"))
            return APIUtil.paramErrorResponse("地址簿参数address_book_type为空");
        if (!paramObject.containsKey("address")) return APIUtil.paramErrorResponse("地址簿参数address为空");
        JSONObject address_OBJ = paramObject.getJSONObject("address");

        if (!address_OBJ.containsKey("name") || address_OBJ.getString("name").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数name为空");
        if (!address_OBJ.containsKey("phone") || address_OBJ.getString("phone").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数phone为空");
        if (!address_OBJ.containsKey("province") || address_OBJ.getString("province").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数province为空");
        if (!address_OBJ.containsKey("city") || address_OBJ.getString("city").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数city为空");
        if (!address_OBJ.containsKey("area") || address_OBJ.getString("area").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数area为空");
        if (!address_OBJ.containsKey("address") || address_OBJ.getString("address").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数address为空");
        if (!address_OBJ.containsKey("longitude") || address_OBJ.getString("longitude").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数longitude为空");
        if (!address_OBJ.containsKey("latitude") || address_OBJ.getString("latitude").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数latitude为空");


        String supplementary_info = address_OBJ.containsKey("supplementary_info") ? address_OBJ.getString("supplementary_info") : null;
        AddressBookDTO addressBookDTO;
        Address address;
        try {
            addressBookDTO = (AddressBookDTO) JSONObject.toBean(paramObject, AddressBookDTO.class);
            address = (Address) JSONObject.toBean(address_OBJ, Address.class);
        } catch (JSONException e) {
            return APIUtil.paramErrorResponse("参数含有非法字符");
        }

        // 查找重复信息  去重
        List<AddressBookDTO> addressBookDTOList = addressBookMapper.selectAddressForRemoveDuplicate(
                user_id,
                paramObject.getString("address_type"),
                paramObject.getString("address_book_type"),
                address_OBJ.getString("name"),
                address_OBJ.getString("phone"),
                address_OBJ.getString("province"),
                address_OBJ.getString("city"),
                address_OBJ.getString("area"),
                address_OBJ.getString("address"),
                supplementary_info
        );

        if (addressBookDTOList.size() != 0) return APIUtil.submitErrorResponse("地址簿已有该地址", null);

        String create_time = Long.toString(System.currentTimeMillis());
        // 插入地址记录
        address.setCreate_time(create_time);
        address.setUser_id(addressBookDTO.getUser_id());
        address.setUser_id(user_id);
        addressMapper.addAddress(address);
        // 插入地址簿记录
        addressBookDTO.setCreate_time(create_time);
        addressBookDTO.setAddress_id(address.getId());
        addressBookDTO.setUser_id(user_id);
        AddressBook addressBook = gson.fromJson(gson.toJson(addressBookDTO), AddressBook.class);
        addressBookDao.save(addressBook);

        addressBookDTO.setAddress(address);
        addressBookDTO.setId(addressBook.getId());
        return APIUtil.getResponse(SUCCESS, addressBookDTO);
    }


    public APIResponse deleteAddressBook(APIRequest apiRequest) {
        ///验参
        JSONObject paramObject = JSONObject.fromObject(apiRequest.getRequestParam());
        if (paramObject.getString("addressBook_id") == null || "".equals(paramObject.getString("addressBook_id")))
            return APIUtil.paramErrorResponse("addressBook_id参数为空");
        Long addressBook_id = Long.parseLong(paramObject.getString("addressBook_id"));

        AddressBook addressBook = addressBookDao.findOne(addressBook_id);
        if (addressBook == null) return APIUtil.selectErrorResponse("该地址簿不存在", null);
        addressBook.setId(addressBook_id);
        addressBook.setIs_delete(1);
        /// 执行删除操作
        addressBookDao.save(addressBook);
        return APIUtil.getResponse(SUCCESS, addressBook);
    }


    public APIResponse updateAddressBook(APIRequest apiRequest) {
        // 修改地址时 改变创建时间 以供查询地址簿列表时根据时间排序
        JSONObject paramObject = JSONObject.fromObject(apiRequest.getRequestParam());
        AddressBookDTO addressBookParam = (AddressBookDTO) JSONObject.toBean(paramObject, AddressBookDTO.class);
        if (!paramObject.containsKey("id")) return APIUtil.paramErrorResponse("地址簿id为空");

        //地址的参数中只有补充地址可以为空
        JSONObject address_OBJ = paramObject.getJSONObject("address");
        if (!address_OBJ.containsKey("name") || address_OBJ.getString("name").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数name为空");
        if (!address_OBJ.containsKey("province") || address_OBJ.getString("province").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数province为空");
        if (!address_OBJ.containsKey("city") || address_OBJ.getString("city").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数city为空");
        if (!address_OBJ.containsKey("area") || address_OBJ.getString("area").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数area为空");
        if (!address_OBJ.containsKey("address") || address_OBJ.getString("address").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数address为空");
        if (!address_OBJ.containsKey("longitude") || address_OBJ.getString("longitude").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数longitude为空");
        if (!address_OBJ.containsKey("latitude") || address_OBJ.getString("latitude").equals(""))
            return APIUtil.paramErrorResponse("地址簿参数latitude为空");

        ///更新 地址簿记录时间 包括映射关系和地址实体的时间
        String create_time = Long.toString(System.currentTimeMillis());

        //TODO 修改地址映射的时间
        addressBookParam.setCreate_time(create_time);

        // 查找重复信息去重
        String supplementary_info = address_OBJ.containsKey("supplementary_info") ? address_OBJ.getString("supplementary_info") : null;
        List<AddressBookDTO> addressBookDTOList = addressBookMapper.selectDuplicateAddress(
                address_OBJ.getString("name"),
                address_OBJ.getString("phone"),
                address_OBJ.getString("province"),
                address_OBJ.getString("city"),
                address_OBJ.getString("area"),
                address_OBJ.getString("address"),
                supplementary_info
        );
        AddressBookDTO addressBookDTO = addressBookMapper.selectByPrimaryKey(addressBookParam.getId());
        if (addressBookDTOList.size() != 0) {

            if (addressBookDTO == null) return APIUtil.selectErrorResponse("该地址簿不存在", null);
            addressMapper.deleteAddress(addressBookDTO.getAddress_id());
            addressBookMapper.deleteAddressBookById(addressBookDTO.getId());
        } else {

            if (addressBookDTO == null) return APIUtil.selectErrorResponse("该地址簿不存在", null);
            AddressBook addressBook = gson.fromJson(gson.toJson(addressBookDTO), AddressBook.class);
            addressBookDao.save(addressBook);

            Address address = addressBookParam.getAddress();
            address.setId(addressBook.getAddress_id());
            address.setCreate_time(create_time);
            address.setUser_id(addressBookDTO.getUser_id());
            addressDao.save(address);
        }

        return APIUtil.getResponse(SUCCESS, null);
    }


    public APIResponse selectAddressBookList(APIRequest apiRequest) {
        /// 处理参数
        HttpServletRequest httpServletRequest = apiRequest.getRequest();
        Integer user_id = TokenUtils.getInstance().getUserId();
        String address_book_type = httpServletRequest.getParameter("address_book_type");

        //执行查询
        List<AddressBookDTO> addressBookDTOS = addressBookMapper.selectAddressBookList(user_id, address_book_type);
        if (addressBookDTOS.size() > 0) {
            return APIUtil.getResponse(SUCCESS, addressBookDTOS);
        } else {
            return APIUtil.selectErrorResponse("用户无地址簿信息", null);
        }
    }


}
