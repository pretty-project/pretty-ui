
(ns pretty-elements.breadcrumbs.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.basic.api :as pretty-css.basic]
              [pretty-css.layout.api :as pretty-css.layout]
              [pretty-css.appearance.api :as pretty-css.appearance]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-body-attributes
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-column-gap (keyword)
  ;  :data-scroll-axis (boolean)}
  [_ breadcrumbs-props]
  (-> {:class            :pe-breadcrumbs--body
       :data-column-gap  :xs
       :data-scroll-axis :x}
      (pretty-css.basic/style-attributes   breadcrumbs-props)
      (pretty-css.layout/indent-attributes breadcrumbs-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-attributes
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ breadcrumbs-props]
  (-> {:class :pe-breadcrumbs}
      (pretty-css.appearance/theme-attributes breadcrumbs-props)
      (pretty-css.basic/class-attributes      breadcrumbs-props)
      (pretty-css.basic/state-attributes      breadcrumbs-props)
      (pretty-css.layout/outdent-attributes   breadcrumbs-props)))
