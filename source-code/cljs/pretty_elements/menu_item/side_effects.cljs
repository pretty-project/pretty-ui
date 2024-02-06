
(ns pretty-elements.menu-item.side-effects
    (:require [pretty-elements.dropdown-menu.side-effects :as dropdown-menu.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-mouse-over-f
  ; @ignore
  ;
  ; @param (keyword) item-id
  ; @param (map) item-props
  ; {:dropdown-content (metamorphic-content)(opt)
  ;  :menu-id (keyword)(opt)}
  [_ {:keys [dropdown-content menu-id]}]
  (and dropdown-content menu-id (dropdown-menu.side-effects/set-dropdown-content! menu-id dropdown-content)))
