/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.entity.converter;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.springframework.stereotype.Service;

/**
 *
 * @author Burhani152
 */
@Service
public class EntityConverter implements Converter{

    private static Map<Object, String> entities = new WeakHashMap<Object, String>();
    //private final static String SELECT_ONE = "Select Item";
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (Map.Entry<Object, String> entry : entities.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object entity) {
        //if(entity==null){
        //    return SELECT_ONE;
        //}
        synchronized (entities) {
            if (!entities.containsKey(entity)) {
                String uuid = UUID.randomUUID().toString();
                entities.put(entity, uuid);
                return uuid;
            } else {
                return entities.get(entity);
            }
        }
    }
    
}
