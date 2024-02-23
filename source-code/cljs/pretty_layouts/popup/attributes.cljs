
(ns pretty-layouts.popup.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-body-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [popup-id popup-props]
  (-> {:class :pl-popup--body}
      (pretty-attributes/background-color-attributes popup-props)
      (pretty-attributes/body-size-attributes        popup-props)
      (pretty-attributes/border-attributes           popup-props)
      (pretty-attributes/flex-attributes             popup-props)
      (pretty-attributes/indent-attributes           popup-props)
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
      (pretty-attributes/class-attributes    popup-props)
      (pretty-attributes/outdent-attributes  popup-props)
      (pretty-attributes/overlay-attributes  popup-props)
      (pretty-attributes/position-attributes popup-props)
      (pretty-attributes/size-attributes     popup-props)
      (pretty-attributes/state-attributes    popup-props)
      (pretty-attributes/theme-attributes    popup-props)))

; + fullscreen-props ... stretch-orientation
