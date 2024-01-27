
(ns pretty-elements.button.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.accessories.api :as pretty-css.accessories]
              [pretty-css.layout.api :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ button-props]
  (-> {:class :pe-button--icon}
      (pretty-css/icon-attributes button-props)))

(defn button-label-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ button-props]
  (-> {:class :pe-button--label}
      (pretty-css/font-attributes              button-props)
      (pretty-css/unselectable-text-attributes button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-body-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ button-props]
  (-> {:class :pe-button--body}
      (pretty-css.accessories/badge-attributes        button-props)
      (pretty-css.appearance/background-attributes        button-props)
      (pretty-css.appearance/border-attributes       button-props)
      (pretty-css/control-attributes      button-props)
      (pretty-css/cursor-attributes       button-props)
      (pretty-css/effect-attributes       button-props)
      (pretty-css.layout/element-size-attributes button-props)
      (pretty-css/focus-attributes        button-props)
      (pretty-css/href-attributes         button-props)
      (pretty-css.layout/indent-attributes       button-props)
      (pretty-css.accessories/marker-attributes       button-props)
      (pretty-css/mouse-event-attributes  button-props)
      (pretty-css/progress-attributes     button-props)
      (pretty-css/row-attributes          button-props)
      (pretty-css/style-attributes        button-props)
      (pretty-css/tab-attributes          button-props)
      (pretty-css.accessories/tooltip-attributes      button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ button-props]
  (-> {:class :pe-button}
      (pretty-css/class-attributes        button-props)
      (pretty-css.layout/outdent-attributes      button-props)
      (pretty-css/state-attributes        button-props)
      (pretty-css/theme-attributes        button-props)
      (pretty-css.layout/wrapper-size-attributes button-props)))
