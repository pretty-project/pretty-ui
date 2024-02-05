
(ns pretty-elements.dropdown-menu.prototypes
    (:require [fruits.noop.api                     :refer [return]]
              [fruits.vector.api                   :as vector]
              [pretty-elements.dropdown-menu.state :as dropdown-menu.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:menu-bar (map)(opt)}
  ;
  ; @return (map)
  [menu-id {:keys [menu-bar]}]
  ;(let [bar-id ()] ; (pretty-elements.engine/element-id->subitem-id menu-id :menu-bar)
  (-> menu-bar
         (assoc-in [:menu-item-default :menu-id] menu-id)))

(defn surface-props-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:surface (map)(opt)}
  ;
  ; @return (map)
  [_ {:keys [surface]}]
  (merge
     { :visible? false
           :positioning :absolute :layer :uppermost
           :width :parent}
     surface))
                       ;:fill-color :warning :width :l :height :l)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:menu-items (maps in vector)}
  [menu-id {:keys [item-default menu-items]}]
  (letfn [(f0 [dex %] (swap! dropdown-menu.state/MENUS assoc-in [menu-id :active-dex] dex))
          (f1 [dex %] (= dex (-> @dropdown-menu.state/MENUS menu-id :active-dex)))

          ; Sets the f0 function as the 'on-mouse-over-f' function on every item.
          ; By the f0 function, the items can set their indexes as the active index
          ; and the dropdown content can displays the active item's content by index.
          (f2 [dex %] (assoc % :on-mouse-over-f #(f0 dex %)
                               :highlighted?     (f1 dex %)))]

         ; Iterates over the menu items, applies these functions on them and returns
         ; the updated menu items vector.
         (vector/->items menu-items f2 {:provide-dex? true})))

(defn menu-props-prototype
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {}
  [menu-id menu-props]
  (-> menu-props))
