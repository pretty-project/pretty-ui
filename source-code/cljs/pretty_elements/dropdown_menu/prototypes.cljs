
(ns pretty-elements.dropdown-menu.prototypes
    (:require [fruits.vector.api                   :as vector]
              [pretty-elements.dropdown-menu.state :as dropdown-menu.state]
              [pretty-build-kit.api :as pretty-build-kit]
              [fruits.noop.api :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-items-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:menu-items (maps in vector)}
  [menu-id {:keys [item-default menu-items]}]
  (letfn [; XXX#1239
          ; The :on-mouse-over property of items in menu-bar element takes metamorphic-events.
          ; If the f0 function returned the reseted value (by the reset! function),
          ; the on-mouse-over handler would try to dispatch it as a metamorphic-event.
          ; Therefore, the f0 function returns a nil to avoid that.
          (f0 [dex %] (swap! dropdown-menu.state/MENUS assoc-in [menu-id :active-dex] dex)
                      (-> nil))

          ; If an item's index matches the active index, sets the hover color
          ; of the item as its fill color to makes the item looks like an active one.
          ; If the item has no hover color it tries to set the default hover color
          ; read from the item-default map (which contains the default settings of items).
          (f1 [dex %] (if (= dex (-> @dropdown-menu.state/MENUS menu-id :active-dex))
                          (or (:hover-color %)
                              (:hover-color item-default))))

          ; Sets the f0 function as an on-mouse-over event on every item.
          ; By the f0 function, the items can set their index as the active index
          ; and the dropdown content can displays the active item's content by index.
          (f2 [dex %] (assoc % :on-mouse-over #(f0 dex %)
                               :fill-color     (f1 dex %)))]

         ; Iterates over the menu items, applies these functions on them and returns
         ; the updated menu items vector.
         (vector/->items menu-items f2 {:provide-dex? true})))

(defn surface-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {{:keys [border-color]} :surface :keys [surface] :as menu-props}]
  (merge (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> surface)))

(defn menu-props-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {}
  [menu-id menu-props]
  (merge menu-props {:menu-items (menu-items-prototype menu-id menu-props)
                     :surface    (surface-prototype    menu-id menu-props)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; @ignore
  ;
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {}
  [menu-props]
  ; Filters the menu bar properties to avoid property duplications between the 'dropdown-menu'
  ; element and the implemented 'menu-bar' element.
  (dissoc menu-props :class :indent :outdent :style))
