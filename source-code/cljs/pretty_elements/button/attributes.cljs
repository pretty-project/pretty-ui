
(ns pretty-elements.button.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

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
      (pretty-build-kit/icon-attributes button-props)))

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
      (pretty-build-kit/font-attributes button-props)
      (pretty-build-kit/text-attributes button-props)))

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
      (pretty-build-kit/badge-attributes        button-props)
      (pretty-build-kit/border-attributes       button-props)
      (pretty-build-kit/color-attributes        button-props)
      (pretty-build-kit/control-attributes      button-props)
      (pretty-build-kit/cursor-attributes       button-props)
      (pretty-build-kit/effect-attributes       button-props)
      (pretty-build-kit/element-size-attributes button-props)
      (pretty-build-kit/focus-attributes        button-props)
      (pretty-build-kit/href-attributes         button-props)
      (pretty-build-kit/indent-attributes       button-props)
      (pretty-build-kit/marker-attributes       button-props)
      (pretty-build-kit/mouse-event-attributes  button-props)
      (pretty-build-kit/progress-attributes     button-props)
      (pretty-build-kit/row-attributes          button-props)
      (pretty-build-kit/style-attributes        button-props)
      (pretty-build-kit/tab-attributes          button-props)
      (pretty-build-kit/tooltip-attributes      button-props)
      (pretty-build-kit/unselectable-attributes button-props)))

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
      (pretty-build-kit/class-attributes        button-props)
      (pretty-build-kit/outdent-attributes      button-props)
      (pretty-build-kit/state-attributes        button-props)
      (pretty-build-kit/wrapper-size-attributes button-props)))
