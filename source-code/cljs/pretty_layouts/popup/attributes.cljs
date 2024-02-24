
(ns pretty-layouts.popup.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-content-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ popup-props]
  (-> {:class :pl-popup--content}
      (pretty-attributes/content-size-attributes popup-props)
      (pretty-attributes/overflow-attributes     popup-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ popup-props]
  (-> {:class :pl-popup--inner}
      (pretty-attributes/background-color-attributes popup-props)
      (pretty-attributes/border-attributes           popup-props)
      (pretty-attributes/flex-attributes             popup-props)
      (pretty-attributes/inner-size-attributes       popup-props)
      (pretty-attributes/inner-space-attributes      popup-props)
      (pretty-attributes/style-attributes            popup-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ popup-props]
  (-> {:class :pl-popup}
      (pretty-attributes/class-attributes          popup-props)
      (pretty-attributes/inner-position-attributes popup-props)
      (pretty-attributes/outer-position-attributes popup-props)
      (pretty-attributes/outer-size-attributes     popup-props)
      (pretty-attributes/outer-space-attributes    popup-props)
      (pretty-attributes/overlay-attributes        popup-props)
      (pretty-attributes/state-attributes          popup-props)
      (pretty-attributes/theme-attributes          popup-props)))
