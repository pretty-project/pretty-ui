
(ns pretty-elements.adornment.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.layout.api :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [adornment-id adornment-props]
  (-> {:class :pe-adornment--icon}
      (pretty-css/icon-attributes adornment-props)))

(defn adornment-label-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ adornment-props]
  (-> {:class :pe-adornment--label}
      (pretty-css/font-attributes              adornment-props)
      (pretty-css/unselectable-text-attributes adornment-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-body-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [adornment-id adornment-props]
  (-> {:class :pe-adornment--body}
      (pretty-css.layout/block-size-attributes  adornment-props)
      (pretty-css.appearance/background-attributes adornment-props)
      (pretty-css/control-attributes     adornment-props)
      (pretty-css/cursor-attributes      adornment-props)
      (pretty-css/effect-attributes      adornment-props)
      (pretty-css/focus-attributes       adornment-props)
      (pretty-css/href-attributes        adornment-props)
      (pretty-css.layout/indent-attributes      adornment-props)
      (pretty-css/mouse-event-attributes adornment-props)
      (pretty-css/progress-attributes    adornment-props)
      (pretty-css/style-attributes       adornment-props)
      (pretty-css/tab-attributes         adornment-props)))

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
      (pretty-css/class-attributes   adornment-props)
      (pretty-css.layout/outdent-attributes adornment-props)
      (pretty-css/state-attributes   adornment-props)
      (pretty-css/theme-attributes   adornment-props)))
