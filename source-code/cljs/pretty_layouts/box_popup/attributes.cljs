
(ns pretty-layouts.box-popup.attributes
    (:require [pretty-build-kit.api                        :as pretty-build-kit]
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
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [style] :as popup-props}]
  (-> {:class :pl-box-popup--structure
       :style style}
      (pretty-build-kit/border-attributes popup-props)
      (pretty-build-kit/color-attributes  popup-props)
      (pretty-build-kit/indent-attributes popup-props)))

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
      (pretty-build-kit/content-max-size-attributes popup-props)
      (pretty-build-kit/content-min-size-attributes popup-props)
      (pretty-build-kit/outdent-attributes          popup-props)))

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
