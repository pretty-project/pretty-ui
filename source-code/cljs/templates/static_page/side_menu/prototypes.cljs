
(ns templates.static-page.side-menu.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-props-prototype
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {}
  [menu-props]
  (merge {:border-color    :highlight
          :border-position :right
          :indent          {:left :xs}
          :min-width       :m
          :threshold       640}
         (param menu-props)))
