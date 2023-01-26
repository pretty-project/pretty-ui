
(ns elements.dropdown-menu.env
    (:require [elements.dropdown-menu.state :as dropdown-menu.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-surface-content
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {}
  ;
  ; @return (metamorphic-content)
  [menu-id {:keys [menu-items]}]
  (if-let [active-dex (-> @dropdown-menu.state/MENUS menu-id :active-dex)]
          (get-in menu-items [active-dex :content])))
