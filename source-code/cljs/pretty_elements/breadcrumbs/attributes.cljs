
(ns pretty-elements.breadcrumbs.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.layout.api     :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-body-attributes
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ breadcrumbs-props]
  (-> {:class :pe-breadcrumbs--body}
      (pretty-css.basic/style-attributes         breadcrumbs-props)
      (pretty-css.layout/flex-attributes         breadcrumbs-props)
      (pretty-css.layout/element-size-attributes breadcrumbs-props)
      (pretty-css.layout/indent-attributes       breadcrumbs-props)))

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
      (pretty-css.appearance/theme-attributes    breadcrumbs-props)
      (pretty-css.basic/class-attributes         breadcrumbs-props)
      (pretty-css.basic/state-attributes         breadcrumbs-props)
      (pretty-css.layout/outdent-attributes      breadcrumbs-props)
      (pretty-css.layout/wrapper-size-attributes breadcrumbs-props)))
