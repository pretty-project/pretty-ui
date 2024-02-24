
(ns pretty-elements.breadcrumbs.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ breadcrumbs-props]
  (-> {:class :pe-breadcrumbs--inner}
      (pretty-attributes/flex-attributes        breadcrumbs-props)
      (pretty-attributes/inner-size-attributes  breadcrumbs-props)
      (pretty-attributes/inner-space-attributes breadcrumbs-props)
      (pretty-attributes/style-attributes       breadcrumbs-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-attributes
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ breadcrumbs-props]
  (-> {:class :pe-breadcrumbs}
      (pretty-attributes/class-attributes          breadcrumbs-props)
      (pretty-attributes/inner-position-attributes breadcrumbs-props)
      (pretty-attributes/outer-position-attributes breadcrumbs-props)
      (pretty-attributes/outer-size-attributes     breadcrumbs-props)
      (pretty-attributes/outer-space-attributes    breadcrumbs-props)
      (pretty-attributes/state-attributes          breadcrumbs-props)
      (pretty-attributes/theme-attributes          breadcrumbs-props)))
