package com.ruijie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruijie.mapper.AddressBookMapper;
import com.ruijie.pojo.AddressBook;
import com.ruijie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
