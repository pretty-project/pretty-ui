
(ns pretty-elements.label.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ label-props]
  (-> {:class :pe-label--icon}
      (pretty-attributes/icon-attributes label-props)))

(defn label-content-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ label-props]
  (-> {:class :pe-label--content}
      (pretty-attributes/font-attributes label-props)
      (pretty-attributes/text-attributes label-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-body-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ label-props]
  (-> {:class :pe-label--body}
      (pretty-attributes/background-attributes      label-props)
      (pretty-attributes/border-attributes          label-props)
      (pretty-attributes/flex-attributes            label-props)
      (pretty-attributes/full-block-size-attributes label-props)
      (pretty-attributes/indent-attributes          label-props)
      (pretty-attributes/style-attributes           label-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-attributes
  ; @ignore
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ label-props]
  (-> {:class :pe-label}
      (pretty-attributes/class-attributes        label-props)
      (pretty-attributes/outdent-attributes      label-props)
      (pretty-attributes/state-attributes        label-props)
      (pretty-attributes/theme-attributes        label-props)
      (pretty-attributes/wrapper-size-attributes label-props)))
