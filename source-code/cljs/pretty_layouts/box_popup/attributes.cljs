
(ns pretty-layouts.box-popup.attributes
    (:require [pretty-attributes.api                 :as pretty-attributes]
              [pretty-layouts.plain-popup.attributes :as plain-popup.attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-cover-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [popup-id popup-props]
  (merge (plain-popup.attributes/popup-cover-attributes popup-id popup-props)
         {:class :pl-box-popup--cover}))

(defn popup-structure-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [_ popup-props]
  (-> {:class :pl-box-popup--structure}
      (pretty-attributes/background-attributes  popup-props)
      (pretty-attributes/border-attributes popup-props)
      (pretty-attributes/indent-attributes popup-props)
      (pretty-attributes/style-attributes  popup-props)))

(defn popup-wrapper-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ; {}
  [_ popup-props]
  (-> {:class :pl-box-popup--wrapper}
      (pretty-attributes/content-size-attributes popup-props)
      (pretty-attributes/outdent-attributes      popup-props)
      (pretty-attributes/theme-attributes        popup-props)))

(defn popup-attributes
  ; @ignore
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [stretch-orientation]}]
  {:class                    :pl-box-popup
   :data-stretch-orientation stretch-orientation})

   ; class-attributes?
   ; + class-attributes, state-attributes, style-attributes, theme-attributes
