package com.smb.erp.controller;

import com.smb.erp.repo.BaseRepository;
import com.smb.erp.util.JsfUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ActionEvent;
import org.springframework.data.domain.Sort;

/**
 * Represents an abstract shell of to be used as JSF Controller to be used in
 * AJAX-enabled applications. No outcomes will be generated from its methods
 * since handling is designed to be done inside one page.
 *
 * @param <T> the concrete Entity type of the Controller bean to be created
 */
public abstract class AbstractController<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Class<T> itemClass;
    protected T selected;
    protected List<T> items;
    private BaseRepository repository;
    private Sort sortFields;

    DocumentTab.MODE mode = DocumentTab.MODE.LIST;

    public AbstractController() {
    }

    public AbstractController(Class<T> itemClass, BaseRepository repo) {
        this.itemClass = itemClass;
        this.repository = repo;
    }

    public T find(Object key) {
        Optional<T> val = repository.findById(key);
        if (val.isPresent()) {
            return val.get();
        }
        return null;
    }

    /**
     * Retrieve the currently selected item.
     *
     * @return the currently selected Entity
     */
    public T getSelected() {
        return selected;
    }

    /**
     * Pass in the currently selected item.
     *
     * @param selected the Entity that should be set as selected
     */
    public void setSelected(T selected) {
        this.selected = selected;
    }

    /**
     * Returns all items as a Collection object.
     *
     * @return a collection of Entity items returned by the data layer
     */
    public List<T> getItems() {
        if (items == null) {
            if (sortFields != null) {
                items = repository.findAll(sortFields);
                System.out.println("SortedList: " + items.size());
            } else {
                items = repository.findAll();
                System.out.println("List: " + items.size());
            }
        }
        return items;
    }

    /**
     * Pass in collection of items
     *
     * @param items a collection of Entity items
     */
    public void setItems(List<T> items) {
        this.items = items;
    }

    /**
     * Inform the user interface whether any validation error exist on a page.
     *
     * @return a logical value whether form validation has passed or failed
     */
    public boolean isValidationFailed() {
        return JsfUtil.isValidationFailed();
    }

    /**
     * Retrieve all messages as a String to be displayed on the page.
     *
     * @param clientComponent the component for which the message applies
     * @param defaultMessage a default message to be shown
     * @return a concatenation of all messages
     */
    public String getComponentMessages(String clientComponent, String defaultMessage) {
        return JsfUtil.getComponentMessages(clientComponent, defaultMessage);
    }

    /**
     * Creates a new instance of an underlying entity and assigns it to Selected
     * property.
     *
     * @param event an event from the widget that wants to create a new,
     * unmanaged Entity for the data layer
     * @return a new, unmanaged Entity
     */
    public T prepareCreate(ActionEvent event) {
        //System.out.println("prepareCreate: " + new Date());
        T newItem;
        try {
            newItem = itemClass.newInstance();
            this.selected = newItem;
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Sets the concrete embedded key of an Entity that uses composite keys.
     * This method will be overriden inside concrete controller classes and does
     * nothing if the specific entity has no composite keys.
     */
    protected void initializeEmbeddableKey() {
        // Nothing to do if entity does not have any embeddable key.
    }

    public void save() {
        if (selected != null) {
            try {
                repository.save(selected);
                setItems(null);
                //JsfUtil.addSuccessMessage(itemClass.getSimpleName() + " saved");
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "Could not save due to error");
            }
        }
    }

    public void delete() {
        if (selected != null) {
            try {
                repository.delete(selected);
                setItems(null);
                //JsfUtil.addSuccessMessage(itemClass.getSimpleName() + " deleted");
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "Could not deleted due to error");
            }
        }
    }

    /**
     * @return the sortField
     */
    public Sort getSortFields() {
        return sortFields;
    }

    /**
     * @param sortField the sortField to set
     */
    public void setSortFields(Sort sortFields) {
        this.sortFields = sortFields;
    }
}
