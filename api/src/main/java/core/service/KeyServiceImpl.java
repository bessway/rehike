package core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.dao.KeyDao;
import core.pojo.KeyPojo;

@Service("KeyService")
public class KeyServiceImpl implements KeyService{
    @Autowired
    private KeyDao keyDao=null;
    public List<KeyPojo> getAllKeys(){
        return keyDao.getAllKeys();
    }

}