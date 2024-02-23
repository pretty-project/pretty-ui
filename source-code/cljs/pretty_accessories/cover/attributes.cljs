
(ns pretty-accessories.cover.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cover-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) cover-id
  ; @param (map) cover-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [cover-id cover-props]
  (-> {:class :pa-cover--icon}
      (pretty-attributes/icon-attributes cover-props)))

(defn cover-label-attributes
  ; @ignore
  ;
  ; @param (keyword) cover-id
  ; @param (map) cover-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ cover-props]
  (-> {:class :pa-cover--label}
      (pretty-attributes/font-attributes cover-props)
      (pretty-attributes/text-attributes cover-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cover-body-attributes
  ; @ignore
  ;
  ; @param (keyword) cover-id
  ; @param (map) cover-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [cover-id cover-props]
  (-> {:class :pa-cover--body}
      (pretty-attributes/body-size-attributes  cover-props)
      (pretty-attributes/flex-attributes       cover-props)
      (pretty-attributes/indent-attributes     cover-props)
      (pretty-attributes/style-attributes      cover-props)
      (pretty-attributes/visibility-attributes cover-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cover-attributes
  ; @ignore
  ;
  ; @param (keyword) cover-id
  ; @param (map) cover-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ cover-props]
  (-> {:class :pa-cover}
      (pretty-attributes/class-attributes    cover-props)
      (pretty-attributes/outdent-attributes  cover-props)
      (pretty-attributes/overlay-attributes  cover-props)
      (pretty-attributes/position-attributes cover-props)
      (pretty-attributes/size-attributes     cover-props)
      (pretty-attributes/state-attributes    cover-props)
      (pretty-attributes/theme-attributes    cover-props)))
