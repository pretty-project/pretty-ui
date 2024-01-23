
(ns pretty-elements.adornment.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {}
  [adornment-id adornment-props]
  (-> {:class :pe-adornment--icon}
      (pretty-build-kit/icon-attributes adornment-props)))

(defn adornment-label-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {}
  [_ adornment-props]
  (-> {:class :pe-adornment--label}
      (pretty-build-kit/font-attributes adornment-props)
      (pretty-build-kit/text-attributes adornment-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-body-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {}
  [adornment-id adornment-props]
  (-> {:class :pe-adornment--body}
      (pretty-build-kit/color-attributes        adornment-props)
      (pretty-build-kit/control-attributes      adornment-props)
      (pretty-build-kit/cursor-attributes       adornment-props)
      (pretty-build-kit/effect-attributes       adornment-props)
      (pretty-build-kit/focus-attributes        adornment-props)
      (pretty-build-kit/href-attributes         adornment-props)
      (pretty-build-kit/indent-attributes       adornment-props)
      (pretty-build-kit/mouse-event-attributes  adornment-props)
      (pretty-build-kit/style-attributes        adornment-props)
      (pretty-build-kit/tab-attributes          adornment-props)
      (pretty-build-kit/unselectable-attributes adornment-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ adornment-props]
  (-> {:class :pe-adornment}
      (pretty-build-kit/class-attributes   adornment-props)
      (pretty-build-kit/outdent-attributes adornment-props)
      (pretty-build-kit/state-attributes   adornment-props)))
