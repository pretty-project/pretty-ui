
(ns pretty-elements.dropdown-menu.prototypes
    (:require [fruits.noop.api                     :refer [return]]
              [fruits.vector.api                   :as vector]
              [pretty-elements.dropdown-menu.state :as dropdown-menu.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-items-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:menu-items (maps in vector)}
  [menu-id {:keys [item-default menu-items]}]
  (letfn [; ...
          (f0 [dex %] (swap! dropdown-menu.state/MENUS assoc-in [menu-id :active-dex] dex))

          ; If an item's index matches the active index, sets the hover color
          ; of the item as its fill color to makes the item looks like an active one.
          ; If the item has no hover color it tries to set the default hover color
          ; read from the item-default map (which contains the default settings of items).
          (f1 [dex %] (if (= dex (-> @dropdown-menu.state/MENUS menu-id :active-dex))
                          (or (:hover-color %)
                              (:hover-color item-default))))

          ; Sets the f0 function as the 'on-mouse-over-f' function on every item.
          ; By the f0 function, the items can set their indexes as the active index
          ; and the dropdown content can displays the active item's content by index.
          (f2 [dex %] (assoc % :on-mouse-over-f #(f0 dex %)
                               :fill-color       (f1 dex %)))]

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

(defn surface-props-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {}
  [_ menu-props]
  ; Filters the menu bar properties to avoid property duplications between the 'dropdown-menu'
  ; element and the implemented 'menu-bar' element.
  menu-props)

(defn bar-props-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {}
  [_ menu-props]
  ; Filters the menu bar properties to avoid property duplications between the 'dropdown-menu'
  ; element and the implemented 'menu-bar' element.
  (dissoc menu-props :class :indent :outdent :preset :style))
