
(ns layouts.struct-popup.attributes
    (:require [layouts.box-popup.attributes :as box-popup.attributes]
              [layouts.struct-popup.state   :as struct-popup.state]
              [pretty-css.api               :as pretty-css]
              [re-frame.api                 :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-footer-attributes
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [popup-id _]
  (merge {:class :l-struct-popup--footer}
         (if (popup-id @struct-popup.state/FOOTER-SHADOW-VISIBLE?)
             {:data-shadow-position :top :data-shadow-strength :s})))

(defn popup-header-attributes
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [popup-id _]
  (merge {:class :l-struct-popup--header}
         (if (popup-id @struct-popup.state/HEADER-SHADOW-VISIBLE?)
             {:data-shadow-position :bottom :data-shadow-strength :s})))

(defn popup-cover-attributes
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [popup-id popup-props]
  (merge (box-popup.attributes/popup-cover-attributes popup-id popup-props)
         {:class :l-struct-popup--cover}))

(defn popup-structure-attributes
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [popup-id popup-props]
  (merge (box-popup.attributes/popup-structure-attributes popup-id popup-props)
         {:class :l-struct-popup--structure}))

(defn popup-wrapper-attributes
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [popup-id popup-props]
  (merge (box-popup.attributes/popup-wrapper-attributes popup-id popup-props)
         {:class :l-struct-popup--wrapper}))

(defn popup-attributes
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [popup-id popup-props]
  (merge (box-popup.attributes/popup-attributes popup-id popup-props)
         {:class :l-struct-popup}))
