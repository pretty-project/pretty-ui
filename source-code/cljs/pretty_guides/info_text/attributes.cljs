
(ns pretty-guides.info-text.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-content-attributes
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ text-props]
  (-> {:class :pg-info-text--content}
      (pretty-attributes/font-attributes text-props)
      (pretty-attributes/text-attributes text-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [text-id text-props]
  (-> {:class :pg-info-text--inner}
      (pretty-attributes/inner-size-attributes  text-props)
      (pretty-attributes/inner-space-attributes text-props)
      (pretty-attributes/style-attributes       text-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-attributes
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ text-props]
  (-> {:class :pg-info-text}
      (pretty-attributes/class-attributes          text-props)
      (pretty-attributes/inner-position-attributes text-props)
      (pretty-attributes/outer-position-attributes text-props)
      (pretty-attributes/outer-size-attributes     text-props)
      (pretty-attributes/outer-space-attributes    text-props)
      (pretty-attributes/state-attributes          text-props)
      (pretty-attributes/theme-attributes          text-props)))
