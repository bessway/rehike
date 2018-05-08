package core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.dao.KeyDaoImpl;
import core.pojo.KeyPojo;

@Service("KeyService")
public class KeyServiceImpl{
    @Autowired
    private KeyDaoImpl project=null;
    public List<KeyPojo> getAllKeys(){
        return project.getAllKeys();
    }

}