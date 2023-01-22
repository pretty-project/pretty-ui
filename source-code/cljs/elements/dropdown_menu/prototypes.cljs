
(ns elements.dropdown-menu.prototypes
    (:require [elements.dropdown-menu.state :as dropdown-menu.state]
              [noop.api                     :refer [param return]]
              [vector.api                   :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-items-prototype
  ; @ignore
  ;
  ; @param (map) menu-props
  ; {:menu-items (maps in vector)}
  [{:keys [item-default menu-items]}]
  (letfn [; XXX#1239
          ; The :on-mouse-over property of items in menu-bar element takes metamorphic-events.
          ; In case of the f0 function returns the reseted value (returned by the reset! function),
          ; the on-mouse-over handler might try to dispatch it as a metamorphic-event.
          ; Therefore the f0 function returns a nil to avoid this.
          (f0 [dex %] (reset! dropdown-menu.state/ACTIVE-DEX dex)
                      (return nil))

          ; If an item's index matches with the active index, sets the hover color
          ; of the item as its fill color to makes the item looks like an active one.
          ; If the item has no hover color it tries to set the default hover color
          ; read from the item-default map (which contains the default settings of items).
          (f1 [dex %] (if (= @dropdown-menu.state/ACTIVE-DEX dex)
                          (or (:hover-color %)
                              (:hover-color item-default))))

          ; Sets the f0 function as an on-mouse-over event on every item.
          ; By the f0 function, the items can set their index as the active index
          ; and the dropdown content can displays the active item's content by index.
          (f2 [dex %] (assoc % :on-mouse-over #(f0 dex %)
                               :fill-color     (f1 dex %)))]

         ; Iterates over the menu items, applies these functions on them and returns
         ; the updated menu items vector.
         (vector/->items-indexed menu-items f2)))

(defn menu-props-prototype
  ; @ignore
  ;
  ; @param (map) menu-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [border-color] :as menu-props}]
  (merge (if border-color {:border-position :all
                           :border-width    :xxs})
         (param menu-props)
         {:menu-items (menu-items-prototype menu-props)}))
