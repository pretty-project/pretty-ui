
(ns layouts.box-popup.attributes
    (:require [layouts.plain-popup.attributes :as plain-popup.attributes]
              [pretty-css.api                 :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-cover-attributes
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [popup-id popup-props]
  (merge (plain-popup.attributes/popup-cover-attributes popup-id popup-props)
         {:class :l-box-popup--cover}))

(defn popup-structure-attributes
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [style] :as popup-props}]
  (-> {:class :l-box-popup--structure
       :style style}
      (pretty-css/border-attributes popup-props)
      (pretty-css/color-attributes  popup-props)
      (pretty-css/indent-attributes popup-props)))

(defn popup-wrapper-attributes
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [_ popup-props]
  (-> {:class :l-box-popup--wrapper}
      (pretty-css/content-max-size-attributes popup-props)
      (pretty-css/content-min-size-attributes popup-props)
      (pretty-css/outdent-attributes          popup-props)))

(defn popup-attributes
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [stretch-orientation]}]
  {:class                    :l-box-popup
   :data-stretch-orientation stretch-orientation})
