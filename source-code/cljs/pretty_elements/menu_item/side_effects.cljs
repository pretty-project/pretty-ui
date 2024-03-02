
(ns pretty-elements.menu-item.side-effects
    (:require [pretty-elements.dropdown-menu.side-effects :as dropdown-menu.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-mouse-over-f
  ; @ignore
  ;
  ; @description
  ; In case the ':dropdown-content' and ':dropdown-menu-id' properties are provided,
  ; the mouse over event of the menu item sets the given dropdown content
  ; on the dropdown menu (identified by the given menu ID).
  ;
  ; @param (keyword) item-id
  ; @param (map) item-props
  ; {:dropdown-content (multitype-content)(opt)
  ;  :dropdown-menu-id (keyword)(opt)
  ;  ...}
  [_ {:keys [dropdown-content dropdown-menu-id]}]
  (and dropdown-content dropdown-menu-id (dropdown-menu.side-effects/set-dropdown-content! dropdown-menu-id dropdown-content)))
