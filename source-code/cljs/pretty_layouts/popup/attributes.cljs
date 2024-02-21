
(ns pretty-layouts.popup.attributes
    (:require [pretty-layouts.box-popup.attributes :as box-popup.attributes]
              [pretty-layouts.popup.state   :as popup.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-footer-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [popup-id _]
  (merge {:class :pl-popup--footer}
         (if (popup-id @popup.state/FOOTER-SHADOW-VISIBLE?)
             {:data-shadow-position :top :data-shadow-strength :s})))

(defn popup-header-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [popup-id _]
  (merge {:class :pl-popup--header}
         (if (popup-id @popup.state/HEADER-SHADOW-VISIBLE?)
             {:data-shadow-position :bottom :data-shadow-strength :s})))

(defn popup-cover-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [popup-id popup-props]
  (merge (box-popup.attributes/popup-cover-attributes popup-id popup-props)
         {:class :pl-popup--cover}))

(defn popup-structure-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [popup-id popup-props]
  (merge (box-popup.attributes/popup-structure-attributes popup-id popup-props)
         {:class :pl-popup--structure}))

(defn popup-wrapper-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [popup-id popup-props]
  (merge (box-popup.attributes/popup-wrapper-attributes popup-id popup-props)
         {:class :pl-popup--wrapper}))

(defn popup-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [popup-id popup-props]
  (merge (box-popup.attributes/popup-attributes popup-id popup-props)
         {:class :pl-popup}))

; + class-attributes, state-attributes, style-attributes, theme-attributes
