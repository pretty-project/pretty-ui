
(ns components.user-avatar.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn avatar-props-prototype
  ; @param (map) avatar-props
  ;
  ; @return (map)
  ; {:colors (strings in vector)
  ;  :icon (keyword)
  ;  :icon-family (keyword)
  ;  :size (px)}
  [avatar-props]
  (merge {:colors      ["var( --color-muted )"]
          :icon        :person
          :icon-family :material-symbols-outlined
          :size        60}
         (param avatar-props)))
