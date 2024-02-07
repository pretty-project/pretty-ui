
(ns pretty-elements.breadcrumbs.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-bullet-attributes
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ breadcrumbs-props]
  (-> {:class :pe-breadcrumbs--bullet}
      (pretty-attributes/bullet-attributes breadcrumbs-props)))

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
  ;  ...}
  [_ breadcrumbs-props]
  (-> {:class :pe-breadcrumbs--body}
      (pretty-attributes/flex-attributes            breadcrumbs-props)
      (pretty-attributes/full-block-size-attributes breadcrumbs-props)
      (pretty-attributes/indent-attributes          breadcrumbs-props)
      (pretty-attributes/style-attributes           breadcrumbs-props)))

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
      (pretty-attributes/class-attributes        breadcrumbs-props)
      (pretty-attributes/outdent-attributes      breadcrumbs-props)
      (pretty-attributes/state-attributes        breadcrumbs-props)
      (pretty-attributes/theme-attributes        breadcrumbs-props)
      (pretty-attributes/wrapper-size-attributes breadcrumbs-props)))
