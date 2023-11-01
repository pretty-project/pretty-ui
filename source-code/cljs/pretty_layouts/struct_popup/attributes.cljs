
(ns pretty-layouts.struct-popup.attributes
    (:require [pretty-layouts.box-popup.attributes :as box-popup.attributes]
              [pretty-layouts.struct-popup.state   :as struct-popup.state]))

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
  (merge {:class :pl-struct-popup--footer}
         (if (popup-id @struct-popup.state/FOOTER-SHADOW-VISIBLE?)
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
  (merge {:class :pl-struct-popup--header}
         (if (popup-id @struct-popup.state/HEADER-SHADOW-VISIBLE?)
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
         {:class :pl-struct-popup--cover}))

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
         {:class :pl-struct-popup--structure}))

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
         {:class :pl-struct-popup--wrapper}))

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
         {:class :pl-struct-popup}))
