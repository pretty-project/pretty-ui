
(ns pretty-elements.menu-bar.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-body-attributes
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ bar-props]
  (-> {:class :pe-menu-bar--body}
      (pretty-attributes/background-color-attributes  bar-props)
      (pretty-attributes/border-attributes            bar-props)
      (pretty-attributes/double-block-size-attributes bar-props)
      (pretty-attributes/flex-attributes              bar-props)
      (pretty-attributes/indent-attributes            bar-props)
      (pretty-attributes/style-attributes             bar-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-attributes
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ bar-props]
  (-> {:class :pe-menu-bar}
      (pretty-attributes/class-attributes        bar-props)
      (pretty-attributes/outdent-attributes      bar-props)
      (pretty-attributes/state-attributes        bar-props)
      (pretty-attributes/theme-attributes        bar-props)
      (pretty-attributes/wrapper-size-attributes bar-props)))
