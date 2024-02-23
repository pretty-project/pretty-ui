
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
      (pretty-attributes/background-color-attributes  popup-props)
      (pretty-attributes/border-attributes popup-props)
      (pretty-attributes/indent-attributes popup-props)
      (pretty-attributes/style-attributes  popup-props)))

      ; a popup-ok cover-je
      ; lehetne a cover-attributes-al
      ; és akkor lehetne covered? cover-color (:light, :dark) prop-al szabályozni a covereket
      ; és amugy lehetne pluszba cover-content ami a cover közepén egy kisméretű szöveg (dark-cover nél világos betüszin & vica-versa)
      ; és akkor ez a cover mehetne az on-click-timeout gombokra, mert a visszaszámláló 'rángatja a szöveget'

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
      (pretty-attributes/outdent-attributes popup-props)
      ;(pretty-attributes/size-attributes    popup-props)
      (pretty-attributes/theme-attributes   popup-props)))

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
