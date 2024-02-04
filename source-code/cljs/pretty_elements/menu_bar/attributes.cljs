
(ns pretty-elements.menu-bar.attributes
    (:require [pretty-css.appearance.api  :as pretty-css.appearance]
              [pretty-css.basic.api       :as pretty-css.basic]
              [pretty-css.layout.api      :as pretty-css.layout]))

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
      (pretty-css.basic/style-attributes              bar-props)
      (pretty-css.layout/double-block-size-attributes bar-props)
      (pretty-css.layout/flex-attributes              bar-props)
      (pretty-css.layout/indent-attributes            bar-props)))

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
      (pretty-css.appearance/theme-attributes    bar-props)
      (pretty-css.basic/class-attributes         bar-props)
      (pretty-css.basic/state-attributes         bar-props)
      (pretty-css.layout/outdent-attributes      bar-props)
      (pretty-css.layout/wrapper-size-attributes bar-props)))
