
(ns pretty-website.multi-menu.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-props-prototype
  ; @ignore
  ;
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {}
  [menu-props]
  ; By default threshold is set to 0, and the menu items visible independently
  ; from the viewport width.
  (merge {:threshold 0}
         (-> menu-props)))
