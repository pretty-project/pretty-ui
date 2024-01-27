
(ns pretty-elements.breadcrumbs.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.layout.api :as pretty-css.layout]))

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
      (pretty-css.layout/indent-attributes breadcrumbs-props)
      (pretty-css/style-attributes  breadcrumbs-props)))

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
      (pretty-css/class-attributes   breadcrumbs-props)
      (pretty-css.layout/outdent-attributes breadcrumbs-props)
      (pretty-css/state-attributes   breadcrumbs-props)
      (pretty-css/theme-attributes   breadcrumbs-props)))
