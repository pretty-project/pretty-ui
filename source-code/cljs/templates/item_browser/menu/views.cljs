
(ns templates.item-browser.menu.views
    (:require [templates.item-lister.menu.views :as menu.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu
  ; @param (keyword) browser-id
  ; @param (map) menu-props
  ; {:content (metamorphic-content)(opt)
  ;  :group-icon (keyword)
  ;  :group-label (metamorphic-content)}
  ;
  ; @usage
  ; [menu :my-lister {...}]
  [browser-id menu-props]
  [menu.views/menu browser-id menu-props])
