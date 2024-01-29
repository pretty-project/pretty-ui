
(ns pretty-elements.adornment.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.accessories.api :as pretty-css.accessories]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.content.api    :as pretty-css.content]
              [pretty-css.control.api    :as pretty-css.control]
              [pretty-css.layout.api     :as pretty-css.layout]
              [pretty-css.live.api       :as pretty-css.live]))

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
      (pretty-css.content/icon-attributes adornment-props)))

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
      (pretty-css.content/font-attributes              adornment-props)
      (pretty-css.content/unselectable-text-attributes adornment-props)))

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
      (pretty-css.accessories/tooltip-attributes   adornment-props)
      (pretty-css.appearance/background-attributes adornment-props)
      (pretty-css.basic/style-attributes           adornment-props)
      (pretty-css.control/anchor-attributes        adornment-props)
      (pretty-css.control/focus-attributes         adornment-props)
      (pretty-css.control/mouse-event-attributes   adornment-props)
      (pretty-css.control/state-attributes         adornment-props)
      (pretty-css.control/tab-attributes           adornment-props)
      (pretty-css.content/cursor-attributes        adornment-props)
      (pretty-css.layout/block-size-attributes     adornment-props)
      (pretty-css.layout/indent-attributes         adornment-props)
      (pretty-css.live/effect-attributes           adornment-props)
      (pretty-css.live/progress-attributes         adornment-props)))

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
      (pretty-css.appearance/theme-attributes adornment-props)
      (pretty-css.layout/outdent-attributes   adornment-props)
      (pretty-css.basic/class-attributes      adornment-props)
      (pretty-css.basic/state-attributes      adornment-props)))
