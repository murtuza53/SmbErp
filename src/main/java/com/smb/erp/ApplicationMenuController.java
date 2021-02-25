/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp;

import com.smb.erp.controller.ModuleController;
import com.smb.erp.controller.PageAccessController;
import com.smb.erp.controller.WebpageController;
import com.smb.erp.entity.Module;
import com.smb.erp.entity.PageAccess;
import com.smb.erp.entity.Webpage;
import com.smb.erp.util.JsfUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.web.context.annotation.SessionScope;

/**
 *
 * @author Burhani152
 */
@Named(value = "appmenuController")
@SessionScope
public class ApplicationMenuController implements Serializable {

    @Autowired
    ModuleController moduleCon;

    @Autowired
    WebpageController pageCon;

    @Autowired
    PageAccessController accessController;

    @Autowired
    UserSession userSession;

    private MenuModel menuModel;

    public ApplicationMenuController() {

    }

    @PostConstruct
    public void init() {
        if (userSession.isAdmin()) {
            loadMenu();
        } else {
            loadMenuForUser();
        }
    }

    public void loadMenuForUser() {
        setMenuModel(new DefaultMenuModel());

        List<PageAccess> pacesses = accessController.findAccessByRole(userSession.getLoggedInRole());

        if (pacesses == null || pacesses.isEmpty()) {
            JsfUtil.addErrorMessage("User doesn't have access to any module");
            return;
        }
        HashMap<Long, DefaultSubMenu> map = new HashMap<Long, DefaultSubMenu>();
        for (PageAccess pa : pacesses) {
            DefaultSubMenu smenu = map.get(pa.getPageid().getModuleid().getModuleid());
            if (smenu == null) {
                Module mod = pa.getPageid().getModuleid();
                smenu = DefaultSubMenu.builder().label(mod.getModulename()).build();
                getMenuModel().getElements().add(smenu);
                map.put(mod.getModuleid(), smenu);
            }
            smenu.getElements().add(createMenuItem(pa.getPageid()));
        }
    }

    public void loadMenu() {
        setMenuModel(new DefaultMenuModel());

        List<Module> modules = moduleCon.getActiveAll();

        modules.forEach((mod) -> {
            List<Webpage> pages = pageCon.getWebpagesByModule(mod.getModuleid());
            DefaultSubMenu smenu = null;
            if (pages != null && pages.size() > 0) {
                if (smenu == null) {
                    smenu = DefaultSubMenu.builder().label(mod.getModulename()).build();
                    getMenuModel().getElements().add(smenu);
                }
                for (Webpage page : pages) {
                    smenu.getElements().add(createMenuItem(page));
                }
            }
        });
    }

    public DefaultMenuItem createMenuItem(Webpage m) {
        DefaultMenuItem i = DefaultMenuItem.builder()
                .id(m.getJsfId())
                .value(m.getTitle()).build();
        if (m.getIcon() != null) {
            i.setIcon(m.getIcon());
        }
        if (m.getListurl() != null) {
            i.setUrl("../../" + m.getListurl());
            //i.setUrl("/" + m.getModule() + "/" + m.getAppmenuid());
        }

        return i;
    }

    /**
     * @return the menuModel
     */
    public MenuModel getMenuModel() {
        return menuModel;
    }

    /**
     * @param menuModel the menuModel to set
     */
    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }
}
