/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smb.erp;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class GuestPreferences implements Serializable {

    private static final long serialVersionUID = 1L;

    private String layout = "moody";

    private String theme = "noir";

    private boolean darkMenu = true;

    private boolean gradientMenu = true;

    private boolean darkMegaMenu = true;

    private boolean gradientMegaMenu = true;

    private String menuLayout = "layout-wrapper-slim-sidebar";	//layout-wrapper-slim-sidebar	//static

    private String busdocMenuLayout = "layout-wrapper-overlay-sidebar";  //"layout-wrapper-horizontal-sidebar";    //"layout-wrapper-overlay-sidebar"

    private String profileMode = "inline";

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public boolean isDarkMenu() {
        return this.darkMenu;
    }

    public boolean isGradientMenu() {
        return this.gradientMenu;
    }

    public boolean isDarkMegaMenu() {
        return this.darkMegaMenu;
    }

    public boolean isGradientMegaMenu() {
        return this.gradientMegaMenu;
    }

    public void setMenuMode(boolean dark, boolean gradient, String theme) {
        this.darkMenu = dark;
        this.gradientMenu = gradient;
        this.theme = theme;
    }

    public void setMegaMenuMode(boolean dark, boolean gradient) {
        this.darkMegaMenu = dark;
        this.gradientMegaMenu = gradient;
    }

    public String getMenuLayout() {
        return menuLayout;
    }

    public void setMenuLayout(String menuLayout) {
        this.menuLayout = menuLayout;

        if (this.menuLayout.equals("layout-wrapper-horizontal-sidebar")) {
            this.profileMode = "topbar";
        }
    }

    public String getProfileMode() {
        return profileMode;
    }

    public void setProfileMode(String profileMode) {
        if (this.menuLayout.equals("layout-wrapper-horizontal-sidebar")) {
            this.profileMode = "topbar";
        } else {
            this.profileMode = profileMode;
        }
    }

    /**
     * @return the busdocMenuLayout
     */
    public String getBusdocMenuLayout() {
        return busdocMenuLayout;
    }

    /**
     * @param busdocMenuLayout the busdocMenuLayout to set
     */
    public void setBusdocMenuLayout(String busdocMenuLayout) {
        this.busdocMenuLayout = busdocMenuLayout;
    }
}
