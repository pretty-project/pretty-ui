
(ns pretty-accessories.bullet.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bullet-body-attributes
  ; @ignore
  ;
  ; @param (keyword) bullet-id
  ; @param (map) bullet-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [bullet-id bullet-props]
  (-> {:class :pa-bullet--body}
      (pretty-attributes/background-color-attributes bullet-props)
      (pretty-attributes/body-size-attributes        bullet-props)
      (pretty-attributes/border-attributes           bullet-props)
      (pretty-attributes/indent-attributes           bullet-props)
      (pretty-attributes/style-attributes            bullet-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bullet-attributes
  ; @ignore
  ;
  ; @param (keyword) bullet-id
  ; @param (map) bullet-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ bullet-props]
  (-> {:class :pa-bullet}
      (pretty-attributes/class-attributes    bullet-props)
      (pretty-attributes/outdent-attributes  bullet-props)
      (pretty-attributes/position-attributes bullet-props)
      (pretty-attributes/size-attributes     bullet-props)
      (pretty-attributes/state-attributes    bullet-props)
      (pretty-attributes/theme-attributes    bullet-props)))
